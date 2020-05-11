package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class FirePit_L extends FirePitBase {
    
    private static final VoxelShape PART_LOWER = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
    private static final VoxelShape PART_MID   = Block.makeCuboidShape(2.0D, 6.0D, 2.0D, 14.0D, 10.0D, 14.0D);
    private static final VoxelShape PART_UPPER = Block.makeCuboidShape(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public FirePit_L(Block mainblock) {
        super("fire_pit_l_"
        , mainblock
        , VoxelShapes.or( PART_LOWER, PART_MID, PART_UPPER ) );
    }

}
