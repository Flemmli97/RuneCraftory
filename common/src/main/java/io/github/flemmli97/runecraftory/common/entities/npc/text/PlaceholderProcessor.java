package io.github.flemmli97.runecraftory.common.entities.npc.text;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.BiFunction;

public record PlaceholderProcessor(BiFunction<String, String, Component> component, ChatFormatting... colors) {

    public PlaceholderProcessor(ChatFormatting... colors) {
        this((key, value) -> null, colors);
    }
}