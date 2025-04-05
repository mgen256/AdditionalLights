package com.mgen256.al.blocks;

import com.mgen256.al.*;
import com.mgen256.al.items.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Waterloggable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;

import org.jetbrains.annotations.Nullable;


public abstract class Pedestal extends ModBlock implements Waterloggable, IHasFire {

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty ACCEPT_POWER = BooleanProperty.of("accept_power");
    public static final BooleanProperty ISPOWERED = BooleanProperty.of("ispowered");
    public static final BooleanProperty ACTIVATED = BooleanProperty.of("activated");

    enum SIZE {S,L}
    protected SIZE size;

    protected Pedestal(Block mainblock, RegistryKey<Block> key, VoxelShape shape, SIZE size) {
        super(Block.Settings.copy(mainblock).registryKey(key),shape);
        this.size = size;
        setDefaultState(getStateManager().getDefaultState()
            .with(WATERLOGGED, false)
            .with(FIRE_TYPE, FireTypes.NORMAL)
            .with(PREVIOUS_FIRE_TYPE, FireTypes.NORMAL)
            .with(ACCEPT_POWER, true)
            .with(ISPOWERED, false)
            .with(ACTIVATED, false)
        );

        if (ignitionSound == null) {
            ignitionSound = size == SIZE.L ? ModSoundList.Fire_Ignition_L.get()
                : ModSoundList.Fire_Ignition_S.get();
        }
    }

    private static SoundEvent ignitionSound;
    protected abstract ModBlockList getFireBlock(BlockState state);
    public abstract PedestalTypes getType();

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FIRE_TYPE, PREVIOUS_FIRE_TYPE, ACCEPT_POWER, ISPOWERED, ACTIVATED);
    }

    @Override
    public boolean canFillWithFluid(@Nullable LivingEntity filler, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return true;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    public boolean igniteFire(World world, BlockPos pos, BlockState state, boolean replaceOnly) {
        var upperPos = pos.up();
        var upperBlockState = world.getBlockState(upperPos);
        var upperBlock = upperBlockState.getBlock();
        FireBase fireBase = null;
        if (upperBlock instanceof FireBase) {
            fireBase = (FireBase) upperBlock;
        }
        if (replaceOnly) {
            if (fireBase == null) {
                return false;
            }
        } else {
            if (!(upperBlockState.isAir() || upperBlock == Blocks.WATER || fireBase != null)) {
                return false;
            }
        }
        if (fireBase != null && !world.getBlockState(upperPos).get(FireBase.SUMMONED)) {
            world.breakBlock(upperPos, true);
        }
        var ret = world.setBlockState(upperPos, getFireBlock(state).get().getDefaultState()
            .with(FireBase.SET, true)
            .with(FireBase.SUMMONED, true));

    return ret;
    }

    public void removeFire(World world, BlockPos pos, BlockState state) {
        if (!(world.getBlockState(pos.up()).getBlock() instanceof FireBase)) {
            return;
        }
        world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!igniteFire(world, pos, state, false))
            return ActionResult.PASS;

        playIgnitionSound(world, player, state.getBlock(), pos);
        return ActionResult.SUCCESS;
    }
    
    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof Wand) 
            return ActionResult.PASS;
        
        return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
    }

    private static void playIgnitionSound(World world, PlayerEntity player, Block block, BlockPos pos) {
        float volume = block instanceof FirePitBase ? 2.0f : 1.5f;
        world.playSound(player, pos, ignitionSound, SoundCategory.BLOCKS, volume, 1.0f);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (placer == null) {
            return;
        }

        if (placer.getOffHandStack().getItem() instanceof SoulWand) {
            state = state.with(FIRE_TYPE, FireTypes.SOUL);
        }

        if (placer.isSneaking()) {
            world.setBlockState(pos, state.with(ACCEPT_POWER, false));
        } else {
            world.setBlockState(pos, state);
            igniteFire(world, pos, state, false);
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        state = world.getBlockState(pos);
        if (!state.get(ACCEPT_POWER)) {
            return;
        }

        if (world.isReceivingRedstonePower(pos)) {
            if (state.get(ACTIVATED)) {
                return;
            }

            if (igniteFire(world, pos, state, false)) {
                playIgnitionSound(world, null, state.getBlock(), pos);
            }

            world.setBlockState(pos, state.with(ISPOWERED, true).with(ACTIVATED, true));
        } else if (state.get(ISPOWERED) && state.get(ACTIVATED)) {
            removeFire(world, pos, state);
            world.setBlockState(pos, state.with(ISPOWERED, false).with(ACTIVATED, false));
        }
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}