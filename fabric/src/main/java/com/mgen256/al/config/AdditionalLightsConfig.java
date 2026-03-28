package com.mgen256.al.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mgen256.al.CommonConstants;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public final class AdditionalLightsConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConstants.MOD_ID);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = CommonConstants.MOD_ID + ".json";

    private static boolean loaded = false;
    private static AdditionalLightsConfig instance = new AdditionalLightsConfig();

    public boolean enableFireCrafting = false;

    public static AdditionalLightsConfig get() {
        if (!loaded) {
            load();
        }
        return instance;
    }

    public static Path getPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
    }

    public static void load() {
        loaded = true;

        var path = getPath();
        if (!Files.exists(path)) {
            save();
            return;
        }

        try {
            var json = Files.readString(path, StandardCharsets.UTF_8);
            var loadedConfig = GSON.fromJson(json, AdditionalLightsConfig.class);
            if (loadedConfig != null) {
                instance = loadedConfig;
            }
        } catch (JsonParseException e) {
            LOGGER.warn("Failed to parse config file: {}", path, e);
        } catch (IOException e) {
            LOGGER.warn("Failed to read config file: {}", path, e);
        }
    }

    public static void save() {
        var path = getPath();
        try {
            Files.createDirectories(path.getParent());
            var json = GSON.toJson(instance);
            var tmpPath = path.resolveSibling(path.getFileName() + ".tmp");

            Files.writeString(
                    tmpPath,
                    json,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            try {
                Files.move(tmpPath, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                Files.move(tmpPath, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            LOGGER.warn("Failed to write config file: {}", path, e);
        }
    }

    private AdditionalLightsConfig() {}
}

