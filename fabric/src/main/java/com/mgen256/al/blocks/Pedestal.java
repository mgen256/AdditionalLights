package com.mgen256.al.blocks;

import com.mgen256.al.AdditionalLightsFabric;
import com.mgen256.al.FireTypes;
import com.mgen256.al.FabricFireTypesAdapter;
import com.mgen256.al.blocks.FabricFireTrait;
import com.mgen256.al.PedestalTypes;
import com.mgen256.al.PedestalSize;
import com.mgen256.al.ModBlocks;
import com.mgen256.al.BlockSpec;
import com.mgen256.al.ModSoundList;
import com.mgen256.al.items.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.mgen256.al.blocks.PedestalTrait;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class Pedestal extends ModBlock
    implements SimpleWaterloggedBlock, FabricFireTrait, PedestalTrait<Level, BlockPos, BlockState> {

    private static final Map<PedestalTypes, java.util.List<VoxelShape>> LIGHT_SHAPE_PARTS =
        PedestalLightShapeSpec.createShapePartMap(
            part -> Block.box(part.minX(), part.minY(), part.minZ(), part.maxX(), part.maxY(), part.maxZ()));

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty ACCEPT_POWER = BooleanProperty.create("accept_power");
    public static final BooleanProperty ISPOWERED = BooleanProperty.create("ispowered");
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private final VoxelShape baseShape;
    protected PedestalSize size;
    private VoxelShape lightHitboxShape;

    private static Properties createProperties(Block mainblock, ResourceKey<Block> key) {
        return Properties.ofFullCopy(mainblock)
            .lightLevel(Pedestal::getLightLevel)
            .setId(key);
    }

    private static int getLightLevel(BlockState state) {
        return state.getValue(FabricFireTrait.FIRE_TYPE).toCore() == FireTypes.LIGHT && state.getValue(LIT)
            ? FireTypes.LIGHT.getLuminance()
            : 0;
    }

    protected Pedestal(Block mainblock, ResourceKey<Block> key, VoxelShape shape, PedestalSize size) {
        super(createProperties(mainblock, key), shape, key);
        this.baseShape = shape;
        this.size = size;
        registerDefaultState(getStateDefinition().any()
            .setValue(WATERLOGGED, false)
            .setValue(FIRE_TYPE, FabricFireTypesAdapter.fromCore(FireTypes.NORMAL))
            .setValue(PREVIOUS_FIRE_TYPE, FabricFireTypesAdapter.fromCore(FireTypes.NORMAL))
            .setValue(ACCEPT_POWER, true)
            .setValue(ISPOWERED, false)
            .setValue(ACTIVATED, false)
            .setValue(LIT, false)
        );

        if (ignitionSound == null) {
            ignitionSound = size == PedestalSize.L
                ? AdditionalLightsFabric.getSound(ModSoundList.Fire_Ignition_L)
                : AdditionalLightsFabric.getSound(ModSoundList.Fire_Ignition_S);
        }
    }

    private static SoundEvent ignitionSound;
    protected abstract BlockSpec getFireBlock(BlockState state);
    public abstract PedestalTypes getType();

    @Override
    public BlockPos offsetUp(BlockPos pos) { return pos.above(); }

    @Override
    public BlockState getBlockState(Level world, BlockPos pos) { return world.getBlockState(pos); }

    @Override
    public boolean setBlockState(Level world, BlockPos pos, BlockState state) {
        return world.setBlockAndUpdate(pos, state);
    }

    @Override
    public void breakBlock(Level world, BlockPos pos) { world.destroyBlock(pos, true); }

    @Override
    public boolean isAir(BlockState state) { return state.getBlock() == Blocks.AIR; }

    @Override
    public boolean isWater(BlockState state) { return state.getBlock() == Blocks.WATER; }

    @Override
    public boolean hasRedstonePower(Level world, BlockPos pos) { return world.hasNeighborSignal(pos); }

    @Override
    public boolean isFireBlock(BlockState state) { return state.getBlock() instanceof FireBase; }

    @Override
    public boolean isFireSummoned(BlockState state) { return state.getValue(FireBase.SUMMONED); }

    @Override
    public BlockState createFireState(BlockState pedestalState) {
        return ModBlocks.get(getFireBlock(pedestalState)).defaultBlockState()
            .setValue(FireBase.SET, true)
            .setValue(FireBase.SUMMONED, true);
    }

    @Override
    public boolean shouldPlaceFire(BlockState state) {
        return state.getValue(FIRE_TYPE).toCore() != FireTypes.LIGHT;
    }

    @Override
    public boolean getAcceptPower(BlockState state) { return state.getValue(ACCEPT_POWER); }

    @Override
    public boolean getIsPowered(BlockState state) { return state.getValue(ISPOWERED); }

    @Override
    public boolean getActivated(BlockState state) { return state.getValue(ACTIVATED); }

    @Override
    public boolean isLightFireType(BlockState state) { return state.getValue(FIRE_TYPE).toCore() == FireTypes.LIGHT; }

    @Override
    public boolean getLit(BlockState state) { return state.getValue(LIT); }

    @Override
    public BlockState setIsPowered(BlockState state, boolean value) { return state.setValue(ISPOWERED, value); }

    @Override
    public BlockState setActivated(BlockState state, boolean value) { return state.setValue(ACTIVATED, value); }

    @Override
    public BlockState setLit(BlockState state, boolean value) { return state.setValue(LIT, value); }

    @Override
    public void onIgnited(Level world, BlockPos pos) {
        playIgnitionSound(world, null, world.getBlockState(pos).getBlock(), pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FIRE_TYPE, PREVIOUS_FIRE_TYPE, ACCEPT_POWER, ISPOWERED, ACTIVATED, LIT);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity filler, BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return resolveActiveShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return resolveActiveShape(state);
    }

    private VoxelShape resolveActiveShape(BlockState state) {
        if (state.getValue(FIRE_TYPE).toCore() != FireTypes.LIGHT) {
            return baseShape;
        }

        if (lightHitboxShape == null) {
            VoxelShape mergedShape = baseShape;
            for (VoxelShape part : LIGHT_SHAPE_PARTS.get(getType())) {
                mergedShape = Shapes.or(mergedShape, part);
            }
            lightHitboxShape = mergedShape;
        }
        return lightHitboxShape;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }


    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!igniteByInteraction(world, pos, state))
            return InteractionResult.PASS;

        playIgnitionSound(world, player, state.getBlock(), pos);
        return InteractionResult.SUCCESS;
    }
    
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof Wand)
            return InteractionResult.PASS;

        if (igniteByInteraction(world, pos, state)) {
            playIgnitionSound(world, player, state.getBlock(), pos);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    private static void playIgnitionSound(Level world, Player player, Block block, BlockPos pos) {
        float volume = block instanceof FirePitBase ? 2.0f : 1.5f;
        world.playSound(player, pos, ignitionSound, SoundSource.BLOCKS, volume, 1.0f);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (placer == null) {
            return;
        }

        if (placer.getOffhandItem().getItem() instanceof FireTypeWandTrait wand) {
            FireTypes target = wand.getTargetFireType();
            boolean allow = target == FireTypes.SOUL || (
                target == FireTypes.LIGHT && (
                    getType() == PedestalTypes.standing_torch_s || getType() == PedestalTypes.standing_torch_l
                    || getType() == PedestalTypes.fire_pit_s
                    || getType() == PedestalTypes.fire_pit_l
                )
            );
            if (allow) {
                state = state.setValue(FIRE_TYPE, FabricFireTypesAdapter.fromCore(target));
            }
        }

        if (placer.isShiftKeyDown()) {
            world.setBlockAndUpdate(pos, withLightLit(state.setValue(ACCEPT_POWER, false), false));
        } else {
            state = withLightLit(state, true);
            world.setBlockAndUpdate(pos, state);
            igniteFire(world, pos, state, false);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
        handleNeighborUpdate(state, world, pos);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
