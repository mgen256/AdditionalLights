
package com.mgen256.al.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import com.mgen256.al.*;


public abstract class Pedestal extends ModBlock {

    public Pedestal( String basename, Block mainblock, String mainblockName ) {
        super( basename, mainblock, mainblockName, mainblock.getMaterial(null) );
    }

    protected abstract ModBlockList getFireKey();


    private Block getFireBlock(){
        return AdditionalLights.modBlocks.get(getFireKey());
    }

    private boolean setFire( World worldIn, BlockPos pos ) {
        IBlockState upperBlockState = worldIn.getBlockState(pos.up());
        if( ( upperBlockState.getMaterial() == Material.AIR || upperBlockState.getMaterial() == Material.WATER ) == false )
            return false;

        worldIn.setBlockState( pos.up(), getFireBlock().getDefaultState() );
        return true;
    }

   @Override
   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn
        , EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if( setFire( worldIn, pos ) ){
            playerIn.playSound( SoundEvents.ITEM_FLINTANDSTEEL_USE, 0.7f, 1.4f );
            return true;
        }
        return false;
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if( placer.isSneaking() )
            return;
        setFire( worldIn, pos );
    }

}