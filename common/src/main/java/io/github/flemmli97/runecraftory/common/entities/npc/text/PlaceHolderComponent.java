package io.github.flemmli97.runecraftory.common.entities.npc.text;

import com.mojang.datafixers.util.Either;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.NPCDialogueLanguageManager;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceHolderComponent {

    private static final String PATH_OR_NAMESPACE = "([A-Za-z_.-]+)(?::([A-Za-z0-9_/.-]+))?";
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%(" + PATH_OR_NAMESPACE + ")%");
    private static final Pattern TAG_PATTERN = Pattern.compile("<(/?)" + PATH_OR_NAMESPACE + ">");

    private static final Pattern FORMAT_PATTERN = Pattern.compile("%(s|$)");

    public static final char SEPARATOR = ':';

    /**
     * Parse the component by replacing the placeholder texts
     */
    public static Component parseDialogueComponent(Component component, Map<String, Component> placeholders, Map<String, Component> participants) {
        String text = null;
        List<Object> args = null;
        if (component.getContents() instanceof TranslatableContents translatable) {
            text = NPCDialogueLanguageManager.INSTANCE
                    .getOrDefault(translatable.getKey());
            if (text.equals(translatable.getKey())) {
                Language language = Language.getInstance();
                text = language.getOrDefault(translatable.getKey());
            }
            text = convertArguments(text);
            args = new ArrayList<>(List.of(translatable.getArgs()));
        } else if (component.getContents() instanceof PlainTextContents.LiteralContents literal) {
            text = literal.text();
        }
        Component parsed;
        if (text == null) {
            parsed = component.plainCopy();
        } else {
            Map<String, Component> placeholderMap = new HashMap<>();
            placeholderMap.putAll(placeholders);
            placeholderMap.putAll(participants);
            parsed = parseTags(text, component.getStyle(), placeholderMap, args);
        }
        List<Component> children = component.getSiblings().stream().map(c -> parseDialogueComponent(c, placeholders, participants)).toList();
        parsed.getSiblings().addAll(children);
        return parsed;
    }

    /**
     * Converts implicit args to explicit ones
     */
    private static String convertArguments(String text) {
        Matcher matcher = FORMAT_PATTERN.matcher(text);
        StringBuilder result = new StringBuilder();
        int implicitIndex = 1;
        int cursor = 0;
        while (matcher.find()) {
            result.append(text, cursor, matcher.start());
            if (matcher.group(1) == null) {
                result.append("%").append(implicitIndex++).append("$s");
            }
            cursor = matcher.end();
        }
        result.append(text.substring(cursor));
        return result.toString();
    }

    private static Component parseTags(String text, Style style, Map<String, Component> placeholderMap, @Nullable List<Object> args) {
        List<ComponentStack> stacks = new ArrayList<>();
        Matcher matcher = TAG_PATTERN.matcher(text);
        int cursor = 0;
        while (matcher.find()) {
            String content = text.substring(cursor, matcher.start());
            String tag = matcher.group(2);
            String value = matcher.group(3);
            if (!content.isEmpty()) {
                TagProcessor processor = TagProcessorRegistry.get(tag);
                if (processor != null)
                    content = processor.processTag(tag, value, content);
                MutableComponent component = content.isBlank() ? Component.literal(content) : parsePlaceholders(content, placeholderMap, args);
                if (stacks.isEmpty()) {
                    ComponentStack stack = new ComponentStack("", null, style);
                    stack.children().add(Either.left(component));
                    stacks.add(stack);
                } else {
                    ComponentStack last = stacks.getLast();
                    component.setStyle(last.style());
                    stacks.getLast().children().add(Either.left(component));
                }
            }
            boolean close = !matcher.group(1).isEmpty();
            if (close) {
                if (stacks.isEmpty() || !stacks.getLast().tag().equals(tag)) {
                    RuneCraftory.LOGGER.error("Component placeholder processor error: Mismatched tag containers");
                    return args == null ? Component.literal(text) : Component.translatable(text, args);
                }
                stacks.removeLast();
            } else {
                TagProcessor processor = TagProcessorRegistry.get(tag);
                Style componentStyle = stacks.isEmpty() ? style : stacks.getLast().style();
                if (processor != null)
                    componentStyle = processor.processStyle(tag, value, componentStyle);
                ComponentStack stack = new ComponentStack(tag, value, componentStyle);
                if (!stacks.isEmpty()) {
                    stacks.getLast().children().add(Either.right(stack));
                }
                stacks.add(stack);
            }
            cursor = matcher.end();
        }
        String content = text.substring(cursor);
        if (!content.isEmpty()) {
            MutableComponent component = content.isBlank() ? Component.literal(content) : parsePlaceholders(content, placeholderMap, args);
            if (stacks.isEmpty()) {
                return component;
            } else {
                ComponentStack last = stacks.getLast();
                component.setStyle(last.style());
                stacks.getLast().children().add(Either.left(component));
            }
        }
        if (stacks.isEmpty()) {
            return args == null ? Component.literal(text) : Component.translatable(text, args);
        }
        return stacks.getLast().toComponent();
    }

    private static MutableComponent parsePlaceholders(String text,
                                                      Map<String, Component> replacements,
                                                      @Nullable List<Object> args) {
        Object2IntMap<String> lookup = new Object2IntOpenHashMap<>();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);
        List<Object> arguments = args == null ? null : new ArrayList<>(args);
        while (matcher.find()) {
            String pattern = matcher.group(1);
            String namespace = matcher.group(2);
            Component replacement = replacements.get(pattern);
            PlaceholderProcessor processor = PlaceholderProcessorRegistry.get(namespace);
            if (replacement == null && processor != null) {
                replacement = processor.component().apply(namespace, matcher.group(3));
            }
            if (replacement != null) {
                if (arguments == null)
                    arguments = new ArrayList<>();
                int argIndex = arguments.size() + 1;
                int idx = lookup.computeIfAbsent(pattern, k -> argIndex);
                text = text.replace("%" + pattern + "%", "%" + idx + "$s");
                if (processor != null) {
                    replacement = replacement.copy().withStyle(processor.colors());
                }
                if (argIndex == idx)
                    arguments.add(replacement);
            }
        }
        return arguments == null || arguments.isEmpty() ? Component.literal(text) : Component.translatable(text, arguments.toArray());
    }

    private record ComponentStack(String tag, @Nullable String tagValue, Style style,
                                  List<Either<MutableComponent, ComponentStack>> children) {

        public ComponentStack(String tag, @Nullable String tagValue, Style style) {
            this(tag, tagValue, style, new ArrayList<>());
        }

        public MutableComponent toComponent() {
            if (this.children.isEmpty()) {
                return Component.empty();
            }
            MutableComponent component = this.children().getFirst().map(comp -> comp.setStyle(this.style()), ComponentStack::toComponent);
            component.setStyle(this.style());
            for (int i = 1; i < this.children().size(); i++) {
                Either<MutableComponent, ComponentStack> stack = this.children().get(i);
                component.append(stack.map(comp -> comp.setStyle(this.style()), ComponentStack::toComponent));
            }
            return component;
        }
    }
}
