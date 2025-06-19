package io.github.flemmli97.runecraftory.neoforge.data;

import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.NPCDialogueLanguageManager;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * Same as LanguageProvider but with a linked hashmap
 */
public class NPCDialogLangGen implements DataProvider {

    private final PackOutput packOutput;
    private final String modid;
    private final String locale;
    private final NPCDataGen npcDataGen;

    public NPCDialogLangGen(PackOutput packOutput, @Nullable NPCDataGen npcDataGen) {
        this.packOutput = packOutput;
        this.modid = RuneCraftory.MODID;
        this.locale = "en_us";
        this.npcDataGen = npcDataGen;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        if (this.npcDataGen != null) {
            return CompletableFuture.allOf(this.npcDataGen.dialogueTranslations.entrySet().stream().map(entry -> {
                Path path = this.packOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modid + "/" + NPCDialogueLanguageManager.DIRECTORY + "/" + entry.getKey() + "/" + this.locale + ".json");
                JsonObject json = new JsonObject();
                entry.getValue().forEach(json::addProperty);
                return DataProvider.saveStable(cache, json, path);
            }).toArray(CompletableFuture[]::new));
        }
        return CompletableFuture.allOf();
    }

    @Override
    public String getName() {
        return "NPC Dialogue Translation: " + this.locale;
    }
}
