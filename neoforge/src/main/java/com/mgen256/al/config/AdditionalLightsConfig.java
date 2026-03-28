package com.mgen256.al.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class AdditionalLightsConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue ENABLE_FIRE_CRAFTING;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        ENABLE_FIRE_CRAFTING = builder
                .comment(
                        "If true, Additional Lights fire blocks (for example fire_for_*) can be crafted via recipes.",
                        "Set this to false if another mod conflicts with them.",
                        "Run /reload or rejoin the world for the change to take effect.")
                .define("enableFireCrafting", false);

        SPEC = builder.build();
    }

    private AdditionalLightsConfig() {}
}
