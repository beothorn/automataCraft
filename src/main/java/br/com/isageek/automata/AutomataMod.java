package br.com.isageek.automata;

import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.Register;
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

@Mod(AutomataMod.MOD_ID)
public class AutomataMod
{
    public static final String MOD_ID = "automata";

    public AutomataMod() {
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        RegistryObject<Block> automata_termination = Register.block(
                AutomataMod.MOD_ID,
                modEventBus,
                "automata_termination",
                () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE))
        );

        RegistryObject<Block> automata_start = Register.block(
                AutomataMod.MOD_ID,
                modEventBus,
                "automata_start",
                () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE))
        );

        RegistryObject<Block> automata_air_placeholder = Register.block(
                AutomataMod.MOD_ID,
                modEventBus,
                "automata_air_placeholder",
                () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE))
        );

        RegistryObject<Block> automata_water_placeholder = Register.block(
                AutomataMod.MOD_ID,
                modEventBus,
                "automata_water_placeholder",
                () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE))
        );

        RegistryObject<Block> automata_lava_placeholder = Register.block(
                AutomataMod.MOD_ID,
                modEventBus,
                "automata_lava_placeholder",
                () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE))
        );

        RegistryObject<Block> automata_bedrock_placeholder = Register.block(
                AutomataMod.MOD_ID,
                modEventBus,
                "automata_bedrock_placeholder",
                () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE))
        );

        RegistryObject<Block> automata_off = Register.block(
                AutomataMod.MOD_ID,
                modEventBus,
                "automata_off",
                () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE))
        );

        RegistryObject<Block> automata_placeholder = Register.block(
                AutomataMod.MOD_ID,
                modEventBus,
                "automata_placeholder",
                () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE))
        );

        Register.blockWithTileEntity(
            AutomataMod.MOD_ID,
            modEventBus,
            "automata",
            (voxsophonMoverSupplierPlaceholder) -> new AutomataBlock(voxsophonMoverSupplierPlaceholder),
            (tileEntityRegistry, automataBlock) -> {
                AutomataTileEntity automataTileEntity = new AutomataTileEntity(
                        (TileEntityType<AutomataTileEntity>) tileEntityRegistry.get(),
                        automataBlock.get(),
                        automata_placeholder.get(),
                        automata_start.get(),
                        automata_termination.get(),
                        automata_off.get(),
                        automata_air_placeholder.get(),
                        automata_water_placeholder.get(),
                        automata_lava_placeholder.get(),
                        automata_bedrock_placeholder.get()
                );
                automataTileEntity.setAutomataStepper(
                    new AutomataStepper(
                        Blocks.AIR.getDescriptionId(),
                        BlockStateHolder.b(Blocks.AIR.defaultBlockState()),
                        BlockStateHolder.b(Blocks.WATER.defaultBlockState()),
                        BlockStateHolder.b(Blocks.LAVA.defaultBlockState()),
                        BlockStateHolder.b(Blocks.OBSIDIAN.defaultBlockState())
                    )
                );
                return automataTileEntity;
            }
        );
    }

}
