package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedRangedGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.NearestTargetHorizontal;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityLightBall;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EntityFairy extends BaseMonster implements HealingPredicateEntity {

    public static final AnimatedAction LIGHT = new AnimatedAction(15, 6, "light");
    public static final AnimatedAction WIND = new AnimatedAction(15, 10, "wind");
    public static final AnimatedAction HEAL = AnimatedAction.copyOf(LIGHT, "heal");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(LIGHT, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{LIGHT, WIND, HEAL, INTERACT, SLEEP};
    public final AnimatedRangedGoal<EntityFairy> attack = new AnimatedRangedGoal<>(this, 8, e -> true);
    private final AnimationHandler<EntityFairy> animationHandler = new AnimationHandler<>(this, ANIMS);

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

    public EntityFairy(EntityType<? extends EntityFairy> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
        this.setNoGravity(true);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.35);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    protected NearestAttackableTargetGoal<Player> createTargetGoalPlayer() {
        return new NearestTargetHorizontal<>(this, Player.class, 5, true, true, player -> !this.isTamed());
    }

    @Override
    protected NearestAttackableTargetGoal<Mob> createTargetGoalMobs() {
        return new NearestTargetHorizontal<>(this, Mob.class, 5, true, true, this.targetPred);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.RANGED) {
            if (anim.is(LIGHT))
                return this.level.getEntities(this, this.getBoundingBox().inflate(4), e -> e instanceof EntityLightBall light && light.getOwner() == this).size() < 2;
            return anim.is(WIND) || this.getRandom().nextFloat() < 0.45f && anim.is(HEAL);
        }
        return false;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(LIGHT)) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                ModSpells.SHINE.get().use(this);
            }
        } else if (anim.is(WIND)) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                ModSpells.DOUBLE_SONIC.get().use(this);
            }
        } else if (anim.is(HEAL)) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                ModSpells.CURE_ALL.get().use(this);
            }
        }
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 1 ? ModSpells.SHINE.get() : ModSpells.DOUBLE_BULLET.get()))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(LIGHT);
            else
                this.getAnimationHandler().setAnimation(WIND);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_FAIRY_AMBIENT.get();
    }

    @Override
    public float attackChance(AnimationType type) {
        return type != AnimationType.MELEE ? 1 : 0;
    }

    @Override
    public AnimationHandler<EntityFairy> getAnimationHandler() {
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
        return new Vec3(0, 10 / 16d, -6 / 16d);
    }
}