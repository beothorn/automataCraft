package br.com.isageek.automata.forge;

import br.com.isageek.automata.AutomataMod;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Register {

    private static final Logger LOGGER = LogManager.getLogger(Register.class);

    public static void blockWithTileEntity(
            String modId,
            IEventBus modEventBus,
            String blockName,
            Function<TileEntitySupplierPlaceholder, Block> blockSupplier,
            BiFunction<RegistryObject<TileEntityType<? extends TileEntity>>, RegistryObject<Block>, ? extends TileEntity> tileEntitySupplier
    ) {
        TileEntitySupplierPlaceholder tileEntitySupplierPlaceholder = new TileEntitySupplierPlaceholder();

        RegistryObject<Block> blockRegister = block(
            blockName,
            modId,
            modEventBus,
            () -> blockSupplier.apply(tileEntitySupplierPlaceholder)
        );

        DeferredRegister<TileEntityType<?>> tileEntityTypeDeferredRegister = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, modId);

        RegistryObject<TileEntityType<? extends TileEntity>> tileEntityRegistry = tileEntityTypeDeferredRegister.register(blockName +"_tile_entity", () ->
            TileEntityType.Builder.of(tileEntitySupplierPlaceholder, blockRegister.get()).build(null)
        );
        Supplier<? extends TileEntity> supplier = () -> tileEntitySupplier.apply(tileEntityRegistry, blockRegister);
        tileEntitySupplierPlaceholder.setRealSupplier(supplier);
        tileEntityTypeDeferredRegister.register(modEventBus);
    }

    public static RegistryObject<Block> block(
            String blockName,
            String modId,
            IEventBus modEventBus,
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

    public static RegistryObject<Structure<NoFeatureConfig>> structure(
        String structureName,
        String modId,
        IEventBus modEventBus,
        IEventBus forgeBus,
        Supplier<Structure<NoFeatureConfig>> structureSupplier
    ){
        DeferredRegister<Structure<?>> structureDeferredRegister = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, modId);
        RegistryObject<Structure<NoFeatureConfig>> structure = structureDeferredRegister.register(structureName, structureSupplier);
        structureDeferredRegister.register(modEventBus);
        forgeBus.addListener(EventPriority.NORMAL, (WorldEvent.Load event) -> Register.addStructureDimensionToChunkGenerator(event, structure));
        forgeBus.addListener(EventPriority.HIGH, (BiomeLoadingEvent event) -> Register.addStructureToAllBiomes(event, structure));
        modEventBus.addListener((FMLCommonSetupEvent e) -> setup(e, structureName, structure));
        return structure;
    }

    private static <T extends IFeatureConfig> void addStructureDimensionToChunkGenerator(
            final WorldEvent.Load event,
            RegistryObject<Structure<T>> structure
    ) {
        LOGGER.debug("addStructureDimensionToChunkGenerator");
        if(event.getWorld() instanceof ServerWorld){
            int averageSpawningDistance = 10;
            int minimumDistanceInChunk = 5;
            int seed = 1234567890;
            StructureSeparationSettings structureSeparationSettings = new StructureSeparationSettings(averageSpawningDistance, minimumDistanceInChunk, seed);
            ServerWorld serverWorld = (ServerWorld)event.getWorld();
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

    public static void addStructureToAllBiomes(
            final BiomeLoadingEvent event,
            RegistryObject<Structure<NoFeatureConfig>> structure
    ) {
        LOGGER.debug("addStructureToAllBiomes");
        event.getGeneration().getStructures().add(() -> structure.get().configured(IFeatureConfig.NONE));
    }

    /**
     * Here, setupStructures will be ran after registration of all structures are finished.
     * This is important to be done here so that the Deferred Registry has already ran and
     * registered/created our structure for us.
     *
     * Once after that structure instance is made, we then can now do the rest of the setup
     * that requires a structure instance such as setting the structure spacing, creating the
     * configured structure instance, and more.
     */
    public static void setup(
            final FMLCommonSetupEvent event,
            String structureName,
            RegistryObject<Structure<NoFeatureConfig>> structure
    ){
        LOGGER.debug("setup enqueued");
        event.enqueueWork(() -> {
            LOGGER.debug("setup executed");
            setupStructures(structure);
            registerConfiguredStructures(structureName, structure);
        });
    }

    public static void setupStructures(RegistryObject<Structure<NoFeatureConfig>> structure) {
        LOGGER.debug("setupStructures");
//        int averageSpawningDistance = 10;
//        int minimumDistanceInChunk = 5;
//        int seed = 1234567890;
//        new StructureSeparationSettings(averageSpawningDistance, minimumDistanceInChunk, seed);
        String structureRegistryName = structure.get().getRegistryName().toString();
        LOGGER.debug("Registring structure "+structureRegistryName);
        Structure.STRUCTURES_REGISTRY.put(structureRegistryName, structure.get());
    }

    /**
     * Registers the configured structure which is what gets added to the biomes.
     * Noticed we are not using a forge registry because there is none for configured structures.
     *
     * We can register configured structures at any time before a world is clicked on and made.
     * But the best time to register configured features by code is honestly to do it in FMLCommonSetupEvent.
     */
    public static void registerConfiguredStructures(
        String structureMame,
        RegistryObject<Structure<NoFeatureConfig>> structure
    ) {
        LOGGER.debug("registerConfiguredStructures");
        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(AutomataMod.MOD_ID, structureMame), structure.get().configured(IFeatureConfig.NONE));
    }

}
