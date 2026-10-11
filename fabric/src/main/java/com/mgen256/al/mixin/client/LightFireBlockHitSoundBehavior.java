package com.mgen256.al.mixin.client;

import com.mgen256.al.blocks.Fire_Light;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MultiPlayerGameMode.class)
public abstract class LightFireBlockHitSoundBehavior {
    @Shadow @Final private Minecraft minecraft;

    @Redirect(
            method = "continueDestroyBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getSoundType()Lnet/minecraft/world/level/block/SoundType;"))
    private SoundType additionalLights$usePedestalHitSound(
            final BlockState state,
            final BlockPos pos,
            final Direction direction) {
        final ClientLevel level = this.minecraft.level;
        if (level == null) {
            return state.getSoundType();
        }
        if (state.getBlock() instanceof Fire_Light light) {
            final BlockPos target = light.resolveBreakTarget(level, pos);
            if (!target.equals(pos)) {
                return level.getBlockState(target).getSoundType();
            }
        }
        return state.getSoundType();
    }
}
