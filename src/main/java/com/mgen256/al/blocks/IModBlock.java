package com.mgen256.al.blocks;

import net.minecraft.item.Item;

public interface IModBlock 
{
    String getName();

    Item getItem();

    default boolean notRequireItemRegistration(){
        return false;
    }
}