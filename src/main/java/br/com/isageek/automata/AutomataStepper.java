package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.Coord;
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
    private Coord nearestStart;

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

    public boolean isLoaded(){
        return loaded;
    }

    private Coord findNearestStart(WorldController worldController, BlockPos center){
        //go Around center searching, maximun radius of RADIUS

        for (int r = 1; r <= MAX_SEARCH_RADIUS; r++){

            int yUp = -r;
            int yDown = r;
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    if(worldController.isAutomataStart(x, yUp, z, center)){
                        return new Coord(x, yUp, z);
                    }
                    if(worldController.isAutomataStart(x, yDown, z, center)){
                        return new Coord(x, yDown, z);
                    }
                }
            }

            int xLeft = -r;
            int xRight = r;

            for (int y = -r + 1; y < r; y++) {
                for (int z = -r; z <= r; z++) {
                    if(worldController.isAutomataStart(xLeft, y, z, center)){
                        return new Coord(xLeft, y, z);
                    }
                    if(worldController.isAutomataStart(xRight, y, z, center)){
                        return new Coord(xRight, y, z);
                    }
                }
            }

            int zFront = -r;
            int zBack = r;
            for (int x = -r + 1; x < r; x++) {
                for (int y = -r + 1; y < r; y++) {
                    if(worldController.isAutomataStart(x, y, zFront, center)){
                        return new Coord(x, y, zFront);
                    }
                    if(worldController.isAutomataStart(x, y, zBack, center)){
                        return new Coord(x, y, zBack);
                    }
                }
            }
        }

        return null;
    }

    private Coord findTerminatorFor(WorldController worldController, Coord start, BlockPos center){
        // search on each axis in intervals of 6 maximun of 128
        for(int i = 7; i <= PATTERN_LIMIT; i+=6){
            if(worldController.isTerminator(start.x + i, start.y, start.z, center)){
                return new Coord(start.x + i, start.y, start.z);
            }
            if(worldController.isTerminator(start.x - i, start.y, start.z, center)){
                return new Coord(start.x - i, start.y, start.z);
            }
            if(worldController.isTerminator(start.x, start.y, start.z + i, center)){
                return new Coord(start.x, start.y, start.z + i);
            }
            if(worldController.isTerminator(start.x, start.y, start.z - i, center)){
                return new Coord(start.x, start.y, start.z - i);
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
        if(nearestStart == null || !worldController.isAutomataStart(nearestStart.x, nearestStart.y, nearestStart.z, center)){
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
    public boolean load(WorldController worldController, BlockPos center) {
        if(loaded) return false; // was already loaded, no transition
        loaded = tryToLoadPattern(worldController, center);
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
                        boolean isNotBedrock = !worldController.isBedrock(x, y, z, center);
                        boolean isNotMatchAll = !blockStateHolder.descriptionId.equals(BlockTree.ANY.descriptionId);
                        boolean shouldBeReplaced = isNotBedrock && isNotMatchAll;
                        if(shouldBeReplaced){
                            if(worldController.isAutomataPlaceholder(blockStateHolder)){
                                worldController.setBlockAutomata(x, y, z, center);
                                AutomataTileEntity blockEntity = (AutomataTileEntity) worldController.getBlockEntity(x, y, z, center);
                                if(blockEntity != null) blockEntity.setAutomataStepper(this);
                            } else{
                                BlockStateHolder blockState = blockStateHolder;
                                if(worldController.isAirPlaceholder(blockStateHolder)    ) blockState = air;
                                if(worldController.isWaterPlaceholder(blockStateHolder)  ) blockState = water;
                                if(worldController.isLavaPlaceholder(blockStateHolder)   ) blockState = lava;
                                if(worldController.isBedrockPlaceholder(blockStateHolder)) blockState = obsidian;
                                worldController.setBlock(x, y, z, blockState, center);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean tryToLoadPattern(WorldController worldController, BlockPos center) {
        if(!worldController.isAutomataStartWithRedstoneCharge(nearestStart.x, nearestStart.y, nearestStart.z, center)){
            return false;
        }
        Coord terminator = findTerminatorFor(worldController, nearestStart, center);
        if(terminator != null){
            Coord cursor = Coord.coord(nearestStart);
            int count = 0;
            cursor.moveTowards(terminator, 1);
            while(!worldController.isTerminator(cursor, center) && count <= PATTERN_LIMIT){
                cursor.moveTowards(terminator, 1);

                BlockStateHolder[] result = worldController.surrounding(cursor, center);
                replaceBlockWithAnyMatcherBlock(result);
                replacePlaceHoldersIn(worldController, result);
                cursor.moveTowards(terminator, 3);
                final BlockStateHolder[] match = worldController.surrounding(cursor, center);
                final BlockStateHolder centerBlock = match[BlockTree.AUTOMATA_BLOCK_POSITION];

                replaceBlockWithAnyMatcherBlock(match);
                replacePlaceHoldersIn(worldController, match);
                blockTree.addPattern(match, result);

                if(worldController.isYRotation(centerBlock)){
                    blockTree.addPatternRotateY(match, result);
                }else{
                    blockTree.addPattern(match, result);
                }

                cursor.moveTowards(terminator, 2);
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
