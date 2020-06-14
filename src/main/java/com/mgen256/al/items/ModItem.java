package com.mgen256.al.items;

import com.mgen256.al.AdditionalLights;

import net.minecraft.item.Item;

public class ModItem extends Item {
    
    public ModItem( Properties props )
    {
        super(props);
    }


    protected void Log( String string )
    {
        AdditionalLights.Log(string);
    }
}