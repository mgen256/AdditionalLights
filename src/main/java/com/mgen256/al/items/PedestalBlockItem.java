package com.mgen256.al.items;

import java.util.function.Consumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

public class PedestalBlockItem extends BlockItem {

    private static Text txt_shift;
    private static Text txt_tips;
    private static Text txt_rightclick;
    private static Text txt_sneaking;
    private static Text txt_signals;


    public PedestalBlockItem(Block block, Item.Settings settings) {
        super(block, settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT)
            return;

        if (txt_shift == null) {
            txt_shift = Text.translatable("additional_lights.txt.shift");
            txt_tips = Text.translatable("additional_lights.txt.tips");
            txt_rightclick = Text.translatable("additional_lights.txt.block.pedestal.rightclick");
            txt_sneaking = Text.translatable("additional_lights.txt.block.pedestal.sneaking");
            txt_signals = Text.translatable("additional_lights.txt.block.pedestal.signals");
        }

        if (Screen.hasShiftDown()) {
            textConsumer.accept(txt_tips);
            textConsumer.accept(txt_rightclick);
            textConsumer.accept(txt_sneaking);
            textConsumer.accept(txt_signals);
        } else {
            textConsumer.accept(txt_shift);
        }
    }
}
