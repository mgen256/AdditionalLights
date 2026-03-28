package com.mgen256.al.blocks;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;

import static java.lang.Boolean.TRUE;

import javax.annotation.Nullable;

import com.mgen256.al.AdditionalLightsNeoForge;
import com.mgen256.al.ModBlockList;
import com.mgen256.al.ModSoundList;
import com.mgen256.al.PedestalTypes;
import com.mgen256.al.PedestalSize;
import com.mgen256.al.FireTypes;
import com.mgen256.al.NeoForgeFireTypesAdapter;
import com.mgen256.al.BlockSpec;
import com.mgen256.al.items.*;
import com.mgen256.al.blocks.PedestalTrait;
import com.mgen256.al.blocks.NeoForgeFireTrait;

import java.util.Map;

public abstract class Pedestal extends ModBlock
    implements SimpleWaterloggedBlock, NeoForgeFireTrait, PedestalTrait<Level, BlockPos, BlockState> {

    private static final Map<PedestalTypes, java.util.List<VoxelShape>> LIGHT_SHAPE_PARTS =
        PedestalLightShapeSpec.createShapePartMap(
            part -> Block.box(part.minX(), part.minY(), part.minZ(), part.maxX(), part.maxY(), part.maxZ()));

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty ACCEPT_POWER = BooleanProperty.create("accept_power");
    public static final BooleanProperty ISPOWERED = BooleanProperty.create("ispowered");
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private final VoxelShape baseShape;
    protected PedestalSize size;
    private VoxelShape lightHitboxShape;

    private static BlockBehaviour.Properties createProps(Block mainblock, String name) {
        return mainblock.properties()
            .lightLevel(Pedestal::getLightLevel)
            .setId(AdditionalLightsNeoForge.createResourceKey(name));
    }

    private static int getLightLevel(BlockState state) {
        return state.getValue(NeoForgeFireTrait.FIRE_TYPE).toCore() == FireTypes.LIGHT && state.getValue(LIT)
            ? FireTypes.LIGHT.getLuminance()
            : 0;
    }

    protected Pedestal( Block mainblock, String name, VoxelShape shape, PedestalSize size ) {
        super(mainblock, name, createProps(mainblock, name), shape);
        this.baseShape = shape;

        registerDefaultState( stateDefinition.any()
            .setValue(BlockStateProperties.WATERLOGGED, false)
            .setValue(FIRE_TYPE, NeoForgeFireTypesAdapter.fromCore(FireTypes.NORMAL))
            .setValue(PREVIOUS_FIRE_TYPE, NeoForgeFireTypesAdapter.fromCore(FireTypes.NORMAL))
            .setValue( ACCEPT_POWER, true )
            .setValue( ISPOWERED, false )
            .setValue( ACTIVATED, false )
            .setValue( LIT, false )
            );

        this.size = size;

        if( ignitionSound == null )
        {
            ignitionSound = size == PedestalSize.L ? AdditionalLightsNeoForge.getSound(ModSoundList.Fire_Ignition_L)
                : AdditionalLightsNeoForge.getSound( ModSoundList.Fire_Ignition_S );
        }
    }


    private static SoundEvent ignitionSound;
    protected abstract BlockSpec getFireKey(BlockState state);
    public abstract PedestalTypes getType( );

    @Override
    public BlockPos offsetUp(BlockPos pos) { return pos.above(); }

    @Override
    public BlockState getBlockState(Level level, BlockPos pos) { return level.getBlockState(pos); }

    @Override
    public boolean setBlockState(Level level, BlockPos pos, BlockState state) {
        return level.setBlockAndUpdate(pos, state);
    }

    @Override
    public void breakBlock(Level level, BlockPos pos) { level.destroyBlock(pos, true); }

    @Override
    public boolean isAir(BlockState state) { return state.getBlock() == Blocks.AIR; }

    @Override
    public boolean isWater(BlockState state) { return state.getBlock() == Blocks.WATER; }

    @Override
    public boolean hasRedstonePower(Level level, BlockPos pos) { return level.hasNeighborSignal(pos); }

    @Override
    public boolean isFireBlock(BlockState state) { return state.getBlock() instanceof FireBase; }

    @Override
    public boolean isFireSummoned(BlockState state) { return state.getValue(FireBase.SUMMONED); }

    @Override
    public BlockState createFireState(BlockState pedestalState) {
        return getFireBlock(pedestalState).defaultBlockState()
            .setValue(FireBase.SET, true)
            .setValue(FireBase.SUMMONED, true);
    }

    @Override
    public boolean shouldPlaceFire(BlockState state) {
        return state.getValue(FIRE_TYPE).toCore() != FireTypes.LIGHT;
    }

    @Override
    public boolean getAcceptPower(BlockState state) { return state.getValue(ACCEPT_POWER); }

    @Override
    public boolean getIsPowered(BlockState state) { return state.getValue(ISPOWERED); }

    @Override
    public boolean getActivated(BlockState state) { return state.getValue(ACTIVATED); }

    @Override
    public boolean isLightFireType(BlockState state) { return state.getValue(FIRE_TYPE).toCore() == FireTypes.LIGHT; }

    @Override
    public boolean getLit(BlockState state) { return state.getValue(LIT); }

    @Override
    public BlockState setIsPowered(BlockState state, boolean value) { return state.setValue(ISPOWERED, value); }

    @Override
    public BlockState setActivated(BlockState state, boolean value) { return state.setValue(ACTIVATED, value); }

    @Override
    public BlockState setLit(BlockState state, boolean value) { return state.setValue(LIT, value); }

    @Override
    public void onIgnited(Level level, BlockPos pos) {
        playIgnitionSound(level, null, level.getBlockState(pos).getBlock(), pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add( BlockStateProperties.WATERLOGGED );
        builder.add( FIRE_TYPE );
        builder.add( PREVIOUS_FIRE_TYPE );
        builder.add( ACCEPT_POWER );
        builder.add( ISPOWERED );
        builder.add( ACTIVATED );
        builder.add( LIT );
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity p_393688_, BlockGetter blockgetter, BlockPos pos, BlockState state, Fluid fluidIn) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return resolveActiveShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return resolveActiveShape(state);
    }

    private VoxelShape resolveActiveShape(BlockState state) {
        if (state.getValue(FIRE_TYPE).toCore() != FireTypes.LIGHT) {
            return baseShape;
        }

        if (lightHitboxShape == null) {
            VoxelShape mergedShape = baseShape;
            for (VoxelShape part : LIGHT_SHAPE_PARTS.get(getType())) {
                mergedShape = Shapes.or(mergedShape, part);
            }
            lightHitboxShape = mergedShape;
        }
        return lightHitboxShape;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        
        return state.getValue(BlockStateProperties.WATERLOGGED)  == Boolean.TRUE 
            ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return getLightLevel(state);
    }

    private Block getFireBlock(BlockState state){
        return AdditionalLightsNeoForge.getBlock( getFireKey(state) );
    }
    
    @Override
    protected InteractionResult useItemOn(
        ItemStack p_330929_, BlockState p_335716_, Level p_336112_, BlockPos p_328869_, Player p_332840_, InteractionHand p_336117_, BlockHitResult p_332723_
    ) {
        if( p_330929_.getItem() instanceof Wand )
            return InteractionResult.FAIL;

        if( !igniteByInteraction( p_336112_, p_328869_, p_335716_ ) )
            return InteractionResult.PASS;

        playIgnitionSound( p_336112_, p_332840_, p_335716_.getBlock(), p_328869_ );

        return InteractionResult.SUCCESS;
    }

    private static void playIgnitionSound(Level level, Player player, Block block, BlockPos pos)
    {
        float volume = block instanceof FirePitBase ? 2.0f : 1.5f;
        level.playSound( player, pos, ignitionSound, SoundSource.BLOCKS, volume, 1.0f );
    }

    
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if( placer == null )
            return;
            
        if (placer.getOffhandItem().getItem() instanceof FireTypeWandTrait wand) {
            FireTypes target = wand.getTargetFireType();
            boolean allow = target == FireTypes.SOUL || (
                target == FireTypes.LIGHT && (
                    getType() == PedestalTypes.standing_torch_s || getType() == PedestalTypes.standing_torch_l
                    || getType() == PedestalTypes.fire_pit_s
                    || getType() == PedestalTypes.fire_pit_l
                )
            );
            if (allow) {
                state = state.setValue(FIRE_TYPE, NeoForgeFireTypesAdapter.fromCore(target));
            }
        }

        if( placer.isSuppressingSlidingDownLadder() )
            level.setBlockAndUpdate( pos, withLightLit(state.setValue( ACCEPT_POWER, false ), false) );
        else
        {
            state = withLightLit(state, true);
            level.setBlockAndUpdate( pos, state );
            igniteFire( level, pos, state, false );
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, @Nullable Orientation orientation, boolean isMoving) {
        handleNeighborUpdate(state, level, pos);
        super.neighborChanged(state, level, pos, blockIn, orientation, isMoving);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
