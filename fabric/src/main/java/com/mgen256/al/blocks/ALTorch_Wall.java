package com.mgen256.al.blocks;
import com.mgen256.al.BlockSpec;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.Map;

import com.mgen256.al.FireTypes;
import com.mgen256.al.FabricFireTypesAdapter;
import com.mgen256.al.blocks.FabricFireTrait;
import com.mgen256.al.blocks.ALTorchWallSpec;
import com.mgen256.al.blocks.ALTorchWallCore;
import com.mgen256.al.items.FireTypeWandTrait;


public class ALTorch_Wall extends WallTorchBlock implements ModBlockSpec, FabricFireTrait, ALTorchWallSpec {
    private static final ALTorchWallCore.ShapeFactory<VoxelShape> SHAPE_FACTORY = box ->
        Block.box(box[0], box[1], box[2], box[3], box[4], box[5]);

    private final ALTorchWallCore<VoxelShape, Direction> core;

    private BlockSpec myKey;
    private final ResourceKey<Block> regKey;

    public ALTorch_Wall(Block mainblock, ResourceKey<Block> key) {
        super(ParticleTypes.FLAME, ALTorch.createSettings(mainblock, key));
        this.regKey = key;
        this.core = new ALTorchWallCore<>(
            key.identifier().getPath(),
            SHAPE_FACTORY,
            Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
        );
        registerDefaultState(getStateDefinition().any()
        .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
        .setValue(FIRE_TYPE, FabricFireTypesAdapter.fromCore(FireTypes.NORMAL))
        .setValue(PREVIOUS_FIRE_TYPE, FabricFireTypesAdapter.fromCore(FireTypes.NORMAL)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FIRE_TYPE);
        builder.add(PREVIOUS_FIRE_TYPE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return core.getShape(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        var fireType = state.getValue(FIRE_TYPE).toCore();
        if (fireType == FireTypes.LIGHT) {
            return;
        }

        var direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        var x = pos.getX() + 0.5;
        var y = pos.getY() + 0.9;
        var z = pos.getZ() + 0.5;
        var opposite = direction.getOpposite();
        var offset = 0.38;

        world.addParticle(ParticleTypes.SMOKE, x + offset * opposite.getStepX(), y, z + offset * opposite.getStepZ(), 0.0, 0.0, 0.0);

        var particleOption = fireType == FireTypes.SOUL ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;

        world.addParticle(particleOption, x + offset * opposite.getStepX(), y, z + offset * opposite.getStepZ(), 0.0, 0.0, 0.0);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (placer == null)
            return;

        if (placer.getOffhandItem().getItem() instanceof FireTypeWandTrait wand && wand.supportsAutomaticPlacementOnTorch()) {
            world.setBlock(pos, state.setValue(FIRE_TYPE, FabricFireTypesAdapter.fromCore(wand.getTargetFireType())), 3);
        }
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
