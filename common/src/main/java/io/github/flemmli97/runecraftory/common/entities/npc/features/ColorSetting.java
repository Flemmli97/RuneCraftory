package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import java.util.List;

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

    public static final Codec<ColorSetting> CODEC = Codec.either(ColorRange.HEX_COLOR, ColorRange.CODEC).listOf()
            .xmap(ColorSetting::new, ColorSetting::colors);

    public int getRandom(RandomSource random) {
        if (this.colors.isEmpty())
            return 0xffffff;
        Either<Integer, ColorRange> rand = this.colors.get(random.nextInt(this.colors.size()));
        return rand.map(i -> i, range -> range.getRandom(random));
    }

    public record ColorRange(int colorMin, int colorMax) {

        public static Codec<Integer> HEX_COLOR = Codec.STRING.flatXmap(s -> {
            try {
                return DataResult.success(Integer.parseInt(s, 16));
            } catch (NumberFormatException e) {
                return DataResult.error(() -> "Could not parse color " + s);
            }
        }, i -> DataResult.success(String.format("%06x", i)));
        public static final Codec<ColorRange> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(HEX_COLOR.fieldOf("color_min").forGetter(d -> d.colorMin),
                        HEX_COLOR.fieldOf("color_max").forGetter(d -> d.colorMax)).apply(inst, ColorRange::new));

        static int randomRange(RandomSource random, int first, int second) {
            if (first > second)
                return Mth.nextInt(random, second, first);
            return Mth.nextInt(random, first, second);
        }

        public int getRandom(RandomSource random) {
            int red = randomRange(random, this.colorMin >> 16 & 255, this.colorMax >> 16 & 255);
            int green = randomRange(random, this.colorMin >> 8 & 255, this.colorMax >> 8 & 255);
            int blue = randomRange(random, this.colorMin & 255, this.colorMax & 255);
            return red << 16 | green << 8 | blue;
        }
    }
}
