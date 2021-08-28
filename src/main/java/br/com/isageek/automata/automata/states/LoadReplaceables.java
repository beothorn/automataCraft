package br.com.isageek.automata.automata.states;

import br.com.isageek.automata.automata.AutomataStartState;
import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.HashSet;

public class LoadReplaceables implements EntityTick {

    private HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables;

    public LoadReplaceables() {
        this.replaceables = new HashMap<>();
    }

    LoadReplaceables(
            final WorldController worldController,
            final BlockPos pos,
            final HashMap<BlockStateHolder,
            HashSet<BlockPos>> replaceables
    ) {
        worldController.setStateAt(pos, AutomataStartState.SEARCH);
        this.replaceables = replaceables;
    }

    @Override
    public EntityTick tick(
        final BlockPos center,
        final WorldController worldController,
        final long delta
    ) {
        if(!worldController.hasNeighborSignal(center)) {
            return this;
        }
        final BlockPos terminatorPos = worldController.findTerminatorFor(center);
        if(terminatorPos == null){
            return this;
        }
        this.loadReplaceables(worldController, terminatorPos, center);
        final AutomataSearch automataSearch = new AutomataSearch(worldController, center, this.replaceables);
        return automataSearch.tick(center, worldController, delta);
    }

    private void loadReplaceables(
        final WorldController worldController,
        final BlockPos terminator,
        final BlockPos center
    ){
        int xDirection = 0;
        int zDirection = 0;

        if(center.getX() == terminator.getX()){
            if(center.getZ() > terminator.getZ()){
                zDirection = -1;
            }else{
                zDirection = 1;
            }
        }else{
            if(center.getX() > terminator.getX()){
                xDirection = -1;
            }else{
                xDirection = 1;
            }
        }

        BlockPos cursor = center;

        final HashMap<BlockStateHolder, HashSet<BlockPos>> newReplaceables = new HashMap<>();

        while(worldController.isTerminator(cursor.offset(xDirection * 7, 0, zDirection * 7))){
            cursor = cursor.offset(xDirection * 2, 0, zDirection * 2);
            BlockStateHolder blockToReplace = worldController.getBlockStateHolderAt(cursor);
            if(worldController.isAny(blockToReplace)){
                blockToReplace = worldController.getAutomata();
            }

            blockToReplace = worldController.replacePlaceholder(blockToReplace);

            if(!this.replaceables.containsKey(blockToReplace)){
                newReplaceables.put(blockToReplace, new HashSet<>());
            }else{
                newReplaceables.put(blockToReplace, this.replaceables.get(blockToReplace));
            }

            // move where start block would be if next sequence was first
            cursor = cursor.offset(xDirection * 5, 0, zDirection * 5);
        }
        this.replaceables = newReplaceables;
    }

    HashMap<BlockStateHolder, HashSet<BlockPos>> getReplaceables() {
        return this.replaceables;
    }
}
