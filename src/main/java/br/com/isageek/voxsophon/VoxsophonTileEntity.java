package br.com.isageek.voxsophon;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public class VoxsophonTileEntity extends TileEntity implements ITickableTileEntity {
    public VoxsophonTileEntity(TileEntityType<VoxsophonTileEntity> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public void tick() {
        BlockPos blockPos = getBlockPos();
        setPosition(blockPos.offset(1, 0, 0));
        //world set chunk somehow???
    }
}
