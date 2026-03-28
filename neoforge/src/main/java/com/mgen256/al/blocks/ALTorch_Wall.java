package com.mgen256.al.blocks;
import com.mgen256.al.BlockSpec;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

import com.mgen256.al.AdditionalLightsNeoForge;
import java.util.List;

import javax.annotation.Nullable;

import com.mgen256.al.FireTypes;
import com.mgen256.al.NeoForgeFireTypesAdapter;
import com.mgen256.al.items.FireTypeWandTrait;

public class ALTorch_Wall extends WallTorchBlock implements ModBlockSpec, NeoForgeFireTrait, ALTorchWallSpec {

    private static final ALTorchWallCore.ShapeFactory<VoxelShape> SHAPE_FACTORY = box ->
        Block.box(box[0], box[1], box[2], box[3], box[4], box[5]);

    private final ALTorchWallCore<VoxelShape, Direction> core;

    public ALTorch_Wall(Block mainblock, BlockSpec _floorKey, String name ) {
        super(ParticleTypes.FLAME, ALTorch.createProps(mainblock, name) );
        floorKey = _floorKey;
        this.name = name;
        this.core = new ALTorchWallCore<>(
            name,
            SHAPE_FACTORY,
            Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
        );
        registerDefaultState(stateDefinition.any()
            .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
            .setValue(FIRE_TYPE, NeoForgeFireTypesAdapter.fromCore(FireTypes.NORMAL))
            .setValue(PREVIOUS_FIRE_TYPE, NeoForgeFireTypesAdapter.fromCore(FireTypes.NORMAL)));
    }

    private BlockSpec floorKey;
    private BlockSpec myKey;
    private String name;
    
    @Override
    public void setMyKey(BlockSpec key) {
        myKey = key;
    }
 
    @Override
    public String getRegName() {
        return name;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FIRE_TYPE);
        builder.add(PREVIOUS_FIRE_TYPE);
    }
    
    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(FIRE_TYPE).toCore().getLuminance();
    }

    @Override
    public VoxelShape getShape(BlockState p_58152_, BlockGetter p_58153_, BlockPos p_58154_, CollisionContext p_58155_) {
        return core.getShape(p_58152_.getValue(FACING));
    }
 
    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        var fireType = stateIn.getValue(FIRE_TYPE).toCore();
        if (fireType == FireTypes.LIGHT) {
            return;
        }

        Direction direction = stateIn.getValue(BlockStateProperties.HORIZONTAL_FACING);
        double dx = pos.getX() + 0.5D;
        double dy = pos.getY() + 0.9D;
        double dz = pos.getZ() + 0.5D;
  
        Direction direction1 = direction.getOpposite();
        double d3 = 0.38D;
        level.addParticle(ParticleTypes.SMOKE, dx + d3 * direction1.getStepX(), dy, dz + d3 * direction1.getStepZ(), 0.0D, 0.0D, 0.0D);

        ParticleOptions particleOption;
        particleOption = fireType == FireTypes.SOUL ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;
            
        level.addParticle(particleOption, dx + d3 * direction1.getStepX(), dy, dz + d3 * direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
       }

    
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
    
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(AdditionalLightsNeoForge.getBlockItem(floorKey)));

        return list;
    }
    
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer == null) {
            return;
        }
        if (placer.getOffhandItem().getItem() instanceof FireTypeWandTrait wand && wand.supportsAutomaticPlacementOnTorch()) {
            level.setBlockAndUpdate(pos, state.setValue(FIRE_TYPE, NeoForgeFireTypesAdapter.fromCore(wand.getTargetFireType())));
        }
    }
}
