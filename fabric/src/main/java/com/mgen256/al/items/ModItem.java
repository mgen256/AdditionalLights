package com.mgen256.al.items;

import com.mgen256.al.AdditionalLightsFabric;
import com.mgen256.al.items.ModItemCore;
import net.minecraft.world.item.Item;

public abstract class ModItem extends Item implements ModItemTrait {

    private final ModItemCore core;

    public ModItem(Properties settings, String name) {
        super(settings);
        this.core = new ModItemCore(name, AdditionalLightsFabric.LOG_PROVIDER);
    }

    @Override
    public String getRegName() {
        return core.getRegName();
    }

    @Override
    public void log(String message) {
        core.log(message);
    }
}
