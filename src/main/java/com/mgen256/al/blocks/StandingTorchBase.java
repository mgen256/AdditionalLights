
package com.mgen256.al.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;


public abstract class StandingTorchBase extends Pedestal  {

    public StandingTorchBase(Block mainblock, String name, VoxelShape shape, SIZE size) {
        super(mainblock, name, shape, size);
    }
}