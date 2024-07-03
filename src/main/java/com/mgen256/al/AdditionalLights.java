package com.mgen256.al;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class AdditionalLights implements ModInitializer {
	public static final String MOD_ID = "additional_lights";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
		.icon(() -> new ItemStack( ModBlockList.ALTorch_Acacia.get()) )
		.displayName(Text.translatable("Additional Lights"))
		.entries( (context, entries) -> { 
			entries.add( ModItemList.SoulWand.getItem() );
			
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
		
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
			setupBlockRenderLayers();
	}

	@Environment(EnvType.CLIENT)
	private void setupBlockRenderLayers() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlockList.Fire_For_FirePit_S.get(), RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlockList.Fire_For_FirePit_L.get(), RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlockList.Fire_For_StandingTorch_S.get(), RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlockList.Fire_For_StandingTorch_L.get(), RenderLayer.getCutout());
	
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlockList.SoulFire_For_FirePit_S.get(), RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlockList.SoulFire_For_FirePit_L.get(), RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlockList.SoulFire_For_StandingTorch_S.get(), RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlockList.SoulFire_For_StandingTorch_L.get(), RenderLayer.getCutout());
	}

}