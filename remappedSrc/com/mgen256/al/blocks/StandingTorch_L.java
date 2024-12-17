package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class StandingTorch_L extends StandingTorchBase {

    private static final VoxelShape PART_LOWER1 = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D);
    private static final VoxelShape PART_LOWER2 = Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 4.0D, 11.0D);
    private static final VoxelShape PART_MID    = Block.createCuboidShape(6.0D, 4.0D, 6.0D, 10.0D, 12.0D, 10.0D);
    private static final VoxelShape PART_UPPER1 = Block.createCuboidShape(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D);
    private static final VoxelShape PART_UPPER2 = Block.createCuboidShape(4.0D, 14.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public StandingTorch_L(Block mainblock, RegistryKey<Block> key ) {
        super( mainblock, key, VoxelShapes.union( PART_LOWER1, PART_LOWER2, PART_MID, PART_UPPER1, PART_UPPER2 ), SIZE.L );
      }


    @Override
    public PedestalTypes getType(){ return PedestalTypes.standing_torch_l; }


    @Override
    protected ModBlockList getFireBlock( BlockState state ) {
      switch( state.get(FIRE_TYPE) )
      {
        case SOUL:
          return ModBlockList.SoulFire_For_StandingTorch_L;
        default:
          return ModBlockList.Fire_For_StandingTorch_L;
      }
    }
}
