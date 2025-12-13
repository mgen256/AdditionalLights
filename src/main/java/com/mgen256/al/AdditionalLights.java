package com.mgen256.al;

import com.mgen256.al.conditions.EnableFireCraftingCondition;
import com.mgen256.al.config.AdditionalLightsConfig;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;


@Mod(AdditionalLights.MOD_ID)
public class AdditionalLights {
    public static final String MOD_ID = "additional_lights";

    private static final Logger LOGGER = LogManager.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, MOD_ID);

    static {
        CONDITION_CODECS.register("enable_fire_crafting", () -> EnableFireCraftingCondition.CODEC);
    }

    public static Map<ModBlockList, DeferredHolder<Block, Block>> modBlocks = new LinkedHashMap<>();
    public static Map<ModBlockList, DeferredHolder<Item, BlockItem>> modBlockItems = new LinkedHashMap<>();
    public static Map<ModItemList, DeferredHolder<Item, Item>> modItems = new LinkedHashMap<>();
    public static Map<ModSoundList, DeferredHolder<SoundEvent, SoundEvent>> modSounds;    
    public static DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB;


    public AdditionalLights(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, AdditionalLightsConfig.SPEC);
        modEventBus.addListener(this::commonSetup);
        
        modSounds = new LinkedHashMap<>() {{
            put(ModSoundList.Change, SOUNDS.register("change", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(MOD_ID, "change"))));
            put(ModSoundList.Undo, SOUNDS.register("undo", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(MOD_ID, "undo"))));
            put(ModSoundList.Fire_Ignition_S, SOUNDS.register("fire_ignition_s", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(MOD_ID, "fire_ignition_s"))));
            put(ModSoundList.Fire_Ignition_L, SOUNDS.register("fire_ignition_l", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(MOD_ID, "fire_ignition_l"))));
            put(ModSoundList.Fire_Extinguish, SOUNDS.register("fire_extinguish", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(MOD_ID, "fire_extinguish"))));
        }};

        
        for (ModBlockList block : ModBlockList.values()) {
            block.register();
        }

        for (ModItemList item : ModItemList.values()) {
            item.register();
        }

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
        CONDITION_CODECS.register(modEventBus);
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
            
    static public ResourceKey<Block> createResourceKey(String name) {
        return ResourceKey.create(
                BuiltInRegistries.BLOCK.key(),
                Identifier.fromNamespaceAndPath(AdditionalLights.MOD_ID, name)
                );
    }

    static public ResourceKey<Item> createItemResourceKey(String name) {
        return ResourceKey.create(
                BuiltInRegistries.ITEM.key(),
                Identifier.fromNamespaceAndPath(AdditionalLights.MOD_ID, name)
                );
    }
}
