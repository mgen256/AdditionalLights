package com.mgen256.al.blocks;

import com.mgen256.al.ModBlockList;
import com.mgen256.al.PedestalTypes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class StandingTorch_S extends StandingTorchBase{

    private static final VoxelShape PART_LOWER = Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D);
    private static final VoxelShape PART_MID   = Block.makeCuboidShape(6.0D, 2.0D, 6.0D, 10.0D, 8.0D, 10.0D);
    private static final VoxelShape PART_UPPER = Block.makeCuboidShape(4.0D, 8.0D, 4.0D, 12.0D, 12.0D, 12.0D);

  
    public StandingTorch_S( Block mainblock ) {
      super( "standing_torch_s_"
      , mainblock      
      , VoxelShapes.or( PART_LOWER, PART_MID, PART_UPPER ) );
    }


    @Override
    public PedestalTypes getType(){ return PedestalTypes.standing_torch_s; }
    

    @Override
    protected ModBlockList getFireKey( BlockState state ) {
      switch( state.get(FIRE_TYPE) )
      {
        case SOUL:
          return ModBlockList.SoulFire_For_StandingTorch_S;
        default:
          return ModBlockList.Fire_For_StandingTorch_S;
      }
    }
}
