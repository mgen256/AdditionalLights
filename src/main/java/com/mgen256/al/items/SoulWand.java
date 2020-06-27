package com.mgen256.al.items;

import com.google.common.collect.PeekingIterator;
import com.mgen256.al.FireTypes;
import com.mgen256.al.blocks.Pedestal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
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
        if( block instanceof Pedestal )
        {
            Pedestal pedestal = (Pedestal)block;

            FireTypes currentType = state.get( Pedestal.FIRE_TYPE );
            FireTypes prevType = state.get( Pedestal.PREVIOUS_FIRE_TYPE );
            if( prevType == FireTypes.SOUL )
                prevType = FireTypes.NORMAL;

            if( playerIn.isSneaking() )
            {
                if( currentType == FireTypes.SOUL )
                {
                    state = pedestal.setFireType( worldIn, pos, state, prevType, prevType );
                    playSound( worldIn, playerIn, SoundEvents.UNDO );
                }
            }
            else
            {
                if( currentType != FireTypes.SOUL )
                {
                    state = pedestal.setFireType( worldIn, pos, state, FireTypes.SOUL, prevType );
                    playSound( worldIn, playerIn, SoundEvents.CHANGE );      
                }
            }

            pedestal.setFire(worldIn, pos, state, true );

        }
        return new ActionResult<ItemStack>(ActionResultType.CONSUME, stack);
    }

}