package io.github.flemmli97.runecraftory.mixin;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;

@Mixin(LootContext.Builder.class)
public class LootContextBuilderMixin {

    /**
     * Disables the vanilla verification check of having the provided parameter set needing to match the current parameters
     */
    @ModifyVariable(method = "create", at = @At(value = "INVOKE_ASSIGN",
            target = "Lcom/google/common/collect/Sets;difference(Ljava/util/Set;Ljava/util/Set;)Lcom/google/common/collect/Sets$SetView;",
            remap = false, ordinal = 0), ordinal = 0)
    private Set<LootContextParam<?>> verificationRemove(Set<LootContextParam<?>> origin) {
        return Set.of();
    }
}
