package com.mgen256.al.items;

import com.mgen256.al.AdditionalLights;
import com.mgen256.al.FireTypes;
import com.mgen256.al.blocks.*;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ClipContext;

public class SoulWand extends Wand {
    
    private static Properties createProps(String name){
        Properties p = new Item.Properties();
        p.stacksTo(1);
        p.durability(1);
        p.setId(AdditionalLights.createItemResourceKey(name));
        return p;
    }

    public SoulWand(String name) {
        super( createProps(name), name );
    }


    @Override
    public InteractionResult use(Level level, Player player, InteractionHand handIn) {
        var hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        var pos = hitresult.getBlockPos();
        var state = level.getBlockState(pos);
        var block = state.getBlock();

        if (block instanceof IHasFire) {
            changeFire(level, player, pos, state, (IHasFire)block);
            return InteractionResult.SUCCESS;
        }
        else if (block instanceof FireBase) {
            var underPos = pos.below();
            var underBlock = level.getBlockState(underPos).getBlock();
            if (underBlock instanceof IHasFire) {
                changeFire(level, player, underPos, level.getBlockState(underPos), (IHasFire)underBlock);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }


    private void changeFire( Level level, Player player, BlockPos pos, BlockState state, IHasFire modblock ) {

        FireTypes currentType = state.getValue( IHasFire.FIRE_TYPE );
        FireTypes prevType = state.getValue( IHasFire.PREVIOUS_FIRE_TYPE );
        if( prevType == FireTypes.SOUL )
            prevType = FireTypes.NORMAL;

        if( player.isSuppressingSlidingDownLadder() )
        {
            if( currentType == FireTypes.SOUL )
            {
                state = modblock.setFireType( level, pos, state, prevType, prevType );
                playSound( level, player, SoundEvents.UNDO, 0.6f );
            }
        }
        else
        {
            if( currentType != FireTypes.SOUL )
            {
                state = modblock.setFireType( level, pos, state, FireTypes.SOUL, prevType );
                playSound( level, player, SoundEvents.CHANGE, 0.8f );      
            }
        }

        if( modblock instanceof Pedestal )
            ((Pedestal)modblock).setFire(level, pos, state, true );
    }
}