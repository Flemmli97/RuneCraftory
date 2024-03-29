package io.github.flemmli97.runecraftory.common.entities.monster;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.AnimatedMeleeGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityOrc extends BaseMonster {

    public AnimatedMeleeGoal<EntityOrc> attack = new AnimatedMeleeGoal<>(this);
    private static final AnimatedAction melee1 = new AnimatedAction(22, 14, "attack_1");
    private static final AnimatedAction melee2 = new AnimatedAction(23, 13, "attack_2");

    private static final AnimatedAction[] anims = new AnimatedAction[]{melee1, melee2};

    private final AnimationHandler<EntityOrc> animationHandler = new AnimationHandler<>(this, anims);

    public EntityOrc(EntityType<? extends EntityOrc> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
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
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.getID().equals(melee2.getID()))
            return 1.2;
        return 1.1;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(melee1.getID()) || anim.getID().equals(melee2.getID());
        return false;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (this.rand.nextInt(2) == 0)
                this.getAnimationHandler().setAnimation(melee1);
            else
                this.getAnimationHandler().setAnimation(melee2);
        }
    }

    @Override
    public double getMountedYOffset() {
        return this.getHeight() * 0.85D;
    }
}
