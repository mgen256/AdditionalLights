package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class StandingTorch_L extends StandingTorchBase {

    private static final VoxelShape PART_LOWER1 = Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D);
    private static final VoxelShape PART_LOWER2 = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 4.0D, 11.0D);
    private static final VoxelShape PART_MID   = Block.makeCuboidShape(6.0D, 4.0D, 6.0D, 10.0D, 12.0D, 10.0D);
    private static final VoxelShape PART_UPPER1 = Block.makeCuboidShape(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D);
    private static final VoxelShape PART_UPPER2 = Block.makeCuboidShape(4.0D, 14.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public StandingTorch_L(Block mainblock ) {
        super( "standing_torch_l_"
        , mainblock
        , VoxelShapes.or( PART_LOWER1, PART_LOWER2, PART_MID, PART_UPPER1, PART_UPPER2 ) );
      }

}
