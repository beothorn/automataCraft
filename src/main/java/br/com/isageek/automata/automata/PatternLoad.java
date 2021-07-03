package br.com.isageek.automata.automata;

import br.com.isageek.automata.BlockTree;
import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

public class PatternLoad implements EntityTick{

    private static final int PATTERN_LIMIT = 128;

    private String descriptionIdForBlockRepresentingAny = "TODO";
    private HashSet<BlockPos> automataPositions;

    public PatternLoad(HashSet<BlockPos> automataPositions) {
        this.automataPositions = automataPositions;
    }

    @Override
    public EntityTick tick(BlockPos center, WorldController worldController) {
        if(!worldController.hasNeighborSignal(center)) return new AutomataSearch();
        BlockPos terminatorPos = findTerminatorFor(worldController, center);
        if(terminatorPos == null) return this;
        BlockTree replacementPattern = loadPattern(worldController, terminatorPos, center);
        return new ExecutePattern(automataPositions, replacementPattern);
    }

    private BlockPos findTerminatorFor(WorldController worldController, BlockPos start){
        // search on each axis in intervals of 6 maximun of 128
        for(int i = 7; i <= PATTERN_LIMIT; i+=6){
            BlockPos xPlus = start.offset(i, 0, 0);
            if(worldController.isTerminator(xPlus)){
                return xPlus;
            }
            BlockPos xMinus = start.offset(-i, 0, 0);
            if(worldController.isTerminator(xMinus)){
                return xMinus;
            }
            BlockPos zPlus = start.offset(0, 0, i);
            if(worldController.isTerminator(zPlus)){
                return zPlus;
            }
            BlockPos zMinus = start.offset(0, 0, -i);
            if(worldController.isTerminator(zMinus)){
                return zMinus;
            }
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
        int count = 0;
        cursor = cursor.offset(xDirection, 0, zDirection);

        while(!worldController.isTerminator(cursor) && count <= PATTERN_LIMIT){
            cursor = cursor.offset(xDirection, 0, zDirection);

            BlockStateHolder[] result = worldController.surrounding(cursor);
            replaceBlockWithAnyMatcherBlock(result);
            replacePlaceHoldersIn(worldController, result);
            // move 3 towards terminator
            cursor = cursor.offset(xDirection * 3, 0, zDirection * 3);
            final BlockStateHolder[] match = worldController.surrounding(cursor);
            final BlockStateHolder centerBlock = match[BlockTree.AUTOMATA_BLOCK_POSITION];

            replaceBlockWithAnyMatcherBlock(match);
            replacePlaceHoldersIn(worldController, match);
            blockTree.addPattern(match, result);

            if(worldController.isYRotation(centerBlock)){
                blockTree.addPatternRotateY(match, result);
            }else{
                blockTree.addPattern(match, result);
            }
            // move 2 towards terminator
            cursor = cursor.offset(xDirection * 2, 0, zDirection * 2);
            count++;
        }
        return blockTree;
    }

    private void replaceBlockWithAnyMatcherBlock(BlockStateHolder[] blockStates) {
        for (int i = 0; i < blockStates.length; i++) {
            BlockStateHolder blockState = blockStates[i];
            if(blockState.descriptionId.equals(descriptionIdForBlockRepresentingAny)){
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