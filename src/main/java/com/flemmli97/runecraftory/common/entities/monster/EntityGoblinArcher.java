package com.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.monster.ai.AnimatedRangedGoal;
import com.flemmli97.runecraftory.common.entities.projectiles.EntityMobArrow;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityGoblinArcher extends EntityOrc {

    public AnimatedRangedGoal<EntityGoblinArcher> rangedGoal = new AnimatedRangedGoal<>(this, 8, (e) -> e.getHeldItemMainhand().getItem() instanceof BowItem);
    private static final AnimatedAction bow = new AnimatedAction(19, 13, "bow");
    private static final AnimatedAction triple = new AnimatedAction(20, 12, "triple");

    private static final AnimatedAction[] anims = new AnimatedAction[]{bow, triple};

    public EntityGoblinArcher(EntityType<? extends EntityOrcArcher> type, World world) {
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
                    this.shootArrow(this.getAttackTarget());
                }
                this.resetActiveHand();
            }
        } else
            super.handleAttack(anim);
    }

    private void shootArrow(LivingEntity target) {
        EntityMobArrow arrow = new EntityMobArrow(this.world, this, 0.8f);
        arrow.shootAtPosition(target.getX(), target.getBodyY(0.33), target.getZ(), 1.3f, (14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(arrow);
    }
}
