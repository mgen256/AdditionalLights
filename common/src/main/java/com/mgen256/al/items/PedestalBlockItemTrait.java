package com.mgen256.al.items;

import java.util.function.Consumer;

public interface PedestalBlockItemTrait extends ModItemTrait {
    String KEY_SHIFT = "additional_lights.txt.shift";
    String KEY_TIPS = "additional_lights.txt.tips";
    String KEY_RIGHTCLICK = "additional_lights.txt.block.pedestal.rightclick";
    String KEY_SNEAKING = "additional_lights.txt.block.pedestal.sneaking";
    String KEY_SIGNALS = "additional_lights.txt.block.pedestal.signals";

    default void buildPedestalTooltip(boolean shiftDown, Consumer<String> adder) {
        if (shiftDown) {
            adder.accept(KEY_TIPS);
            adder.accept(KEY_RIGHTCLICK);
            adder.accept(KEY_SNEAKING);
            adder.accept(KEY_SIGNALS);
        } else {
            adder.accept(KEY_SHIFT);
        }
    }
}
