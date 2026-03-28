package com.mgen256.al.compat.modmenu;

import com.mgen256.al.client.gui.AdditionalLightsConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public final class AdditionalLightsModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return AdditionalLightsConfigScreen::new;
    }
}
