package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ai.EvadingRangedAttackGoal;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityGoblinGangster extends EntityGoblin {

    private static final AnimatedAction DOUBLE_STAB = new AnimatedAction(18, 8, "double_stab");
    private static final AnimatedAction DOUBLE_THROW = new AnimatedAction(20, 9, "double_throw");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(DOUBLE_THROW, "interact");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{DOUBLE_STAB, DOUBLE_THROW, INTERACT, SLEEP};

    private final AnimationHandler<EntityGoblinGangster> animationHandler = new AnimationHandler<>(this, ANIMS);
    public EvadingRangedAttackGoal<EntityGoblin> rangedGoal = new EvadingRangedAttackGoal<>(this, 1.5f, 8, (e) -> !e.getMainHandItem().isEmpty());

    public EntityGoblinGangster(EntityType<? extends EntityGoblin> type, Level level) {
        super(type, level);
        this.goalSelector.removeGoal(this.attack);
        this.goalSelector.addGoal(2, this.rangedGoal);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.cutlassProp.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.cutlassProp.get()));
        this.setDropChance(EquipmentSlot.OFFHAND, 0);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.RANGED)
            return anim.is(DOUBLE_THROW);
        if (type == AnimationType.MELEE)
            return anim.is(DOUBLE_STAB);
        return false;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 1)
                this.getAnimationHandler().setAnimation(DOUBLE_THROW);
            else
                this.getAnimationHandler().setAnimation(DOUBLE_STAB);
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        if (type == AnimationType.MELEE)
            return 0.7f;
        return 1;
    }

    @Override
    public AnimationHandler<EntityGoblinGangster> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(DOUBLE_THROW)) {
            this.getNavigation().stop();
            if (anim.getTick() == 1 && this.getTarget() != null)
                this.lookAt(this.getTarget(), 360, 90);
            if (anim.canAttack() || anim.getTick() == 16) {
                if (this.getTarget() != null && this.getSensing().hasLineOfSight(this.getTarget())) {
                    ModSpells.THROW_HAND_ITEM.get().use(this);
                }
                this.stopUsingItem();
            }
        } else if (anim.is(DOUBLE_STAB)) {
            this.getNavigation().stop();
            if (anim.getTick() == 1 && this.getTarget() != null)
                this.lookAt(this.getTarget(), 360, 90);
            if (anim.canAttack() || anim.getTick() == 14) {
                this.mobAttack(anim, this.getTarget(), this::quickAttack);
            }
        }
    }

    public boolean quickAttack(Entity target) {
        CustomDamage.Builder source = new CustomDamage.Builder(this).noKnockback().hurtResistant(1);
        double damagePhys = CombatUtils.getAttributeValue(this, Attributes.ATTACK_DAMAGE);
        return CombatUtils.mobAttack(this, target, source, damagePhys);
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }
}
