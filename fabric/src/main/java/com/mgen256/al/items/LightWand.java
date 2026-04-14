package com.mgen256.al.items;

import com.mgen256.al.AdditionalLightsFabric;
import com.mgen256.al.FireTypes;
import com.mgen256.al.blocks.FireBase;
import com.mgen256.al.blocks.FabricFireTrait;
import com.mgen256.al.blocks.LightWandTargetSpec;
import com.mgen256.al.blocks.Pedestal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class LightWand extends Wand implements LightWandTrait {

    private static final LightWandCore CORE = new LightWandCore();

    private static Properties createSettings(String name) {
        return new Item.Properties()
                .stacksTo(1)
                .durability(1)
                .setId(AdditionalLightsFabric.createItemRegistryKey(name));
    }

    public LightWand(String name) {
        super(createSettings(name), name);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        var hitResult = (BlockHitResult) user.pick(20.0D, 0.0F, false);
        var pos = hitResult.getBlockPos();
        var state = world.getBlockState(pos);
        var block = state.getBlock();

        if (block instanceof FabricFireTrait trait && block instanceof LightWandTargetSpec) {
            changeFire(world, user, pos, state, trait);
            return InteractionResult.SUCCESS;
        } else if (block instanceof FireBase) {
            var underPos = pos.below();
            var underState = world.getBlockState(underPos);
            var underBlock = underState.getBlock();
            if (underBlock instanceof FabricFireTrait traitUnder && underBlock instanceof LightWandTargetSpec) {
                changeFire(world, user, underPos, underState, traitUnder);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private void changeFire(Level world, Player player, BlockPos pos, BlockState state, FabricFireTrait modblock) {
        int previousOutput = modblock instanceof Pedestal pedestal ? pedestal.getComparatorOutput(world, pos, state) : 0;
        var currentType = state.getValue(FabricFireTrait.FIRE_TYPE).toCore();
        var prevType = state.getValue(FabricFireTrait.PREVIOUS_FIRE_TYPE).toCore();

        BlockState[] stateRef = new BlockState[] { state };
        CORE.changeFire(currentType, prevType, player.isShiftKeyDown(), (n, p, sound, volume) -> {
            BlockState newState = modblock.setFireType(world, pos, stateRef[0], n, p);
            playSound(world, player, AdditionalLightsFabric.getSound(sound), volume);
            stateRef[0] = newState;
        });

        state = stateRef[0];
        if (modblock instanceof Pedestal) {
            Pedestal pedestal = (Pedestal) modblock;
            if (state.getValue(FabricFireTrait.FIRE_TYPE).toCore() == FireTypes.LIGHT) {
                state = pedestal.updateLightLit(world, pos, state, true);
            }
            boolean replaceOnly = FireTypeWandCore.shouldReplacePedestalFireOnly(currentType);
            pedestal.igniteFire(world, pos, state, replaceOnly);
            pedestal.updateComparatorOutputIfChanged(world, pos, previousOutput);
        }
    }
}
