package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;


public class StandingTorch_L extends StandingTorchBase{

  private static AxisAlignedBB SHAPE = new AxisAlignedBB(0.25, 0, 0.25, 0.75, 1, 0.75);

  public StandingTorch_L(Block mainblock, String mainblockName) {
    super("standing_torch_l_", mainblock, mainblockName );

  }

  @Override
  protected ModBlockList getFireKey() {
    return ModBlockList.Fire_For_StandingTorch_L;
  }

  @Override
  public AxisAlignedBB getShapes(IBlockState state) {
    return SHAPE;
  }
}

