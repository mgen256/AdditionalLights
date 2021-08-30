package com.mgen256.al.blocks;

import com.mgen256.al.ModBlockList;
import com.mgen256.al.PedestalTypes;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StandingTorch_L extends StandingTorchBase {

    private static final VoxelShape PART_LOWER1 = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D);
    private static final VoxelShape PART_LOWER2 = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 4.0D, 11.0D);
    private static final VoxelShape PART_MID    = Block.box(6.0D, 4.0D, 6.0D, 10.0D, 12.0D, 10.0D);
    private static final VoxelShape PART_UPPER1 = Block.box(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D);
    private static final VoxelShape PART_UPPER2 = Block.box(4.0D, 14.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public StandingTorch_L(Block mainblock ) {
        super( "standing_torch_l_"
        , mainblock
        , Shapes.or( PART_LOWER1, PART_LOWER2, PART_MID, PART_UPPER1, PART_UPPER2 ) );
      }


    @Override
    public PedestalTypes getType(){ return PedestalTypes.standing_torch_l; }


    @Override
    protected ModBlockList getFireKey( BlockState state ) {
      switch( state.getValue(FIRE_TYPE) )
      {
        case SOUL:
          return ModBlockList.SoulFire_For_StandingTorch_L;
        default:
          return ModBlockList.Fire_For_StandingTorch_L;
      }
    }
}
