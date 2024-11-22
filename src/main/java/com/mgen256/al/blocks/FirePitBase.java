package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShape;

public abstract class FirePitBase extends Pedestal  {

    public FirePitBase( Block mainblock, RegistryKey<Block> key, VoxelShape shape, SIZE size ) {
        super(mainblock, key, shape, size);
    }
}
