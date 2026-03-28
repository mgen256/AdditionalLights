package com.mgen256.al.client.color;

import com.mgen256.al.CommonConstants;
import com.mgen256.al.ModBlockList;
import com.mgen256.al.ModItemList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.ItemStack;

public final class ItemTextureColorCacheCore {
    public static final int UNKNOWN_RGB = -1;

    public static final ItemTextureColorSpec UNKNOWN_COLOR_SPEC =
            new ItemTextureColorSpec(UNKNOWN_RGB, UNKNOWN_RGB);

    public static final Identifier RELOAD_LISTENER_ID =
            Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "item_texture_color_cache");

    private static final int MAX_SAMPLE_AXIS = 32;
    private static final int MIN_ALPHA = 16;
    private static final int BIN_COUNT = 16 * 16 * 16;

    private static volatile Map<Identifier, ItemTextureColorSpec> itemColorById = Map.of();

    private ItemTextureColorCacheCore() {}

    public static ItemTextureColorSpec getColorSpec(final ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return UNKNOWN_COLOR_SPEC;
        }

        Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (id == null) {
            return UNKNOWN_COLOR_SPEC;
        }

        ItemTextureColorSpec colorSpec = itemColorById.get(id);
        return colorSpec != null ? colorSpec : UNKNOWN_COLOR_SPEC;
    }

    static void apply(final Map<Identifier, ItemTextureColorSpec> newItemColorById) {
        itemColorById = Map.copyOf(newItemColorById);
    }

    static Map<Identifier, ItemTextureColorSpec> build(final ResourceManager resourceManager) {
        var itemIds = collectAdditionalLightsItemIds();
        var itemTextureFiles = new LinkedHashMap<Identifier, Identifier>();
        var uniqueTextureFiles = new LinkedHashSet<Identifier>();

        for (Identifier itemId : itemIds) {
            Identifier textureFile = resolveRepresentativeTextureFile(resourceManager, itemId);
            if (textureFile == null) {
                continue;
            }

            itemTextureFiles.put(itemId, textureFile);
            uniqueTextureFiles.add(textureFile);
        }

        var textureColorByFile = new HashMap<Identifier, ItemTextureColorSpec>();
        for (Identifier textureFile : uniqueTextureFiles) {
            ItemTextureColorSpec colorSpec = computeTextureColorSpec(resourceManager, textureFile);
            if (colorSpec != null) {
                textureColorByFile.put(textureFile, colorSpec);
            }
        }

        var itemColors = new HashMap<Identifier, ItemTextureColorSpec>();
        for (var entry : itemTextureFiles.entrySet()) {
            ItemTextureColorSpec colorSpec = textureColorByFile.get(entry.getValue());
            if (colorSpec != null) {
                itemColors.put(entry.getKey(), colorSpec);
            }
        }

        return itemColors;
    }

    private static ArrayList<Identifier> collectAdditionalLightsItemIds() {
        var itemIds = new ArrayList<Identifier>();
        for (var itemKey : ModItemList.values()) {
            itemIds.add(Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, itemKey.getRegName()));
        }
        for (var blockKey : ModBlockList.values()) {
            itemIds.add(Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, blockKey.regName()));
        }
        return itemIds;
    }

    private static Identifier resolveRepresentativeTextureFile(final ResourceManager resourceManager, final Identifier itemId) {
        if (itemId == null || !CommonConstants.MOD_ID.equals(itemId.getNamespace())) {
            return null;
        }

        Identifier resolvedFromModel = resolveTextureFileFromItemDefinition(resourceManager, itemId);
        if (resolvedFromModel != null) {
            return resolvedFromModel;
        }

        Identifier resolvedFromBlockModel = resolveTextureFileFromBlockModel(resourceManager, itemId);
        if (resolvedFromBlockModel != null) {
            return resolvedFromBlockModel;
        }

        String path = itemId.getPath();
        String base = stripBaseBlockSuffix(path);
        if (base == null) {
            return null;
        }
        return Identifier.fromNamespaceAndPath("minecraft", "textures/block/" + base + ".png");
    }

    private static Identifier resolveTextureFileFromBlockModel(final ResourceManager resourceManager, final Identifier itemId) {
        if (resourceManager == null || itemId == null) {
            return null;
        }

        String[] candidateFolders = {
            "al_lamp",
            "al_torch",
            "standing_torch_s",
            "standing_torch_l",
            "fire_pit_s",
            "fire_pit_l",
            "fire"
        };

        for (String folder : candidateFolders) {
            Identifier modelFile =
                    Identifier.fromNamespaceAndPath(itemId.getNamespace(), "models/block/" + folder + "/" + itemId.getPath() + ".json");
            String textureRef = resolveTextureReferenceFromModelChain(resourceManager, modelFile);
            if (textureRef == null) {
                continue;
            }

            Identifier textureId = parseIdentifierOrMinecraft(textureRef);
            Identifier textureFile = toTextureFile(textureId);
            if (textureFile != null) {
                return textureFile;
            }
        }

        return null;
    }

    private static Identifier resolveTextureFileFromItemDefinition(
            final ResourceManager resourceManager, final Identifier itemId) {
        if (resourceManager == null || itemId == null) {
            return null;
        }

        Identifier itemDefFile =
                Identifier.fromNamespaceAndPath(itemId.getNamespace(), "items/" + itemId.getPath() + ".json");
        Identifier modelId = tryReadModelIdFromItemDefinition(resourceManager, itemDefFile);
        if (modelId == null) {
            return null;
        }

        Identifier modelFile =
                Identifier.fromNamespaceAndPath(modelId.getNamespace(), "models/" + modelId.getPath() + ".json");
        String textureRef = resolveTextureReferenceFromModelChain(resourceManager, modelFile);
        if (textureRef == null) {
            return null;
        }

        Identifier textureId = parseIdentifierOrMinecraft(textureRef);
        return toTextureFile(textureId);
    }

    private static Identifier tryReadModelIdFromItemDefinition(
            final ResourceManager resourceManager, final Identifier itemDefFile) {
        Optional<Resource> resourceOptional = resourceManager.getResource(itemDefFile);
        if (resourceOptional.isEmpty()) {
            return null;
        }

        try (InputStream stream = resourceOptional.get().open()) {
            JsonObject root =
                    JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            JsonObject model = root.getAsJsonObject("model");
            if (model == null) {
                return null;
            }

            JsonElement modelId = model.get("model");
            if (modelId == null) {
                return null;
            }

            return parseIdentifierOrMinecraft(modelId.getAsString());
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String resolveTextureReferenceFromModelChain(
            final ResourceManager resourceManager, final Identifier modelFile) {
        var textures = new HashMap<String, String>();
        Identifier current = modelFile;

        for (int depth = 0; depth < 16; depth++) {
            Optional<Resource> resourceOptional = resourceManager.getResource(current);
            if (resourceOptional.isEmpty()) {
                break;
            }

            JsonObject root;
            try (InputStream stream = resourceOptional.get().open()) {
                root = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            } catch (Exception ignored) {
                break;
            }

            JsonObject texturesObject = root.getAsJsonObject("textures");
            if (texturesObject != null) {
                for (var entry : texturesObject.entrySet()) {
                    textures.putIfAbsent(entry.getKey(), entry.getValue().getAsString());
                }
            }

            if (!root.has("parent")) {
                break;
            }

            String parentIdString = root.get("parent").getAsString();
            Identifier parentId = parseIdentifierOrMinecraft(parentIdString);
            current = Identifier.fromNamespaceAndPath(parentId.getNamespace(), "models/" + parentId.getPath() + ".json");
        }

        String[] preferredKeys = {"main", "particle", "texture", "texture1", "layer0", "fire"};
        for (String key : preferredKeys) {
            String resolved = resolveTextureReference(textures, key);
            if (resolved != null) {
                return resolved;
            }
        }

        return null;
    }

    private static String resolveTextureReference(final Map<String, String> textures, final String key) {
        if (textures == null || key == null) {
            return null;
        }

        String value = textures.get(key);
        for (int i = 0; i < 16 && value != null && value.startsWith("#"); i++) {
            value = textures.get(value.substring(1));
        }

        if (value == null || value.startsWith("#")) {
            return null;
        }

        return value;
    }

    private static Identifier parseIdentifierOrMinecraft(final String raw) {
        if (raw == null || raw.isEmpty()) {
            return null;
        }

        int index = raw.indexOf(':');
        if (index >= 0) {
            return Identifier.fromNamespaceAndPath(raw.substring(0, index), raw.substring(index + 1));
        }
        return Identifier.fromNamespaceAndPath("minecraft", raw);
    }

    private static Identifier toTextureFile(final Identifier textureId) {
        if (textureId == null) {
            return null;
        }

        String path = textureId.getPath();
        if (path.startsWith("textures/")) {
            return Identifier.fromNamespaceAndPath(textureId.getNamespace(), path);
        }

        String filePath = "textures/" + path;
        if (!filePath.endsWith(".png")) {
            filePath = filePath + ".png";
        }

        return Identifier.fromNamespaceAndPath(textureId.getNamespace(), filePath);
    }

    private static String stripBaseBlockSuffix(final String path) {
        String[] prefixes = {
            "al_lamp_",
            "al_wall_lamp_",
            "al_torch_",
            "al_wall_torch_",
            "standing_torch_s_",
            "standing_torch_l_",
            "fire_pit_s_",
            "fire_pit_l_"
        };
        for (String prefix : prefixes) {
            if (path.startsWith(prefix)) {
                return path.substring(prefix.length());
            }
        }
        return null;
    }

    private static ItemTextureColorSpec computeTextureColorSpec(
            final ResourceManager resourceManager, final Identifier textureFile) {
        try {
            var optional = resourceManager.getResource(textureFile);
            if (optional.isEmpty()) {
                return null;
            }

            try (InputStream stream = optional.get().open(); NativeImage image = NativeImage.read(stream)) {
                Integer dominantRgb = computeDominantRgb(image);
                Integer averageRgb = computeAverageRgb(image);
                if (dominantRgb == null || averageRgb == null) {
                    return null;
                }

                return new ItemTextureColorSpec(dominantRgb, averageRgb);
            }
        } catch (IOException ignored) {
            return null;
        }
    }

    private static Integer computeDominantRgb(final NativeImage image) {
        if (image == null) {
            return null;
        }

        int width = image.getWidth();
        int height = image.getHeight();
        if (width <= 0 || height <= 0) {
            return null;
        }

        int stepX = Math.max(1, width / MAX_SAMPLE_AXIS);
        int stepY = Math.max(1, height / MAX_SAMPLE_AXIS);

        int[] pixelsAbgr = image.getPixelsABGR();

        var weightByBin = new int[BIN_COUNT];
        var sumRedByBin = new long[BIN_COUNT];
        var sumGreenByBin = new long[BIN_COUNT];
        var sumBlueByBin = new long[BIN_COUNT];

        for (int y = 0; y < height; y += stepY) {
            for (int x = 0; x < width; x += stepX) {
                int abgr = pixelsAbgr[x + (y * width)];
                int alpha = (abgr >>> 24) & 0xFF;
                if (alpha < MIN_ALPHA) {
                    continue;
                }

                int blue = (abgr >>> 16) & 0xFF;
                int green = (abgr >>> 8) & 0xFF;
                int red = abgr & 0xFF;

                int bin = ((red >>> 4) << 8) | ((green >>> 4) << 4) | (blue >>> 4);
                weightByBin[bin] += alpha;
                sumRedByBin[bin] += (long) red * alpha;
                sumGreenByBin[bin] += (long) green * alpha;
                sumBlueByBin[bin] += (long) blue * alpha;
            }
        }

        int bestBin = -1;
        int bestWeight = 0;
        for (int bin = 0; bin < BIN_COUNT; bin++) {
            int weight = weightByBin[bin];
            if (weight > bestWeight) {
                bestBin = bin;
                bestWeight = weight;
            }
        }

        if (bestBin < 0 || bestWeight <= 0) {
            return null;
        }

        int red = (int) (sumRedByBin[bestBin] / bestWeight);
        int green = (int) (sumGreenByBin[bestBin] / bestWeight);
        int blue = (int) (sumBlueByBin[bestBin] / bestWeight);
        return (red << 16) | (green << 8) | blue;
    }

    private static Integer computeAverageRgb(final NativeImage image) {
        if (image == null) {
            return null;
        }

        int width = image.getWidth();
        int height = image.getHeight();
        if (width <= 0 || height <= 0) {
            return null;
        }

        int stepX = Math.max(1, width / MAX_SAMPLE_AXIS);
        int stepY = Math.max(1, height / MAX_SAMPLE_AXIS);

        int[] pixelsAbgr = image.getPixelsABGR();
        long sumRed = 0L;
        long sumGreen = 0L;
        long sumBlue = 0L;
        long sumAlpha = 0L;

        for (int y = 0; y < height; y += stepY) {
            for (int x = 0; x < width; x += stepX) {
                int abgr = pixelsAbgr[x + (y * width)];
                int alpha = (abgr >>> 24) & 0xFF;
                if (alpha < MIN_ALPHA) {
                    continue;
                }

                int blue = (abgr >>> 16) & 0xFF;
                int green = (abgr >>> 8) & 0xFF;
                int red = abgr & 0xFF;

                sumRed += (long) red * alpha;
                sumGreen += (long) green * alpha;
                sumBlue += (long) blue * alpha;
                sumAlpha += alpha;
            }
        }

        if (sumAlpha <= 0L) {
            return null;
        }

        int red = (int) (sumRed / sumAlpha);
        int green = (int) (sumGreen / sumAlpha);
        int blue = (int) (sumBlue / sumAlpha);
        return (red << 16) | (green << 8) | blue;
    }
}
