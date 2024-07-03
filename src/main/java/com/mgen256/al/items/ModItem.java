package com.mgen256.al.items;

import com.mgen256.al.AdditionalLights;

import net.minecraft.item.Item;


public abstract class ModItem extends Item implements IModItem {
    
    public ModItem( Settings settings, String name )
    {
        super(settings);
        this.name = name;
    }

    protected String name;

    @Override
    public String getModRegistryName(){
        return name;
    }


    protected void Log( String string )
    {
        AdditionalLights.LOGGER.info(string);
    }
}