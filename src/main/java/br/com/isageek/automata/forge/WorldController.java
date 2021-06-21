package br.com.isageek.automata.forge;

import br.com.isageek.automata.AutomataBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldController {

    private World world;
    private BlockPos center;
    private final Block automata;
    private final Block terminator;
    private final Block start;
    private final Block automataPlaceholder;
    private final Block airPlaceholder;
    private final Block waterPlaceholder;
    private final Block lavaPlaceholder;
    private final Block bedrockPlaceholder;
    private final Block caveAir;
    private final Block yRotation;

    public WorldController(
        Block automata,
        Block terminator,
        Block start,
        Block automataPlaceholder,
        Block airPlaceholder,
        Block waterPlaceholder,
        Block lavaPlaceholder,
        Block bedrockPlaceholder,
        Block caveAir,
        Block yRotation
    ) {
        this.automata = automata;
        this.terminator = terminator;
        this.start = start;
        this.automataPlaceholder = automataPlaceholder;
        this.airPlaceholder = airPlaceholder;
        this.waterPlaceholder = waterPlaceholder;
        this.lavaPlaceholder = lavaPlaceholder;
        this.bedrockPlaceholder = bedrockPlaceholder;
        this.caveAir = caveAir;
        this.yRotation = yRotation;
    }

    public void set(World world, BlockPos center){
        this.world = world;
        this.center = center;
    }

    public BlockStateHolder[] surrounding(int x, int y, int z){
        BlockStateHolder[] result = new BlockStateHolder[27];

        int i = 0;
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    result[i++] =  createStateHolderFor(x+ix, y+iy, z+iz);
                }
            }
        }

        return result;
    }

    public BlockStateHolder[] surrounding(Coord c){
        return surrounding(c.x, c.y, c.z);
    }

    public void setBlock(
            int x,
            int y,
            int z,
            BlockStateHolder blockState
    ) {
        world.setBlock(
                center.offset(x, y, z),
                blockState.blockState,
                0,
                0
        );
    }

    public BlockStateHolder[] surrounding() {
        return surrounding(0, 0, 0);
    }

    private BlockStateHolder createStateHolderFor(int x, int y, int z){
        BlockState blockState = world.getBlockState(center.offset(x, y, z));
        return BlockStateHolder.block(blockState);
    }

    public TileEntity getBlockEntity(int x, int y, int z) {
        return world.getBlockEntity(center.offset(x, y, z));
    }

    public boolean isTerminator(int x, int y, int z) {
        BlockState blockState = world.getBlockState(center.offset(x, y, z));
        return blockState.getBlock() == terminator;
    }

    public boolean isTerminator(Coord c) {
        return isTerminator(c.x, c.y, c.z);
    }

    public boolean isAutomataStart(int x, int y, int z) {
        BlockState blockState = world.getBlockState(center.offset(x, y, z));
        return blockState.getBlock() == start;
    }

    public boolean isAutomataStartWithRedstoneCharge(int x, int y, int z) {
        if(!isAutomataStart(x, y, z)) return false;
        return world.hasNeighborSignal(center.offset(x, y, z));
    }

    public boolean isAutomataPlaceholder(BlockStateHolder blockStateHolder){
        return blockStateHolder.is(automataPlaceholder);
    }

    public boolean isAirPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.is(airPlaceholder);
    }

    public boolean isWaterPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.is(waterPlaceholder);
    }

    public boolean isLavaPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.is(lavaPlaceholder);
    }

    public boolean isBedrockPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.is(bedrockPlaceholder);
    }

    public boolean isYRotation(BlockStateHolder blockStateHolder) {
        return blockStateHolder.is(yRotation);
    }

    public boolean isBedrock(int x, int y, int z) {
        BlockState blockState = world.getBlockState(center.offset(x, y, z));
        return blockState.getBlock() == Blocks.BEDROCK;
    }

    public void destroyBlock(){
        world.destroyBlock(center, true);
    }

    public void setBlockAutomata(int x, int y, int z) {
        BlockState blockState = automata.defaultBlockState().setValue(AutomataBlock.loaded, true);
        world.setBlock(
            center.offset(x, y, z),
            blockState,
            0,
            0
        );
    }

    public BlockStateHolder replacePlaceholder(BlockStateHolder blockState) {
        if(isAirPlaceholder(blockState)){
            return BlockStateHolder.block(Blocks.AIR.defaultBlockState());
        }
        if(isWaterPlaceholder(blockState)) {
            return BlockStateHolder.block(Blocks.WATER.defaultBlockState());
        }
        if(isLavaPlaceholder(blockState)) {
            return BlockStateHolder.block(Blocks.LAVA.defaultBlockState());
        }
        if(isBedrockPlaceholder(blockState)) {
            return BlockStateHolder.block(Blocks.BEDROCK.defaultBlockState());
        }
        return blockState;
    }


}
