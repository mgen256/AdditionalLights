package com.mgen256.al.blocks;

import com.mgen256.al.BlockSpec;
import com.mgen256.al.PedestalTypes;
import com.mgen256.al.FireTypes;
import com.mgen256.al.blocks.StandingTorchLSpec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.mgen256.al.PedestalSize;

public class StandingTorch_L extends StandingTorchBase implements StandingTorchLSpec {

    private static final VoxelShape PART_LOWER1 = Block.box(
        LOWER_BOX1[0], LOWER_BOX1[1], LOWER_BOX1[2], LOWER_BOX1[3], LOWER_BOX1[4], LOWER_BOX1[5]);
    private static final VoxelShape PART_LOWER2 = Block.box(
        LOWER_BOX2[0], LOWER_BOX2[1], LOWER_BOX2[2], LOWER_BOX2[3], LOWER_BOX2[4], LOWER_BOX2[5]);
    private static final VoxelShape PART_MID    = Block.box(
        MID_BOX[0], MID_BOX[1], MID_BOX[2], MID_BOX[3], MID_BOX[4], MID_BOX[5]);
    private static final VoxelShape PART_UPPER1 = Block.box(
        UPPER_BOX1[0], UPPER_BOX1[1], UPPER_BOX1[2], UPPER_BOX1[3], UPPER_BOX1[4], UPPER_BOX1[5]);
    private static final VoxelShape PART_UPPER2 = Block.box(
        UPPER_BOX2[0], UPPER_BOX2[1], UPPER_BOX2[2], UPPER_BOX2[3], UPPER_BOX2[4], UPPER_BOX2[5]);

    public StandingTorch_L(Block mainblock, ResourceKey<Block> key ) {
        super( mainblock, key, Shapes.or( PART_LOWER1, PART_LOWER2, PART_MID, PART_UPPER1, PART_UPPER2 ), PedestalSize.L );
      }


    @Override
    public PedestalTypes getType(){ return StandingTorchLSpec.super.getType(); }


    @Override
    protected BlockSpec getFireBlock( BlockState state ) {
      return getFireFromType(state.getValue(FIRE_TYPE).toCore());
    }
}

