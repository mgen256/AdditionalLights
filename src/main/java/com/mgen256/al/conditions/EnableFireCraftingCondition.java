package com.mgen256.al.conditions;

import com.mgen256.al.config.AdditionalLightsConfig;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;

public final class EnableFireCraftingCondition implements ICondition {
    public static final EnableFireCraftingCondition INSTANCE = new EnableFireCraftingCondition();
    public static final MapCodec<EnableFireCraftingCondition> CODEC = MapCodec.unit(INSTANCE).stable();

    private EnableFireCraftingCondition() {}

    @Override
    public boolean test(IContext context) {
        return AdditionalLightsConfig.ENABLE_FIRE_CRAFTING.get();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
