package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.common.registry.ModLootRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public class FirstKillCondition implements LootItemCondition {

    public static final FirstKillCondition INSTANCE = new FirstKillCondition();
    public static final MapCodec<FirstKillCondition> CODEC = MapCodec.unit(INSTANCE);

    public static LootItemCondition.Builder get() {
        return () -> INSTANCE;
    }

    private FirstKillCondition() {
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootRegistries.FIRST_KILL.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.ATTACKING_ENTITY);
    }

    @Override
    public boolean test(LootContext ctx) {
        Entity entity = ctx.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
        ServerPlayer player = null;
        if (entity instanceof LivingEntity living) {
            if (living instanceof ServerPlayer serverPlayer) {
                player = serverPlayer;
            } else if (living instanceof OwnableEntity ownable && ownable.getOwner() instanceof ServerPlayer serverPlayer) {
                player = serverPlayer;
            }
            return player != null && player.getStats().getValue(Stats.ENTITY_KILLED.get(entity.getType())) <= 0;
        }
        return false;
    }
}
