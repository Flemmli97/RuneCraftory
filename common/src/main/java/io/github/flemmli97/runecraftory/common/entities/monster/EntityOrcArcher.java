package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedRangedGoal;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMobArrow;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class EntityOrcArcher extends EntityOrc {

    private static final AnimatedAction MELEE = new AnimatedAction(19, 13, "kick");
    private static final AnimatedAction RANGED = new AnimatedAction(20, 12, "bow");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, RANGED, INTERACT, SLEEP};
    private final AnimationHandler<EntityOrcArcher> animationHandler = new AnimationHandler<>(this, ANIMS);
    public AnimatedRangedGoal<EntityOrc> rangedGoal = new AnimatedRangedGoal<>(this, 8, (e) -> e.getMainHandItem().getItem() instanceof BowItem);

    public EntityOrcArcher(EntityType<? extends EntityOrcArcher> type, Level level) {
        super(type, level);
        this.goalSelector.removeGoal(this.attack);
        this.goalSelector.addGoal(2, this.rangedGoal);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.isAnimOfType(anim, AnimationType.RANGED)) {
            if (anim.getTick() == 1)
                this.startUsingItem(InteractionHand.MAIN_HAND);
            this.getNavigation().stop();
            if (anim.canAttack()) {
                if (this.getTarget() != null && this.getSensing().hasLineOfSight(this.getTarget())) {
                    this.shootArrow(this.getTarget());
                } else if (this.getFirstPassenger() instanceof Player)
                    this.shootArrowFromRotation(this);
                this.stopUsingItem();
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public float attackChance(AnimationType type) {
        if (type == AnimationType.MELEE)
            return 0.6f;
        return this.getType() == ModEntities.ORC_ARCHER.get() ? 0.85f : 0.95f;
    }

    @Override
    public AnimationHandler<EntityOrcArcher> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(MELEE.getID());
        if (type == AnimationType.RANGED)
            return anim.getID().equals(RANGED.getID());
        return false;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(MELEE);
            else
                this.getAnimationHandler().setAnimation(RANGED);
        }
    }

    private void shootArrow(LivingEntity target) {
        EntityMobArrow arrow = new EntityMobArrow(this.level, this, 0.8f);
        arrow.shootAtEntity(target, 1.3f, 14 - this.level.getDifficulty().getId() * 4, 0.2f);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(arrow);
    }

    private void shootArrowFromRotation(LivingEntity shooter) {
        EntityMobArrow arrow = new EntityMobArrow(this.level, this, 0.8f);
        arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 1.3f, 7 - this.level.getDifficulty().getId() * 2);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(arrow);
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }
}
