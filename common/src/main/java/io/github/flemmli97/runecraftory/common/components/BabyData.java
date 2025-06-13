package io.github.flemmli97.runecraftory.common.components;

import net.minecraft.network.chat.Component;

import java.util.Optional;
import java.util.UUID;

public record BabyData(boolean male, Optional<Component> name, UUID father, UUID mother,) {
}
