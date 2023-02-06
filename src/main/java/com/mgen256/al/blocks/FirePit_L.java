package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FirePit_L extends FirePitBase {

    private static final VoxelShape PART_LOWER = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
    private static final VoxelShape PART_MID   = Block.box(2.0D, 6.0D, 2.0D, 14.0D, 10.0D, 14.0D);
    private static final VoxelShape PART_UPPER = Block.box(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public FirePit_L(Block mainblock) {
        super( mainblock, Shapes.or( PART_LOWER, PART_MID, PART_UPPER ), SIZE.L );
    }


    @Override
    public PedestalTypes getType(){ return PedestalTypes.fire_pit_l; }


    @Override
    protected ModBlockList getFireKey( BlockState state ) {
      switch( state.getValue(FIRE_TYPE) )
      {
        case SOUL:
          return ModBlockList.SoulFire_For_FirePit_L;
        default:
          return ModBlockList.Fire_For_FirePit_L;
      }
    }
}
