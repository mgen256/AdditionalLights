package com.mgen256.al;

import com.mgen256.al.blocks.FireBase;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public final class MyBlockLootTables extends FabricBlockLootSubProvider {
    public MyBlockLootTables(
            final FabricPackOutput packOutput,
            final CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, registryLookup);
    }

    @Override
    public void generate() {
        for (final BlockSpec key : ModBlockList.values()) {
            final var item = ModBlocks.getBlockItem(key);
            if (item != null) {
                final var block = ModBlocks.get(key);
                if (block instanceof FireBase) {
                    addFireBaseDrop(key);
                } else {
                    add(block, createSingleItemTable(item));
                }
            } else if (key instanceof WallTorchSpec wallTorchSpec) {
                final BlockSpec floorKey = wallTorchSpec.getFloorTorchKey();
                add(ModBlocks.get(key), createSingleItemTable(ModBlocks.getBlockItem(floorKey)));
            } else if (key instanceof LightFireForSpec) {
                add(ModBlocks.get(key), LootTable.lootTable());
            }
        }
    }

    private void addFireBaseDrop(final BlockSpec key) {
        final var builder =
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1))
                                        .when(ExplosionCondition.survivesExplosion())
                                        .when(
                                                LootItemBlockStatePropertyCondition
                                                        .hasBlockStateProperties(ModBlocks.get(key))
                                                        .setProperties(
                                                                StatePropertiesPredicate.Builder
                                                                        .properties()
                                                                        .hasProperty(
                                                                                FireBase.SUMMONED,
                                                                                false)))
                                        .add(LootItem.lootTableItem(ModBlocks.getBlockItem(key))));

        this.add(ModBlocks.get(key), builder);
    }
}
