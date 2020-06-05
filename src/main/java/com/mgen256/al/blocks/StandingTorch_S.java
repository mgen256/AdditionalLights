package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

public class StandingTorch_S extends StandingTorchBase{

  private static AxisAlignedBB SHAPE = new AxisAlignedBB(0.25, 0, 0.25, 0.75, 0.75, 0.75);

  public StandingTorch_S(Block mainblock, String mainblockName) {
    super("standing_torch_s_", mainblock, mainblockName );

  }

  @Override
  protected ModBlockList getFireKey() {
    return ModBlockList.Fire_For_StandingTorch_S;
  }

  @Override
  public AxisAlignedBB getShapes(IBlockState state) {
    return SHAPE;
  }
}
