package com.mgen256.al.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;

public final class ShiftKeyBehavior {
    private ShiftKeyBehavior() {}

    public static boolean isShiftDown() {
        var client = Minecraft.getInstance();
        if (client == null) {
            return false;
        }

        return InputConstants.isKeyDown(InputConstants.KEY_LSHIFT)
                || InputConstants.isKeyDown(InputConstants.KEY_RSHIFT);
    }
}
