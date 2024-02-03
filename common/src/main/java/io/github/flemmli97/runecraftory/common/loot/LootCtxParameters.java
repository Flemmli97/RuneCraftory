package io.github.flemmli97.runecraftory.common.loot;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.UUID;

public class LootCtxParameters {
    public static final LootContextParam<UUID> UUID_CONTEXT = new LootContextParam<>(new ResourceLocation(RuneCraftory.MODID, "uuid_context"));

    public static final LootContextParamSet NPC_INTERACTION = LootContextParamSets.register(RuneCraftory.MODID + ":npc_interaction", b -> b.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootCtxParameters.UUID_CONTEXT));
    public static final LootContextParamSet MONSTER_INTERACTION = LootContextParamSets.register(RuneCraftory.MODID + ":monster_interaction", b ->
            b.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN)
                    .optional(LootCtxParameters.UUID_CONTEXT).optional(LootContextParams.TOOL));
}
