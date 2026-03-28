package com.mgen256.al;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import com.mgen256.al.items.LightWand;
import com.mgen256.al.items.SoulWand;

public class ModItems {
    private static final Map<ModItemList, Function<String, Item>> FACTORIES = new EnumMap<>(ModItemList.class);
    private static final ModItemsCore<Item> CORE = new ModItemsCore<>();

    static {
        FACTORIES.put(ModItemList.SoulWand, SoulWand::new);
        FACTORIES.put(ModItemList.LightWand, LightWand::new);
    }

    public static Item get(ModItemList key) {
        return CORE.get(key);
    }

    public static void registerAll() {
        for (var entry : FACTORIES.entrySet()) {
            AdditionalLightsFabric.LOG_PROVIDER.log("register item: " + entry.getKey().getRegName());
            var key = entry.getKey();
            CORE.register(
                key,
                k -> Registry.register(
                    BuiltInRegistries.ITEM,
                    AdditionalLightsFabric.createItemRegistryKey(k.getRegName()),
                    entry.getValue().apply(k.getRegName())
                )
            );
        }
    }
}
