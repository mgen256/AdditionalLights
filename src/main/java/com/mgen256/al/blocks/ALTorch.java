package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

import com.mgen256.al.ModBlockList;
import com.mgen256.al.AdditionalLights;
import com.mgen256.al.blocks.IModBlock;

public class ALTorch extends TorchBlock implements IModBlock {

    public static Properties createProps(Block mainblock){
        Properties p = Block.Properties.create(Material.MISCELLANEOUS);
        p.doesNotBlockMovement();
        p.hardnessAndResistance(0.0f);
        p.lightValue(14);
        p.sound(mainblock.getSoundType(null, null, null, null));
        return p;
    }
    
    public ALTorch( Block mainblock, ModBlockList _wallKey ) {
        super(createProps(mainblock));

        name = "al_torch_" + mainblock.getRegistryName().getPath();
        wallKey = _wallKey;
    }
    
    private BlockItem blockItem;
    private ModBlockList wallKey;
    private String name;

    @Override
    public void init() {
        setRegistryName(name);
        blockItem = new WallOrFloorItem(this, AdditionalLights.modBlocks.get(wallKey) , AdditionalLights.ItemProps);
        blockItem.setRegistryName(getRegistryName());
    }

    @Override
    public String getName(){
        return name;
    }
    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double)pos.getX() + 0.5D;
        double d1 = (double)pos.getY() + 0.7D;
        double d2 = (double)pos.getZ() + 0.5D;
        worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        worldIn.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
     }
}