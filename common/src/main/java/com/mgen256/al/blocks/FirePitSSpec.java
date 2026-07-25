package com.mgen256.al.blocks;

import com.mgen256.al.PedestalTypes;

public interface FirePitSSpec extends PedestalFireSpec {
    double[] LOWER_BOX = {2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D};
    double[] UPPER_BOX = {0.0D, 4.0D, 0.0D, 16.0D, 8.0D, 16.0D};

    default PedestalTypes getType() {
        return PedestalTypes.fire_pit_s;
    }

}
