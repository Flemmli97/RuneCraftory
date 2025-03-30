package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureHolder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record FaceFeaturesType(IndexedColorSetting irisSetting,
                               IndexedColorSetting scleraSetting,
                               IndexedColorSetting eyebrowSetting,
                               Map<String, ExpressionFeature> expressionMap) implements NPCFeatureHolder<FaceFeaturesType.FaceFeatures> {

    public static final Codec<FaceFeaturesType> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    IndexedColorSetting.CODEC.fieldOf("iris").forGetter(d -> d.irisSetting),
                    IndexedColorSetting.CODEC.fieldOf("sclera").forGetter(d -> d.scleraSetting),
                    IndexedColorSetting.CODEC.fieldOf("eyebrow").forGetter(d -> d.eyebrowSetting),
                    Codec.unboundedMap(Codec.STRING, ExpressionFeature.CODEC).fieldOf("expressions").forGetter(d -> d.expressionMap)
            ).apply(inst, FaceFeaturesType::new));

    public static final Map<String, ExpressionFeature> DEFAULT_EXPRESSIONS = Map.of(
            "eyes_closed", new ExpressionFeature(new TextureType("closed", true), new TextureType("closed", true), "closed"),
            "angry", new ExpressionFeature(TextureType.NONE, TextureType.NONE, "angry"));

    @Override
    public FaceFeatures create(EntityNPCBase npc) {
        return new FaceFeatures(this.irisSetting.resolve(npc.getRandom()),
                this.scleraSetting.resolve(npc.getRandom()),
                this.eyebrowSetting.resolve(npc.getRandom()),
                this.expressionMap);
    }

    @Override
    public NPCFeatureType<FaceFeatures> getType() {
        return ModNPCLooks.FACE.get();
    }

    public static class FaceFeatures implements NPCFeature {

        public final IndexedColorSetting.ResolvedIndexColor iris, sclera, eyebrow;
        public final Map<String, ExpressionFeature> expressionMap;

        public FaceFeatures(FriendlyByteBuf buf) {
            this(new IndexedColorSetting.ResolvedIndexColor(buf),
                    new IndexedColorSetting.ResolvedIndexColor(buf),
                    new IndexedColorSetting.ResolvedIndexColor(buf),
                    buf.readMap(FriendlyByteBuf::readUtf, b ->
                            new ExpressionFeature(new TextureType(b.readUtf(), b.readBoolean()),
                                    new TextureType(b.readUtf(), b.readBoolean()),
                                    b.readUtf())));
        }

        public FaceFeatures(Tag tag) {
            this(new IndexedColorSetting.ResolvedIndexColor(((CompoundTag) tag).getCompound("Iris")),
                    new IndexedColorSetting.ResolvedIndexColor(((CompoundTag) tag).getCompound("Sclera")),
                    new IndexedColorSetting.ResolvedIndexColor(((CompoundTag) tag).getCompound("Eyebrow")),
                    fromTag(((CompoundTag) tag).getCompound("Expressions")));
        }

        public FaceFeatures(IndexedColorSetting.ResolvedIndexColor iris, IndexedColorSetting.ResolvedIndexColor sclera,
                            IndexedColorSetting.ResolvedIndexColor eyebrow, Map<String, ExpressionFeature> expressionMap) {
            this.iris = iris;
            this.sclera = sclera;
            this.eyebrow = eyebrow;
            this.expressionMap = expressionMap;
        }

        private static Map<String, ExpressionFeature> fromTag(CompoundTag tag) {
            ImmutableMap.Builder<String, ExpressionFeature> builder = new ImmutableMap.Builder<>();
            tag.getAllKeys().forEach((k -> {
                CompoundTag expTag = tag.getCompound(k);
                builder.put(k, new ExpressionFeature(new TextureType(expTag.getString("IrisSuffix"), expTag.getBoolean("IrisColor")),
                        new TextureType(expTag.getString("ScleraSuffix"), expTag.getBoolean("ScleraColor")),
                        expTag.getString("Eyebrow")));
            }));
            return builder.build();
        }

        @Nullable
        public String expressionTexture(NPCFeatureContainer features, String expression, ExpressionType type) {
            FaceFeatures feat = features.getFeature(ModNPCLooks.FACE.get());
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
            FaceFeatures feat = features.getFeature(ModNPCLooks.FACE.get());
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
        public void writeToBuffer(FriendlyByteBuf buf) {
            this.iris.writeToBuffer(buf);
            this.sclera.writeToBuffer(buf);
            this.eyebrow.writeToBuffer(buf);
            buf.writeMap(this.expressionMap, FriendlyByteBuf::writeUtf, (b, exp) -> {
                b.writeUtf(exp.iris.suffix());
                b.writeBoolean(exp.iris.useSkinColor());
                b.writeUtf(exp.sclera.suffix());
                b.writeBoolean(exp.sclera.useSkinColor());
                b.writeUtf(exp.eyebrow);
            });
        }

        @Override
        public Tag save() {
            CompoundTag tag = new CompoundTag();
            tag.put("Iris", this.iris.save());
            tag.put("Sclera", this.sclera.save());
            tag.put("Eyebrow", this.eyebrow.save());
            CompoundTag expMap = new CompoundTag();
            this.expressionMap.forEach((s, e) -> {
                CompoundTag expTag = new CompoundTag();
                expTag.putString("IrisSuffix", e.iris.suffix());
                expTag.putBoolean("IrisColor", e.iris.useSkinColor());
                expTag.putString("ScleraSuffix", e.sclera.suffix());
                expTag.putBoolean("ScleraColor", e.iris.useSkinColor());
                expTag.putString("Eyebrow", e.eyebrow);
                expMap.put(s, expTag);
            });
            tag.put("Expressions", expMap);
            return tag;
        }

        @Override
        public NPCFeatureType<HairFeatureType.HairFeature> getType() {
            return ModNPCLooks.HAIR.get();
        }
    }

    public record ExpressionFeature(TextureType iris, TextureType sclera, String eyebrow) {

        public static final Codec<ExpressionFeature> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        TextureType.CODEC.fieldOf("iris").forGetter(d -> d.iris),
                        TextureType.CODEC.fieldOf("sclera").forGetter(d -> d.sclera),
                        Codec.STRING.fieldOf("eyebrow").forGetter(d -> d.eyebrow)
                ).apply(inst, ExpressionFeature::new));
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
    }

    public enum ExpressionType {
        IRIS,
        SCLERA,
        EYEBROWS
    }
}