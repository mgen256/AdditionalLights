package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class FirePit_L extends FirePitBase {

    private static final VoxelShape PART_LOWER = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
    private static final VoxelShape PART_MID   = Block.createCuboidShape(2.0D, 6.0D, 2.0D, 14.0D, 10.0D, 14.0D);
    private static final VoxelShape PART_UPPER = Block.createCuboidShape(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public FirePit_L(Block mainblock) {
        super(mainblock, VoxelShapes.union(PART_LOWER, PART_MID, PART_UPPER), SIZE.L);
    }

    @Override
    public PedestalTypes getType() {
        return PedestalTypes.fire_pit_l;
    }

    @Override
    protected ModBlockList getFireBlock(BlockState state) {
        switch (state.get(FIRE_TYPE)) {
          case SOUL:
              return ModBlockList.SoulFire_For_FirePit_L;
          default:
              return ModBlockList.Fire_For_FirePit_L;
        }
    }
}