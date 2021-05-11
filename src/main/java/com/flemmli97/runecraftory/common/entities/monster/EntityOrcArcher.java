package com.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.monster.ai.AnimatedRangedGoal;
import com.flemmli97.runecraftory.common.entities.misc.EntityMobArrow;
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

public class EntityOrcArcher extends EntityOrc {

    public AnimatedRangedGoal<EntityOrc> rangedGoal = new AnimatedRangedGoal<>(this, 8, (e) -> e.getHeldItemMainhand().getItem() instanceof BowItem);
    private static final AnimatedAction melee = new AnimatedAction(19, 13, "kick");
    private static final AnimatedAction ranged = new AnimatedAction(20, 12, "bow");

    private static final AnimatedAction[] anims = new AnimatedAction[]{melee, ranged};

    public EntityOrcArcher(EntityType<? extends EntityOrcArcher> type, World world) {
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
        if (type == AnimationType.MELEE)
            return anim.getID().equals(melee.getID());
        if (type == AnimationType.RANGED)
            return anim.getID().equals(ranged.getID());
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

    @Override
    public void handleRidingCommand(int command) {
        if (this.getAnimation() == null) {
            if (command == 2)
                this.setAnimation(melee);
            else
                this.setAnimation(ranged);
        }
    }

    private void shootArrow(LivingEntity target) {
        EntityMobArrow arrow = new EntityMobArrow(this.world, this, 0.8f);
        arrow.shootAtEntity(target, 1.3f, 14 - this.world.getDifficulty().getId() * 4, 0.2f);
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(arrow);
    }
}
