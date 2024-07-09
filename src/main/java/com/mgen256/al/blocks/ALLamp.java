package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import org.jetbrains.annotations.Nullable;

public class ALLamp extends Block implements Waterloggable {

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final DirectionProperty FACING = Properties.FACING;

    private static final VoxelShape[] SHAPES = {
        Block.createCuboidShape(5.0, 14.0, 5.0, 11.0, 16.0, 11.0),  // down
        Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 2.0, 11.0),    // up
        Block.createCuboidShape(6.0, 7.0, 12.0, 10.0, 13.0, 16.0),  // north
        Block.createCuboidShape(6.0, 7.0, 0.0, 10.0, 13.0, 4.0),    // south
        Block.createCuboidShape(12.0, 7.0, 6.0, 16.0, 13.0, 10.0),  // west
        Block.createCuboidShape(0.0, 7.0, 6.0, 4.0, 13.0, 10.0),    // east
    };

    public ALLamp(Block mainblock) {
        super(Settings.create()
            .sounds(mainblock.getDefaultState().getSoundGroup())
            .noCollision()
            .breakInstantly()
            .luminance((state) -> 15));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var waterlogged = ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER;
        var direction = willBeReplaced(ctx.getWorld(), ctx.getBlockPos()) ? Direction.UP : ctx.getSide();
        return this.getDefaultState().with(FACING, direction).with(WATERLOGGED, waterlogged);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        var facing = state.get(FACING);
        return SHAPES[facing.getId()];
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (willBeReplaced(world, pos)) 
            return !world.isAir(pos.down());

        var direction = state.get(FACING);
        var blockpos = pos.offset(direction.getOpposite());

        return !world.isAir(blockpos) && !(world.getBlockState(blockpos).getBlock() == this);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.get(FACING).getOpposite() && !state.canPlaceAt(world, pos)) {
            return net.minecraft.block.Blocks.AIR.getDefaultState();
        }
        return state;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    private boolean willBeReplaced(WorldView world, BlockPos pos) {
        var blockstate = world.getBlockState(pos);
        return blockstate.isReplaceable() && (!blockstate.isAir() && blockstate.getBlock() != net.minecraft.block.Blocks.WATER);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
}