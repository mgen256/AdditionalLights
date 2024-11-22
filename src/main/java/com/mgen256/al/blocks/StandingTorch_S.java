package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class StandingTorch_S extends StandingTorchBase{

    private static final VoxelShape PART_LOWER = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D);
    private static final VoxelShape PART_MID   = Block.createCuboidShape(6.0D, 2.0D, 6.0D, 10.0D, 8.0D, 10.0D);
    private static final VoxelShape PART_UPPER = Block.createCuboidShape(4.0D, 8.0D, 4.0D, 12.0D, 12.0D, 12.0D);

  
    public StandingTorch_S( Block mainblock, RegistryKey<Block> key ) {
      super( mainblock, key, VoxelShapes.union( PART_LOWER, PART_MID, PART_UPPER ), SIZE.S );
    }


    @Override
    public PedestalTypes getType(){ return PedestalTypes.standing_torch_s; }
    

    @Override
    protected ModBlockList getFireBlock( BlockState state ) {
      switch( state.get(FIRE_TYPE) )
      {
        case SOUL:
          return ModBlockList.SoulFire_For_StandingTorch_S;
        default:
          return ModBlockList.Fire_For_StandingTorch_S;
      }
    }
}
