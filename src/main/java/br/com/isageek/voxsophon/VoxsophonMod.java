package br.com.isageek.voxsophon;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VoxsophonMod.MOD_ID)
public class VoxsophonMod
{
    public static final String MOD_ID = "voxsophon";

    public VoxsophonMod() {
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        RegisterStuff.registerBlockWithTileEntity(
            VoxsophonMod.MOD_ID,
            modEventBus,
            "voxsophon_reader",
            (voxsophonReaderSupplierPlaceholder) -> new BlockWithTileEntity(
                voxsophonReaderSupplierPlaceholder,
                Block.Properties.of(Material.STONE, MaterialColor.STONE)
            ),
            (registry) -> new VoxsophonReaderTileEntity((TileEntityType<VoxsophonReaderTileEntity>) registry.get())
        );

        RegisterStuff.registerBlockWithTileEntity(
                VoxsophonMod.MOD_ID,
                modEventBus,
                "voxsophon_mover",
                (voxsophonMoverSupplierPlaceholder) -> new BlockWithTileEntity(
                        voxsophonMoverSupplierPlaceholder,
                        Block.Properties.of(Material.STONE, MaterialColor.STONE)
                ),
                (registry) -> new VoxsophonMoverTileEntity((TileEntityType<VoxsophonMoverTileEntity>) registry.get())
        );
    }

}
