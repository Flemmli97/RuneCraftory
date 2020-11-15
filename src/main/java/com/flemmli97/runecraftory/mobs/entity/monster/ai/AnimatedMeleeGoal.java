package com.flemmli97.runecraftory.mobs.entity.monster.ai;

import com.flemmli97.runecraftory.mobs.entity.AnimationType;
import com.flemmli97.runecraftory.mobs.entity.BaseMonster;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

public class AnimatedMeleeGoal<T extends BaseMonster> extends AnimatedAttackGoal<T> {

    protected int iddleMoveDelay, iddleMoveFlag, attackMoveDelay;

    public AnimatedMeleeGoal(T entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.getRNG().nextFloat() < this.attacker.attackChance(AnimationType.GENERICATTACK))
            return this.attacker.getRandomAnimation(AnimationType.MELEE);
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }

    @Override
    public void handlePreAttack() {
        this.moveToWithDelay(1);
        if (this.attackMoveDelay <= 0)
            this.attackMoveDelay = this.attacker.getRNG().nextInt(50) + 100;
        Vector3d dir = this.target.getPositionVec().subtract(this.attacker.getPositionVec()).normalize();
        double reach = Math.min(this.attacker.maxAttackRange(this.next) * 0.5 + this.attacker.getWidth() * 0.5, Math.sqrt(this.distanceToTargetSq));
        Vector3d attackPos = this.attacker.getPositionVec().add(dir.scale(reach));
        AxisAlignedBB aabb = this.attacker.attackAABB(this.next).offset(attackPos.x, this.attacker.getY(), attackPos.z);
        if (aabb.intersects(this.target.getBoundingBox())) {
            this.movementDone = true;
            this.attacker.getLookController().setLookPositionWithEntity(this.target, 0, 0);
        } else if (this.attackMoveDelay-- == 1) {
            this.attackMoveDelay = 0;
            this.next = null;
        }
    }

    @Override
    public void handleAttack(AnimatedAction animatedAction) {

    }

    @Override
    public void handleIddle() {
        if (this.iddleMoveDelay <= 0) {
            this.iddleMoveFlag = this.attacker.getRNG().nextInt(3);
            this.iddleMoveDelay = this.attacker.getRNG().nextInt(35) + 55 - this.iddleMoveFlag * 10;
        }
        switch (this.iddleMoveFlag) {
            case 0:
                this.moveToWithDelay(1);
                break;
            case 1:
                this.moveRandomlyAround(36);
                break;
        }
    }

    @Override
    public int coolDown(AnimatedAction animatedAction) {
        return this.attacker.animationCooldown(this.next);
    }

    @Override
    public void tick() {
        super.tick();
        this.iddleMoveDelay--;
    }
}
