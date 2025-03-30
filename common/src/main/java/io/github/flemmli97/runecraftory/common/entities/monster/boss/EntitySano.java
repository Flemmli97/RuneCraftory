package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.monster.EntitySanoUno;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.spells.FireWallSpell;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
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
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BiConsumer;

public class EntitySano extends EntitySanoUno {

    public static final AnimatedAction FIREBALL_3X = AnimatedAction.builder(2, "fireball_3x").marker("attack", 0.8, 1, 1.2).build();
    public static final AnimatedAction FIREBALL_BARRAGE = AnimatedAction.builder(4.5, "fireball_barrage")
            .marker("single", 3).marker("double", 3.8)
            .marker("triple", 0.8).marker("quad", 1.2, 3.4).build();
    public static final AnimatedAction EXPLOSION = AnimatedAction.builder(1.5, "explosion").marker("attack", 0.8).build();
    public static final AnimatedAction FIRE_BREATH = AnimatedAction.builder(2, "fire_breath").marker("attack", 0.8, 1.2).build();
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(10, "defeat").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{FIREBALL_3X, FIREBALL_BARRAGE, EXPLOSION, FIRE_BREATH, DEFEAT};

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntitySano>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(FIREBALL_3X, (anim, entity) -> {
            if (anim.isAt("attack"))
                ModSpells.FIREBALL.get().use(entity);
        });
        b.put(FIREBALL_BARRAGE, (anim, entity) -> {
            if (anim.isAt("single"))
                ModSpells.FIREBALL.get().use(entity);
            if (anim.isAt("double"))
                ModSpells.DOUBLE_FIRE_BALL.get().use(entity);
            if (anim.isAt("triple"))
                ModSpells.TRIPLE_FIRE_BALL.get().use(entity);
            if (anim.isAt("quad"))
                ModSpells.QUAD_FIRE_BALL.get().use(entity);
        });
        b.put(EXPLOSION, (anim, entity) -> {
            if (anim.isAt("attack"))
                ModSpells.EXPLOSION.get().use(entity);
        });
        b.put(FIRE_BREATH, (anim, entity) -> {
            if (anim.isAt("attack")) {
                Vec3 from = FireWallSpell.offset(entity);
                Vec3 dir = entity.getTarget() != null ? EntityUtils.getStraightProjectileTarget(from, entity.getTarget()).subtract(from).normalize() : entity.getLookAngle();
                dir = dir.scale(10).add(entity.random.nextGaussian() * 2.3, -Math.abs(entity.random.nextGaussian()) * 0.6, entity.random.nextGaussian() * 2.3);
                entity.targetPos = entity.position().add(dir);
                ModSpells.FIRE_WALL.get().use(entity);
            }
        });
    });

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntitySano>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.<EntitySano>nonRepeatableAttack(FIREBALL_3X)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySano>nonRepeatableAttack(FIREBALL_BARRAGE)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySano>nonRepeatableAttack(EXPLOSION)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySano>nonRepeatableAttack(FIRE_BREATH)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 10)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntitySano>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<EntitySano>(DoNothingRunner::new)
                    .duration(e -> e.getRandom().nextInt(20) + 35), 1)
    );

    public final AnimatedAttackGoal<EntitySano> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntitySano> animationHandler = new AnimationHandler<>(this, ANIMS);

    private EntityUno other;

    public EntitySano(EntityType<? extends EntitySano> type, Level world) {
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
        BiConsumer<AnimatedAction, EntitySano> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.FIRE_WALL.get()))
                    this.getAnimationHandler().setAnimation(FIRE_BREATH);
            } else if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.FIREBALL_BARRAGE.get()))
                    this.getAnimationHandler().setAnimation(FIREBALL_BARRAGE);
            } else if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.TRIPLE_FIRE_BALL.get()))
                this.getAnimationHandler().setAnimation(FIREBALL_3X);
        }
    }

    @Override
    public AnimationHandler<EntitySano> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public EntityUno getLinked() {
        if (this.other != null && !this.other.isRemoved()) {
            return this.other;
        }
        if (this.getLinkedID() != null) {
            List<EntityUno> results = this.level.getEntities(EntityTypeTest.forClass(EntityUno.class), this.getBoundingBox().inflate(64), e -> this.getLinkedID().equals(e.getLinkedID()));
            if (!results.isEmpty()) {
                this.other = results.get(0);
            }
        }
        return this.other;
    }
}
