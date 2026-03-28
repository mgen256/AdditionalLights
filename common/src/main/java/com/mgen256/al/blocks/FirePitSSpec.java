package com.mgen256.al.blocks;

import com.mgen256.al.FireTypes;
import com.mgen256.al.BlockSpec;
import com.mgen256.al.ModBlockList;
import com.mgen256.al.FireForSpec;
import com.mgen256.al.SoulFireForSpec;
import com.mgen256.al.PedestalTypes;

public interface FirePitSSpec extends LightWandTargetSpec {
    double[] LOWER_BOX = {2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D};
    double[] UPPER_BOX = {0.0D, 4.0D, 0.0D, 16.0D, 8.0D, 16.0D};

    default PedestalTypes getType() {
        return PedestalTypes.fire_pit_s;
    }

    default BlockSpec getFireFromType(FireTypes fireType) {
        return fireType == FireTypes.SOUL
            ? SoulFireForSpec.SoulFire_For_FirePit_S
            : FireForSpec.Fire_For_FirePit_S;
    }
}
