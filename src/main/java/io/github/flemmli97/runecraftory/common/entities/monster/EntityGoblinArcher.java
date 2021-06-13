package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMobArrow;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.AnimatedRangedGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityGoblinArcher extends EntityGoblin {

    public AnimatedRangedGoal<EntityGoblinArcher> rangedGoal = new AnimatedRangedGoal<>(this, 8, (e) -> e.getHeldItemMainhand().getItem() instanceof BowItem);
    private static final AnimatedAction bow = new AnimatedAction(15, 9, "bow");
    private static final AnimatedAction triple = new AnimatedAction(15, 9, "triple", "bow");
    private static final AnimatedAction kick = new AnimatedAction(11, 7, "kick");

    private static final AnimatedAction[] anims = new AnimatedAction[]{bow, triple, kick};

    public EntityGoblinArcher(EntityType<? extends EntityGoblin> type, World world) {
        super(type, world);
        this.goalSelector.removeGoal(this.attack);
        this.goalSelector.addGoal(2, this.rangedGoal);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
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
    public float attackChance(AnimationType type) {
        if (type == AnimationType.MELEE)
            return 0.6f;
        return 0.85f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.isAnimOfType(anim, AnimationType.RANGED)) {
            if (anim.getTick() == 1)
                this.setActiveHand(Hand.MAIN_HAND);
            this.getNavigator().clearPath();
            if (anim.canAttack()) {
                if (this.getAttackTarget() != null && this.getEntitySenses().canSee(this.getAttackTarget())) {
                    if (anim.getID().equals(bow.getID()))
                        this.shootArrow(this.getAttackTarget());
                    else
                        this.shootTripleArrow(this.getAttackTarget());
                }
                this.resetActiveHand();
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (this.getAnimation() == null) {
            if (command == 2)
                this.setAnimation(triple);
            else if (command == 1)
                this.setAnimation(bow);
            else
                this.setAnimation(kick);
        }
    }

    private void shootArrow(LivingEntity target) {
        EntityMobArrow arrow = new EntityMobArrow(this.world, this, 0.8f);
        Vector3d dir = new Vector3d(target.getPosX() - arrow.getPosX(), target.getPosYHeight(0.33) - arrow.getPosY(), target.getPosZ() - arrow.getPosZ());
        double l = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        dir = dir.add(0, l * 0.2, 0);
        arrow.shoot(dir.x, dir.y, dir.z, 1.3f, 7 - this.world.getDifficulty().getId() * 2);
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(arrow);
    }

    private void shootTripleArrow(LivingEntity target) {
        EntityMobArrow arrow = new EntityMobArrow(this.world, this, 0.8f);
        Vector3d dir = new Vector3d(target.getPosX() - arrow.getPosX(), target.getPosYHeight(0.33) - arrow.getPosY(), target.getPosZ() - arrow.getPosZ());
        double l = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        dir = dir.add(0, l * 0.2, 0);
        arrow.shoot(dir.x, dir.y, dir.z, 1.3f, 7 - this.world.getDifficulty().getId() * 2);
        this.world.addEntity(arrow);
        Vector3d up = this.getUpVector(1);

        for (float y = -15; y <= 15; y += 30) {
            Quaternion quaternion = new Quaternion(new Vector3f(up), y, true);
            Vector3f newDir = new Vector3f(dir);
            newDir.transform(quaternion);
            EntityMobArrow arrowO = new EntityMobArrow(this.world, this, 0.8f);
            arrowO.shoot(newDir.getX(), newDir.getY(), newDir.getZ(), 1.3f, 7 - this.world.getDifficulty().getId() * 2);
            this.world.addEntity(arrowO);
        }

        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
    }
}
