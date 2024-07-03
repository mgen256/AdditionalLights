package com.mgen256.al;

import java.util.function.Supplier;

import com.mgen256.al.items.SoulWand;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public enum ModItemList {
    SoulWand("soul_wand", SoulWand::new);

    private final String name;
    private final Supplier<Item> itemSupplier;
    private Item item;

    ModItemList(String name, Supplier<Item> itemSupplier) {
        this.name = name;
        this.itemSupplier = itemSupplier;
    }

    public Item getItem() {
        if (item == null)
            item = itemSupplier.get();
        return item;
    }

    public void Register() {
        Registry.register(Registries.ITEM, Identifier.of(AdditionalLights.MOD_ID, name), getItem());
    }
}
