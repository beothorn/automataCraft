package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;

public class AutomataStepper {

    private static final int PATTERN_LIMIT = 128;
    private static final int MAX_SEARCH_RADIUS = 20;

    private final BlockTree blockTree;
    private boolean loaded = false;
    private final String descriptionIdForBlockRepresentingAny;
    private final BlockStateHolder air;
    private final BlockStateHolder water;
    private final BlockStateHolder lava;
    private final BlockStateHolder obsidian;
    private BlockPos nearestStart;

    public AutomataStepper(
        String descriptionIdForBlockRepresentingAny,
        BlockStateHolder air,
        BlockStateHolder water,
        BlockStateHolder lava,
        BlockStateHolder obsidian
    ){
        blockTree = new BlockTree();
        this.descriptionIdForBlockRepresentingAny = descriptionIdForBlockRepresentingAny;
        this.air = air;
        this.water = water;
        this.lava = lava;
        this.obsidian = obsidian;
    }

    public BlockStateHolder[] tick(
            WorldController worldController,
            BlockPos center,
            BlockStateHolder[] replacement,
            Runnable destroy
    ){
        // first step, search for nearest star
        if(nearestStart == null || !worldController.isAutomataStart(nearestStart)){
            nearestStart = findNearestStart(worldController, center);
            if(nearestStart == null){
                destroy.run();
            }else{
                loaded = tryToLoadPattern(worldController);
                return null;
            }
            return null;
        }

        // second step, try to load pattern

        if(!loaded){
            loaded = tryToLoadPattern(worldController);
            return null;
        }

        // third, if loaded checks the start block, if it is ok get replacement or replace

        if(!worldController.isAutomataStart(nearestStart)){
            destroy.run();
            return null;
        }
        if(!worldController.isAutomataStartWithRedstoneCharge(nearestStart)){
            loaded = false;
            blockTree.clear();
            return null;
        }

        if(replacement == null){
            return getReplacement(worldController, center);
        }else{
            replace(worldController, replacement, center);
        }
        return null;
    }

    public boolean isLoaded(){
        return loaded;
    }

    private BlockPos findNearestStart(WorldController worldController, BlockPos center){
        //go Around center searching, maximun radius of RADIUS

        for (int r = 1; r <= MAX_SEARCH_RADIUS; r++){

            int yUp = -r;
            int yDown = r;
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos yUpPos = center.offset(x, yUp, z);
                    if(worldController.isAutomataStart(yUpPos)){
                        return yUpPos;
                    }
                    BlockPos yDownPos = center.offset(x, yDown, z);
                    if(worldController.isAutomataStart(yDownPos)){
                        return yDownPos;
                    }
                }
            }

            int xLeft = -r;
            int xRight = r;

            for (int y = -r + 1; y < r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos xLeftPos = center.offset(xLeft, y, z);
                    if(worldController.isAutomataStart(xLeftPos)){
                        return xLeftPos;
                    }
                    BlockPos xRightPos = center.offset(xRight, y, z);
                    if(worldController.isAutomataStart(xRightPos)){
                        return xRightPos;
                    }
                }
            }

            int zFront = -r;
            int zBack = r;
            for (int x = -r + 1; x < r; x++) {
                for (int y = -r + 1; y < r; y++) {
                    BlockPos zFrontPos = center.offset(x, y, zFront);
                    if(worldController.isAutomataStart(zFrontPos)){
                        return zFrontPos;
                    }
                    BlockPos zBackPos = center.offset(x, y, zBack);
                    if(worldController.isAutomataStart(zBackPos)){
                        return zBackPos;
                    }
                }
            }
        }

        return null;
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

    /***
     * Search start block, caches start position and return if cache is filled
     *
     * @param worldController implementation of the world, fake on tests and controller on minecraft
     * @return if the load status was false and turned true
     */
    public boolean findStart(WorldController worldController, BlockPos center) {
        if(nearestStart == null || !worldController.isAutomataStart(nearestStart)){
            nearestStart = findNearestStart(worldController, center);
        }
        return nearestStart != null;
    }

    /***
     * Loads and returns if just loaded
     *
     * @param worldController implementation of the world, fake on tests and controller on minecraft
     * @return if the load status was false and turned true
     */
    public boolean load(WorldController worldController) {
        if(loaded) return false; // was already loaded, no transition
        loaded = tryToLoadPattern(worldController);
        return loaded;
    }

    public BlockStateHolder[] getReplacement(WorldController worldController, BlockPos center) {
        if(!loaded) return null;
        BlockStateHolder[] toBeReplaced = worldController.surrounding(center);
        return blockTree.getReplacementFor(toBeReplaced);
    }

    public void replace(
            WorldController worldController,
            BlockStateHolder[] toReplace,
            BlockPos center
    ){
        if(toReplace != null){
            int i = 0;
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockStateHolder blockStateHolder = toReplace[i++];
                        BlockPos currentPos = center.offset(x, y, z);
                        boolean isNotBedrock = !worldController.isBedrock(currentPos);
                        boolean isNotMatchAll = !blockStateHolder.descriptionId.equals(BlockTree.ANY.descriptionId);
                        boolean shouldBeReplaced = isNotBedrock && isNotMatchAll;
                        if(shouldBeReplaced){
                            if(worldController.isAutomataPlaceholder(blockStateHolder)){
                                worldController.setBlockAutomata(currentPos);
                                AutomataTileEntity blockEntity = (AutomataTileEntity) worldController.getBlockEntity(currentPos);
                                if(blockEntity != null) blockEntity.setAutomataStepper(this);
                            } else{
                                BlockStateHolder blockState = blockStateHolder;
                                if(worldController.isAirPlaceholder(blockStateHolder)    ) blockState = air;
                                if(worldController.isWaterPlaceholder(blockStateHolder)  ) blockState = water;
                                if(worldController.isLavaPlaceholder(blockStateHolder)   ) blockState = lava;
                                if(worldController.isBedrockPlaceholder(blockStateHolder)) blockState = obsidian;
                                worldController.setBlock(blockState, currentPos);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean tryToLoadPattern(WorldController worldController) {
        if(!worldController.isAutomataStartWithRedstoneCharge(nearestStart)){
            return false;
        }
        BlockPos terminator = findTerminatorFor(worldController, nearestStart);
        if(terminator != null){

            int xDirection = 0;
            int zDirection = 0;

            if(nearestStart.getX() == terminator.getX()){
                if(nearestStart.getZ() > terminator.getZ()){
                    zDirection = -1;
                }else{
                    zDirection = 1;
                }
            }else{
                if(nearestStart.getX() > terminator.getX()){
                    xDirection = -1;
                }else{
                    xDirection = 1;
                }
            }

            BlockPos cursor = nearestStart;
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
            return true;
        }
        return false;
    }

    private void replacePlaceHoldersIn(WorldController worldController, BlockStateHolder[] blockStates) {
        for (int i = 0; i < blockStates.length; i++) {
            blockStates[i] = worldController.replacePlaceholder(blockStates[i]);
        }
    }

    private void replaceBlockWithAnyMatcherBlock(BlockStateHolder[] blockStates) {
        for (int i = 0; i < blockStates.length; i++) {
            BlockStateHolder blockState = blockStates[i];
            if(blockState.descriptionId.equals(descriptionIdForBlockRepresentingAny)){
                blockStates[i] = BlockTree.ANY;
            }
        }
    }
}
