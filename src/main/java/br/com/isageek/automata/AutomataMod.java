package br.com.isageek.automata;

import br.com.isageek.automata.forge.Register;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AutomataMod.MOD_ID)
public class AutomataMod
{
    public static final String MOD_ID = "automata";

    public AutomataMod() {
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Register.blockWithTileEntity(
                AutomataMod.MOD_ID,
                modEventBus,
                "automata",
                (voxsophonMoverSupplierPlaceholder) -> new AutomataBlock(voxsophonMoverSupplierPlaceholder),
                (registry) -> new AutomataTileEntity(
                    (TileEntityType<AutomataTileEntity>) registry.get()
                )
        );
    }

}
