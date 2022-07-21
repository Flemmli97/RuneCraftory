package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStone;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.ChargeAttackGoal;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EntityGoblin extends ChargingMonster {

    private static final AnimatedAction melee = new AnimatedAction(12, 7, "slash");
    private static final AnimatedAction leap = new AnimatedAction(19, 6, "leap");
    private static final AnimatedAction stone = new AnimatedAction(14, 9, "throw");
    private static final AnimatedAction[] anims = new AnimatedAction[]{melee, leap, stone};
    public ChargeAttackGoal<EntityGoblin> attack = new ChargeAttackGoal<>(this);
    protected List<LivingEntity> hitEntity;
    private final AnimationHandler<EntityGoblin> animationHandler = new AnimationHandler<>(this, anims)
            .setAnimationChangeCons(a -> {
                if (!leap.checkID(a))
                    this.hitEntity = null;
            });

    public EntityGoblin(EntityType<? extends EntityGoblin> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.steelSword.get()));
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(melee.getID()) || anim.getID().equals(stone.getID());
        if (type == AnimationType.CHARGE)
            return anim.getID().equals(leap.getID());
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.getID().equals(stone.getID()))
            return 8;
        return 1;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(stone);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(leap);
            else
                this.getAnimationHandler().setAnimation(melee);
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.85f;
    }

    @Override
    public AnimationHandler<? extends EntityGoblin> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(stone.getID())) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                if (this.getTarget() != null) {
                    this.shootStone(this.getTarget());
                }
            }
        } else if (anim.getID().equals(leap.getID())) {
            if (anim.canAttack()) {
                Vec3 target = this.getTarget() != null ? this.getTarget().position() : this.getLookAngle();
                Vec3 vec32 = new Vec3(target.x - this.getX(), 0.0, target.z - this.getZ())
                        .normalize().scale(1.35);
                this.setDeltaMovement(vec32.x, 0.35f, vec32.z);
            }
            if (anim.getTick() >= anim.getAttackTime()) {
                if (this.hitEntity == null)
                    this.hitEntity = new ArrayList<>();
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
            }
        } else
            super.handleAttack(anim);
    }

    private void shootStone(LivingEntity target) {
        EntityStone stone = new EntityStone(this.level, this);
        Vec3 dir = new Vec3(target.getX() - stone.getX(), target.getY(0.33) - stone.getY(), target.getZ() - stone.getZ());
        double l = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        dir = dir.add(0, l * 0.2, 0);
        stone.shoot(dir.x, dir.y, dir.z, 1.3f, 7 - this.level.getDifficulty().getId() * 2);
        this.playSound(SoundEvents.FISHING_BOBBER_THROW, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(stone);
    }
}
