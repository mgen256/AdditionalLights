package com.mgen256.al.client.color;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.Identifier;

public final class ItemTextureColorCacheFabricReloadListenerBehavior
        extends ItemTextureColorCacheReloadListenerCore implements IdentifiableResourceReloadListener {

    @Override
    public Identifier getFabricId() {
        return ItemTextureColorCacheCore.RELOAD_LISTENER_ID;
    }
}

