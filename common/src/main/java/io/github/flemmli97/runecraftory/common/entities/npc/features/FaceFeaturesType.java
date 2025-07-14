package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public record FaceFeaturesType(IndexedColorSetting irisSetting,
                               IndexedColorSetting scleraSetting,
                               IndexedColorSetting eyebrowSetting,
                               Map<String, ExpressionFeature> expressionMap) implements NPCFeature.NPCFeatureHolder<FaceFeaturesType.FaceFeatures> {

    public static final MapCodec<FaceFeaturesType> TYPE_CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(IndexedColorSetting.CODEC.fieldOf("iris").forGetter(d -> d.irisSetting),
                    IndexedColorSetting.CODEC.fieldOf("sclera").forGetter(d -> d.scleraSetting),
                    IndexedColorSetting.CODEC.fieldOf("eyebrow").forGetter(d -> d.eyebrowSetting),
                    Codec.unboundedMap(Codec.STRING, ExpressionFeature.CODEC).fieldOf("expressions").forGetter(d -> d.expressionMap)
            ).apply(inst, FaceFeaturesType::new));
    public static final MapCodec<FaceFeatures> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(IndexedColorSetting.ResolvedIndexColor.CODEC.fieldOf("iris").forGetter(FaceFeatures::iris),
                    IndexedColorSetting.ResolvedIndexColor.CODEC.fieldOf("sclera").forGetter(FaceFeatures::sclera),
                    IndexedColorSetting.ResolvedIndexColor.CODEC.fieldOf("eyebrow").forGetter(FaceFeatures::eyebrow),
                    Codec.unboundedMap(Codec.STRING, ExpressionFeature.CODEC).fieldOf("expressions").forGetter(FaceFeatures::expressionMap)
            ).apply(inst, FaceFeatures::new));
    public static final StreamCodec<ByteBuf, FaceFeatures> STREAM_CODEC = StreamCodec.composite(
            IndexedColorSetting.ResolvedIndexColor.STREAM_CODEC, FaceFeatures::iris,
            IndexedColorSetting.ResolvedIndexColor.STREAM_CODEC, FaceFeatures::sclera,
            IndexedColorSetting.ResolvedIndexColor.STREAM_CODEC, FaceFeatures::eyebrow,
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ExpressionFeature.STREAM_CODEC), FaceFeatures::expressionMap,
            FaceFeatures::new);

    public static final Map<String, ExpressionFeature> DEFAULT_EXPRESSIONS = Map.of(
            "eyes_closed", new ExpressionFeature(new TextureType("closed", true), new TextureType("closed", true), "closed"),
            "angry", new ExpressionFeature(TextureType.NONE, TextureType.NONE, "angry"));

    @Override
    public FaceFeatures create(NPCEntity npc) {
        return new FaceFeatures(this.irisSetting.resolve(npc.getRandom()),
                this.scleraSetting.resolve(npc.getRandom()),
                this.eyebrowSetting.resolve(npc.getRandom()),
                this.expressionMap);
    }

    @Override
    public NPCFeatureType<FaceFeatures> getType() {
        return RuneCraftoryNPCLooks.FACE.get();
    }

    public record FaceFeatures(IndexedColorSetting.ResolvedIndexColor iris,
                               IndexedColorSetting.ResolvedIndexColor sclera,
                               IndexedColorSetting.ResolvedIndexColor eyebrow,
                               Map<String, ExpressionFeature> expressionMap) implements NPCFeature {

        public FaceFeatures(IndexedColorSetting.ResolvedIndexColor iris, IndexedColorSetting.ResolvedIndexColor sclera,
                            IndexedColorSetting.ResolvedIndexColor eyebrow, Map<String, ExpressionFeature> expressionMap) {
            this.iris = iris;
            this.sclera = sclera;
            this.eyebrow = eyebrow;
            this.expressionMap = Map.copyOf(expressionMap);
        }

        @Nullable
        public String expressionTexture(NPCFeatureContainer features, String expression, ExpressionType type) {
            FaceFeatures feat = features.getFeature(RuneCraftoryNPCLooks.FACE.get());
            ExpressionFeature exp = feat.expressionMap.get(expression);
            if (exp != null) {
                return switch (type) {
                    case IRIS -> exp.iris().suffix();
                    case SCLERA -> exp.sclera().suffix();
                    case EYEBROWS -> exp.eyebrow();
                };
            }
            return null;
        }

        public boolean useSkinColor(NPCFeatureContainer features, String expression, ExpressionType type) {
            if (type == ExpressionType.EYEBROWS)
                return false;
            FaceFeatures feat = features.getFeature(RuneCraftoryNPCLooks.FACE.get());
            ExpressionFeature exp = feat.expressionMap.get(expression);
            if (exp != null) {
                if (type == ExpressionType.IRIS) {
                    return exp.iris().useSkinColor();
                } else {
                    return exp.sclera().useSkinColor();
                }
            }
            return false;
        }

        @Override
        public NPCFeatureType<FaceFeatures> type() {
            return RuneCraftoryNPCLooks.FACE.get();
        }
    }

    public record ExpressionFeature(TextureType iris, TextureType sclera, String eyebrow) {

        public static final Codec<ExpressionFeature> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        TextureType.CODEC.fieldOf("iris").forGetter(d -> d.iris),
                        TextureType.CODEC.fieldOf("sclera").forGetter(d -> d.sclera),
                        Codec.STRING.fieldOf("eyebrow").forGetter(d -> d.eyebrow)
                ).apply(inst, ExpressionFeature::new));
        public static final StreamCodec<ByteBuf, ExpressionFeature> STREAM_CODEC = StreamCodec.composite(
                TextureType.STREAM_CODEC, ExpressionFeature::iris, TextureType.STREAM_CODEC, ExpressionFeature::sclera,
                ByteBufCodecs.STRING_UTF8, ExpressionFeature::eyebrow, ExpressionFeature::new);
    }

    public record TextureType(String suffix, boolean useSkinColor) {

        public static TextureType NONE = new TextureType("", false);

        public static final Codec<TextureType> CODEC = Codec.either(Codec.STRING, RecordCodecBuilder.<TextureType>create(inst ->
                        inst.group(
                                Codec.STRING.fieldOf("suffix").forGetter(d -> d.suffix),
                                Codec.BOOL.fieldOf("use_skin_color").forGetter(d -> d.useSkinColor)
                        ).apply(inst, TextureType::new)))
                .xmap(e -> e.map(s -> new TextureType(s, false), t -> t),
                        t -> !t.useSkinColor ? Either.left(t.suffix()) : Either.right(t));
        public static final StreamCodec<ByteBuf, TextureType> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, TextureType::suffix, ByteBufCodecs.BOOL, TextureType::useSkinColor, TextureType::new);
    }

    public enum ExpressionType {
        IRIS,
        SCLERA,
        EYEBROWS
    }
}