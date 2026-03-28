package com.mgen256.al.client.fire;

import com.mojang.blaze3d.platform.NativeImage;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.Identifier;

public final class FireSurfaceTextureGeneratorCore {
    private static final int DEFAULT_FRAME_TIME = 20;
    private static final double CHANNEL_RATIO_EPSILON = 8.0D;
    private static final double CHANNEL_RATIO_MIN = 0.35D;
    private static final double CHANNEL_RATIO_MAX = 3.0D;

    private FireSurfaceTextureGeneratorCore() {}

    public enum FireSurfaceTextureVariantSpec {
        LARGE(true),

        SMALL(false);

        private final boolean animated;

        FireSurfaceTextureVariantSpec(final boolean animated) {
            this.animated = animated;
        }

        public boolean isAnimated() {
            return this.animated;
        }
    }

    private record FireFrameColorSpec(int red, int green, int blue) {}

    private record FireSourceImageSpec(
            int[] pixelsAbgr,
            int imageWidth,
            int frameWidth,
            int frameHeight,
            int frameCount,
            FireFrameColorSpec[] frameColors) {}

    public record FireSurfaceTextureResourceSpec(
            NativeImage image,
            Optional<AnimationMetadataSection> animationMetadata) {
        public FireSurfaceTextureResourceSpec {
            if (image == null) {
                throw new IllegalArgumentException("image must not be null");
            }
            animationMetadata = animationMetadata == null ? Optional.empty() : animationMetadata;
        }
    }

    public record FireSurfaceTextureImageSpec(
            NativeImage image,
            int frameSize,
            Optional<AnimationMetadataSection> animationMetadata) {

        public SpriteContents toSpriteContents(final Identifier spriteId) {
            try {
                return new SpriteContents(
                        spriteId,
                        new FrameSize(this.frameSize, this.frameSize),
                        this.image,
                        this.animationMetadata,
                        List.of(),
                        Optional.empty());
            } catch (RuntimeException e) {
                this.image.close();
                throw e;
            }
        }
    }

    public static FireSurfaceTextureImageSpec generate(
            final FireSurfaceTextureResourceSpec source,
            final FireSurfaceTextureResourceSpec reference,
            final FireSurfaceTextureResourceSpec template,
            final FireSurfaceTextureVariantSpec variant) {
        final FireSourceImageSpec sourceSpec = createSourceImageSpec(source);
        final FireSourceImageSpec referenceSpec = createSourceImageSpec(reference);
        final int templateFrameSize = Math.max(1, template.image().getWidth());
        final int templateFrameCount = Math.max(1, template.image().getHeight() / templateFrameSize);
        final int targetFrameSize = sourceSpec.frameWidth();
        final int outputFrameCount = variant.isAnimated() ? sourceSpec.frameCount() : 1;
        final int[] templatePixelsAbgr = template.image().getPixelsABGR();
        final NativeImage outputImage = new NativeImage(targetFrameSize, targetFrameSize * outputFrameCount, true);

        if (variant.isAnimated()) {
            for (int frameIndex = 0; frameIndex < outputFrameCount; frameIndex++) {
                renderFrame(
                        sourceSpec,
                        frameIndex,
                        referenceSpec,
                        templatePixelsAbgr,
                        template.image().getWidth(),
                        templateFrameSize,
                        frameIndex % templateFrameCount,
                        targetFrameSize,
                        outputImage,
                        frameIndex * targetFrameSize);
                applyTransparentColorBleed(outputImage, frameIndex * targetFrameSize, targetFrameSize);
            }
        } else {
            renderCollapsedFrame(
                    sourceSpec,
                    referenceSpec,
                    templatePixelsAbgr,
                    template.image().getWidth(),
                    templateFrameSize,
                    targetFrameSize,
                    outputImage);
            applyTransparentColorBleed(outputImage, 0, targetFrameSize);
        }

        return new FireSurfaceTextureImageSpec(
                outputImage,
                targetFrameSize,
                createAnimationMetadata(
                        outputFrameCount,
                        targetFrameSize,
                        variant,
                        template.animationMetadata()));
    }

    private static FireSourceImageSpec createSourceImageSpec(final FireSurfaceTextureResourceSpec resource) {
        final NativeImage sourceImage = resource.image();
        final FrameSize sourceFrameSize = resolveSourceFrameSize(sourceImage, resource.animationMetadata());
        final int sourceFrameWidth = Math.max(1, sourceFrameSize.width());
        final int sourceFrameHeight = Math.max(1, sourceFrameSize.height());
        final int sourceFrameCount = Math.max(1, sourceImage.getHeight() / sourceFrameHeight);
        final int[] sourcePixelsAbgr = sourceImage.getPixelsABGR();
        return new FireSourceImageSpec(
                sourcePixelsAbgr,
                sourceImage.getWidth(),
                sourceFrameWidth,
                sourceFrameHeight,
                sourceFrameCount,
                computeSourceFrameColors(
                        sourcePixelsAbgr,
                        sourceImage.getWidth(),
                        sourceFrameWidth,
                        sourceFrameHeight,
                        sourceFrameCount));
    }

