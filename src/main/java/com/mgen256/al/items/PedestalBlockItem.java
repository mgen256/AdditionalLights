package com.mgen256.al.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.level.block.Block;
import java.util.function.Consumer;

public class PedestalBlockItem extends BlockItem {

    private static Component txt_shift;
    private static Component txt_tips;
    private static Component txt_rightclick;
    private static Component txt_sneaking;
    private static Component txt_signals;


    public PedestalBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        if( txt_shift == null )
        {
            if( !I18n.exists("additional_lights.txt.shift") )
                return;

            txt_shift = Component.translatable( "additional_lights.txt.shift" );
            txt_tips = Component.translatable( "additional_lights.txt.tips" );
            txt_rightclick = Component.translatable( "additional_lights.txt.block.pedestal.rightclick" );
            txt_sneaking = Component.translatable( "additional_lights.txt.block.pedestal.sneaking" );
            txt_signals = Component.translatable( "additional_lights.txt.block.pedestal.signals" );
        }
        
        txt_shift = Component.translatable("additional_lights.txt.shift");
        txt_tips = Component.translatable("additional_lights.txt.tips");
        txt_rightclick = Component.translatable("additional_lights.txt.block.pedestal.rightclick");
        txt_sneaking = Component.translatable("additional_lights.txt.block.pedestal.sneaking");
        txt_signals = Component.translatable("additional_lights.txt.block.pedestal.signals");
        
        if (Minecraft.getInstance().hasShiftDown()) {
            consumer.accept(txt_tips);
            consumer.accept(txt_rightclick);
            consumer.accept(txt_sneaking);
            consumer.accept(txt_signals);
        } else {
            consumer.accept(txt_shift);
        }
    }
}
