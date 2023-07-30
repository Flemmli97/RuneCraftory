package io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia;

import io.github.flemmli97.runecraftory.common.entities.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EntityRafflesiaHorseTail extends EntityRafflesiaPart implements HealingPredicateEntity {

    private static final Vec3 OFFSET = new Vec3(1.05, 0, 0);

    private final AnimationHandler<EntityRafflesiaHorseTail> animationHandler = new AnimationHandler<>(this, new AnimatedAction[]{EntityRafflesiaPart.HORSE_TAIL_ACTION});

    public EntityRafflesiaHorseTail(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public EntityRafflesiaHorseTail(Level level, EntityRafflesia parent) {
        super(ModEntities.RAFFLESIA_HORSETAIL.get(), level, parent);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level.isClientSide) {
            this.getAnimationHandler().runIfNotNull(anim -> {
                if (anim.canAttack()) {
                    ModSpells.CUREALL.get().use(this);
                }
            });
        }
    }

    @Override
    public Vec3 offset() {
        return OFFSET;
    }

    @Override
    public AnimatedAction attackAnim() {
        return EntityRafflesiaPart.HORSE_TAIL_ACTION;
    }

    @Override
    public AnimationHandler<?> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public Predicate<LivingEntity> healeableEntities() {
        return e -> {
            if (e.getUUID().equals(this.getOwnerUUID()))
                return true;
            if (e instanceof OwnableEntity ownableEntity && this.getOwnerUUID() == ownableEntity.getOwnerUUID())
                return true;
            if (this.getOwner() != null && this.getOwner().getOwner() == null)
                return e instanceof Enemy;
            return false;
        };
    }
}