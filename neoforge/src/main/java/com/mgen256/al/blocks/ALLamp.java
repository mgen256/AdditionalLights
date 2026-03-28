package com.mgen256.al.blocks;

import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

import com.mgen256.al.AdditionalLightsNeoForge;
import com.mgen256.al.blocks.ALLampSpec;
import com.mgen256.al.blocks.ALLampCore;



public class ALLamp extends ModBlock implements SimpleWaterloggedBlock, ALLampSpec {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    
    private static final ALLampCore.ShapeFactory<VoxelShape> SHAPE_FACTORY = box ->
        Block.box(box[0], box[1], box[2], box[3], box[4], box[5]);

    private final ALLampCore<VoxelShape, LevelReader, BlockPos, BlockState, Direction, FluidState> core;

    private static Properties createProps( Block mainblock, String name ){
        return BlockBehaviour.Properties.of()
            .sound( mainblock.defaultBlockState().getSoundType() )
            .mapColor( MapColor.NONE )
            .pushReaction( PushReaction.NORMAL )
            .instabreak()
            .lightLevel( lightLevel -> LUMINANCE )
            .noCollision()
            .setId(AdditionalLightsNeoForge.createResourceKey(name))
            ;
    }

    public ALLamp(Block mainblock, String name ) {
        super( mainblock, name, createProps(mainblock, name), Shapes.empty());
        this.core = new ALLampCore<>(
            name,
            SHAPE_FACTORY,
            Direction.DOWN, Direction.UP, Direction.NORTH,
            Direction.SOUTH, Direction.WEST, Direction.EAST
        ) {
            @Override
            protected BlockState getBlockState(LevelReader level, BlockPos pos) { return level.getBlockState(pos); }

            @Override
            protected FluidState getFluidState(LevelReader level, BlockPos pos) { return level.getFluidState(pos); }

            @Override
            protected boolean isWaterFluid(FluidState state) { return state.getType() == Fluids.WATER; }

            @Override
            protected boolean canBeReplaced(BlockState state) { return state.canBeReplaced(); }

            @Override
            protected boolean isAir(BlockState state) { return state.isAir(); }

            @Override
            protected boolean isWaterBlock(BlockState state) { return state.getBlock() == Blocks.WATER; }

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
            protected BlockState withFacing(BlockState state, Direction dir) { return state.setValue(BlockStateProperties.FACING, dir); }

            @Override
            protected BlockState withWaterlogged(BlockState state, boolean value) { return state.setValue(BlockStateProperties.WATERLOGGED, value); }

            @Override
            protected Direction getFacing(BlockState state) { return state.getValue(BlockStateProperties.FACING); }

            @Override
            protected boolean isSameBlock(BlockState state) { return state.getBlock() == ALLamp.this; }
        };
    }
      
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.WATERLOGGED);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return core.getPlacementState(context.getLevel(), context.getClickedPos(), context.getClickedFace());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockgetter, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(BlockStateProperties.FACING);
        return core.getShape(facing);
    }

    @Override
    public RenderShape getRenderShape( BlockState state ) {
        return RenderShape.MODEL;
    }
    
    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity p_393688_, BlockGetter blockgetter, BlockPos pos, BlockState state, Fluid fluidIn) {
        return true;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
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
        return facing == stateIn.getValue(BlockStateProperties.FACING).getOpposite() 
            && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : stateIn;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return core.canSurvive(state, level, pos);
    }
}
