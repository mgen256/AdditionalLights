package com.mgen256.al.mixin.client;

import com.mgen256.al.blocks.Fire_Light;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class LightFireBlockClientBreakBehavior {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    private void additionalLights$redirectClientLightBreak(
            final BlockPos pos,
            final CallbackInfoReturnable<Boolean> callback) {
        final ClientLevel level = this.minecraft.level;
        if (level == null) {
            return;
        }

        final BlockState lightState = level.getBlockState(pos);
        final BlockPos pedestalPos = additionalLights$resolveBreakTarget(level, pos);
        if (pedestalPos.equals(pos)) {
            return;
        }

        final boolean destroyed =
                ((MultiPlayerGameMode) (Object) this).destroyBlock(pedestalPos);
        if (destroyed
                && additionalLights$resolveBreakTarget(level, pos).equals(pos)
                && level.getBlockState(pos).getBlock() == lightState.getBlock()) {
            level.removeBlock(pos, false);
        }
        callback.setReturnValue(destroyed);
    }

    private BlockPos additionalLights$resolveBreakTarget(
            final BlockGetter world,
            final BlockPos lightPos) {
        final BlockState lightState = world.getBlockState(lightPos);
        if (!(lightState.getBlock() instanceof Fire_Light light)) {
            return lightPos;
        }
        return light.resolveBreakTarget(world, lightPos);
    }
}
