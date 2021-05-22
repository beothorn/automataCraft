package br.com.isageek.voxsophon;

import br.com.isageek.voxsophon.forge.BlockWithTileEntity;
import br.com.isageek.voxsophon.forge.TileEntitySupplierPlaceholder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;

public class VoxsophonBlock extends BlockWithTileEntity {

    public static final BooleanProperty loaded = BooleanProperty.create("loaded");

    public VoxsophonBlock(TileEntitySupplierPlaceholder tileEntityType) {
        super(tileEntityType, Block.Properties.of(Material.STONE, MaterialColor.STONE));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(loaded);
        super.createBlockStateDefinition(stateBuilder);
    }

}
