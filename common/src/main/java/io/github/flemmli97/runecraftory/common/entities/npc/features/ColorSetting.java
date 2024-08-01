package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureHolder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public record ColorSetting(List<Either<Integer, ColorRange>> colors) {

    public static final ColorSetting DEFAULT = new ColorSetting(List.of());
    public static final ColorSetting SKIN_COLOR_RANGE = new ColorSetting(List.of(
            Either.right(new ColorRange(0xfeead9, 0xf4a98b)), //Roughly based on Fitzpatrick scale
            Either.right(new ColorRange(0xefdbca, 0xeca188)),
            Either.right(new ColorRange(0xd5bfa7, 0xc18356)),
            Either.right(new ColorRange(0xa1836d, 0x94563c)),
            Either.right(new ColorRange(0x664f44, 0x592e19)),
            Either.right(new ColorRange(0x312926, 0x2c130c))
    ));
    public static final ColorSetting HAIR_COLOR_RANGE = new ColorSetting(List.of(
            Either.right(new ColorRange(0x000000, 0x161616)), //black
            Either.right(new ColorRange(0x321503, 0x663d24)), //brown
            Either.right(new ColorRange(0xd8c811, 0xf1eaa0)), //blonde
            Either.right(new ColorRange(0x750808, 0xc55252)), //red
            Either.right(new ColorRange(0xb93aa3, 0xc37bb7)), //pink
            Either.right(new ColorRange(0x3d41a5, 0x7287d2)), //blue
            Either.right(new ColorRange(0xffffff, 0xdde5f7)) //white
    ));
    public static final ColorSetting EYE_COLOR_RANGE = new ColorSetting(List.of(
            Either.right(new ColorRange(0x000000, 0x2f2f2f)), //black
            Either.right(new ColorRange(0x462a05, 0x6d4720)), //brown
            Either.right(new ColorRange(0x0b2a75, 0x204a87)), //blue
            Either.right(new ColorRange(0x42a20c, 0x6fbe36)), //green
            Either.right(new ColorRange(0xb9ae08, 0xdbbf41)) //yellow
    ));
    public static final ColorSetting EYEBROW_COLOR_RANGE = new ColorSetting(List.of(
            Either.right(new ColorRange(0x3c1d05, 0x8d7053)) // Brown
    ));
    public static final ColorSetting BLUSH_COLOR_RANGE = new ColorSetting(List.of(
            Either.right(new ColorRange(0xe1a9e8, 0xf5cffa)) // Pink
    ));

    public static final Codec<ColorSetting> CODEC = Codec.either(Codec.INT, ColorRange.CODEC).listOf()
            .xmap(ColorSetting::new, ColorSetting::colors);

    public int getRandom(Random random) {
        if (this.colors.isEmpty())
            return 0xffffff;
        Either<Integer, ColorRange> rand = this.colors.get(random.nextInt(this.colors.size()));
        return rand.map(i -> i, range -> range.getRandom(random));
    }

    public static NPCFeatureType<ColorSettingFeature> createSimple(Supplier<NPCFeatureType<ColorSettingFeature>> type) {
        return new NPCFeatureType<>(ColorSetting.CODEC.xmap(s -> new ColorSettingFeatureType(s, type), f -> f.setting),
                buf -> new ColorSettingFeature(buf) {
                    @Override
                    public NPCFeatureType<?> getType() {
                        return type.get();
                    }
                },
                tag -> new ColorSettingFeature(tag) {
                    @Override
                    public NPCFeatureType<?> getType() {
                        return type.get();
                    }
                });
    }

    public record ColorRange(int colorMin, int colorMax) {

        public static Codec<Integer> HEX_COLOR = Codec.STRING.flatXmap(s -> {
            try {
                return DataResult.success(Integer.parseInt(s, 16));
            } catch (NumberFormatException e) {
                return DataResult.error("Could not parse color " + s);
            }
        }, i -> DataResult.success(String.format("%06x", i)));
        public static final Codec<ColorRange> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(HEX_COLOR.fieldOf("colorMin").forGetter(d -> d.colorMin),
                        HEX_COLOR.fieldOf("colorMax").forGetter(d -> d.colorMax)).apply(inst, ColorRange::new));

        static int randomRange(Random random, int first, int second) {
            if (first > second)
                return Mth.nextInt(random, second, first);
            return Mth.nextInt(random, first, second);
        }

        public int getRandom(Random random) {
            int red = randomRange(random, this.colorMin >> 16 & 255, this.colorMax >> 16 & 255);
            int green = randomRange(random, this.colorMin >> 8 & 255, this.colorMax >> 8 & 255);
            int blue = randomRange(random, this.colorMin & 255, this.colorMax & 255);
            return red << 16 | green << 8 | blue;
        }
    }

    public static class ColorSettingFeatureType implements NPCFeatureHolder<ColorSettingFeature> {

        private final ColorSetting setting;
        private final Supplier<NPCFeatureType<ColorSettingFeature>> type;

        protected ColorSettingFeatureType(ColorSetting setting, Supplier<NPCFeatureType<ColorSettingFeature>> type) {
            this.setting = setting;
            this.type = type;
        }

        @Override
        public ColorSettingFeature create(EntityNPCBase npc) {
            return new ColorSettingFeature(this.setting.getRandom(npc.getRandom())) {
                @Override
                public NPCFeatureType<?> getType() {
                    return ColorSettingFeatureType.this.type.get();
                }
            };
        }

        @Override
        public NPCFeatureType<ColorSettingFeature> getType() {
            return this.type.get();
        }
    }

    public static abstract class ColorSettingFeature implements NPCFeature {

        public final int color;

        public ColorSettingFeature(FriendlyByteBuf buf) {
            this(buf.readInt());
        }

        public ColorSettingFeature(Tag tag) {
            this(((IntTag) tag).getAsInt());
        }

        public ColorSettingFeature(int color) {
            this.color = color;
        }

        @Override
        public void writeToBuffer(FriendlyByteBuf buf) {
            buf.writeInt(this.color);
        }

        @Override
        public Tag save() {
            return IntTag.valueOf(this.color);
        }
    }
}
