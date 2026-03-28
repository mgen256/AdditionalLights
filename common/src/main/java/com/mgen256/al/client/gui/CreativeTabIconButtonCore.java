package com.mgen256.al.client.gui;

import com.mgen256.al.CommonConstants;
import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

final class CreativeTabIconButtonCore extends ImageButton {
    private static final int BACKGROUND_SOURCE_WIDTH = 16;
    private static final int BACKGROUND_SOURCE_HEIGHT = 16;
    private static final float TEXT_SCALE = 0.8F;
    private static final int TEXT_BASELINE_HEIGHT = 8;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;
    private static final int INACTIVE_TEXT_COLOR = 0xFFB0B0B0;
    private static final WidgetSprites DISABLED_BACKGROUND_SPRITES =
            new WidgetSprites(
                    Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "widget/creative_tab_button"),
                    Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "widget/creative_tab_button_highlighted"));
    private static final WidgetSprites ENABLED_BACKGROUND_SPRITES =
            new WidgetSprites(
                    Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "widget/creative_tab_button_selected"),
                    Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "widget/creative_tab_button_selected"),
                    Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "widget/creative_tab_button_highlighted"),
                    Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "widget/creative_tab_button_highlighted"));

    private final BooleanSupplier selectedStateSupplier;
    private final float textXOffset;
    private final float textYOffset;

    CreativeTabIconButtonCore(
            final Component message,
            final Button.OnPress onPress,
            final BooleanSupplier selectedStateSupplier,
            final float textXOffset,
            final float textYOffset) {
        super(
                0,
                0,
                BACKGROUND_SOURCE_WIDTH,
                BACKGROUND_SOURCE_HEIGHT,
                DISABLED_BACKGROUND_SPRITES,
                onPress,
                message);
        this.selectedStateSupplier = selectedStateSupplier;
        this.textXOffset = textXOffset;
        this.textYOffset = textYOffset;
    }

    @Override
    public void extractContents(
            final GuiGraphicsExtractor guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
        final WidgetSprites backgroundSprites =
                this.selectedStateSupplier.getAsBoolean() ? ENABLED_BACKGROUND_SPRITES : DISABLED_BACKGROUND_SPRITES;
        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                backgroundSprites.get(this.isActive(), this.isHovered()),
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight());

        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null) {
            return;
        }

        final int textWidth = minecraft.font.width(this.getMessage());
        final int textColor = ARGB.multiplyAlpha(this.active ? DEFAULT_TEXT_COLOR : INACTIVE_TEXT_COLOR, this.alpha);
        final float textCenterX = this.getX() + (this.getWidth() / 2.0F) + this.textXOffset;
        final float textCenterY = this.getY() + (this.getHeight() / 2.0F) + this.textYOffset;

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(textCenterX, textCenterY);
        guiGraphics.pose().scale(TEXT_SCALE, TEXT_SCALE);
        guiGraphics.text(
                minecraft.font,
                this.getMessage(),
                Math.round(-textWidth / 2.0F),
                Math.round(-TEXT_BASELINE_HEIGHT / 2.0F),
                textColor,
                false);
        guiGraphics.pose().popMatrix();
    }
}
