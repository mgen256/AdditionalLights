package com.mgen256.al.blocks;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.mgen256.al.*;
import com.mgen256.al.items.SoulWand;

public class ALTorch_Wall extends WallTorchBlock implements IModBlock, IHasFire {
    
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap( Map.of( 
        Direction.NORTH, Block.box(5.5D, 2.0D, 11.0D, 10.5D, 13.0D, 16.0D), 
        Direction.SOUTH, Block.box(5.5D, 2.0D, 0.0D, 10.5D, 13.0D, 5.0D), 
        Direction.WEST, Block.box(11.0D, 2.0D, 5.5D, 16.0D, 13.0D, 10.5D), 
        Direction.EAST, Block.box(0.0D, 2.0D, 5.5D, 5.0D, 13.0D, 10.5D)) );

    public ALTorch_Wall(Block mainblock, ModBlockList _floorKey ) {
        super(ParticleTypes.FLAME, ALTorch.createProps(mainblock) );
        floorKey = _floorKey;
        registerDefaultState( stateDefinition.any()
            .setValue( BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH )
            .setValue( FIRE_TYPE, FireTypes.NORMAL ) 
            .setValue( PREVIOUS_FIRE_TYPE, FireTypes.NORMAL ) );
    }

    private ModBlockList floorKey;
    private ModBlockList myKey;
    
    @Override
    public void setMyKey(ModBlockList key) {
        myKey = key;
    }
 
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add( FIRE_TYPE );
        builder.add( PREVIOUS_FIRE_TYPE );
    }
    
    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue( FIRE_TYPE ) == FireTypes.SOUL ? 10 : 14;
    }

    @Override
    public VoxelShape getShape(BlockState p_58152_, BlockGetter p_58153_, BlockPos p_58154_, CollisionContext p_58155_) {
        return getShape(p_58152_);
    }

    public static VoxelShape getShape(BlockState p_58157_) {
        return SHAPES.get(p_58157_.getValue(FACING));
    }
 
    // @Override
    // public void setRenderLayer() {
    //     ItemBlockRenderTypes.setRenderLayer(this, name.contains("glass") ? RenderType.cutout() : RenderType.solid() );
    // }
    
    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        Direction direction = stateIn.getValue(BlockStateProperties.HORIZONTAL_FACING);
        double dx = pos.getX() + 0.5D;
        double dy = pos.getY() + 0.9D;
        double dz = pos.getZ() + 0.5D;
  
        Direction direction1 = direction.getOpposite();
        double d3 = 0.38D;
        level.addParticle(ParticleTypes.SMOKE, dx + d3 * direction1.getStepX(), dy, dz + d3 * direction1.getStepZ(), 0.0D, 0.0D, 0.0D);

        ParticleOptions particleOption;
        particleOption = stateIn.getValue( FIRE_TYPE ) == FireTypes.SOUL ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;
            
        level.addParticle(particleOption, dx + d3 * direction1.getStepX(), dy, dz + d3 * direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
       }

    
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
    
        List<ItemStack> list = new ArrayList<>();
        list.add( new ItemStack( floorKey.getBlockItem() ) );

        return list;
    }
    
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if( placer.getOffhandItem().getItem() instanceof SoulWand )
            level.setBlockAndUpdate( pos, state.setValue( FIRE_TYPE, FireTypes.SOUL ) );
    }
}