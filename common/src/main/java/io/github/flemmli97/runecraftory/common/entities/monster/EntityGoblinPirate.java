package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ai.LeapingAttackGoal;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
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

public class EntityGoblinPirate extends EntityGoblin {

    private static final AnimatedAction DOUBLE_SLASH = new AnimatedAction(20, 9, "double_slash");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(DOUBLE_SLASH, "interact");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{DOUBLE_SLASH, LEAP, INTERACT, SLEEP};

    private final AnimationHandler<EntityGoblinPirate> animationHandler = new AnimationHandler<>(this, ANIMS);
    public LeapingAttackGoal<EntityGoblin> ai = new LeapingAttackGoal<>(this, true, 2);

    public EntityGoblinPirate(EntityType<? extends EntityGoblin> type, Level level) {
        super(type, level);
        this.goalSelector.removeGoal(this.attack);
        this.goalSelector.addGoal(2, this.ai);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.THIEF_KNIFE_PROP.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.LEAP)
            return anim.is(LEAP);
        if (type == AnimationType.MELEE)
            return anim.is(DOUBLE_SLASH);
        return false;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(LEAP);
            else
                this.getAnimationHandler().setAnimation(DOUBLE_SLASH);
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        if (type == AnimationType.MELEE)
            return 0.8f;
        return 1;
    }

    @Override
    public AnimationHandler<EntityGoblinPirate> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(DOUBLE_SLASH)) {
            this.getNavigation().stop();
            if (anim.getTick() == 1 && this.getTarget() != null)
                this.lookAt(this.getTarget(), 360, 90);
            if (anim.canAttack() || anim.getTick() == 16) {
                this.mobAttack(anim, this.getTarget(), this::quickAttack);
            }
        } else
            super.handleAttack(anim);
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
