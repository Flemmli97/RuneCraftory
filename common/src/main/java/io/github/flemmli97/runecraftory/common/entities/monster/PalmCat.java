package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class PalmCat extends LeapingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String MELEE = BUILDER.add("attack", AnimationsBuilder.definition(0.72).marker("attack", 0.44));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
    public static final String LEAP = BUILDER.add("attack_2", AnimationsBuilder.definition(0.76)
            .marker("attack_start", 0.28).marker("attack_end", 0.64));
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<PalmCat> animationHandler = new AnimationHandler<>(this, ANIMS);

    private boolean hitAny;
    private boolean consecutive;

    public PalmCat(EntityType<? extends PalmCat> type, Level level) {
        super(type, level);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);
        super.applyAttributes();
    }

    @Override
    protected Consumer<AnimationDefinition> animatedActionConsumer() {
        return anim -> {
            super.animatedActionConsumer().accept(anim);
            if (!this.level().isClientSide) {
                AnimationState current = this.animationHandler.getAnimation();
                if (current != null) {
                    if (current.is(MELEE) || current.is(LEAP)) {
                        if (this.hitAny && !this.consecutive) {
                            this.consecutive = true;
                            BrainUtils.clearMemory(this, MemoryModuleType.ATTACK_COOLING_DOWN);
                        } else {
                            this.consecutive = false;
                        }
                    }
                }
                this.hitAny = false;
            }
        };
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(MELEE).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(4)
                .start(LEAP).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<BaseMonster>().min(2).max(5)).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(3)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(6, new SetWalkTargetToAttackTarget<>(), MonsterBehaviourUtils.moveTo())
                .add(2, new SetRandomWalkTarget<>(), MonsterBehaviourUtils.moveTo()).build();
    }

    @Override
    protected ExtendedBehaviour<? extends BaseMonster> getWanderBehaviour() {
        return new SetRandomWalkTarget<BaseMonster>().speedModifier(0.7f);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        DynamicDamage.Builder source = new DynamicDamage.Builder(this).noKnockback().hurtResistant(1);
        if (this.getAnimationHandler().isCurrent(LEAP))
            source.knock(DynamicDamage.KnockBackType.UP, 1);
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
    public AnimationHandler<? extends PalmCat> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected boolean isLeapingAnim(String anim) {
        return anim.equals(LEAP);
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
    public double sprintSpeedThreshold() {
        return 0.9;
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }
}
