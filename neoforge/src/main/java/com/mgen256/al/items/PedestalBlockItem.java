package com.mgen256.al.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.level.block.Block;
import java.util.function.Consumer;
import com.mgen256.al.client.ShiftKeyBehavior;
import com.mgen256.al.items.PedestalBlockItemCore;

public class PedestalBlockItem extends BlockItem implements PedestalBlockItemTrait {

    private static final PedestalBlockItemCore.Translator<Component> TRANSLATOR = new PedestalBlockItemCore.Translator<>() {
        @Override
        public boolean exists(String key) { return I18n.exists(key); }

        @Override
        public Component translate(String key) { return Component.translatable(key); }
    };

    private static final PedestalBlockItemCore<Component> CORE = new PedestalBlockItemCore<>();


    public PedestalBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        CORE.appendTooltip(ShiftKeyBehavior.isShiftDown(), consumer, TRANSLATOR);
    }
}
