package com.mgen256.al.items;

import java.util.function.Consumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import com.mgen256.al.client.ShiftKeyBehavior;
import com.mgen256.al.items.PedestalBlockItemCore;

public class PedestalBlockItem extends BlockItem implements PedestalBlockItemTrait {

    private static final PedestalBlockItemCore.Translator<Component> TRANSLATOR = new PedestalBlockItemCore.Translator<>() {
        @Override
        public boolean exists(String key) { return true; }

        @Override
        public Component translate(String key) { return Component.translatable(key); }
    };

    private static final PedestalBlockItemCore<Component> CORE = new PedestalBlockItemCore<>();


    public PedestalBlockItem(Block block, Item.Properties settings) {
        super(block, settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT)
            return;

        CORE.appendTooltip(ShiftKeyBehavior.isShiftDown(), textConsumer, TRANSLATOR);
    }
}
