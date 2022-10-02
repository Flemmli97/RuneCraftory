package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.SwimWalkMoveController;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.AirWanderGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.AnimatedRangedGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.NearestTargetHorizontal;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class EntitySkyFish extends BaseMonster {

    public static final AnimatedAction slap = new AnimatedAction(11, 6, "slap");
    public static final AnimatedAction beam = new AnimatedAction(14, 7, "beam");
    public static final AnimatedAction swipe = new AnimatedAction(16, 4, "swipe");
    public static final AnimatedAction interact = AnimatedAction.copyOf(slap, "interact");
    private static final AnimatedAction[] anims = new AnimatedAction[]{slap, beam, swipe, interact};
    private final AnimationHandler<EntitySkyFish> animationHandler = new AnimationHandler<>(this, anims);
    public AnimatedRangedGoal<EntitySkyFish> rangedGoal = new AnimatedRangedGoal<>(this, 8, e -> true);

    public EntitySkyFish(EntityType<? extends BaseMonster> type, Level world) {
        super(type, world);
        this.goalSelector.removeGoal(this.wander);
        this.goalSelector.addGoal(6, this.wander = new AirWanderGoal(this));
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        //this.setPathPriority(BlockPathTypes.OPEN, 0.5f);
        this.goalSelector.addGoal(2, this.rangedGoal);
        this.moveControl = new FlySwimMoveController(this);
        this.goalSelector.removeGoal(this.swimGoal);
        this.wander.setInterval(50);
        this.setNoGravity(true);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.13);
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
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.getTarget() == null && !this.isInWater() && this.belowSoldid()) {
            Vec3 mot = this.getDeltaMovement();
            double newY = Math.max(0, mot.y);
            newY += 0.03;
            this.setDeltaMovement(mot.x, Math.min(0.3, newY), mot.z);
        }
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.RANGED) {
            return anim.getID().equals(beam.getID()) || anim.getID().equals(swipe.getID());
        }
        return type == AnimationType.MELEE && anim.getID().equals(slap.getID());
    }

    @Override
    public void travel(Vec3 vec) {
        this.handleNoGravTravel(vec);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(beam.getID())) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                ModSpells.WATERLASER.get().use((ServerLevel) this.level, this);
            }
        } else if (anim.getID().equals(swipe.getID())) {
            if (anim.canAttack()) {
                if (EntityUtils.sealed(this))
                    return;
                EntityWaterLaser laser = new EntityWaterLaser(this.level, this, -4).setMaxTicks(10);
                Vec3 dir;
                if (this.getTarget() != null) {
                    dir = this.getTarget().getEyePosition(1).subtract(this.position());
                } else {
                    dir = this.getLookAngle();
                }
                laser.setRotationToDirWithOffset(dir.x(), dir.y(), dir.z(), 0, 20);
                this.level.addFreshEntity(laser);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(swipe);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(beam);
            else
                this.getAnimationHandler().setAnimation(slap);
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.75f;
    }

    @Override
    public AnimationHandler<EntitySkyFish> getAnimationHandler() {
        return this.animationHandler;
    }

    private boolean belowSoldid() {
        BlockPos pos = this.blockPosition().below();
        return this.level.getBlockState(pos).entityCanStandOn(this.level, pos, this);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    //==========Water stuff

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean rideableUnderWater() {
        return false;
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(interact);
    }

    static class FlySwimMoveController extends SwimWalkMoveController {

        public FlySwimMoveController(Mob entity) {
            super(entity);
        }

        @Override
        protected void moveSpeed() {
        }

        @Override
        public void tick() {
            this.handleWaterMovement();
        }

    }
}
