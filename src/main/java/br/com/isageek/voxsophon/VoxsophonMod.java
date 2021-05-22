package br.com.isageek.voxsophon;

import br.com.isageek.voxsophon.forge.Register;
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

        Register.blockWithTileEntity(
                VoxsophonMod.MOD_ID,
                modEventBus,
                "voxsophon",
                (voxsophonMoverSupplierPlaceholder) -> new VoxsophonBlock(voxsophonMoverSupplierPlaceholder),
                (registry) -> new VoxsophonMoverTileEntity(
                    (TileEntityType<VoxsophonMoverTileEntity>) registry.get()
                )
        );
    }

}
