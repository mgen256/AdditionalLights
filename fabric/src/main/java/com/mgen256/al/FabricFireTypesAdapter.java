package com.mgen256.al;

import net.minecraft.util.StringRepresentable;

public enum FabricFireTypesAdapter implements StringRepresentable {
    NORMAL(FireTypesAdapter.NORMAL),
    SOUL(FireTypesAdapter.SOUL),
    LIGHT(FireTypesAdapter.LIGHT);

    private final FireTypesAdapter core;

    FabricFireTypesAdapter(FireTypesAdapter core) {
        this.core = core;
    }

    public FireTypes toCore() {
        return core.toCore();
    }

    public static FabricFireTypesAdapter fromCore(FireTypes type) {
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
