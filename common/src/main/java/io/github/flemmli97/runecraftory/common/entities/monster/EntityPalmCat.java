package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.RestrictedWaterAvoidingStrollGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class EntityPalmCat extends LeapingMonster {

    private static final AnimatedAction MELEE = new AnimatedAction(15, 9, "attack");
    private static final AnimatedAction LEAP = new AnimatedAction(15, 8, "attack_2");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, LEAP, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityPalmCat>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> e.consecutive ? 1 : 0.7f), 4),
            WeightedEntry.wrap(new GoalAttackAction<EntityPalmCat>(LEAP)
                    .cooldown(e -> e.animationCooldown(LEAP))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 3))), 2)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityPalmCat>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1)), 3),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(12, 5)), 1)
    );

    public final AnimatedAttackGoal<EntityPalmCat> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
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
                if (MELEE.is(current) || LEAP.is(current)) {
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
    protected boolean isLeapingAnim(AnimatedAction anim) {
        return anim.is(LEAP);
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
    public AnimationHandler<? extends EntityPalmCat> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public Vec3 getLeapVec(@Nullable Vec3 target) {
        if (target != null) {
            Vec3 leap = new Vec3(target.x - this.getX(), 0.0, target.z - this.getZ());
            if (leap.lengthSqr() > 7)
                return leap.normalize();
            return leap.scale(0.9);
        }
        return super.getLeapVec(null);
    }

    @Override
    public double leapHeightMotion() {
        return 0.2;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        CustomDamage.Builder source = new CustomDamage.Builder(this).noKnockback().hurtResistant(1);
        if (this.getAnimationHandler().isCurrent(LEAP))
            source.knock(CustomDamage.KnockBackType.UP).knockAmount(1);
        boolean hurt = CombatUtils.mobAttack(this, entity, source);
        if (hurt)
            this.hitAny = true;
        return hurt;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (LEAP.is(anim))
            return 1.15;
        return 1.2;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
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

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 15.75 / 16d, -4 / 16d);
    }
}
