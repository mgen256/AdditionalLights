package com.mgen256.al.items;

import com.mgen256.al.AdditionalLights;

import net.minecraft.world.item.Item;

public abstract class ModItem extends Item implements IModItem {
    
    public ModItem( Properties props, String name )
    {
        super(props);
        this.name = name;
    }

    protected String name;

    @Override
    public String getRegName(){
        return name;
    }


    protected void Log( String string )
    {
        AdditionalLights.Log(string);
    }
}