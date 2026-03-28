package com.mgen256.al.mixin.client;

import com.mgen256.al.CommonConstants;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.SessionSearchTrees;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenSearchBarBehavior {
    @Shadow private static CreativeModeTab selectedTab;
    @Shadow private float scrollOffs;
    @Shadow private EditBox searchBox;
    @Shadow private boolean ignoreTextInput;
    @Shadow private java.util.Set<TagKey<Item>> visibleTags;

    @Shadow protected abstract void refreshSearchResults();
    @Shadow protected abstract void updateVisibleTags(String query);

    @Inject(method = "selectTab", at = @At("TAIL"))
    private void additionalLights$enableSearchBox(final CreativeModeTab tab, final CallbackInfo ci) {
        if (!additionalLights$isAdditionalLightsTab(tab) || this.searchBox == null) {
            return;
        }

        this.searchBox.setVisible(true);
        this.searchBox.setCanLoseFocus(false);
        this.searchBox.setFocused(true);
        refreshSearchResults();
    }

    @Redirect(
            method = "keyPressed",
            at =
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/item/CreativeModeTab;getType()Lnet/minecraft/world/item/CreativeModeTab$Type;"))
    private CreativeModeTab.Type additionalLights$redirectKeyPressedTabType(final CreativeModeTab tab) {
        if (additionalLights$isAdditionalLightsTab(tab)) {
            return CreativeModeTab.Type.SEARCH;
        }
        return tab.getType();
    }

    @Redirect(
            method = "charTyped",
            at =
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/item/CreativeModeTab;getType()Lnet/minecraft/world/item/CreativeModeTab$Type;"))
    private CreativeModeTab.Type additionalLights$redirectCharTypedTabType(final CreativeModeTab tab) {
        if (additionalLights$isAdditionalLightsTab(tab)) {
            return CreativeModeTab.Type.SEARCH;
        }
        return tab.getType();
    }

    @Inject(method = "refreshSearchResults", at = @At("HEAD"), cancellable = true)
    private void additionalLights$refreshSearchResultsForAdditionalLights(final CallbackInfo ci) {
        if (!additionalLights$isAdditionalLightsTab(selectedTab) || this.searchBox == null) {
            return;
        }

        var menu = ((CreativeModeInventoryScreen) (Object) this).getMenu();
        menu.items.clear();
        this.visibleTags.clear();

        String query = this.searchBox.getValue();
        if (query.isEmpty()) {
            menu.items.addAll(selectedTab.getDisplayItems());
            this.scrollOffs = 0.0F;
            menu.scrollTo(0.0F);
            ci.cancel();
            return;
        }

        Minecraft client = Minecraft.getInstance();
        ClientPacketListener connection = client != null ? client.getConnection() : null;
        if (connection == null) {
            this.scrollOffs = 0.0F;
            menu.scrollTo(0.0F);
            ci.cancel();
            return;
        }

        SessionSearchTrees trees = connection.searchTrees();
        SearchTree<ItemStack> searchTree;
        if (query.startsWith("#")) {
            query = query.substring(1);
            searchTree = trees.creativeTagSearch();
            updateVisibleTags(query);
        } else {
            searchTree = trees.creativeNameSearch();
        }

        for (ItemStack stack : searchTree.search(query.toLowerCase(Locale.ROOT))) {
            if (selectedTab.contains(stack)) {
                menu.items.add(stack);
            }
        }

        this.scrollOffs = 0.0F;
        menu.scrollTo(0.0F);
        ci.cancel();
    }

    private static boolean additionalLights$isAdditionalLightsTab(final CreativeModeTab tab) {
        if (tab == null) {
            return false;
        }

        Identifier key = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
        if (key == null) {
            return false;
        }

        return CommonConstants.MOD_ID.equals(key.getNamespace());
    }
}
