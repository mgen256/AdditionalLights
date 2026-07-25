package com.mgen256.al;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import com.mgen256.al.BlockSpec;
import com.mgen256.al.blocks.*;
import com.mgen256.al.items.PedestalBlockItem;

import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import java.util.Locale;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;

import com.mgen256.al.ModItemFactory;

import com.mgen256.al.ModBlocksCore;
import com.mgen256.al.ModBlocksInitHelper;
import com.mgen256.al.VanillaBlockMap;

public class ModBlocks {
    static final Map<BlockSpec, Function<String, Block>> FACTORIES = new LinkedHashMap<>();
    static final ModBlocksCore<DeferredHolder<Block, Block>, DeferredHolder<Item, ? extends BlockItem>> CORE = new ModBlocksCore<>();

    static {
        initFactories();
    }

    private static void initFactories() {
        ModBlocksInitHelper.initFactories(
                FACTORIES,
                ModBlocks::resolveBlock,
                (base, n) -> new ALLamp(base, n),
                (base, floor) -> (n) -> new ALTorch_Wall(base, floor, n),
                (base, n) -> new ALTorch(base, n),
                (base, n) -> new StandingTorch_S(base, n),
                (base, n) -> new StandingTorch_L(base, n),
                (base, n) -> new FirePit_S(base, n),
                (base, n) -> new FirePit_L(base, n),
                (type, n) -> new Fire(type, n),
                (type, n) -> new Fire_Soul(type, n),
                (type, n) -> new Fire_Light(type, n)
        );
    }

    private static Block resolveBlock(String fieldName) {
        var id = Identifier.fromNamespaceAndPath("minecraft", fieldName.toLowerCase(Locale.ROOT));
        return BuiltInRegistries.BLOCK.get(id).orElseThrow().value();
    }

    public static void registerAll() {
        for (var entry : FACTORIES.entrySet()) {
            AdditionalLightsNeoForge.LOG_PROVIDER.log("register block: " + entry.getKey().regName());
            var key = entry.getKey();

            CORE.register(
                key,
                key.regName(),
                n -> AdditionalLightsNeoForge.BLOCKS.register(n, () -> {
                    Block block = entry.getValue().apply(n);
                    if (block instanceof ModBlockSpec mod) {
                        mod.setMyKey(key);
                    }
                    return block;
                }),
                (k, holder) -> {
                    ModItemFactory.ItemType type = k.getItemType();
                    if (type == null) {
                        return null;
                    }

                    return switch (type) {
                        case VERTICALLY_ATTACHABLE -> {
                            BlockSpec wall = k.getWallTorchKey();
                            var itemProps = new Item.Properties()
                                    .setId(AdditionalLightsNeoForge.createItemResourceKey(k.regName()));
                            yield AdditionalLightsNeoForge.ITEMS.register(
                                    k.regName(),
                                    () -> new StandingAndWallBlockItem(
                                            holder.get(),
                                            CORE.getBlock(wall).get(),
                                            Direction.DOWN,
                                            itemProps
                                    ));
                        }
                        case PEDESTAL -> {
                            var itemProps = new Item.Properties()
                                    .setId(AdditionalLightsNeoForge.createItemResourceKey(k.regName()));
                            yield AdditionalLightsNeoForge.ITEMS.register(
                                    k.regName(),
                                    () -> new PedestalBlockItem(holder.get(), itemProps));
                        }
                        case STANDARD -> {
                            var itemProps = new Item.Properties()
                                    .setId(AdditionalLightsNeoForge.createItemResourceKey(k.regName()));
                            yield AdditionalLightsNeoForge.ITEMS.register(
                                    k.regName(),
                                    () -> new BlockItem(holder.get(), itemProps));
                        }
                    };
                }
            );

            AdditionalLightsNeoForge.CORE.putBlock(key, CORE.getBlock(key));
            var itemHolder = CORE.getBlockItem(key);
            if (itemHolder != null) {
                @SuppressWarnings("unchecked")
                var holder = (DeferredHolder<Item, BlockItem>) (DeferredHolder<?, ?>) itemHolder;
                AdditionalLightsNeoForge.CORE.putBlockItem(key, holder);
            }
        }
    }

    public static void initAll() {
        for (var key : FACTORIES.keySet()) {
            Block block = CORE.getBlock(key).get();
            if (block instanceof ModBlockSpec mod) {
                mod.setMyKey(key);
            }
        }
    }

    public static Block get(BlockSpec key) {
        return CORE.getBlock(key).get();
    }

    public static BlockItem getBlockItem(BlockSpec key) {
        var holder = CORE.getBlockItem(key);
        return holder != null ? (BlockItem) holder.get() : null;
    }
}
