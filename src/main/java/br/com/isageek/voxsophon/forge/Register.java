package br.com.isageek.voxsophon.forge;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public class Register {

    public static void blockWithTileEntity(
            String modId,
            IEventBus modEventBus,
            String blockName,
            Function<TileEntitySupplierPlaceholder, Block> blockSupplier,
            Function<RegistryObject<TileEntityType<? extends TileEntity>>, ? extends TileEntity> tileEntitySupplier
    ) {
        TileEntitySupplierPlaceholder tileEntitySupplierPlaceholder = new TileEntitySupplierPlaceholder();

        RegistryObject<Block> blockRegister = block(
            modId,
            modEventBus,
            blockName,
            () -> blockSupplier.apply(tileEntitySupplierPlaceholder)
        );

        DeferredRegister<TileEntityType<?>> tileEntityTypeDeferredRegister = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, modId);

        RegistryObject<TileEntityType<? extends TileEntity>> tileEntityRegistry = tileEntityTypeDeferredRegister.register(blockName +"_tile_entity", () ->
            TileEntityType.Builder.of(tileEntitySupplierPlaceholder, blockRegister.get()).build(null)
        );
        Supplier<? extends TileEntity> supplier = () -> tileEntitySupplier.apply(tileEntityRegistry);
        tileEntitySupplierPlaceholder.setRealSupplier(supplier);
        tileEntityTypeDeferredRegister.register(modEventBus);
    }

    private static RegistryObject<Block> block(
        String modId,
        IEventBus modEventBus,
        String blockName,
        Supplier<Block> blockSupplier
    ) {

        DeferredRegister<Block> blockDeferredRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, modId);
        RegistryObject<Block> block = blockDeferredRegister.register(blockName, blockSupplier);
        blockDeferredRegister.register(modEventBus);

        DeferredRegister<Item> itemDeferredRegister = DeferredRegister.create(ForgeRegistries.ITEMS, modId);
        itemDeferredRegister.register(blockName+"_item", () ->
            new BlockItem(block.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS))
        );
        itemDeferredRegister.register(modEventBus);


        return block;
    }
}
