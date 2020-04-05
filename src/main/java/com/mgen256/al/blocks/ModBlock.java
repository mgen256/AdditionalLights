package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import com.mgen256.al.AdditionalLights;
import com.mgen256.al.blocks.IModBlock;

public abstract class ModBlock extends Block implements IModBlock {

    public ModBlock(String basename, Block mainblock, Properties props, VoxelShape shape) {
        super(props);
        name = basename + mainblock.getRegistryName().getPath();
        voxelShape = shape;
    }

    protected BlockItem blockItem;
    protected String name;
    private VoxelShape voxelShape;

    @Override
    public void init() {
        setRegistryName(name);
        blockItem = new BlockItem(this, AdditionalLights.ItemProps);
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
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return voxelShape;
    }

    @Override
    public BlockRenderType getRenderType(final BlockState state) {
        return BlockRenderType.MODEL;
    }

    protected static Properties createBasicProps(Block mainblock ){
        Properties p = Block.Properties.create(mainblock.getMaterial(null));
        p.harvestTool(mainblock.getHarvestTool(null));
        p.harvestLevel(mainblock.getHarvestLevel(null));
        p.hardnessAndResistance(mainblock.getBlockHardness(null, null, null), mainblock.getExplosionResistance() );
        p.sound(mainblock.getSoundType(null, null, null, null));
        return p;
    }

    
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        ResourceLocation res = getRegistryName();
        if ( ForgeRegistries.ITEMS.containsKey(res) == false )
            return super.getDrops(state, builder);

        List<ItemStack> list = new ArrayList<>();
        list.add( new ItemStack(ForgeRegistries.ITEMS.getValue(res)) );

        return list;
    }
}