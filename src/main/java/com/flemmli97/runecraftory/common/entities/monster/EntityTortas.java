package com.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.ChargingMonster;
import com.flemmli97.runecraftory.common.entities.SwimWalkMoveController;
import com.flemmli97.runecraftory.common.entities.monster.ai.ChargeAttackGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityTortas extends ChargingMonster {

    public final ChargeAttackGoal<EntityTortas> ai = new ChargeAttackGoal<>(this);
    public static final AnimatedAction bite = new AnimatedAction(11, 6, "bite");
    public static final AnimatedAction spin = new AnimatedAction(51, 0, "spin");
    //public static final AnimatedAction swim = new AnimatedAction(32, 6, "swim");
    //public static final AnimatedAction walk = new AnimatedAction(21, 5, "walk");

    private static final AnimatedAction[] anims = new AnimatedAction[]{bite, spin};

    protected final SwimmerPathNavigator waterNavigator;
    protected final GroundPathNavigator groundNavigator;

    public EntityTortas(EntityType<? extends EntityTortas> type, World world) {
        super(type, world);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.goalSelector.addGoal(2, this.ai);
        this.moveController = new SwimWalkMoveController(this);
        this.goalSelector.removeGoal(this.swimGoal);
        this.waterNavigator = new SwimmerPathNavigator(this, world);
        this.groundNavigator = new GroundPathNavigator(this, world);
        this.wander.setExecutionChance(2);
        this.stepHeight = 1;
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.16);
        super.applyAttributes();
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.7f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public AxisAlignedBB calculateAttackAABB(AnimatedAction anim, LivingEntity target) {
        if (anim != null && anim.getID().equals(spin.getID()))
            return this.attackAABB(anim).offset(this.getX(), this.getY(), this.getZ());
        return super.calculateAttackAABB(anim, target);
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        if (anim != null && anim.getID().equals(spin.getID()))
            return super.animationCooldown(anim) * 3;
        return super.animationCooldown(anim);
    }

    @Override
    public void doWhileCharge() {
        if (this.ticksExisted % 10 == 0) {
            this.hitEntity.clear();
        }
    }

    @Override
    public boolean handleChargeMovement() {
        Vector3d prevMotion = this.getMotion();
        if (this.getAttackTarget() != null) {
            Vector3d pos = this.getPositionVec();
            Vector3d target = this.getAttackTarget().getPositionVec();
            Vector3d mot = target.subtract(pos.x, this.isInWater() ? pos.y : target.y, pos.z).normalize().scale(0.4);
            this.setMotion(mot.x, mot.y, mot.z);
            if (!this.isOnGround() && !this.isInWater())
                this.setMotion(this.getMotion().add(0, prevMotion.y, 0));
        } else {
            Vector3d look = this.getVectorForRotation(this.isInWater() ? this.rotationPitch : 0, this.rotationYaw).scale(0.4);
            this.setMotion(look.x, look.y, look.z);
            if (!this.isOnGround() && !this.isInWater())
                this.setMotion(this.getMotion().add(0, prevMotion.y, 0));
        }
        return true;
    }

    @Override
    public boolean adjustRotFromRider(LivingEntity rider) {
        return true;
    }

    @Override
    public float chargingYaw() {
        return this.rotationYaw;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1;
    }

    @Override
    public float chargingLength() {
        return 9;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.CHARGE) {
            return anim.getID().equals(spin.getID());
        }
        return type == AnimationType.MELEE && anim.getID().equals(bite.getID());
    }

    @Override
    public void setDoJumping(boolean jump) {
    }

    @Override
    public void handleRidingCommand(int command) {
        if (this.getAnimation() == null) {
            if (command == 2)
                this.setAnimation(spin);
            else
                this.setAnimation(bite);
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
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
    public void updateSwimming() {
        if (!this.world.isRemote) {
            if (this.isInWater()) {
                this.navigator = this.waterNavigator;
                this.wander.setExecutionChance(2);
                this.setSwimming(true);
            } else {
                this.navigator = this.groundNavigator;
                this.wander.setExecutionChance(100);
                this.setSwimming(false);
            }
        }
    }

    @Override
    public void travel(Vector3d vec) {
        if (this.isServerWorld() && this.isInWater()) {
            this.handleWaterTravel(vec);
        } else {
            super.travel(vec);
        }
    }

    @Override
    public double ridingSpeedModifier() {
        return 0.4;
    }

    @Override
    public double getMountedYOffset() {
        return this.getHeight() * 0.7D;
    }
}
