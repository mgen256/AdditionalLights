package com.mgen256.al.items;

import java.util.function.Consumer;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public abstract class Wand extends ModItem {
    
    protected Wand( Settings settings ) {
        super( settings );
    }

    private static Text txt_shift;
    private static Text txt_usage;
    private static Text txt_rightclick;
    private static Text txt_lefthand;
    private static Text txt_piglin;

    
    protected void playSound(World world, PlayerEntity player, SoundEvent sound, float volume) {
        world.playSound(player, player.getBlockPos(), sound, SoundCategory.PLAYERS, volume, 1.0f);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        if (txt_shift == null) {
            if (!I18n.hasTranslation("additional_lights.txt.shift")) {
                return;
            }
                
            txt_shift = Text.translatable("additional_lights.txt.shift");
            txt_usage = Text.translatable("additional_lights.txt.usage");
            txt_rightclick = Text.translatable("additional_lights.txt.item.soul_wand.rightclick");
            txt_lefthand = Text.translatable("additional_lights.txt.item.soul_wand.lefthand");
            txt_piglin = Text.translatable("additional_lights.txt.item.soul_wand.piglin");
        }

        if (Screen.hasShiftDown()) {
            textConsumer.accept(txt_usage);
            textConsumer.accept(txt_rightclick);
            textConsumer.accept(txt_lefthand);
            textConsumer.accept(txt_piglin);
        } else {
            textConsumer.accept(txt_shift);
        }
    }
}