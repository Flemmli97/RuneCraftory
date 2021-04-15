package com.flemmli97.runecraftory.common.entities.monster.ai;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;
import net.minecraft.util.math.AxisAlignedBB;

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
        AxisAlignedBB aabb = this.attacker.calculateAttackAABB(this.next, this.target);
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
