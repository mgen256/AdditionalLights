package com.mgen256.al.blocks;

import com.mgen256.al.PedestalTypes;

public interface StandingTorchSSpec extends PedestalFireSpec {
    double[] LOWER_BOX = {4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D};
    double[] MID_BOX = {6.0D, 2.0D, 6.0D, 10.0D, 8.0D, 10.0D};
    double[] UPPER_BOX = {4.0D, 8.0D, 4.0D, 12.0D, 12.0D, 12.0D};

    default PedestalTypes getType() {
        return PedestalTypes.standing_torch_s;
    }

}
