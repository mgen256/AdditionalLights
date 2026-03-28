package com.mgen256.al;

import net.minecraft.util.StringRepresentable;

public enum NeoForgeFireTypesAdapter implements StringRepresentable {
    NORMAL(FireTypesAdapter.NORMAL),
    SOUL(FireTypesAdapter.SOUL),
    LIGHT(FireTypesAdapter.LIGHT);

    private final FireTypesAdapter core;

    NeoForgeFireTypesAdapter(FireTypesAdapter core) {
        this.core = core;
    }

    public FireTypes toCore() {
        return core.toCore();
    }

    public static NeoForgeFireTypesAdapter fromCore(FireTypes type) {
        if (type == FireTypes.SOUL) {
            return SOUL;
        }
        if (type == FireTypes.LIGHT) {
            return LIGHT;
        }
        return NORMAL;
    }

    @Override
    public String getSerializedName() {
        return core.getSerializedName();
    }
}
