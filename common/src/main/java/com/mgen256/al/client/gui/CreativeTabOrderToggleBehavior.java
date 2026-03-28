package com.mgen256.al.client.gui;

import com.mgen256.al.CommonConstants;
import com.mgen256.al.client.color.ItemTextureColorCacheCore;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class CreativeTabOrderToggleBehavior {
    private static final int BUTTON_GAP = 2;
    private static final int SEARCH_BOX_BUTTON_GAP = 4;
    private static final int BUTTON_HEIGHT_OFFSET = 1;
    private static final int BUTTON_Y_OFFSET = 0;
    private static final ButtonVisualSpec ORDER_BUTTON_VISUAL_SPEC =
            new ButtonVisualSpec(Component.literal("⇵"), 0.0F, 0.5F, "gui.additional_lights.creative_tab.toggle_order");
    private static final ButtonVisualSpec COLOR_SORT_BUTTON_VISUAL_SPEC =
            new ButtonVisualSpec(Component.literal("C"), 0.5F, 0.5F, "gui.additional_lights.creative_tab.sort_by_color");

    private static final Map<CreativeModeInventoryScreen, List<ItemStack>> COLOR_SORT_ORIGINAL_ITEMS =
            new WeakHashMap<>();
    private static final Map<CreativeModeInventoryScreen, List<Identifier>> SYNCED_ITEM_ORDER_IDS =
            new WeakHashMap<>();

    private static Field cachedSearchBoxField;
    private static Field cachedScrollOffsField;
    private static Field cachedSelectedTabField;

    private CreativeTabOrderToggleBehavior() {}

    public static Button createButton(CreativeModeInventoryScreen screen) {
        var button =
                createVisualButton(
                        ORDER_BUTTON_VISUAL_SPEC,
                        pressed -> toggleOrder(screen),
                        CreativeTabToggleStateCore::isReverseOrderEnabled);
        updateButton(screen, button);
        return button;
    }

    public static Button createColorSortButton(CreativeModeInventoryScreen screen) {
        var button =
                createVisualButton(
                        COLOR_SORT_BUTTON_VISUAL_SPEC,
                        pressed -> sortByColor(screen),
                        CreativeTabToggleStateCore::isColorSortEnabled);
        updateColorSortButton(screen, button);
        return button;
    }

    private static Button createVisualButton(
            final ButtonVisualSpec visualSpec,
            final Button.OnPress onPress,
            final BooleanSupplier selectedStateSupplier) {
        var button = new CreativeTabIconButtonCore(
                visualSpec.label(),
                onPress,
                selectedStateSupplier,
                visualSpec.textXOffset(),
                visualSpec.textYOffset());
        button.setTooltip(Tooltip.create(Component.translatable(visualSpec.tooltipKey())));
        return button;
    }

    public static void updateButton(CreativeModeInventoryScreen screen, Button button) {
        updateSearchAlignedButton(screen, button, 1);
    }

    public static void updateColorSortButton(CreativeModeInventoryScreen screen, Button button) {
        updateSearchAlignedButton(screen, button, 2);
    }

    private static void updateSearchAlignedButton(
            final CreativeModeInventoryScreen screen, final Button button, final int buttonIndexFromSearch) {
        var searchBox = getSearchBox(screen);
        var selectedTab = getSelectedTab();
        synchronizeSavedToggleState(screen, selectedTab);
        boolean showButton = searchBox != null && searchBox.isVisible() && isAdditionalLightsTab(selectedTab);

        button.visible = showButton;
        button.active = showButton;
        if (!showButton) {
            return;
        }

        int buttonHeight = computeSearchAlignedButtonHeight(searchBox);
        int buttonWidth = computeSearchAlignedButtonWidth(buttonHeight);
        button.setSize(buttonWidth, buttonHeight);
        button.setX(computeSearchAlignedButtonX(searchBox, buttonWidth, buttonIndexFromSearch));
        button.setY(computeSearchAlignedButtonY(searchBox, buttonHeight));
    }

    private static int computeSearchAlignedButtonX(
            final EditBox searchBox, final int buttonWidth, final int buttonIndexFromSearch) {
        return searchBox.getX()
                - SEARCH_BOX_BUTTON_GAP
                - (buttonWidth * buttonIndexFromSearch)
                - (BUTTON_GAP * (buttonIndexFromSearch - 1));
    }

    private static int computeSearchAlignedButtonY(final EditBox searchBox, final int buttonHeight) {
        return searchBox.getY() + Math.floorDiv(searchBox.getHeight() - buttonHeight, 2) + BUTTON_Y_OFFSET;
    }

    private static int computeSearchAlignedButtonWidth(final int buttonHeight) {
        return buttonHeight;
    }

    private static int computeSearchAlignedButtonHeight(final EditBox searchBox) {
        return searchBox.getHeight() + BUTTON_HEIGHT_OFFSET;
    }

    private record ButtonVisualSpec(
            Component label, float textXOffset, float textYOffset, String tooltipKey) {}

    private static void toggleOrder(CreativeModeInventoryScreen screen) {
        if (!isAdditionalLightsTab(getSelectedTab())) {
            return;
        }

        var menu = screen.getMenu();
        if (menu == null || menu.items.isEmpty()) {
            return;
        }

        reorderByReversingTypeGroups(menu.items);
        boolean reversed = !CreativeTabToggleStateCore.isReverseOrderEnabled();
        CreativeTabToggleStateCore.setReverseOrderEnabled(reversed);
        rememberSyncedItemOrder(screen, menu.items);
        setScrollOffs(screen, 0.0F);
        menu.scrollTo(0.0F);
    }

    private static void sortByColor(final CreativeModeInventoryScreen screen) {
        if (!isAdditionalLightsTab(getSelectedTab())) {
            return;
        }

        var menu = screen.getMenu();
        if (menu == null || menu.items.isEmpty()) {
            return;
        }

        var currentItems = menu.items;
        var originalSnapshot = COLOR_SORT_ORIGINAL_ITEMS.get(screen);

        if (originalSnapshot != null) {
            if (haveSameItemIds(originalSnapshot, currentItems)) {
                restoreWithinTypeOrderFromSnapshot(currentItems, originalSnapshot);
                COLOR_SORT_ORIGINAL_ITEMS.remove(screen);
                CreativeTabToggleStateCore.setColorSortEnabled(false);
            } else {
                COLOR_SORT_ORIGINAL_ITEMS.remove(screen);
                COLOR_SORT_ORIGINAL_ITEMS.put(screen, new ArrayList<>(currentItems));
                reorderBySortingWithinTypeGroupsByColor(currentItems);
                CreativeTabToggleStateCore.setColorSortEnabled(true);
            }
        } else {
            COLOR_SORT_ORIGINAL_ITEMS.put(screen, new ArrayList<>(currentItems));
            reorderBySortingWithinTypeGroupsByColor(currentItems);
            CreativeTabToggleStateCore.setColorSortEnabled(true);
        }

        rememberSyncedItemOrder(screen, currentItems);
        setScrollOffs(screen, 0.0F);
        menu.scrollTo(0.0F);
    }

    private static void reorderByReversingTypeGroups(final List<ItemStack> items) {
        if (items.isEmpty()) {
            return;
        }

        var groups = new LinkedHashMap<BlockTypeGroup, List<ItemStack>>();

        for (ItemStack stack : items) {
            BlockTypeGroup group = classifyBlockGroup(stack);
            groups.computeIfAbsent(group, unused -> new ArrayList<>()).add(stack);
        }

        normalizeFlameOrder(groups.get(BlockTypeGroup.FIRE));
        var orderedKeys = new ArrayList<>(groups.keySet());
        Collections.reverse(orderedKeys);

        items.clear();
        for (BlockTypeGroup group : orderedKeys) {
            items.addAll(groups.get(group));
        }
    }

    private static void reorderBySortingWithinTypeGroupsByColor(final List<ItemStack> items) {
        if (items.isEmpty()) {
            return;
        }

        var groups = new LinkedHashMap<BlockTypeGroup, List<ItemStack>>();
        for (ItemStack stack : items) {
            BlockTypeGroup group = classifyBlockGroup(stack);
            groups.computeIfAbsent(group, unused -> new ArrayList<>()).add(stack);
        }

        for (var entry : groups.entrySet()) {
            if (entry.getKey() == BlockTypeGroup.FIRE) {
                sortFlameGroupByColor(entry.getValue());
            } else {
                sortGroupByColor(entry.getValue());
            }
        }

        items.clear();
        for (var groupItems : groups.values()) {
            items.addAll(groupItems);
        }
    }

    private static void restoreWithinTypeOrderFromSnapshot(
            final List<ItemStack> currentItems, final List<ItemStack> originalSnapshot) {
        var groupOrder = new ArrayList<BlockTypeGroup>();
        for (ItemStack stack : currentItems) {
            BlockTypeGroup group = classifyBlockGroup(stack);
            if (!groupOrder.contains(group)) {
                groupOrder.add(group);
            }
        }

        var snapshotGroups = new LinkedHashMap<BlockTypeGroup, List<ItemStack>>();
        for (ItemStack stack : originalSnapshot) {
            BlockTypeGroup group = classifyBlockGroup(stack);
            snapshotGroups.computeIfAbsent(group, unused -> new ArrayList<>()).add(stack);
        }
        normalizeFlameOrder(snapshotGroups.get(BlockTypeGroup.FIRE));

        currentItems.clear();
        for (BlockTypeGroup group : groupOrder) {
            var groupItems = snapshotGroups.remove(group);
            if (groupItems != null) {
                currentItems.addAll(groupItems);
            }
        }
        for (var remaining : snapshotGroups.values()) {
            currentItems.addAll(remaining);
        }
    }

    private static boolean haveSameItemIds(final List<ItemStack> left, final List<ItemStack> right) {
        if (left.size() != right.size()) {
            return false;
        }

        var counts = new HashMap<Identifier, Integer>();
        for (ItemStack stack : left) {
            Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (id == null) {
                continue;
            }
            counts.merge(id, 1, Integer::sum);
        }

        for (ItemStack stack : right) {
            Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (id == null) {
                return false;
            }

            Integer count = counts.get(id);
            if (count == null) {
                return false;
            }
            if (count == 1) {
                counts.remove(id);
            } else {
                counts.put(id, count - 1);
            }
        }

        return counts.isEmpty();
    }

    private static void sortFlameGroupByColor(final List<ItemStack> flameItems) {
        if (flameItems == null || flameItems.size() < 2) {
            return;
        }

        var fire = new ArrayList<ItemStack>();
        var soul = new ArrayList<ItemStack>();
        var other = new ArrayList<ItemStack>();

        for (ItemStack stack : flameItems) {
            Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            String path = id != null ? id.getPath() : "";
            if (path.startsWith("fire_for_")) {
                fire.add(stack);
            } else if (path.startsWith("soul_fire_for_")) {
                soul.add(stack);
            } else {
                other.add(stack);
            }
        }

        sortGroupByColor(fire);
        sortGroupByColor(soul);
        sortGroupByColor(other);

        flameItems.clear();
        flameItems.addAll(fire);
        flameItems.addAll(soul);
        flameItems.addAll(other);
    }

    private static void sortGroupByColor(final List<ItemStack> groupItems) {
        if (groupItems == null || groupItems.size() < 2) {
            return;
        }

        var sortKeyByItemId = new HashMap<Identifier, Long>();
        groupItems.sort(
                (left, right) ->
                        Long.compare(
                                getColorSortKey(left, sortKeyByItemId), getColorSortKey(right, sortKeyByItemId)));
    }

    private static long getColorSortKey(final ItemStack stack, final Map<Identifier, Long> sortKeyByItemId) {
        if (stack == null || stack.isEmpty()) {
            return Long.MAX_VALUE;
        }

        Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (id == null) {
            return Long.MAX_VALUE;
        }

        Long cached = sortKeyByItemId.get(id);
        if (cached != null) {
            return cached;
        }

        long key = CreativeTabColorSortCore.computeColorSortKey(ItemTextureColorCacheCore.getColorSpec(stack));
        sortKeyByItemId.put(id, key);
        return key;
    }

    private static void normalizeFlameOrder(final List<ItemStack> flameItems) {
        if (flameItems == null || flameItems.size() < 2) {
            return;
        }

        var fire = new ArrayList<ItemStack>();
        var soul = new ArrayList<ItemStack>();
        var other = new ArrayList<ItemStack>();

        for (ItemStack stack : flameItems) {
            Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            String path = id != null ? id.getPath() : "";
            if (path.startsWith("fire_for_")) {
                fire.add(stack);
            } else if (path.startsWith("soul_fire_for_")) {
                soul.add(stack);
            } else {
                other.add(stack);
            }
        }

        flameItems.clear();
        flameItems.addAll(fire);
        flameItems.addAll(soul);
        flameItems.addAll(other);
    }

    private static BlockTypeGroup classifyBlockGroup(final ItemStack stack) {
        Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (id == null || !CommonConstants.MOD_ID.equals(id.getNamespace())) {
            return BlockTypeGroup.OTHER;
        }

        if (!(stack.getItem() instanceof BlockItem)) {
            return BlockTypeGroup.ITEM;
        }

        String path = id.getPath();
        if (path.startsWith("standing_torch_s_")) {
            return BlockTypeGroup.STANDING_TORCH_S;
        }
        if (path.startsWith("standing_torch_l_")) {
            return BlockTypeGroup.STANDING_TORCH_L;
        }
        if (path.startsWith("al_lamp_") || path.startsWith("al_wall_lamp_")) {
            return BlockTypeGroup.LAMP;
        }
        if (path.startsWith("al_torch_") || path.startsWith("al_wall_torch_")) {
            return BlockTypeGroup.TORCH;
        }
        if (path.startsWith("fire_pit_s_")) {
            return BlockTypeGroup.FIRE_PIT_S;
        }
        if (path.startsWith("fire_pit_l_")) {
            return BlockTypeGroup.FIRE_PIT_L;
        }
        if (path.startsWith("fire_for_")) {
            return BlockTypeGroup.FIRE;
        }
        if (path.startsWith("soul_fire_for_")) {
            return BlockTypeGroup.FIRE;
        }

        return BlockTypeGroup.OTHER;
    }

    private enum BlockTypeGroup {
        ITEM,
        LAMP,
        TORCH,
        STANDING_TORCH_S,
        STANDING_TORCH_L,
        FIRE_PIT_S,
        FIRE_PIT_L,
        FIRE,
        OTHER
    }

    private static boolean isAdditionalLightsTab(CreativeModeTab tab) {
        if (tab == null) {
            return false;
        }

        Identifier key = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
        if (key == null) {
            return false;
        }

        return CommonConstants.MOD_ID.equals(key.getNamespace());
    }

    private static void synchronizeSavedToggleState(
            final CreativeModeInventoryScreen screen, final CreativeModeTab selectedTab) {
        if (!isAdditionalLightsTab(selectedTab)) {
            return;
        }

        final var menu = screen.getMenu();
        if (menu == null || menu.items.isEmpty()) {
            return;
        }

        final List<Identifier> currentOrder = snapshotItemOrder(menu.items);
        final List<Identifier> synchronizedOrder = SYNCED_ITEM_ORDER_IDS.get(screen);
        if (currentOrder.equals(synchronizedOrder)) {
            return;
        }

        boolean reordered = false;
        COLOR_SORT_ORIGINAL_ITEMS.remove(screen);
        if (CreativeTabToggleStateCore.isColorSortEnabled()) {
            COLOR_SORT_ORIGINAL_ITEMS.put(screen, new ArrayList<>(menu.items));
            reorderBySortingWithinTypeGroupsByColor(menu.items);
            reordered = true;
        }

        if (CreativeTabToggleStateCore.isReverseOrderEnabled()) {
            reorderByReversingTypeGroups(menu.items);
            reordered = true;
        }

        rememberSyncedItemOrder(screen, menu.items);
        if (reordered) {
            setScrollOffs(screen, 0.0F);
            menu.scrollTo(0.0F);
        }
    }

    private static void rememberSyncedItemOrder(
            final CreativeModeInventoryScreen screen, final List<ItemStack> items) {
        SYNCED_ITEM_ORDER_IDS.put(screen, snapshotItemOrder(items));
    }

    private static List<Identifier> snapshotItemOrder(final List<ItemStack> items) {
        final List<Identifier> itemIds = new ArrayList<>(items.size());
        for (ItemStack stack : items) {
            itemIds.add(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        }
        return itemIds;
    }

    private static EditBox getSearchBox(CreativeModeInventoryScreen screen) {
        try {
            if (cachedSearchBoxField == null) {
                cachedSearchBoxField = CreativeModeInventoryScreen.class.getDeclaredField("searchBox");
                cachedSearchBoxField.setAccessible(true);
            }
            return (EditBox) cachedSearchBoxField.get(screen);
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    private static CreativeModeTab getSelectedTab() {
        try {
            if (cachedSelectedTabField == null) {
                cachedSelectedTabField = CreativeModeInventoryScreen.class.getDeclaredField("selectedTab");
                cachedSelectedTabField.setAccessible(true);
            }
            return (CreativeModeTab) cachedSelectedTabField.get(null);
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    private static void setScrollOffs(CreativeModeInventoryScreen screen, float value) {
        try {
            if (cachedScrollOffsField == null) {
                cachedScrollOffsField = CreativeModeInventoryScreen.class.getDeclaredField("scrollOffs");
                cachedScrollOffsField.setAccessible(true);
            }
            cachedScrollOffsField.setFloat(screen, value);
        } catch (ReflectiveOperationException ignored) {
        }
    }
}
