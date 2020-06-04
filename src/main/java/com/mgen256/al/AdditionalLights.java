package com.mgen256.al;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
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
import net.minecraftforge.registries.IForgeRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
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

    public static final CreativeTabs MYTAB = new CreativeTabs( NAME ) {
		@Override
		public ItemStack createIcon() {
            //ALTorch_Oak
			return new ItemStack( modBlocks.get( ModBlockList.ALTorch_Stone ));
        }
        @Override
		public boolean hasSearchBar() {
			return true;
		}
    };
    
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
            put(ModBlockList.ALLamp_Stone, new ALLamp(Blocks.STONE, null));
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
        
            
            put(ModBlockList.FirePit_L_Stone, new FirePit_L(Blocks.STONE, null ));


            put(ModBlockList.Fire_For_FirePit_L,new Fire(FireBlockList.fire_pit_l));
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
