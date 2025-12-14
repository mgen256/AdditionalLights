package com.mgen256.al.compat.clothconfig;

import com.mgen256.al.config.AdditionalLightsConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class AdditionalLightsClothConfigScreen {
	private AdditionalLightsClothConfigScreen() {}

	public static Screen create(Screen parent) {
		var config = AdditionalLightsConfig.get();

		var builder = ConfigBuilder.create()
			.setParentScreen(parent)
			.setTitle(Text.translatable("additional_lights.configuration.title", "Additional Lights"))
			.setSavingRunnable(AdditionalLightsConfig::save);

		var category = builder.getOrCreateCategory(Text.translatable("additional_lights.configuration.recipes"));
		var entryBuilder = builder.entryBuilder();

		category.addEntry(
			entryBuilder.startBooleanToggle(
					Text.translatable("additional_lights.configuration.enableFireCrafting"),
					config.enableFireCrafting
				)
				.setDefaultValue(false)
				.setTooltip(Text.translatable("additional_lights.configuration.enableFireCrafting.tooltip"))
				.setSaveConsumer(value -> config.enableFireCrafting = value)
				.build()
		);

		return builder.build();
	}
}

