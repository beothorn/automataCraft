package br.com.isageek.automata.automata;

import br.com.isageek.automata.AutomataMod;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class AutomataStartBaseEntityBlock extends BaseEntityBlock {

    public static final Property<AutomataStartState> state = EnumProperty.create("state", AutomataStartState.class);
    private final AtomicReference<RegistryObject<BlockEntityType<?>>> blockEntityType;
    private final Map<String, RegistryObject<Block>> registeredBlocks;

    public AutomataStartBaseEntityBlock(
            AtomicReference<RegistryObject<BlockEntityType<?>>> blockEntityType,
            Map<String, RegistryObject<Block>> registeredBlocks
    ) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
        this.blockEntityType = blockEntityType;
        this.registeredBlocks = registeredBlocks;
        this.registerDefaultState(this.getStateDefinition().any().setValue(state, AutomataStartState.LOAD_REPLACEABLES));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(state);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(
        BlockPos blockPos,
        BlockState blockState
    ) {
        return new AutomataStartBlockEntity(blockEntityType, blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level,
        BlockState blockState,
        BlockEntityType<T> type
    ) {
        return type == blockEntityType.get().get() ? new Ticker(new WorldController(
            new Block[]{Blocks.AIR, Blocks.CAVE_AIR},
            registeredBlocks.get(AutomataMod.automata).get(),
            registeredBlocks.get(AutomataMod.automata_termination).get(),
            registeredBlocks.get(AutomataMod.automata_start).get(),
            registeredBlocks.get(AutomataMod.automata_air_placeholder).get(),
            registeredBlocks.get(AutomataMod.automata_gravel_placeholder).get(),
            registeredBlocks.get(AutomataMod.automata_red_sand_placeholder).get(),
            registeredBlocks.get(AutomataMod.automata_sand_placeholder).get(),
            registeredBlocks.get(AutomataMod.automata_water_placeholder).get(),
            registeredBlocks.get(AutomataMod.automata_lava_placeholder).get(),
            registeredBlocks.get(AutomataMod.automata_tnt_placeholder).get(),
            registeredBlocks.get(AutomataMod.automata_bedrock_placeholder).get(),
            registeredBlocks.get(AutomataMod.automata_y_rotation).get()
        )) : null;
    }

}
