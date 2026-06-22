package com.mgen256.al.client.gui;

import com.mgen256.al.config.AdditionalLightsConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public final class AdditionalLightsConfigScreen extends Screen {
    private static final int CONTENT_WIDTH = 220;

    private final Screen parent;
    private boolean enableFireCrafting;
    private Button fireCraftingButton;

    public AdditionalLightsConfigScreen(final Screen parent) {
        super(Component.translatable("additional_lights.configuration.title", "Additional Lights"));
        this.parent = parent;
        this.enableFireCrafting = AdditionalLightsConfig.get().enableFireCrafting;
    }

    @Override
    protected void init() {
        final int centerX = this.width / 2;
        final int buttonY = 88;

        this.fireCraftingButton =
                addRenderableWidget(
                        Button.builder(createFireCraftingLabel(), button -> toggleFireCrafting())
                                .bounds(centerX - 100, buttonY, 200, 20)
                                .build());
        this.fireCraftingButton.setTooltip(
                Tooltip.create(
                        Component.translatable(
                                "additional_lights.configuration.enableFireCrafting.tooltip")));

        addRenderableWidget(
                Button.builder(Component.translatable("gui.done"), button -> saveAndClose())
                        .bounds(centerX - 102, this.height - 29, 100, 20)
                        .build());
        addRenderableWidget(
                Button.builder(Component.translatable("gui.cancel"), button -> onClose())
                        .bounds(centerX + 2, this.height - 29, 100, 20)
                        .build());
    }

    private void toggleFireCrafting() {
        this.enableFireCrafting = !this.enableFireCrafting;
        this.fireCraftingButton.setMessage(createFireCraftingLabel());
    }

    private void saveAndClose() {
        final AdditionalLightsConfig config = AdditionalLightsConfig.get();
        config.enableFireCrafting = this.enableFireCrafting;
        AdditionalLightsConfig.save();
        onClose();
    }

    private Component createFireCraftingLabel() {
        final Component status =
                Component.translatable(this.enableFireCrafting ? "options.on" : "options.off");
        return Component.translatable("additional_lights.configuration.enableFireCrafting")
                .append(Component.literal(": "))
                .append(status);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.gui.setScreen(this.parent);
        }
    }

    @Override
    public void extractRenderState(
            final GuiGraphicsExtractor graphics,
            final int mouseX,
            final int mouseY,
            final float partialTick) {
        graphics.centeredText(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        graphics.centeredText(
                this.font,
                Component.translatable("additional_lights.configuration.recipes"),
                this.width / 2,
                48,
                0xFFFFFF);

        int descriptionY = 62;
        for (final FormattedCharSequence line :
                this.font.split(
                        Component.translatable(
                                "additional_lights.configuration.enableFireCrafting.tooltip"),
                        CONTENT_WIDTH)) {
            graphics.text(this.font, line, this.width / 2 - CONTENT_WIDTH / 2, descriptionY, 0xA0A0A0, false);
            descriptionY += 12;
        }

        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }
}
