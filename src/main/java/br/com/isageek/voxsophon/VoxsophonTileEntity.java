package br.com.isageek.voxsophon;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VoxsophonTileEntity extends TileEntity implements ITickableTileEntity {

    private int moveCount = 0;

    public VoxsophonTileEntity(TileEntityType<VoxsophonTileEntity> tileEntityType) {
        super(tileEntityType);
    }

    public void setMoveCount(int newCount){
        moveCount = newCount;
    }

    @Override
    public void tick() {
        BlockPos blockPos = getBlockPos();
        BlockPos newBlockPosition = blockPos.offset(1, 0, 0);
        World world = getLevel();
        BlockState otherBlock = world.getBlockState(newBlockPosition);
        if(moveCount >= 3){
            return;
        }
        if(otherBlock.getBlock() == Blocks.AIR){
            BlockState blockState = getBlockState();
            world.setBlock(
                    newBlockPosition,
                    blockState,
                    0,
                    0
            );
            VoxsophonTileEntity blockEntity = (VoxsophonTileEntity)world.getBlockEntity(newBlockPosition);
            blockEntity.setMoveCount(moveCount + 1);
            world.removeBlock(
                    blockPos,
                    true
            );
        }
    }
}
