package br.com.isageek.automata.automata;

import br.com.isageek.automata.forge.BlockWithTileEntity;
import br.com.isageek.automata.forge.TileEntitySupplierPlaceholder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;

public class AutomataStartBlock extends BlockWithTileEntity {

    public static final EnumProperty<AutomataStartState> state = EnumProperty.create("state", AutomataStartState.class);

    public AutomataStartBlock(TileEntitySupplierPlaceholder tileEntityType) {
        super(tileEntityType, Properties.of(Material.STONE, MaterialColor.STONE).harvestLevel(0));
        registerDefaultState(defaultBlockState().setValue(state, AutomataStartState.SEARCH));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(state);
    }

}
