package com.example.examplemod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ExampleMod.MODID, name = ExampleMod.NAME, version = ExampleMod.VERSION)
public class ExampleMod
{
    public static final String MODID = "voxsophon";
    public static final String NAME = "Voxsophon";
    public static final String VERSION = "0.0.1";

    private static Logger logger;

    public static ToolMaterial toolMaterial;
    public static Item voxsophon;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
//        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        voxsophon = new Voxsophon(Blocks.BRICK_BLOCK);
        voxsophon.setRegistryName("Voxsophon");
        voxsophon.setUnlocalizedName("Voxsophon");
        voxsophon.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
}
