package com.flemmli97.runecraftory.api.datapack;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;

public class AttributeSerializer implements JsonDeserializer<Attribute>, JsonSerializer<Attribute> {

    @Override
    public Attribute deserialize(JsonElement el, Type type, JsonDeserializationContext context) throws JsonParseException {
        ResourceLocation res = new ResourceLocation(el.getAsString());
        Attribute att = ForgeRegistries.ATTRIBUTES.getValue(res);
        if (!att.getRegistryName().equals(res))
            throw new JsonParseException("No such attribute: " + res.toString());
        return att;
    }

    @Override
    public JsonElement serialize(Attribute att, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(att.getRegistryName().toString());
    }
}
