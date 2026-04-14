package com.mgen256.al.blocks;
import com.mgen256.al.BlockSpec;

import java.util.Map;

import com.mgen256.al.PedestalTypes;
import com.mgen256.al.blocks.FireBaseCore;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public abstract class FireBase extends Block
    implements ModBlockSpec, FireBaseCore<VoxelShape, ParticleOptions, Level, BlockPos> {
    public static final BooleanProperty SET = BooleanProperty.create("set");
    public static final BooleanProperty SUMMONED = BooleanProperty.create("summoned");

    private static final Map<PedestalTypes, VoxelShape> SHAPES =
        FireBaseCore.createShapes(
            c -> Shapes.box(c[0], c[1], c[2], c[3], c[4], c[5]),
            16.0D);
    private static final Map<PedestalTypes, ParticleOptions> PARTICLE_TYPES = Map.of(
        PedestalTypes.standing_torch_s, ParticleTypes.SMOKE,
        PedestalTypes.standing_torch_l, ParticleTypes.SMOKE,
        PedestalTypes.fire_pit_s, ParticleTypes.LARGE_SMOKE,
        PedestalTypes.fire_pit_l, ParticleTypes.LARGE_SMOKE);

    private static final VoxelShape COLLISION_SHAPE = Shapes.box(0, 0, 0, 1, 0, 1);


    private BlockSpec myKey;
    private final ResourceKey<Block> regKey;

    private final PedestalTypes pedestalKey;

    @Override
    public Map<PedestalTypes, VoxelShape> getShapes() { return SHAPES; }

    @Override
    public Map<PedestalTypes, ParticleOptions> getParticleTypes() { return PARTICLE_TYPES; }

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
    public void spawnParticle(Level world, ParticleOptions particle, double x, double y, double z) {
        world.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    protected FireBase(PedestalTypes pedestalKey, Properties settings, ResourceKey<Block> key) {
        super(settings);
        this.pedestalKey = pedestalKey;
        this.regKey = key;
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(SET, false)
            .setValue(SUMMONED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SET, SUMMONED);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return FireBaseCore.super.getCollisionShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return FireBaseCore.super.getOutlineShape(state, world, pos, context);
    }
    
    @Environment(EnvType.CLIENT)
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        FireBaseCore.super.randomDisplayTick(state, world, pos, new java.util.Random());
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return direction == Direction.DOWN && !this.canSurvive(state, world, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    private void updatePedestalComparatorBelow(Level world, BlockPos pos, BlockState state) {
        if (!state.getValue(SUMMONED)) {
            return;
        }

        BlockPos belowPos = pos.below();
        BlockState belowState = world.getBlockState(belowPos);
        if (belowState.getBlock() instanceof Pedestal pedestal) {
            pedestal.updateComparatorOutput(world, belowPos);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean movedByPiston) {
        updatePedestalComparatorBelow(world, pos, state);
        super.affectNeighborsAfterRemoval(state, world, pos, movedByPiston);
    }

    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        var belowPos = pos.below();
        var belowState = world.getBlockState(belowPos);
        return !world.isEmptyBlock(belowPos) && belowState.getBlock() != Blocks.WATER;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        var lowerBlock = world.getBlockState(pos.below()).getBlock();
        if (!(lowerBlock instanceof Pedestal)) {
            return;
        }

        if (((Pedestal)lowerBlock).getType() == pedestalKey) {
            world.setBlockAndUpdate(pos, state.setValue(SET, true));
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public void setMyKey(BlockSpec key) {
        this.myKey = key;
    }

    @Override
    public String getRegName() {
        return regKey.identifier().getPath();
    }
}
