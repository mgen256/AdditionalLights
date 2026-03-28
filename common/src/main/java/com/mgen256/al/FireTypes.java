package com.mgen256.al;

public enum FireTypes {
    NORMAL(14),
    SOUL(10),
    LIGHT(15);

    private final int luminance;

    FireTypes(int luminance) {
        this.luminance = luminance;
    }

    public int getLuminance() {
        return luminance;
    }
}
