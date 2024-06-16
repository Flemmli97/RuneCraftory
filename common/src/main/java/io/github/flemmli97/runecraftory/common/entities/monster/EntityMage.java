package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedRangedGoal;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EntityMage extends BaseMonster implements HealingPredicateEntity {

    public static final AnimatedAction SWING = new AnimatedAction(0.64, 0.36, "swing");
    public static final AnimatedAction CAST_1 = new AnimatedAction(0.84, 0.4, "cast_1");
    public static final AnimatedAction CAST_DOUBLE = AnimatedAction.copyOf(CAST_1, "cast");
    public static final AnimatedAction CAST_2 = new AnimatedAction(0.92, 0.36, "cast_2");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(SWING, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{SWING, CAST_1, CAST_2, CAST_DOUBLE, INTERACT, SLEEP};
    public final AnimatedRangedGoal<EntityMage> attack = new AnimatedRangedGoal<>(this, 8, e -> true);
    private final AnimationHandler<EntityMage> animationHandler = new AnimationHandler<>(this, ANIMS);

    private final Predicate<LivingEntity> healingPredicate = e -> {
        if (this.getOwnerUUID() == null) {
            if (e instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null)
                return false;
            return e instanceof Enemy;
        }
        if (e instanceof OwnableEntity ownable && this.getOwnerUUID().equals(ownable.getOwnerUUID()))
            return true;
        return this.getOwnerUUID().equals(e.getUUID());
    };

    public EntityMage(EntityType<? extends EntityMage> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.RANGED) {
            return anim.is(CAST_1, CAST_DOUBLE, CAST_2);
        }
        return type == AnimationType.MELEE && this.random.nextFloat() < 0.8f && anim.is(SWING);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(CAST_1)) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                this.getFirstSpell().use(this);
            }
        } else if (anim.is(CAST_DOUBLE)) {
            this.getNavigation().stop();
            if (anim.canAttack() || anim.isAtTick(anim.getAttackTime() + 8)) {
                ModSpells.DARK_BALL.get().use(this);
            }
        } else if (anim.is(CAST_2)) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                this.getSecondSpell().use(this);
            }
        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            Spell spell = switch (command) {
                case 2 -> this.getSecondSpell();
                case 1 -> this.getFirstSpell();
                default -> null;
            };
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), spell))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(CAST_2);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(CAST_1);
            else
                this.getAnimationHandler().setAnimation(SWING);
        }
    }

    @Override
    public AnimationHandler<EntityMage> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public Predicate<LivingEntity> healeableEntities() {
        return this.healingPredicate;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return SLEEP;
    }

    public Spell getFirstSpell() {
        return ModSpells.PARALYSIS_BALL.get();
    }

    public Spell getSecondSpell() {
        return ModSpells.EXPANDING_QUAD_LIGHT.get();
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 13.5 / 16d, -5 / 16d);
    }
}