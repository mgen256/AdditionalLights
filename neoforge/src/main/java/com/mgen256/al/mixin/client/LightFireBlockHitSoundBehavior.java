package com.mgen256.al.mixin.client;

import com.mgen256.al.blocks.Fire_Light;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientLevel.class)
public abstract class LightFireBlockHitSoundBehavior {
    @Redirect(
            method = "playBreakingSound",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getSoundType()Lnet/minecraft/world/level/block/SoundType;"))
    private SoundType additionalLights$usePedestalHitSound(
            final BlockState state,
            final BlockPos pos,
            final BlockState originalState) {
        if (state.getBlock() instanceof Fire_Light light) {
            final ClientLevel level = (ClientLevel) (Object) this;
            final BlockPos target = light.resolveBreakTarget(level, pos);
            if (!target.equals(pos)) {
                return level.getBlockState(target).getSoundType();
            }
        }
        return state.getSoundType();
    }
}
