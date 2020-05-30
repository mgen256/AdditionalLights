package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;


// implements IWaterLoggable
public class ALLamp extends ModBlock {
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

    public ALLamp(final Block mainblock ) {
        super( "al_lamp_", mainblock, mainblock.getMaterial(null) );
        lightValue = 15;
        blockHardness = 0.0f;
      }

            
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube( final IBlockState state ){
        return false;
    }

    @Override
    public boolean isFullCube( final IBlockState state ){
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.AIR;
    }

    private AxisAlignedBB getShapes( final IBlockState state )  {
        return SHAPES[state.getValue(FACING).getIndex()];
    }

    @Override
    public AxisAlignedBB getBoundingBox( final IBlockState state, final IBlockAccess source, final BlockPos pos ) {
        return getShapes(state);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox( final IBlockState state, final IBlockAccess source, final BlockPos pos ) {
        return new AxisAlignedBB( 0,0,0,0,0,0 );
    }
/*
    @Override
    public AxisAlignedBB getCollisionBoundingBox( final IBlockState state, final IBlockAccess source, final BlockPos pos ) {
        return getShapes(state);
    }
*/
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
	}

    @Override
    public BlockFaceShape getBlockFaceShape( final IBlockAccess access, final IBlockState state, final BlockPos pos, final EnumFacing facing) {
        return facing.getOpposite() == state.getValue(FACING) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing
    , final float hitX, final float hitY, final float hitZ, final int meta,final EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, facing);//
    }

    @Override
    public boolean canPlaceBlockOnSide( final World worldIn, final BlockPos pos, final EnumFacing facing ){
        return this.canPlaceBlockAt(worldIn, pos);
    }

    @Override
    public int getMetaFromState( final IBlockState state ) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta( final int meta ) {
        return getDefaultState().withProperty( FACING, EnumFacing.values()[meta] );
    }

    @Override
    public IBlockState withRotation( final IBlockState state, final Rotation rot ) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror( final IBlockState state, final Mirror mirrorIn ) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }
}