package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.Blocks;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.BooleanProperty;

public class Fire extends FireBase {
    
    public static final BooleanProperty SET = BooleanProperty.of("set");

    public Fire( PedestalTypes pedestalKey ) {
        super(pedestalKey, Settings.create()
            .noCollision()
            .breakInstantly()
            .mapColor(Blocks.FIRE.getDefaultState().getMapColor(null, null))
            .sounds(BlockSoundGroup.WOOL)
            .luminance(state -> 15));
    }
}