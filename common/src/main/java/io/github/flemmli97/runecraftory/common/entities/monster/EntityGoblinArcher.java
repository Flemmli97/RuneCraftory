package io.github.flemmli97.runecraftory.common.entities.monster;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedRangedGoal;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMobArrow;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityGoblinArcher extends EntityGoblin {

    private static final AnimatedAction bow = new AnimatedAction(15, 9, "bow");
    private static final AnimatedAction triple = AnimatedAction.copyOf(bow, "triple");
    private static final AnimatedAction kick = new AnimatedAction(11, 7, "kick");
    public static final AnimatedAction interact = AnimatedAction.copyOf(kick, "interact");
    private static final AnimatedAction[] anims = new AnimatedAction[]{bow, triple, kick, interact};
    private final AnimationHandler<EntityGoblinArcher> animationHandler = new AnimationHandler<>(this, anims);
    public AnimatedRangedGoal<EntityGoblin> rangedGoal = new AnimatedRangedGoal<>(this, 8, (e) -> e.getMainHandItem().getItem() instanceof BowItem);

    public EntityGoblinArcher(EntityType<? extends EntityGoblin> type, Level level) {
        super(type, level);
        this.goalSelector.removeGoal(this.attack);
        this.goalSelector.addGoal(2, this.rangedGoal);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.RANGED)
            return anim.getID().equals(bow.getID()) || anim.getID().equals(triple.getID());
        if (type == AnimationType.MELEE)
            return anim.getID().equals(kick.getID());
        return false;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(triple);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(bow);
            else
                this.getAnimationHandler().setAnimation(kick);
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        if (type == AnimationType.MELEE)
            return 0.6f;
        return 0.85f;
    }

    @Override
    public AnimationHandler<EntityGoblinArcher> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.isAnimOfType(anim, AnimationType.RANGED)) {
            if (anim.getTick() == 1)
                this.startUsingItem(InteractionHand.MAIN_HAND);
            this.getNavigation().stop();
            if (anim.canAttack()) {
                if (this.getTarget() != null && this.getSensing().hasLineOfSight(this.getTarget())) {
                    if (anim.getID().equals(bow.getID()))
                        this.shootArrow(this.getTarget());
                    else
                        this.shootTripleArrow(this.getTarget());
                }
                this.stopUsingItem();
            }
        } else
            super.handleAttack(anim);
    }

    private void shootArrow(LivingEntity target) {
        EntityMobArrow arrow = new EntityMobArrow(this.level, this, 0.8f);
        Vec3 dir = new Vec3(target.getX() - arrow.getX(), target.getY(0.33) - arrow.getY(), target.getZ() - arrow.getZ());
        double l = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        dir = dir.add(0, l * 0.2, 0);
        arrow.shoot(dir.x, dir.y, dir.z, 1.3f, 7 - this.level.getDifficulty().getId() * 2);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(arrow);
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

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(interact);
    }
}
