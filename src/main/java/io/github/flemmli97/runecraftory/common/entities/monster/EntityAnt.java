package io.github.flemmli97.runecraftory.common.entities.monster;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.AnimatedMeleeGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityAnt extends BaseMonster {

    public final AnimatedMeleeGoal<EntityAnt> attack = new AnimatedMeleeGoal<>(this);
    public static final AnimatedAction melee = new AnimatedAction(23, 12, "attack");

    private static final AnimatedAction[] anims = new AnimatedAction[]{melee};

    private final AnimationHandler<EntityAnt> animationHandler = new AnimationHandler<>(this, anims);

    public EntityAnt(EntityType<? extends EntityAnt> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1f;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 0.7;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation())
            this.getAnimationHandler().setAnimation(melee);
    }

    @Override
    public AnimationHandler<EntityAnt> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return type == AnimationType.MELEE && anim.getID().equals(melee.getID());
    }
}