    private static FrameSize resolveSourceFrameSize(
            final NativeImage sourceImage,
            final Optional<AnimationMetadataSection> sourceAnimation) {
        if (sourceAnimation.isPresent()) {
            try {
                return sourceAnimation.get().calculateFrameSize(sourceImage.getWidth(), sourceImage.getHeight());
            } catch (RuntimeException ignored) {
            }
        }

        final int inferredSize = Math.max(1, Math.min(sourceImage.getWidth(), sourceImage.getHeight()));
        return new FrameSize(sourceImage.getWidth(), inferredSize);
    }

    private static Optional<AnimationMetadataSection> createAnimationMetadata(
            final int outputFrameCount,
            final int targetFrameSize,
            final FireSurfaceTextureVariantSpec variant,
            final Optional<AnimationMetadataSection> templateAnimation) {
        if (!variant.isAnimated() || outputFrameCount <= 1) {
            return Optional.empty();
        }

        if (templateAnimation.isPresent()) {
            return Optional.of(copyAnimationPlaybackPolicy(templateAnimation.get(), targetFrameSize));
        }

        return Optional.of(
                new AnimationMetadataSection(
                        Optional.empty(),
                        Optional.of(targetFrameSize),
                        Optional.of(targetFrameSize),
                        DEFAULT_FRAME_TIME,
                        true));
    }

    private static AnimationMetadataSection copyAnimationPlaybackPolicy(
            final AnimationMetadataSection templateAnimation,
            final int targetFrameSize) {
        return new AnimationMetadataSection(
                Optional.empty(),
                Optional.of(targetFrameSize),
                Optional.of(targetFrameSize),
                Math.max(1, templateAnimation.defaultFrameTime()),
                templateAnimation.interpolatedFrames());
    }

    private static FireFrameColorSpec[] computeSourceFrameColors(
            final int[] sourcePixelsAbgr,
            final int sourceImageWidth,
            final int sourceFrameWidth,
            final int sourceFrameHeight,
            final int sourceFrameCount) {
        final FireFrameColorSpec[] colors = new FireFrameColorSpec[sourceFrameCount];
        for (int frameIndex = 0; frameIndex < sourceFrameCount; frameIndex++) {
            long alphaSum = 0L;
            long redSum = 0L;
            long greenSum = 0L;
            long blueSum = 0L;
            final int frameOriginY = frameIndex * sourceFrameHeight;

            for (int y = 0; y < sourceFrameHeight; y++) {
                for (int x = 0; x < sourceFrameWidth; x++) {
                    final int abgr = sourcePixelsAbgr[x + ((frameOriginY + y) * sourceImageWidth)];
                    final int alpha = (abgr >>> 24) & 0xFF;
                    if (alpha <= 0) {
                        continue;
                    }

                    alphaSum += alpha;
                    blueSum += (long) ((abgr >>> 16) & 0xFF) * alpha;
                    greenSum += (long) ((abgr >>> 8) & 0xFF) * alpha;
                    redSum += (long) (abgr & 0xFF) * alpha;
                }
            }

            if (alphaSum <= 0L) {
                colors[frameIndex] = new FireFrameColorSpec(255, 170, 40);
                continue;
            }

            colors[frameIndex] =
                    new FireFrameColorSpec(
                            clampToByte((int) (redSum / alphaSum)),
                            clampToByte((int) (greenSum / alphaSum)),
                            clampToByte((int) (blueSum / alphaSum)));
        }
        return colors;
    }

    private static void renderFrame(
            final FireSourceImageSpec sourceSpec,
            final int sourceFrameIndex,
            final FireSourceImageSpec referenceSpec,
            final int[] templatePixelsAbgr,
            final int templateImageWidth,
            final int templateFrameSize,
            final int templateFrameIndex,
            final int targetFrameSize,
            final NativeImage outputImage,
            final int outputOriginY) {
        for (int y = 0; y < targetFrameSize; y++) {
            for (int x = 0; x < targetFrameSize; x++) {
                outputImage.setPixelABGR(
                        x,
                        outputOriginY + y,
                        composeFirePixel(
                                sourceSpec,
                                sourceFrameIndex,
                                referenceSpec,
                                templatePixelsAbgr,
                                templateImageWidth,
                                templateFrameSize,
                                templateFrameIndex,
                                targetFrameSize,
                                x,
                                y));
            }
        }
    }

