package com.mgen256.al.blocks;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.mgen256.al.*;


import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;


public abstract class FireBase extends ModBlock{
    
    public static BooleanProperty SET = BooleanProperty.create("set");
    public static BooleanProperty SUMMONED = BooleanProperty.create("summoned");
    public static BooleanProperty TEMP = BooleanProperty.create("temp");

    private static Map<PedestalTypes, VoxelShape> SHAPES;
    private static Map<PedestalTypes, SimpleParticleType> PARTICLE_TYPES;
    private static Map<PedestalTypes, Double> SMOKE_POS;

    private static final VoxelShape COLLISION_SHAPE = Block.box(0, 0, 0, 16, 0, 16);

    static {
        SHAPES = new LinkedHashMap<PedestalTypes, VoxelShape>();
        SHAPES.put( PedestalTypes.standing_torch_s, Block.box(4.0D, -6.0D, 4.0D, 12.0D, 2.0D, 12.0D) );
        SHAPES.put( PedestalTypes.standing_torch_l, Block.box(4.0D, -2.0D, 4.0D, 12.0D, 6.0D, 12.0D) );
        SHAPES.put( PedestalTypes.fire_pit_s, Block.box(0.0D, -10.0D, 0.0D, 16.0D, 2.0D, 16.0D) );
        SHAPES.put( PedestalTypes.fire_pit_l, Block.box(0.0D, -2.0D, 0.0D, 16.0D, 7.0D, 16.0D) );

        PARTICLE_TYPES = new LinkedHashMap<PedestalTypes, SimpleParticleType>();
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
    
    protected static Properties createProps( MaterialColor mapColor ){
        return BlockBehaviour.Properties.of( Material.DECORATION, mapColor)
            .instabreak()
            .noCollission()
            .sound(new ForgeSoundType(1.5F, 1.0F,() -> AdditionalLights.getSound( ModSoundList.Fire_Extinguish ), () -> SoundEvents.WOOL_STEP
            , () -> SoundEvents.STONE_PLACE, () -> SoundEvents.WOOL_HIT, () -> SoundEvents.WOOL_FALL ) );
    }

    protected FireBase( PedestalTypes _pedestalKey, Properties props ) {
        super( null, props, SHAPES.get(_pedestalKey));

        pedestalKey = _pedestalKey;
        this.registerDefaultState(this.stateDefinition.any().setValue(SET, Boolean.valueOf(false) ).setValue(SUMMONED, false).setValue(TEMP, false) );
      }

      
    private PedestalTypes pedestalKey;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add( SET );
        builder.add( SUMMONED );
        builder.add( TEMP );
    }

    protected float getFireDamageAmount() {
        return 0.0F;
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter blockgetter, BlockPos pos, CollisionContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockgetter, BlockPos pos, CollisionContext context) {
        return SHAPES.get(pedestalKey);
    }
    
    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        double d0 = pos.getX() + 0.5D;
        double d1 = pos.getY() + SMOKE_POS.get(pedestalKey);
        double d2 = pos.getZ() + 0.5D;
        level.addParticle(PARTICLE_TYPES.get(pedestalKey), d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState
        , LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return ( facing == Direction.DOWN ) && !isValidPosition(stateIn, level, currentPos) ? Blocks.AIR.defaultBlockState() : stateIn;
    }

    public boolean isValidPosition(BlockState state, LevelAccessor level, BlockPos pos) {
        return !level.isEmptyBlock(pos.below()) && level.getBlockState(pos.below()).getMaterial() != Material.WATER;
     }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        if( state.getValue(SUMMONED) == FALSE && state.getValue(TEMP) == FALSE )
            return super.getDrops( state.setValue(SET, false), builder );

        List<ItemStack> list = new ArrayList<>();
        return list;   
     }
     
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        Block lowerblock = level.getBlockState(pos.below()).getBlock();
        if( lowerblock instanceof Pedestal != TRUE )
            return;
        
        Pedestal pedestal = (Pedestal)lowerblock;
        if( pedestal.getType() == pedestalKey )
            level.setBlockAndUpdate( pos, state.setValue(SET, true ) );
    }
      
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter blockgetter, BlockPos pos, PathComputationType type) {
        return false;
    }

}