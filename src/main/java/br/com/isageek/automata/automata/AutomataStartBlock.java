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

    public AutomataStartBlock(final TileEntitySupplierPlaceholder tileEntityType) {
        super(tileEntityType, Properties.of(Material.STONE, MaterialColor.STONE).harvestLevel(0));
        this.registerDefaultState(this.defaultBlockState().setValue(state, AutomataStartState.LOAD_REPLACEABLES));
    }

    @Override
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(state);
    }

}
