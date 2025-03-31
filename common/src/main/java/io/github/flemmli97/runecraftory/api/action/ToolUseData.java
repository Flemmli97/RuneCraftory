package io.github.flemmli97.runecraftory.api.action;

import net.minecraft.world.phys.HitResult;

public record ToolUseData(HitResult result, int charge) {
}
