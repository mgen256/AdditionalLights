package com.mgen256.al.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.AxisAlignedBB;




public abstract class LampAndTorchBase extends ModBlock {

    public LampAndTorchBase(String basename, Block mainblock, String mainblockName, Material material) {
        super(basename, mainblock, mainblockName, material);
        blockHardness = 0.0f;
    }

    abstract PropertyDirection getFacing();
    abstract boolean isExceptionBlockForAttaching2(Block attachBlock);

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }
    
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.AIR;
    }
        
    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.DESTROY;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox( IBlockState state, IBlockAccess source, BlockPos pos ) {
        return NULL_AABB;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, getFacing() );
    }
    
    @Override
    public IBlockState withRotation( IBlockState state, Rotation rot ) {
        return state.withProperty( getFacing(), rot.rotate(state.getValue(getFacing())));
    }

    @Override
    public IBlockState withMirror( IBlockState state, Mirror mirrorIn ) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(getFacing())));
    }
   
    @Override
    public int getMetaFromState( IBlockState state ) {
        return state.getValue(getFacing()).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta( int meta ) {
        return getDefaultState().withProperty( getFacing(), EnumFacing.values()[meta] );
    }

    protected boolean isExceptBlockForAttachWithPiston2(Block attachBlock) {
        return isExceptionBlockForAttaching2(attachBlock) || attachBlock == Blocks.PISTON 
        || attachBlock == Blocks.STICKY_PISTON || attachBlock == Blocks.PISTON_HEAD;
    }


}
