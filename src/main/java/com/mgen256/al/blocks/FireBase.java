package com.mgen256.al.blocks;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.mgen256.al.*;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.particles.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.*;
import net.minecraft.world.storage.loot.LootContext;

public abstract class FireBase extends ModBlock{
    
    public static BooleanProperty SET = BooleanProperty.create("set");
    public static BooleanProperty SUMMONED = BooleanProperty.create("summoned");
    public static BooleanProperty TEMP = BooleanProperty.create("temp");

    private static Map<PedestalTypes, VoxelShape> SHAPES;
    private static Map<PedestalTypes, BasicParticleType> PARTICLE_TYPES;
    private static Map<PedestalTypes, Double> SMOKE_POS;

    static {
        SHAPES = new LinkedHashMap<PedestalTypes, VoxelShape>();
        SHAPES.put( PedestalTypes.standing_torch_s, Block.makeCuboidShape(4.0D, -6.0D, 4.0D, 12.0D, 2.0D, 12.0D) );
        SHAPES.put( PedestalTypes.standing_torch_l, Block.makeCuboidShape(4.0D, -2.0D, 4.0D, 12.0D, 6.0D, 12.0D) );
        SHAPES.put( PedestalTypes.fire_pit_s, Block.makeCuboidShape(0.0D, -10.0D, 0.0D, 16.0D, 2.0D, 16.0D) );
        SHAPES.put( PedestalTypes.fire_pit_l, Block.makeCuboidShape(0.0D, -2.0D, 0.0D, 16.0D, 7.0D, 16.0D) );

        PARTICLE_TYPES = new LinkedHashMap<PedestalTypes, BasicParticleType>();
        PARTICLE_TYPES.put( PedestalTypes.standing_torch_s, ParticleTypes.SMOKE );
        PARTICLE_TYPES.put( PedestalTypes.standing_torch_l, ParticleTypes.SMOKE );
        PARTICLE_TYPES.put( PedestalTypes.fire_pit_s, ParticleTypes.LARGE_SMOKE );
        PARTICLE_TYPES.put( PedestalTypes.fire_pit_l, ParticleTypes.LARGE_SMOKE );

        SMOKE_POS = new LinkedHashMap<PedestalTypes, Double>();
        SMOKE_POS.put( PedestalTypes.standing_torch_s, 0.2 );
        SMOKE_POS.put( PedestalTypes.standing_torch_l, 0.7 );
        SMOKE_POS.put( PedestalTypes.fire_pit_s, 0.0 );
        SMOKE_POS.put( PedestalTypes.fire_pit_l, 0.8 );
    }

    private static Properties createProps(){
        Properties p = Block.Properties.create(Material.MISCELLANEOUS);
        p.hardnessAndResistance(0.0f);
        p.doesNotBlockMovement();
        p.sound(new SoundType(1.5F, 1.0F,AdditionalLights.modSounds.get(ModSoundList.Fire_Extinguish), SoundEvents.BLOCK_WOOL_STEP
            , SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_WOOL_HIT, SoundEvents.BLOCK_WOOL_FALL) );
            
        return p;
    }

    public FireBase( String basename, PedestalTypes _pedestalKey ) {
        super( basename + _pedestalKey, null, createProps(), SHAPES.get(_pedestalKey));

        pedestalKey = _pedestalKey;
        this.setDefaultState(this.stateContainer.getBaseState().with(SET, Boolean.valueOf(false) ).with(SUMMONED, false).with(TEMP, false) );
      }

      
    private PedestalTypes pedestalKey;
    
      
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add( SET );
        builder.add( SUMMONED );
        builder.add( TEMP );
    }

    protected float getFireDamageAmount() {
        return 0.0F;
    }

    @Override
    public void setRenderLayer() {
        RenderTypeLookup.setRenderLayer(this, RenderType.getCutout());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(pedestalKey);
    }
    
    @Override
    public void animateTick(final BlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        final double d0 = (double) pos.getX() + 0.5D;
        final double d1 = (double) pos.getY() + SMOKE_POS.get(pedestalKey);
        final double d2 = (double) pos.getZ() + 0.5D;
        worldIn.addParticle(PARTICLE_TYPES.get(pedestalKey), d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState
        , IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !isValidPosition(stateIn, worldIn, currentPos) 
        ? Blocks.AIR.getDefaultState() : stateIn;
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return !worldIn.isAirBlock(pos.down());
     }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        if( state.get(SUMMONED) == false && state.get(TEMP) == false )
            return super.getDrops( state.with(SET, false), builder );

        List<ItemStack> list = new ArrayList<>();
        return list;   
     }
     
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        Block lowerblock = worldIn.getBlockState(pos.down()).getBlock();
        if( lowerblock instanceof Pedestal == false )
            return;
        
        Pedestal pedestal = (Pedestal)lowerblock;
        if( pedestal.getType() == pedestalKey )
            worldIn.setBlockState( pos, state.with(SET, true ) );
    }
  
/*
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.isImmuneToFire() && entityIn instanceof LivingEntity
                && !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn)) {
            entityIn.attackEntityFrom(DamageSource.IN_FIRE, getFireDamageAmount());
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }
*/
}