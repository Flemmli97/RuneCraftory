package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.AirWanderGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedRangedGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.NearestTargetHorizontal;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EntityWeagle extends BaseMonster {

    public static final AnimatedAction GALE = new AnimatedAction(19, 5, "gale");
    public static final AnimatedAction PECK = new AnimatedAction(11, 4, "peck");
    public static final AnimatedAction SWOOP = new AnimatedAction(14, 4, "swoop");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(PECK, "interact");
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(2, "defeat").infinite().changeDelay(AnimationHandler.DEFAULT_ADJUST_TIME).build();
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().changeDelay(AnimationHandler.DEFAULT_ADJUST_TIME).build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{GALE, PECK, SWOOP, INTERACT, DEFEAT, SLEEP};
    public AnimatedRangedGoal<EntityWeagle> rangedGoal = new AnimatedRangedGoal<>(this, 8, e -> true);
    protected List<LivingEntity> hitEntity;
    private final AnimationHandler<EntityWeagle> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeCons(anim -> this.hitEntity = null);

    public EntityWeagle(EntityType<? extends BaseMonster> type, Level world) {
        super(type, world);
        this.goalSelector.removeGoal(this.wander);
        this.goalSelector.addGoal(6, this.wander = new AirWanderGoal(this));
        this.goalSelector.addGoal(2, this.rangedGoal);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
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
            return anim.getID().equals(GALE.getID());
        }
        return type == AnimationType.MELEE && (anim.getID().equals(PECK.getID()) || anim.getID().equals(SWOOP.getID()));
    }

    @Override
    public void travel(Vec3 vec) {
        if (this.isEffectiveAi() && this.isVehicle() && this.canBeControlledByRider() && this.getControllingPassenger() instanceof LivingEntity) {
            this.handleNoGravTravel(vec);
        } else {
            super.travel(vec);
        }
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.5;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(GALE.getID())) {
            if (anim.canAttack()) {
                ModSpells.GUSTSPELL.get().use(this);
            }
        } else if (anim.getID().equals(SWOOP.getID())) {
            if (this.hitEntity == null)
                this.hitEntity = new ArrayList<>();
            Vec3 dir;
            if (this.getTarget() != null) {
                dir = this.getTarget().position().subtract(this.position()).normalize();
            } else {
                dir = this.getLookAngle();
            }
            dir = new Vec3(dir.x(), -1.5, dir.z()).normalize().scale(0.5);
            if (anim.getTick() > anim.getAttackTime() && anim.getLength() - anim.getTick() > 3) {
                this.setDeltaMovement(dir.scale(0.55));
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
            } else {
                this.setDeltaMovement(dir.multiply(-0.3, -0.225, -0.3));
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, LivingEntity target) {
        if (anim.getID().equals(SWOOP.getID()))
            return super.calculateAttackAABB(anim, target).move(this.getDeltaMovement());
        return super.calculateAttackAABB(anim, target);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            switch (command) {
                case 2 -> this.getAnimationHandler().setAnimation(GALE);
                case 1 -> this.getAnimationHandler().setAnimation(SWOOP);
                default -> this.getAnimationHandler().setAnimation(PECK);
            }
        }
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    public float attackChance(AnimationType type) {
        if (type == AnimationType.MELEE)
            return 0.8f;
        return 1;
    }

    @Override
    public AnimationHandler<EntityWeagle> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected void checkFallDamage(double dist, boolean groundLogic, BlockState state, BlockPos pos) {
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return SLEEP;
    }
}
