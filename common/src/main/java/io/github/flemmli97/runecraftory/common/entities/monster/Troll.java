package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.function.Consumer;

public class Troll extends BaseMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String PUNCH = BUILDER.add("punch", AnimationsBuilder.definition(0.8).marker("attack", 0.52));
    public static final String INTERACT = BUILDER.add("interact", PUNCH);
    public static final String DOUBLE_PUNCH = BUILDER.add("double_fist_punch", AnimationsBuilder.definition(0.8).marker("attack", 0.52));
    public static final String SLAM = BUILDER.add("fist_slam", AnimationsBuilder.definition(0.8).marker("attack", 0.52));
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Troll> animationHandler = new AnimationHandler<>(this, ANIMS);

    public Troll(EntityType<? extends Troll> type, Level level) {
        super(type, level);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.23);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(PUNCH).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(5)
                .start(DOUBLE_PUNCH).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(4)
                .start(SLAM).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(5)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(5, new SetWalkTargetToAttackTarget<>(), MonsterBehaviourUtils.moveTo())
                .add(2, new SetRandomWalkTarget<>(), MonsterBehaviourUtils.moveTo()).build();
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.is(SLAM)) {
            return new OrientedBoundingBox(this.attackBB(anim), this.getYRot(), 0, this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        if (anim.is(SLAM)) {
            double range = this.getBbWidth() * 2.1;
            return new AABB(-range * 0.5, -0.02, -range * 0.25, range * 0.5, this.getBbHeight() + 0.02, range * 0.75);
        }
        double width = this.getBbWidth() * 1.4;
        double length = this.getBbWidth() * 1.8;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void mobAttack(AnimationState anim, LivingEntity target, Consumer<LivingEntity> cons) {
        super.mobAttack(anim, target, cons);
        if (anim.is(SLAM)) {
            S2CScreenShake.sendAround(this, 16, 10, 2);
            this.level().playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), this.getSoundSource(), 1.0f, 1.0f);
        }
    }

    @Override
    public DynamicDamage.Builder damageSourceAttack() {
        DynamicDamage.Builder source = super.damageSourceAttack();
        if (this.getAnimationHandler().isCurrent(SLAM, DOUBLE_PUNCH))
            source.withChangedAttribute(RuneCraftoryAttributes.STUN.asHolder(), 30);
        return source;
    }

    @Override
    public AnimationHandler<Troll> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(SLAM);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(DOUBLE_PUNCH);
            else
                this.getAnimationHandler().setAnimation(PUNCH);
        }
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
