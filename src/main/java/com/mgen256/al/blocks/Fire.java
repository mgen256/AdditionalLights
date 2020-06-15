package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.*;
import net.minecraft.state.BooleanProperty;

public class Fire extends FireBase {
    
    public static BooleanProperty SET = BooleanProperty.create("set");

    public Fire( PedestalBlockList pedestalKey ) {
        super( "fire_for_", pedestalKey );
      }

    protected float getFireDamageAmount() {
        return 0.0F;
    }

    @Override
    public int getLightValue(BlockState state) {
        return 15;
     }
}