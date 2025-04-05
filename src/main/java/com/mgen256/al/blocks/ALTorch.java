package com.mgen256.al.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.util.shape.VoxelShape;

import com.mgen256.al.*;
import com.mgen256.al.items.SoulWand;

public class ALTorch extends TorchBlock implements IHasFire  {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(5.5D, 0.0D, 5.5D, 10.5D, 10.0D, 10.5D);

    
    public static Settings createSettings(Block mainblock, RegistryKey<Block> key) {
        return Settings.create()
            .sounds(mainblock.getDefaultState().getSoundGroup())
            .noCollision()
            .breakInstantly()
            .luminance((value)->value.get(FIRE_TYPE) == FireTypes.SOUL ? 10 : 14)
            .registryKey(key)
            ;
    }

    
    public ALTorch(Block mainblock, RegistryKey<Block> key) {
        super(ParticleTypes.FLAME, createSettings(mainblock, key));
        setDefaultState(getStateManager().getDefaultState()
            .with(FIRE_TYPE, FireTypes.NORMAL)
            .with(PREVIOUS_FIRE_TYPE, FireTypes.NORMAL));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FIRE_TYPE, PREVIOUS_FIRE_TYPE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
        var d0 = pos.getX() + 0.5D;
        var d1 = pos.getY() + 0.7D;
        var d2 = pos.getZ() + 0.5D;
        world.addParticleClient(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);

        var particleOption = state.get(FIRE_TYPE) == FireTypes.SOUL ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;

        world.addParticleClient(particleOption, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (placer == null)
            return;

        if (placer.getOffHandStack().getItem() instanceof SoulWand)
            world.setBlockState(pos, state.with(FIRE_TYPE, FireTypes.SOUL), 3);
    }

}
