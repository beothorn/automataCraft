package br.com.isageek.automata;

import br.com.isageek.automata.automata.AutomataStartBaseEntityBlock;
import br.com.isageek.automata.automata.AutomataStartBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
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

    private final Map<String, RegistryObject<Block>> registeredBlocks = new HashMap<>();

    public AutomataMod() {
        final IEventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.register(this);
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        for (String blockName : AUTOMATA_BLOCK) {
            registerBlock(blockName, modEventBus);
        }
        // This is a mess, a lot of circular dependencies for BlockEntityType
        // Using AtomicReference is a way to make it lazy
        // Having to add this on top of DeferredRegister is madness
        // but the suggested solution everywhere is static fields, so this is slightly better
        AtomicReference<RegistryObject<BlockEntityType<?>>> typeReference = new AtomicReference<>();

        registerBlock("automata_start", modEventBus, () -> new AutomataStartBaseEntityBlock(typeReference, registeredBlocks));

        final DeferredRegister<BlockEntityType<?>> blockDeferredRegister = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITY_TYPES,
            AutomataMod.MOD_ID
        );
        RegistryObject<Block> automataStartBlockEntity = registeredBlocks.get(automata_start);
        RegistryObject<BlockEntityType<?>> automataStartType = blockDeferredRegister.register("automata_start",
                () -> BlockEntityType.Builder.of((p, s) -> new AutomataStartBlockEntity(typeReference, p, s), automataStartBlockEntity.get())
                        .build(null));
        typeReference.set(automataStartType);

        blockDeferredRegister.register(modEventBus);


        modEventBus.addListener(this::addCreative);


//        public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = REGISTRAR.register("example", () -> CreativeModeTab.builder()
//                // Set name of tab to display
//                .title(Component.translatable("item_group." + MOD_ID + ".example"))
//                // Set icon of creative tab
//                .icon(() -> new ItemStack(ITEM.get()))
//                // Add default items to tab
//                .displayItems((params, output) -> {
//                    output.accept(ITEM.get());
//                    output.accept(BLOCK.get());
//                })
//                .build()
//        );

        // https://docs.minecraftforge.net/en/latest/legacy/porting/

//https://gist.github.com/50ap5ud5/beebcf056cbdd3c922cc8993689428f4
        //final RegistryObject<BaseEntityBlock> automata_start = block("automata_start", AutomataMod.MOD_ID, modEventBus, AutomataStartBaseEntityBlock::new);
        // public static final BlockEntityType<BellBlockEntity> BELL = register("bell", BlockEntityType.Builder.of(BellBlockEntity::new, Blocks.BELL));
        // net.minecraft.world.level.block.entity.BlockEntityType <--- copy from here, leave comment later

        // net.minecraft.world.level.block.Blocks <------ copy from here, leave comment later

//        final DeferredRegister<BlockEntityType<?>> blockDeferredRegister =
//                DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AutomataMod.MOD_ID);
//        final RegistryObject<BlockEntityType<?>> block = blockDeferredRegister.register("automata_start_block_type",
//                () -> {
//                    return BlockEntityType.Builder.of(AutomataStartBlockEntity::new, automata_start.get());
//                });
//        blockDeferredRegister.register(modEventBus);

        /*
        final WorldController worldController = new WorldController(
                new Block[]{Blocks.AIR, Blocks.CAVE_AIR},
                automata.get(),
                automata_termination.get(),
                automata_start.get(),
                automata_air_placeholder.get(),
                automata_gravel_placeholder.get(),
                automata_red_sand_placeholder.get(),
                automata_sand_placeholder.get(),
                automata_water_placeholder.get(),
                automata_lava_placeholder.get(),
                automata_tnt_placeholder.get(),
                automata_bedrock_placeholder.get(),
                automata_y_rotation.get()
        );
        final LoadReplaceables initial = new LoadReplaceables();
        final SystemEntityClock entityClock = new SystemEntityClock();
         */
        // TODO: New ticker here
    }

    private void registerBlock(
        final String blockName,
        final IEventBus modEventBus
    ) {
        Supplier<Block> blockSupplier = () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
        registerBlock(blockName, modEventBus, blockSupplier);
    }

    private void registerBlock(String blockName, IEventBus modEventBus, Supplier<Block> blockSupplier) {
        final DeferredRegister<Block> blockDeferredRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, AutomataMod.MOD_ID);
        final RegistryObject<Block> block = blockDeferredRegister.register(blockName, blockSupplier);
        blockDeferredRegister.register(modEventBus);

        final DeferredRegister<Item> itemDeferredRegister = DeferredRegister.create(ForgeRegistries.ITEMS, AutomataMod.MOD_ID);
        itemDeferredRegister.register(blockName +"_item", () -> new BlockItem(block.get(), new Item.Properties()));
        itemDeferredRegister.register(modEventBus);

        registeredBlocks.put(blockName, block);
    }

    public void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            for (RegistryObject<Block> automataBlock : registeredBlocks.values()) {
                event.accept(automataBlock);
            }
        }
    }
}
