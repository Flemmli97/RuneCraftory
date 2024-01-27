package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedRangedGoal;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EntitySpider extends BaseMonster {

    private static final EntityDataAccessor<Boolean> climbingSync = SynchedEntityData.defineId(EntitySpider.class, EntityDataSerializers.BOOLEAN);

    public static final AnimatedAction MELEE = new AnimatedAction(13, 9, "attack");
    public static final AnimatedAction WEBSHOT = new AnimatedAction(14, 6, "webshot");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction STILL = AnimatedAction.builder(1, "still").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, WEBSHOT, INTERACT, STILL};
    private final AnimationHandler<EntitySpider> animationHandler = new AnimationHandler<>(this, ANIMS);
    public AnimatedRangedGoal<EntitySpider> attack = new AnimatedRangedGoal<>(this, 7, (e) -> true);

    public int climbingTicker = -1;
    public static int climbMax = 9;

    public EntitySpider(EntityType<? extends EntitySpider> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.27);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(climbingSync, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
        if (this.isClimbing() && this.isAlive() && !this.playDeath()) {
            this.climbingTicker = Math.min(this.climbingTicker + 1, climbMax);
        } else
            this.climbingTicker = Math.max(this.climbingTicker - 1, -1);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    @Override
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }
    }

    public boolean isClimbing() {
        return this.entityData.get(climbingSync);
    }

    public void setClimbing(boolean climbing) {
        this.entityData.set(climbingSync, climbing);
    }

    @Override
    public float attackChance(AnimationType type) {
        if (type == AnimationType.MELEE)
            return 0.7f;
        return 1;
    }

    @Override
    public AnimationHandler<EntitySpider> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.RANGED)
            return anim.is(WEBSHOT);
        if (type == AnimationType.MELEE)
            return anim.is(MELEE);
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.isAnimOfType(anim, AnimationType.RANGED)) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                if (this.getTarget() != null && this.getSensing().hasLineOfSight(this.getTarget()) || this.getFirstPassenger() instanceof Player) {
                    ModSpells.WEB_SHOT.get().use(this);
                }
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 1 ? ModSpells.WEB_SHOT.get() : null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(WEBSHOT);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return (int) ((super.calculateFallDamage(distance, damageMultiplier) - 3) * 0.5);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
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
