package com.mgen256.al;

import java.util.List;

import com.mgen256.al.items.PedestalBlockItem;
import com.mgen256.al.items.Wand;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class AdditionalLightsClient implements ClientModInitializer {
	private static final Text SHIFT_TEXT = Text.translatable("additional_lights.txt.shift");
	private static final Text PEDESTAL_TIPS_TEXT = Text.translatable("additional_lights.txt.tips");
	private static final Text PEDESTAL_RIGHT_CLICK_TEXT = Text.translatable("additional_lights.txt.block.pedestal.rightclick");
	private static final Text PEDESTAL_SNEAKING_TEXT = Text.translatable("additional_lights.txt.block.pedestal.sneaking");
	private static final Text PEDESTAL_SIGNALS_TEXT = Text.translatable("additional_lights.txt.block.pedestal.signals");
	private static final Text WAND_USAGE_TEXT = Text.translatable("additional_lights.txt.usage");
	private static final Text WAND_RIGHT_CLICK_TEXT = Text.translatable("additional_lights.txt.item.soul_wand.rightclick");
	private static final Text WAND_LEFT_HAND_TEXT = Text.translatable("additional_lights.txt.item.soul_wand.lefthand");
	private static final Text WAND_PIGLIN_TEXT = Text.translatable("additional_lights.txt.item.soul_wand.piglin");

	@Override
	public void onInitializeClient() {
		registerRenderLayers();
		registerTooltips();
	}

	private void registerRenderLayers() {
		for (var block : ModBlockList.values()) {
			var name = block.getRegName();
			if (name.contains("fire_for_") || name.contains("glass")) {
				BlockRenderLayerMap.putBlock(block.get(), BlockRenderLayer.CUTOUT);
			}
		}
	}

	private void registerTooltips() {
		ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
			var item = stack.getItem();
			if (item instanceof PedestalBlockItem) {
				addPedestalTooltip(lines);
			} else if (item instanceof Wand) {
				addWandTooltip(lines);
			}
		});
	}

	private static void addPedestalTooltip(List<Text> lines) {
		if (isShiftDown()) {
			lines.add(PEDESTAL_TIPS_TEXT);
			lines.add(PEDESTAL_RIGHT_CLICK_TEXT);
			lines.add(PEDESTAL_SNEAKING_TEXT);
			lines.add(PEDESTAL_SIGNALS_TEXT);
		} else {
			lines.add(SHIFT_TEXT);
		}
	}

	private static void addWandTooltip(List<Text> lines) {
		if (!I18n.hasTranslation("additional_lights.txt.shift")) {
			return;
		}

		if (isShiftDown()) {
			lines.add(WAND_USAGE_TEXT);
			lines.add(WAND_RIGHT_CLICK_TEXT);
			lines.add(WAND_LEFT_HAND_TEXT);
			lines.add(WAND_PIGLIN_TEXT);
		} else {
			lines.add(SHIFT_TEXT);
		}
	}
	private static boolean isShiftDown() {
		var client = MinecraftClient.getInstance();
		if (client == null) {
			return false;
		}

		var window = client.getWindow();
		return InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_LEFT_SHIFT)
			|| InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
	}
}
