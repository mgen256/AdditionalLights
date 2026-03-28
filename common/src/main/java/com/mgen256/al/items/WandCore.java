package com.mgen256.al.items;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class WandCore<T> implements WandTrait {
    private final Map<String, T> cache = new HashMap<>();

    public interface Translator<T> {
        boolean exists(String key);

        T translate(String key);
    }

    public void appendTooltip(WandTrait wand, boolean shiftDown, Consumer<T> adder, Translator<T> tr) {
        if (!tr.exists(KEY_SHIFT)) {
            return;
        }

        wand.buildWandTooltip(shiftDown, key -> {
            if (!tr.exists(key)) {
                return;
            }
            T txt = cache.get(key);
            if (txt == null) {
                txt = tr.translate(key);
                cache.put(key, txt);
            }
            adder.accept(txt);
        });
    }
}
