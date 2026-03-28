package com.mgen256.al.client.color;

import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class ItemTextureColorCacheReloadListenerCore
        extends SimplePreparableReloadListener<Map<Identifier, ItemTextureColorSpec>> {

    @Override
    protected Map<Identifier, ItemTextureColorSpec> prepare(
            final ResourceManager resourceManager, final ProfilerFiller profiler) {
        return ItemTextureColorCacheCore.build(resourceManager);
    }

    @Override
    protected void apply(
            final Map<Identifier, ItemTextureColorSpec> prepared,
            final ResourceManager resourceManager,
            final ProfilerFiller profiler) {
        ItemTextureColorCacheCore.apply(prepared);
    }
}
