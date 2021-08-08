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
            final Block[] any,
            final Block automata,
            final Block terminator,
            final Block start,
            final Block automataPlaceholder,
            final Block airPlaceholder,
            final Block waterPlaceholder,
            final Block lavaPlaceholder,
            final Block tntPlaceholder,
            final Block bedrockPlaceholder,
            final Block yRotation
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

    public BlockPos findTerminatorFor(final BlockPos start){
        final BlockPos xPlus = start.offset(7, 0, 0);
        if(this.isTerminator(xPlus)){
            BlockPos cursor = xPlus;
            while(this.isTerminator(cursor.offset(7, 0, 0))){
                cursor = cursor.offset(7, 0, 0);
            }
            return cursor;
        }
        final BlockPos xMinus = start.offset(-7, 0, 0);
        if(this.isTerminator(xMinus)){
            BlockPos cursor = xMinus;
            while(this.isTerminator(cursor.offset(-7, 0, 0))){
                cursor = cursor.offset(-7, 0, 0);
            }
            return cursor;
        }
        final BlockPos zPlus = start.offset(0, 0, 7);
        if(this.isTerminator(zPlus)){
            BlockPos cursor = zPlus;
            while(this.isTerminator(cursor.offset(0, 0, 7))){
                cursor = cursor.offset(0, 0, 7);
            }
            return cursor;
        }
        final BlockPos zMinus = start.offset(0, 0, -7);
        if(this.isTerminator(zMinus)){
            BlockPos cursor = zMinus;
            while(this.isTerminator(cursor.offset(0, 0, -7))){
                cursor = cursor.offset(0, 0, -7);
            }
            return cursor;
        }

        return null;
    }

    void set(final World world){
        this.world = world;
    }

    public BlockStateHolder[] surrounding(final BlockPos surrounded){
        final BlockStateHolder[] result = new BlockStateHolder[27];
        int i = 0;
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    result[i++] = this.createStateHolderFor(surrounded.offset(ix, iy, iz));
                }
            }
        }

        return result;
    }

    public void setBlock(
            final BlockStateHolder blockState,
            final BlockPos p
    ) {
        this.world.setBlock(
                p,
                blockState.blockState,
                0,
                0
        );
    }

    private BlockStateHolder createStateHolderFor(final BlockPos pos){
        final BlockState blockState = this.world.getBlockState(pos);
        return BlockStateHolder.block(blockState);
    }

    public boolean isTerminator(final BlockPos p) { return this.is(p, this.terminator);}
    public boolean isAutomata(final BlockPos p) { return this.is(p, this.automata);}
    public boolean isBedrock(final BlockPos p) { return this.is(p, Blocks.BEDROCK);}

    private boolean is(final BlockPos p, final Block blockType) {
        return this.world.getBlockState(p).getBlock() == blockType;
    }

    public boolean is(final BlockPos p, final BlockStateHolder blockType) {
        return this.world.getBlockState(p).getBlock() == blockType.blockState.getBlock();
    }

    public boolean isAutomataPlaceholder(final BlockStateHolder blockStateHolder){
        return blockStateHolder.is(this.automataPlaceholder);
    }

    public boolean isAny(final BlockStateHolder blockStateHolder) {
        for (final Block block : this.any) {
            if (blockStateHolder.is(block)) {
                return true;
            }
        }
        return false;

    }
    public boolean isAirPlaceholder(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.airPlaceholder); }
    public boolean isWaterPlaceholder(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.waterPlaceholder);}
    public boolean isLavaPlaceholder(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.lavaPlaceholder); }
    public boolean isTntPlaceholder(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.tntPlaceholder); }
    public boolean isBedrockPlaceholder(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.bedrockPlaceholder); }
    public boolean isYRotation(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.yRotation); }

    public BlockStateHolder replacePlaceholder(final BlockStateHolder blockState) {
        if(this.isAirPlaceholder(blockState)){
            return BlockStateHolder.block(Blocks.AIR.defaultBlockState());
        }
        if(this.isWaterPlaceholder(blockState)) {
            return BlockStateHolder.block(Blocks.WATER.defaultBlockState());
        }
        if(this.isLavaPlaceholder(blockState)) {
            return BlockStateHolder.block(Blocks.LAVA.defaultBlockState());
        }
        if(this.isTntPlaceholder(blockState)) {
            return BlockStateHolder.block(Blocks.TNT.defaultBlockState());
        }
        if(this.isBedrockPlaceholder(blockState)) {
            return BlockStateHolder.block(Blocks.BEDROCK.defaultBlockState());
        }
        if(this.isAutomataPlaceholder(blockState)){
            return BlockStateHolder.block(this.automata.defaultBlockState());
        }
        return blockState;
    }

    public boolean hasNeighborSignal(final BlockPos p) {
        return this.world.hasNeighborSignal(p);
    }

    public BlockStateHolder getAutomata() { return BlockStateHolder.block(this.automata.defaultBlockState()); }

    public void setStateAt(final BlockPos pos, final AutomataStartState state){
        final BlockState newBlockState = this.start.defaultBlockState().setValue(AutomataStartBlock.state, state);
        this.world.setBlock(pos, newBlockState,0 ,0);
    }

    public BlockStateHolder getBlockStateHolderAt(final BlockPos pos) {
        return BlockStateHolder.block(this.world.getBlockState(pos));
    }
}
