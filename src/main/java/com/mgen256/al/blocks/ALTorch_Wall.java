package com.mgen256.al.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.Map;

import com.mgen256.al.*;
import com.mgen256.al.items.SoulWand;


public class ALTorch_Wall extends WallTorchBlock implements IHasFire {
    private static final Map<Direction, VoxelShape> SHAPES = Map.of(
        Direction.NORTH, VoxelShapes.cuboid(5.5D / 16.0D, 2.0D / 16.0D, 11.0D / 16.0D, 10.5D / 16.0D, 13.0D / 16.0D, 16.0D / 16.0D),
        Direction.SOUTH, VoxelShapes.cuboid(5.5D / 16.0D, 2.0D / 16.0D, 0.0D / 16.0D, 10.5D / 16.0D, 13.0D / 16.0D, 5.0D / 16.0D),
        Direction.WEST, VoxelShapes.cuboid(11.0D / 16.0D, 2.0D / 16.0D, 5.5D / 16.0D, 16.0D / 16.0D, 13.0D / 16.0D, 10.5D / 16.0D),
        Direction.EAST, VoxelShapes.cuboid(0.0D / 16.0D, 2.0D / 16.0D, 5.5D / 16.0D, 5.0D / 16.0D, 13.0D / 16.0D, 10.5D / 16.0D)
    );

    public ALTorch_Wall(Block mainblock, RegistryKey<Block> key) {
        super(ParticleTypes.FLAME, ALTorch.createSettings(mainblock, key));
        setDefaultState(getStateManager().getDefaultState()
        .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
        .with(FIRE_TYPE, FireTypes.NORMAL)
        .with(PREVIOUS_FIRE_TYPE, FireTypes.NORMAL));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FIRE_TYPE);
        builder.add(PREVIOUS_FIRE_TYPE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(Properties.HORIZONTAL_FACING));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        var direction = state.get(Properties.HORIZONTAL_FACING);
        var x = pos.getX() + 0.5;
        var y = pos.getY() + 0.9;
        var z = pos.getZ() + 0.5;
        var opposite = direction.getOpposite();
        var offset = 0.38;

        world.addParticleClient(ParticleTypes.SMOKE, x + offset * opposite.getOffsetX(), y, z + offset * opposite.getOffsetZ(), 0.0, 0.0, 0.0);

        var particleOption = state.get(FIRE_TYPE) == FireTypes.SOUL ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;

        world.addParticleClient(particleOption, x + offset * opposite.getOffsetX(), y, z + offset * opposite.getOffsetZ(), 0.0, 0.0, 0.0);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (placer == null)
            return;

        if (placer.getOffHandStack().getItem() instanceof SoulWand)
            world.setBlockState(pos, state.with(FIRE_TYPE, FireTypes.SOUL), 3);
    }
}