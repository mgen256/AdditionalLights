package com.mgen256.al;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public class ModItemsCore<I> {

    private final Map<ModItemList, I> items = new EnumMap<>(ModItemList.class);

    public ModItemsCore() {
    }

    public void register(ModItemList key, Function<ModItemList, ? extends I> registrar) {
        I item = registrar.apply(key);
        items.put(key, item);
    }

    public I get(ModItemList key) {
        return items.get(key);
    }
}
