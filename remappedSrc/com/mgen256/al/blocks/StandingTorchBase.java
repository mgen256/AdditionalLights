package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShape;

public abstract class StandingTorchBase extends Pedestal  {

    public StandingTorchBase(Block mainblock, RegistryKey<Block> key, VoxelShape shape, SIZE size) {
        super(mainblock, key, shape, size);
    }
}