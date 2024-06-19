package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.ChargeAction;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityChipsqueek extends ChargingMonster {

    public static final AnimatedAction MELEE = new AnimatedAction(11, 6, "tail_slap");
    public static final AnimatedAction ROLL = new AnimatedAction(12, 2, "roll");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, ROLL, MELEE, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityChipsqueek>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> 1), 1),
            WeightedEntry.wrap(new GoalAttackAction<EntityChipsqueek>(ROLL)
                    .cooldown(e -> e.animationCooldown(ROLL))
                    .prepare(ChargeAction::new), 1)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityChipsqueek>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(8, 4)), 3),
            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 2)
    );

    public final AnimatedAttackGoal<EntityChipsqueek> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityChipsqueek> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityChipsqueek(EntityType<? extends EntityChipsqueek> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.ENTITY_CHIPSQUEEK_HURT.get();
    }

    @Override
    protected boolean isChargingAnim(AnimatedAction anim) {
        return anim.is(ROLL);
    }

    @Override
    public float chargingLength() {
        return 5;
    }

    @Override
    public AnimationHandler<EntityChipsqueek> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(ROLL);
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
        return new Vec3(0, 9.5 / 16d, -5 / 16d);
    }
}
