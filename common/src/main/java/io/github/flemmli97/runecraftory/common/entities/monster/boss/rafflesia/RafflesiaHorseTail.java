package io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia;

import io.github.flemmli97.runecraftory.common.entities.utils.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class RafflesiaHorseTail extends RafflesiaPart implements HealingPredicateEntity {

    private static final Vec3 OFFSET = new Vec3(1.05, 0, 0);

    private final AnimationHandler<RafflesiaHorseTail> animationHandler = new AnimationHandler<>(this, ANIMS);

    public RafflesiaHorseTail(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public RafflesiaHorseTail(Level level, Rafflesia parent) {
        super(RuneCraftoryEntities.RAFFLESIA_HORSETAIL.get(), level, parent);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide) {
            this.getAnimationHandler().runIfNotNull(anim -> {
                if (anim.isAt("attack")) {
                    RuneCraftorySpells.CURE_ALL.get().use(this);
                }
            });
        }
    }

    @Override
    public AnimationHandler<?> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public PartType getPartType() {
        return PartType.HORSETAIL;
    }

    @Override
    public Vec3 offset() {
        return OFFSET;
    }

    @Override
    public String attackAnim() {
        return RafflesiaPart.HORSE_TAIL_ACTION;
    }

    @Override
    public Predicate<LivingEntity> healeableEntities() {
        return e -> {
            if (e.getUUID().equals(this.getOwnerUUID()))
                return true;
            if (e instanceof OwnableEntity ownableEntity && this.getOwnerUUID() == ownableEntity.getOwnerUUID())
                return true;
            Rafflesia owner = this.getOwner();
            if (owner != null && this.getOwner().getOwner() == null)
                return e instanceof Enemy && e != owner.getTarget();
            return owner == null || e != owner.getTarget();
        };
    }
}