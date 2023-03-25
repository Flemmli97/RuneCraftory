package io.github.flemmli97.runecraftory.common.entities.monster.wisp;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.NearestTargetNoLoS;
import io.github.flemmli97.runecraftory.common.entities.ai.WispAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.NoClipFlyMoveController;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class EntityWispBase extends BaseMonster {

    public static final AnimatedAction ATTACK_FAR = new AnimatedAction(15, 6, "attack");
    public static final AnimatedAction ATTACK_CLOSE = AnimatedAction.builder(15, "attack_close").marker(10).withClientID("attack").build();
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(ATTACK_FAR, "interact");
    public static final AnimatedAction VANISH = new AnimatedAction(100, 50, "vanish");
    public static final AnimatedAction STILL = AnimatedAction.builder(1, "still").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{ATTACK_FAR, ATTACK_CLOSE, INTERACT, VANISH, STILL};
    public final WispAttackGoal<EntityWispBase> attack = new WispAttackGoal<>(this, 36);
    private boolean vanishNext;

    private final AnimationHandler<EntityWispBase> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeCons(anim -> {
                if (anim != null && anim.getID().equals(VANISH.getID()))
                    this.vanishNext = this.getRandom().nextFloat() < 0.6;
            });

    public EntityWispBase(EntityType<? extends EntityWispBase> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
        this.setNoGravity(true);
        this.noPhysics = true;
        this.moveControl = new NoClipFlyMoveController(this);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.12);
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        super.applyAttributes();
    }

    @Override
    protected NearestAttackableTargetGoal<Player> createTargetGoalPlayer() {
        return new NearestTargetNoLoS<>(this, Player.class, 5, false, player -> !this.isTamed());
    }

    @Override
    protected NearestAttackableTargetGoal<Mob> createTargetGoalMobs() {
        return new NearestTargetNoLoS<>(this, Mob.class, 5, false, this.targetPred);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.RANGED)
            return anim.getID().equals(ATTACK_FAR.getID()) || anim.getID().equals(ATTACK_CLOSE.getID());
        return false;
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        return this.getRandom().nextInt(10) + 30;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getAnimationHandler().isCurrentAnim(VANISH.getID()))
            return false;
        boolean ret = super.hurt(source, amount);
        if (ret)
            this.vanishNext = this.getRandom().nextFloat() < 0.4;
        return ret;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getTarget();
        if (anim.getID().equals(ATTACK_FAR.getID())) {
            this.getNavigation().stop();
            if (target != null)
                this.getLookControl().setLookAt(target, 360, 90);
            if (anim.canAttack()) {
                this.attackFar(target);
            }
        } else if (anim.getID().equals(ATTACK_CLOSE.getID())) {
            this.getNavigation().stop();
            if (target != null)
                this.getLookControl().setLookAt(target, 360, 90);
            if (anim.canAttack()) {
                this.attackClose(target);
            }
        } else if (anim.getID().equals(VANISH.getID())) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                if (target == null) {
                    double rX = this.getX() + (this.random.nextDouble() - 0.5) * 16;
                    double rY = this.getY() + (this.random.nextDouble() - 0.5) * 4;
                    double rZ = this.getZ() + (this.random.nextDouble() - 0.5) * 16;
                    this.teleport(rX, rY, rZ);
                } else {
                    this.teleportTowards(target);
                }
            }
        }
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 1)
                this.getAnimationHandler().setAnimation(ATTACK_CLOSE);
            else
                this.getAnimationHandler().setAnimation(ATTACK_FAR);
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    @Override
    public AnimationHandler<EntityWispBase> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void travel(Vec3 vec) {
        if (this.isEffectiveAi() && this.isVehicle() && this.canBeControlledByRider() && this.getControllingPassenger() instanceof LivingEntity entity) {
            this.noPhysics = entity.noPhysics;
            this.handleNoGravTravel(vec);
        } else {
            this.noPhysics = !this.playDeath();
            super.travel(vec);
        }
    }

    private void teleportTowards(Entity entity) {
        Vec3 look = new Vec3(entity.getLookAngle().x, 0, entity.getLookAngle().z).normalize().scale(-2.5);
        Vec3 behindEntity = entity.position().add(look);
        Vec3 dir = new Vec3(behindEntity.x - this.getX(), behindEntity.y - this.getY(), behindEntity.z - this.getZ());
        if (dir.lengthSqr() < 100)
            this.teleport(behindEntity.x, entity.getY(), behindEntity.z);
        else {
            dir = dir.normalize();
            double e = this.getX() + this.random.nextDouble() * 9 * dir.x;
            double g = this.getZ() + this.random.nextDouble() * 9 * dir.z;
            this.teleport(e, entity.getY(), g);
        }
    }

    private void teleport(double x, double y, double z) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(x, y, z);
        while (mutableBlockPos.getY() > this.level.getMinBuildHeight() && !this.level.getBlockState(mutableBlockPos).getMaterial().blocksMotion()) {
            mutableBlockPos.move(Direction.DOWN);
        }
        BlockState blockState = this.level.getBlockState(mutableBlockPos);
        if (!blockState.getMaterial().blocksMotion()) {
            y = this.getY();
        }
        this.teleportTo(x, y + 1, z);
    }

    public abstract void attackFar(LivingEntity target);

    public abstract void attackClose(LivingEntity target);

    public boolean shouldVanishNext(String prev) {
        LivingEntity target = this.getTarget();
        if (target != null && target.distanceToSqr(this) > 140)
            return true;
        return !prev.equals(VANISH.getID()) && this.vanishNext;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
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