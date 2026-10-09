package io.github.flemmli97.runecraftory.common.entities.npc;

import io.github.flemmli97.runecraftory.client.NPCDialogueLanguageManager;
import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceHolderComponent {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%([A-z0-9_:/.-]*)%");

    private static final char SEPARATOR = ':';

    private static final Map<String, ChatFormatting[]> PLACEHOLDER_STYLES = new HashMap<>();

    public static final String PLAYER = addReplacement("player", ChatFormatting.AQUA);
    public static final String NPC = addReplacement("npc", ChatFormatting.AQUA);
    public static final String FAVORITE = addReplacement("favorite", ChatFormatting.BLUE);
    public static final String LIKE = addReplacement("like", ChatFormatting.BLUE);
    public static final String DISLIKE = addReplacement("dislike", ChatFormatting.RED);
    public static final String HATE = addReplacement("hate", ChatFormatting.RED);
    public static final Function<String, String> QUEST_NPC_TARGET = addReplacement("quest.target_npc", Function.identity(), ChatFormatting.AQUA);

    private static String addReplacement(String pattern, ChatFormatting... styles) {
        PLACEHOLDER_STYLES.put(pattern, styles);
        return pattern;
    }

    private static <T> Function<T, String> addReplacement(String prefix, Function<T, String> func, ChatFormatting... styles) {
        PLACEHOLDER_STYLES.put(prefix, styles);
        return val -> prefix + SEPARATOR + func.apply(val);
    }

    /**
     * Parse the component by replacing the placeholder texts
     */
    public static Component parseDialogueComponent(Component component, Map<String, Component> replacements) {
        Component parse;
        if (component.getContents() instanceof TranslatableContents translatable) {
            String translation = NPCDialogueLanguageManager.INSTANCE
                    .getOrDefault(translatable.getKey());
            if (translation.equals(translatable.getKey())) {
                Language language = Language.getInstance();
                translation = language.getOrDefault(translatable.getKey());
            }
            List<Object> args = new ArrayList<>(List.of(translatable.getArgs()));
            Matcher matcher = PLACEHOLDER_PATTERN.matcher(translation);
            while (matcher.find()) {
                String pattern = matcher.group(1);
                Component replacement = replacements.get(pattern);
                if (replacement != null) {
                    translation = translation.replace("%" + pattern + "%", "%" + (args.size() + 1) + "$s");
                    if (replacement instanceof MutableComponent mut) {
                        int dot = pattern.indexOf(SEPARATOR);
                        String prefix = dot != -1 ? pattern.substring(0, dot) : pattern;
                        ChatFormatting[] styles = PLACEHOLDER_STYLES.get(prefix);
                        if (styles != null) {
                            mut.withStyle(styles);
                        }
                    }
                    args.add(replacement);
                }
            }
            parse = Component.translatable(translation, args.toArray());
        } else {
            parse = component.plainCopy();
        }
        List<Component> children = component.getSiblings().stream().map(c -> parseDialogueComponent(c, replacements)).toList();
        parse.getSiblings().addAll(children);
        return parse;
    }
}
