package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.LootTableTrigger;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LootTableTrigger.class)
public abstract class RandomizableCBEMixin {

    @Inject(method = "trigger", at = @At("HEAD"))
    private void onLootTableUnpack(ServerPlayer player, ResourceKey<LootTable> lootTable, CallbackInfo info) {
        if ((Object) this == CriteriaTriggers.GENERATE_LOOT)
            EntityCalls.onLootTableBlockGen(player);
    }
}
