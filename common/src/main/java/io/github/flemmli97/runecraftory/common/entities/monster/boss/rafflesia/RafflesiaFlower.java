package io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia;

import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RafflesiaFlower extends RafflesiaPart {

    private static final Vec3 OFFSET = new Vec3(0, 0, 1.1);

    private final AnimationHandler<RafflesiaFlower> animationHandler = new AnimationHandler<>(this, ANIMS);

    public RafflesiaFlower(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public RafflesiaFlower(Level level, Rafflesia parent) {
        super(RuneCraftoryEntities.RAFFLESIA_FLOWER.get(), level, parent);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide) {
            this.getAnimationHandler().runIfNotNull(anim -> {
                if (anim.isAt("attack")) {
                    RafflesiaPitcher.rafflesiaSpawning(this);
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
        return PartType.FLOWER;
    }

    @Override
    public Vec3 offset() {
        return OFFSET;
    }

    @Override
    public String attackAnim() {
        return RafflesiaPart.FLOWER_ACTION;
    }

    @Override
    public int cooldown() {
        return this.getRandom().nextInt(60) + 120;
    }
}