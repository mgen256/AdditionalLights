package com.mgen256.al.blocks;

import net.minecraft.block.Block;

public class FirePit_L extends FirePitBase {

    public FirePit_L(Block mainblock) {
        super("fire_pit_l_"
        , mainblock
        , Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.99D, 16.0D));

    }

    @Override
    protected double getSmokePos_Y() {
        return 1.4D;
    }
}
