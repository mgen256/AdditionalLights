package com.mgen256.al.blocks;
import com.mgen256.al.BlockSpec;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import com.mgen256.al.blocks.ALLampSpec;
import com.mgen256.al.blocks.ALLampCore;

public class ALLamp extends Block implements SimpleWaterloggedBlock, ModBlockSpec, ALLampSpec {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    private static final ALLampCore.ShapeFactory<VoxelShape> SHAPE_FACTORY = box ->
        Block.box(box[0], box[1], box[2], box[3], box[4], box[5]);

    private final ALLampCore<VoxelShape, LevelReader, BlockPos, BlockState, Direction, FluidState> core;

    private BlockSpec myKey;
    private final ResourceKey<Block> regKey;

    public ALLamp(Block mainblock, ResourceKey<Block> key) {
        super(Properties.of()
            .sound(mainblock.defaultBlockState().getSoundType())
            .noCollision()
            .instabreak()
            .lightLevel(state -> LUMINANCE)
            .setId(key)
        );
        this.regKey = key;
        this.core = new ALLampCore<>(
            key.identifier().getPath(),
            SHAPE_FACTORY,
            Direction.DOWN, Direction.UP, Direction.NORTH,
            Direction.SOUTH, Direction.WEST, Direction.EAST
        ) {
            @Override
            protected BlockState getBlockState(LevelReader world, BlockPos pos) { return world.getBlockState(pos); }

            @Override
            protected FluidState getFluidState(LevelReader world, BlockPos pos) { return world.getFluidState(pos); }

            @Override
            protected boolean isWaterFluid(FluidState state) { return state.getType() == Fluids.WATER; }

            @Override
            protected boolean canBeReplaced(BlockState state) { return state.canBeReplaced(); }

            @Override
            protected boolean isAir(BlockState state) { return state.isAir(); }

            @Override
            protected boolean isWaterBlock(BlockState state) { return state.getBlock() == net.minecraft.world.level.block.Blocks.WATER; }

            @Override
            protected BlockPos offset(BlockPos pos, Direction dir) { return pos.relative(dir); }

            @Override
            protected Direction opposite(Direction dir) { return dir.getOpposite(); }

            @Override
            protected Direction up() { return Direction.UP; }

            @Override
            protected Direction down() { return Direction.DOWN; }

            @Override
            protected BlockState defaultState() { return defaultBlockState(); }

            @Override
            protected BlockState withFacing(BlockState state, Direction dir) { return state.setValue(FACING, dir); }

            @Override
            protected BlockState withWaterlogged(BlockState state, boolean value) { return state.setValue(WATERLOGGED, value); }

            @Override
            protected Direction getFacing(BlockState state) { return state.getValue(FACING); }

            @Override
            protected boolean isSameBlock(BlockState state) { return state.getBlock() == ALLamp.this; }
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return core.getPlacementState(ctx.getLevel(), ctx.getClickedPos(), ctx.getClickedFace());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return core.getShape(state.getValue(FACING));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return core.canSurvive(state, world, pos);
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction == state.getValue(FACING).getOpposite() && !core.canSurvive(state, world, pos)) {
            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }


    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
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
