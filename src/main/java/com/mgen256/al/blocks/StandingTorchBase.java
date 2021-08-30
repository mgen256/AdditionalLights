
package com.mgen256.al.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;


public abstract class StandingTorchBase extends Pedestal  {

    public StandingTorchBase(String basename, Block mainblock, VoxelShape shape) {
        super(basename, mainblock, shape);
    }
}