package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.*;
import net.minecraft.util.math.shapes.*;

public class FirePit_L extends FirePitBase {

    private static final VoxelShape PART_LOWER = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
    private static final VoxelShape PART_MID   = Block.makeCuboidShape(2.0D, 6.0D, 2.0D, 14.0D, 10.0D, 14.0D);
    private static final VoxelShape PART_UPPER = Block.makeCuboidShape(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public FirePit_L(Block mainblock) {
        super("fire_pit_l_"
        , mainblock
        , VoxelShapes.or( PART_LOWER, PART_MID, PART_UPPER ) );

    }

    @Override
    protected ModBlockList getFireKey( BlockState state ) {
      Log("retern " + state.get(FIRE_TYPE));
      switch( state.get(FIRE_TYPE) )
      {
        case SOUL:
          return ModBlockList.SoulFire_For_FirePit_L;
        default:
          return ModBlockList.Fire_For_FirePit_L;
      }
    }
}
