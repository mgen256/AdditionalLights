package com.mgen256.al.blocks;
import com.mgen256.al.BlockSpec;

import com.mgen256.al.ModBlockList;
import com.mgen256.al.PedestalTypes;
import com.mgen256.al.FireTypes;
import com.mgen256.al.blocks.FirePitSSpec;
import com.mgen256.al.PedestalSize;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class FirePit_S extends FirePitBase implements FirePitSSpec {

    private static final VoxelShape PART_LOWER = Block.box(
        LOWER_BOX[0], LOWER_BOX[1], LOWER_BOX[2], LOWER_BOX[3], LOWER_BOX[4], LOWER_BOX[5]);
    private static final VoxelShape PART_UPPER = Block.box(
        UPPER_BOX[0], UPPER_BOX[1], UPPER_BOX[2], UPPER_BOX[3], UPPER_BOX[4], UPPER_BOX[5]);

    public FirePit_S(Block mainblock, String name) {
        super(  mainblock, name, Shapes.or( PART_LOWER, PART_UPPER ), PedestalSize.S );
    }

    
    @Override
    public PedestalTypes getType(){ return FirePitSSpec.super.getType(); }


    @Override
    protected BlockSpec getFireKey( BlockState state ) {
      return getFireFromType(state.getValue(FIRE_TYPE).toCore());
    }
}

