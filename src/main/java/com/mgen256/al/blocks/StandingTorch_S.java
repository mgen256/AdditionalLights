package com.mgen256.al.blocks;


import net.minecraft.block.Block;

public class StandingTorch_S extends StandingTorchBase{

    public StandingTorch_S( Block mainblock ) {
      super( "standing_torch_s_"
      , mainblock      
      , Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 12.0D, 12.0D) );
    }

    @Override
    protected double getSmokePos_Y() {
        return 0.9D;
    }
}
