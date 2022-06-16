package com.mgen256.al.items;

import com.mgen256.al.AdditionalLights;

import net.minecraft.world.item.Item;

public abstract class ModItem extends Item implements IModItem {
    
    public ModItem( Properties props, String _name )
    {
        super(props);
        name = _name;
    }

    protected String name;

    @Override
    public String getModRegistryName(){
        return name;
    }


    protected void Log( String string )
    {
        AdditionalLights.Log(string);
    }
}