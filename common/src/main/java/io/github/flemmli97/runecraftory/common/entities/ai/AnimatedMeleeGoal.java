package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.RandomAttackSelectorMob;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.ai.AnimatedAttackGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;

public class AnimatedMeleeGoal<T extends PathfinderMob & IAnimated & RandomAttackSelectorMob> extends AnimatedAttackGoal<T> {

    protected int idleMoveDelay, idleMoveFlag, attackMoveDelay;

    public AnimatedMeleeGoal(T entity) {
        super(entity);
    }

    @Override
    public boolean canUse() {
        return !this.attacker.canBeControlledByRider() && super.canUse();
    }

    @Override
    public void stop() {
        super.stop();
        this.attacker.setTarget(null);
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.getRandom().nextFloat() < this.attacker.attackChance(AnimationType.GENERICATTACK))
            return this.attacker.getRandomAnimation(AnimationType.MELEE);
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }

    @Override
    public void handlePreAttack() {
        if (this.attacker.maxAttackRange(this.next) <= 1)
            this.moveToEntityNearer(this.target, 1);
        else
            this.moveToWithDelay(1);
        if (this.attackMoveDelay <= 0)
            this.attackMoveDelay = this.attacker.getRandom().nextInt(50) + 100;
        AABB aabb = this.attacker.attackCheckAABB(this.next, this.target, -0.3);
        //PacketHandler.sendToAll(new S2CAttackDebug(aabb, EnumAABBType.ATTEMPT));
        if (aabb.intersects(this.target.getBoundingBox())) {
            this.movementDone = true;
            this.attacker.getLookControl().setLookAt(this.target, 360, 90);
        } else if (this.attackMoveDelay-- == 1) {
            this.attackMoveDelay = 0;
            this.next = null;
        }
    }

    @Override
    public void handleAttack(AnimatedAction animatedAction) {

    }

    @Override
    public void handleIdle() {
        if (this.idleMoveDelay <= 0) {
            this.idleMoveFlag = this.attacker.getRandom().nextInt(3);
            this.idleMoveDelay = this.attacker.getRandom().nextInt(10) + 7 - this.idleMoveFlag * 10;
        }
        switch (this.idleMoveFlag) {
            case 0 -> this.moveToWithDelay(1);
            case 1 -> this.moveRandomlyAround(36);
        }
    }

    @Override
    public int coolDown(AnimatedAction animatedAction) {
        return this.attacker.animationCooldown(this.next);
    }

    @Override
    public void tick() {
        super.tick();
        this.idleMoveDelay--;
    }

    protected void moveToEntityNearer(LivingEntity target, float speed) {
        if (this.pathFindDelay <= 0) {
            Path path = this.attacker.getNavigation().createPath(target, 0);
            if (path == null || this.attacker.getNavigation().moveTo(path, speed)) {
                this.pathFindDelay += 15;
            }
            this.pathFindDelay += this.attacker.getRandom().nextInt(10) + 5;
        }
    }
}
