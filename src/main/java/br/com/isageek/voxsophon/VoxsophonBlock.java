package br.com.isageek.voxsophon;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class VoxsophonBlock extends Block {

    private VoxsophonMod.TileEntitySupplierPlaceholder tileEntityType;

    public VoxsophonBlock(VoxsophonMod.TileEntitySupplierPlaceholder tileEntityType, Properties props) {
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
