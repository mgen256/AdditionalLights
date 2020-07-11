package com.mgen256.al.items;

import com.mgen256.al.FireTypes;
import com.mgen256.al.blocks.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.world.World;

public class SoulWand extends Wand {
    
    private static Properties createProps(){
        Properties p = new Item.Properties();
        p.setNoRepair();
        p.maxStackSize(1);
        p.defaultMaxDamage(1);
        return p;
    }

    public SoulWand() {
        super( createProps() );

        setRegistryName( "soul_wand" );
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);

        RayTraceResult rayTraceResult = rayTrace(worldIn, playerIn, FluidMode.NONE);
        BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;

        BlockPos pos = blockRayTraceResult.getPos();
        BlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();

        if( block instanceof IHasFire )
        {
            changeFire( worldIn, playerIn, pos, state, (IHasFire)block );
        }
        else if( block instanceof FireBase )
        {
            BlockPos underPos = pos.down();
            Block underBlock = worldIn.getBlockState( underPos ).getBlock();
            if( underBlock instanceof IHasFire )
                changeFire( worldIn, playerIn, underPos, worldIn.getBlockState(underPos), (IHasFire)underBlock );
        }
      
        return new ActionResult<ItemStack>(ActionResultType.CONSUME, stack);
    }


    private void changeFire( World worldIn, PlayerEntity playerIn, BlockPos pos, BlockState state, IHasFire modblock ) {

        FireTypes currentType = state.get( IHasFire.FIRE_TYPE );
        FireTypes prevType = state.get( IHasFire.PREVIOUS_FIRE_TYPE );
        if( prevType == FireTypes.SOUL )
            prevType = FireTypes.NORMAL;

        if( playerIn.isSneaking() )
        {
            if( currentType == FireTypes.SOUL )
            {
                state = modblock.setFireType( worldIn, pos, state, prevType, prevType );
                playSound( worldIn, playerIn, SoundEvents.UNDO, 0.6f );
            }
        }
        else
        {
            if( currentType != FireTypes.SOUL )
            {
                state = modblock.setFireType( worldIn, pos, state, FireTypes.SOUL, prevType );
                playSound( worldIn, playerIn, SoundEvents.CHANGE, 0.8f );      
            }
        }

        if( modblock instanceof Pedestal )
            ((Pedestal)modblock).setFire(worldIn, pos, state, true );
    }
}