package io.github.flemmli97.runecraftory.common.entities.ai.boss;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedMonsterAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityRaccoon;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RaccoonAttackGoal<T extends EntityRaccoon> extends AnimatedMonsterAttackGoal<T> {

    private int moveDelay;
    private boolean moveFlag;

    public RaccoonAttackGoal(T entity) {
        super(entity);
    }

    @Override
    public boolean canUse() {
        return !this.attacker.canBeControlledByRider() && super.canUse();
    }

    @Override
    public void stop() {
        super.stop();
        this.moveDelay = 0;
        this.moveFlag = false;
    }

    @Override
    public AnimatedAction randomAttack() {
        AnimatedAction anim = this.attacker.getRandomAnimation(AnimationType.GENERICATTACK);
        if (anim == null || (this.attacker.isBerserk() && anim.getID().equals(this.prevAnim))) {
            return this.randomAttack();
        }
        return anim;
    }

    @Override
    public void handlePreAttack() {
        if (this.attacker.isBerserk()) {
            this.moveToWithDelay(1.2);
        }
        if (!this.moveFlag) {
            this.pathFindDelay = 0;
            this.moveDelay = 25 + this.attacker.getRandom().nextInt(10);
            if (!this.attacker.isBerserk() && this.distanceToTargetSq < 25) {
                this.moveDelay += 20;
                Vec3 away = DefaultRandomPos.getPosAway(this.attacker, 6, 4, this.target.position());
                if (away != null) {
                    this.attacker.getNavigation().moveTo(away.x, away.y, away.z, 1.1);
                }
            }
            this.moveFlag = true;
        } else if (this.moveDelay-- <= 0 || this.next.is(EntityRaccoon.JUMP) || (this.next.is(EntityRaccoon.LEAF_SHOOT, EntityRaccoon.LEAF_BOOMERANG) && this.distanceToTargetSq < 49)) {
            this.movementDone = true;
            this.moveFlag = false;
        } else if (this.attacker.isBerserk()) {
            AABB aabb = this.attacker.attackCheckAABB(this.next, this.target, -0.3);
            if (aabb.intersects(this.target.getBoundingBox())) {
                this.movementDone = true;
                this.attacker.getLookControl().setLookAt(this.target, 360, 90);
            }
        }
    }
}