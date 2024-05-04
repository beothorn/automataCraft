package br.com.isageek.automata.forge;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.profiling.jfr.event.WorldLoadFinishedEvent;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Register {

    private static final Logger LOGGER = LogManager.getLogger(Register.class);

    public static void blockWithTileEntity(
            final String modId,
            final IEventBus modEventBus,
            final String blockName,
            final Function<TileEntitySupplierPlaceholder, BlockEntity> blockSupplier,
            final Consumer<RegistryObject<Block>> tileEntitySupplier
    ) {
        final TileEntitySupplierPlaceholder tileEntitySupplierPlaceholder = new TileEntitySupplierPlaceholder();

        final RegistryObject<Block> blockRegister = block(
            blockName,
            modId,
            modEventBus,
            () -> blockSupplier.apply(tileEntitySupplierPlaceholder)
        );
        tileEntitySupplier.accept(blockRegister);
    }

    public static RegistryObject<Block> block(
            final String blockName,
            final String modId,
            final IEventBus modEventBus,
            final Supplier<Block> blockSupplier
    ) {

        final DeferredRegister<Block> blockDeferredRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, modId);
        final RegistryObject<Block> block = blockDeferredRegister.register(blockName, blockSupplier);
        blockDeferredRegister.register(modEventBus);

        final DeferredRegister<Item> itemDeferredRegister = DeferredRegister.create(ForgeRegistries.ITEMS, modId);
        itemDeferredRegister.register(blockName+"_item", () ->
            new BlockItem(block.get(), new Item.Properties().)
        );
        itemDeferredRegister.register(modEventBus);


        return block;
    }

    public static RegistryObject<Structure<NoneFeatureConfiguration>> structure(
            final String structureName,
            final int seed,
            final int averageSpawningDistance,
            final int minimumDistanceInChunk,
            final String modId,
            final IEventBus modEventBus,
            final IEventBus forgeBus,
            final Supplier<Structure> structureSupplier
    ){
        final DeferredRegister<Structure> structureDeferredRegister = DeferredRegister.create(ForgeRegistries.STRUCTURE_MODIFIER_SERIALIZERS.get(), modId);
        final RegistryObject<Structure<NoneFeatureConfiguration>> structure = structureDeferredRegister.register(structureName, structureSupplier);
        structureDeferredRegister.register(modEventBus);
        DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, modId);
        forgeBus.addListener(EventPriority.NORMAL, (WorldLoadFinishedEvent event) -> Register.addStructureDimensionToChunkGenerator(
            event,
            structure,
            seed,
            averageSpawningDistance,
            minimumDistanceInChunk
        ));
        forgeBus.addListener(EventPriority.HIGH, (BiomeLoadingEvent event) -> Register.addStructureToAllBiomes(event, structure));
        modEventBus.addListener((FMLCommonSetupEvent e) -> e.enqueueWork(() -> {
            final String structureRegistryName = structure.get().getRegistryName().toString();
            Structure.STRUCTURES_REGISTRY.put(structureRegistryName, structure.get());
        }));
        return structure;
    }

    private static <T extends IFeatureConfig> void addStructureDimensionToChunkGenerator(
            final WorldEvent.Load event,
            final RegistryObject<Structure<T>> structure,
            final int seed,
            final int averageSpawningDistance,
            final int minimumDistanceInChunk
    ) {
        LOGGER.debug("addStructureDimensionToChunkGenerator");
        if(event.getWorld() instanceof ServerWorld){
            final StructureSeparationSettings structureSeparationSettings = new StructureSeparationSettings(
                averageSpawningDistance,
                minimumDistanceInChunk,
                seed
            );
            final ServerWorld serverWorld = (ServerWorld)event.getWorld();
            serverWorld
                .getChunkSource()
                .generator
                .getSettings()
                .structureConfig()
                .putIfAbsent(
                    structure.get(),
                    structureSeparationSettings
                );
        }
    }

    private static void addStructureToAllBiomes(
            final BiomeLoadingEvent event,
            final RegistryObject<Structure<NoFeatureConfig>> structure
    ) {
        LOGGER.debug("addStructureToAllBiomes");
        event.getGeneration().getStructures().add(() -> structure.get().configured(IFeatureConfig.NONE));
    }


}
