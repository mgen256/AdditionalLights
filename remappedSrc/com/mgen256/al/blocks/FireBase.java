package com.mgen256.al.blocks;

import java.util.Map;

import com.mgen256.al.*;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import java.util.HashMap;

public abstract class FireBase extends Block {
    public static final BooleanProperty SET = BooleanProperty.of("set");
    public static final BooleanProperty SUMMONED = BooleanProperty.of("summoned");

    private static final Map<PedestalTypes, VoxelShape> SHAPES = new HashMap<>();
    private static final Map<PedestalTypes, ParticleEffect> PARTICLE_TYPES = new HashMap<>();
    private static final Map<PedestalTypes, Double> SMOKE_POS = new HashMap<>();

    private static final VoxelShape COLLISION_SHAPE = VoxelShapes.cuboid(0, 0, 0, 1, 0, 1);

    static {
        SHAPES.put(PedestalTypes.standing_torch_s, VoxelShapes.cuboid(0.25D, -0.375D, 0.25D, 0.75D, 0.125D, 0.75D));
        SHAPES.put(PedestalTypes.standing_torch_l, VoxelShapes.cuboid(0.25D, -0.125D, 0.25D, 0.75D, 0.375D, 0.75D));
        SHAPES.put(PedestalTypes.fire_pit_s, VoxelShapes.cuboid(0.0D, -0.625D, 0.0D, 1.0D, 0.125D, 1.0D));
        SHAPES.put(PedestalTypes.fire_pit_l, VoxelShapes.cuboid(0.0D, -0.125D, 0.0D, 1.0D, 0.4375D, 1.0D));

        PARTICLE_TYPES.put(PedestalTypes.standing_torch_s, ParticleTypes.SMOKE);
        PARTICLE_TYPES.put(PedestalTypes.standing_torch_l, ParticleTypes.SMOKE);
        PARTICLE_TYPES.put(PedestalTypes.fire_pit_s, ParticleTypes.LARGE_SMOKE);
        PARTICLE_TYPES.put(PedestalTypes.fire_pit_l, ParticleTypes.LARGE_SMOKE);

        SMOKE_POS.put(PedestalTypes.standing_torch_s, 0.2);
        SMOKE_POS.put(PedestalTypes.standing_torch_l, 0.7);
        SMOKE_POS.put(PedestalTypes.fire_pit_s, 0.0);
        SMOKE_POS.put(PedestalTypes.fire_pit_l, 0.8);
    }

    private final PedestalTypes pedestalKey;

    protected FireBase(PedestalTypes pedestalKey, Settings settings) {
        super(settings);
        this.pedestalKey = pedestalKey;
        this.setDefaultState(this.stateManager.getDefaultState()
            .with(SET, false)
            .with(SUMMONED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SET, SUMMONED);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(pedestalKey);
    }
    
    @Environment(EnvType.CLIENT)
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double d0 = pos.getX() + 0.5D;
        double d1 = pos.getY() + SMOKE_POS.get(pedestalKey);
        double d2 = pos.getZ() + 0.5D;
        
        ParticleEffect particleType = PARTICLE_TYPES.get(pedestalKey);
        world.addParticle(particleType, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        return direction == Direction.DOWN && !this.canPlaceAt(state, world, pos) ? Blocks.AIR.getDefaultState() : state;
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var belowPos = pos.down();
        var belowState = world.getBlockState(belowPos);
        return !world.isAir(belowPos) && belowState.getBlock() != Blocks.WATER;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        var lowerBlock = world.getBlockState(pos.down()).getBlock();
        if (!(lowerBlock instanceof Pedestal)) {
            return;
        }

        if (((Pedestal)lowerBlock).getType() == pedestalKey) {
            world.setBlockState(pos, state.with(SET, true));
        }
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}