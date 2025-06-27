package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class EntityPalmCat extends LeapingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String MELEE = BUILDER.add("attack", AnimationsBuilder.definition(0.72).marker("attack", 0.44));
    public static final String LEAP = BUILDER.add("attack_2", AnimationsBuilder.definition(0.76)
            .marker("attack_start", 0.28).marker("attack_end", 0.64));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();
    //
//    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityPalmCat>>> ATTACKS = List.of(
//            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> e.consecutive ? 1 : 0.7f), 4),
//            WeightedEntry.wrap(new GoalAttackAction<EntityPalmCat>(LEAP)
//                    .cooldown(e -> e.animationCooldown(LEAP))
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 3))), 2)
//    );
//    private static final List<WeightedEntry.Wrapper<IdleAction<EntityPalmCat>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 3),
//            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(12, 5)), 1)
//    );
//
//    public final AnimatedAttackGoal<EntityPalmCat> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityPalmCat> animationHandler = new AnimationHandler<>(this, ANIMS);

    private boolean hitAny;
    private boolean consecutive;

    public EntityPalmCat(EntityType<? extends EntityPalmCat> type, Level world) {
        super(type, world);
    }

    @Override
    protected Consumer<AnimationDefinition> animatedActionConsumer() {
        return anim -> {
            super.animatedActionConsumer().accept(anim);
            if (!this.level().isClientSide) {
                AnimationState current = this.animationHandler.getAnimation();
//                if (MELEE.is(current) || LEAP.is(current)) {
//                    if (this.hitAny && !this.consecutive) {
//                        this.consecutive = true;
//                        this.attack.resetCooldown();
//                    } else {
//                        this.consecutive = false;
//                    }
//                }
                this.hitAny = false;
            }
        };
    }

    @Override
    protected boolean isLeapingAnim(String anim) {
        return anim.equals(LEAP);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);
    }

//    @Override
//    public void addGoal() {
//        super.addGoal();
//        this.goalSelector.removeGoal(this.wander);
//        this.wander = new RestrictedWaterAvoidingStrollGoal(this, 0.6);
//        this.goalSelector.addGoal(6, this.wander);
//    }

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
        DynamicDamage.Builder source = new DynamicDamage.Builder(this).noKnockback().hurtResistant(1);
        if (this.getAnimationHandler().isCurrent(LEAP))
            source.knock(DynamicDamage.KnockBackType.UP).knockAmount(1);
        boolean hurt = CombatUtils.mobAttack(this, entity, source);
        if (hurt)
            this.hitAny = true;
        return hurt;
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.3;
        double length = this.getBbWidth() * 2.1;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
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
    public String getSleepAnimation() {
        return SLEEP;
    }
//
//    @Override
//    public Vec3 passengerOffset(Entity passenger) {
//        return new Vec3(0, 15.75 / 16d, -4 / 16d);
//    }
}
