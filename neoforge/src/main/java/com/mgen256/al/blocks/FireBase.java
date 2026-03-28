package com.mgen256.al.blocks;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.mgen256.al.AdditionalLightsNeoForge;
import com.mgen256.al.ModSoundList;
import com.mgen256.al.PedestalTypes;
import com.mgen256.al.blocks.FireBaseCore;


import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.neoforged.neoforge.common.util.DeferredSoundType;


public abstract class FireBase extends ModBlock
    implements FireBaseCore<VoxelShape, SimpleParticleType, Level, BlockPos> {
    
    public static BooleanProperty SET = BooleanProperty.create("set");
    public static BooleanProperty SUMMONED = BooleanProperty.create("summoned");
    public static BooleanProperty TEMP = BooleanProperty.create("temp");

    private static final Map<PedestalTypes, VoxelShape> SHAPES =
        FireBaseCore.createShapes(
            c -> Block.box(c[0], c[1], c[2], c[3], c[4], c[5]),
            1.0D);
    private static final Map<PedestalTypes, SimpleParticleType> PARTICLE_TYPES = Map.of(
        PedestalTypes.standing_torch_s, ParticleTypes.SMOKE,
        PedestalTypes.standing_torch_l, ParticleTypes.SMOKE,
        PedestalTypes.fire_pit_s, ParticleTypes.LARGE_SMOKE,
        PedestalTypes.fire_pit_l, ParticleTypes.LARGE_SMOKE);

    private static final VoxelShape COLLISION_SHAPE = Block.box(0, 0, 0, 16, 0, 16);

    
    protected static Properties createProps( MapColor mapColor, String name ){
        return BlockBehaviour.Properties.of()
            .instabreak()
            .noCollision()
            .mapColor( mapColor )
            .sound( new DeferredSoundType(1.5F, 1.0F,() -> AdditionalLightsNeoForge.getSound( ModSoundList.Fire_Extinguish ), () -> SoundEvents.WOOL_STEP
            , () -> SoundEvents.STONE_PLACE, () -> SoundEvents.WOOL_HIT, () -> SoundEvents.WOOL_FALL ) )
            .setId(AdditionalLightsNeoForge.createResourceKey(name))
            ;
    }

    protected FireBase( PedestalTypes _pedestalKey, String name, Properties props ) {
        super( null, name, props, SHAPES.get(_pedestalKey));

        pedestalKey = _pedestalKey;
        this.registerDefaultState(this.stateDefinition.any().setValue(SET, Boolean.valueOf(false) ).setValue(SUMMONED, false).setValue(TEMP, false) );
      }

      
    private PedestalTypes pedestalKey;

    @Override
    public Map<PedestalTypes, VoxelShape> getShapes() { return SHAPES; }

    @Override
    public Map<PedestalTypes, SimpleParticleType> getParticleTypes() { return PARTICLE_TYPES; }

    @Override
    public PedestalTypes getPedestalKey() { return pedestalKey; }

    @Override
    public VoxelShape getCollisionShapeImpl() { return COLLISION_SHAPE; }

    @Override
    public double getX(BlockPos pos) { return pos.getX(); }

    @Override
    public double getY(BlockPos pos) { return pos.getY(); }

    @Override
    public double getZ(BlockPos pos) { return pos.getZ(); }

    @Override
    public void spawnParticle(Level level, SimpleParticleType type, double x, double y, double z) {
        level.addParticle(type, x, y, z, 0.0D, 0.0D, 0.0D);
    }

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
        return FireBaseCore.super.getCollisionShape(state, blockgetter, pos, context);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockgetter, BlockPos pos, CollisionContext context) {
        return FireBaseCore.super.getOutlineShape(state, blockgetter, pos, context);
    }
    
    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        double d0 = pos.getX() + 0.5D;
        double d1 = pos.getY() + FireBaseCore.SMOKE_POS.get(pedestalKey);
        double d2 = pos.getZ() + 0.5D;
        level.addParticle(PARTICLE_TYPES.get(pedestalKey), d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }
    
    @Override
    public BlockState updateShape(
        BlockState stateIn, 
        LevelReader level, 
        ScheduledTickAccess sta, 
        BlockPos currentPos, 
        Direction facing, 
        BlockPos facingPos,
        BlockState facingState, 
        RandomSource randomSource
        ) {
        return ( facing == Direction.DOWN ) && !isValidPosition(stateIn, level, currentPos) ? Blocks.AIR.defaultBlockState() : stateIn;
    }

    public boolean isValidPosition(BlockState state, LevelReader level, BlockPos pos) {
        var belowPos = pos.below();
        var belowState = level.getBlockState(belowPos);

        return !level.isEmptyBlock(belowPos) && belowState.getBlock() != Blocks.WATER;
     }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
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
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

}
