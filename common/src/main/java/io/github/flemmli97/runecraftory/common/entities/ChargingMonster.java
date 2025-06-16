package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableDatas;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.common.network.S2CMobUpdate;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ChargingMonster extends BaseMonster {

    protected static final ResourceLocation CHARGING_STEP = RuneCraftory.modRes("charging_step");

    protected List<LivingEntity> hitEntity;
    private Vec3 chargeMotion;
    private final Consumer<AnimationDefinition> chargingAnim;
    private boolean initAnim;

    public ChargingMonster(EntityType<? extends ChargingMonster> type, Level level) {
        super(type, level);
        this.chargingAnim = this.animatedActionConsumer();
    }

    protected Consumer<AnimationDefinition> animatedActionConsumer() {
        return anim -> {
            if (!this.level().isClientSide) {
                if (anim != null && this.isChargingAnim(anim.id())) {
                    this.getAttribute(Attributes.STEP_HEIGHT).addTransientModifier(new AttributeModifier(CHARGING_STEP, 1, AttributeModifier.Operation.ADD_VALUE));
                } else {
                    this.getAttribute(Attributes.STEP_HEIGHT).removeModifier(CHARGING_STEP);
                }
                if (this.isChargingAnimation()) {
                    this.hitEntity = null;
                } else {
                    this.chargeMotion = null;
                }
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
        if (this.fixedYaw()) {
            return this.chargeMotion;
        }
        return super.directionToLookAt();
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (this.isChargingAnim(anim.getAnimation())) {
            if (this.chargeMotion == null) {
                this.setChargeMotion(this.getChargeTo(anim));
            }
            this.getNavigation().stop();
            if (anim.isPast("attack_start") && !anim.isPast("attack_end")) {
                if (!this.handleChargeMovement(anim))
                    return;
                if (this.hitEntity == null)
                    this.hitEntity = new ArrayList<>();
                this.mobAttack(anim, this.getTarget(), e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
                this.doWhileCharge();
            }
        } else {
            super.handleAttack(anim);
        }
    }

    protected abstract boolean isChargingAnim(String anim);

    protected boolean fixedYaw() {
        return this.isChargingAnimation();
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(String anim, Vec3 target, double grow) {
        if (!this.isChargingAnim(anim))
            return super.calculateAttackAABB(anim, target, grow);
        double width = this.getBbWidth();
        double speed = Math.max(width, this.getDeltaMovement().length() - width);
        float yRot = 0;
        if (this.chargeMotion != null) {
            yRot = MathsHelper.YRotFrom(this.chargeMotion);
        }
        return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                .inflate(0.2)
                .inflate(grow).expandTowards(0, 0, speed), yRot, 0, this.position());
    }

    @Override
    public boolean adjustRotFromRider(LivingEntity rider) {
        return !this.isChargingAnimation();
    }

    public void setChargeMotion(Vec3 chargeMotion) {
        this.chargeMotion = chargeMotion;
        S2CMobUpdate.send(this, SyncableDatas.MOTION_DIR, this.chargeMotion);
    }

    public Vec3 getChargeMotion() {
        return this.chargeMotion;
    }

    @Override
    public void push(Entity entity) {
        if (this.isChargingAnimation())
            return;
        super.push(entity);
    }

    public boolean handleChargeMovement(AnimationState anim) {
        if (this.chargeMotion != null) {
            this.setDeltaMovement(this.chargeMotion.x, this.getDeltaMovement().y, this.chargeMotion.z);
            return true;
        }
        return false;
    }

    public void doWhileCharge() {

    }

    public double chargingSpeed() {
        return 0.4;
    }

    public Vec3 getChargeTo(AnimationState anim) {
        return EntityUtils.getTargetDirection(this, EntityAnchorArgument.Anchor.FEET, true)
                .scale(this.chargingSpeed());
    }

    private boolean isChargingAnimation() {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        return anim != null && this.isChargingAnim(anim.getAnimation());
    }

    @Override
    public void onUpdate(SyncableEntityData.SyncedContainer<?> data) {
        super.onUpdate(data);
        data.runIf(SyncableDatas.MOTION_DIR, charge -> this.chargeMotion = charge);
    }
}