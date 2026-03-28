package com.mgen256.al.items;

public class SoulWandCore implements SoulWandTrait {

    private static final FireTypeWandCore CORE = new FireTypeWandCore();

    public void changeFire(
            com.mgen256.al.FireTypes current,
            com.mgen256.al.FireTypes prev,
            boolean sneaking,
            FireTypeWandCore.Action action) {
        CORE.changeFire(this, current, prev, sneaking, action);
    }
}
