package com.mgen256.al.items;

import com.mgen256.al.FireTypes;

public interface LightWandTrait extends FireTypeWandTrait {

    @Override
    default FireTypes getTargetFireType() {
        return FireTypes.LIGHT;
    }

    @Override
    default boolean supportsAutomaticPlacementOnTorch() {
        return false;
    }

    @Override
    default String getRightClickKey() {
        return "additional_lights.txt.item.light_wand.rightclick";
    }

    @Override
    default String getLeftHandKey() {
        return "additional_lights.txt.item.light_wand.lefthand";
    }

    @Override
    default String getPiglinKey() {
        return null;
    }
}
