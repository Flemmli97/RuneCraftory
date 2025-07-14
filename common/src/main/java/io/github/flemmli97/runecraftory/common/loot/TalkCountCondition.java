package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryLootRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;
import java.util.UUID;

public record TalkCountCondition(int count) implements LootItemCondition {

    public static final MapCodec<TalkCountCondition> CODEC = ExtraCodecs.NON_NEGATIVE_INT.fieldOf("count")
            .xmap(TalkCountCondition::new, TalkCountCondition::count);

    public static Builder of(int count) {
        return () -> new TalkCountCondition(count);
    }

    @Override
    public LootItemConditionType getType() {
        return RuneCraftoryLootRegistries.TALKCOUNT.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.THIS_ENTITY);
    }

    @Override
    public boolean test(LootContext ctx) {
        if (ctx.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof NPCEntity npc) {
            UUID uuid = ctx.getParamOrNull(LootCtxParameters.UUID_CONTEXT);
            if (uuid != null)
                return npc.talkCount(uuid) >= this.count;
        }
        return false;
    }
}