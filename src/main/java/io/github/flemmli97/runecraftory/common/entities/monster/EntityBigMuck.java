package io.github.flemmli97.runecraftory.common.entities.monster;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpore;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.AnimatedMeleeGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import java.util.List;

public class EntityBigMuck extends BaseMonster {

    public final AnimatedMeleeGoal<EntityBigMuck> ai = new AnimatedMeleeGoal<>(this);
    public static final AnimatedAction slapAttack = new AnimatedAction(24, 7, "slap");
    public static final AnimatedAction sporeAttack = new AnimatedAction(44, 18, "spore");

    private static final AnimatedAction[] anims = new AnimatedAction[]{slapAttack, sporeAttack};

    private final AnimationHandler<EntityBigMuck> animationHandler = new AnimationHandler<>(this, anims);

    private List<Vector3f> attackPos;

    public EntityBigMuck(EntityType<? extends EntityBigMuck> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.ai);
        this.getAnimationHandler().setAnimationChangeCons(a -> this.attackPos = null);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(sporeAttack.getID())) {
            this.getNavigator().clearPath();
            if (this.attackPos == null) {
                Vector3d look = Vector3d.fromPitchYaw(0, this.rotationYawHead).scale(1.1);
                this.attackPos = RayTraceUtils.rotatedVecs(look, new Vector3d(0, 1, 0), -180, 135, 45);
            }
            if (anim.getTick() > anim.getAttackTime()) {
                int i = (anim.getTick() - anim.getAttackTime()) / 3;
                if (i < this.attackPos.size()) {
                    Vector3f vec = this.attackPos.get(i);
                    EntitySpore spore = new EntitySpore(this.world, this);
                    spore.setPosition(spore.getPosX() + vec.getX(), spore.getPosY() + 0.4, spore.getPosZ() + vec.getZ());
                    this.world.addEntity(spore);
                }
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.9f;
    }

    @Override
    public AnimationHandler<EntityBigMuck> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.getID().equals(sporeAttack.getID()))
            return 1.7;
        return 0.8;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(slapAttack.getID()) || anim.getID().equals(sporeAttack.getID());
        return false;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(sporeAttack);
            else
                this.getAnimationHandler().setAnimation(slapAttack);
        }
    }
}
