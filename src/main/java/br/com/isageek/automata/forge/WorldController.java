package br.com.isageek.automata.forge;

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
    private final Block automataOff;
    private final Block start;
    private final Block automataPlaceholder;
    private final Block airPlaceholder;
    private final Block waterPlaceholder;
    private final Block lavaPlaceholder;
    private final Block bedrockPlaceholder;
    private final Block caveAir;

    public WorldController(
        Block automata,
        Block terminator,
        Block automataOff,
        Block start,
        Block automataPlaceholder,
        Block airPlaceholder,
        Block waterPlaceholder,
        Block lavaPlaceholder,
        Block bedrockPlaceholder,
        Block caveAir
    ) {
        this.automata = automata;
        this.terminator = terminator;
        this.automataOff = automataOff;
        this.start = start;
        this.automataPlaceholder = automataPlaceholder;
        this.airPlaceholder = airPlaceholder;
        this.waterPlaceholder = waterPlaceholder;
        this.lavaPlaceholder = lavaPlaceholder;
        this.bedrockPlaceholder = bedrockPlaceholder;
        this.caveAir = caveAir;
    }

    public void set(World world, BlockPos center){
        this.world = world;
        this.center = center;
    }

    public void remove(int x, int y, int z) {
        boolean p_217377_2_ = false;
        world.removeBlock(center.offset(x, y, z), p_217377_2_);
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
        return BlockStateHolder.b(blockState);
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

    public boolean isAutomataOff(int x, int y, int z) {
        BlockState blockState = world.getBlockState(center.offset(x, y, z));
        return blockState.getBlock() == automataOff;
    }

    public boolean isAutomataStart(int x, int y, int z) {
        BlockState blockState = world.getBlockState(center.offset(x, y, z));
        return blockState.getBlock() == start;
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

    public boolean isBedrock(int x, int y, int z) {
        BlockState blockState = world.getBlockState(center.offset(x, y, z));
        return blockState.getBlock() == Blocks.BEDROCK;
    }

    public boolean isAutomata(int x, int y, int z) {
        BlockState blockState = world.getBlockState(center.offset(x, y, z));
        return blockState.getBlock() == automata;
    }

    public void setBlockAutomata(int x, int y, int z) {
        world.setBlock(
            center.offset(x, y, z),
            automata.defaultBlockState(),
            0,
            0
        );
    }

    public BlockStateHolder replacePlaceholder(BlockStateHolder blockState) {
        if(isAirPlaceholder(blockState)){
            return BlockStateHolder.b(Blocks.AIR.defaultBlockState());
        }
        if(isWaterPlaceholder(blockState)) {
            return BlockStateHolder.b(Blocks.WATER.defaultBlockState());
        }
        if(isLavaPlaceholder(blockState)) {
            return BlockStateHolder.b(Blocks.LAVA.defaultBlockState());
        }
        if(isBedrockPlaceholder(blockState)) {
            return BlockStateHolder.b(Blocks.BEDROCK.defaultBlockState());
        }
        return blockState;
    }
}
