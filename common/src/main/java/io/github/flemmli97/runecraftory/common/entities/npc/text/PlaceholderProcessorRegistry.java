package io.github.flemmli97.runecraftory.common.entities.npc.text;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PlaceholderProcessorRegistry {

    private static final Map<String, PlaceholderProcessor> REGISTRY = new HashMap<>();

    public static final String PLAYER = registerDynamicPlaceholder("player", ChatFormatting.AQUA);
    public static final String NPC = registerDynamicPlaceholder("npc", ChatFormatting.AQUA);
    public static final String FAVORITE = registerDynamicPlaceholder("favorite", ChatFormatting.BLUE);
    public static final String LIKE = registerDynamicPlaceholder("like", ChatFormatting.BLUE);
    public static final String DISLIKE = registerDynamicPlaceholder("dislike", ChatFormatting.RED);
    public static final String HATE = registerDynamicPlaceholder("hate", ChatFormatting.RED);
    public static final Function<ResourceLocation, String> SPEAKER = registerDynamicPlaceholder("speaker", ResourceLocation::toString, ChatFormatting.AQUA);
    public static final Function<String, String> QUEST_NPC_TARGET = registerDynamicPlaceholder("quest.target_npc", Function.identity(), ChatFormatting.AQUA);
    public static final Function<String, String> QUEST_PROGRESS = registerDynamicPlaceholder("quest.progress", Function.identity(), ChatFormatting.AQUA);

    public static final String KEY = registerStaticPlaceholder((k, v) -> v == null ? null : Component.keybind(v), "key", ChatFormatting.DARK_PURPLE);

    public static synchronized String registerDynamicPlaceholder(String pattern, ChatFormatting... styles) {
        REGISTRY.put(pattern, new PlaceholderProcessor(styles));
        return pattern;
    }

    public static synchronized <T> Function<T, String> registerDynamicPlaceholder(String prefix, Function<T, String> func, ChatFormatting... styles) {
        REGISTRY.put(prefix, new PlaceholderProcessor(styles));
        return val -> prefix + PlaceHolderComponent.SEPARATOR + func.apply(val);
    }

    public static synchronized String registerStaticPlaceholder(BiFunction<String, String, Component> component, String pattern, ChatFormatting... styles) {
        REGISTRY.put(pattern, new PlaceholderProcessor(component, styles));
        return pattern;
    }

    @Nullable
    public static PlaceholderProcessor get(String tag) {
        return REGISTRY.get(tag);
    }
}
