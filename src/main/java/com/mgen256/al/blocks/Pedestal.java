
package com.mgen256.al.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;

import javax.annotation.Nullable;

import com.mgen256.al.*;
import com.mgen256.al.items.*;


public abstract class Pedestal extends ModBlock implements IWaterLoggable{

    public static EnumProperty<FireTypes> FIRE_TYPE = EnumProperty.create( "firetype", FireTypes.class );

    public Pedestal( String basename, Block mainblock, Properties props, VoxelShape shape ) {
        super(basename, mainblock, props, shape);

        setDefaultState( getDefaultState()
            .with(FIRE_TYPE, FireTypes.NORMAL)
            .with(BlockStateProperties.WATERLOGGED, false) );
    }

    protected abstract ModBlockList getFireKey(BlockState state);
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add( BlockStateProperties.WATERLOGGED );
        builder.add( FIRE_TYPE );
    }

    public BlockState setFireType( World worldIn, BlockPos pos, BlockState state, FireTypes fireType ) {
        
        BlockState newState = state.with( FIRE_TYPE, fireType );
        if( worldIn.setBlockState( pos, newState ) )
            return newState;
        
        return state;
    }

    @Override
    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return true;
    }

    @Override
    public IFluidState getFluidState(final BlockState state) {
        return state.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    private Block getFireBlock(BlockState state){
        return AdditionalLights.modBlocks.get(getFireKey(state));
    }

    public boolean setFire( World worldIn, BlockPos pos, BlockState state, boolean replaceOnly ) {
        BlockState upperBlockState = worldIn.getBlockState(pos.up());

        if( replaceOnly )
        {
            if( upperBlockState.getBlock() instanceof FireBase == false )
                return false;
        }
        else
        {
            if( ( upperBlockState.isAir() || upperBlockState.getMaterial() == Material.WATER 
            || upperBlockState.getBlock() instanceof FireBase ) == false )
            return false;
        }

        worldIn.setBlockState(pos.up(), getFireBlock(state).getDefaultState().with(FireBase.SET, Boolean.valueOf(true)) );
        return true;
    }


   @Override
   @OnlyIn(Dist.CLIENT)
   public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos
        , PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    
        if( handIn == Hand.OFF_HAND )
            return ActionResultType.PASS;
    
        ItemStack stack = player.getHeldItem(handIn);
        if( stack.getItem() instanceof Wand )
            return ActionResultType.FAIL;

        if( setFire( worldIn, pos, state, false ) == false )
            return ActionResultType.PASS;
        
        player.playSound( SoundEvents.ITEM_FLINTANDSTEEL_USE, 0.7f, 1.4f );
        return ActionResultType.SUCCESS;
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if( placer.isSneaking() )
            return;
        setFire( worldIn, pos, state, false );
    }
}