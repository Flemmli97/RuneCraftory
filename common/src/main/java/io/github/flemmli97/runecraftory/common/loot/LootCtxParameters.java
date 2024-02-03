package io.github.flemmli97.runecraftory.common.loot;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.UUID;

public class LootCtxParameters {

    public static final LootContextParam<Player> INTERACTING_PLAYER = new LootContextParam<>(new ResourceLocation(RuneCraftory.MODID, "interacting_player"));
    public static final LootContextParam<UUID> UUID_CONTEXT = new LootContextParam<>(new ResourceLocation(RuneCraftory.MODID, "uuid_context"));
    public static final LootContextParamSet NPC_INTERACTION = LootContextParamSets.register(RuneCraftory.MODID, b -> b.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootCtxParameters.INTERACTING_PLAYER));
}
