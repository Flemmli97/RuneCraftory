package io.github.flemmli97.runecraftory.common.utils;

import com.google.gson.Gson;
import com.mojang.serialization.Codec;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class JsonCodecHelper {

    private static final Gson GSON = Deserializers.createConditionSerializer()
            .create();

    public static final Codec<EntityPredicate> ENTITY_PREDICATE_CODEC = CodecUtils.jsonCodecBuilder(EntityPredicate::serializeToJson, EntityPredicate::fromJson, "EntityPredicate");
    public static final Codec<NumberProvider> NUMER_PROVIDER_CODEC = CodecUtils.jsonCodecBuilder(GSON::toJsonTree, e -> GSON.fromJson(e, NumberProvider.class), "NumberProvider");
}
