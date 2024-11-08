package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

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
        if (this.isLeapingAnim(anim)) {
            this.getNavigation().stop();
            this.lookAt(EntityAnchorArgument.Anchor.FEET, this.position().add(this.getDeltaMovement().x, 0, this.getDeltaMovement().z));
            if (anim.getTick() == 1 && this.getTarget() != null) {
                this.targetPosition = this.getTarget().position();
            }
            if (anim.canAttack()) {
                Vec3 vec32 = this.getLeapVec(this.getTarget() == null ? this.targetPosition : this.getTarget().position());
                this.setDeltaMovement(vec32.x, this.leapHeightMotion(), vec32.z);
            }
            if (anim.isPastTick(anim.getAttackTime())) {
                if (this.hitEntity == null)
                    this.hitEntity = new ArrayList<>();
                this.mobAttack(anim, this.getTarget(), e -> {
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

    protected abstract boolean isLeapingAnim(AnimatedAction anim);

    public Vec3 getLeapVec(@Nullable Vec3 target) {
        if (target != null) {
            return new Vec3(target.x - this.getX(), 0.0, target.z - this.getZ()).normalize();
        }
        return this.getLookAngle();
    }

    public double leapHeightMotion() {
        return 0.1f;
    }

    public float maxLeapDistance() {
        return 3;
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (!this.isLeapingAnim(anim))
            return super.calculateAttackAABB(anim, target, grow);
        double width = this.getBbWidth();
        double speed = Math.max(width, this.getDeltaMovement().length() - width);
        return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                .inflate(grow).expandTowards(0, 0, speed), this.getYRot(), 0, this.position());
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
        return anim != null && this.isLeapingAnim(anim);
    }
}