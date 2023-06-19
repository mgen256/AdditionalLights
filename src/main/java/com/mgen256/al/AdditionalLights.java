package com.mgen256.al;

import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mgen256.al.items.SoulWand;

import java.util.LinkedHashMap;
import java.util.Map;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(AdditionalLights.MOD_ID)
public class AdditionalLights {
    public static final String MOD_ID = "additional_lights";

    private static final Logger LOGGER = LogManager.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static Map<ModBlockList, RegistryObject<Block>> modBlocks= new LinkedHashMap<ModBlockList, RegistryObject<Block>>();
    public static Map<ModBlockList, RegistryObject<BlockItem>> modBlockItems = new LinkedHashMap<ModBlockList, RegistryObject<BlockItem>>();
    public static Map<ModItemList, RegistryObject<Item>> modItems = new LinkedHashMap<ModItemList, RegistryObject<Item>>();
    public static Map<ModSoundList, RegistryObject<SoundEvent>> modSounds;    
    public static RegistryObject<CreativeModeTab> CREATIVE_TAB;

    static {
        modSounds = new LinkedHashMap<ModSoundList, RegistryObject<SoundEvent>>(){
            private static final long serialVersionUID = 4L;
            {
                put( ModSoundList.Change, SOUNDS.register( "change", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation( MOD_ID, "change" ) )));
                put( ModSoundList.Undo, SOUNDS.register( "undo",  ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation( MOD_ID, "undo" ) ) ) );
                put( ModSoundList.Fire_Ignition_S, SOUNDS.register( "fire_ignition_s", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation( MOD_ID, "fire_ignition_s" ) ) ) );
                put( ModSoundList.Fire_Ignition_L, SOUNDS.register( "fire_ignition_l", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation( MOD_ID, "fire_ignition_l" ) ) ) );
                put( ModSoundList.Fire_Extinguish, SOUNDS.register( "fire_extinguish", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation( MOD_ID, "fire_extinguish" ) ) ) );
            }};

        for (ModBlockList block : ModBlockList.values()) {
            block.register();
        }

        modItems.put( ModItemList.SoulWand, ITEMS.register( "soul_wand", () -> new SoulWand()));
    }

    public AdditionalLights() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        
        CREATIVE_TAB = CREATIVE_MODE_TABS.register("creative_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("Additional Lights"))
        .icon(() -> new ItemStack(modBlockItems.get( ModBlockList.ALTorch_Oak ).get() ))
        .withSearchBar()
        .hideTitle()
        .displayItems(( param, output ) -> {
            modItems.forEach( (key, item) -> output.accept( item.get() ));
            modBlockItems.forEach( (key, item) -> output.accept( item.get() ));
            }).build());

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        SOUNDS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
     
    private void commonSetup(final FMLCommonSetupEvent event) {
        for (ModBlockList block : ModBlockList.values()) {
            block.init();
        }
    }

    public static void Log(String message) {
        LOGGER.info(MOD_ID + "::" + message);
    }

    public static Block getBlock( ModBlockList key )
    {
        return modBlocks.get( key ).get();
    }

    public static BlockItem getBlockItem( ModBlockList key )
    {
        return modBlockItems.get( key ).get();
    }


    public static SoundEvent getSound( ModSoundList key )
    {
        return modSounds.get( key ).get();
    }
}
