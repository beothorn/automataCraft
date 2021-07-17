package br.com.isageek.automata.forge;

import br.com.isageek.automata.automata.AutomataStartBlock;
import br.com.isageek.automata.automata.AutomataStartState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldController {

    private World world;
    private final Block[] any;
    private final Block automata;
    private final Block terminator;
    private final Block start;
    private final Block automataPlaceholder;
    private final Block airPlaceholder;
    private final Block waterPlaceholder;
    private final Block lavaPlaceholder;
    private final Block tntPlaceholder;
    private final Block bedrockPlaceholder;
    private final Block yRotation;

    public WorldController(
        Block[] any,
        Block automata,
        Block terminator,
        Block start,
        Block automataPlaceholder,
        Block airPlaceholder,
        Block waterPlaceholder,
        Block lavaPlaceholder,
        Block tntPlaceholder,
        Block bedrockPlaceholder,
        Block yRotation
    ) {
        this.any = any;
        this.automata = automata;
        this.terminator = terminator;
        this.start = start;
        this.automataPlaceholder = automataPlaceholder;
        this.airPlaceholder = airPlaceholder;
        this.waterPlaceholder = waterPlaceholder;
        this.lavaPlaceholder = lavaPlaceholder;
        this.tntPlaceholder = tntPlaceholder;
        this.bedrockPlaceholder = bedrockPlaceholder;
        this.yRotation = yRotation;
    }

    public void set(World world){
        this.world = world;
    }

    public BlockStateHolder[] surrounding(BlockPos surrounded){
        BlockStateHolder[] result = new BlockStateHolder[27];
        int i = 0;
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    result[i++] =  createStateHolderFor(surrounded.offset(ix, iy, iz));
                }
            }
        }

        return result;
    }

    public void setBlock(
            BlockStateHolder blockState,
            BlockPos p
    ) {
        world.setBlock(
                p,
                blockState.blockState,
                0,
                0
        );
    }

    private BlockStateHolder createStateHolderFor(BlockPos pos){
        BlockState blockState = world.getBlockState(pos);
        return BlockStateHolder.block(blockState);
    }

    public boolean isTerminator(BlockPos p) { return is(p, terminator);}
    public boolean isAutomata(BlockPos p) { return is(p, automata);}
    public boolean isBedrock(BlockPos p) { return is(p, Blocks.BEDROCK);}

    private boolean is(BlockPos p, Block blockType) {
        return world.getBlockState(p).getBlock() == blockType;
    }

    public boolean isAutomataPlaceholder(BlockStateHolder blockStateHolder){
        return blockStateHolder.is(automataPlaceholder);
    }

    public boolean isAny(BlockStateHolder blockStateHolder) {
        for (Block block : any) {
            if (blockStateHolder.is(block)) return true;
        }
        return false;

    }
    public boolean isAirPlaceholder(BlockStateHolder blockStateHolder) { return blockStateHolder.is(airPlaceholder); }
    public boolean isWaterPlaceholder(BlockStateHolder blockStateHolder) { return blockStateHolder.is(waterPlaceholder);}
    public boolean isLavaPlaceholder(BlockStateHolder blockStateHolder) { return blockStateHolder.is(lavaPlaceholder); }
    public boolean isTntPlaceholder(BlockStateHolder blockStateHolder) { return blockStateHolder.is(tntPlaceholder); }
    public boolean isBedrockPlaceholder(BlockStateHolder blockStateHolder) { return blockStateHolder.is(bedrockPlaceholder); }
    public boolean isYRotation(BlockStateHolder blockStateHolder) { return blockStateHolder.is(yRotation); }

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
        if(isTntPlaceholder(blockState)) {
            return BlockStateHolder.block(Blocks.TNT.defaultBlockState());
        }
        if(isBedrockPlaceholder(blockState)) {
            return BlockStateHolder.block(Blocks.BEDROCK.defaultBlockState());
        }
        if(isAutomataPlaceholder(blockState)){
            return BlockStateHolder.block(automata.defaultBlockState());
        }
        return blockState;
    }

    public boolean hasNeighborSignal(BlockPos p) {
        return world.hasNeighborSignal(p);
    }

    public BlockStateHolder getAutomata() { return BlockStateHolder.block(automata.defaultBlockState()); }

    public void setStateAt(BlockPos pos, AutomataStartState state){
        BlockState newBlockState = start.defaultBlockState().setValue(AutomataStartBlock.state, state);
        world.setBlock(pos, newBlockState,0 ,0);
    }
}
