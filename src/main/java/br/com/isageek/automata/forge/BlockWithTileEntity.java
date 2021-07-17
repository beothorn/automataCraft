package br.com.isageek.automata.forge;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockWithTileEntity extends Block {

    private final TileEntitySupplierPlaceholder tileEntityType;

    public BlockWithTileEntity(
        TileEntitySupplierPlaceholder tileEntityType,
        Properties props
    ) {
        super(props);
        this.tileEntityType = tileEntityType;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return this.tileEntityType.get();
    }

}
