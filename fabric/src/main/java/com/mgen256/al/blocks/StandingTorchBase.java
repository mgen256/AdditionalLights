package com.mgen256.al.blocks;

import com.mgen256.al.PedestalSize;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class StandingTorchBase extends Pedestal  {

    public StandingTorchBase(Block mainblock, ResourceKey<Block> key, VoxelShape shape, PedestalSize size) {
        super(mainblock, key, shape, size);
    }
}