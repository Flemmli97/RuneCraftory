package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedRangedGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.NearestTargetHorizontal;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityLeafBall extends BaseMonster {

    public static final AnimatedAction MELEE = new AnimatedAction(15, 6, "tackle");
    public static final AnimatedAction WIND = new AnimatedAction(15, 10, "wind_blade");
    public static final AnimatedAction SLEEP_ATTACK = new AnimatedAction(15, 10, "sleep_aura");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction STILL = AnimatedAction.builder(1, "still").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, WIND, SLEEP_ATTACK, INTERACT, STILL};

    public final AnimatedRangedGoal<EntityLeafBall> attack = new AnimatedRangedGoal<>(this, 8, false, e -> true);
    private final AnimationHandler<EntityLeafBall> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityLeafBall(EntityType<? extends EntityLeafBall> type, Level world) {
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
            return anim.getID().equals(WIND.getID()) || (anim.getID().equals(SLEEP_ATTACK.getID()) && this.getRandom().nextFloat() < 0.6f);
        }
        return type == AnimationType.MELEE && anim.getID().equals(MELEE.getID());
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        return this.getRandom().nextInt(20) + 30;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 0.8;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(WIND.getID())) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                ModSpells.DOUBLESONIC.get().use(this);
            }
        } else if (anim.getID().equals(SLEEP_ATTACK.getID())) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                ModSpells.SLEEP_AURA.get().use(this);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 1)
                this.getAnimationHandler().setAnimation(MELEE);
            else
                this.getAnimationHandler().setAnimation(WIND);
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        if (type == AnimationType.MELEE)
            return 0.6f;
        return 1;
    }

    @Override
    public AnimationHandler<EntityLeafBall> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return STILL;
    }
}
