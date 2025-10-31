package com.mgen256.al.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public abstract class Wand extends ModItem {
    
    protected Wand(Settings settings) {
        super(settings);
    }

    protected void playSound(World world, PlayerEntity player, SoundEvent sound, float volume) {
        world.playSound(player, player.getBlockPos(), sound, SoundCategory.PLAYERS, volume, 1.0f);
    }
}
