package io.github.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.SwimWalkMoveController;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.AirWanderGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.AnimatedRangedGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class EntitySkyFish extends BaseMonster {

    public AnimatedRangedGoal<EntitySkyFish> rangedGoal = new AnimatedRangedGoal<>(this, 8, e -> true);

    public static final AnimatedAction slap = new AnimatedAction(11, 6, "slap");
    public static final AnimatedAction beam = new AnimatedAction(14, 7, "beam");
    public static final AnimatedAction swipe = new AnimatedAction(16, 4, "swipe");

    private static final AnimatedAction[] anims = new AnimatedAction[]{slap, beam, swipe};

    public EntitySkyFish(EntityType<? extends BaseMonster> type, World world) {
        super(type, world);
        this.goalSelector.removeGoal(this.wander);
        this.goalSelector.addGoal(2, this.wander = new AirWanderGoal(this));
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        //this.setPathPriority(PathNodeType.OPEN, 0.5f);
        this.goalSelector.addGoal(2, this.rangedGoal);
        this.moveController = new FlySwimMoveController(this);
        this.goalSelector.removeGoal(this.swimGoal);
        this.wander.setExecutionChance(50);
        this.setNoGravity(true);
    }

    @Override
    protected PathNavigator createNavigator(World world) {
        return new FloatingFlyNavigator(this, world);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.13);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        super.applyAttributes();
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.RANGED) {
            return anim.getID().equals(beam.getID()) || anim.getID().equals(swipe.getID());
        }
        return type == AnimationType.MELEE && anim.getID().equals(slap.getID());
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.75f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (this.getAnimation() == null) {
            if (command == 2)
                this.setAnimation(swipe);
            else if (command == 1)
                this.setAnimation(beam);
            else
                this.setAnimation(slap);
        }
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(beam.getID())) {
            this.getNavigator().clearPath();
            if (anim.canAttack()) {
                ModSpells.WATERLASER.get().use((ServerWorld) this.world, this, null);
            }
        } else if (anim.getID().equals(swipe.getID())) {
            if (anim.canAttack()) {
                EntityWaterLaser laser = new EntityWaterLaser(this.world, this, -8).setMaxTicks(5);
                Vector3d dir;
                if (this.getAttackTarget() != null) {
                    dir = this.getAttackTarget().getEyePosition(1).subtract(this.getPositionVec());
                } else {
                    dir = this.getLookVec();
                }
                laser.setRotationToDirWithOffset(dir.getX(), dir.getY(), dir.getZ(), 0, 20);
                this.world.addEntity(laser);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote && this.getAttackTarget() == null && !this.isInWater() && this.belowSoldid()) {
            Vector3d mot = this.getMotion();
            double newY = Math.max(0, mot.y);
            newY += 0.03;
            this.setMotion(mot.x, Math.min(0.3, newY), mot.z);
        }
    }

    private boolean belowSoldid() {
        BlockPos pos = this.getPosition().down();
        return this.world.getBlockState(pos).canSpawnMobs(this.world, pos, this);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    protected void updateFallState(double dist, boolean groundLogic, BlockState state, BlockPos pos) {
    }

    //==========Water stuff

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean canBeRiddenInWater() {
        return false;
    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.WATER;
    }

    @Override
    public void travel(Vector3d vec) {
        this.handleWaterTravel(vec);
    }

    static class FlySwimMoveController extends SwimWalkMoveController {

        public FlySwimMoveController(MobEntity entity) {
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
