package com.mgen256.al.items;

import java.util.List;

import javax.annotation.Nullable;

import com.mgen256.al.AdditionalLights;
import com.mgen256.al.ModSoundList;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.*;

public abstract class Wand extends ModItem {
    
    public Wand( Properties props ) {
        super( props );
    }

    private static StringTextComponent txt_shift;
    private static StringTextComponent txt_usage;
    private static StringTextComponent txt_rightclick;
    private static StringTextComponent txt_lefthand;
    private static StringTextComponent txt_piglin;

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


    protected void playSound( World worldIn, PlayerEntity playerIn, SoundEvent sound, float volume )
    {
        worldIn.playSound( playerIn, playerIn.getPosition(), sound, SoundCategory.PLAYERS, volume, 1.0f );
    }
    

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if( txt_shift == null )
        {
            if( I18n.hasKey("additional_lights.txt.shift") == false )
                return;
                
            txt_shift = new StringTextComponent( I18n.format( "additional_lights.txt.shift" ) );
            txt_usage = new StringTextComponent( I18n.format( "additional_lights.txt.usage" ) );
            txt_rightclick = new StringTextComponent( I18n.format( "additional_lights.txt.item.soul_wand.rightclick" ) );
            txt_lefthand = new StringTextComponent( I18n.format( "additional_lights.txt.item.soul_wand.lefthand" ) );
            txt_piglin = new StringTextComponent( I18n.format( "additional_lights.txt.item.soul_wand.piglin" ) );
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