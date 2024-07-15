package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.monster.EntitySanoUno;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;

import java.util.List;
import java.util.function.BiConsumer;

public class EntityUno extends EntitySanoUno {

    public static final AnimatedAction WATER_LASER = new AnimatedAction(40, 15, "water_laser");
    public static final AnimatedAction WATER_LASER_2 = new AnimatedAction(40, 15, "water_swipe");
    public static final AnimatedAction ICEBALLS_5 = new AnimatedAction(50, 15, "iceballs");
    public static final AnimatedAction HOMING_WATER_WAVE = new AnimatedAction(40, 15, "water_wave");
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(150, "defeat").marker(150).infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{WATER_LASER, WATER_LASER_2, ICEBALLS_5, HOMING_WATER_WAVE, DEFEAT};

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityUno>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(WATER_LASER, (anim, entity) -> {
            if (anim.isAtTick(15))
                ModSpells.WATER_LASER_LONG.get().use(entity);
            if (anim.isAtTick(25))
                ModSpells.PARALLEL_LASER_LONG.get().use(entity);
        });
        b.put(WATER_LASER_2, (anim, entity) -> {
            if (anim.isAtTick(15)) {
                entity.reversedSwipe = false;
                ModSpells.WATER_SWIPE.get().use(entity);
            }
            if (anim.isAtTick(30)) {
                entity.reversedSwipe = true;
                ModSpells.WATER_SWIPE.get().use(entity);
            }
        });
        b.put(ICEBALLS_5, (anim, entity) -> {
            if (anim.isAtTick(15) || anim.isAtTick(20) || anim.isAtTick(25) || anim.isAtTick(30) || anim.isAtTick(35)) {
                ModSpells.ICE_BALL_DROP.get().use(entity);
            }
        });
        b.put(HOMING_WATER_WAVE, (anim, entity) -> {
            if (anim.canAttack())
                ModSpells.ICE_TRAIL.get().use(entity);
        });
    });

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityUno>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.<EntityUno>nonRepeatableAttack(WATER_LASER)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityUno>nonRepeatableAttack(WATER_LASER_2)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityUno>nonRepeatableAttack(ICEBALLS_5)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityUno>nonRepeatableAttack(HOMING_WATER_WAVE)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 10)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityUno>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<EntityUno>(DoNothingRunner::new)
                    .duration(e -> e.getRandom().nextInt(20) + 35), 1)
    );

    public final AnimatedAttackGoal<EntityUno> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityUno> animationHandler = new AnimationHandler<>(this, ANIMS);
    private EntitySano other;

    public EntityUno(EntityType<? extends EntityUno> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        BiConsumer<AnimatedAction, EntityUno> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.ICE_BALL_DROP.get()))
                    this.getAnimationHandler().setAnimation(ICEBALLS_5);
            } else if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.PARALLEL_LASER.get()))
                    this.getAnimationHandler().setAnimation(WATER_LASER_2);
            } else if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.WATER_LASER.get()))
                this.getAnimationHandler().setAnimation(WATER_LASER);
        }
    }

    @Override
    public AnimationHandler<EntityUno> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public EntitySano getLinked() {
        if (this.other != null && !this.other.isRemoved()) {
            return this.other;
        }
        if (this.getLinkedID() != null) {
            List<EntitySano> results = this.level.getEntities(EntityTypeTest.forClass(EntitySano.class), this.getBoundingBox().inflate(64), e -> this.getLinkedID().equals(e.getLinkedID()));
            if (!results.isEmpty()) {
                this.other = results.get(0);
            }
        }
        return this.other;
    }
}
