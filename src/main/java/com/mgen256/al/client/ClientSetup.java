package com.mgen256.al.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import com.mgen256.al.AdditionalLights;
import com.mgen256.al.ModBlockList;

@Mod.EventBusSubscriber(modid = AdditionalLights.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
     @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // ここで各ブロックにレンダータイプを設定
            for (ModBlockList block : ModBlockList.values()) {
                if (needCutout(block.getRegName())) {
                    ItemBlockRenderTypes.setRenderLayer(
                        AdditionalLights.getBlock(block), 
                        RenderType.cutout()
                    );
                }
            }
        });
    }

    private static boolean needCutout( String name)
    {
        if( name.contains("fire_for_") || name.contains("glass"))
            return true;
        return false;   
    }
}