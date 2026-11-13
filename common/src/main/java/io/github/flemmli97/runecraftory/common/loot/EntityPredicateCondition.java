package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryLootRegistries;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public record EntityPredicateCondition(EntityPredicate predicate) implements LootItemCondition {

    public static final MapCodec<EntityPredicateCondition> CODEC = EntityPredicate.CODEC.xmap(EntityPredicateCondition::new, EntityPredicateCondition::predicate)
            .fieldOf("predicate");

    public static LootItemCondition.Builder get(EntityPredicate.Builder builder) {
        return () -> new EntityPredicateCondition(builder.build());
    }

    @Override
    public LootItemConditionType getType() {
        return RuneCraftoryLootRegistries.PREDICATE_CONDITION.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.THIS_ENTITY);
    }

    @Override
    public boolean test(LootContext ctx) {
        Entity entity = ctx.getParam(LootContextParams.THIS_ENTITY);
        return this.predicate.matches(ctx.getLevel(), entity.position(), entity);
    }
}
