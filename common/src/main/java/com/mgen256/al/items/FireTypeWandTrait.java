package com.mgen256.al.items;

import com.mgen256.al.FireTypes;
import com.mgen256.al.ModSoundList;

public interface FireTypeWandTrait extends WandTrait {

    record FireChange(FireTypes newType, FireTypes prevType, ModSoundList sound, float volume) {}

    FireTypes getTargetFireType();

    default boolean supportsAutomaticPlacementOnTorch() {
        return true;
    }

    default FireChange computeFireChange(FireTypes current, FireTypes prev, boolean sneaking) {
        FireTypes target = getTargetFireType();
        if (prev == target) {
            prev = FireTypes.NORMAL;
        }

        if (sneaking) {
            if (current == target) {
                return new FireChange(prev, prev, ModSoundList.Undo, 0.6f);
            }
        } else {
            if (current != target) {
                return new FireChange(target, current, ModSoundList.Change, 0.8f);
            }
        }
        return null;
    }
}
