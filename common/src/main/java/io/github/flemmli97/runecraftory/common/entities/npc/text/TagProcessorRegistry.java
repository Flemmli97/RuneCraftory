package io.github.flemmli97.runecraftory.common.entities.npc.text;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TagProcessorRegistry {

    private static final Map<String, TagProcessor> REGISTRY = new HashMap<>();

    public static final String BOLD = registerSerializer("b", new TagProcessor.SimpleStyleProcessor(style -> style.withBold(true)));
    public static final String BOLD2 = registerSerializer("bold", get(BOLD));
    public static final String ITALICS = registerSerializer("i", new TagProcessor.SimpleStyleProcessor(style -> style.withItalic(true)));
    public static final String ITALICS2 = registerSerializer("italics", get(ITALICS));
    public static final String UNDERLINE = registerSerializer("u", new TagProcessor.SimpleStyleProcessor(style -> style.withUnderlined(true)));
    public static final String UNDERLINE2 = registerSerializer("underline", get(UNDERLINE));
    public static final String OBFUSCATED = registerSerializer("o", new TagProcessor.SimpleStyleProcessor(style -> style.withObfuscated(true)));
    public static final String OBFUSCATED2 = registerSerializer("obfuscated", get(OBFUSCATED));
    public static final String STRIKETHROUGH = registerSerializer("s", new TagProcessor.SimpleStyleProcessor(style -> style.withStrikethrough(true)));
    public static final String STRIKETHROUGH2 = registerSerializer("strikethrough", get(STRIKETHROUGH));
    public static final String RESET = registerSerializer("r", new TagProcessor.SimpleStyleProcessor(style -> style.withStrikethrough(true)));
    public static final String RESET2 = registerSerializer("reset", get(RESET));

    public static final Map<ChatFormatting, String> COLORS = colors();
    public static final String COLOR = registerSerializer("c", new TagProcessor() {
        @Override
        public Style processStyle(String tag, @Nullable String tagValue, Style style) {
            try {
                if (tagValue == null)
                    return style;
                int color = Integer.parseInt(tagValue.replace("0x", "")
                        .replaceFirst("#", ""), 16);
                return style.withColor(color);
            } catch (NumberFormatException e) {
                RuneCraftory.LOGGER.error("Invalid color value: {}", tagValue);
                return style;
            }
        }
    });
    public static final String COLOR2 = registerSerializer("color", get(COLOR));

    private static Map<ChatFormatting, String> colors() {
        ImmutableMap.Builder<ChatFormatting, String> colors = new ImmutableMap.Builder<>();
        for (ChatFormatting color : ChatFormatting.values()) {
            if (color.isColor()) {
                colors.put(color, registerSerializer(color.getSerializedName().toLowerCase(Locale.ROOT), new TagProcessor.SimpleStyleProcessor(style -> style.withColor(color))));
            }
        }
        return colors.build();
    }

    public static synchronized String registerSerializer(String tag, TagProcessor tagProcessor) {
        if (REGISTRY.containsKey(tag))
            throw new IllegalStateException("Processor for " + tag + " already registered");
        REGISTRY.put(tag, tagProcessor);
        return tag;
    }

    @Nullable
    public static TagProcessor get(String tag) {
        return REGISTRY.get(tag);
    }
}
