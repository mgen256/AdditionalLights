package com.mgen256.al.mixin;

import com.mgen256.al.blocks.Fire_Light;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerPlayerGameMode.class)
public abstract class LightFireBlockBreakBehavior {

    @Shadow protected ServerLevel level;

    @ModifyVariable(method = "destroyBlock", at = @At("HEAD"), argsOnly = true)
    private BlockPos additionalLights$redirectLightBreak(final BlockPos pos) {
        if (!(this.level.getBlockState(pos).getBlock() instanceof Fire_Light light)) {
            return pos;
        }
        return light.resolveBreakTarget(this.level, pos);
    }
}
