package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.client.NPCDialogueLanguageManager;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {

    @ModifyVariable(method = "onResourceManagerReload", at = @At("RETURN"))
    private List<LanguageInfo> injectDialogReloader(List<LanguageInfo> origin, ResourceManager resourceManager) {
        NPCDialogueLanguageManager.INSTANCE.onResourceManagerReload(resourceManager, origin);
        return origin;
    }
}
