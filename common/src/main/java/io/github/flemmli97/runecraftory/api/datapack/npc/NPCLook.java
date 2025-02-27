package io.github.flemmli97.runecraftory.api.datapack.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureHolder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public record NPCLook(NPCData.Gender gender, @Nullable String playerSkin, int weight,
                      Map<NPCFeatureType<?>, NPCFeatureHolder<?>> additionalFeatures) {

    public static final ResourceLocation DEFAULT_LOOK_ID = new ResourceLocation(RuneCraftory.MODID, "default_look");
    public static final NPCLook DEFAULT_LOOK = new NPCLook(NPCData.Gender.MALE, null, 0, Map.of());

    public static final Codec<NPCLook> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(Codec.STRING.optionalFieldOf("player_skin").forGetter(d -> Optional.ofNullable(d.playerSkin)),
                    CodecUtils.stringEnumCodec(NPCData.Gender.class, NPCData.Gender.UNDEFINED).fieldOf("gender").forGetter(d -> d.gender),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("weight").forGetter(d -> d.weight),
                    NPCFeature.CODEC.listOf().fieldOf("additional_features").forGetter(d -> List.copyOf(d.additionalFeatures.values()))
            ).apply(inst, (skin, gender, weight, features) -> new NPCLook(gender, skin.orElse(null), weight, features
                    .stream().collect(Collectors.toMap(
                            NPCFeatureHolder::getType,
                            h -> h,
                            (e1, e2) -> e1,
                            HashMap::new
                    )))));

    public static NPCLook fromBuffer(FriendlyByteBuf buf) {
        String skin = null;
        if (buf.readBoolean())
            skin = buf.readUtf();
        return new NPCLook(buf.readEnum(NPCData.Gender.class), skin, buf.readInt(), Map.of());
    }

    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeBoolean(this.playerSkin != null);
        if (this.playerSkin != null)
            buf.writeUtf(this.playerSkin);
        buf.writeEnum(this.gender());
        buf.writeInt(this.weight());
    }
}
