package io.github.flemmli97.runecraftory.common.entities.monster;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMobArrow;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityOrcHunter extends EntityOrcArcher {

    public EntityOrcHunter(EntityType<? extends EntityOrcHunter> type, Level level) {
        super(type, level);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.isAnimOfType(anim, AnimationType.RANGED)) {
            if (anim.getTick() == 1)
                this.startUsingItem(InteractionHand.MAIN_HAND);
            this.getNavigation().stop();
            if (anim.canAttack()) {
                if (this.getTarget() != null && this.getSensing().hasLineOfSight(this.getTarget())) {
                    this.shootTripleArrow(this.getTarget());
                } else if (this.getFirstPassenger() instanceof Player)
                    this.shootTripleArrowFromRotation(this);
                this.stopUsingItem();
            }
        } else
            super.handleAttack(anim);
    }

    private void shootTripleArrow(LivingEntity target) {
        EntityMobArrow arrow = new EntityMobArrow(this.level, this, 0.8f);
        Vec3 dir = new Vec3(target.getX() - arrow.getX(), target.getY(0.33) - arrow.getY(), target.getZ() - arrow.getZ());
        double l = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        dir = dir.add(0, l * 0.2, 0);
        arrow.shoot(dir.x, dir.y, dir.z, 1.3f, 7 - this.level.getDifficulty().getId() * 2);
        this.level.addFreshEntity(arrow);
        Vec3 up = this.getUpVector(1);

        for (float y = -15; y <= 15; y += 30) {
            Quaternion quaternion = new Quaternion(new Vector3f(up), y, true);
            Vector3f newDir = new Vector3f(dir);
            newDir.transform(quaternion);
            EntityMobArrow arrowO = new EntityMobArrow(this.level, this, 0.8f);
            arrowO.shoot(newDir.x(), newDir.y(), newDir.z(), 1.3f, 7 - this.level.getDifficulty().getId() * 2);
            this.level.addFreshEntity(arrowO);
        }

        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    private void shootTripleArrowFromRotation(LivingEntity shooter) {
        for (int i = 0; i < 3; i++) {
            EntityMobArrow arrow = new EntityMobArrow(this.level, this, 0.8f);
            arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + (i - 1) * 15, 0.0F, 1.3f, 7 - this.level.getDifficulty().getId() * 2);
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(arrow);
        }
    }
}
