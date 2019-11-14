package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.javahelper.MathUtils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class EntityAIAttackBase<T extends EntityMobBase> extends EntityAIBase {

    protected T attacker;
    protected EntityLivingBase target;
    protected AnimatedAction next;
    protected String prevAnim;
    protected int iddleTime, pathFindDelay;
    protected double distanceToTargetSq;
    protected boolean movementDone;

    public EntityAIAttackBase(T entity) {
        this.attacker = entity;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        return entitylivingbase != null && entitylivingbase.isEntityAlive();
    }

    @Override
    public boolean shouldContinueExecuting() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        return entitylivingbase != null && entitylivingbase.isEntityAlive() && this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(entitylivingbase));
    }

    @Override
    public void resetTask() {
        this.attacker.getNavigator().clearPath();
    }

    public abstract AnimatedAction randomAttack();

    public abstract void handlePreAttack();

    public abstract void handleAttack(AnimatedAction anim);

    public abstract void handleIddle();

    public int coolDown(AnimatedAction anim) {
        return this.attacker.animationCooldown(this.next);
    }

    @Override
    public void updateTask() {
        this.target = this.attacker.getAttackTarget();
        AnimatedAction anim = this.attacker.getAnimation();
        this.distanceToTargetSq = this.attacker.getDistanceSq(target);
        --pathFindDelay;
        if (anim != null)
            this.prevAnim = anim.getID();
        if (this.next == null && anim == null) {
            if (this.iddleTime <= 0) {
                this.next = this.randomAttack();
                this.iddleTime = this.coolDown(this.next);
                this.movementDone=false;
            } else {
                this.handleIddle();
                this.iddleTime--;
            }
        }
        if (this.next != null) {
            this.handlePreAttack();
            if (this.movementDone) {
                this.attacker.setAnimation(anim = this.next);
                this.next = null;
            }
        }
        if (anim != null)
            this.handleAttack(anim);
    }

    protected void moveRandomlyAround() {
        this.moveRandomlyAround(81);
    }

    protected void moveRandomlyAround(double maxDistSq) {
        this.attacker.getLookHelper().setLookPositionWithEntity(this.target, 30.0f, 30.0f);
        if (this.distanceToTargetSq <= maxDistSq) {
            if (this.attacker.getNavigator().noPath()) {
                Vec3d rand = RandomPositionGenerator.findRandomTarget(this.attacker, 5, 4);
                if (rand != null)
                    this.attacker.getNavigator().tryMoveToXYZ(rand.x, rand.y, rand.z, 1);
            }
        } else
            this.attacker.getNavigator().tryMoveToEntityLiving(this.target, 1.5);
    }

    protected void moveToWithDelay(double x, double y, double z, double speed) {
        if (this.pathFindDelay <= 0) {
            if (!this.attacker.getNavigator().tryMoveToXYZ(x, y, z, speed))
                this.pathFindDelay += 15;
            this.pathFindDelay += this.attacker.getRNG().nextInt(10) + 10;
        }
    }

    protected void moveToWithDelay(double speed) {
        if (this.pathFindDelay <= 0) {
            if (!this.attacker.getNavigator().tryMoveToEntityLiving(this.target, speed))
                this.pathFindDelay += 15;
            this.pathFindDelay += this.attacker.getRNG().nextInt(10) + 10;
        }
    }

    protected BlockPos randomPosAwayFrom(EntityLivingBase away, float minDis) {
        double angle = Math.random() * 3.141592653589793 * 2.0;
        double x = Math.cos(angle) * minDis;
        double z = Math.sin(angle) * minDis;
        float min = minDis * minDis;
        BlockPos pos = this.attacker.getPosition().add(x, 0.0, z);
        if (away.getDistanceSq(pos) > min && (!this.attacker.hasHome() || this.attacker.getDistanceSq(pos) < this.attacker.getMaximumHomeDistance() * this.attacker.getMaximumHomeDistance())) {
            return pos;
        }
        return this.attacker.getPosition();
    }

    /**
     * Circle around given point. y coord not needed
     */
    protected void circleAround(double posX, double posZ, float radius, boolean clockWise, float speed) {
        double x = this.attacker.posX - posX;
        double z = this.attacker.posZ - posZ;
        double r = x * x + z * z;
        if (r < (radius - 1.5) * (radius - 1.5) || r > (radius + 1.5) * (radius + 1.5)) {
            double[] c = MathUtils.closestOnCircle(posX, posZ, this.attacker.posX, this.attacker.posZ, radius);
            this.attacker.getNavigator().tryMoveToXYZ(c[0], this.attacker.posY, c[1], speed);
        } else {
            double angle = MathUtils.phiFromPoint(posX, posZ, this.attacker.posX, this.attacker.posZ) + (clockWise ? MathUtils.degToRad(15) : -MathUtils.degToRad(15));
            double nPosX = radius * Math.cos(angle);
            double nPosZ = radius * Math.sin(angle);
            this.attacker.getNavigator().tryMoveToXYZ(posX + nPosX, this.attacker.posY, posZ + nPosZ, speed);
        }
    }
    
    protected void circleAroundFacing(double posX, double posZ, float radius, boolean clockWise, float speed) {
        double x = this.attacker.posX - posX;
        double z = this.attacker.posZ - posZ;
        double r = x * x + z * z;
        this.attacker.getMoveHelper().strafe(r < (radius - 1.5) * (radius - 1.5) ? -0.5f : r > (radius + 1.5) * (radius + 1.5)?0.5f:0, clockWise ? 0.5f : -0.5f);
        this.attacker.faceEntity(target, 30.0f, 30.0f);
    }

    protected void teleportAround(double posX, double posY, double posZ, int range) {
        double x = posX + (this.attacker.getRNG().nextDouble() - 0.5D) * range * 2;
        double y = posY + (this.attacker.getRNG().nextInt(3));
        double z = posZ + (this.attacker.getRNG().nextDouble() - 0.5D) * range * 2;
        this.attacker.attemptTeleport(x, y, z);
    }

    protected double getAttackReachSqr(EntityLivingBase attackTarget, float mult) {
        return (this.attacker.width * 2.0f * this.attacker.width * 2.0f + attackTarget.width) * mult;
    }
}
