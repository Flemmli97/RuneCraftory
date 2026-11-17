package io.github.flemmli97.runecraftory.common.entities.npc.text;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.BiFunction;
import java.util.function.Function;

public record PlaceholderProcessor(BiFunction<String, String, Component> component,
                                   Function<MutableComponent, MutableComponent> styles) {

    public PlaceholderProcessor(ChatFormatting... colors) {
        this((key, value) -> null, comp -> comp.withStyle(colors));
    }

    public PlaceholderProcessor(Function<MutableComponent, MutableComponent> styles) {
        this((key, value) -> null, styles);
    }

    public PlaceholderProcessor(BiFunction<String, String, Component> component) {
        this(component, Function.identity());
    }
}