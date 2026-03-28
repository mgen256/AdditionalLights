package com.mgen256.al;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import com.mgen256.al.CommonConstants;
import org.slf4j.LoggerFactory;
import com.mgen256.al.LogProvider;
import com.mgen256.al.AdditionalLightsCore;
import com.mgen256.al.config.AdditionalLightsConfig;
import com.mgen256.al.conditions.EnableFireCraftingCondition;

public class AdditionalLightsFabric implements ModInitializer {
    public static final LogProvider LOG_PROVIDER = LoggerFactory.getLogger(CommonConstants.MOD_ID)::info;

    private static final AdditionalLightsCore<Block, BlockItem, SoundEvent> CORE =
            new AdditionalLightsCore<>();

    private static final CreativeModeTab ITEM_GROUP =
            FabricCreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.get(FloorTorchSpec.ALTorch_Oak)))
                    .title(AdditionalLightsCreativeTabCore.title())
                    .hideTitle()
                    .backgroundTexture(AdditionalLightsCreativeTabCore.searchBackgroundTexture())
                    .displayItems(
                            (context, entries) ->
                                    AdditionalLightsCreativeTabCore.collectDisplayItems(
                                                    ModItems::get, ModBlocks::getBlockItem)
                                            .forEach(entries::accept))
                    .build();

    @Override
    public void onInitialize() {
        LOG_PROVIDER.log("onInitialize start");
        AdditionalLightsConfig.load();
        ResourceConditions.register(EnableFireCraftingCondition.TYPE);
        registerSounds();

        ModBlocks.registerAll();
        ModItems.registerAll();

        Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "al"),
                ITEM_GROUP);

        LOG_PROVIDER.log("onInitialize complete");
    }

    private void registerSounds() {
        for (final ModSoundList key : ModSoundList.values()) {
            final String name = key.getFileName();
            final Identifier id = Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, name);
            final SoundEvent event = SoundEvent.createVariableRangeEvent(id);
            CORE.putSound(key, event);
            Registry.register(BuiltInRegistries.SOUND_EVENT, id, event);
        }
    }

    public static SoundEvent getSound(final ModSoundList key) {
        return CORE.getSound(key);
    }

    public static ResourceKey<Block> createRegistryKey(final String name) {
        return ResourceKey.create(
                Registries.BLOCK, Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, name));
    }

    public static ResourceKey<Item> createItemRegistryKey(final String name) {
        return ResourceKey.create(
                Registries.ITEM, Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, name));
    }
}
