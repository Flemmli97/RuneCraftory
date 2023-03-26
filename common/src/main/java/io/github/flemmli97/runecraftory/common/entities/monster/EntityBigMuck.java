package io.github.flemmli97.runecraftory.common.entities.monster;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedMeleeGoal;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpore;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityBigMuck extends BaseMonster {

    public static final AnimatedAction SLAP = new AnimatedAction(24, 7, "slap");
    public static final AnimatedAction SPORE = new AnimatedAction(44, 18, "spore");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(SLAP, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(2, "sleep").infinite().changeDelay(AnimationHandler.DEFAULT_ADJUST_TIME).build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{SLAP, SPORE, INTERACT, SLEEP};
    public final AnimatedMeleeGoal<EntityBigMuck> ai = new AnimatedMeleeGoal<>(this);
    private final AnimationHandler<EntityBigMuck> animationHandler = new AnimationHandler<>(this, ANIMS);

    private List<Vector3f> attackPos;

    public EntityBigMuck(EntityType<? extends EntityBigMuck> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.ai);
        this.getOrCreateAnimationHandler().setAnimationChangeCons(a -> this.attackPos = null);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.8f;
    }

    protected AnimationHandler<EntityBigMuck> getOrCreateAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public AnimationHandler<EntityBigMuck> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(SLAP.getID()) || anim.getID().equals(SPORE.getID());
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.getID().equals(SPORE.getID()))
            return 1.7;
        return 0.8;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(SPORE.getID())) {
            this.getNavigation().stop();
            if (this.attackPos == null) {
                Vec3 look = Vec3.directionFromRotation(0, this.yHeadRot).scale(1.3);
                this.attackPos = RayTraceUtils.rotatedVecs(look, new Vec3(0, 1, 0), -180, 135, 45);
            }
            if (anim.getTick() > anim.getAttackTime()) {
                if (EntityUtils.sealed(this))
                    return;
                int i = (anim.getTick() - anim.getAttackTime()) / 3;
                if (i < this.attackPos.size()) {
                    Vector3f vec = this.attackPos.get(i);
                    EntitySpore spore = new EntitySpore(this.level, this);
                    spore.setPos(spore.getX() + vec.x(), spore.getY() + 0.4, spore.getZ() + vec.z());
                    this.level.addFreshEntity(spore);
                }
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(SPORE);
            else
                this.getAnimationHandler().setAnimation(SLAP);
        }
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
