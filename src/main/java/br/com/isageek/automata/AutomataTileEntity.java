package br.com.isageek.automata;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AutomataTileEntity extends TileEntity implements ITickableTileEntity {

    private BlockTree blockTree;
    private final Block termination;
    private final Block automataPlaceholder;
    private final Block airPlaceholder;
    private final Block waterPlaceholder;
    private final Block lavaPlaceholder;
    private final Block bedrockPlaceholder;
    private final TileEntityType<AutomataTileEntity> tileEntityType;

    private boolean loaded = false;
    private int wait = 10;

    public AutomataTileEntity(
            TileEntityType<AutomataTileEntity> tileEntityType,
            BlockTree blockTree,
            Block automataPlaceholder,
            Block termination,
            Block airPlaceholder,
            Block waterPlaceholder,
            Block lavaPlaceholder,
            Block bedrockPlaceholder
    ) {
        super(tileEntityType);
        this.tileEntityType = tileEntityType;
        this.blockTree = blockTree;
        this.termination = termination;
        this.automataPlaceholder = automataPlaceholder;
        this.airPlaceholder = airPlaceholder;
        this.waterPlaceholder = waterPlaceholder;
        this.lavaPlaceholder = lavaPlaceholder;
        this.bedrockPlaceholder = bedrockPlaceholder;
    }

    public void setBlockTree(BlockTree blockTree){
        this.loaded = true;
        this.blockTree = blockTree;
    }

    private BlockStateHolder newStateHolderForRelativePosition(
        World world,
        BlockPos automataBlockPosition,
        int xOffset,
        int yOffset,
        int zOffset
    ){
        BlockState blockState = world.getBlockState(automataBlockPosition.offset(xOffset, yOffset, zOffset));
        if(blockState.getBlock() == airPlaceholder) return BlockStateHolder.b(Blocks.AIR.defaultBlockState());
        if(blockState.getBlock() == waterPlaceholder) return BlockStateHolder.b(Blocks.WATER.defaultBlockState());
        if(blockState.getBlock() == lavaPlaceholder) return BlockStateHolder.b(Blocks.LAVA.defaultBlockState());
        if(blockState.getBlock() == bedrockPlaceholder) return BlockStateHolder.b(Blocks.BEDROCK.defaultBlockState());
        return BlockStateHolder.b(blockState);
    }

    private BlockStateHolder[] surrounding(
            World world,
            BlockPos blockPos
    ){
        BlockStateHolder[] result = new BlockStateHolder[27];

        int i = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    result[i++] =  newStateHolderForRelativePosition(
                        world,
                        blockPos,
                        x,
                        y,
                        z
                    );
                }
            }
        }

        return result;

    }

    @Override
    public void tick() {
        try {
            internalTick();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void internalTick() {
        if(wait > 0){
            wait--;
            return;
        }
        World w = getLevel();
        BlockPos p = getBlockPos();

        Block immediateBlockXMinus = w.getBlockState(p.offset
                (-1, 0, 0)).getBlock();
        Block immediateBlockXPlus = w.getBlockState(p.offset
                (1, 0, 0)).getBlock();
        Block immediateBlockZMinus = w.getBlockState(p.offset
                (0, 0, -1)).getBlock();
        Block immediateBlockZPlus = w.getBlockState(p.offset
                (0, 0, 1)).getBlock();
        if(immediateBlockXMinus == termination && !loaded){
            BlockPos maybeTerminatorPosition = p.offset(-8, 0, 0);
            Block maybeTerminator = w.getBlockState(maybeTerminatorPosition).getBlock();
            if(maybeTerminator == termination){
                loaded = true;
                BlockStateHolder[] match1 = surrounding(w, p.offset(-6, 0, 0));
                BlockStateHolder[] result1 = surrounding(w, p.offset(-3, 0, 0));
                blockTree.addPattern(match1, result1);
            }
        }else{
            if(loaded){
                BlockState defaultBlockState = getBlockState().getBlock().defaultBlockState();
                BlockStateHolder[] toReplace = blockTree.getReplacementFor(surrounding(w, p));
                if(toReplace != null){
                    int i = 0;
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                BlockStateHolder blockStateHolder = toReplace[i++];

                                if(w.getBlockState(p.offset(x, y, z)).getBlock() != Blocks.BEDROCK){
                                    if(blockStateHolder.blockState.getBlock() == automataPlaceholder){
                                        w.setBlock(
                                                p.offset(x, y, z),
                                                defaultBlockState,
                                                0,
                                                0
                                        );

                                        AutomataTileEntity blockEntity = (AutomataTileEntity) w.getBlockEntity(p.offset(x, y, z));
                                        blockEntity.setBlockTree(this.blockTree);
                                    } else{

                                        BlockState blockState = blockStateHolder.blockState;
                                        if(blockStateHolder.blockState.getBlock() == airPlaceholder){
                                            blockState = Blocks.AIR.defaultBlockState();
                                        }
                                        if(blockStateHolder.blockState.getBlock() == waterPlaceholder){
                                            blockState = Blocks.WATER.defaultBlockState();
                                        }
                                        if(blockStateHolder.blockState.getBlock() == lavaPlaceholder){
                                            blockState = Blocks.LAVA.defaultBlockState();
                                        }
                                        if(blockStateHolder.blockState.getBlock() == bedrockPlaceholder){
                                            blockState = Blocks.OBSIDIAN.defaultBlockState();
                                        }


                                        w.setBlock(
                                                p.offset(x, y, z),
                                                blockState,
                                                0,
                                                0
                                        );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        if(immediateBlockXPlus == termination){
            w.removeBlock(p.offset(1, 0, 0), false);
        }
        if(immediateBlockZMinus == termination){
            w.removeBlock(p.offset(0, 0, -1), false);
        }
        if(immediateBlockZPlus == termination){
            w.removeBlock(p.offset(0, 0, 1), false);
        }
    }
}
