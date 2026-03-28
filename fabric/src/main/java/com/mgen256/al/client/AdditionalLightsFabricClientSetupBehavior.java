package com.mgen256.al.client;

import com.mgen256.al.client.color.ItemTextureColorCacheFabricReloadListenerBehavior;
import com.mgen256.al.client.fire.FireSurfaceAtlasSourceCore;
import com.mgen256.al.client.gui.CreativeTabOrderToggleBehavior;
import java.util.Map;
import java.util.WeakHashMap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.SpriteSourceRegistry;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.server.packs.PackType;

public final class AdditionalLightsFabricClientSetupBehavior implements ClientModInitializer {
    private static final Map<Screen, Button> ORDER_TOGGLE_BUTTONS = new WeakHashMap<>();
    private static final Map<Screen, Button> COLOR_SORT_BUTTONS = new WeakHashMap<>();

    @Override
    public void onInitializeClient() {
        SpriteSourceRegistry.register(FireSurfaceAtlasSourceCore.TYPE_ID, FireSurfaceAtlasSourceCore.CODEC);

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
                .registerReloadListener(new ItemTextureColorCacheFabricReloadListenerBehavior());

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof CreativeModeInventoryScreen creativeScreen)) {
                return;
            }

            Button orderToggleButton =
                    ORDER_TOGGLE_BUTTONS.computeIfAbsent(
                            screen,
                            unusedScreen -> {
                                Button created = CreativeTabOrderToggleBehavior.createButton(creativeScreen);
                                ScreenEvents.beforeExtract(screen).register(
                                        (currentScreen, graphics, mouseX, mouseY, tickDelta) ->
                                                CreativeTabOrderToggleBehavior.updateButton(creativeScreen, created));
                                ScreenEvents.remove(screen)
                                        .register(
                                                removedScreen -> {
                                                    ORDER_TOGGLE_BUTTONS.remove(removedScreen);
                                                    COLOR_SORT_BUTTONS.remove(removedScreen);
                                                });
                                return created;
                            });

            Button colorSortButton =
                    COLOR_SORT_BUTTONS.computeIfAbsent(
                            screen,
                            unusedScreen -> {
                                Button created = CreativeTabOrderToggleBehavior.createColorSortButton(creativeScreen);
                                ScreenEvents.beforeExtract(screen).register(
                                        (currentScreen, graphics, mouseX, mouseY, tickDelta) ->
                                                CreativeTabOrderToggleBehavior.updateColorSortButton(
                                                        creativeScreen, created));
                                return created;
                            });

            var buttons = Screens.getWidgets(screen);
            if (!buttons.contains(orderToggleButton)) {
                buttons.add(orderToggleButton);
            }
            if (!buttons.contains(colorSortButton)) {
                buttons.add(colorSortButton);
            }

            CreativeTabOrderToggleBehavior.updateButton(creativeScreen, orderToggleButton);
            CreativeTabOrderToggleBehavior.updateColorSortButton(creativeScreen, colorSortButton);
        });
    }
}
