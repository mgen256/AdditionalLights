package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.VoxelShape;


public abstract class FirePitBase extends Pedestal  {

    public FirePitBase( String basename, Block mainblock, VoxelShape shape ) {
        super(basename, mainblock, shape);
    }
}
