package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.BooleanProperty;

public class Fire_Soul extends FireBase {
    
    public static BooleanProperty SET = BooleanProperty.create("set");

    public Fire_Soul( PedestalTypes pedestalKey ) {
        super( "soul_fire_for_", pedestalKey, createProps( MaterialColor.DIAMOND )
            .setLightLevel( lightLevel -> 10 ) );
      }

    protected float getFireDamageAmount() {
        return 0.0F;
    }
}