package com.mgen256.al;

public enum FireTypesAdapter implements NameSerializable {
    NORMAL(FireTypes.NORMAL),
    SOUL(FireTypes.SOUL),
    LIGHT(FireTypes.LIGHT);

    private final FireTypes coreType;

    FireTypesAdapter(FireTypes coreType) {
        this.coreType = coreType;
    }

    public FireTypes toCore() {
        return coreType;
    }

    public static FireTypesAdapter fromCore(FireTypes type) {
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
        return coreType.name().toLowerCase();
    }
}
