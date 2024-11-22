package com.mgen256.al.items;

import com.mgen256.al.AdditionalLights;

import net.minecraft.item.Item;


public abstract class ModItem extends Item {
    
    public ModItem( Settings settings )
    {
        super(settings);
    }

    protected void Log( String string )
    {
        AdditionalLights.LOGGER.info(string);
    }
}