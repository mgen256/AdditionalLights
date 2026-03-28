package com.mgen256.al.conditions;

import com.mgen256.al.CommonConstants;
import com.mgen256.al.config.AdditionalLightsConfig;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;

public final class EnableFireCraftingCondition implements ResourceCondition {
    private static final EnableFireCraftingCondition INSTANCE = new EnableFireCraftingCondition();
    private static final MapCodec<EnableFireCraftingCondition> CODEC = MapCodec.unit(INSTANCE).stable();

    public static final Identifier ID = Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "enable_fire_crafting");
    public static final ResourceConditionType<EnableFireCraftingCondition> TYPE = ResourceConditionType.create(ID, CODEC);

    private EnableFireCraftingCondition() {}

    @Override
    public ResourceConditionType<?> getType() {
        return TYPE;
    }

    @Override
    public boolean test(RegistryOps.RegistryInfoLookup context) {
        AdditionalLightsConfig.load();
        return AdditionalLightsConfig.get().enableFireCrafting;
    }
}
