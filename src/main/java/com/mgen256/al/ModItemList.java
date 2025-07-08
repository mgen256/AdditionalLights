package com.mgen256.al;

import com.mgen256.al.items.SoulWand;

import net.minecraft.world.item.Item;

public enum ModItemList {
    SoulWand("soul_wand") {
        @Override
        public Item createItem() {
            return new SoulWand(getRegName());
        }
    };

    private final String regName;

    ModItemList(String regName) {
        this.regName = regName;
    }

    public String getRegName() {
        return regName;
    }

    public abstract Item createItem();

    public void register() {
        AdditionalLights.modItems.put(this,
                AdditionalLights.ITEMS.register(getRegName(), () -> createItem()));
    }
}
