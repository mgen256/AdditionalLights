package com.mgen256.al.blocks;

import net.minecraft.item.Item;

public interface IModBlock 
{
    void init();
    String getName();

    Item getItem();

    default boolean notRequireItemRegistration(){
        return false;
    }
}