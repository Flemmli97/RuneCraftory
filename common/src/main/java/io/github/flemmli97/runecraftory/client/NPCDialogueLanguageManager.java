package io.github.flemmli97.runecraftory.client;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPCDialogueLanguageManager {

    public static final NPCDialogueLanguageManager INSTANCE = new NPCDialogueLanguageManager();

    public static final String DIRECTORY = "lang/npc_conversations";

    private Map<String, String> translations = new HashMap<>();

    private NPCDialogueLanguageManager() {
    }

    private static Map<String, String> loadFrom(ResourceManager resourceManager, List<LanguageInfo> languageInfo) {
        Map<String, Map<String, String>> translations = new HashMap<>();
        Map<String, String> result = new HashMap<>();
        List<String> codes = languageInfo.stream().map(LanguageInfo::getCode).toList();
        for (ResourceLocation res : resourceManager.listResources(DIRECTORY, p -> p.endsWith(".json"))) {
            String[] dirs = res.getPath().split("/");
            String lang = dirs[dirs.length - 1].replace(".json", "");
            if (!codes.contains(lang))
                continue;
            Map<String, String> map = new HashMap<>();
            try {
                for (Resource resource : resourceManager.getResources(res)) {
                    try (InputStream inputStream = resource.getInputStream()) {
                        Language.loadFromJson(inputStream, map::put);
                    }
                }
            } catch (IOException iOException) {
                RuneCraftory.logger.warn("Failed to load dialog translations for language {}", lang, iOException);
            }
            translations.computeIfAbsent(lang, k -> new HashMap<>()).putAll(map);
        }
        for (String code : codes) {
            result.putAll(translations.getOrDefault(code, Map.of()));
        }
        return ImmutableMap.copyOf(result);
    }

    public void onResourceManagerReload(ResourceManager resourceManager, List<LanguageInfo> infos) {
        this.translations = loadFrom(resourceManager, infos);
    }

    public String getOrDefault(String key) {
        return this.translations.getOrDefault(key, key);
    }
}

