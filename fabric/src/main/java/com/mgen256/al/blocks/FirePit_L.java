package com.mgen256.al.blocks;

import com.mgen256.al.BlockSpec;
import com.mgen256.al.PedestalTypes;
import com.mgen256.al.FireTypes;
import com.mgen256.al.blocks.FirePitLSpec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.mgen256.al.PedestalSize;

public class FirePit_L extends FirePitBase implements FirePitLSpec {

    private static final VoxelShape PART_LOWER = Block.box(
        LOWER_BOX[0], LOWER_BOX[1], LOWER_BOX[2], LOWER_BOX[3], LOWER_BOX[4], LOWER_BOX[5]);
    private static final VoxelShape PART_MID   = Block.box(
        MID_BOX[0], MID_BOX[1], MID_BOX[2], MID_BOX[3], MID_BOX[4], MID_BOX[5]);
    private static final VoxelShape PART_UPPER = Block.box(
        UPPER_BOX[0], UPPER_BOX[1], UPPER_BOX[2], UPPER_BOX[3], UPPER_BOX[4], UPPER_BOX[5]);

    public FirePit_L(Block mainblock, ResourceKey<Block> key) {
        super(mainblock, key, Shapes.or(PART_LOWER, PART_MID, PART_UPPER), PedestalSize.L);
    }

    @Override
    public PedestalTypes getType() {
        return FirePitLSpec.super.getType();
    }

    @Override
    protected BlockSpec getFireBlock(BlockState state) {
        return getFireFromType(state.getValue(FIRE_TYPE).toCore());
    }
}
