package com.mgen256.al.items;

import com.mgen256.al.AdditionalLightsNeoForge;
import com.mgen256.al.FireTypes;
import com.mgen256.al.NeoForgeFireTypesAdapter;
import com.mgen256.al.ModSoundList;
import com.mgen256.al.blocks.*;
import com.mgen256.al.items.SoulWandCore;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ClipContext;

public class SoulWand extends Wand implements SoulWandTrait {

    private static final SoulWandCore CORE = new SoulWandCore();
    
    private static Properties createProps(String name){
        Properties p = new Item.Properties()
                .setId(AdditionalLightsNeoForge.createItemResourceKey(name));
        p.stacksTo(1);
        p.durability(1);
        return p;
    }

    public SoulWand(String name) {
        super( createProps(name), name );
    }


    @Override
    public InteractionResult use(Level level, Player player, InteractionHand handIn) {
        var hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        var pos = hitresult.getBlockPos();
        var state = level.getBlockState(pos);
        var block = state.getBlock();

        if (block instanceof NeoForgeFireTrait trait) {
            changeFire(level, player, pos, state, trait);
            return InteractionResult.SUCCESS;
        }
        else if (block instanceof FireBase) {
            var underPos = pos.below();
            var underBlock = level.getBlockState(underPos).getBlock();
            if (underBlock instanceof NeoForgeFireTrait underTrait) {
                changeFire(level, player, underPos, level.getBlockState(underPos), underTrait);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }


    private void changeFire( Level level, Player player, BlockPos pos, BlockState state, NeoForgeFireTrait modblock ) {
        int previousOutput = modblock instanceof Pedestal pedestal ? pedestal.getComparatorOutput(level, pos, state) : 0;

        FireTypes currentType = state.getValue(NeoForgeFireTrait.FIRE_TYPE).toCore();
        FireTypes prevType = state.getValue(NeoForgeFireTrait.PREVIOUS_FIRE_TYPE).toCore();

        BlockState[] stateRef = new BlockState[] { state };
        CORE.changeFire(currentType, prevType, player.isSuppressingSlidingDownLadder(), (n, p, sound, volume) -> {
            BlockState newState = modblock.setFireType(level, pos, stateRef[0], n, p);
            var s = sound == ModSoundList.Change ? SoundEvents.CHANGE : SoundEvents.UNDO;
            playSound(level, player, s, volume);
            stateRef[0] = newState;
        });

        state = stateRef[0];

        if (modblock instanceof Pedestal)
        {
            boolean replaceOnly = FireTypeWandCore.shouldReplacePedestalFireOnly(currentType);
            Pedestal pedestal = (Pedestal)modblock;
            pedestal.igniteFire(level, pos, state, replaceOnly);
            pedestal.updateComparatorOutputIfChanged(level, pos, previousOutput);
        }
    }
}
