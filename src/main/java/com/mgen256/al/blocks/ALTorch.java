package com.mgen256.al.blocks;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mgen256.al.*;
import com.mgen256.al.items.SoulWand;

public class ALTorch extends TorchBlock implements IModBlock, IHasFire {

    protected static final VoxelShape SHAPE = Block.box(5.5D, 0.0D, 5.5D, 10.5D, 10.0D, 10.5D);
    
    public static Properties createProps( Block mainblock ){
        return BlockBehaviour.Properties.of()
            .noCollission()
            .instabreak()
            .lightLevel( lightLevel -> 14 )
            .sound( mainblock.defaultBlockState().getSoundType() );
    }
    
    public ALTorch( Block mainblock ) {
        super( createProps(mainblock), ParticleTypes.FLAME );

        registerDefaultState( stateDefinition.any()
            .setValue( FIRE_TYPE, FireTypes.NORMAL )
            .setValue( PREVIOUS_FIRE_TYPE, FireTypes.NORMAL ) );
    }
        
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
    public VoxelShape getShape(BlockState p_57510_, BlockGetter p_57511_, BlockPos p_57512_, CollisionContext p_57513_) {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        double d0 = pos.getX() + 0.5D;
        double d1 = pos.getY() + 0.7D;
        double d2 = pos.getZ() + 0.5D;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        
        ParticleOptions particleOption;
        particleOption = stateIn.getValue( FIRE_TYPE ) == FireTypes.SOUL ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;

        level.addParticle(particleOption, d0, d1, d2, 0.0D, 0.0D, 0.0D);
     }

        
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {

        List<ItemStack> list = new ArrayList<>();
        list.add( new ItemStack( myKey.getBlockItem() ));

        return list;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if( placer == null ) 
            return;

        if( placer.getOffhandItem().getItem() instanceof SoulWand )
            level.setBlockAndUpdate( pos, state.setValue( FIRE_TYPE, FireTypes.SOUL ) );
    }
}