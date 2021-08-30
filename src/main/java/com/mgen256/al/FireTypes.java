package com.mgen256.al;

import net.minecraft.util.StringRepresentable;

public enum FireTypes implements StringRepresentable {
    NORMAL("normal"),
    SOUL("soul");

    private final String name;

    private FireTypes(String name) {
       this.name = name;
    }
 
    public String toString() {
       return this.getSerializedName();
    }
 
    public String getSerializedName() {
       return this.name;
    }
}