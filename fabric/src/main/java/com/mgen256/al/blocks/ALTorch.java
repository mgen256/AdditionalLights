package com.mgen256.al.blocks;
import com.mgen256.al.BlockSpec;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.mgen256.al.FireTypes;
import com.mgen256.al.FabricFireTypesAdapter;
import com.mgen256.al.blocks.FabricFireTrait;
import com.mgen256.al.blocks.ALTorchSpec;
import com.mgen256.al.blocks.ALTorchCore;
import com.mgen256.al.items.FireTypeWandTrait;

public class ALTorch extends TorchBlock
    implements ModBlockSpec, FabricFireTrait, ALTorchSpec {

    private static final VoxelShape SHAPE = Block.box(
        SHAPE_BOX[0], SHAPE_BOX[1], SHAPE_BOX[2], SHAPE_BOX[3], SHAPE_BOX[4], SHAPE_BOX[5]
    );

    private final ALTorchCore<VoxelShape> core;

    private BlockSpec myKey;
    private final ResourceKey<Block> regKey;

    
    public static Properties createSettings(Block mainblock, ResourceKey<Block> key) {
        return Properties.of()
            .sound(mainblock.defaultBlockState().getSoundType())
            .noCollision()
            .instabreak()
            .lightLevel(value -> value.getValue(FIRE_TYPE).toCore().getLuminance())
            .setId(key);
    }

    
    public ALTorch(Block mainblock, ResourceKey<Block> key) {
        super(ParticleTypes.FLAME, createSettings(mainblock, key));
        this.regKey = key;
        this.core = new ALTorchCore<>(key.identifier().getPath(), SHAPE);
        registerDefaultState(getStateDefinition().any()
            .setValue(FIRE_TYPE, FabricFireTypesAdapter.fromCore(FireTypes.NORMAL))
            .setValue(PREVIOUS_FIRE_TYPE, FabricFireTypesAdapter.fromCore(FireTypes.NORMAL)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FIRE_TYPE, PREVIOUS_FIRE_TYPE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return core.getShape();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
        var fireType = state.getValue(FIRE_TYPE).toCore();
        if (fireType == FireTypes.LIGHT) {
            return;
        }

        var d0 = pos.getX() + 0.5D;
        var d1 = pos.getY() + 0.7D;
        var d2 = pos.getZ() + 0.5D;
        world.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);

        var particleOption = fireType == FireTypes.SOUL ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;

        world.addParticle(particleOption, d0, d1, d2, 0.0D, 0.0D, 0.0D);
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
