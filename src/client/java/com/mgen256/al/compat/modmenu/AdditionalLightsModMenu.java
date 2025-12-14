package com.mgen256.al.compat.modmenu;

import com.mgen256.al.client.gui.MissingDependencyScreen;
import com.mgen256.al.compat.clothconfig.AdditionalLightsClothConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class AdditionalLightsModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> createConfigScreen(parent);
	}

	private static Screen createConfigScreen(Screen parent) {
		var title = Text.translatable("additional_lights.configuration.title", "Additional Lights");

		if (!isClothConfigLoaded()) {
			return new MissingDependencyScreen(
				parent,
				title,
				Text.literal("Cloth Config is not installed.\nInstall it to edit settings, or edit config/additional_lights.json manually and run /reload.")
			);
		}

		try {
			return AdditionalLightsClothConfigScreen.create(parent);
		} catch (Throwable t) {
			return new MissingDependencyScreen(
				parent,
				title,
				Text.literal("Failed to open the config screen. Check your Cloth Config version.")
			);
		}
	}

	private static boolean isClothConfigLoaded() {
		var loader = FabricLoader.getInstance();
		return loader.isModLoaded("cloth-config") || loader.isModLoaded("cloth-config2");
	}
}
