package com.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.entities.misc.EntitySpore;
import com.flemmli97.runecraftory.common.entities.monster.ai.AnimatedMeleeGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
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
    private List<Vector3f> attackPos;

    public EntityBigMuck(EntityType<? extends EntityBigMuck> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.ai);
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
                    spore.setPosition(spore.getX() + vec.getX(), spore.getY() + 0.4, spore.getZ() + vec.getZ());
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
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.getID().equals(sporeAttack.getID()))
            return 4;
        return 0.8;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(slapAttack.getID()) || anim.getID().equals(sporeAttack.getID());
        return false;
    }

    @Override
    public void setAnimation(AnimatedAction anim) {
        super.setAnimation(anim);
        this.attackPos = null;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (this.getAnimation() == null) {
            if (command == 2)
                this.setAnimation(sporeAttack);
            else
                this.setAnimation(slapAttack);
        }
    }
}
