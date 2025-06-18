package io.github.flemmli97.runecraftory.common.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ComponentUtils {

    public static MutableComponent ofBool(boolean bool) {
        return Component.translatable(bool ? "runecraftory.generic.yes" : "runecraftory.generic.no");
    }
}
