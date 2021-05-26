package br.com.isageek.automata;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static br.com.isageek.automata.BlockStateHolder.b;

public class AutomataTileEntity extends TileEntity implements ITickableTileEntity {

    private BlockTree blockTree;
    private final Block termination;
    private final Block automataPlaceholder;
    private final Block airPlaceholder;
    private final Block waterPlaceholder;
    private final Block lavaPlaceholder;
    private final TileEntityType<AutomataTileEntity> tileEntityType;

    private boolean loaded = false;
    private int wait = 30;

    public AutomataTileEntity(
            TileEntityType<AutomataTileEntity> tileEntityType,
            BlockTree blockTree,
            Block automataPlaceholder,
            Block termination,
            Block airPlaceholder,
            Block waterPlaceholder,
            Block lavaPlaceholder
    ) {
        super(tileEntityType);
        this.tileEntityType = tileEntityType;
        this.blockTree = blockTree;
        this.termination = termination;
        this.automataPlaceholder = automataPlaceholder;
        this.airPlaceholder = airPlaceholder;
        this.waterPlaceholder = waterPlaceholder;
        this.lavaPlaceholder = lavaPlaceholder;
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
        return BlockStateHolder.b(world.getBlockState(automataBlockPosition.offset(xOffset, yOffset, zOffset)));
    }

    private BlockStateHolder[] startingAt(
            World world,
            BlockPos blockPos,
            int axis,
            int xOffset,
            int yOffset,
            int zOffset
    ){
        BlockStateHolder[] result = new BlockStateHolder[27];

        int i = 0;
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                for (int c = 0; c < 3; c++) {
                    if(axis == 0){
                        result[i++] =  newStateHolderForRelativePosition(
                                world,
                                blockPos,
                                a+xOffset,
                                b+yOffset,
                                c+zOffset
                        );
                    }
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

                BlockStateHolder[] match1 = startingAt(w, p,0, -7, -1, -1);
                BlockStateHolder[] result1 = startingAt(w, p, 0, -4, -1, -1);
                blockTree.addPattern(match1, result1);
            }
        }else{
            if(loaded){
                BlockState defaultBlockState = getBlockState().getBlock().defaultBlockState();
                BlockStateHolder[] toReplace = blockTree.getResultFor(startingAt(w, p,0, -3, -1, -1));
                if(toReplace != null){
                    int i = 0;
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                BlockStateHolder blockStateHolder = toReplace[i++];

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
