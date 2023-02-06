package com.mgen256.al.blocks;

import com.mgen256.al.ModBlockList;
import com.mgen256.al.PedestalTypes;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class FirePit_S extends FirePitBase {

    private static final VoxelShape PART_LOWER = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
    private static final VoxelShape PART_UPPER = Block.box(0.0D, 4.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    public FirePit_S(Block mainblock) {
        super(  mainblock, Shapes.or( PART_LOWER, PART_UPPER ), SIZE.S );
    }

    
    @Override
    public PedestalTypes getType(){ return PedestalTypes.fire_pit_s; }


    @Override
    protected ModBlockList getFireKey( BlockState state ) {
      switch( state.getValue(FIRE_TYPE) )
      {
        case SOUL:
          return ModBlockList.SoulFire_For_FirePit_S;
        default:
          return ModBlockList.Fire_For_FirePit_S;
      }
    }
}
