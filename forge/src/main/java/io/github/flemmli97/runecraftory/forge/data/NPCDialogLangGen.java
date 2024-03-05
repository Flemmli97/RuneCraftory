package io.github.flemmli97.runecraftory.forge.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.NPCDialogueLanguageManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

/**
 * Same as LanguageProvider but with a linked hashmap
 */
public class NPCDialogLangGen implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final DataGenerator gen;
    private final String modid;
    private final String locale;
    private final NPCDataGen npcDataGen;

    public NPCDialogLangGen(DataGenerator gen, @Nullable NPCDataGen npcDataGen) {
        this.gen = gen;
        this.modid = RuneCraftory.MODID;
        this.locale = "en_us";
        this.npcDataGen = npcDataGen;
    }

    @Override
    public void run(HashCache cache) throws IOException {
        for (Map.Entry<String, Map<String, String>> entry : this.npcDataGen.translations.entrySet()) {
            this.save(cache, entry.getValue(), this.gen.getOutputFolder().resolve("assets/" + this.modid + "/" + NPCDialogueLanguageManager.DIRECTORY + "/" + entry.getKey() + "/" + this.locale + ".json"));
        }
    }

    @Override
    public String getName() {
        return "NPC Dialogue Translation: " + this.locale;
    }

    @SuppressWarnings("deprecation")
    private void save(HashCache cache, Object object, Path target) throws IOException {
        String data = GSON.toJson(object);
        data = JavaUnicodeEscaper.outsideOf(0, 0x7f).translate(data); // Escape unicode after the fact so that it's not double escaped by GSON
        String hash = DataProvider.SHA1.hashUnencodedChars(data).toString();
        if (!Objects.equals(cache.getHash(target), hash) || !Files.exists(target)) {
            Files.createDirectories(target.getParent());

            try (BufferedWriter bufferedwriter = Files.newBufferedWriter(target)) {
                bufferedwriter.write(data);
            }
        }

        cache.putNew(target, hash);
    }
}
