package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;


public class FirePit_S extends FirePitBase {
    
    private static final VoxelShape PART_LOWER = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
    private static final VoxelShape PART_UPPER = Block.makeCuboidShape(0.0D, 4.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    public FirePit_S(Block mainblock) {
        super("fire_pit_s_"
        , mainblock
        , VoxelShapes.or( PART_LOWER, PART_UPPER ) );
    }

    @Override
    protected double getSmokePos_Y() {
        return 0.8D;
    }
 
}
