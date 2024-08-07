package io.github.flemmli97.runecraftory.client;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaceHolderComponent {

    private static final List<String> PLACEHOLDERS = new ArrayList<>();

    public static final String PLAYER = addReplacement("%player%");
    public static final String NPC = addReplacement("%npc%");

    private static String addReplacement(String pattern) {
        PLACEHOLDERS.add(pattern);
        return pattern;
    }

    /**
     * Parse the component by replacing the placeholder texts
     */
    public static Component parseDialogueComponent(Component component, Map<String, Object> replacements) {
        Component parse;
        if (component instanceof TranslatableComponent translatable) {
            String translation = NPCDialogueLanguageManager.INSTANCE
                    .getOrDefault(translatable.getKey());
            if (translation.equals(translatable.getKey())) {
                Language language = Language.getInstance();
                translation = language.getOrDefault(translatable.getKey());
            }
            List<Object> args = new ArrayList<>(List.of(translatable.getArgs()));
            for (String pattern : PLACEHOLDERS) {
                Object replacement = replacements.get(pattern);
                if (replacement != null) {
                    translation = translation.replace(pattern, "%" + (args.size() + 1) + "$s");
                    args.add(replacement);
                }
            }
            parse = new TranslatableComponent(translation, args.toArray());
        } else {
            parse = component.plainCopy();
        }
        List<Component> children = component.getSiblings().stream().map(c -> parseDialogueComponent(c, replacements)).toList();
        parse.getSiblings().addAll(children);
        return parse;
    }
}
