package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ElementalAttackMob;
import io.github.flemmli97.runecraftory.common.entities.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
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
import java.util.Optional;
import java.util.function.Predicate;

public class EntityDemon extends BaseMonster implements HealingPredicateEntity, ElementalAttackMob {

    public static final AnimatedAction DARK = new AnimatedAction(0.88, 0.52, "cast");
    public static final AnimatedAction HEAL = AnimatedAction.copyOf(DARK, "heal");
    public static final AnimatedAction STAB = new AnimatedAction(0.68, 0.4, "stab");
    public static final AnimatedAction STAB_LONG = new AnimatedAction(0.8, 0.48, "stab_long");
    public static final AnimatedAction SWIPE = new AnimatedAction(0.88, 0.44, "swipe");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(DARK, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
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
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1)), 1)
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
            if (anim.canAttack()) {
                ModSpells.DARK_BALL.get().use(this);
            }
        } else if (anim.is(HEAL)) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                ModSpells.CURE_ALL.get().use(this);
            }
        } else if (anim.is(STAB) || anim.is(STAB_LONG)) {
            this.getNavigation().stop();
            if (anim.getTick() == 1 && this.getTarget() != null) {
                this.lookAtNow(this.getTarget(), 360, 90);
            }
            if (anim.canAttack()) {
                final float range = anim.is(STAB_LONG) ? 5 : 2.5f;
                AABB aabb = AABB.ofSize(this.position(), 2 * (range + 1), 2 * (range + 1), 2 * (range + 1));
                this.level.getEntitiesOfClass(LivingEntity.class, aabb, e -> this.hitPred.test(e) && this.spearHit(e, range))
                        .forEach(this::doHurtTarget);
            }
        } else {
            super.handleAttack(anim);
        }
    }

    protected boolean spearHit(Entity e, float range) {
        final float width = 2;
        Vec3 from = this.position().add(0, this.getBbHeight() * 0.5, 0);
        Vec3 to = from.add(this.getLookAngle().scale(range));
        if (e.isSpectator() || !e.isAlive() || !e.isPickable())
            return false;
        AABB aabb = e.getBoundingBox().inflate(width + 0.3);
        Optional<Vec3> ray = aabb.clip(from, to);
        if (ray.isEmpty() && !aabb.contains(this.position()))
            return false;
        double dist = MathUtils.distTo(e, from, to);
        Vec3 dir = to.subtract(from).normalize().scale(0.1);
        double maxdist = width + e.getBbWidth() + 0.3;
        return dist <= maxdist * maxdist && MathUtils.isInFront(e.position(), from, dir);
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
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.is(STAB))
            return 2;
        if (anim.is(STAB_LONG))
            return 4;
        if (anim.is(SWIPE))
            return 2.5;
        return super.maxAttackRange(anim);
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