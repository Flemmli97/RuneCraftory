package io.github.flemmli97.runecraftory.common.loot;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.mixin.LootContextParamSetsAccessor;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.UUID;
import java.util.function.Consumer;

public class LootCtxParameters {

    public static final LootContextParam<UUID> UUID_CONTEXT = new LootContextParam<>(RuneCraftory.modRes("uuid_context"));
    public static final LootContextParam<Integer> ITEM_LEVEL_CONTEXT = new LootContextParam<>(RuneCraftory.modRes("item_level_context"));

    public static final LootContextParamSet NPC_INTERACTION = register("npc_interaction", b -> b.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootCtxParameters.UUID_CONTEXT));
    public static final LootContextParamSet MONSTER_INTERACTION = register("monster_interaction", b ->
            b.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN)
                    .optional(LootCtxParameters.UUID_CONTEXT).optional(LootContextParams.TOOL));

    private static LootContextParamSet register(String path, Consumer<LootContextParamSet.Builder> consumer) {
        LootContextParamSet.Builder builder = new LootContextParamSet.Builder();
        consumer.accept(builder);
        LootContextParamSet paramSet = builder.build();
        LootContextParamSetsAccessor.getRegistry().put(RuneCraftory.modRes(path), paramSet);
        return paramSet;
    }
}
