package com.mgen256.al.blocks;

import net.minecraft.block.Block;


public class FirePit_S extends FirePitBase {

    public FirePit_S(Block mainblock) {
        super("fire_pit_s_"
        , mainblock
        , Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D));
    }

    @Override
    protected double getSmokePos_Y() {
        return 0.8D;
    }
 
}
