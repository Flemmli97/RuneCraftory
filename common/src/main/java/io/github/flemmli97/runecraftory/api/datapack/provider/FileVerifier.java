package io.github.flemmli97.runecraftory.api.datapack.provider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public interface FileVerifier {

    boolean exists(ResourceLocation loc, PackType packType, String prefix);

    void track(ResourceLocation loc, PackType packType, String prefix);
}