    private static void renderCollapsedFrame(
            final FireSourceImageSpec sourceSpec,
            final FireSourceImageSpec referenceSpec,
            final int[] templatePixelsAbgr,
            final int templateImageWidth,
            final int templateFrameSize,
            final int targetFrameSize,
            final NativeImage outputImage) {
        for (int y = 0; y < targetFrameSize; y++) {
            for (int x = 0; x < targetFrameSize; x++) {
                long alphaWeight = 0L;
                long redSum = 0L;
                long greenSum = 0L;
                long blueSum = 0L;

                for (int frameIndex = 0; frameIndex < sourceSpec.frameCount(); frameIndex++) {
                    final int abgr =
                            composeFirePixel(
                                    sourceSpec,
                                    frameIndex,
                                    referenceSpec,
                                    templatePixelsAbgr,
                                    templateImageWidth,
                                    templateFrameSize,
                                    0,
                                    targetFrameSize,
                                    x,
                                    y);
                    final int alpha = (abgr >>> 24) & 0xFF;
                    if (alpha <= 0) {
                        continue;
                    }

                    alphaWeight += alpha;
                    blueSum += (long) ((abgr >>> 16) & 0xFF) * alpha;
                    greenSum += (long) ((abgr >>> 8) & 0xFF) * alpha;
                    redSum += (long) (abgr & 0xFF) * alpha;
                }

                if (alphaWeight <= 0L) {
                    outputImage.setPixelABGR(x, y, 0);
                    continue;
                }

                outputImage.setPixelABGR(
                        x,
                        y,
                        toAbgr(
                                clampToByte((int) Math.round((double) alphaWeight / sourceSpec.frameCount())),
                                clampToByte((int) (redSum / alphaWeight)),
                                clampToByte((int) (greenSum / alphaWeight)),
                                clampToByte((int) (blueSum / alphaWeight))));
            }
        }
    }

    private static int composeFirePixel(
            final FireSourceImageSpec sourceSpec,
            final int sourceFrameIndex,
            final FireSourceImageSpec referenceSpec,
            final int[] templatePixelsAbgr,
            final int templateImageWidth,
            final int templateFrameSize,
            final int templateFrameIndex,
            final int targetFrameSize,
            final int targetX,
            final int targetY) {
        final double u = targetFrameSize <= 1 ? 0.5D : (double) targetX / (double) (targetFrameSize - 1);
        final double v = targetFrameSize <= 1 ? 0.5D : (double) targetY / (double) (targetFrameSize - 1);
        final int templatePixel =
                sampleTemplatePixel(
                        templatePixelsAbgr,
                        templateImageWidth,
                        templateFrameSize,
                        templateFrameIndex,
                        u,
                        v);
        final int templateAlpha = (templatePixel >>> 24) & 0xFF;
        if (templateAlpha <= 0) {
            return 0;
        }

        final FireFrameColorSpec sampledColor =
                sampleSourceColor(
                        sourceSpec,
                        sourceFrameIndex,
                        u,
                        v);
        final FireFrameColorSpec referenceColor =
                sampleSourceColor(
                        referenceSpec,
                        sourceFrameIndex,
                        u,
                        v);
        return tintTemplatePixel(templatePixel, templateAlpha, sampledColor, referenceColor);
    }

    private static FireFrameColorSpec sampleSourceColor(
            final FireSourceImageSpec sourceSpec,
            final int sourceFrameIndex,
            final double u,
            final double v) {
        long alphaSum = 0L;
        long redSum = 0L;
        long greenSum = 0L;
        long blueSum = 0L;
        final int normalizedFrameIndex = Math.floorMod(sourceFrameIndex, sourceSpec.frameCount());
        final int frameOriginY = normalizedFrameIndex * sourceSpec.frameHeight();

        final int[][] rotatedCoords = {
            sampleFrameCoords(u, v, sourceSpec.frameWidth(), sourceSpec.frameHeight()),
            sampleFrameCoords(1.0D - v, u, sourceSpec.frameWidth(), sourceSpec.frameHeight()),
            sampleFrameCoords(1.0D - u, 1.0D - v, sourceSpec.frameWidth(), sourceSpec.frameHeight()),
            sampleFrameCoords(v, 1.0D - u, sourceSpec.frameWidth(), sourceSpec.frameHeight())
        };

        for (int[] coord : rotatedCoords) {
            final int abgr = sourceSpec.pixelsAbgr()[coord[0] + ((frameOriginY + coord[1]) * sourceSpec.imageWidth())];
            final int alpha = (abgr >>> 24) & 0xFF;
            if (alpha <= 0) {
                continue;
            }

            alphaSum += alpha;
            blueSum += (long) ((abgr >>> 16) & 0xFF) * alpha;
            greenSum += (long) ((abgr >>> 8) & 0xFF) * alpha;
            redSum += (long) (abgr & 0xFF) * alpha;
        }

        if (alphaSum <= 0L) {
            return sourceSpec.frameColors()[normalizedFrameIndex];
        }

        return new FireFrameColorSpec(
                clampToByte((int) (redSum / alphaSum)),
                clampToByte((int) (greenSum / alphaSum)),
                clampToByte((int) (blueSum / alphaSum)));
    }

