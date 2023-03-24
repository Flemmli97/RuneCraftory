package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class LeapingMonster extends BaseMonster {

    protected List<LivingEntity> hitEntity;
    private final Consumer<AnimatedAction> chargingAnim;
    private boolean initAnim;

    public LeapingMonster(EntityType<? extends LeapingMonster> type, Level level) {
        super(type, level);
        this.chargingAnim = this.animatedActionConsumer();
    }

    protected Consumer<AnimatedAction> animatedActionConsumer() {
        return anim -> {
            if (this.isLeapingAnimation()) {
                this.hitEntity = null;
            }
        };
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.initAnim) {
            this.getAnimationHandler().setAnimationChangeCons(this.chargingAnim);
            this.initAnim = true;
        }
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.isAnimOfType(anim, AnimationType.LEAP)) {
            if (anim.canAttack()) {
                Vec3 vec32 = this.getLeapVec(this.getTarget());
                this.setDeltaMovement(vec32.x, this.leapHeightMotion(), vec32.z);
                this.lookAt(EntityAnchorArgument.Anchor.EYES, vec32);
            }
            if (anim.getTick() >= anim.getAttackTime()) {
                if (this.hitEntity == null)
                    this.hitEntity = new ArrayList<>();
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
            }
        } else {
            super.handleAttack(anim);
        }
    }

    public Vec3 getLeapVec(@Nullable LivingEntity target) {
        if (target != null) {
            Vec3 targetPos = target.position();
            return new Vec3(targetPos.x - this.getX(), 0.0, targetPos.z - this.getZ()).normalize();
        }
        return this.getLookAngle();
    }

    public double leapHeightMotion() {
        return 0.1f;
    }

    public float maxLeapDistance() {
        return 6;
    }

    @Override
    public boolean adjustRotFromRider(LivingEntity rider) {
        return !this.isLeapingAnimation();
    }

    @Override
    public void push(Entity entity) {
        if (this.isLeapingAnimation())
            return;
        super.push(entity);
    }

    private boolean isLeapingAnimation() {
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        return anim != null && this.isAnimOfType(anim, AnimationType.LEAP);
    }
}