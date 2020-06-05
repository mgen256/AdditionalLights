package com.mgen256.al;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mgen256.al.blocks.*;


@Mod(modid = AdditionalLights.MOD_ID, name = AdditionalLights.NAME, version = AdditionalLights.VERSION)
@Mod.EventBusSubscriber
public class AdditionalLights {

    public static final String MOD_ID = "additional_lights";
    public static final String NAME = "Additional Lights";
    public static final String VERSION = "@VERSION@";

    public static Map<ModBlockList, Block> modBlocks;

    private static Logger LOGGER = LogManager.getLogger();

    public static final CreativeTabs MYTAB = new CreativeTabs( MOD_ID ) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack( modBlocks.get( ModBlockList.ALTorch_Oak ));
        }
        @Override
		public boolean hasSearchBar() {
			return true;
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public void displayAllRelevantItems(NonNullList<ItemStack> itemStacks) 
        {
            itemStacks.clear();

            for (final Entry<ModBlockList, Block> entry : modBlocks.entrySet()) {
                final IModBlock block = (IModBlock) entry.getValue();
                if( block.notRequireItemRegistration())
                    continue;

                final Item item = block.getItem();
                //if (item.getCreativeTabs().contains(this))
                itemStacks.add(new ItemStack(item));
            }
        }

    }.setBackgroundImageName("item_search.png").setNoTitle();
    
    public AdditionalLights() {
    }
 
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        modBlocks = new LinkedHashMap<ModBlockList, Block>(){ 
            private static final long serialVersionUID = 2L;
            {
            put(ModBlockList.ALLamp_Acacia, new ALLamp(Blocks.PLANKS, "acacia_planks" ));
            put(ModBlockList.ALLamp_Birch, new ALLamp(Blocks.PLANKS, "birch_planks"));
            put(ModBlockList.ALLamp_Oak, new ALLamp(Blocks.PLANKS, "oak_planks"));
            put(ModBlockList.ALLamp_Dark_Oak, new ALLamp(Blocks.PLANKS, "dark_oak_planks"));
            put(ModBlockList.ALLamp_Spruce, new ALLamp(Blocks.PLANKS, "spruce_planks"));
            put(ModBlockList.ALLamp_Jungle, new ALLamp(Blocks.PLANKS, "jungle_planks"));
            put(ModBlockList.ALLamp_Stone, new ALLamp(Blocks.STONE, "stone"));
            put(ModBlockList.ALLamp_CobbleStone, new ALLamp(Blocks.COBBLESTONE, null));
            put(ModBlockList.ALLamp_Mossy_CobbleStone, new ALLamp(Blocks.MOSSY_COBBLESTONE, null));
            put(ModBlockList.ALLamp_End_Stone, new ALLamp(Blocks.END_STONE, null));
            put(ModBlockList.ALLamp_Glass, new ALLamp(Blocks.GLASS, null));
            put(ModBlockList.ALLamp_Iron, new ALLamp(Blocks.IRON_BLOCK, null));
            put(ModBlockList.ALLamp_Gold, new ALLamp(Blocks.GOLD_BLOCK, null));
            put(ModBlockList.ALLamp_Diamond, new ALLamp(Blocks.DIAMOND_BLOCK, null));
            put(ModBlockList.ALLamp_Ice, new ALLamp(Blocks.PACKED_ICE, null));
            put(ModBlockList.ALLamp_Pink_Wool, new ALLamp(Blocks.WOOL, "pink_wool"));
            put(ModBlockList.ALLamp_Magenta_Wool, new ALLamp(Blocks.WOOL, "magenta_wool"));
            put(ModBlockList.ALLamp_Nether_Bricks, new ALLamp(Blocks.NETHER_BRICK, "nether_bricks" ) );
            put(ModBlockList.ALLamp_Red_Nether_Bricks, new ALLamp(Blocks.RED_NETHER_BRICK, "red_nether_bricks"));
        

            put(ModBlockList.ALTorch_Acacia, new ALTorch(Blocks.PLANKS, "acacia_planks"));
            put(ModBlockList.ALTorch_Birch, new ALTorch(Blocks.PLANKS, "birch_planks"));
            put(ModBlockList.ALTorch_Oak, new ALTorch(Blocks.PLANKS, "oak_planks"));
            put(ModBlockList.ALTorch_Dark_Oak, new ALTorch(Blocks.PLANKS, "dark_oak_planks"));
            put(ModBlockList.ALTorch_Spruce, new ALTorch(Blocks.PLANKS, "spruce_planks"));
            put(ModBlockList.ALTorch_Jungle, new ALTorch(Blocks.PLANKS, "jungle_planks"));
            put(ModBlockList.ALTorch_Stone, new ALTorch(Blocks.STONE, "stone"));
            put(ModBlockList.ALTorch_CobbleStone, new ALTorch(Blocks.COBBLESTONE, null));
            put(ModBlockList.ALTorch_Mossy_CobbleStone, new ALTorch(Blocks.MOSSY_COBBLESTONE, null));
            put(ModBlockList.ALTorch_End_Stone, new ALTorch(Blocks.END_STONE, null));
            put(ModBlockList.ALTorch_Stone_Bricks, new ALTorch(Blocks.STONEBRICK, "stone_bricks"));
            put(ModBlockList.ALTorch_Mossy_Stone_Bricks, new ALTorch(Blocks.STONEBRICK, "mossy_stone_bricks"));
            put(ModBlockList.ALTorch_End_Stone_Bricks, new ALTorch(Blocks.STONEBRICK, "end_stone_bricks"));
            put(ModBlockList.ALTorch_Nether_Bricks, new ALTorch(Blocks.NETHER_BRICK, "nether_bricks"));
            put(ModBlockList.ALTorch_Red_Nether_Bricks, new ALTorch(Blocks.RED_NETHER_BRICK, "red_nether_bricks"));
            put(ModBlockList.ALTorch_Glass, new ALTorch(Blocks.GLASS, null));
            put(ModBlockList.ALTorch_Iron, new ALTorch(Blocks.IRON_BLOCK, null));
            put(ModBlockList.ALTorch_Gold, new ALTorch(Blocks.GOLD_BLOCK, null));
            put(ModBlockList.ALTorch_Diamond, new ALTorch(Blocks.DIAMOND_BLOCK, null));
            put(ModBlockList.ALTorch_Ice, new ALTorch(Blocks.PACKED_ICE, null));
            put(ModBlockList.ALTorch_Pink_Wool, new ALTorch(Blocks.WOOL, "pink_wool"));
            put(ModBlockList.ALTorch_Magenta_Wool, new ALTorch(Blocks.WOOL, "magenta_wool"));


            put(ModBlockList.StandingTorch_S_Stone_Bricks, new StandingTorch_S(Blocks.STONEBRICK, "stone_bricks"));
            put(ModBlockList.StandingTorch_S_Mossy_Stone_Bricks, new StandingTorch_S(Blocks.STONEBRICK, "mossy_stone_bricks"));
            put(ModBlockList.StandingTorch_S_End_Stone_Bricks, new StandingTorch_S(Blocks.END_BRICKS, "end_stone_bricks"));
            put(ModBlockList.StandingTorch_S_Nether_Bricks, new StandingTorch_S(Blocks.NETHER_BRICK, "nether_bricks"));
            put(ModBlockList.StandingTorch_S_Red_Nether_Bricks, new StandingTorch_S(Blocks.RED_NETHER_BRICK, "red_nether_bricks"));
            put(ModBlockList.StandingTorch_S_Polished_Andesite, new StandingTorch_S(Blocks.STONE, "polished_andesite"));
            put(ModBlockList.StandingTorch_S_Polished_Diorite, new StandingTorch_S(Blocks.STONE, "polished_diorite"));
            put(ModBlockList.StandingTorch_S_Polished_Granite, new StandingTorch_S(Blocks.STONE, "polished_granite"));
            put(ModBlockList.StandingTorch_S_Stone, new StandingTorch_S(Blocks.STONE, "stone"));
            put(ModBlockList.StandingTorch_S_CobbleStone, new StandingTorch_S(Blocks.COBBLESTONE, null));
            put(ModBlockList.StandingTorch_S_Mossy_CobbleStone, new StandingTorch_S(Blocks.MOSSY_COBBLESTONE, null));
            put(ModBlockList.StandingTorch_S_End_Stone, new StandingTorch_S(Blocks.END_STONE, null));
            put(ModBlockList.StandingTorch_S_Iron, new StandingTorch_S(Blocks.IRON_BLOCK, null));
            put(ModBlockList.StandingTorch_S_Gold, new StandingTorch_S(Blocks.GOLD_BLOCK, null));
            put(ModBlockList.StandingTorch_S_Diamond, new StandingTorch_S(Blocks.DIAMOND_BLOCK, null));
            put(ModBlockList.StandingTorch_S_Ice, new StandingTorch_S(Blocks.PACKED_ICE, null));
            put(ModBlockList.StandingTorch_S_Pink_Wool, new StandingTorch_S(Blocks.WOOL, "pink_wool"));
            put(ModBlockList.StandingTorch_S_Magenta_Wool, new StandingTorch_S(Blocks.WOOL, "magenta_wool"));


            put(ModBlockList.StandingTorch_L_Stone_Bricks, new StandingTorch_L(Blocks.STONEBRICK, "stone_bricks"));
            put(ModBlockList.StandingTorch_L_Mossy_Stone_Bricks, new StandingTorch_L(Blocks.STONEBRICK, "mossy_stone_bricks"));
            put(ModBlockList.StandingTorch_L_End_Stone_Bricks, new StandingTorch_L(Blocks.END_BRICKS, "end_stone_bricks"));
            put(ModBlockList.StandingTorch_L_Nether_Bricks, new StandingTorch_L(Blocks.NETHER_BRICK, "nether_bricks"));
            put(ModBlockList.StandingTorch_L_Red_Nether_Bricks, new StandingTorch_L(Blocks.RED_NETHER_BRICK, "red_nether_bricks"));
            put(ModBlockList.StandingTorch_L_Polished_Andesite, new StandingTorch_L(Blocks.STONE, "polished_andesite"));
            put(ModBlockList.StandingTorch_L_Polished_Diori, new StandingTorch_L(Blocks.STONE, "polished_diorite"));
            put(ModBlockList.StandingTorch_L_Polished_Granite, new StandingTorch_L(Blocks.STONE, "polished_granite"));
            put(ModBlockList.StandingTorch_L_Stone, new StandingTorch_L(Blocks.STONE, "stone"));
            put(ModBlockList.StandingTorch_L_CobbleStone, new StandingTorch_L(Blocks.COBBLESTONE, null));
            put(ModBlockList.StandingTorch_L_Mossy_CobbleStone, new StandingTorch_L(Blocks.MOSSY_COBBLESTONE, null));
            put(ModBlockList.StandingTorch_L_End_Stone, new StandingTorch_L(Blocks.END_STONE, null));
            put(ModBlockList.StandingTorch_L_Iron, new StandingTorch_L(Blocks.IRON_BLOCK, null));
            put(ModBlockList.StandingTorch_L_Gold, new StandingTorch_L(Blocks.GOLD_BLOCK, null));
            put(ModBlockList.StandingTorch_L_Diamond, new StandingTorch_L(Blocks.DIAMOND_BLOCK, null));
            put(ModBlockList.StandingTorch_L_Ice, new StandingTorch_L(Blocks.PACKED_ICE, null));
            put(ModBlockList.StandingTorch_L_Pink_Wool, new StandingTorch_L(Blocks.WOOL, "pink_wool"));
            put(ModBlockList.StandingTorch_L_Magenta_Wool, new StandingTorch_L(Blocks.WOOL, "magenta_wool"));


            put(ModBlockList.FirePit_S_Stone_Bricks, new FirePit_S(Blocks.STONEBRICK, "stone_bricks"));
            put(ModBlockList.FirePit_S_Mossy_Stone_Bricks, new FirePit_S(Blocks.STONEBRICK, "mossy_stone_bricks"));
            put(ModBlockList.FirePit_S_End_Stone_Bricks, new FirePit_S(Blocks.END_BRICKS, "end_stone_bricks"));
            put(ModBlockList.FirePit_S_Nether_Bricks, new FirePit_S(Blocks.NETHER_BRICK, "nether_bricks"));
            put(ModBlockList.FirePit_S_Red_Nether_Bricks, new FirePit_S(Blocks.RED_NETHER_BRICK, "red_nether_bricks"));
            put(ModBlockList.FirePit_S_Polished_Andesite, new FirePit_S(Blocks.STONE, "polished_andesite"));
            put(ModBlockList.FirePit_S_Polished_Diori, new FirePit_S(Blocks.STONE, "polished_diorite"));
            put(ModBlockList.FirePit_S_Polished_Granite, new FirePit_S(Blocks.STONE, "polished_granite"));
            put(ModBlockList.FirePit_S_Stone, new FirePit_S(Blocks.STONE, "stone"));
            put(ModBlockList.FirePit_S_CobbleStone, new FirePit_S(Blocks.COBBLESTONE, null));
            put(ModBlockList.FirePit_S_Mossy_CobbleStone, new FirePit_S(Blocks.MOSSY_COBBLESTONE, null));
            put(ModBlockList.FirePit_S_End_Stone, new FirePit_S(Blocks.END_STONE, null));
            put(ModBlockList.FirePit_S_Iron, new FirePit_S(Blocks.IRON_BLOCK, null));
            put(ModBlockList.FirePit_S_Gold, new FirePit_S(Blocks.GOLD_BLOCK, null));
            put(ModBlockList.FirePit_S_Diamond, new FirePit_S(Blocks.DIAMOND_BLOCK, null));
            put(ModBlockList.FirePit_S_Ice, new FirePit_S(Blocks.PACKED_ICE, null));
            put(ModBlockList.FirePit_S_Pink_Wool, new FirePit_S(Blocks.WOOL, "pink_wool"));
            put(ModBlockList.FirePit_S_Magenta_Wool, new FirePit_S(Blocks.WOOL, "magenta_wool"));


            put(ModBlockList.FirePit_L_Stone_Bricks, new FirePit_L(Blocks.STONEBRICK, "stone_bricks"));
            put(ModBlockList.FirePit_L_Mossy_Stone_Bricks, new FirePit_L(Blocks.STONEBRICK, "mossy_stone_bricks"));
            put(ModBlockList.FirePit_L_End_Stone_Bricks, new FirePit_L(Blocks.END_BRICKS, "end_stone_bricks"));
            put(ModBlockList.FirePit_L_Nether_Bricks, new FirePit_L(Blocks.NETHER_BRICK, "nether_bricks"));
            put(ModBlockList.FirePit_L_Red_Nether_Bricks, new FirePit_L(Blocks.RED_NETHER_BRICK, "red_nether_bricks"));
            put(ModBlockList.FirePit_L_Polished_Andesite, new FirePit_L(Blocks.STONE, "polished_andesite"));
            put(ModBlockList.FirePit_L_Polished_Diorite, new FirePit_L(Blocks.STONE, "polished_diorite"));
            put(ModBlockList.FirePit_L_Polished_Granite, new FirePit_L(Blocks.STONE, "polished_granite"));
            put(ModBlockList.FirePit_L_Stone, new FirePit_L(Blocks.STONE, "stone"));
            put(ModBlockList.FirePit_L_CobbleStone, new FirePit_L(Blocks.COBBLESTONE, null));
            put(ModBlockList.FirePit_L_Mossy_CobbleStone, new FirePit_L(Blocks.MOSSY_COBBLESTONE, null));
            put(ModBlockList.FirePit_L_End_Stone, new FirePit_L(Blocks.END_STONE, null));
            put(ModBlockList.FirePit_L_Iron, new FirePit_L(Blocks.IRON_BLOCK, null));
            put(ModBlockList.FirePit_L_Gold, new FirePit_L(Blocks.GOLD_BLOCK, null));
            put(ModBlockList.FirePit_L_Diamond, new FirePit_L(Blocks.DIAMOND_BLOCK, null));
            put(ModBlockList.FirePit_L_Ice, new FirePit_L(Blocks.PACKED_ICE, null));
            put(ModBlockList.FirePit_L_Pink_Wool, new FirePit_L(Blocks.WOOL, "pink_wool"));
            put(ModBlockList.FirePit_L_Magenta_Wool, new FirePit_L(Blocks.WOOL, "magenta_wool"));


            put(ModBlockList.Fire_For_StandingTorch_S, new Fire(FireBlockList.standing_torch_s));
            put(ModBlockList.Fire_For_StandingTorch_L, new Fire(FireBlockList.standing_torch_l));
            put(ModBlockList.Fire_For_FirePit_S, new Fire(FireBlockList.fire_pit_s));
            put(ModBlockList.Fire_For_FirePit_L, new Fire(FireBlockList.fire_pit_l));
            
        }
        };
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }

    public static void Log(final String message) {
        LOGGER.info(MOD_ID + "::" + message);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (final Entry<ModBlockList, Block> entry : modBlocks.entrySet()){
            IModBlock modblock = (IModBlock) entry.getValue();
            event.getRegistry().register(modblock.getItem());
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (final Entry<ModBlockList, Block> entry : modBlocks.entrySet()){
            event.getRegistry().register(entry.getValue());
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (final Entry<ModBlockList, Block> entry : modBlocks.entrySet()){
            IModBlock modblock = (IModBlock) entry.getValue();
            ModelResourceLocation resourceLocation = new ModelResourceLocation( new ResourceLocation(MOD_ID, modblock.getName() ), "inventory" );
            ModelLoader.setCustomModelResourceLocation(modblock.getItem(), 0, resourceLocation );
        }
    }
}
