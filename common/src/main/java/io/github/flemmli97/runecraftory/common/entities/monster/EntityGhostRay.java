package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityGhostRay extends EntityGhost {

    public EntityGhostRay(EntityType<? extends EntityGhostRay> type, Level world) {
        super(type, world);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.9;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return super.passengerOffset(passenger).scale(1.4);
    }
}
