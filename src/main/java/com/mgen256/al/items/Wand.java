package com.mgen256.al.items;

import com.mgen256.al.AdditionalLights;
import com.mgen256.al.ModSoundList;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public abstract class Wand extends ModItem {
    
    public Wand( Properties props ) {
        super( props );
    }

    protected static class SoundEvents
    {
        static 
        {
            CHANGE = AdditionalLights.modSounds.get( ModSoundList.Change );
            UNDO = AdditionalLights.modSounds.get( ModSoundList.Undo );
        }

        public static SoundEvent CHANGE;
        public static SoundEvent UNDO;
    }


    protected void playSound( World worldIn, PlayerEntity playerIn, SoundEvent sound )
    {
        worldIn.playSound( playerIn, playerIn.getPosition(), sound, SoundCategory.PLAYERS, 1.0f, 1.0f );
    }
}