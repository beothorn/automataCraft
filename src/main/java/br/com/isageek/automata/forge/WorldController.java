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
    private final Block airPlaceholder;
    private final Block gravelPlaceholder;
    private final Block redSandPlaceholder;
    private final Block sandPlaceholder;
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
            final Block airPlaceholder,
            final Block gravelPlaceholder,
            final Block redSandPlaceholder,
            final Block sandPlaceholder,
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
        this.airPlaceholder = airPlaceholder;
        this.gravelPlaceholder = gravelPlaceholder;
        this.redSandPlaceholder = redSandPlaceholder;
        this.sandPlaceholder = sandPlaceholder;
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
    public boolean isBedrock(final BlockPos p) { return this.is(p, Blocks.BEDROCK);}

    private boolean is(final BlockPos p, final Block blockType) {
        return this.world.getBlockState(p).getBlock() == blockType;
    }

    public boolean is(final BlockPos p, final BlockStateHolder blockType) {
        return this.world.getBlockState(p).getBlock() == blockType.blockState.getBlock();
    }

    public boolean isAny(final BlockStateHolder blockStateHolder) {
        for (final Block block : this.any) {
            if (blockStateHolder.is(block)) {
                return true;
            }
        }
        return false;

    }
    protected boolean isAirPlaceholder(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.airPlaceholder); }
    private boolean isGravelPlaceholder(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.gravelPlaceholder); }
    private boolean isRedSandPlaceholder(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.redSandPlaceholder); }
    private boolean isSandPlaceholder(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.sandPlaceholder); }
    protected  boolean isWaterPlaceholder(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.waterPlaceholder);}
    protected  boolean isLavaPlaceholder(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.lavaPlaceholder); }
    protected  boolean isTntPlaceholder(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.tntPlaceholder); }
    protected  boolean isBedrockPlaceholder(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.bedrockPlaceholder); }
    public boolean isYRotation(final BlockStateHolder blockStateHolder) { return blockStateHolder.is(this.yRotation); }

    public BlockStateHolder replacePlaceholder(final BlockStateHolder blockState) {
        if(this.isAirPlaceholder(blockState)){
            return BlockStateHolder.block(Blocks.AIR.defaultBlockState());
        }
        if(this.isGravelPlaceholder(blockState)){
            return BlockStateHolder.block(Blocks.GRAVEL.defaultBlockState());
        }
        if(this.isRedSandPlaceholder(blockState)){
            return BlockStateHolder.block(Blocks.RED_SAND.defaultBlockState());
        }
        if(this.isSandPlaceholder(blockState)){
            return BlockStateHolder.block(Blocks.SAND.defaultBlockState());
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
