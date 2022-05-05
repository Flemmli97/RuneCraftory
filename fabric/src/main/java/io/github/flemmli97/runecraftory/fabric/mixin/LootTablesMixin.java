package io.github.flemmli97.runecraftory.fabric.mixin;

import com.google.gson.JsonElement;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.LootTableID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LootTables.class)
public class LootTablesMixin {

    @Shadow
    private Map<ResourceLocation, LootTable> tables;

    @Inject(method = "apply", at = @At("RETURN"))
    private void setIDs(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo info) {
        if (this.tables != null)
            this.tables.forEach((id, table) -> ((LootTableID) table).setLootTableId(id));
    }
}
