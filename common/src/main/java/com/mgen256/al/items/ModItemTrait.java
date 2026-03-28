package com.mgen256.al.items;

public interface ModItemTrait extends ModItemSpec {
    @Override
    default String getRegName() {
        return "";
    }
    default void log(String message) {
    }
}
