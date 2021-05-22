package br.com.isageek.voxsophon;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VoxsophonMoverTileEntity extends TileEntity implements ITickableTileEntity {

    public VoxsophonMoverTileEntity(TileEntityType<VoxsophonMoverTileEntity> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public void tick() {
        BlockPos blockPos = getBlockPos();
        BlockPos up = blockPos.offset(0, 1, 0);
        BlockPos down = blockPos.offset(0, -1, 0);
//        BlockPos left = blockPos.offset(0, 1, 0);
//        BlockPos right = blockPos.offset(0, -1, 0);
//        BlockPos front = blockPos.offset(0, 0, -1);
//        BlockPos back = blockPos.offset(0, 0, 1);
        World world = getLevel();

        BlockState blockStateUp = world.getBlockState(up);
        BlockState blockStateDown = world.getBlockState(down);

        world.setBlock(
                down,
                blockStateUp,
                0,
                0
        );


        world.setBlock(
                up,
                blockStateDown,
                0,
                0
        );

        world.removeBlock(
                blockPos,
                true
        );
    }
}
