package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.EntityMobBase.AnimationType;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class EntityAIMeleeBase<T extends EntityMobBase> extends EntityAIAttackBase<T> {
    
    protected int iddleMoveDelay, iddleMoveFlag, attackMoveDelay;

    public EntityAIMeleeBase(T entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.getRNG().nextFloat() < this.attacker.attackChance())
            return this.attacker.getRandomAnimation(AnimationType.MELEE);
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }

    @Override
    public void handlePreAttack() {
        this.moveToWithDelay(1);
        if(this.attackMoveDelay<=0)
            this.attackMoveDelay = this.attacker.getRNG().nextInt(50)+100;
        Vec3d dir = this.target.getPositionVector().subtract(this.attacker.getPositionVector()).normalize();
        double reach = Math.min(this.attacker.maxAttackRange(this.next)*0.5 + this.attacker.width*0.5, Math.sqrt(this.distanceToTargetSq));
        Vec3d attackPos = this.attacker.getPositionVector().add(dir.scale(reach));
        AxisAlignedBB aabb = this.attacker.attackAABB(this.next).offset(attackPos.x, this.attacker.posY, attackPos.z);
        if (aabb.intersects(this.target.getEntityBoundingBox())) {
            this.movementDone = true;
            this.attacker.getLookHelper().setLookPositionWithEntity(this.target, 0, 0);
        }
        else if(this.attackMoveDelay--==1){
            this.attackMoveDelay = 0;
            this.next = null;
        }
    }

    @Override
    public void handleIddle() {
        if(this.iddleMoveDelay<=0) {
            this.iddleMoveFlag=this.attacker.getRNG().nextInt(3);
            this.iddleMoveDelay=this.attacker.getRNG().nextInt(35)+55-this.iddleMoveFlag*10;
        }
        switch(this.iddleMoveFlag) {
            case 0:
                this.moveToWithDelay(1);
                break;
            case 1:
                this.moveRandomlyAround(36);
                break;
        }
    }

    @Override
    public void updateTask() {
        super.updateTask();
        this.iddleMoveDelay--;
    }
}
