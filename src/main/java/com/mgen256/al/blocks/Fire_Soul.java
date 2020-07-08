package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.*;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class Fire_Soul extends FireBase {
    
    public static BooleanProperty SET = BooleanProperty.create("set");

    public Fire_Soul( PedestalTypes pedestalKey ) {
        super( "soul_fire_for_", pedestalKey );
      }

    protected float getFireDamageAmount() {
        return 0.0F;
    }

    @Override
    public int getLightValue(BlockState state) {
        return 10;
     }

     @Override
     public MaterialColor getMaterialColor(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return MaterialColor.DIAMOND;
     }
}