package com.mgen256.al.client.gui;

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

public final class CreativeTabToggleStateCore {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConstants.MOD_ID);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = CommonConstants.MOD_ID + "_client.json";
    private static final CreativeTabToggleStateSpec DEFAULT_STATE =
            new CreativeTabToggleStateSpec(false, false);

    private static boolean loaded = false;
    private static CreativeTabToggleStateSpec state = DEFAULT_STATE;

    private CreativeTabToggleStateCore() {}

    public static boolean isReverseOrderEnabled() {
        ensureLoaded();
        return state.reverseOrderEnabled();
    }

    public static boolean isColorSortEnabled() {
        ensureLoaded();
        return state.colorSortEnabled();
    }

    public static void setReverseOrderEnabled(final boolean enabled) {
        ensureLoaded();
        if (state.reverseOrderEnabled() == enabled) {
            return;
        }

        state = new CreativeTabToggleStateSpec(enabled, state.colorSortEnabled());
        save();
    }

    public static void setColorSortEnabled(final boolean enabled) {
        ensureLoaded();
        if (state.colorSortEnabled() == enabled) {
            return;
        }

        state = new CreativeTabToggleStateSpec(state.reverseOrderEnabled(), enabled);
        save();
    }

    static CreativeTabToggleStateSpec fromJson(final String json) {
        final CreativeTabToggleStateSpec parsed = GSON.fromJson(json, CreativeTabToggleStateSpec.class);
        if (parsed == null) {
            return DEFAULT_STATE;
        }

        return new CreativeTabToggleStateSpec(parsed.reverseOrderEnabled(), parsed.colorSortEnabled());
    }

    static String toJson(final CreativeTabToggleStateSpec stateSpec) {
        return GSON.toJson(stateSpec);
    }

    static record CreativeTabToggleStateSpec(boolean reverseOrderEnabled, boolean colorSortEnabled) {}

    private static void ensureLoaded() {
        if (!loaded) {
            load();
        }
    }

    private static void load() {
        loaded = true;
        state = DEFAULT_STATE;

        final Path path = getPath();
        if (path == null || !Files.exists(path)) {
            return;
        }

        try {
            state = fromJson(Files.readString(path, StandardCharsets.UTF_8));
        } catch (JsonParseException e) {
            LOGGER.warn("Failed to parse client toggle state file: {}", path, e);
        } catch (IOException e) {
            LOGGER.warn("Failed to read client toggle state file: {}", path, e);
        }
    }

    private static void save() {
        final Path path = getPath();
        if (path == null) {
            return;
        }

        try {
            Files.createDirectories(path.getParent());
            final String json = toJson(state);
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
            LOGGER.warn("Failed to write client toggle state file: {}", path, e);
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
