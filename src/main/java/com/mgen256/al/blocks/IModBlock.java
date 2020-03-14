package com.mgen256.al.blocks;

import net.minecraft.item.BlockItem;


public interface IModBlock 
{
    void init();
    String getName();
    BlockItem getBlockItem();

    default boolean notRequireItemRegistration(){
        return false;
    }
}