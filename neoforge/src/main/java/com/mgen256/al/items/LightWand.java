package com.mgen256.al.items;

import com.mgen256.al.AdditionalLightsNeoForge;
import com.mgen256.al.FireTypes;
import com.mgen256.al.blocks.FireBase;
import com.mgen256.al.blocks.LightWandTargetSpec;
import com.mgen256.al.blocks.NeoForgeFireTrait;
import com.mgen256.al.ModSoundList;
import com.mgen256.al.blocks.Pedestal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ClipContext;

public class LightWand extends Wand implements LightWandTrait {

    private static final LightWandCore CORE = new LightWandCore();

    private static Properties createProps(String name) {
        Properties p = new Item.Properties()
                .setId(AdditionalLightsNeoForge.createItemResourceKey(name));
        p.stacksTo(1);
        p.durability(1);
        return p;
    }

    public LightWand(String name) {
        super(createProps(name), name);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand handIn) {
        var hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        var pos = hitresult.getBlockPos();
        var state = level.getBlockState(pos);
        var block = state.getBlock();

        if (block instanceof NeoForgeFireTrait trait && block instanceof LightWandTargetSpec) {
            changeFire(level, player, pos, state, trait);
            return InteractionResult.SUCCESS;
        } else if (block instanceof FireBase) {
            var underPos = pos.below();
            var underState = level.getBlockState(underPos);
            var underBlock = underState.getBlock();
            if (underBlock instanceof NeoForgeFireTrait underTrait && underBlock instanceof LightWandTargetSpec) {
                changeFire(level, player, underPos, underState, underTrait);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private void changeFire(Level level, Player player, BlockPos pos, BlockState state, NeoForgeFireTrait modblock) {
        int previousOutput = modblock instanceof Pedestal pedestal ? pedestal.getComparatorOutput(level, pos, state) : 0;
        var currentType = state.getValue(NeoForgeFireTrait.FIRE_TYPE).toCore();
        var prevType = state.getValue(NeoForgeFireTrait.PREVIOUS_FIRE_TYPE).toCore();

        BlockState[] stateRef = new BlockState[] { state };
        CORE.changeFire(currentType, prevType, player.isSuppressingSlidingDownLadder(), (n, p, sound, volume) -> {
            BlockState newState = modblock.setFireType(level, pos, stateRef[0], n, p);
            var s = sound == ModSoundList.Change ? SoundEvents.CHANGE : SoundEvents.UNDO;
            playSound(level, player, s, volume);
            stateRef[0] = newState;
        });

        state = stateRef[0];
        if (modblock instanceof Pedestal) {
            Pedestal pedestal = (Pedestal) modblock;
            if (state.getValue(NeoForgeFireTrait.FIRE_TYPE).toCore() == FireTypes.LIGHT) {
                state = pedestal.updateLightLit(level, pos, state, true);
            }
            boolean replaceOnly = FireTypeWandCore.shouldReplacePedestalFireOnly(currentType);
            pedestal.igniteFire(level, pos, state, replaceOnly);
            pedestal.updateComparatorOutputIfChanged(level, pos, previousOutput);
        }
    }
}
