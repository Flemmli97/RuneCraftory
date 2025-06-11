package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.utils.ElementalAttackMob;
import io.github.flemmli97.runecraftory.common.entities.utils.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class EntityDemon extends BaseMonster implements HealingPredicateEntity, ElementalAttackMob {

    public static final AnimatedAction DARK = AnimatedAction.builder(0.88, "cast").marker("attack", 0.52).build();
    public static final AnimatedAction HEAL = AnimatedAction.copyOf(DARK, "heal");
    public static final AnimatedAction STAB = AnimatedAction.builder(0.68, "stab").marker("attack", 0.4).build();
    public static final AnimatedAction STAB_LONG = AnimatedAction.builder(0.8, "stab_long").marker("attack", 0.48).build();
    public static final AnimatedAction SWIPE = AnimatedAction.builder(0.88, "swipe").marker("attack", 0.44).build();
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(DARK, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(0, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{DARK, HEAL, STAB, STAB_LONG, SWIPE, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityDemon>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(STAB, e -> 1), 3),
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(STAB_LONG, e -> 1), 3),
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(SWIPE, e -> 1), 3),
            WeightedEntry.wrap(new GoalAttackAction<EntityDemon>(DARK)
                    .cooldown(e -> e.animationCooldown(DARK))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityDemon>(HEAL)
                    .cooldown(e -> e.animationCooldown(HEAL))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 1)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityDemon>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 2),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 1)
    );

    public final AnimatedAttackGoal<EntityDemon> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityDemon> animationHandler = new AnimationHandler<>(this, ANIMS);

    private final Predicate<LivingEntity> healingPredicate = e -> {
        if (this.getOwnerUUID() == null) {
            if (e instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null)
                return false;
            return e instanceof Enemy && e != this.getTarget();
        }
        if (e instanceof OwnableEntity ownable && this.getOwnerUUID().equals(ownable.getOwnerUUID()))
            return true;
        return this.getOwnerUUID().equals(e.getUUID());
    };

    public EntityDemon(EntityType<? extends EntityDemon> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(DARK)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                ModSpells.DARK_BALL.get().use(this);
            }
        } else if (anim.is(HEAL)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                ModSpells.CURE_ALL.get().use(this);
            }
        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? ModSpells.DARK_BALL.get() : null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(DARK);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(SWIPE);
            else
                this.getAnimationHandler().setAnimation(STAB);
        }
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        double width = this.getBbWidth() * 1;
        double length = width;
        if (anim.is(STAB)) {
            width = this.getBbWidth() * 1.4;
            length = this.getBbWidth() * 2.6;
        } else if (anim.is(STAB_LONG)) {
            width = this.getBbWidth() * 1.4;
            length = this.getBbWidth() * 3.2;
        } else if (anim.is(SWIPE)) {
            width = this.getBbWidth() * 2.75;
            length = this.getBbWidth() * 2.3;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public AnimationHandler<EntityDemon> getAnimationHandler() {
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

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 15.5 / 16d, -5 / 16d);
    }

    @Override
    public EnumElement getAttackElement() {
        return EnumElement.DARK;
    }
}