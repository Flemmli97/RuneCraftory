package io.github.flemmli97.runecraftory.forge.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.api.datapack.EntityProperties;
import io.github.flemmli97.runecraftory.api.datapack.GsonInstances;
import io.github.flemmli97.runecraftory.common.datapack.manager.MonsterPropertiesManager;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public record MobPropertiesgen(DataGenerator gen) implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void run(HashCache cache) {
        ModEntities.getDefaultMobProperties().forEach((res, builder) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + res.getNamespace() + "/" + MonsterPropertiesManager.DIRECTORY + "/" + res.getPath() + ".json");
            try {
                JsonElement obj = EntityProperties.CODEC.encodeStart(JsonOps.INSTANCE, builder.build())
                        .getOrThrow(false, LOGGER::error);
                DataProvider.save(GsonInstances.ATTRIBUTE_EFFECTS, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save entity properties {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "EntityProperties";
    }
}