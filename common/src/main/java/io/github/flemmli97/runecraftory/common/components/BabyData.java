package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;
import java.util.UUID;

public record BabyData(boolean male, Optional<String> name, UUID father, UUID mother, Optional<Component> player) {

    public static BabyData DEFAULT = new BabyData(true, Optional.empty(), Util.NIL_UUID, Util.NIL_UUID, Optional.empty());

    public static final Codec<BabyData> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.BOOL.fieldOf("male").forGetter(BabyData::male),
                    Codec.STRING.optionalFieldOf("name").forGetter(BabyData::name),
                    UUIDUtil.CODEC.fieldOf("father").forGetter(BabyData::father),
                    UUIDUtil.CODEC.fieldOf("father").forGetter(BabyData::father),
                    ComponentSerialization.FLAT_CODEC.optionalFieldOf("player").forGetter(BabyData::player)
            ).apply(instance, BabyData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BabyData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL,
            BabyData::male, ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), BabyData::name, UUIDUtil.STREAM_CODEC, BabyData::father,
            UUIDUtil.STREAM_CODEC, BabyData::mother, ByteBufCodecs.optional(ComponentSerialization.STREAM_CODEC), BabyData::player, BabyData::new);
}
