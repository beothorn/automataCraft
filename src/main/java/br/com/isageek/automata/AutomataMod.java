package br.com.isageek.automata;

import br.com.isageek.automata.automata.AutomataStartBlock;
import br.com.isageek.automata.automata.AutomataStartBlockEntity;
import br.com.isageek.automata.structures.NamedStructure;
import br.com.isageek.automata.structures.Rule30;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@Mod(AutomataMod.MOD_ID)
public class AutomataMod
{
    public static final String MOD_ID = "automata";

    public static final int MAX_SEARCH_RADIUS = 100;
    public static final int EXECUTE_MINIMAL_TICK_INTERVAL = 500;
    public static final int EXECUTE_THROTTLE_AFTER_AUTOMATA_COUNT = 8000;
    public final static long SEARCH_AGAIN_TIMEOUT = 3000;



    public static final String automata_start = "automata_start";

    // REGULAR BLOCKS

    public static final String automata = "automata";
    public static final String automata_termination = "automata_termination";
    public static final String automata_air_placeholder = "automata_air_placeholder";
    public static final String automata_water_placeholder = "automata_water_placeholder";
    public static final String automata_lava_placeholder = "automata_lava_placeholder";
    public static final String automata_tnt_placeholder = "automata_tnt_placeholder";
    public static final String automata_bedrock_placeholder = "automata_bedrock_placeholder";
    public static final String automata_gravel_placeholder = "automata_gravel_placeholder";
    public static final String automata_red_sand_placeholder = "automata_red_sand_placeholder";
    public static final String automata_sand_placeholder = "automata_sand_placeholder";
    public static final String automata_y_rotation = "automata_y_rotation";

    public static final String[] AUTOMATA_BLOCK = {
        automata,
        automata_termination,
        automata_air_placeholder,
        automata_water_placeholder,
        automata_lava_placeholder,
        automata_tnt_placeholder,
        automata_bedrock_placeholder,
        automata_gravel_placeholder,
        automata_red_sand_placeholder,
        automata_sand_placeholder,
        automata_y_rotation
    };

    // STRUCTURES

    public static final String rule30 = "r30";
    public static final String lavaTrap = "lavatrap";
    public static final String rainbow = "rainbow";

    public static final NamedStructure[] STRUCTURES = {
    };

    private final Map<String, RegistryObject<Block>> registeredBlocks = new HashMap<>();

    public AutomataMod() {
        final IEventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.register(this);
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        for (String blockName : AUTOMATA_BLOCK) {
            registerBlock(blockName, modEventBus);
        }
        registerAutomataStartBlockWithBlockEntity(modEventBus);

//        for (String structureName : STRUCTURES) {
//            registerStructure(structureName, modEventBus);
//        }

//        registerStructure(rule30, modEventBus, () -> new Rule30().type());

        // https://gist.github.com/GentlemanRevvnar/98a8f191f46d28f63592672022c41497
        // https://minecraft.wiki/w/Tutorials/Custom_structures
        // https://github.com/TelepathicGrunt/StructureTutorialMod?tab=readme-ov-file
        DeferredRegister<StructureType<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(Registries.STRUCTURE_TYPE, AutomataMod.MOD_ID);
        DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);

        modEventBus.addListener(this::addAllBlocksToRedstoneTabOnCreative);
    }

    private void registerAutomataStartBlockWithBlockEntity(IEventBus modEventBus) {
        // This is a mess, a lot of circular dependencies for BlockEntityType
        // BlockEntityType supplier needs AutomataStartBlockEntity
        // AutomataStartBlockEntity supplier needs BlockEntityType
        // AutomataStartBaseEntityBlock supplier needs AutomataStartBlockEntity
        // Using AtomicReference is a way to make it lazy
        // Having to add this on top of DeferredRegister is madness
        // but the suggested solution everywhere is static fields, so this is slightly better
        AtomicReference<RegistryObject<BlockEntityType<?>>> blockEntityTypeHolder = new AtomicReference<>();

        registerBlock(
            automata_start,
                modEventBus,
            () -> new AutomataStartBlock(blockEntityTypeHolder, registeredBlocks)
        );

        final DeferredRegister<BlockEntityType<?>> blockDeferredRegister = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITY_TYPES,
            AutomataMod.MOD_ID
        );
        RegistryObject<Block> automataStartBlockEntity = registeredBlocks.get(automata_start);
        RegistryObject<BlockEntityType<?>> automataStartType = blockDeferredRegister.register(
            automata_start+"_entity_type",
            () -> BlockEntityType.Builder.of(
                (pos, state) -> new AutomataStartBlockEntity(blockEntityTypeHolder, pos, state),
                automataStartBlockEntity.get()
            )
            .build(null));
        blockEntityTypeHolder.set(automataStartType);

        blockDeferredRegister.register(modEventBus);
    }

    private void registerBlock(
        final String blockName,
        final IEventBus modEventBus
    ) {
        Supplier<Block> blockSupplier = () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
        registerBlock(blockName, modEventBus, blockSupplier);
    }

    private void registerBlock(
        final String blockName,
        final IEventBus modEventBus,
        final Supplier<Block> blockSupplier
    ) {
        final DeferredRegister<Block> blockDeferredRegister = DeferredRegister.create(
            ForgeRegistries.BLOCKS,
            AutomataMod.MOD_ID
        );
        final RegistryObject<Block> block = blockDeferredRegister.register(blockName, blockSupplier);
        blockDeferredRegister.register(modEventBus);

        final DeferredRegister<Item> itemDeferredRegister = DeferredRegister.create(
            ForgeRegistries.ITEMS,
            AutomataMod.MOD_ID
        );
        itemDeferredRegister.register(
            blockName +"_item",
            () -> new BlockItem(block.get(), new Item.Properties())
        );
        itemDeferredRegister.register(modEventBus);

        registeredBlocks.put(blockName, block);
    }

    public void addAllBlocksToRedstoneTabOnCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            for (RegistryObject<Block> automataBlock : registeredBlocks.values()) {
                event.accept(automataBlock);
            }
        }
    }

    private void registerStructure(
        final String structureName,
        final IEventBus modEventBus,
        final Supplier<StructureType<?>> structureSupplier
    ) {
        final DeferredRegister<StructureType<?>> structureDeferredRegister = DeferredRegister.create(
            Registries.STRUCTURE_TYPE,
            AutomataMod.MOD_ID
        );
        final RegistryObject<StructureType<?>> structure = structureDeferredRegister.register(structureName, structureSupplier);
        structureDeferredRegister.register(modEventBus);

        modEventBus.addListener(EventPriority.NORMAL, (GatherDataEvent event) -> System.out.println("xxx"));
    }
}
