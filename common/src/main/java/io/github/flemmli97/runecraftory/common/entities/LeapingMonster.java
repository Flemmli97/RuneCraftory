package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
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
    private Vec3 leapingDir;
    private boolean initAnim;

    public LeapingMonster(EntityType<? extends LeapingMonster> type, Level level) {
        super(type, level);
        this.chargingAnim = this.animatedActionConsumer();
    }

    protected Consumer<AnimatedAction> animatedActionConsumer() {
        return anim -> {
            if (this.isLeapingAnimation()) {
                this.hitEntity = null;
            } else {
                this.leapingDir = null;
            }
        };
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.initAnim) {
            this.getAnimationHandler().withChangeListener(anim -> {
                this.chargingAnim.accept(anim);
                return false;
            });
            this.initAnim = true;
        }
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().hasAnimation() && this.isLeapingAnim(this.getAnimationHandler().getAnimation())) {
            if (this.getDeltaMovement().lengthSqr() > 0.01)
                return this.getDeltaMovement();
            return null;
        }
        return super.directionToLookAt();
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.isLeapingAnim(anim)) {
            this.getNavigation().stop();
            if (anim.isAt("attack_start")) {
                Vec3 vec32 = this.getLeapVec(this.tryGetTargetPosition(this.getTarget()));
                this.setDeltaMovement(vec32.x, this.leapHeightMotion(), vec32.z);
                this.leapingDir = this.getDeltaMovement();
            }
            if (anim.isPast("attack_start") && !anim.isPast("attack_end")) {
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

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (!this.isLeapingAnim(anim))
            return super.calculateAttackAABB(anim, target, grow);
        double width = this.getBbWidth();
        double speed = Math.max(width, this.getDeltaMovement().length() - width);
        float yRot = 0;
        if (this.leapingDir != null) {
            yRot = MathsHelper.YRotFrom(this.leapingDir);
        }
        return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                .inflate(0.2)
                .inflate(grow).expandTowards(0, 0, speed), yRot, 0, this.position());
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