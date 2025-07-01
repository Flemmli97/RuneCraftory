package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.monster.EntitySanoUno;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;
import java.util.function.BiConsumer;

public class EntityUno extends EntitySanoUno {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String WATER_LASER = BUILDER.add("water_laser", AnimationsBuilder.definition(2)
            .marker("attack_1", 0.8).marker("attack_2", 1.2));
    public static final String WATER_LASER_2 = BUILDER.add("water_swipe", AnimationsBuilder.definition(2)
            .marker("attack_1", 0.8).marker("attack_2", 1.6));
    public static final String ICEBALLS_5 = BUILDER.add("iceballs", AnimationsBuilder.definition(3).marker("attack", 0.8, 1.2, 1.6, 2, 2.4));
    public static final String HOMING_WATER_WAVE = BUILDER.add("water_wave", AnimationsBuilder.definition(2).marker("attack", 0.8));
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(10).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimationState, EntityUno>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(WATER_LASER, (anim, entity) -> {
            if (anim.isAt("attack_1"))
                ModSpells.WATER_LASER_LONG.get().use(entity);
            if (anim.isAt("attack_2"))
                ModSpells.PARALLEL_LASER_LONG.get().use(entity);
        });
        b.put(WATER_LASER_2, (anim, entity) -> {
            if (anim.isAt("attack_1")) {
                entity.reversedSwipe = false;
                ModSpells.WATER_SWIPE.get().use(entity);
            }
            if (anim.isAt("attack_2")) {
                entity.reversedSwipe = true;
                ModSpells.WATER_SWIPE.get().use(entity);
            }
        });
        b.put(ICEBALLS_5, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.ICE_BALL_DROP.get().use(entity);
            }
        });
        b.put(HOMING_WATER_WAVE, (anim, entity) -> {
            if (anim.isAt("attack"))
                ModSpells.ICE_TRAIL.get().use(entity);
        });
    });

    private final AnimationHandler<EntityUno> animationHandler = new AnimationHandler<>(this, ANIMS);
    private EntitySano other;

    public EntityUno(EntityType<? extends EntityUno> type, Level world) {
        super(type, world);
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<EntitySano>create()
                .start(MonsterBehaviourUtils.checkedAttack(WATER_LASER)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(WATER_LASER_2)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(ICEBALLS_5)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(HOMING_WATER_WAVE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(10)
                .build();
    }

    @Override
    public void handleAttack(AnimationState anim) {
        BiConsumer<AnimationState, EntityUno> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AnimationHandler<EntityUno> getAnimationHandler() {
        return this.animationHandler;
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
    public EntitySano getLinked() {
        if (this.other != null && !this.other.isRemoved()) {
            return this.other;
        }
        if (this.getLinkedID() != null) {
            List<EntitySano> results = this.level().getEntities(EntityTypeTest.forClass(EntitySano.class), this.getBoundingBox().inflate(64), e -> this.getLinkedID().equals(e.getLinkedID()));
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
