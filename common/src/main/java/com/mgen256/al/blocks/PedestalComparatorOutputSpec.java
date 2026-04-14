package com.mgen256.al.blocks;

import com.mgen256.al.FireTypes;

public final class PedestalComparatorOutputSpec {
    private static final int NORMAL_OUTPUT = 6;
    private static final int SOUL_OUTPUT = 3;
    private static final int LIGHT_OUTPUT = 9;

    private PedestalComparatorOutputSpec() {
    }

    public static int resolve(FireTypes fireType) {
        if (fireType == FireTypes.SOUL) {
            return SOUL_OUTPUT;
        }
        if (fireType == FireTypes.LIGHT) {
            return LIGHT_OUTPUT;
        }
        return NORMAL_OUTPUT;
    }
}
