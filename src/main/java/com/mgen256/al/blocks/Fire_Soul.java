package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;

public class Fire_Soul extends FireBase {
    
    public static BooleanProperty SET = BooleanProperty.create("set");

    public Fire_Soul( PedestalTypes pedestalKey ) {
        super( pedestalKey, createProps( MapColor.COLOR_LIGHT_BLUE )
            .lightLevel( lightLevel -> 10 ) );
      }

    @Override
    protected float getFireDamageAmount() {
        return 0.0F;
    }
}