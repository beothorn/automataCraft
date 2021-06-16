package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockOperations;
import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.Coord;
import br.com.isageek.automata.forge.WorldController;

public class AutomataStepper {

    private static final int PATTERN_LIMIT = 128;
    private static final int MAX_SEARCH_RADIUS = 20;

    private BlockTree blockTree;
    private boolean loaded = false;
    private String descriptionIdForBlockRepresentingAny;
    private BlockStateHolder air;
    private BlockStateHolder water;
    private BlockStateHolder lava;
    private BlockStateHolder obsidian;

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

    private Coord findNearestStart(WorldController worldController){
        //go Around center searching, maximun radius of RADIUS

        for (int r = 1; r <= MAX_SEARCH_RADIUS; r++){

            int yUp = -r;
            int yDown = r;
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    if(worldController.isAutomataStart(x, yUp, z)){
                        return new Coord(x, yUp, z);
                    }
                    if(worldController.isAutomataStart(x, yDown, z)){
                        return new Coord(x, yDown, z);
                    }
                }
            }

            int xLeft = -r;
            int xRight = r;

            for (int y = -r + 1; y < r; y++) {
                for (int z = -r; z <= r; z++) {
                    if(worldController.isAutomataStart(xLeft, y, z)){
                        return new Coord(xLeft, y, z);
                    }
                    if(worldController.isAutomataStart(xRight, y, z)){
                        return new Coord(xRight, y, z);
                    }
                }
            }

            int zFront = -r;
            int zBack = r;
            for (int x = -r + 1; x < r; x++) {
                for (int y = -r + 1; y < r; y++) {
                    if(worldController.isAutomataStart(x, y, zFront)){
                        return new Coord(x, y, zFront);
                    }
                    if(worldController.isAutomataStart(x, y, zBack)){
                        return new Coord(x, y, zBack);
                    }
                }
            }
        }

        return null;
    }

    private Coord findTerminatorFor(WorldController worldController, Coord start){
        // search on each axis in intervals of 6 maximun of 128
        for(int i = 7; i <= PATTERN_LIMIT; i+=6){
            if(worldController.isTerminator(start.x + i, start.y, start.z)){
                return new Coord(start.x + i, start.y, start.z);
            }
            if(worldController.isTerminator(start.x - i, start.y, start.z)){
                return new Coord(start.x - i, start.y, start.z);
            }
            if(worldController.isTerminator(start.x, start.y, start.z + i)){
                return new Coord(start.x, start.y, start.z + i);
            }
            if(worldController.isTerminator(start.x, start.y, start.z - i)){
                return new Coord(start.x, start.y, start.z - i);
            }
        }

        return null;
    }

    public void automataTick(WorldController worldController) {
        if(loaded){
            executePattern(worldController);
        }else{
            tryToLoadPattern(worldController);
        }
    }

    private void executePattern(WorldController worldController) {
        BlockStateHolder[] toBeReplaced = worldController.surrounding();
        BlockStateHolder[] toReplace = blockTree.getReplacementFor(toBeReplaced);
        if(toReplace != null){
            int i = 0;
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockStateHolder blockStateHolder = toReplace[i++];
                        boolean isNotBedrock = !worldController.isBedrock(x, y, z);
                        boolean isNotMatchAll = blockStateHolder.descriptionId != BlockTree.ANY.descriptionId;
                        boolean shouldBeReplaced = isNotBedrock && isNotMatchAll;
                        if(shouldBeReplaced){
                            if(worldController.isAutomataPlaceholder(blockStateHolder)){
                                worldController.setBlockAutomata(x, y, z);
                                AutomataTileEntity blockEntity = (AutomataTileEntity) worldController.getBlockEntity(x, y, z);
                                if(blockEntity != null) blockEntity.setAutomataStepper(this);
                            } else{
                                BlockStateHolder blockState = blockStateHolder;
                                if(worldController.isAirPlaceholder(blockStateHolder)    ) blockState = air;
                                if(worldController.isWaterPlaceholder(blockStateHolder)  ) blockState = water;
                                if(worldController.isLavaPlaceholder(blockStateHolder)   ) blockState = lava;
                                if(worldController.isBedrockPlaceholder(blockStateHolder)) blockState = obsidian;
                                worldController.setBlock(x, y, z, blockState);
                            }
                        }
                    }
                }
            }
        }
    }

    private void tryToLoadPattern(WorldController worldController) {
        Coord nearestStart = findNearestStart(worldController);
        if(nearestStart != null){
            Coord terminator = findTerminatorFor(worldController, nearestStart);
            if(terminator != null){
                loaded = true;
                Coord cursor = Coord.coord(nearestStart);
                int count = 0;
                cursor.moveTowards(terminator, 1);
                while(!worldController.isTerminator(cursor) && count <= PATTERN_LIMIT){
                    cursor.moveTowards(terminator, 1);

                    BlockStateHolder[] result = worldController.surrounding(cursor);
                    replaceBlockWithAnyMatcherBlock(result);
                    replacePlaceHoldersIn(worldController, result);
                    cursor.moveTowards(terminator, 3);
                    BlockStateHolder[] match = worldController.surrounding(cursor);
                    BlockStateHolder center = match[BlockTree.AUTOMATA_BLOCK_POSITION];

                    replaceBlockWithAnyMatcherBlock(match);
                    replacePlaceHoldersIn(worldController, match);
                    blockTree.addPattern(match, result);

                    if(worldController.isYRotation(center)){
                        BlockStateHolder[] match90 = BlockOperations.rotateY(match);
                        BlockStateHolder[] result90 = BlockOperations.rotateY(result);
                        blockTree.addPattern(match90, result90);
                        BlockStateHolder[] match180 = BlockOperations.rotateY(match90);
                        BlockStateHolder[] result180 = BlockOperations.rotateY(result90);
                        blockTree.addPattern(match180, result180);
                        BlockStateHolder[] match270 = BlockOperations.rotateY(match180);
                        BlockStateHolder[] result270 = BlockOperations.rotateY(result180);
                        blockTree.addPattern(match270, result270);
                    }

                    cursor.moveTowards(terminator, 2);
                    count++;
                };
            }
        }
    }

    private void replacePlaceHoldersIn(WorldController worldController, BlockStateHolder[] blockStates) {
        for (int i = 0; i < blockStates.length; i++) {
            blockStates[i] = worldController.replacePlaceholder(blockStates[i]);
        }
    }

    private void replaceBlockWithAnyMatcherBlock(BlockStateHolder[] blockStates) {
        for (int i = 0; i < blockStates.length; i++) {
            BlockStateHolder blockState = blockStates[i];
            if(blockState.descriptionId == descriptionIdForBlockRepresentingAny){
                blockStates[i] = BlockTree.ANY;
            }
        }
    }
}
