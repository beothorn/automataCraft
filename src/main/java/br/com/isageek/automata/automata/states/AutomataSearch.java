package br.com.isageek.automata.automata.states;

import br.com.isageek.automata.AutomataMod;
import br.com.isageek.automata.automata.AutomataStartState;
import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AutomataSearch implements EntityTick {

    private final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables;

    AutomataSearch(final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables){
        this.replaceables = replaceables;
    }

    AutomataSearch(
            final WorldController worldController,
            final BlockPos pos,
            final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables
    ){
        worldController.setStateAt(pos, AutomataStartState.SEARCH);
        this.replaceables = replaceables;
    }

    AutomataSearch(){
        this.replaceables = new HashMap<>();
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

        final Set<Map.Entry<BlockStateHolder, HashSet<BlockPos>>> entries = this.replaceables.entrySet();
        for (final Map.Entry<BlockStateHolder, HashSet<BlockPos>> entry : entries) {
            // Verify if blocks are still there
            final BlockStateHolder blockToReplace = entry.getKey();
            final HashSet<BlockPos> blockPositions = entry.getValue();
            final HashSet<BlockPos> newBlockPositions = new HashSet<>();
            for (final BlockPos p: blockPositions) {
                if(worldController.is(p, blockToReplace)){
                    newBlockPositions.add(p);
                }
            }

            // Seach for new blocks
            for (int x = -AutomataMod.MAX_SEARCH_RADIUS; x <= AutomataMod.MAX_SEARCH_RADIUS; x++) {
                for (int y = -AutomataMod.MAX_SEARCH_RADIUS; y <= AutomataMod.MAX_SEARCH_RADIUS; y++) {
                    for (int z = -AutomataMod.MAX_SEARCH_RADIUS; z <= AutomataMod.MAX_SEARCH_RADIUS; z++) {
                        final BlockPos currentPos = center.offset(x, y, z);
                        if(worldController.is(currentPos, blockToReplace)){
                            newBlockPositions.add(currentPos);
                        }
                    }
                }
            }

            this.replaceables.put(blockToReplace, newBlockPositions);
        }

        final PatternLoad patternLoad = new PatternLoad(worldController, center, this.replaceables);
        return patternLoad.tick(center, worldController, delta);
    }

    HashMap<BlockStateHolder, HashSet<BlockPos>> getReplaceables() {
        return this.replaceables;
    }
}
