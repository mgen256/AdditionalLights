package com.mgen256.al.blocks;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;


import com.mgen256.al.*;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Fire extends ModBlock{
    
    private static Map<FireBlockList, AxisAlignedBB> SHAPES;
    private static Map<FireBlockList, EnumParticleTypes> PARTICLE_TYPES;
    private static Map<FireBlockList, Double> SMOKE_POS;

    static {
        SHAPES = new LinkedHashMap<FireBlockList, AxisAlignedBB>();
        SHAPES.put( FireBlockList.standing_torch_s, new AxisAlignedBB(0.25, -0.375, 0.25, 0.75, 0.125, 0.75) );
        SHAPES.put( FireBlockList.standing_torch_l, new AxisAlignedBB(0.25, -0.125, 0.25, 0.75, 0.375, 0.75) );
        SHAPES.put( FireBlockList.fire_pit_s, new AxisAlignedBB(0, -0.625, 0, 1, 0.1, 1) );
        SHAPES.put( FireBlockList.fire_pit_l, new AxisAlignedBB(0, -0.125, 0, 1, 0.4375, 1) );

        PARTICLE_TYPES = new LinkedHashMap<FireBlockList, EnumParticleTypes>();
        PARTICLE_TYPES.put( FireBlockList.standing_torch_s, EnumParticleTypes.SMOKE_NORMAL );
        PARTICLE_TYPES.put( FireBlockList.standing_torch_l, EnumParticleTypes.SMOKE_NORMAL );
        PARTICLE_TYPES.put( FireBlockList.fire_pit_s, EnumParticleTypes.SMOKE_LARGE );
        PARTICLE_TYPES.put( FireBlockList.fire_pit_l, EnumParticleTypes.SMOKE_LARGE );

        SMOKE_POS = new LinkedHashMap<FireBlockList, Double>();
        SMOKE_POS.put( FireBlockList.standing_torch_s, 0.2 );
        SMOKE_POS.put( FireBlockList.standing_torch_l, 0.7 );
        SMOKE_POS.put( FireBlockList.fire_pit_s, 0.0 );
        SMOKE_POS.put( FireBlockList.fire_pit_l, 0.8 );
    }

    public Fire( FireBlockList _baseFireBlock ) {
        super( "fire_for_" + _baseFireBlock, null, null, Material.FIRE );
        baseFireBlock = _baseFireBlock;
        blockHardness = 0.0f;
        lightValue = 15;
        setTickRandomly(true);
        setSoundType(new SoundType(0.5F, 2.0F, SoundEvents.BLOCK_CLOTH_BREAK, SoundEvents.BLOCK_CLOTH_STEP
        , SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_CLOTH_HIT, SoundEvents.BLOCK_CLOTH_FALL));
      }
      
    private FireBlockList baseFireBlock;

    
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
    protected AxisAlignedBB getShapes( IBlockState state )  {
        return SHAPES.get(baseFireBlock);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        final double d0 = (double) pos.getX() + 0.5D;
        final double d1 = (double) pos.getY() + SMOKE_POS.get(baseFireBlock);
        final double d2 = (double) pos.getZ() + 0.5D;
        worldIn.spawnParticle(PARTICLE_TYPES.get(baseFireBlock), d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }

    private boolean canBlockStay(World worldIn, BlockPos pos) {
        return !worldIn.isAirBlock(pos.down());
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        this.checkForDrop(worldIn, pos, state);
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(worldIn, pos)) {
            worldIn.setBlockToAir(pos);
            return false;
        }
        else
            return true;
    }
}
