package com.mgen256.al.client.fire;

import java.util.Locale;

public enum FireSurfaceTextureModeSpec {
    STATIC,

    DYNAMIC;

    public String serializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public static FireSurfaceTextureModeSpec parse(final String raw) {
        if (raw == null || raw.isBlank()) {
            return DYNAMIC;
        }

        return switch (raw.trim().toLowerCase(Locale.ROOT)) {
            case "static" -> STATIC;
            case "dynamic" -> DYNAMIC;
            default -> DYNAMIC;
        };
    }
}
