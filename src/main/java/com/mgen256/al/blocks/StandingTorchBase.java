package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.shape.VoxelShape;

public abstract class StandingTorchBase extends Pedestal  {

    public StandingTorchBase(Block mainblock, VoxelShape shape, SIZE size) {
        super(mainblock, shape, size);
    }
}