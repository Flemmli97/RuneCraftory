package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.monster.SanoUno;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.spells.FireWallSpell;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;
import java.util.function.BiConsumer;

public class Sano extends SanoUno {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String FIREBALL_3X = BUILDER.add("fireball_3x", AnimationsBuilder.definition(2).marker("attack", 0.8, 1, 1.2));
    public static final String FIREBALL_BARRAGE = BUILDER.add("fireball_barrage", AnimationsBuilder.definition(4.5)
            .marker("single", 3).marker("double", 3.8)
            .marker("triple", 0.8).marker("quad", 1.2, 3.4));
    public static final String EXPLOSION = BUILDER.add("explosion", AnimationsBuilder.definition(1.5).marker("attack", 0.8));
    public static final String FIRE_BREATH = BUILDER.add("fire_breath", AnimationsBuilder.definition(2).marker("attack", 0.8, 1.2));
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(10).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimationState, Sano>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(FIREBALL_3X, (anim, entity) -> {
            if (anim.isAt("attack"))
                RuneCraftorySpells.FIREBALL.get().use(entity);
        });
        b.put(FIREBALL_BARRAGE, (anim, entity) -> {
            if (anim.isAt("single"))
                RuneCraftorySpells.FIREBALL.get().use(entity);
            if (anim.isAt("double"))
                RuneCraftorySpells.DOUBLE_FIRE_BALL.get().use(entity);
            if (anim.isAt("triple"))
                RuneCraftorySpells.TRIPLE_FIRE_BALL.get().use(entity);
            if (anim.isAt("quad"))
                RuneCraftorySpells.QUAD_FIRE_BALL.get().use(entity);
        });
        b.put(EXPLOSION, (anim, entity) -> {
            if (anim.isAt("attack"))
                RuneCraftorySpells.EXPLOSION.get().use(entity);
        });
        b.put(FIRE_BREATH, (anim, entity) -> {
            if (anim.isAt("attack")) {
                Vec3 from = FireWallSpell.offset(entity);
                Vec3 dir = entity.getTarget() != null ? EntityUtils.getStraightProjectileTarget(from, entity.getTarget()).subtract(from).normalize() : entity.getLookAngle();
                dir = dir.scale(10).add(entity.random.nextGaussian() * 2.3, -Math.abs(entity.random.nextGaussian()) * 0.6, entity.random.nextGaussian() * 2.3);
                entity.setTargetPosition(TargetPosition.of(entity.position().add(dir)));
                RuneCraftorySpells.FIRE_WALL.get().use(entity);
            }
        });
    });

    private final AnimationHandler<Sano> animationHandler = new AnimationHandler<>(this, ANIMS);

    private Uno other;

    public Sano(EntityType<? extends Sano> type, Level level) {
        super(type, level);
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<Sano>create()
                .start(MonsterBehaviourUtils.checkedAttack(FIREBALL_3X)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(FIREBALL_BARRAGE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(EXPLOSION)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(FIRE_BREATH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(10)
                .build();
    }

    @Override
    public void handleAttack(AnimationState anim) {
        BiConsumer<AnimationState, Sano> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AnimationHandler<Sano> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), RuneCraftorySpells.FIRE_WALL.get()))
                    this.getAnimationHandler().setAnimation(FIRE_BREATH);
            } else if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), RuneCraftorySpells.FIREBALL_BARRAGE.get()))
                    this.getAnimationHandler().setAnimation(FIREBALL_BARRAGE);
            } else if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), RuneCraftorySpells.TRIPLE_FIRE_BALL.get()))
                this.getAnimationHandler().setAnimation(FIREBALL_3X);
        }
    }

    @Override
    public Uno getLinked() {
        if (this.other != null && !this.other.isRemoved()) {
            return this.other;
        }
        if (this.getLinkedID() != null) {
            List<Uno> results = this.level().getEntities(EntityTypeTest.forClass(Uno.class), this.getBoundingBox().inflate(64), e -> this.getLinkedID().equals(e.getLinkedID()));
            if (!results.isEmpty()) {
                this.other = results.getFirst();
            }
        }
        return this.other;
    }

    @Override
    public String getDeathAnimation() {
        return DEFEAT;
    }
}
