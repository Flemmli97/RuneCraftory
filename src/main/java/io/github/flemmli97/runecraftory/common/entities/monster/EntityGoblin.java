package io.github.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.ChargeAttackGoal;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityGoblin extends ChargingMonster {

    public ChargeAttackGoal<EntityGoblin> attack = new ChargeAttackGoal<>(this);
    private static final AnimatedAction melee = new AnimatedAction(12, 7, "slash");
    private static final AnimatedAction leap = new AnimatedAction(19, 6, "leap");
    private static final AnimatedAction stone = new AnimatedAction(14, 9, "throw");

    private static final AnimatedAction[] anims = new AnimatedAction[]{melee, leap, stone};

    public EntityGoblin(EntityType<? extends EntityGoblin> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.steelSword.get()));
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.85f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.getID().equals(stone.getID()))
            return 8;
        return 1;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(stone.getID())) {

        } else if (anim.getID().equals(leap.getID())) {

        } else
            super.handleAttack(anim);
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
    public void handleRidingCommand(int command) {
        if (this.getAnimation() == null) {
            if (command == 2)
                this.setAnimation(stone);
            else if (command == 1)
                this.setAnimation(leap);
            else
                this.setAnimation(melee);
        }
    }
}
