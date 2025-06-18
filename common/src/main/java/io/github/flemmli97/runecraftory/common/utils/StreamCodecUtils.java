package io.github.flemmli97.runecraftory.common.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.HashMap;
import java.util.Map;

public class StreamCodecUtils {

    public static final StreamCodec<RegistryFriendlyByteBuf, Map<Holder<Attribute>, Double>> ATTRIBUTE_CODEC = ByteBufCodecs
            .map(HashMap::new, ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE), ByteBufCodecs.DOUBLE);
}
