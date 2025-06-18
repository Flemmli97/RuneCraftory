package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityBigMuck extends BaseMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String SLAP = BUILDER.add("slap", AnimationsBuilder.definition(1.2).marker("attack", 0.36));
    public static final String SPORE = BUILDER.add("spore", AnimationsBuilder.definition(2.16).marker("attack", 0.96));
    public static final String INTERACT = BUILDER.add("interact", SLAP);
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    //    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityBigMuck>>> ATTACKS = List.of(
//            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(SLAP, e -> 1), 1),
//            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(SPORE, e -> 1), 2)
//    );
//    private static final List<WeightedEntry.Wrapper<IdleAction<EntityBigMuck>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 2),
//            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(12, 4)), 1)
//    );
//
//    public AnimatedAttackGoal<EntityBigMuck> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityBigMuck> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityBigMuck(EntityType<? extends EntityBigMuck> type, Level world) {
        super(type, world);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
    }

    @Override
    public AnimationHandler<EntityBigMuck> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.equals(SPORE)) {
            return new OrientedBoundingBox(this.attackBB(anim), this.getYRot(), 0, this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        if (anim.equals(SPORE)) {
            double attackSize = this.getBbWidth() * 1.5;
            return new AABB(-attackSize, -0.2, -attackSize, attackSize, this.getBbHeight() + 0.2, attackSize);
        }
        double width = this.getBbWidth() * 1.4;
        double length = this.getBbWidth() * 2;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(SPORE)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                ModSpells.SPORE_CIRCLE_SPELL.get().use(this);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 1 ? ModSpells.SPORE_CIRCLE_SPELL.get() : null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(SPORE);
            else
                this.getAnimationHandler().setAnimation(SLAP);
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
//        return new Vec3(0, 26 / 16d, -3 / 16d);
//    }
}
