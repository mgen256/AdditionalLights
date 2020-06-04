package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

public class FirePit_L extends FirePitBase {

  private static AxisAlignedBB SHAPE = new AxisAlignedBB(0, 0, 0, 1, 1, 1);

  public FirePit_L(Block mainblock, String mainblockName) {
    super("fire_pit_l_", mainblock, mainblockName );

  }

  @Override
  protected ModBlockList getFireKey() {
    return ModBlockList.Fire_For_FirePit_L;
  }

  @Override
  public AxisAlignedBB getShapes(IBlockState state) {
    return SHAPE;
  }
}
