package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockWithTileEntity;
import br.com.isageek.automata.forge.TileEntitySupplierPlaceholder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;

public class AutomataBlock extends BlockWithTileEntity {

    public static final BooleanProperty loaded = BooleanProperty.create("loaded");

    public AutomataBlock(TileEntitySupplierPlaceholder tileEntityType) {
        super(tileEntityType, Block.Properties.of(Material.STONE, MaterialColor.STONE).harvestLevel(0));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(loaded);
        super.createBlockStateDefinition(stateBuilder);
    }

}
