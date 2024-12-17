package com.mgen256.al;

import java.util.function.Function;

import com.mgen256.al.items.SoulWand;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public enum ModItemList {
    SoulWand("soul_wand", (key) -> new SoulWand(key));

    private final String name;
    private Function<RegistryKey<Item>, Item> itemFactory;
    private Item item;

    ModItemList(String name, Function<RegistryKey<Item>, Item> itemFactory) {
        this.name = name;
        this.itemFactory = itemFactory;
    }

    public Item get() {
        return get(null);
    }

    public Item get(RegistryKey<Item> key) {
        if (item == null)
        {
            assert key != null : "RegistryKey is null"; 
            item = itemFactory.apply(key);
        }
        return item;
    }

    public void Register() {
        var id = Identifier.of(AdditionalLights.MOD_ID, name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
        Registry.register(Registries.ITEM, key, get(key));
    }
}
