package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.BooleanProperty;

public class Fire extends FireBase {
    
    public static BooleanProperty SET = BooleanProperty.create("set");

    public Fire( PedestalTypes pedestalKey ) {
        super( "fire_for_", pedestalKey, createProps( MaterialColor.ADOBE )
            .setLightValue( p_235838_1_-> 15 ) );
      }

    protected float getFireDamageAmount() {
        return 0.0F;
    }
}