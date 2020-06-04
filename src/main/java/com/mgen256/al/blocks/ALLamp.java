package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
    public BlockFaceShape getBlockFaceShape( IBlockAccess access, IBlockState state, BlockPos pos, EnumFacing facing) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing
    , float hitX, float hitY, float hitZ, int meta,EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, facing);//
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.checkForDrop(worldIn, pos, state);
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

            if (worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, enumfacing) != BlockFaceShape.SOLID)
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

    private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing){
        BlockPos blockpos = pos.offset(facing.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        BlockFaceShape blockfaceshape = iblockstate.getBlockFaceShape(worldIn, blockpos, facing);

        if ( blockfaceshape != BlockFaceShape.UNDEFINED )
            return !isExceptBlockForAttachWithPiston2(block) ;
        else
            return false;
    }

    //
    @Override
    public boolean canPlaceBlockOnSide( World worldIn, BlockPos pos, EnumFacing facing ){
        return this.canPlaceBlockAt(worldIn, pos);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos){
        for (EnumFacing enumfacing : FACING.getAllowedValues())
            if (this.canPlaceAt(worldIn, pos, enumfacing))
                return true;

        return false;
    }

    protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getBlock() == this && canPlaceAt(worldIn, pos, (EnumFacing)state.getValue(FACING)))
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