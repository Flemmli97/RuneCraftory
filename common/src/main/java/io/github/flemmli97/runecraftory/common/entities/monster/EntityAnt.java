package io.github.flemmli97.runecraftory.common.entities.monster;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.SequentialBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.List;

public class EntityAnt extends BaseMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String MELEE = BUILDER.add("attack", AnimationsBuilder.definition(1.16).marker("attack", 0.6));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<EntityAnt> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityAnt(EntityType<? extends EntityAnt> type, Level world) {
        super(type, world);
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(MELEE).prepare(new SetWalkTargetToAttackTarget<>(), new MoveToAttackTarget<>())
                .end(1)
                .build();
        //e -> BrainUtils.setForgettableMemory(this, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.animationCooldown(MELEE))
//                .startCondition(e -> !BrainUtils.hasMemory(this, MemoryModuleType.ATTACK_COOLING_DOWN))
//                .cooldownFor(e -> e.animationCooldown(null));
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity entity) {
        return false;
    }

    @Override
    public List<Pair<ExtendedBehaviour<? extends BaseMonster>, Integer>> getIdleAI() {
        return List.of(Pair.of(new SequentialBehaviour<>(new SetWalkTargetToAttackTarget<>(), new MoveToWalkTarget<>()), 1),
                Pair.of(new SequentialBehaviour<>(new SetRandomWalkTarget<>(), new MoveToWalkTarget<>()), 1)
        );
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
        super.applyAttributes();
    }

    @Override
    public AnimationHandler<EntityAnt> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public int animationCooldown(String anim) {
        return Math.max(25, (int) (super.animationCooldown(anim) * 0.7));
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.4;
        double length = this.getBbWidth() * 1.5;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
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
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public boolean hasSleepingAnimation() {
        return true;
    }

//    @Override
//    public Vec3 passengerOffset(Entity passenger) {
//        Vec3 off = new Vec3(0, 6 / 16d, 0);
//        if (this.getType() == ModEntities.ANT.get())
//            return off.scale(0.7);
//        return off;
//    }
}
