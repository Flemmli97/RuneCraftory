package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedMeleeGoal;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityOrc extends BaseMonster {

    private static final AnimatedAction MELEE_1 = new AnimatedAction(22, 14, "attack_1");
    private static final AnimatedAction MELEE_2 = new AnimatedAction(23, 13, "attack_2");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE_1, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(2, "sleep").infinite().changeDelay(AnimationHandler.DEFAULT_ADJUST_TIME).build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE_1, MELEE_2, INTERACT, SLEEP};
    private final AnimationHandler<EntityOrc> animationHandler = new AnimationHandler<>(this, ANIMS);
    public AnimatedMeleeGoal<EntityOrc> attack = new AnimatedMeleeGoal<>(this);

    public EntityOrc(EntityType<? extends EntityOrc> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.orcMaze.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.85f;
    }

    @Override
    public AnimationHandler<? extends EntityOrc> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(MELEE_1.getID()) || anim.getID().equals(MELEE_2.getID());
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.getID().equals(MELEE_2.getID()))
            return 1.2;
        return 1.1;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (this.random.nextInt(2) == 0)
                this.getAnimationHandler().setAnimation(MELEE_1);
            else
                this.getAnimationHandler().setAnimation(MELEE_2);
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.85D;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return SLEEP;
    }
}
