package com.mgen256.al.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.math.AxisAlignedBB;




public abstract class LampAndTorchBase extends ModBlock {

    public LampAndTorchBase(String basename, Block mainblock, String mainblockName, Material material) {
        super(basename, mainblock, mainblockName, material);
        blockHardness = 0.0f;
    }

    abstract PropertyDirection getFacing();
    abstract boolean isExceptionBlockForAttaching2(Block attachBlock);
    abstract boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing);

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
       
    protected int getMaxMeta( ) {
        return 6;
    }

    @Override
    public int getMetaFromState( IBlockState state ) {
        return state.getValue(getFacing()).getIndex();
    }

    @Override
    public BlockFaceShape getBlockFaceShape( IBlockAccess access, IBlockState state, BlockPos pos, EnumFacing facing) {
        return BlockFaceShape.UNDEFINED;
    }

    protected boolean isExceptBlockForAttachWithPiston2(Block attachBlock) {
        return isExceptionBlockForAttaching2(attachBlock) || attachBlock == Blocks.PISTON 
        || attachBlock == Blocks.STICKY_PISTON || attachBlock == Blocks.PISTON_HEAD;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.checkForDrop(worldIn, pos, state);
    }
    
    protected boolean canPlaceOn(World worldIn, BlockPos pos){
        IBlockState state = worldIn.getBlockState(pos);
        return state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
    }
    
    protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getBlock() == this && canPlaceAt(worldIn, pos, (EnumFacing)state.getValue(getFacing())))
            return true;
        else {
            if (worldIn.getBlockState(pos).getBlock() == this)  {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        return false;
        }
    }

}
