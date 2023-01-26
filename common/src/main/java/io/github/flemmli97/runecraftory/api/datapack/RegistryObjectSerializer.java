package io.github.flemmli97.runecraftory.api.datapack;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.flemmli97.tenshilib.platform.registry.SimpleRegistryWrapper;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Type;
import java.util.function.Supplier;

public record RegistryObjectSerializer<V>(
        Supplier<SimpleRegistryWrapper<V>> registry) implements JsonDeserializer<V>, JsonSerializer<V> {

    @Override
    public V deserialize(JsonElement el, Type type, JsonDeserializationContext context) throws JsonParseException {
        ResourceLocation res = new ResourceLocation(el.getAsString());
        V entry = this.registry.get().getFromId(res);
        if (!this.registry.get().getIDFrom(entry).equals(res))
            throw new JsonParseException("No such registry value: " + res);
        return entry;
    }

    @Override
    public JsonElement serialize(V obj, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(this.registry.get().getIDFrom(obj).toString());
    }
}
