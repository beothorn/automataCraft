package br.com.isageek.automata;

import br.com.isageek.automata.automata.AutomataSearch;
import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.ClockedTileEntity;
import br.com.isageek.automata.forge.SystemEntityClock;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Supplier;

import static br.com.isageek.automata.forge.Register.block;
import static br.com.isageek.automata.forge.Register.blockWithTileEntity;

@Mod(AutomataMod.MOD_ID)
public class AutomataMod
{
    public static final String MOD_ID = "automata";

    public AutomataMod() {
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Supplier<Block> regularBlock = () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE));

        RegistryObject<Block> automata_termination = block("automata_termination", AutomataMod.MOD_ID, modEventBus, regularBlock);
        RegistryObject<Block> automata = block("automata", AutomataMod.MOD_ID, modEventBus, regularBlock);
        RegistryObject<Block> automata_air_placeholder = block("automata_air_placeholder", AutomataMod.MOD_ID, modEventBus, regularBlock);
        RegistryObject<Block> automata_water_placeholder = block("automata_water_placeholder", AutomataMod.MOD_ID, modEventBus, regularBlock);
        RegistryObject<Block> automata_lava_placeholder = block("automata_lava_placeholder", AutomataMod.MOD_ID, modEventBus, regularBlock);
        RegistryObject<Block> automata_bedrock_placeholder = block("automata_bedrock_placeholder", AutomataMod.MOD_ID, modEventBus, regularBlock);
        RegistryObject<Block> automata_placeholder = block("automata_placeholder", AutomataMod.MOD_ID, modEventBus, regularBlock);
        RegistryObject<Block> automata_y_rotation = block("automata_y_rotation", AutomataMod.MOD_ID, modEventBus, regularBlock);

        blockWithTileEntity(AutomataMod.MOD_ID, modEventBus,
            "automata_start",
            AutomataStartBlock::new,
            (tileEntityRegistry, automata_start) -> {
                WorldController worldController = new WorldController(
                    new Block[]{Blocks.AIR, Blocks.CAVE_AIR},
                    automata.get(),
                    automata_termination.get(),
                    automata_start.get(),
                    automata_placeholder.get(),
                    automata_air_placeholder.get(),
                    automata_water_placeholder.get(),
                    automata_lava_placeholder.get(),
                    automata_bedrock_placeholder.get(),
                    automata_y_rotation.get()
                );
                AutomataSearch initial = new AutomataSearch();
                SystemEntityClock entityClock = new SystemEntityClock();
                return new ClockedTileEntity(
                    (TileEntityType<ClockedTileEntity>) tileEntityRegistry.get(),
                    worldController,
                    initial,
                    entityClock
                );
            }
        );
    }

}
