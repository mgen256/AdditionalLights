package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.BooleanProperty;

public class Fire_Soul extends FireBase {
    
    public static final BooleanProperty SET = BooleanProperty.of("set");

    public Fire_Soul( PedestalTypes pedestalKey, RegistryKey<Block> key ) {
        super( pedestalKey, Settings.create()
            .noCollision()
            .breakInstantly()
            .mapColor(Blocks.SOUL_FIRE.getDefaultState().getMapColor(null, null))
            .sounds(BlockSoundGroup.WOOL)
            .luminance(state -> 10) 
            .registryKey(key)
            );
      }
}