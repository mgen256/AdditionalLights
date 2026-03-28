package com.mgen256.al.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class ShiftKeyBehavior {
    private ShiftKeyBehavior() {}

    public static boolean isShiftDown() {
        var minecraft = Minecraft.getInstance();
        if (minecraft == null) {
            return false;
        }

        var window = minecraft.getWindow();
        return InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT)
                || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }
}
