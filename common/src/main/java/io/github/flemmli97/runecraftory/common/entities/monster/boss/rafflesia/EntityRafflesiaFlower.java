package io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityRafflesiaFlower extends EntityRafflesiaPart {

    private static final Vec3 OFFSET = new Vec3(0, 0, 1.1);

    private final AnimationHandler<EntityRafflesiaFlower> animationHandler = new AnimationHandler<>(this, new AnimatedAction[]{EntityRafflesiaPart.FLOWER_ACTION});

    public EntityRafflesiaFlower(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public EntityRafflesiaFlower(Level level, EntityRafflesia parent) {
        super(ModEntities.RAFFLESIA_FLOWER.get(), level, parent);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level.isClientSide) {
            this.getAnimationHandler().runIfNotNull(anim -> {
                if (anim.canAttack()) {
                    EntityRafflesiaPitcher.rafflesiaSpawning(this);
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
        return EntityRafflesiaPart.FLOWER_ACTION;
    }

    @Override
    public AnimationHandler<?> getAnimationHandler() {
        return this.animationHandler;
    }
}