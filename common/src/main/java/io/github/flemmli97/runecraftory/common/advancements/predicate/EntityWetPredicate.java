package io.github.flemmli97.runecraftory.common.advancements.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntityWetPredicate implements EntitySubPredicate {

    public static final EntityWetPredicate INSTANCE = new EntityWetPredicate();
    public static final MapCodec<EntityWetPredicate> CODEC = MapCodec.unit(INSTANCE);

    private EntityWetPredicate() {
    }

    @Override
    public MapCodec<? extends EntitySubPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
        return entity.isInWaterOrRain();
    }
}
