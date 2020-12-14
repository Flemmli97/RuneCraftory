package com.flemmli97.runecraftory.api.datapack;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;

public class EffectSerializer implements JsonDeserializer<Effect>, JsonSerializer<Effect> {

    @Override
    public Effect deserialize(JsonElement el, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        ResourceLocation res = new ResourceLocation(el.getAsString());
        Effect effect = ForgeRegistries.POTIONS.getValue(res);
        if (!effect.getRegistryName().equals(res))
            throw new JsonParseException("No such effect: " + res.toString());
        return effect;
    }

    @Override
    public JsonElement serialize(Effect effect, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
        return new JsonPrimitive(effect.getRegistryName().toString());
    }
}