package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

public class FirePit_S extends FirePitBase {

  private static AxisAlignedBB SHAPE = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);

  public FirePit_S(Block mainblock, String mainblockName) {
    super("fire_pit_s_", mainblock, mainblockName );

  }

  @Override
  protected ModBlockList getFireKey() {
    return ModBlockList.Fire_For_FirePit_S;
  }

  @Override
  public AxisAlignedBB getShapes(IBlockState state) {
    return SHAPE;
  }
}
