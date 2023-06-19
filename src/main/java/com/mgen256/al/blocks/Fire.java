package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;

public class Fire extends FireBase {
    
    public static BooleanProperty SET = BooleanProperty.create("set");

    public Fire( PedestalTypes pedestalKey ) {
        super( pedestalKey, createProps( MapColor.FIRE )
            .lightLevel( lightLevel-> 15 ) );
      }

    @Override
    protected float getFireDamageAmount() {
        return 0.0F;
    }
}