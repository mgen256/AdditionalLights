package com.mgen256.al;

public final class ModItemFactory {

    public enum ItemType {
        STANDARD,
        PEDESTAL,
        VERTICALLY_ATTACHABLE
    }

    private ModItemFactory() {
    }

    public static ItemType resolve(BlockSpec spec) {
        return spec.getItemType();
    }
}
