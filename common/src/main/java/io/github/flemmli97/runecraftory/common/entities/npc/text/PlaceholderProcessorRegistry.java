package io.github.flemmli97.runecraftory.common.entities.npc.text;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PlaceholderProcessorRegistry {

    private static final Map<String, PlaceholderProcessor> REGISTRY = new HashMap<>();

    public static final int HIGHLIGHT_BLUE = 0x4080d0;
    public static final int HIGHLIGHT_RED = 0xad212d;

    public static final String PLAYER = registerDynamicPlaceholder("player", ChatFormatting.AQUA);
    public static final String NPC = registerDynamicPlaceholder("npc", ChatFormatting.AQUA);
    public static final String FAVORITE = registerHighlightColor("favorite", HIGHLIGHT_BLUE);
    public static final String LIKE = registerHighlightColor("like", HIGHLIGHT_BLUE);
    public static final String DISLIKE = registerHighlightColor("dislike", HIGHLIGHT_RED);
    public static final String HATE = registerHighlightColor("hate", HIGHLIGHT_RED);
    public static final String BIRTHDAY = registerHighlightColor("birthday", HIGHLIGHT_BLUE);
    public static final Function<ResourceLocation, String> SPEAKER = registerDynamicPlaceholder("speaker", ResourceLocation::toString, ChatFormatting.AQUA);
    public static final String QUEST_GIVER = registerDynamicPlaceholder("quest_giver", ChatFormatting.AQUA);
    public static final Function<String, String> QUEST_NPC_TARGET = registerDynamicPlaceholder("quest.target_npc", Function.identity(), ChatFormatting.AQUA);
    public static final Function<String, String> QUEST_PROGRESS = registerDynamicPlaceholder("quest.progress", Function.identity(), HIGHLIGHT_BLUE);

    public static final String KEY = registerStaticPlaceholder((k, v) -> v == null ? null : Component.keybind(v).withStyle(ChatFormatting.DARK_PURPLE), "key");
    public static final String ITEM = registerStaticPlaceholder((k, v) -> {
        if (v == null)
            return null;
        try {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(v));
            if (item == Items.AIR)
                return null;
            return Component.translatable(item.getDescriptionId()).withColor(HIGHLIGHT_BLUE);
        } catch (ResourceLocationException ex) {
            RuneCraftory.LOGGER.error("Failed to parse item {}", v, ex);
            return null;
        }
    }, "item");
    public static final String ENTITY = registerStaticPlaceholder((k, v) -> {
        if (v == null)
            return null;
        try {
            ResourceLocation id = ResourceLocation.parse(v);
            EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(id);
            if (entityType == EntityType.PIG && !id.toString().equals("minecraft:pig"))
                return null;
            return Component.translatable(entityType.getDescriptionId()).withColor(HIGHLIGHT_BLUE);
        } catch (ResourceLocationException ex) {
            RuneCraftory.LOGGER.error("Failed to parse entity {}", v, ex);
            return null;
        }
    }, "entity");

    public static synchronized String registerDynamicPlaceholder(String pattern, ChatFormatting... styles) {
        REGISTRY.put(pattern, new PlaceholderProcessor(styles));
        return pattern;
    }

    public static synchronized String registerHighlightColor(String pattern, int color) {
        REGISTRY.put(pattern, new PlaceholderProcessor(comp -> comp.withColor(color)));
        return pattern;
    }

    public static synchronized <T> Function<T, String> registerDynamicPlaceholder(String prefix, Function<T, String> func, int color) {
        REGISTRY.put(prefix, new PlaceholderProcessor(comp -> comp.withColor(color)));
        return val -> prefix + PlaceHolderComponent.SEPARATOR + func.apply(val);
    }

    public static synchronized <T> Function<T, String> registerDynamicPlaceholder(String prefix, Function<T, String> func, ChatFormatting... styles) {
        REGISTRY.put(prefix, new PlaceholderProcessor(styles));
        return val -> prefix + PlaceHolderComponent.SEPARATOR + func.apply(val);
    }

    public static synchronized String registerStaticPlaceholder(BiFunction<String, String, Component> component, String pattern) {
        REGISTRY.put(pattern, new PlaceholderProcessor(component));
        return pattern;
    }

    @Nullable
    public static PlaceholderProcessor get(String tag) {
        return REGISTRY.get(tag);
    }
}
