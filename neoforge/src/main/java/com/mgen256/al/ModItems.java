package com.mgen256.al;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import com.mgen256.al.items.LightWand;
import com.mgen256.al.items.SoulWand;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.item.Item;

public class ModItems {
    private static final Map<ModItemList, Function<String, DeferredHolder<Item, Item>>> FACTORIES =
            new EnumMap<>(ModItemList.class);
    private static final ModItemsCore<DeferredHolder<Item, Item>> CORE = new ModItemsCore<>();

    static {
        FACTORIES.put(ModItemList.SoulWand, name -> AdditionalLightsNeoForge.ITEMS.register(name, () -> new SoulWand(name)));
        FACTORIES.put(ModItemList.LightWand, name -> AdditionalLightsNeoForge.ITEMS.register(name, () -> new LightWand(name)));
    }

    public static Item get(final ModItemList key) {
        final DeferredHolder<Item, Item> holder = CORE.get(key);
        return holder != null ? holder.get() : null;
    }

    public static void registerAll() {
        for (var entry : FACTORIES.entrySet()) {
            AdditionalLightsNeoForge.LOG_PROVIDER.log("register item: " + entry.getKey().getRegName());
            var key = entry.getKey();
            CORE.register(key, k -> entry.getValue().apply(k.getRegName()));
        }
    }
}
