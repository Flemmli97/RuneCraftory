package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.registry.TenshilibSyncableEntityDatas;
import io.github.flemmli97.tenshilib.common.utils.TypedResource;
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
    private final Consumer<AnimationDefinition> chargingAnim;
    private boolean initAnim;

    public static final TypedResource<Vec3> LEAP_MOTION = new TypedResource<>(RuneCraftory.modRes("leap_motion"));

    public LeapingMonster(EntityType<? extends LeapingMonster> type, Level level) {
        super(type, level);
        this.chargingAnim = this.animatedActionConsumer();
    }

    protected Consumer<AnimationDefinition> animatedActionConsumer() {
        return anim -> {
            if (!this.level().isClientSide) {
                if (this.isLeapingAnimation()) {
                    this.hitEntity = null;
                } else {
                    this.setLeapMotion(null);
                }
            }
        };
    }

    @Override
    protected void definedAdditinoalSyncedData(SyncedDataContainer.Builder<BaseMonster> builder) {
        super.definedAdditinoalSyncedData(builder);
        builder.define(LEAP_MOTION, TenshilibSyncableEntityDatas.VEC_3.get(), null);
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
        if (this.getAnimationHandler().hasAnimation() && this.isLeapingAnim(this.getAnimationHandler().getAnimation().getID())) {
            return this.getLeapMotion();
        }
        return super.directionToLookAt();
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (this.isLeapingAnim(anim.getID())) {
            this.getNavigation().stop();
            if (anim.isAt("attack_start")) {
                Vec3 vec32 = this.getLeapVec(this.tryGetTargetPosition(this.getTarget()));
                this.setDeltaMovement(vec32.x, this.leapHeightMotion(), vec32.z);
                this.setLeapMotion(this.getDeltaMovement());
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

    protected abstract boolean isLeapingAnim(String anim);

    public Vec3 getLeapVec(@Nullable Vec3 target) {
        if (target != null) {
            return new Vec3(target.x - this.getX(), 0.0, target.z - this.getZ()).normalize();
        }
        return EntityUtils.horizontalLookAngle(this);
    }

    public double leapHeightMotion() {
        return 0.1f;
    }

    public Vec3 getLeapMotion() {
        return this.getDataContainer().get(LEAP_MOTION);
    }

    public void setLeapMotion(Vec3 chargeMotion) {
        this.getDataContainer().set(LEAP_MOTION, chargeMotion);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (!this.isLeapingAnim(anim.getID()))
            return super.calculateAttackAABB(anim, target, grow);
        double width = this.getBbWidth();
        double speed = Math.max(width, this.getDeltaMovement().length() - width);
        float yRot = 0;
        if (this.getLeapMotion() != null) {
            yRot = MathsHelper.YRotFrom(this.getLeapMotion());
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
        AnimationState anim = this.getAnimationHandler().getAnimation();
        return anim != null && this.isLeapingAnim(anim.getID());
    }
}