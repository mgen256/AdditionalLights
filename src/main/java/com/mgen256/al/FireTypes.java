package com.mgen256.al;

import net.minecraft.util.StringIdentifiable;

public enum FireTypes implements StringIdentifiable {
    NORMAL("normal"),
    SOUL("soul");

    private final String name;

    FireTypes(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
