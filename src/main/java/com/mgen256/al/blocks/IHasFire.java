package com.mgen256.al.blocks;

import com.mgen256.al.*;

import net.minecraft.block.*;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public interface IHasFire {
    public static EnumProperty<FireTypes> FIRE_TYPE = EnumProperty.create( "firetype", FireTypes.class );
    public static EnumProperty<FireTypes> PREVIOUS_FIRE_TYPE = EnumProperty.create( "previous_firetype", FireTypes.class );

    default BlockState setFireType( World worldIn, BlockPos pos, BlockState state, FireTypes fireType, FireTypes prevFireType ) {
        
        BlockState newState = state.with( FIRE_TYPE, fireType ).with( PREVIOUS_FIRE_TYPE, prevFireType );
        if( worldIn.setBlockState( pos, newState ) )
            return newState;
        return state;
    }
}