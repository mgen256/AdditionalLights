package com.mgen256.al.items;

import com.mgen256.al.FireTypes;
import com.mgen256.al.ModSoundList;

public class FireTypeWandCore {

    public static boolean shouldReplacePedestalFireOnly(FireTypes current) {
        return current != FireTypes.LIGHT;
    }

    @FunctionalInterface
    public interface Action {
        void apply(FireTypes newType, FireTypes prevType, ModSoundList sound, float volume);
    }

    public void changeFire(
            FireTypeWandTrait wand,
            FireTypes current,
            FireTypes prev,
            boolean sneaking,
            Action action) {
        FireTypeWandTrait.FireChange result = wand.computeFireChange(current, prev, sneaking);
        if (result != null) {
            action.apply(result.newType(), result.prevType(), result.sound(), result.volume());
        }
    }
}
