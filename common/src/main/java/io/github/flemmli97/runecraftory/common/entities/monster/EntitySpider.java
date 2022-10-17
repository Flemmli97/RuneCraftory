package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedRangedGoal;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpiderWeb;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EntitySpider extends BaseMonster {

    private static final EntityDataAccessor<Boolean> climbingSync = SynchedEntityData.defineId(EntitySpider.class, EntityDataSerializers.BOOLEAN);

    public static final AnimatedAction melee = new AnimatedAction(13, 9, "attack");
    public static final AnimatedAction webshot = new AnimatedAction(14, 6, "webshot");
    public static final AnimatedAction interact = AnimatedAction.copyOf(melee, "interact");
    private static final AnimatedAction[] anims = new AnimatedAction[]{melee, webshot, interact};
    private final AnimationHandler<EntitySpider> animationHandler = new AnimationHandler<>(this, anims);
    public AnimatedRangedGoal<EntitySpider> attack = new AnimatedRangedGoal<>(this, 7, (e) -> true);

    public int climbingTicker = -1;
    public static int climbMax = 9;

    public EntitySpider(EntityType<? extends EntitySpider> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
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
        return 1;
    }

    @Override
    public AnimationHandler<EntitySpider> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.RANGED)
            return anim.getID().equals(webshot.getID());
        if (type == AnimationType.MELEE)
            return anim.getID().equals(melee.getID());
        return false;
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        return this.getRandom().nextInt(10) + 30;
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
                if (this.getTarget() != null && this.getSensing().hasLineOfSight(this.getTarget())) {
                    this.shootWeb(this.getTarget());
                }
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 1)
                this.getAnimationHandler().setAnimation(webshot);
            else
                this.getAnimationHandler().setAnimation(melee);
        }
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return (int) ((super.calculateFallDamage(distance, damageMultiplier) - 3) * 0.5);
    }

    private void shootWeb(LivingEntity target) {
        EntitySpiderWeb web = new EntitySpiderWeb(this.level, this);
        Vec3 dir = new Vec3(target.getX() - web.getX(), target.getY(0.33) - web.getY(), target.getZ() - web.getZ());
        double l = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        dir = dir.add(0, l * 0.2, 0);
        web.shoot(dir.x, dir.y, dir.z, 1.3f, 7 - this.level.getDifficulty().getId() * 2);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(web);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(interact);
    }
}
