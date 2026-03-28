package com.mgen256.al.items;

import com.mgen256.al.AdditionalLightsNeoForge;
import com.mgen256.al.items.ModItemCore;

import net.minecraft.world.item.Item;

public abstract class ModItem extends Item implements ModItemTrait {

    private final ModItemCore core;

    public ModItem( Properties props, String name )
    {
        super(props);
        core = new ModItemCore(name, AdditionalLightsNeoForge.LOG_PROVIDER);
    }

    @Override
    public String getRegName(){
        return core.getRegName();
    }


    @Override
    public void log(String message) {
        core.log(message);
    }
}
