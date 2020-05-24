package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.VoxelShape;


public abstract class FirePitBase extends Pedestal  {

    private static Properties createProps(Block mainblock){
        Properties p = createBasicProps(mainblock);
        p.lightValue(0);
        return p;
    }

    public FirePitBase( String basename, Block mainblock, VoxelShape shape ) {
        super(basename, mainblock, createProps(mainblock), shape);
    }
}
