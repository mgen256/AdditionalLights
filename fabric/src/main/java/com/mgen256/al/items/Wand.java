package com.mgen256.al.items;

import java.util.function.Consumer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import com.mgen256.al.client.ShiftKeyBehavior;

public abstract class Wand extends ModItem implements WandTrait {

    private static final WandCore.Translator<Component> TRANSLATOR = new WandCore.Translator<>() {
        @Override
        public boolean exists(String key) { return I18n.exists(key); }

        @Override
        public Component translate(String key) { return Component.translatable(key); }
    };

    private static final WandCore<Component> CORE = new WandCore<>();

    protected Wand(Properties settings, String name) {
        super(settings, name);
    }


    
    protected void playSound(Level world, Player player, SoundEvent sound, float volume) {
        world.playSound(player, player.blockPosition(), sound, SoundSource.PLAYERS, volume, 1.0f);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        CORE.appendTooltip(this, ShiftKeyBehavior.isShiftDown(), textConsumer, TRANSLATOR);
    }
}
