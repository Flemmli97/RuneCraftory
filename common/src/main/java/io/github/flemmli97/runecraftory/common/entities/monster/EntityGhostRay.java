package io.github.flemmli97.runecraftory.common.entities.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityGhostRay extends EntityGhost {

    public EntityGhostRay(EntityType<? extends EntityGhostRay> type, Level world) {
        super(type, world);
    }
//
//    @Override
//    public Vec3 passengerOffset(Entity passenger) {
//        return super.passengerOffset(passenger).scale(1.4);
//    }
}
