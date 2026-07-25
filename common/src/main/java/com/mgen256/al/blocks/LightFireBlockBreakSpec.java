package com.mgen256.al.blocks;

import com.mgen256.al.PedestalTypes;

public final class LightFireBlockBreakSpec {

    private LightFireBlockBreakSpec() {
    }

    public static boolean shouldDelegate(
            final boolean summoned,
            final PedestalTypes lightType,
            final PedestalTypes pedestalType,
            final boolean pedestalUsesLight) {
        return summoned
                && lightType == pedestalType
                && pedestalUsesLight;
    }

    public static boolean shouldUseSilentFallbackSound(final boolean summoned) {
        return summoned;
    }
}
