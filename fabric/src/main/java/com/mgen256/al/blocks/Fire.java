package com.mgen256.al.blocks;

import com.mgen256.al.PedestalTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class Fire extends FireBase {
    
    public static final BooleanProperty SET = BooleanProperty.create("set");

    public Fire( PedestalTypes pedestalKey, ResourceKey<Block> key ) {
        super(pedestalKey, Properties.of()
            .noCollision()
            .instabreak()
            .mapColor(Blocks.FIRE.defaultBlockState().getMapColor(null, null))
            .sound(SoundType.WOOL)
            .lightLevel(state -> 15)
            .setId(key)
            , key);
    }
}