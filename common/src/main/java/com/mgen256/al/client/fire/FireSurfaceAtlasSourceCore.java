package com.mgen256.al.client.fire;

import com.mgen256.al.CommonConstants;
import com.mgen256.al.client.fire.FireSurfaceTextureGeneratorCore.FireSurfaceTextureImageSpec;
import com.mgen256.al.client.fire.FireSurfaceTextureGeneratorCore.FireSurfaceTextureResourceSpec;
import com.mgen256.al.client.fire.FireSurfaceTextureGeneratorCore.FireSurfaceTextureVariantSpec;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.MapCodec;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FireSurfaceAtlasSourceCore implements SpriteSource {
    public static final Identifier TYPE_ID =
            Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "fire_surface");
    public static final FireSurfaceAtlasSourceCore INSTANCE = new FireSurfaceAtlasSourceCore();
    public static final MapCodec<FireSurfaceAtlasSourceCore> CODEC = MapCodec.unit(INSTANCE).stable();

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConstants.MOD_ID);
    private static final List<FireSurfaceAtlasEntrySpec> ENTRIES = List.of(
            new FireSurfaceAtlasEntrySpec(
                    Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "block/fire_lit_l"),
                    Identifier.fromNamespaceAndPath("minecraft", "textures/block/campfire_fire.png"),
                    Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "textures/block/fire_lit_l.png"),
                    FireSurfaceTextureVariantSpec.LARGE),
            new FireSurfaceAtlasEntrySpec(
                    Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "block/fire_lit_s"),
                    Identifier.fromNamespaceAndPath("minecraft", "textures/block/campfire_fire.png"),
                    Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "textures/block/fire_lit_s.png"),
                    FireSurfaceTextureVariantSpec.SMALL),
            new FireSurfaceAtlasEntrySpec(
                    Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "block/soul_fire_lit_l"),
                    Identifier.fromNamespaceAndPath("minecraft", "textures/block/soul_campfire_fire.png"),
                    Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "textures/block/soul_fire_lit_l.png"),
                    FireSurfaceTextureVariantSpec.LARGE),
            new FireSurfaceAtlasEntrySpec(
                    Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "block/soul_fire_lit_s"),
                    Identifier.fromNamespaceAndPath("minecraft", "textures/block/soul_campfire_fire.png"),
                    Identifier.fromNamespaceAndPath(CommonConstants.MOD_ID, "textures/block/soul_fire_lit_s.png"),
                    FireSurfaceTextureVariantSpec.SMALL));

    private FireSurfaceAtlasSourceCore() {}

    private record FireSurfaceAtlasEntrySpec(
            Identifier targetSpriteId,
            Identifier sourceTextureFile,
            Identifier templateTextureFile,
            FireSurfaceTextureVariantSpec variant) {}

    @Override
    public void run(final ResourceManager resourceManager, final Output output) {
        if (FireSurfaceTextureConfigCore.readMode() != FireSurfaceTextureModeSpec.DYNAMIC) {
            return;
        }

        for (FireSurfaceAtlasEntrySpec entry : ENTRIES) {
            replaceSprite(resourceManager, output, entry);
        }
    }

    @Override
    public MapCodec<? extends SpriteSource> codec() {
        return CODEC;
    }

    private static void replaceSprite(
            final ResourceManager resourceManager,
            final Output output,
            final FireSurfaceAtlasEntrySpec entry) {
        final Optional<Resource> sourceResourceOptional = resourceManager.getResource(entry.sourceTextureFile());
        final List<Resource> sourceResourceStack = resourceManager.getResourceStack(entry.sourceTextureFile());
        final Optional<Resource> templateResourceOptional = resourceManager.getResource(entry.templateTextureFile());
        if (sourceResourceOptional.isEmpty() || templateResourceOptional.isEmpty()) {
            return;
        }
        final Resource referenceResource = resolveReferenceResource(sourceResourceOptional.get(), sourceResourceStack);

        try {
            final SpriteContents spriteContents =
                    createSpriteContents(
                            sourceResourceOptional.get(),
                            referenceResource,
                            templateResourceOptional.get(),
                            entry);
            output.removeAll(entry.targetSpriteId()::equals);
            output.add(entry.targetSpriteId(), new GeneratedSpriteLoaderCore(spriteContents));
        } catch (Exception e) {
            LOGGER.warn(
                    "Failed to generate dynamic fire surface sprite {} from {} using template {}",
                    entry.targetSpriteId(),
                    entry.sourceTextureFile(),
                    entry.templateTextureFile(),
                    e);
        }
    }

    private static Resource resolveReferenceResource(
            final Resource sourceResource,
            final List<Resource> sourceResourceStack) {
        for (Resource resource : sourceResourceStack) {
            if ("vanilla".equals(resource.sourcePackId())) {
                return resource;
            }
        }
        if (!sourceResourceStack.isEmpty()) {
            return sourceResourceStack.get(sourceResourceStack.size() - 1);
        }
        return sourceResource;
    }

    private static SpriteContents createSpriteContents(
            final Resource sourceResource,
            final Resource referenceResource,
            final Resource templateResource,
            final FireSurfaceAtlasEntrySpec entry) throws IOException {
        final Optional<AnimationMetadataSection> sourceAnimationMetadata =
                sourceResource.metadata().getSection(AnimationMetadataSection.TYPE);
        final Optional<AnimationMetadataSection> referenceAnimationMetadata =
                referenceResource.metadata().getSection(AnimationMetadataSection.TYPE);
        final Optional<AnimationMetadataSection> templateAnimationMetadata =
                templateResource.metadata().getSection(AnimationMetadataSection.TYPE);
        try (InputStream stream = sourceResource.open();
                NativeImage sourceImage = NativeImage.read(stream);
                InputStream referenceStream = referenceResource.open();
                NativeImage referenceImage = NativeImage.read(referenceStream);
                InputStream templateStream = templateResource.open();
                NativeImage templateImage = NativeImage.read(templateStream)) {
            final FireSurfaceTextureImageSpec generated =
                    FireSurfaceTextureGeneratorCore.generate(
                            new FireSurfaceTextureResourceSpec(sourceImage, sourceAnimationMetadata),
                            new FireSurfaceTextureResourceSpec(referenceImage, referenceAnimationMetadata),
                            new FireSurfaceTextureResourceSpec(templateImage, templateAnimationMetadata),
                            entry.variant());
            return generated.toSpriteContents(entry.targetSpriteId());
        }
    }

    private static final class GeneratedSpriteLoaderCore implements SpriteSource.DiscardableLoader {
        private final SpriteContents spriteContents;

        private GeneratedSpriteLoaderCore(final SpriteContents spriteContents) {
            this.spriteContents = spriteContents;
        }

        @Override
        public SpriteContents get(final SpriteResourceLoader ignoredLoader) {
            return this.spriteContents;
        }

        @Override
        public void discard() {
            this.spriteContents.close();
        }
    }
}
