package com.mgen256.al;

import com.mgen256.al.conditions.EnableFireCraftingCondition;
import com.mgen256.al.config.AdditionalLightsConfig;
import com.mojang.serialization.MapCodec;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import com.mgen256.al.client.AdditionalLightsNeoForgeClientSetupBehavior;
import com.mgen256.al.CommonConstants;
import com.mgen256.al.AdditionalLightsCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import org.slf4j.LoggerFactory;
import com.mgen256.al.LogProvider;



@Mod(CommonConstants.MOD_ID)
public class AdditionalLightsNeoForge {

    public static final LogProvider LOG_PROVIDER =
            LoggerFactory.getLogger(CommonConstants.MOD_ID)::info;

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CommonConstants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CommonConstants.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, CommonConstants.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CommonConstants.MOD_ID);
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, CommonConstants.MOD_ID);

    static {
        CONDITION_CODECS.register("enable_fire_crafting", () -> EnableFireCraftingCondition.CODEC);
    }

    public static final AdditionalLightsCore<
            DeferredHolder<Block, Block>,
            DeferredHolder<Item, BlockItem>,
            DeferredHolder<SoundEvent, SoundEvent>> CORE = new AdditionalLightsCore<>();
    public static DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB;


    public AdditionalLightsNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, AdditionalLightsConfig.SPEC);
        LOG_PROVIDER.log("construct AdditionalLightsNeoForge");
        modEventBus.addListener(this::commonSetup);
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            modEventBus.addListener(AdditionalLightsNeoForgeClientSetupBehavior::registerCreativeTabWidgets);
            modEventBus.addListener(AdditionalLightsNeoForgeClientSetupBehavior::registerClientReloadListeners);
            modEventBus.addListener(AdditionalLightsNeoForgeClientSetupBehavior::registerSpriteSources);
        }

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        SOUNDS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        CONDITION_CODECS.register(modEventBus);

        for (var key : ModSoundList.values()) {
            var name = key.getFileName();
            var holder = SOUNDS.register(name,
                    () -> SoundEvent.createVariableRangeEvent(
                            Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, name)));
            CORE.putSound(key, holder);
        }


        ModBlocks.registerAll();

        ModItems.registerAll();

        CREATIVE_TAB = CREATIVE_MODE_TABS.register("creative_tab", () -> CreativeModeTab.builder()
        .title(AdditionalLightsCreativeTabCore.title())
        .icon(() -> {
            var item = ModBlocks.getBlockItem(FloorTorchSpec.ALTorch_Oak);
            return new ItemStack(item != null ? item : Items.TORCH);
        })
        .withSearchBar()
        .hideTitle()
        .displayItems((param, output) ->
                AdditionalLightsCreativeTabCore.collectDisplayItems(ModItems::get, ModBlocks::getBlockItem)
                        .forEach(output::accept))
        .build());

        LOG_PROVIDER.log("construct AdditionalLightsNeoForge complete");
    }
     
    private void commonSetup(final FMLCommonSetupEvent event) {
        ModBlocks.initAll();
    }

    public static void log(String message) {
        LOG_PROVIDER.log(CommonConstants.MOD_ID + "::" + message);
    }

    public static Block getBlock(BlockSpec key)
    {
        return CORE.getBlock(key).get();
    }

    public static BlockItem getBlockItem(BlockSpec key)
    {
        var holder = CORE.getBlockItem(key);
        return holder != null ? holder.get() : null;
    }

    public static SoundEvent getSound(ModSoundList key)
    {
        return CORE.getSound(key).get();
    }
            
    static public ResourceKey<Block> createResourceKey(String name) {
        return ResourceKey.create(
                BuiltInRegistries.BLOCK.key(),
                Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, name)
                );
    }

    static public ResourceKey<Item> createItemResourceKey(String name) {
        return ResourceKey.create(
                BuiltInRegistries.ITEM.key(),
                Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, name)
                );
    }
}
