package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.common.entities.utils.IBaseMob;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryLootRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;
import java.util.UUID;

public record FriendPointCondition(int points) implements LootItemCondition {

    public static final MapCodec<FriendPointCondition> CODEC = ExtraCodecs.NON_NEGATIVE_INT.fieldOf("points")
            .xmap(FriendPointCondition::new, FriendPointCondition::points);

    public static Builder of(int points) {
        return () -> new FriendPointCondition(points);
    }

    @Override
    public LootItemConditionType getType() {
        return RuneCraftoryLootRegistries.FRIENDPOINTS.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.THIS_ENTITY);
    }

    @Override
    public boolean test(LootContext ctx) {
        if (ctx.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof IBaseMob mob) {
            UUID uuid = ctx.getParamOrNull(LootCtxParameters.UUID_CONTEXT);
            if (uuid != null)
                return mob.friendPoints(uuid) >= this.points;
        }
        return false;
    }
}