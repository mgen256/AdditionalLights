package com.mgen256.al.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;

public class ALTorch extends LampAndTorchBase {

    public static PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>() {
        public boolean apply(@Nullable EnumFacing p_apply_1_) {
            return p_apply_1_ != EnumFacing.DOWN;
        }
    });

    // D-U-N-S-W-E
    private static AxisAlignedBB[] SHAPES = {
        new AxisAlignedBB( 0.375, 0.0, 0.375, 0.625, 0.625, 0.625 ), // up
        new AxisAlignedBB( 0.34375, 0.125, 0.6875, 0.65625, 0.8125, 1 ), // north
        new AxisAlignedBB( 0.34375, 0.125, 0.0, 0.65625, 0.8125, 0.3125 ), // south
        new AxisAlignedBB( 0.6875, 0.125, 0.34375, 1, 0.8125, 0.65625 ), // west
        new AxisAlignedBB( 0.0, 0.125, 0.34375, 0.3125, 0.8125, 0.65625 ), // east
    };

    public ALTorch( Block mainblock ) {
        super("al_torch_", mainblock,  Material.CIRCUITS );
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        setTickRandomly(true);
        lightValue = 14;
        
    }
    
    @Override
    protected PropertyDirection getFacing(){
        return FACING;
    }

    private boolean canPlaceOn(World worldIn, BlockPos pos){
        IBlockState state = worldIn.getBlockState(pos);
        return state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos){
        for (EnumFacing enumfacing : FACING.getAllowedValues())
        {
            if (this.canPlaceAt(worldIn, pos, enumfacing))
            {
                return true;
            }
        }
        return false;
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing){
        BlockPos blockpos = pos.offset(facing.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        BlockFaceShape blockfaceshape = iblockstate.getBlockFaceShape(worldIn, blockpos, facing);

        if (facing.equals(EnumFacing.UP) && this.canPlaceOn(worldIn, blockpos))
            return true;
        else if (facing != EnumFacing.UP && facing != EnumFacing.DOWN)
            return !isExceptBlockForAttachWithPiston2(block) && blockfaceshape == BlockFaceShape.SOLID;
        else
            return false;
    }

    @Override
    protected AxisAlignedBB getShapes( IBlockState state )  {
        int index = state.getValue(FACING).getIndex();
        if( index <= 0 )
            return NULL_AABB;

        return SHAPES[index-1];
    }

    @Override
    public BlockFaceShape getBlockFaceShape( IBlockAccess access, IBlockState state, BlockPos pos, EnumFacing facing) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing
    , float hitX, float hitY, float hitZ, int meta,EntityLivingBase placer) {
        if (this.canPlaceAt(worldIn, pos, facing))
            return this.getDefaultState().withProperty(FACING, facing);
        else{
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                if (this.canPlaceAt(worldIn, pos, enumfacing))
                    return this.getDefaultState().withProperty(FACING, enumfacing);

            return this.getDefaultState();
        }
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
            EnumFacing.Axis enumfacing$axis = enumfacing.getAxis();
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            BlockPos blockpos = pos.offset(enumfacing1);
            boolean flag = false;

            if (enumfacing$axis.isHorizontal() && worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, enumfacing) != BlockFaceShape.SOLID)
                flag = true;
            else if (enumfacing$axis.isVertical() && !this.canPlaceOn(worldIn, blockpos))
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

    protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getBlock() == this && this.canPlaceAt(worldIn, pos, (EnumFacing)state.getValue(FACING)))
            return true;
        else {
            if (worldIn.getBlockState(pos).getBlock() == this)  {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        EnumFacing enumfacing = (EnumFacing)stateIn.getValue(FACING);
        double dx = (double)pos.getX() + 0.5D;
        double dy = (double)pos.getY();
        double dz = (double)pos.getZ() + 0.5D;
        double d3 = 0.38D;

        if (enumfacing.getAxis().isHorizontal())
        {
            dy += + 0.9D;
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,dx + d3 * (double)enumfacing1.getXOffset(), dy, dz + d3 * (double)enumfacing1.getZOffset(), 0.0D, 0.0D, 0.0D);
            worldIn.spawnParticle(EnumParticleTypes.FLAME,       dx + d3 * (double)enumfacing1.getXOffset(), dy, dz + d3 * (double)enumfacing1.getZOffset(), 0.0D, 0.0D, 0.0D);
        }
        else
        {
            dy += + 0.7D;
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, dx, dy, dz, 0.0D, 0.0D, 0.0D);
            worldIn.spawnParticle(EnumParticleTypes.FLAME,        dx, dy, dz, 0.0D, 0.0D, 0.0D);
        }
    }
}