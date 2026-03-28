package com.mgen256.al;

public enum ModSoundList {
    Change("change"),
    Undo("undo"),
    Fire_Ignition_S("fire_ignition_s"),
    Fire_Ignition_L("fire_ignition_l"),
    Fire_Extinguish("fire_extinguish");

    private final String fileName;

    ModSoundList(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
