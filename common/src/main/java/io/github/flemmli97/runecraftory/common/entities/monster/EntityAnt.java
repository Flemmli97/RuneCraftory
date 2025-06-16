package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;

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
    public BrainActivityGroup<? extends BaseMonster> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<BaseMonster>(),
                AttackBehaviourBuilder.<BaseMonster>create()
                        .start(MELEE).prepare(new SetWalkTargetToAttackTarget<>(), new MoveToAttackTarget<>())
                        .end(1)
                        .build()
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
    public int animationCooldown(AnimatedAction anim) {
        return Math.max(25, (int) (super.animationCooldown(anim) * 0.7));
    }

    @Override
    public AABB attackBB(String anim) {
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
