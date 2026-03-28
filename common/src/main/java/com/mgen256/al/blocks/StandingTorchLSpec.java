package com.mgen256.al.blocks;

import com.mgen256.al.FireTypes;
import com.mgen256.al.BlockSpec;
import com.mgen256.al.ModBlockList;
import com.mgen256.al.PedestalTypes;
import com.mgen256.al.FireForSpec;
import com.mgen256.al.SoulFireForSpec;

public interface StandingTorchLSpec extends LightWandTargetSpec {
    double[] LOWER_BOX1 = {4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D};
    double[] LOWER_BOX2 = {5.0D, 0.0D, 5.0D, 11.0D, 4.0D, 11.0D};
    double[] MID_BOX = {6.0D, 4.0D, 6.0D, 10.0D, 12.0D, 10.0D};
    double[] UPPER_BOX1 = {5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D};
    double[] UPPER_BOX2 = {4.0D, 14.0D, 4.0D, 12.0D, 16.0D, 12.0D};

    default PedestalTypes getType() {
        return PedestalTypes.standing_torch_l;
    }

    default BlockSpec getFireFromType(FireTypes fireType) {
        return fireType == FireTypes.SOUL
            ? SoulFireForSpec.SoulFire_For_StandingTorch_L
            : FireForSpec.Fire_For_StandingTorch_L;
    }
}
