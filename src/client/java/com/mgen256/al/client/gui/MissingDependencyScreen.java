package com.mgen256.al.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public final class MissingDependencyScreen extends Screen {
	private final Screen parent;
	private final Text message;

	public MissingDependencyScreen(Screen parent, Text title, Text message) {
		super(title);
		this.parent = parent;
		this.message = message;
	}

	@Override
	protected void init() {
		addDrawableChild(
			ButtonWidget.builder(ScreenTexts.DONE, button -> close())
				.dimensions(this.width / 2 - 100, this.height - 29, 200, 20)
				.build()
		);
	}

	@Override
	public void close() {
		if (this.client != null) {
			this.client.setScreen(this.parent);
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderBackground(context, mouseX, mouseY, delta);

		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

		int y = 60;
		for (var line : this.textRenderer.wrapLines(this.message, this.width - 40)) {
			int x = (this.width - this.textRenderer.getWidth(line)) / 2;
			context.drawTextWithShadow(this.textRenderer, line, x, y, 0xFFFFFF);
			y += 12;
		}

		super.render(context, mouseX, mouseY, delta);
	}
}

