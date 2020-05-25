
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

import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;

import javax.annotation.Nullable;

import com.mgen256.al.*;



public abstract class Pedestal extends ModBlock  implements IWaterLoggable{

    public Pedestal( String basename, Block mainblock, Properties props, VoxelShape shape ) {
        super(basename, mainblock, props, shape);
    }

    protected abstract ModBlockList getFireKey();

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(BlockStateProperties.AXIS, context.getFace().getAxis())
                .with(BlockStateProperties.WATERLOGGED, false);
    }
    

    @Override
    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return true;
    }

    @Override
    public IFluidState getFluidState(final BlockState state) {
        return state.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false)
                : super.getFluidState(state);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.AXIS, BlockStateProperties.WATERLOGGED);
    }
        

    private Block getFireBlock(){
        return AdditionalLights.modBlocks.get(getFireKey());
    }

    private boolean setFire( World worldIn, BlockPos pos ) {
        BlockState upperBlockState = worldIn.getBlockState(pos.up());
        if( ( upperBlockState.isAir() || upperBlockState.getMaterial() == Material.WATER ) == false )
            return false;

        worldIn.setBlockState(pos.up(), getFireBlock().getDefaultState().with(BlockStateProperties.WATERLOGGED, false) );
        return true;
    }

   @Override
   public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos
        , PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if( setFire( worldIn, pos ) ){
            player.playSound( SoundEvents.ITEM_FLINTANDSTEEL_USE, 0.7f, 1.4f );
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        setFire( worldIn, pos ) ;
    }

}