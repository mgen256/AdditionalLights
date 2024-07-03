package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.world.World;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;

public interface IHasFire {
    EnumProperty<FireTypes> FIRE_TYPE = EnumProperty.of("firetype", FireTypes.class);
    EnumProperty<FireTypes> PREVIOUS_FIRE_TYPE = EnumProperty.of("previous_firetype", FireTypes.class);

    default BlockState setFireType(World world, BlockPos pos, BlockState state, FireTypes newFireType, FireTypes prevFireType) {
        var newState = state
            .with(FIRE_TYPE, newFireType)
            .with(PREVIOUS_FIRE_TYPE, prevFireType);

        if (world.setBlockState(pos, newState, 3)) {
            return newState;
        }
        return state;
    }
}