package io.github.flemmli97.runecraftory.common.loot;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

import java.util.UUID;

public class LootCtxParameters {

    public static final LootContextParam<Player> INTERACTING_PLAYER = new LootContextParam<>(new ResourceLocation(RuneCraftory.MODID, "interacting_player"));
    public static final LootContextParam<UUID> UUID_CONTEXT = new LootContextParam<>(new ResourceLocation(RuneCraftory.MODID, "uuid_context"));

}
