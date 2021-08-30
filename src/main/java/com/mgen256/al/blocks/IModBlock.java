package com.mgen256.al.blocks;

import net.minecraft.world.item.BlockItem;

public interface IModBlock 
{
    void init();
    String getModRegistryName();
    BlockItem getBlockItem();

    default boolean notRequireItemRegistration(){
        return false;
    }

    default void setRenderLayer(){
    }
}