package com.flemmli97.runecraftory.common.entity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class EntityAIAttackTarget extends EntityAIBase {

    private EntityMobBase taskOwner;
    private EntityLivingBase target;
    private Predicate<EntityLivingBase> pred;
    private Sorter sorter;
    private double maxDist;

    public EntityAIAttackTarget(EntityMobBase entity) {
        this.taskOwner = entity;
        this.setMutexBits(1);
        this.sorter = new Sorter(entity);
        this.pred = (e) -> {
            boolean flag = false;
            if (entity.isTamed()) {
                flag = IMob.VISIBLE_MOB_SELECTOR.apply(e);
            } else if (e instanceof EntityVillager)
                flag = true;
            else if (e instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) e;
                if (!player.capabilities.disableDamage && player.isEntityAlive() && !player.isSpectator()) {
                    double dist = player.getDistanceSq(e.posX, player.posY, e.posZ);
                    double maxD = this.maxDist;
                    if (player.isSneaking()) {
                        maxD *= 0.800000011920929D;
                    }
                    if (player.isInvisible()) {
                        maxD *= 0.7F * Math.max(0.1f, player.getArmorVisibility());
                    }
                    maxD = net.minecraftforge.common.ForgeHooks.getPlayerVisibilityDistance(player, maxD, this.maxDist);
                    if ((Math.abs(player.posY - e.posY) < this.maxDist * this.maxDist) && (dist < maxD * maxD)) {
                        flag = true;
                    }
                }
            }
            return flag && entity.isWithinHomeDistanceFromPosition(new BlockPos(e)) && EntityAITarget.isSuitableTarget(entity, e, false, false);
        };
    }

    @Override
    public boolean shouldExecute() {
        this.maxDist = this.getTargetDistance();
        List<EntityLivingBase> list = this.taskOwner.world.getEntitiesWithinAABB(EntityLivingBase.class, this.targetArea(this.maxDist), this.pred);
        if (list.isEmpty())
            return false;
        Collections.sort(list, this.sorter);
        this.target = list.get(0);
        return true;
    }

    private AxisAlignedBB targetArea(double targetDistance) {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, targetDistance, targetDistance);
    }

    private double getTargetDistance() {
        IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }

    @Override
    public boolean shouldContinueExecuting() {
        EntityLivingBase eTarget = this.taskOwner.getAttackTarget();
        if (eTarget == null)
            eTarget = this.target;
        if (eTarget == null || !eTarget.isEntityAlive())
            return false;
        else {
            Team team = this.taskOwner.getTeam();
            Team team1 = eTarget.getTeam();
            if (team != null && team1.equals(team))
                return false;
            else {
                double maxDis = this.getTargetDistance();
                if (this.taskOwner.getDistanceSq(eTarget) > maxDis * maxDis || !this.taskOwner.isWithinHomeDistanceFromPosition(new BlockPos(eTarget)))
                    return false;
                else {
                    if (eTarget instanceof EntityPlayer && (((EntityPlayer) eTarget).isSpectator() || ((EntityPlayer) eTarget).capabilities.disableDamage))
                        return false;
                    this.taskOwner.setAttackTarget(eTarget);
                    return true;
                }
            }
        }
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.target);
    }

    @Override
    public void resetTask() {
        this.taskOwner.setAttackTarget(null);
        this.target = null;
    }

    private static class Sorter implements Comparator<EntityLivingBase> {

        private final Entity entity;

        public Sorter(EntityMobBase entity) {
            this.entity = entity;
        }

        @Override
        public int compare(EntityLivingBase e1, EntityLivingBase e2) {
            int val1 = this.val(e1);
            int val2 = this.val(e2);
            if (val1 != val2) {
                if (val1 > val2)
                    return -1;
                return 1;
            }
            double d0 = this.entity.getDistanceSq(e1);
            double d1 = this.entity.getDistanceSq(e2);

            if (d0 < d1) {
                return -1;
            } else {
                return d0 > d1 ? 1 : 0;
            }
        }

        private int val(EntityLivingBase e) {
            if (e instanceof EntityPlayer)
                return 2;
            if (e instanceof EntityVillager)
                return 1;
            return 0;
        }
    }
}
