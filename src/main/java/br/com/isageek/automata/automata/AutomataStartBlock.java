package br.com.isageek.automata.automata;

import br.com.isageek.automata.forge.BlockWithTileEntity;
import br.com.isageek.automata.forge.TileEntitySupplierPlaceholder;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class AutomataStartBlock extends BlockWithTileEntity {

    public static final EnumProperty<AutomataStartState> state = EnumProperty.create("state", AutomataStartState.class);

    public AutomataStartBlock(final TileEntitySupplierPlaceholder tileEntityType) {
        super(tileEntityType, BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE));
        this.registerDefaultState(this.defaultBlockState().setValue(state, AutomataStartState.LOAD_REPLACEABLES));
    }

}
