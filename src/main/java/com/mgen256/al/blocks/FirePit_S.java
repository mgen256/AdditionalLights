package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class FirePit_S extends FirePitBase {

    private static final VoxelShape PART_LOWER = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
    private static final VoxelShape PART_UPPER = Block.createCuboidShape(0.0D, 4.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    public FirePit_S(Block mainblock) {
        super(mainblock, VoxelShapes.union(PART_LOWER, PART_UPPER), SIZE.S);
    }

    @Override
    public PedestalTypes getType() {
        return PedestalTypes.fire_pit_s;
    }

    @Override
    protected ModBlockList getFireBlock(BlockState state) {
        switch (state.get(FIRE_TYPE)) {
          case SOUL:
              return ModBlockList.SoulFire_For_FirePit_S;
          default:
              return ModBlockList.Fire_For_FirePit_S;
        }
    }
}
