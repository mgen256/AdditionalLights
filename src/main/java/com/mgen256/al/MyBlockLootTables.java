package com.mgen256.al;

import java.util.concurrent.CompletableFuture;

import com.mgen256.al.blocks.FireBase;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryWrapper;

public class MyBlockLootTables extends FabricBlockLootTableProvider {
    public MyBlockLootTables(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
   }

   @Override
   public void generate() {
        for (var block : ModBlockList.values()) {
            if( block.getBlockItem() != null)
            {
                if (block.get() instanceof FireBase) 
                    addFireBaseDrop(block);
                else
                    addDrop(block.get(), drops(block.getBlockItem()));
            }
            else
                {
                var floorblock = ModBlockList.valueOf( block.name().replace("ALTorch_Wall", "ALTorch") );
                addDrop(block.get(), drops(floorblock.getBlockItem()));
                }
        }
   }

    private void addFireBaseDrop(ModBlockList block) {
        var builder = LootTable.builder()
            .pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(SurvivesExplosionLootCondition.builder())
                .conditionally(BlockStatePropertyLootCondition.builder(block.get())
                    .properties(StatePredicate.Builder.create()
                        .exactMatch(FireBase.SUMMONED, false)))
                .with(ItemEntry.builder(block.getBlockItem())));

        this.addDrop(block.get(), builder);
    }

}
 
