package com.mgen256.al.blocks;

import com.mgen256.al.FireTypesAdapter;
import com.mgen256.al.NeoForgeFireTypesAdapter;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.core.BlockPos;

public interface NeoForgeFireTrait extends com.mgen256.al.blocks.FireTrait<Level, BlockPos, BlockState> {
    EnumProperty<NeoForgeFireTypesAdapter> FIRE_TYPE = EnumProperty.create("firetype", NeoForgeFireTypesAdapter.class);
    EnumProperty<NeoForgeFireTypesAdapter> PREVIOUS_FIRE_TYPE = EnumProperty.create("previous_firetype", NeoForgeFireTypesAdapter.class);

    @Override
    default BlockState withFireType(BlockState state, FireTypesAdapter type) {
        return state.setValue(FIRE_TYPE, NeoForgeFireTypesAdapter.fromCore(type.toCore()));
    }

    @Override
    default BlockState withPreviousFireType(BlockState state, FireTypesAdapter type) {
        return state.setValue(PREVIOUS_FIRE_TYPE, NeoForgeFireTypesAdapter.fromCore(type.toCore()));
    }

    @Override
    default boolean setBlockState(Level level, BlockPos pos, BlockState state) {
        return level.setBlockAndUpdate(pos, state);
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
