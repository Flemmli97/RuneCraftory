package com.flemmli97.runecraftory.api.datapack;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Type;

public class RegistryObjectSerializer<V extends IForgeRegistryEntry<V>> implements JsonDeserializer<V>, JsonSerializer<V> {

    private final IForgeRegistry<V> registry;

    public RegistryObjectSerializer(IForgeRegistry<V> registry) {
        this.registry = registry;
    }

    @Override
    public V deserialize(JsonElement el, Type type, JsonDeserializationContext context) throws JsonParseException {
        ResourceLocation res = new ResourceLocation(el.getAsString());
        V entry = registry.getValue(res);
        if (!entry.getRegistryName().equals(res))
            throw new JsonParseException("No such registry value: " + res.toString());
        return entry;
    }

    @Override
    public JsonElement serialize(V obj, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(obj.getRegistryName().toString());
    }
}
