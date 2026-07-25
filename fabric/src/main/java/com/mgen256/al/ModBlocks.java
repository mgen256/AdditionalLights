package com.mgen256.al;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import com.mgen256.al.blocks.*;
import com.mgen256.al.items.PedestalBlockItem;
import com.mgen256.al.BlockSpec;
import com.mgen256.al.ModBlocksCore;
import com.mgen256.al.ModBlocksInitHelper;
import com.mgen256.al.PedestalTypes;
import com.mgen256.al.ModItemFactory;
import java.util.Locale;

public class ModBlocks {
    private static final Map<BlockSpec, Function<ResourceKey<Block>, Block>> FACTORIES = new LinkedHashMap<>();
    private static final ModBlocksCore<Block, BlockItem> CORE = new ModBlocksCore<>();

    static {
        initFactories();
    }

    private static void initFactories() {
        ModBlocksInitHelper.initFactories(
                FACTORIES,
                ModBlocks::resolveBlock,
                (base, k) -> new ALLamp(base, k),
                (base, floor) -> (k) -> new ALTorch_Wall(base, k),
                (base, k) -> new ALTorch(base, k),
                (base, k) -> new StandingTorch_S(base, k),
                (base, k) -> new StandingTorch_L(base, k),
                (base, k) -> new FirePit_S(base, k),
                (base, k) -> new FirePit_L(base, k),
                (type, k) -> new Fire(type, k),
                (type, k) -> new Fire_Soul(type, k),
                (type, k) -> new Fire_Light(type, k)
        );
    }

    private static Block resolveBlock(String fieldName) {
        var id = Identifier.fromNamespaceAndPath("minecraft", fieldName.toLowerCase(Locale.ROOT));
        return BuiltInRegistries.BLOCK.getValue(id);
    }

    public static Block get(BlockSpec key) {
        return CORE.getBlock(key);
    }

    public static BlockItem getBlockItem(BlockSpec key) {
        return CORE.getBlockItem(key);
    }

    public static void registerAll() {
        for (var entry : FACTORIES.entrySet()) {
            AdditionalLightsFabric.LOG_PROVIDER.log("register block: " + entry.getKey().regName());
            var key = entry.getKey();
            var id = Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, key.regName());
            var regKey = ResourceKey.create(Registries.BLOCK, id);

            CORE.register(
                key,
                regKey,
                rk -> entry.getValue().apply(rk),
                (k, block) -> {
                    ModItemFactory.ItemType type = k.getItemType();
                    if (type == null) {
                        return null;
                    }

                    ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
                    Item.Properties settings = new Item.Properties()
                            .useBlockDescriptionPrefix()
                            .setId(itemKey);

                    return switch (type) {
                        case VERTICALLY_ATTACHABLE -> {
                            BlockSpec wall = k.getWallTorchKey();
                            yield new StandingAndWallBlockItem(block, CORE.getBlock(wall), Direction.DOWN, settings);
                        }
                        case PEDESTAL -> new PedestalBlockItem(block, settings);
                        case STANDARD -> new BlockItem(block, settings);
                    };
                }
            );

            Registry.register(BuiltInRegistries.BLOCK, regKey, CORE.getBlock(key));
            BlockItem item = CORE.getBlockItem(key);
            if (item != null) {
                Registry.register(BuiltInRegistries.ITEM, id, item);
            }
        }
    }
}
