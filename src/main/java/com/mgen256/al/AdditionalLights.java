package com.mgen256.al;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mgen256.al.blocks.IModBlock;
import com.mgen256.al.blocks.ALLamp;


@Mod(modid = AdditionalLights.MOD_ID, name = AdditionalLights.NAME, version = AdditionalLights.VERSION)
public class AdditionalLights {

    public static final String MOD_ID = "additional_lights";
    public static final String NAME = "Additional Lights";
    public static final String VERSION = "@VERSION@";

    public static Map<ModBlockList, Block> modBlocks;

    //private static final Logger LOGGER = LogManager.getLogger();
    private static Logger LOGGER = LogManager.getLogger();
    /*
    static {
        final ItemGroup itemGroup = new ItemGroup(MOD_ID) {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(modBlocks.get(ModBlockList.ALTorch_Oak));
            }
            @Override
            public void fill(final NonNullList<ItemStack> itemStacks) {
                itemStacks.clear();

                for (final Entry<ModBlockList, Block> entry : modBlocks.entrySet()) {
                    final IModBlock block = (IModBlock) entry.getValue();
                    if( block.notRequireItemRegistration())
                        continue;

                    final Item item = block.getBlockItem();
                    //if (item.getCreativeTabs().contains(this))
                    itemStacks.add(new ItemStack(item));
                }
            }
        };
        ItemProps = new Item.Properties().group(itemGroup);
    }
*/
    public AdditionalLights() {
        // Register the setup method for modloading
        
        // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        
        // Register ourselves for server and other game events we are interested in
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        //MinecraftForge.EVENT_BUS.register(this);
    }
 
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }

    public static void Log(final String message) {
        LOGGER.info(MOD_ID + "::" + message);
    }

    public static void init2() {
        if (modBlocks != null)
            return;

        // init blocks
        modBlocks = new LinkedHashMap<ModBlockList, Block>(){ 
            private static final long serialVersionUID = 2L;
            {
            //put(ModBlockList.ALLamp_Acacia, new ALLamp(Blocks.ACACIA_PLANKS));
            put(ModBlockList.ALLamp_Stone, new ALLamp(Blocks.STONE));
            /*
            put(ModBlockList.ALLamp_Birch, new ALLamp(Blocks.BIRCH_PLANKS));
            */
       }
        };
        
        for (final Entry<ModBlockList, Block> entry : modBlocks.entrySet())
            ((IModBlock) entry.getValue()).init();
    }
/*
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        init2();

        for (final Entry<ModBlockList, Block> entry : modBlocks.entrySet()){
            IModBlock modblock = (IModBlock) entry.getValue();
            event.getRegistry().register(modblock.getItem());
        }
    }
 
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        init2();

        for (final Entry<ModBlockList, Block> entry : modBlocks.entrySet()){
            IModBlock modblock = (IModBlock) entry.getValue();
            ModelLoader.setCustomModelResourceLocation(modblock.getItem(), 0, new ModelResourceLocation(modblock.getName(), "inventory"));
        }
    }*/


    @Mod.EventBusSubscriber
    public static class ObjectRegistryHandler {
        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
            init2();

            for (final Entry<ModBlockList, Block> entry : modBlocks.entrySet()){
                IModBlock modblock = (IModBlock) entry.getValue();
                event.getRegistry().register(modblock.getItem());
            }
        }

        @SubscribeEvent
        public static void addBlocks(RegistryEvent.Register<Block> event) {
            init2();

            for (final Entry<ModBlockList, Block> entry : modBlocks.entrySet()){
                event.getRegistry().register(entry.getValue());
            }
        }

    
        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            init2();

        for (final Entry<ModBlockList, Block> entry : modBlocks.entrySet()){
            IModBlock modblock = (IModBlock) entry.getValue();
            ModelLoader.setCustomModelResourceLocation(modblock.getItem(), 0, new ModelResourceLocation( MOD_ID + ":" + modblock.getName(), "inventory"));
        }
        }
    }
/*
    public static void Log(final String message) {
        LOGGER.info(MOD_ID + "::" + message);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
           init();

            final IForgeRegistry<Item> itemRegistry = itemRegistryEvent.getRegistry();

            for (final Entry<ModBlockList, Block> entry : modBlocks.entrySet()) {
                final IModBlock block = (IModBlock) entry.getValue();
                if( block.notRequireItemRegistration() )
                    continue;
                itemRegistry.register(block.getBlockItem());
            }
        }

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            init();

            final IForgeRegistry<Block> blockRegistry = blockRegistryEvent.getRegistry();
            for (final Entry<ModBlockList, Block> entry : modBlocks.entrySet())
                blockRegistry.register(entry.getValue());
        }
    }
*/
}