    private static int tintTemplatePixel(
            final int templatePixel,
            final int templateAlpha,
            final FireFrameColorSpec sampledColor,
            final FireFrameColorSpec referenceColor) {
        final int templateBlue = (templatePixel >>> 16) & 0xFF;
        final int templateGreen = (templatePixel >>> 8) & 0xFF;
        final int templateRed = templatePixel & 0xFF;
        return toAbgr(
                templateAlpha,
                tintTemplateChannel(templateRed, sampledColor.red(), referenceColor.red()),
                tintTemplateChannel(templateGreen, sampledColor.green(), referenceColor.green()),
                tintTemplateChannel(templateBlue, sampledColor.blue(), referenceColor.blue()));
    }

    private static int tintTemplateChannel(
            final int templateChannel,
            final int sampledChannel,
            final int referenceChannel) {
        final double ratio =
                clampToDouble(
                        ((double) sampledChannel + CHANNEL_RATIO_EPSILON)
                                / ((double) referenceChannel + CHANNEL_RATIO_EPSILON),
                        CHANNEL_RATIO_MIN,
                        CHANNEL_RATIO_MAX);
        return clampToByte((int) Math.round(templateChannel * ratio));
    }

    private static int sampleTemplatePixel(
            final int[] templatePixelsAbgr,
            final int templateImageWidth,
            final int templateFrameSize,
            final int templateFrameIndex,
            final double u,
            final double v) {
        final int x = clampToRange((int) Math.round(u * (templateFrameSize - 1)), 0, templateFrameSize - 1);
        final int y = clampToRange((int) Math.round(v * (templateFrameSize - 1)), 0, templateFrameSize - 1);
        return templatePixelsAbgr[x + (((templateFrameIndex * templateFrameSize) + y) * templateImageWidth)];
    }

    private static int[] sampleFrameCoords(
            final double u,
            final double v,
            final int sourceFrameWidth,
            final int sourceFrameHeight) {
        return new int[] {
            clampToRange((int) Math.round(u * (sourceFrameWidth - 1)), 0, sourceFrameWidth - 1),
            clampToRange((int) Math.round(v * (sourceFrameHeight - 1)), 0, sourceFrameHeight - 1)
        };
    }

    private static void applyTransparentColorBleed(
            final NativeImage outputImage,
            final int frameOriginY,
            final int frameSize) {
        final int[] snapshot = outputImage.getPixelsABGR();
        final int outputWidth = outputImage.getWidth();

        for (int y = 0; y < frameSize; y++) {
            for (int x = 0; x < frameSize; x++) {
                final int pixel = snapshot[x + ((frameOriginY + y) * outputWidth)];
                if (((pixel >>> 24) & 0xFF) != 0) {
                    continue;
                }

                final int nearestColor = findNearestVisibleColor(snapshot, outputWidth, frameOriginY, frameSize, x, y);
                if (nearestColor == 0) {
                    continue;
                }

                outputImage.setPixelABGR(
                        x,
                        frameOriginY + y,
                        toAbgr(
                                0,
                                nearestColor & 0xFF,
                                (nearestColor >>> 8) & 0xFF,
                                (nearestColor >>> 16) & 0xFF));
            }
        }
    }

    private static int findNearestVisibleColor(
            final int[] pixelsAbgr,
            final int imageWidth,
            final int frameOriginY,
            final int frameSize,
            final int targetX,
            final int targetY) {
        int bestDistanceSquared = Integer.MAX_VALUE;
        int bestPixel = 0;

        for (int y = 0; y < frameSize; y++) {
            for (int x = 0; x < frameSize; x++) {
                final int pixel = pixelsAbgr[x + ((frameOriginY + y) * imageWidth)];
                if (((pixel >>> 24) & 0xFF) == 0) {
                    continue;
                }

                final int distanceX = x - targetX;
                final int distanceY = y - targetY;
                final int distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
                if (distanceSquared < bestDistanceSquared) {
                    bestDistanceSquared = distanceSquared;
                    bestPixel = pixel;
                }
            }
        }

        return bestPixel;
    }

    private static int clampToByte(final int value) {
        return clampToRange(value, 0, 255);
    }

    private static double clampToDouble(final double value, final double min, final double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static int clampToRange(final int value, final int min, final int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static int toAbgr(final int alpha, final int red, final int green, final int blue) {
        return (alpha << 24) | (blue << 16) | (green << 8) | red;
    }
}
