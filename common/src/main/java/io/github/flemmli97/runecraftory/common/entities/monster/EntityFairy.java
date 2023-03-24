package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedRangedGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.NearestTargetHorizontal;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityLightBall;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.server.level.ServerLevel;
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

import java.util.function.Predicate;

public class EntityFairy extends BaseMonster implements HealingPredicateEntity {

    public static final AnimatedAction LIGHT = new AnimatedAction(15, 6, "light");
    public static final AnimatedAction WIND = new AnimatedAction(15, 10, "wind");
    public static final AnimatedAction HEAL = AnimatedAction.copyOf(LIGHT, "heal");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(LIGHT, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(2, "sleep").infinite().changeDelay(AnimationHandler.DEFAULT_ADJUST_TIME).build();
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
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        super.applyAttributes();
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
            if (anim.getID().equals(LIGHT.getID()))
                return this.level.getEntities(this, this.getBoundingBox().inflate(4), e -> e instanceof EntityLightBall light && light.getOwner() == this).size() < 2;
            return anim.getID().equals(WIND.getID()) || this.getRandom().nextFloat() < 0.45f && anim.getID().equals(HEAL.getID());
        }
        return false;
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        return this.getRandom().nextInt(10) + 30;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 0.8;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(LIGHT.getID())) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                ModSpells.SHINE.get().use((ServerLevel) this.level, this);
            }
        } else if (anim.getID().equals(WIND.getID())) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                ModSpells.DOUBLESONIC.get().use((ServerLevel) this.level, this);
            }
        } else if (anim.getID().equals(HEAL.getID())) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                ModSpells.CUREALL.get().use((ServerLevel) this.level, this);
            }
        }
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 1)
                this.getAnimationHandler().setAnimation(LIGHT);
            else
                this.getAnimationHandler().setAnimation(WIND);
        }
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
}
