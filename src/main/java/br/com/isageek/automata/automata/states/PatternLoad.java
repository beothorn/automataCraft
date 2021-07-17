package br.com.isageek.automata.automata.states;

import br.com.isageek.automata.AutomataMod;
import br.com.isageek.automata.BlockTree;
import br.com.isageek.automata.automata.AutomataStartState;
import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.EntityTick;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

public class PatternLoad implements EntityTick {

    private HashSet<BlockPos> automataPositions;
    private long timeWithoutTerminator = 0;

    public PatternLoad() {
        this.automataPositions = new HashSet<>();
    }

    public PatternLoad(WorldController worldController, BlockPos pos, HashSet<BlockPos> automataPositions) {
        worldController.setStateAt(pos, AutomataStartState.LOAD);
        this.automataPositions = automataPositions;
    }

    @Override
    public EntityTick tick(BlockPos center, WorldController worldController, long delta) {
        if(!worldController.hasNeighborSignal(center)) return new AutomataSearch(worldController, center, automataPositions);
        BlockPos terminatorPos = findTerminatorFor(worldController, center);
        if(terminatorPos == null){
            timeWithoutTerminator += delta;
            if(timeWithoutTerminator > AutomataMod.SEARCH_AGAIN_TIMEOUT)
                return new AutomataSearch(worldController, center, automataPositions);
            else
                return this;
        }
        BlockTree replacementPattern = loadPattern(worldController, terminatorPos, center);
        ExecutePattern executePattern = new ExecutePattern(worldController, center, automataPositions, replacementPattern);
        return executePattern.tick(center, worldController, delta);
    }

    private BlockPos findTerminatorFor(WorldController worldController, BlockPos start){
        // search on each axis in intervals of 6 maximun of 128
        BlockPos xPlus = start.offset(7, 0, 0);
        if(worldController.isTerminator(xPlus)){
            return xPlus;
        }
        BlockPos xMinus = start.offset(-7, 0, 0);
        if(worldController.isTerminator(xMinus)){
            return xMinus;
        }
        BlockPos zPlus = start.offset(0, 0, 7);
        if(worldController.isTerminator(zPlus)){
            return zPlus;
        }
        BlockPos zMinus = start.offset(0, 0, -7);
        if(worldController.isTerminator(zMinus)){
            return zMinus;
        }

        return null;
    }

    private BlockTree loadPattern(
            WorldController worldController,
            BlockPos terminator,
            BlockPos center
    ) {
        BlockTree blockTree = new BlockTree();

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

            BlockStateHolder[] result = worldController.surrounding(cursor);
            replaceBlockWithAnyMatcherBlock(worldController, result);
            replacePlaceHoldersIn(worldController, result);
            // move 3 towards terminator
            cursor = cursor.offset(xDirection * 3, 0, zDirection * 3);
            final BlockStateHolder[] match = worldController.surrounding(cursor);
            final BlockStateHolder centerBlock = match[BlockTree.AUTOMATA_BLOCK_POSITION];

            replaceBlockWithAnyMatcherBlock(worldController, match);
            replacePlaceHoldersIn(worldController, match);
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

    private void replaceBlockWithAnyMatcherBlock(WorldController worldController, BlockStateHolder[] blockStates) {
        for (int i = 0; i < blockStates.length; i++) {
            BlockStateHolder blockState = blockStates[i];
            if(worldController.isAny(blockState)){
                blockStates[i] = BlockTree.ANY;
            }
        }
    }

    private void replacePlaceHoldersIn(WorldController worldController, BlockStateHolder[] blockStates) {
        for (int i = 0; i < blockStates.length; i++) {
            blockStates[i] = worldController.replacePlaceholder(blockStates[i]);
        }
    }


}