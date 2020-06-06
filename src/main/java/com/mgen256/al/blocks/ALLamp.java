package com.mgen256.al.blocks;

import net.minecraft.block.*;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.math.AxisAlignedBB;


public class ALLamp extends LampAndTorchBase {
    static PropertyDirection FACING = PropertyDirection.create("facing");

    // D-U-N-S-W-E
    private static AxisAlignedBB[] SHAPES = {
        new AxisAlignedBB( 0.3125, 0.875, 0.3125, 0.6875, 1, 0.6875), // down
        new AxisAlignedBB( 0.3125, 0.0, 0.3125, 0.6875, 0.125, 0.6875), // up
        new AxisAlignedBB(0.375, 0.4375, 0.75, 0.625, 0.8125, 1), // north
        new AxisAlignedBB(0.375, 0.4375, 0.0, 0.625, 0.8125, 0.25), // south
        new AxisAlignedBB(0.75, 0.4375, 0.375, 1, 0.8125, 0.625), // west
        new AxisAlignedBB(0.0, 0.4375, 0.375, 0.25, 0.8125, 0.625), // east
    };

    public ALLamp( Block mainblock, String mainblockName ) {
        super( "al_lamp_", mainblock, mainblockName, mainblock.getMaterial(null) );
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        lightValue = 15;
      }

    @Override
    protected PropertyDirection getFacing(){
        return FACING;
    }

    @Override
    protected AxisAlignedBB getShapes( IBlockState state )  {
        return SHAPES[state.getValue(FACING).getIndex()];
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing
    , float hitX, float hitY, float hitZ, int meta,EntityLivingBase placer) {

        for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL )
            if (this.canPlaceAt(worldIn, pos, enumfacing))
                return this.getDefaultState().withProperty(FACING, enumfacing);

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            if (this.canPlaceAt(worldIn, pos, enumfacing))
                return this.getDefaultState().withProperty(FACING, enumfacing);

        return this.getDefaultState();
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        this.onNeighborChangeInternal(worldIn, pos, state);
    }
    
    protected boolean onNeighborChangeInternal(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.checkForDrop(worldIn, pos, state))
            return true;
        else
        {
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            BlockPos blockpos = pos.offset(enumfacing1);
            boolean flag = false;

            if (worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, enumfacing) == BlockFaceShape.UNDEFINED)
                flag = true;

            if (flag)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
                return true;
            }
            else
                return false;
        }
    }

    @Override 
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    public boolean canPlaceTopOrDown(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing)
    {
        BlockFaceShape shape = state.getBlockFaceShape(world, pos, facing);
        return (shape == BlockFaceShape.SOLID || shape == BlockFaceShape.CENTER 
            || shape == BlockFaceShape.CENTER_BIG) && !isExceptionBlockForAttaching2(this);
    }

    @Override
    protected boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing){
        BlockPos blockpos = pos.offset(facing.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        BlockFaceShape blockfaceshape = iblockstate.getBlockFaceShape(worldIn, blockpos, facing);
        
        if ( ( facing.equals(EnumFacing.UP) || facing.equals(EnumFacing.DOWN) ) && canPlaceTopOrDown( iblockstate, worldIn, blockpos, facing))
            return true;
        else if (facing != EnumFacing.UP )
            return !isExceptBlockForAttachWithPiston2(block) && blockfaceshape != BlockFaceShape.UNDEFINED;
        else
            return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos){
        for (EnumFacing enumfacing : FACING.getAllowedValues())
            if (canPlaceAt(worldIn, pos, enumfacing))
                return true;

        return false;
    }

    @Override
    protected boolean isExceptionBlockForAttaching2(Block attachBlock) {
        return attachBlock instanceof BlockShulkerBox || attachBlock instanceof BlockTrapDoor 
        || attachBlock == Blocks.BEACON || attachBlock == Blocks.CAULDRON 
        || attachBlock == Blocks.GLOWSTONE || attachBlock == Blocks.ICE 
        || attachBlock == Blocks.SEA_LANTERN || attachBlock == Blocks.STAINED_GLASS;
    }
}