package com.mgen256.al.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class AdditionalLightsConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue ENABLE_FIRE_CRAFTING;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        ENABLE_FIRE_CRAFTING = builder
                .comment(
                        "true の場合、Additional Lights の「炎」ブロック(例: fire_for_*)をレシピでクラフト可能にします。",
                        "他MODと競合する場合は false にしてください。",
                        "※ 反映には /reload または再入場が必要です。")
                .define("enableFireCrafting", false);

        SPEC = builder.build();
    }

    private AdditionalLightsConfig() {}
}
