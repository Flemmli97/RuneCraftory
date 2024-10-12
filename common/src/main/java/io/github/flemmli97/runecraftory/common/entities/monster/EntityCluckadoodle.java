package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityCluckadoodle extends BaseMonster {

    public static final AnimatedAction MELEE = new AnimatedAction(16, 10, "attack");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityCluckadoodle>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> 1), 1)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityCluckadoodle>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 1)
    );

    public final AnimatedAttackGoal<EntityCluckadoodle> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityCluckadoodle> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityCluckadoodle(EntityType<? extends EntityCluckadoodle> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.27);
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        return Math.max(25, (int) (super.animationCooldown(anim) * 0.7));
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.15;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.CHICKEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CHICKEN_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.8f;
    }

    @Override
    public AnimationHandler<EntityCluckadoodle> getAnimationHandler() {
        return this.animationHandler;
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
        return new Vec3(0, 11.5 / 16d, -2 / 16d);
    }
}
