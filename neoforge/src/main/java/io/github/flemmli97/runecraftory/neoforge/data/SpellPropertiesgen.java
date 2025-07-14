package io.github.flemmli97.runecraftory.neoforge.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.api.datapack.SpellProperties;
import io.github.flemmli97.runecraftory.common.datapack.manager.SpellPropertiesManager;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public record SpellPropertiesgen(PackOutput packOutput) implements DataProvider {

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return CompletableFuture.allOf(RuneCraftorySpells.DEFAULT_PROPERTIES.entrySet().stream().map((entry) -> {
            Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(entry.getKey().getID().getNamespace() + "/" + SpellPropertiesManager.DIRECTORY + "/" + entry.getKey().getID().getPath() + ".json");
            JsonElement obj = SpellProperties.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
            return DataProvider.saveStable(cache, obj, path);
        }).toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "SpellProperties";
    }
}