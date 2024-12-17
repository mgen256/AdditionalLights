package com.mgen256.al.items;

import com.mgen256.al.*;
import com.mgen256.al.blocks.*;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.BlockState;


public class SoulWand extends Wand {
        
    private static net.minecraft.item.Item.Settings createSettings(RegistryKey<Item> key) {
        return new Item.Settings()
        .maxCount(1)
        .maxDamage(1)
        .registryKey(key)
        ;
    }

    public SoulWand(RegistryKey<Item> key) {
        super(createSettings(key));
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        var hitResult = (BlockHitResult) user.raycast(20.0D, 0.0F, false);
        var pos = hitResult.getBlockPos();
        var state = world.getBlockState(pos);
        var block = state.getBlock();

        if (block instanceof IHasFire) {
            changeFire(world, user, pos, state, (IHasFire) block);
        } 
        else if (block instanceof FireBase) {
            var underPos = pos.down();
            var underBlock = world.getBlockState(underPos).getBlock();
            if (underBlock instanceof IHasFire) {
                changeFire(world, user, underPos, world.getBlockState(underPos), (IHasFire) underBlock);
            }
        }
        return ActionResult.CONSUME;
    }
    
    private void changeFire(World world, PlayerEntity player, BlockPos pos, BlockState state, IHasFire modblock) {
        var currentType = state.get(IHasFire.FIRE_TYPE);
        var prevType = state.get(IHasFire.PREVIOUS_FIRE_TYPE);
        if (prevType == FireTypes.SOUL) {
            prevType = FireTypes.NORMAL;
        }

        if (player.isSneaking()) {
            if (currentType == FireTypes.SOUL) {
                state = modblock.setFireType(world, pos, state, prevType, prevType);
                playSound(world, player, ModSoundList.Undo.get(), 0.6f);
            }
        } else {
            if (currentType != FireTypes.SOUL) {
                state = modblock.setFireType(world, pos, state, FireTypes.SOUL, prevType);
                playSound(world, player, ModSoundList.Change.get(), 0.8f);
            }
        }

        if (modblock instanceof Pedestal) {
            ((Pedestal) modblock).igniteFire(world, pos, state, true);
        }
    }
}