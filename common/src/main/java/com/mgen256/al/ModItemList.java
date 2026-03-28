package com.mgen256.al;

public enum ModItemList {
    SoulWand("soul_wand"),
    LightWand("light_wand");

    private final String regName;

    ModItemList(String regName) {
        this.regName = regName;
    }

    public String getRegName() {
        return regName;
    }
}
