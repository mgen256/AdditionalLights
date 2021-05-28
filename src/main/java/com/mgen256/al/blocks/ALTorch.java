package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.loot.LootContext;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.mgen256.al.*;
import com.mgen256.al.items.SoulWand;

public class ALTorch extends TorchBlock implements IModBlock, IHasFire {

    public static Properties createProps( Block mainblock ){
        BlockState state = mainblock.getDefaultState();
        
        return Block.Properties.create(Material.MISCELLANEOUS)
            .doesNotBlockMovement()
            .hardnessAndResistance(0.0f)
            .setLightLevel( lightLevel -> 14 )
            .sound( state.getSoundType( null, null, null ) );
    }
    
    public ALTorch( Block mainblock, ModBlockList _wallKey ) {
        super( createProps(mainblock), ParticleTypes.FLAME );

        name = "al_torch_" + mainblock.getRegistryName().getPath();
        wallKey = _wallKey;
        setDefaultState( stateContainer.getBaseState()
            .with( FIRE_TYPE, FireTypes.NORMAL )
            .with( PREVIOUS_FIRE_TYPE, FireTypes.NORMAL ) );
    }
    
    private BlockItem blockItem;
    private ModBlockList wallKey;
    private String name;

    @Override
    public void init() {
        setRegistryName(name);
        blockItem = new WallOrFloorItem(this, AdditionalLights.getBlock(wallKey) , AdditionalLights.ItemProps);
        blockItem.setRegistryName(getRegistryName());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add( FIRE_TYPE );
        builder.add( PREVIOUS_FIRE_TYPE );
    }
    
    @Override
    public String getName(){
        return name;
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.get( FIRE_TYPE ) == FireTypes.SOUL ? 10 : 14;
    }

    @Override
    public void setRenderLayer() {
        RenderTypeLookup.setRenderLayer(this, name.contains("glass") ? RenderType.getCutout() : RenderType.getSolid() );
    }
    
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double)pos.getX() + 0.5D;
        double d1 = (double)pos.getY() + 0.7D;
        double d2 = (double)pos.getZ() + 0.5D;
        worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        
        IParticleData particleType;
        particleType = stateIn.get( FIRE_TYPE ) == FireTypes.SOUL ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;

        worldIn.addParticle(particleType, d0, d1, d2, 0.0D, 0.0D, 0.0D);
     }

        
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {

        List<ItemStack> list = new ArrayList<>();
        list.add( new ItemStack( blockItem ));

        return list;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if( placer.getHeldItemOffhand().getItem() instanceof SoulWand )
            worldIn.setBlockState( pos, state.with( FIRE_TYPE, FireTypes.SOUL ) );
    }
}