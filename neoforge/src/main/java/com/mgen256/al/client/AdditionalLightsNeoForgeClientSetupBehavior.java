package com.mgen256.al.client;

import com.mgen256.al.client.color.ItemTextureColorCacheCore;
import com.mgen256.al.client.color.ItemTextureColorCacheReloadListenerCore;
import com.mgen256.al.client.fire.FireSurfaceAtlasSourceCore;
import com.mgen256.al.client.gui.CreativeTabOrderToggleBehavior;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterSpriteSourcesEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;

public final class AdditionalLightsNeoForgeClientSetupBehavior {
    private static final Map<Screen, Button> ORDER_TOGGLE_BUTTONS = new WeakHashMap<>();
    private static final Map<Screen, Button> COLOR_SORT_BUTTONS = new WeakHashMap<>();

    private AdditionalLightsNeoForgeClientSetupBehavior() {}

    public static void registerCreativeTabWidgets(final FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.addListener(AdditionalLightsNeoForgeClientSetupBehavior::onScreenInitPost);
        NeoForge.EVENT_BUS.addListener(AdditionalLightsNeoForgeClientSetupBehavior::onScreenRenderPre);
        NeoForge.EVENT_BUS.addListener(AdditionalLightsNeoForgeClientSetupBehavior::onScreenClosing);
    }

    public static void registerClientReloadListeners(final AddClientReloadListenersEvent event) {
        event.addListener(ItemTextureColorCacheCore.RELOAD_LISTENER_ID, new ItemTextureColorCacheReloadListenerCore());
    }

    public static void registerSpriteSources(final RegisterSpriteSourcesEvent event) {
        event.register(FireSurfaceAtlasSourceCore.TYPE_ID, FireSurfaceAtlasSourceCore.CODEC);
    }

    private static void onScreenInitPost(final ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof CreativeModeInventoryScreen screen)) {
            return;
        }

        var orderToggleButton = CreativeTabOrderToggleBehavior.createButton(screen);
        var colorSortButton = CreativeTabOrderToggleBehavior.createColorSortButton(screen);

        event.addListener(orderToggleButton);
        event.addListener(colorSortButton);

        ORDER_TOGGLE_BUTTONS.put(event.getScreen(), orderToggleButton);
        COLOR_SORT_BUTTONS.put(event.getScreen(), colorSortButton);
    }

    private static void onScreenRenderPre(final ScreenEvent.Render.Pre event) {
        if (!(event.getScreen() instanceof CreativeModeInventoryScreen screen)) {
            return;
        }

        var orderToggleButton = ORDER_TOGGLE_BUTTONS.get(event.getScreen());
        if (orderToggleButton != null) {
            CreativeTabOrderToggleBehavior.updateButton(screen, orderToggleButton);
        }

        var colorSortButton = COLOR_SORT_BUTTONS.get(event.getScreen());
        if (colorSortButton != null) {
            CreativeTabOrderToggleBehavior.updateColorSortButton(screen, colorSortButton);
        }
    }

    private static void onScreenClosing(final ScreenEvent.Closing event) {
        ORDER_TOGGLE_BUTTONS.remove(event.getScreen());
        COLOR_SORT_BUTTONS.remove(event.getScreen());
    }
}
