package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.PalmCatAttack;
import io.github.flemmli97.runecraftory.common.entities.ai.RestrictedWaterAvoidingStrollGoal;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class EntityPalmCat extends LeapingMonster {

    private static final AnimatedAction MELEE = new AnimatedAction(15, 9, "attack");
    private static final AnimatedAction LEAP = new AnimatedAction(15, 8, "attack_2");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().changeDelay(AnimationHandler.DEFAULT_ADJUST_TIME).build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, LEAP, INTERACT, SLEEP};

    public PalmCatAttack<EntityPalmCat> attack = new PalmCatAttack<>(this);
    private final AnimationHandler<EntityPalmCat> animationHandler = new AnimationHandler<>(this, ANIMS);

    private boolean hitAny;
    private boolean consecutive;

    public EntityPalmCat(EntityType<? extends EntityPalmCat> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected Consumer<AnimatedAction> animatedActionConsumer() {
        return anim -> {
            super.animatedActionConsumer().accept(anim);
            if (!this.level.isClientSide) {
                AnimatedAction current = this.animationHandler.getAnimation();
                if (MELEE.checkID(current) || LEAP.checkID(current)) {
                    if (this.hitAny && !this.consecutive) {
                        this.consecutive = true;
                        this.attack.resetCooldown();
                    } else {
                        this.consecutive = false;
                    }
                }
                this.hitAny = false;
            }
        };
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);
    }

    @Override
    public void addGoal() {
        super.addGoal();
        this.goalSelector.removeGoal(this.wander);
        this.wander = new RestrictedWaterAvoidingStrollGoal(this, 0.6);
        this.goalSelector.addGoal(6, this.wander);
    }

    @Override
    public double sprintSpeedThreshold() {
        return 0.9;
    }

    @Override
    public float attackChance(AnimationType type) {
        if (type == AnimationType.MELEE)
            return 0.7f;
        return 1;
    }

    @Override
    public AnimationHandler<? extends EntityPalmCat> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public Vec3 getLeapVec(@Nullable LivingEntity target) {
        if (target != null) {
            Vec3 targetPos = target.position();
            Vec3 leap = new Vec3(targetPos.x - this.getX(), 0.0, targetPos.z - this.getZ());
            if (leap.lengthSqr() > 4)
                leap = leap.normalize();
            return leap;
        }
        return super.getLeapVec(null);
    }

    @Override
    public float maxLeapDistance() {
        return 3;
    }

    @Override
    public double leapHeightMotion() {
        return 0.2;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        CustomDamage.Builder source = new CustomDamage.Builder(this).noKnockback().hurtResistant(1);
        double damagePhys = CombatUtils.getAttributeValue(this, Attributes.ATTACK_DAMAGE);
        boolean hurt = CombatUtils.mobAttack(this, entity, source, damagePhys);
        if (hurt)
            this.hitAny = true;
        return hurt;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(MELEE.getID());
        if (type == AnimationType.LEAP)
            return anim.getID().equals(LEAP.getID());
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (LEAP.checkID(anim))
            return 1.2;
        return 1;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 1)
                this.getAnimationHandler().setAnimation(LEAP);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return SLEEP;
    }
}
