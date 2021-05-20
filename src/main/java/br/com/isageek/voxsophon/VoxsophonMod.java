package br.com.isageek.voxsophon;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

@Mod(VoxsophonMod.MOD_ID)
public class VoxsophonMod
{
    public static final String MOD_ID = "voxsophon";

    /***
         Workaround circular dependency
         the solutions you find looking online usually
         use static fields referencing each other,
         making it really hard to figure out that exists a
         circular dependency. Instead of using static fields
         as globals, this SupplierPlaceholder makes explicit
         what is going on.
     */
    public static class TileEntitySupplierPlaceholder implements Supplier<VoxsophonTileEntity> {

        private Supplier<VoxsophonTileEntity> realSupplier;

        public void setRealSupplier(Supplier<VoxsophonTileEntity> realSupplier){
            this.realSupplier = realSupplier;
        }

        @Override
        public VoxsophonTileEntity get() {
            return realSupplier.get();
        }
    }

    public VoxsophonMod() {
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        TileEntitySupplierPlaceholder tileEntitySupplierPlaceholder = new TileEntitySupplierPlaceholder();

        RegistryObject<Block> voxsophonBlockRegister = registerBlock(modEventBus, "voxsophon", () ->
                new VoxsophonBlock(tileEntitySupplierPlaceholder, Block.Properties.of(Material.STONE, MaterialColor.STONE))
        );

        DeferredRegister<TileEntityType<?>> tileEntityTypeDeferredRegister = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);

        RegistryObject<TileEntityType<VoxsophonTileEntity>> voxsophonTileEntity = tileEntityTypeDeferredRegister.register("voxsophon_tile_entity", () ->
            TileEntityType.Builder.of(tileEntitySupplierPlaceholder, voxsophonBlockRegister.get()).build(null)
        );
        tileEntitySupplierPlaceholder.setRealSupplier(() -> new VoxsophonTileEntity(voxsophonTileEntity.get()));
        tileEntityTypeDeferredRegister.register(modEventBus);


    }

    private RegistryObject<Block> registerBlock(IEventBus modEventBus, String blockName, Supplier<Block> blockSupplier) {

        DeferredRegister<Block> blockDeferredRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
        RegistryObject<Block> voxsophonBlock = blockDeferredRegister.register(blockName, blockSupplier);
        blockDeferredRegister.register(modEventBus);

        DeferredRegister<Item> itemDeferredRegister = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
        itemDeferredRegister.register(blockName+"_item", () ->
            new BlockItem(voxsophonBlock.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS))
        );
        itemDeferredRegister.register(modEventBus);


        return voxsophonBlock;
    }
}
