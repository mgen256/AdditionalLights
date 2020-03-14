
package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import java.util.Random;


public abstract class FireBlock extends ModBlock  {


    static {
        final Properties p = Block.Properties.create(Material.MISCELLANEOUS);
        p.hardnessAndResistance(0.0f);
        p.lightValue(15);
        p.sound(SoundType.STONE);
        props = p;
    }

    public static final Properties props;

    public FireBlock( String basename, Block mainblock, Properties props, VoxelShape shape, BasicParticleType _particleType ) {
        super(basename, mainblock, props, shape);
        particleType = _particleType;
    }

    protected BasicParticleType particleType;

    protected abstract float getFireDamageAmount();
    protected abstract double getSmokePos_Y();

    @Override
    public void animateTick(final BlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        final double d0 = (double) pos.getX() + 0.5D;
        final double d1 = (double) pos.getY() + getSmokePos_Y();
        final double d2 = (double) pos.getZ() + 0.5D;
        worldIn.addParticle(particleType, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.isImmuneToFire() && entityIn instanceof LivingEntity
                && !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn)) {
            entityIn.attackEntityFrom(DamageSource.IN_FIRE, getFireDamageAmount());
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }
}