package com.mgen256.al.client.gui;

import com.mgen256.al.client.color.ItemTextureColorCacheCore;
import com.mgen256.al.client.color.ItemTextureColorSpec;

final class CreativeTabColorSortCore {
    private static final int GRAYISH_SATURATION_THRESHOLD = 64;
    private static final int HUE_BUCKET_DEGREES = 15;
    private static final long COLORED_GROUP_PREFIX = 1L << 32;

    private CreativeTabColorSortCore() {}

    static long computeColorSortKey(final ItemTextureColorSpec colorSpec) {
        if (colorSpec == null
                || colorSpec.dominantRgb() == ItemTextureColorCacheCore.UNKNOWN_RGB
                || colorSpec.averageRgb() == ItemTextureColorCacheCore.UNKNOWN_RGB) {
            return Long.MAX_VALUE;
        }

        final ColorMetricsSpec dominant = ColorMetricsSpec.fromRgb(colorSpec.dominantRgb());
        final ColorMetricsSpec average = ColorMetricsSpec.fromRgb(colorSpec.averageRgb());
        if (average.saturationByte() < GRAYISH_SATURATION_THRESHOLD) {
            return computeGrayishSortKey(dominant, average);
        }

        return computeColoredSortKey(dominant, average);
    }

    private static long computeGrayishSortKey(
            final ColorMetricsSpec dominant, final ColorMetricsSpec average) {
        final int invertedDominantLuminance = 255 - dominant.luminanceByte();
        final int invertedAverageLuminance = 255 - average.luminanceByte();

        return ((long) invertedDominantLuminance << 24)
                | ((long) invertedAverageLuminance << 16)
                | ((long) average.saturationByte() << 8)
                | average.hueByte();
    }

    private static long computeColoredSortKey(
            final ColorMetricsSpec dominant, final ColorMetricsSpec average) {
        final int invertedAverageSaturation = 255 - average.saturationByte();
        final int invertedDominantLuminance = 255 - dominant.luminanceByte();

        return COLORED_GROUP_PREFIX
                | ((long) average.hueBucket() << 24)
                | ((long) invertedAverageSaturation << 16)
                | ((long) invertedDominantLuminance << 8)
                | average.hueByte();
    }

    private static int clampByte(final int value) {
        return Math.max(0, Math.min(255, value));
    }

    private record ColorMetricsSpec(int hueByte, int hueBucket, int saturationByte, int luminanceByte) {
        private static ColorMetricsSpec fromRgb(final int rgb) {
            final float red = ((rgb >>> 16) & 0xFF) / 255.0F;
            final float green = ((rgb >>> 8) & 0xFF) / 255.0F;
            final float blue = (rgb & 0xFF) / 255.0F;

            final float max = Math.max(red, Math.max(green, blue));
            final float min = Math.min(red, Math.min(green, blue));
            final float delta = max - min;

            float hueDegrees = 0.0F;
            if (delta > 0.0F) {
                if (max == red) {
                    hueDegrees = ((green - blue) / delta) % 6.0F;
                } else if (max == green) {
                    hueDegrees = ((blue - red) / delta) + 2.0F;
                } else {
                    hueDegrees = ((red - green) / delta) + 4.0F;
                }

                hueDegrees *= 60.0F;
                if (hueDegrees < 0.0F) {
                    hueDegrees += 360.0F;
                }
            }

            final float saturation = max <= 0.0F ? 0.0F : (delta / max);
            final float luminance =
                    (0.2126F * ((rgb >>> 16) & 0xFF))
                            + (0.7152F * ((rgb >>> 8) & 0xFF))
                            + (0.0722F * (rgb & 0xFF));

            final int hueByte = clampByte(Math.round((hueDegrees / 360.0F) * 255.0F));
            final int hueBucket = Math.min(23, (int) Math.floor(hueDegrees / HUE_BUCKET_DEGREES));
            final int saturationByte = clampByte(Math.round(saturation * 255.0F));
            final int luminanceByte = clampByte(Math.round(luminance));

            return new ColorMetricsSpec(hueByte, hueBucket, saturationByte, luminanceByte);
        }
    }
}
