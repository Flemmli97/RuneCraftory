package io.github.flemmli97.runecraftory.common.advancements.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record EntitySubPredicateList(List<EntitySubPredicate> subPredicates) implements EntitySubPredicate {

    public static final MapCodec<EntitySubPredicateList> CODEC = EntitySubPredicate.CODEC.listOf()
            .xmap(EntitySubPredicateList::new, EntitySubPredicateList::subPredicates).fieldOf("sub_predicates");

    @Override
    public MapCodec<? extends EntitySubPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
        for (EntitySubPredicate subPredicate : this.subPredicates()) {
            if (!subPredicate.matches(entity, level, position)) {
                return false;
            }
        }
        return true;
    }
}
