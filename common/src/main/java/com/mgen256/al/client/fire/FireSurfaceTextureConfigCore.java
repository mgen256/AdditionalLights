package com.mgen256.al.client.fire;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mgen256.al.CommonConstants;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FireSurfaceTextureConfigCore {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConstants.MOD_ID);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = CommonConstants.MOD_ID + "_fire_textures.json";
    private static final FireSurfaceTextureModeSpec DEFAULT_MODE = FireSurfaceTextureModeSpec.DYNAMIC;

    private FireSurfaceTextureConfigCore() {}

    public static FireSurfaceTextureModeSpec readMode() {
        final Path path = getPath();
        if (path == null) {
            return DEFAULT_MODE;
        }

        if (!Files.exists(path)) {
            writeDefault(path);
            return DEFAULT_MODE;
        }

        try {
            return fromJson(Files.readString(path, StandardCharsets.UTF_8)).mode();
        } catch (JsonParseException e) {
            LOGGER.warn("Failed to parse fire texture config file: {}", path, e);
        } catch (IOException e) {
            LOGGER.warn("Failed to read fire texture config file: {}", path, e);
        }

        return DEFAULT_MODE;
    }

    static FireSurfaceTextureConfigSpec fromJson(final String json) {
        final SerializedFireSurfaceTextureConfigSpec parsed =
                GSON.fromJson(json, SerializedFireSurfaceTextureConfigSpec.class);
        if (parsed == null) {
            return defaultSpec();
        }

        return new FireSurfaceTextureConfigSpec(FireSurfaceTextureModeSpec.parse(parsed.surfaceFireTextureMode()));
    }

    static String toJson(final FireSurfaceTextureConfigSpec spec) {
        return GSON.toJson(new SerializedFireSurfaceTextureConfigSpec(spec.mode().serializedName()));
    }

    static record SerializedFireSurfaceTextureConfigSpec(String surfaceFireTextureMode) {}

    static record FireSurfaceTextureConfigSpec(FireSurfaceTextureModeSpec mode) {}

    private static FireSurfaceTextureConfigSpec defaultSpec() {
        return new FireSurfaceTextureConfigSpec(DEFAULT_MODE);
    }

    private static void writeDefault(final Path path) {
        writeSpec(path, defaultSpec());
    }

    private static void writeSpec(final Path path, final FireSurfaceTextureConfigSpec spec) {
        try {
            Files.createDirectories(path.getParent());
            final String json = toJson(spec);
            final Path tmpPath = path.resolveSibling(path.getFileName() + ".tmp");

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
            LOGGER.warn("Failed to write fire texture config file: {}", path, e);
        }
    }

    private static Path getPath() {
        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.gameDirectory == null) {
            return null;
        }

        return minecraft.gameDirectory.toPath().resolve("config").resolve(FILE_NAME);
    }
}
