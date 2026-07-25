package com.mgen256.al.blocks;

import java.util.Map;

import com.mgen256.al.FireTypes;
import com.mgen256.al.PedestalTypes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Fire_Light extends FireBase {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private static final Map<PedestalTypes, VoxelShape> LIGHT_SHAPES =
            LightFireShapeSpec.createShapeMap(coordinates -> Block.box(
                    coordinates[0], coordinates[1], coordinates[2],
                    coordinates[3], coordinates[4], coordinates[5]));

    public Fire_Light(PedestalTypes pedestalKey, ResourceKey<Block> key) {
        super(pedestalKey, Properties.of()
                .noCollision()
                .instabreak()
                .mapColor(MapColor.SNOW)
                .sound(SoundType.WOOL)
                .lightLevel(state -> FireTypes.LIGHT.getLuminance(state.getValue(LIT)))
                .setId(key),
                key);
        registerDefaultState(defaultBlockState().setValue(LIT, true));
    }

    @Override
    protected void createBlockStateDefinition(
            final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }

    private boolean shouldDelegateToPedestal(
            final BlockState lightState,
            final BlockState pedestalState) {
        return pedestalState.getBlock() instanceof Pedestal pedestal
                && LightFireBlockBreakSpec.shouldDelegate(
                        lightState.getValue(SUMMONED),
                        getPedestalKey(),
                        pedestal.getType(),
                        pedestal.isLightFireType(pedestalState));
    }

    public BlockPos resolveBreakTarget(final BlockGetter world, final BlockPos lightPos) {
        final BlockState lightState = world.getBlockState(lightPos);
        if (lightState.getBlock() != this) {
            return lightPos;
        }

        final BlockPos pedestalPos = lightPos.below();
        final BlockState pedestalState = world.getBlockState(pedestalPos);
        return shouldDelegateToPedestal(lightState, pedestalState) ? pedestalPos : lightPos;
    }

    @Override
    protected float getDestroyProgress(
            BlockState state,
            Player player,
            BlockGetter world,
            BlockPos pos) {
        final BlockPos pedestalPos = pos.below();
        final BlockState pedestalState = world.getBlockState(pedestalPos);
        if (shouldDelegateToPedestal(state, pedestalState)) {
            return pedestalState.getDestroyProgress(player, world, pedestalPos);
        }
        return super.getDestroyProgress(state, player, world, pos);
    }

    @Override
    protected SoundType getSoundType(final BlockState state) {
        return LightFireBlockBreakSpec.shouldUseSilentFallbackSound(
                        state.getValue(SUMMONED))
                ? SoundType.EMPTY
                : super.getSoundType(state);
    }

    @Override
    public VoxelShape getShape(
            BlockState state,
            BlockGetter world,
            BlockPos pos,
            CollisionContext context) {
        return LIGHT_SHAPES.get(getPedestalKey());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
    }
}
