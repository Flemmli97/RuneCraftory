package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityGhostRay extends EntityGhost {

    public EntityGhostRay(EntityType<? extends EntityGhostRay> type, Level world) {
        super(type, world);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 2.2;
    }
}
