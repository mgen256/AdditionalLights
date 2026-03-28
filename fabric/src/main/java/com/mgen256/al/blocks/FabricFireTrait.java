package com.mgen256.al.blocks;

import com.mgen256.al.FireTypesAdapter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import com.mgen256.al.FabricFireTypesAdapter;

public interface FabricFireTrait extends com.mgen256.al.blocks.FireTrait<Level, BlockPos, BlockState> {
    EnumProperty<FabricFireTypesAdapter> FIRE_TYPE = EnumProperty.create("firetype", FabricFireTypesAdapter.class);
    EnumProperty<FabricFireTypesAdapter> PREVIOUS_FIRE_TYPE = EnumProperty.create("previous_firetype", FabricFireTypesAdapter.class);

    @Override
    default BlockState withFireType(BlockState state, FireTypesAdapter type) {
        return state.setValue(FIRE_TYPE, FabricFireTypesAdapter.fromCore(type.toCore()));
    }

    @Override
    default BlockState withPreviousFireType(BlockState state, FireTypesAdapter type) {
        return state.setValue(PREVIOUS_FIRE_TYPE, FabricFireTypesAdapter.fromCore(type.toCore()));
    }

    @Override
    default boolean setBlockState(Level world, BlockPos pos, BlockState state) {
        return world.setBlock(pos, state, 3);
    }

    @Override
    default FireTypesAdapter getFireType(BlockState state) {
        return FireTypesAdapter.fromCore(state.getValue(FIRE_TYPE).toCore());
    }

    @Override
    default FireTypesAdapter getPreviousFireType(BlockState state) {
        return FireTypesAdapter.fromCore(state.getValue(PREVIOUS_FIRE_TYPE).toCore());
    }
}
