package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;


import net.minecraft.item.BlockItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;


import com.mgen256.al.blocks.IModBlock;

public class ALTorch_Wall extends WallTorchBlock implements IModBlock {

    public ALTorch_Wall(Block mainblock ) {
        super(ALTorch.createProps(mainblock));
        name = "al_wall_torch_" + mainblock.getRegistryName().getPath();
    }

    private String name;

    @Override
    public void init() {
        setRegistryName(name);
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public BlockItem getBlockItem() {
        return null;
    }

    @Override
    public boolean notRequireItemRegistration(){
        return true;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        Direction direction = stateIn.get(HORIZONTAL_FACING);
        double dx = (double)pos.getX() + 0.5D;
        double dy = (double)pos.getY() + 0.9D;
        double dz = (double)pos.getZ() + 0.5D;
  
        Direction direction1 = direction.getOpposite();
        double d3 = 0.38D;
        worldIn.addParticle(ParticleTypes.SMOKE, dx + d3 * (double)direction1.getXOffset(), dy, dz + d3 * (double)direction1.getZOffset(), 0.0D, 0.0D, 0.0D);
        worldIn.addParticle(ParticleTypes.FLAME, dx + d3 * (double)direction1.getXOffset(), dy, dz + d3 * (double)direction1.getZOffset(), 0.0D, 0.0D, 0.0D);
       }



}