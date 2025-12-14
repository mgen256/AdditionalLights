package com.mgen256.al.conditions;

import com.mgen256.al.AdditionalLights;
import com.mgen256.al.config.AdditionalLightsConfig;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.registry.RegistryOps;
import net.minecraft.util.Identifier;

public final class EnableFireCraftingCondition implements ResourceCondition {
	private static final EnableFireCraftingCondition INSTANCE = new EnableFireCraftingCondition();
	private static final MapCodec<EnableFireCraftingCondition> CODEC = MapCodec.unit(INSTANCE).stable();

	public static final Identifier ID = Identifier.of(AdditionalLights.MOD_ID, "enable_fire_crafting");
	public static final ResourceConditionType<EnableFireCraftingCondition> TYPE = ResourceConditionType.create(ID, CODEC);

	private EnableFireCraftingCondition() {}

	@Override
	public ResourceConditionType<?> getType() {
		return TYPE;
	}

	@Override
	public boolean test(RegistryOps.RegistryInfoGetter context) {
		AdditionalLightsConfig.load();
		return AdditionalLightsConfig.get().enableFireCrafting;
	}
}
