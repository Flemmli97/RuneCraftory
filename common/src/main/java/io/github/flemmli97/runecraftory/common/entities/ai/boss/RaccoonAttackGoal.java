package io.github.flemmli97.runecraftory.common.entities.ai.boss;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedMeleeGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityRaccoon;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class RaccoonAttackGoal<T extends EntityRaccoon> extends AnimatedMeleeGoal<T> {

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
        return this.attacker.getRandomAnimation(AnimationType.GENERICATTACK);
    }

    @Override
    public void handlePreAttack() {
        if (this.attacker.isBerserk())
            this.moveToWithDelay(1.2);
        if (!this.moveFlag) {
            this.pathFindDelay = 0;
            this.moveDelay = 25 + this.attacker.getRandom().nextInt(10);
            if (!this.attacker.isBerserk()) {
                this.moveDelay += 20;
                Vec3 away = DefaultRandomPos.getPosAway(this.attacker, 6, 4, this.target.position());
                if (away != null) {
                    this.attacker.getNavigation().moveTo(away.x, away.y, away.z, 1.1);
                }
            }
            this.moveFlag = true;
        } else if (this.moveDelay-- <= 0 || (this.attacker.isBerserk() && this.distanceToTargetSq < 4)) {
            this.movementDone = true;
            this.moveFlag = false;
        }
    }
}