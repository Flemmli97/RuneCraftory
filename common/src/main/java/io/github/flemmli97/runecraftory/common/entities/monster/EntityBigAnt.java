package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityBigAnt extends EntityAnt {

    public EntityBigAnt(EntityType<? extends EntityBigAnt> type, Level world) {
        super(type, world);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.3;
    }
}
