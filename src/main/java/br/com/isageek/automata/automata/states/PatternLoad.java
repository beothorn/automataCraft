package br.com.isageek.automata.automata.states;

import br.com.isageek.automata.AutomataMod;
import br.com.isageek.automata.BlockTree;
import br.com.isageek.automata.automata.AutomataStartState;
import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.HashSet;

public class PatternLoad implements EntityTick {

    private final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables;
    private long timeWithoutTerminator = 0;
    private final int AUTOMATA_BLOCK_POSITION = 13;

    PatternLoad() {
        this.replaceables = new HashMap<>();
    }

    PatternLoad(final WorldController worldController, final BlockPos pos, final HashMap<BlockStateHolder, HashSet<BlockPos>> replaceables) {
        worldController.setStateAt(pos, AutomataStartState.LOAD);
        this.replaceables = replaceables;
    }

    @Override
    public EntityTick tick(
            final BlockPos center,
            final WorldController worldController,
            final long delta
    ) {
        if(!worldController.hasNeighborSignal(center)) {
            return new AutomataSearch(worldController, center, this.replaceables);
        }
        final BlockPos terminatorPos = worldController.findTerminatorFor(center);
        if(terminatorPos == null){
            this.timeWithoutTerminator += delta;
            if(this.timeWithoutTerminator > AutomataMod.SEARCH_AGAIN_TIMEOUT) {
                return new AutomataSearch(worldController, center, this.replaceables);
            } else {
                return this;
            }
        }
        final BlockTree replacementPattern = this.loadPattern(worldController, terminatorPos, center);
        final ExecutePattern executePattern = new ExecutePattern(
            worldController,
            center,
            terminatorPos,
            this.replaceables,
            replacementPattern
        );
        return executePattern.tick(center, worldController, delta);
    }

    private BlockTree loadPattern(
            final WorldController worldController,
            final BlockPos terminator,
            final BlockPos center
    ) {
        final BlockTree blockTree = new BlockTree();

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

        while(worldController.isTerminator(cursor.offset(xDirection * 7, 0, zDirection * 7))){
            // move 2 to next center
            cursor = cursor.offset(xDirection * 2, 0, zDirection * 2);

            final BlockStateHolder[] match = worldController.surrounding(cursor);
            final BlockStateHolder centerBlock = match[this.AUTOMATA_BLOCK_POSITION];

            // move 3 towards terminator
            cursor = cursor.offset(xDirection * 3, 0, zDirection * 3);
            final BlockStateHolder[] result = worldController.surrounding(cursor);
            PatternLoad.replaceBlockWithAnyMatcherBlock(worldController, result);
            PatternLoad.replacePlaceHoldersIn(worldController, result);

            PatternLoad.replaceBlockWithAnyMatcherBlock(worldController, match);
            PatternLoad.replacePlaceHoldersIn(worldController, match);
            blockTree.addPattern(match, result);

            if(worldController.isYRotation(centerBlock)){
                blockTree.addPatternRotateY(match, result);
            }else{
                blockTree.addPattern(match, result);
            }
            // move 2 towards terminator
            cursor = cursor.offset(xDirection * 2, 0, zDirection * 2);
        }
        return blockTree;
    }

    private static void replaceBlockWithAnyMatcherBlock(final WorldController worldController, final BlockStateHolder[] blockStates) {
        for (int i = 0; i < blockStates.length; i++) {
            final BlockStateHolder blockState = blockStates[i];
            if(worldController.isAny(blockState)){
                blockStates[i] = BlockTree.ANY;
            }
        }
    }

    private static void replacePlaceHoldersIn(final WorldController worldController, final BlockStateHolder[] blockStates) {
        for (int i = 0; i < blockStates.length; i++) {
            blockStates[i] = worldController.replacePlaceholder(blockStates[i]);
        }
    }


}
