package com.mgen256.al;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

public final class AdditionalLightsCreativeTabCore {
    private static final String TITLE_KEY = "itemGroup.additional_lights";
    private static final Identifier SEARCH_BACKGROUND_TEXTURE =
            CreativeModeTab.createTextureLocation("item_search");

    private AdditionalLightsCreativeTabCore() {}

    public static Component title() {
        return Component.translatable(TITLE_KEY);
    }

    public static Identifier searchBackgroundTexture() {
        return SEARCH_BACKGROUND_TEXTURE;
    }

    public static <I extends ItemLike> List<I> collectDisplayItems(
            final Function<ModItemList, ? extends I> itemResolver,
            final Function<BlockSpec, ? extends I> blockItemResolver) {
        final List<I> displayItems = new ArrayList<>();

        for (final ModItemList itemKey : ModItemList.values()) {
            final I item = itemResolver.apply(itemKey);
            if (item != null) {
                displayItems.add(item);
            }
        }

        for (final BlockSpec blockKey : ModBlockList.values()) {
            final I blockItem = blockItemResolver.apply(blockKey);
            if (blockItem != null) {
                displayItems.add(blockItem);
            }
        }

        return List.copyOf(displayItems);
    }
}
