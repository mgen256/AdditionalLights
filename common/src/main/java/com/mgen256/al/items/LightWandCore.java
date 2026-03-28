package com.mgen256.al.items;

import com.mgen256.al.FireTypes;

public class LightWandCore implements LightWandTrait {

    private static final FireTypeWandCore CORE = new FireTypeWandCore();

    public void changeFire(
            FireTypes current,
            FireTypes prev,
            boolean sneaking,
            FireTypeWandCore.Action action) {
        CORE.changeFire(this, current, prev, sneaking, action);
    }
}
