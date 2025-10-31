package com.mgen256.al;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class AdditionalLights implements ModInitializer {
	public static final String MOD_ID = "additional_lights";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
		.icon(() -> new ItemStack( ModBlockList.ALTorch_Acacia.get()) )
		.displayName(Text.translatable("Additional Lights"))
		.entries( (context, entries) -> { 
			entries.add( ModItemList.SoulWand.get() );
			
			for (var block : ModBlockList.values()) {
				var item = block.getBlockItem();
				if( item != null )
					entries.add(item);
			}
		 	})
		.build();

	@Override
	public void onInitialize() {
		for (var sound: ModSoundList.values())
			sound.register();

		for (var block : ModBlockList.values()) 
            block.register();
		
		for (var item : ModItemList.values())
			item.Register();

		Registry.register(Registries.ITEM_GROUP, Identifier.of(MOD_ID, "al"), ITEM_GROUP);
	}

	static public RegistryKey<Block> createRegistryKey(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(AdditionalLights.MOD_ID, name));
    }


	static public RegistryKey<Item> createItemRegistryKey(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AdditionalLights.MOD_ID, name));
    }
}
