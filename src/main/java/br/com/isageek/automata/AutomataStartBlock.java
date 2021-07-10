package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockWithTileEntity;
import br.com.isageek.automata.forge.ClockedTileEntity;
import br.com.isageek.automata.forge.TileEntitySupplierPlaceholder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;

public class AutomataStartBlock extends BlockWithTileEntity {

    public static final BooleanProperty loaded = BooleanProperty.create("loaded");

    public AutomataStartBlock(TileEntitySupplierPlaceholder tileEntityType) {
        super(tileEntityType, Properties.of(Material.STONE, MaterialColor.STONE).harvestLevel(0));
        registerDefaultState(defaultBlockState().setValue(loaded, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(loaded);
    }

}
