package io.github.flemmli97.runecraftory.api.datapack.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public record NPCLook(NPCData.Gender gender, @Nullable String playerSkin, int weight,
                      Map<NPCFeatureType<?>, NPCFeature.NPCFeatureHolder<?>> additionalFeatures,
                      List<ConditionalFeatures> conditionalFeatures) {

    public static final ReloadableHolder<NPCLook> DEFAULT = new ReloadableHolder<>(RuneCraftory.modRes("default_look"),
            new NPCLook(NPCData.Gender.MALE, null, 0, Map.of(), List.of()));

    public static final Codec<NPCLook> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(Codec.STRING.optionalFieldOf("player_skin").forGetter(d -> Optional.ofNullable(d.playerSkin())),
                    CodecUtils.stringEnumCodec(NPCData.Gender.class, NPCData.Gender.UNDEFINED).fieldOf("gender").forGetter(NPCLook::gender),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("weight").forGetter(NPCLook::weight),
                    NPCFeature.CODEC.listOf().fieldOf("additional_features").forGetter(d -> List.copyOf(d.additionalFeatures.values())),
                    ConditionalFeatures.CODEC.listOf().fieldOf("conditional_features").forGetter(NPCLook::conditionalFeatures)
            ).apply(inst, (skin, gender, weight, features, conditionals) -> new NPCLook(gender, skin.orElse(null), weight, features
                    .stream().collect(Collectors.toMap(
                            NPCFeature.NPCFeatureHolder::getType,
                            h -> h,
                            (e1, e2) -> e1,
                            HashMap::new
                    )), conditionals)));

    public static final StreamCodec<RegistryFriendlyByteBuf, NPCLook> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public NPCLook decode(RegistryFriendlyByteBuf buf) {
            String skin = null;
            if (buf.readBoolean())
                skin = buf.readUtf();
            return new NPCLook(buf.readEnum(NPCData.Gender.class), skin, buf.readInt(), Map.of(), List.of());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, NPCLook look) {
            buf.writeBoolean(look.playerSkin != null);
            if (look.playerSkin != null)
                buf.writeUtf(look.playerSkin);
            buf.writeEnum(look.gender());
            buf.writeInt(look.weight());
        }
    };

    public NPCLook(NPCData.Gender gender, @Nullable String playerSkin, int weight, Map<NPCFeatureType<?>, NPCFeature.NPCFeatureHolder<?>> additionalFeatures) {
        this(gender, playerSkin, weight, additionalFeatures, List.of());
    }

    public record ConditionalFeatures(NPCFeature.NPCFeatureHolder<?> feature, EntityPredicate predicate) {

        public static final Codec<ConditionalFeatures> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(NPCFeature.CODEC.fieldOf("feature").forGetter(ConditionalFeatures::feature),
                        EntityPredicate.CODEC.fieldOf("predicate").forGetter(ConditionalFeatures::predicate)
                ).apply(inst, ConditionalFeatures::new));

    }
}
