package com.mgen256.al.client;

import com.mgen256.al.CommonConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = CommonConstants.MOD_ID, dist = Dist.CLIENT)
public final class AdditionalLightsClient {
    public AdditionalLightsClient(ModContainer container) {
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (mc, parent) -> new ConfigurationScreen(
                        container,
                        parent,
                        (context, key, original) -> original == null
                                ? null
                                : new ConfigurationScreen.ConfigurationSectionScreen.Element(
                                        original.name(),
                                        original.tooltip(),
                                        original.widget(),
                                        original.option(),
                                        false)));
    }
}

