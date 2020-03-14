package com.mgen256.al.blocks;


import net.minecraft.block.Block;

public class StandingTorch_L extends StandingTorchBase {

    public StandingTorch_L(Block mainblock ) {
        super( "standing_torch_l_"
        , mainblock
        , Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D));
      }

      @Override
      protected double getSmokePos_Y() {
          return 1.4D;
      }
}
