package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootContext;

import java.util.ArrayList;
import java.util.List;

import com.mgen256.al.AdditionalLights;
import com.mgen256.al.blocks.IModBlock;

public abstract class ModBlock extends Block implements IModBlock {

//public ModBlock(String basename, Block mainblock, Material material, AxisAlignedBB shape) {

    public ModBlock(String basename, Block mainblock) {
        super(Material.GROUND);
        
        name = mainblock == null ? basename : basename + mainblock.getRegistryName().getPath();
        blockRenderLayer = name.contains("glass") ? BlockRenderLayer.CUTOUT : BlockRenderLayer.SOLID;

        setTranslationKey(AdditionalLights.MOD_ID + "." + name );
        setCreativeTab(CreativeTabs.MATERIALS);
        setRegistryName(name);
        createItem();

        //voxelShape = shape;
    }
/*
    protected BlockItem blockItem;
    
    @Override
    public void init() {
        setRegistryName(name);
        blockItem = new BlockItem(this, AdditionalLights.ItemProps);
        blockItem.setRegistryName(getRegistryName());
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
*/
    protected String name;
    private AxisAlignedBB voxelShape;
    private BlockRenderLayer blockRenderLayer;
    private Item item;

    @Override
    public void init() {
        //setRegistryName(name);
        //CreateItem();
    }

    private void createItem(){
        item = new ItemBlock(this).setRegistryName(name);
    }

    @Override
    public String getName(){
        return name;
    }
    
    @Override
    public Item getItem() {
        return item;
    }

/*
    @Override
    public BlockRenderLayer getRenderLayer() {
        return blockRenderLayer;
    }
    */
}