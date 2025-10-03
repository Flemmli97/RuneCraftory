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
    private final Consumer<AnimationDefinition> chargingAnim;
    private boolean initAnim;

    public static final TypedResource<Vec3> CHARGE_MOTION = new TypedResource<>(RuneCraftory.modRes("charge_motion"));

    public ChargingMonster(EntityType<? extends ChargingMonster> type, Level level) {
        super(type, level);
        this.chargingAnim = this.animatedActionConsumer();
    }

    protected Consumer<AnimationDefinition> animatedActionConsumer() {
        return anim -> {
            if (!this.level().isClientSide) {
                this.getAttribute(Attributes.STEP_HEIGHT).removeModifier(CHARGING_STEP);
                if (anim != null && this.isChargingAnim(anim.id())) {
                    this.getAttribute(Attributes.STEP_HEIGHT).addTransientModifier(new AttributeModifier(CHARGING_STEP, 1, AttributeModifier.Operation.ADD_VALUE));
                }
                if (this.isChargingAnimation()) {
                    this.hitEntity = null;
                } else {
                    this.setChargeMotion(null);
                }
            }
        };
    }

    @Override
    protected void definedAdditinoalSyncedData(SyncedDataContainer.Builder<BaseMonster> builder) {
        super.definedAdditinoalSyncedData(builder);
        builder.define(CHARGE_MOTION, TenshilibSyncableEntityDatas.VEC_3.get(), null);
    }

    @Override
    public void aiStep() {
        if (!this.initAnim) {
            this.getAnimationHandler().withChangeListener(anim -> {
                this.chargingAnim.accept(anim);
                return false;
            });
            this.initAnim = true;
        }
        super.aiStep();
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.fixedYaw()) {
            return this.getChargeMotion();
        }
        return super.directionToLookAt();
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (!this.isChargingAnim(anim.getID()))
            return super.calculateAttackAABB(anim, target, grow);
        double width = this.getBbWidth();
        double speed = Math.max(width, this.getDeltaMovement().length() - width);
        float yRot = 0;
        if (this.getChargeMotion() != null) {
            yRot = MathsHelper.YRotFrom(this.getChargeMotion());
        }
        return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                .inflate(0.2)
                .inflate(grow).expandTowards(0, 0, speed), yRot, 0, this.position());
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (this.isChargingAnim(anim.getID())) {
            if (this.getChargeMotion() == null) {
                this.setChargeMotion(this.getChargeTo(anim.getAnimation()));
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

    @Override
    public void push(Entity entity) {
        if (this.isChargingAnimation())
            return;
        super.push(entity);
    }

    protected abstract boolean isChargingAnim(String anim);

    private boolean isChargingAnimation() {
        AnimationState anim = this.getAnimationHandler().getAnimation();
        return anim != null && this.isChargingAnim(anim.getID());
    }

    public Vec3 getChargeMotion() {
        return this.getDataContainer().get(CHARGE_MOTION);
    }

    public Vec3 getChargeTo(String anim) {
        return EntityUtils.getTargetDirection(this, EntityAnchorArgument.Anchor.FEET, true)
                .scale(this.chargingSpeed());
    }

    public void setChargeMotion(Vec3 chargeMotion) {
        this.getDataContainer().set(CHARGE_MOTION, chargeMotion);
    }

    public double chargingSpeed() {
        return 0.4;
    }

    public boolean handleChargeMovement(AnimationState anim) {
        Vec3 charge = this.getChargeMotion();
        if (charge != null) {
            this.setDeltaMovement(charge.x, this.getDeltaMovement().y, charge.z);
            return true;
        }
        return false;
    }

    public void doWhileCharge() {
    }

    protected boolean fixedYaw() {
        return this.isChargingAnimation();
    }

    @Override
    public boolean adjustRotFromRider(LivingEntity rider) {
        return !this.isChargingAnimation();
    }
}