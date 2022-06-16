package com.mgen256.al.blocks;


import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.mgen256.al.*;
import com.mgen256.al.items.SoulWand;

public class ALTorch extends TorchBlock implements IModBlock, IHasFire {

    public static Properties createProps( Block mainblock ){
        BlockState state = mainblock.defaultBlockState();
        
        return BlockBehaviour.Properties.of(Material.DECORATION)
            .noCollission()
            .instabreak()
            .lightLevel( lightLevel -> 14 )
            .sound( state.getSoundType( null, null, null ) );
    }
    
    public ALTorch( Block mainblock, ModBlockList _wallKey ) {
        super( createProps(mainblock), ParticleTypes.FLAME );

        name = "al_torch_" + Registry.BLOCK.getKey(mainblock).getPath();
        wallKey = _wallKey;
        registerDefaultState( stateDefinition.any()
            .setValue( FIRE_TYPE, FireTypes.NORMAL )
            .setValue( PREVIOUS_FIRE_TYPE, FireTypes.NORMAL ) );
    }
    
    private BlockItem blockItem;
    private ModBlockList wallKey;
    private String name;

    @Override
    public void init() {
        //setRegistryName(name);
        blockItem = new StandingAndWallBlockItem(this, AdditionalLights.getBlock(wallKey) , AdditionalLights.ItemProps);
        //blockItem.setRegistryName(getModRegistryName());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add( FIRE_TYPE );
        builder.add( PREVIOUS_FIRE_TYPE );
    }
    
    @Override
    public String getModRegistryName(){
        return name;
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue( FIRE_TYPE ) == FireTypes.SOUL ? 10 : 14;
    }

    @Override
    public void setRenderLayer() {
        ItemBlockRenderTypes.setRenderLayer(this, name.contains("glass") ? RenderType.cutout() : RenderType.solid() );
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
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {

        List<ItemStack> list = new ArrayList<>();
        list.add( new ItemStack( blockItem ));

        return list;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if( placer.getOffhandItem().getItem() instanceof SoulWand )
            level.setBlockAndUpdate( pos, state.setValue( FIRE_TYPE, FireTypes.SOUL ) );
    }
}