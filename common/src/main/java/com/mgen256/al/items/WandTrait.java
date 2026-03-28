package com.mgen256.al.items;

import java.util.function.Consumer;

public interface WandTrait extends ModItemTrait {
    String KEY_SHIFT = "additional_lights.txt.shift";
    String KEY_USAGE = "additional_lights.txt.usage";

    default String getRightClickKey() {
        return "additional_lights.txt.item.soul_wand.rightclick";
    }

    default String getSneakingRightClickKey() {
        return "additional_lights.txt.item.wand.sneak_rightclick";
    }

    default String getLeftHandKey() {
        return "additional_lights.txt.item.soul_wand.lefthand";
    }

    default String getPiglinKey() {
        return "additional_lights.txt.item.soul_wand.piglin";
    }

    default void buildWandTooltip(boolean shiftDown, Consumer<String> adder) {
        if (shiftDown) {
            adder.accept(KEY_USAGE);
            adder.accept(getRightClickKey());
            adder.accept(getSneakingRightClickKey());
            adder.accept(getLeftHandKey());
            String piglinKey = getPiglinKey();
            if (piglinKey != null && !piglinKey.isEmpty()) {
                adder.accept(piglinKey);
            }
        } else {
            adder.accept(KEY_SHIFT);
        }
    }
}
