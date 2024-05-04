package br.com.isageek.automata;

import br.com.isageek.automata.automata.AutomataStartBlock;
import br.com.isageek.automata.automata.states.LoadReplaceables;
import br.com.isageek.automata.forge.SystemEntityClock;
import br.com.isageek.automata.forge.TickableTileEntityStrategy;
import br.com.isageek.automata.forge.WorldController;
import br.com.isageek.automata.structures.VanillaStructure;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static br.com.isageek.automata.forge.Register.*;

@Mod(AutomataMod.MOD_ID)
public class AutomataMod
{
    public static final String MOD_ID = "automata";

    public static final int MAX_SEARCH_RADIUS = 100;
    public static final int EXECUTE_MINIMAL_TICK_INTERVAL = 500;
    public static final int EXECUTE_THROTTLE_AFTER_AUTOMATA_COUNT = 8000;
    public final static long SEARCH_AGAIN_TIMEOUT = 3000;

    public AutomataMod() {
        final IEventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.register(this);

        final Supplier<Block> regularBlock = () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE));

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        final RegistryObject<Block> automata_termination = block("automata_termination", AutomataMod.MOD_ID, modEventBus, regularBlock);
        final RegistryObject<Block> automata = block("automata", AutomataMod.MOD_ID, modEventBus, regularBlock);

        final RegistryObject<Block> automata_air_placeholder = block("automata_air_placeholder", AutomataMod.MOD_ID, modEventBus, regularBlock);
        final RegistryObject<Block> automata_water_placeholder = block("automata_water_placeholder", AutomataMod.MOD_ID, modEventBus, regularBlock);
        final RegistryObject<Block> automata_lava_placeholder = block("automata_lava_placeholder", AutomataMod.MOD_ID, modEventBus, regularBlock);
        final RegistryObject<Block> automata_tnt_placeholder = block("automata_tnt_placeholder", AutomataMod.MOD_ID, modEventBus, regularBlock);
        final RegistryObject<Block> automata_bedrock_placeholder = block("automata_bedrock_placeholder", AutomataMod.MOD_ID, modEventBus, regularBlock);
        final RegistryObject<Block> automata_gravel_placeholder = block("automata_gravel_placeholder", AutomataMod.MOD_ID, modEventBus, regularBlock);
        final RegistryObject<Block> automata_red_sand_placeholder = block("automata_red_sand_placeholder", AutomataMod.MOD_ID, modEventBus, regularBlock);
        final RegistryObject<Block> automata_sand_placeholder = block("automata_sand_placeholder", AutomataMod.MOD_ID, modEventBus, regularBlock);

        final RegistryObject<Block> automata_y_rotation = block("automata_y_rotation", AutomataMod.MOD_ID, modEventBus, regularBlock);

        blockWithTileEntity(
            AutomataMod.MOD_ID,
            modEventBus,
            "automata_start",
            AutomataStartBlock::new,
            (automata_start) -> {
                final WorldController worldController = new WorldController(
                    new Block[]{Blocks.AIR, Blocks.CAVE_AIR},
                    automata.get(),
                    automata_termination.get(),
                    automata_start.get(),
                    automata_air_placeholder.get(),
                    automata_gravel_placeholder.get(),
                    automata_red_sand_placeholder.get(),
                    automata_sand_placeholder.get(),
                    automata_water_placeholder.get(),
                    automata_lava_placeholder.get(),
                    automata_tnt_placeholder.get(),
                    automata_bedrock_placeholder.get(),
                    automata_y_rotation.get()
                );
                final LoadReplaceables initial = new LoadReplaceables();
                final SystemEntityClock entityClock = new SystemEntityClock();
                return new TickableTileEntityStrategy(
                    automata_start.get(),
                    worldController,
                    initial,
                    entityClock
                );
            }
        );
    }

}
