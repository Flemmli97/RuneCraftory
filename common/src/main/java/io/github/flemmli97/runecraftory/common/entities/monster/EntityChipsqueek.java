package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EntityChipsqueek extends ChargingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String MELEE = BUILDER.add("tail_slap", AnimationsBuilder.definition(0.44).marker("attack", 0.28));
    public static final String ROLL = BUILDER.add("roll", AnimationsBuilder.definition(0.56).marker("attack_start", 0.2));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    //    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityChipsqueek>>> ATTACKS = List.of(
//            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> 1), 1),
//            WeightedEntry.wrap(new GoalAttackAction<EntityChipsqueek>(ROLL)
//                    .cooldown(e -> e.animationCooldown(ROLL))
//                    .prepare(ChargeAction::new), 1)
//    );
//    private static final List<WeightedEntry.Wrapper<IdleAction<EntityChipsqueek>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(8, 4)), 3),
//            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 2)
//    );
//
//    public final AnimatedAttackGoal<EntityChipsqueek> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityChipsqueek> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityChipsqueek(EntityType<? extends EntityChipsqueek> type, Level world) {
        super(type, world);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.ENTITY_CHIPSQUEEK_HURT.get();
    }

    @Override
    protected boolean isChargingAnim(String anim) {
        return anim.equals(ROLL);
    }

    @Override
    public double chargingSpeed() {
        return 0.45f;
    }

    @Override
    public AnimationHandler<EntityChipsqueek> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.4;
        double length = this.getBbWidth() * 2.3;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
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
    public String getSleepAnimation() {
        return SLEEP;
    }
//
//    @Override
//    public Vec3 passengerOffset(Entity passenger) {
//        return new Vec3(0, 9.5 / 16d, -5 / 16d);
//    }
}
