package com.mgen256.al.items;

import java.util.List;

import javax.annotation.Nullable;

import com.mgen256.al.AdditionalLights;
import com.mgen256.al.ModSoundList;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;


public abstract class Wand extends ModItem {
    
    protected Wand( Properties props ) {
        super( props );
    }

    private static Component txt_shift;
    private static Component txt_usage;
    private static Component txt_rightclick;
    private static Component txt_lefthand;
    private static Component txt_piglin;

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


    protected void playSound( Level level, Player player, SoundEvent sound, float volume )
    {
        level.playSound( player, player.blockPosition(), sound, SoundSource.PLAYERS, volume, 1.0f );
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        if( txt_shift == null )
        {
            if( I18n.exists("additional_lights.txt.shift") == false )
                return;
                
            txt_shift = new TranslatableComponent( "additional_lights.txt.shift" );
            txt_usage = new TranslatableComponent( "additional_lights.txt.usage" );
            txt_rightclick = new TranslatableComponent( "additional_lights.txt.item.soul_wand.rightclick" );
            txt_lefthand = new TranslatableComponent( "additional_lights.txt.item.soul_wand.lefthand" );
            txt_piglin = new TranslatableComponent( "additional_lights.txt.item.soul_wand.piglin" );
        }

        if ( Screen.hasShiftDown() )
        {
            tooltip.add( txt_usage );
            tooltip.add( txt_rightclick );
            tooltip.add( txt_lefthand );
            tooltip.add( txt_piglin );
        }
        else
        {
            tooltip.add( txt_shift );
        }
    }
}