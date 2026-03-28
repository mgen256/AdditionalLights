package com.mgen256.al.items;

public interface SoulWandTrait extends FireTypeWandTrait {

    @Override
    default com.mgen256.al.FireTypes getTargetFireType() {
        return com.mgen256.al.FireTypes.SOUL;
    }
}
