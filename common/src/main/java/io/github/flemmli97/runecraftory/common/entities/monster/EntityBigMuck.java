package io.github.flemmli97.runecraftory.common.entities.monster;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedMonsterAttackGoal;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
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
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{SLAP, SPORE, INTERACT, SLEEP};
    public final AnimatedMonsterAttackGoal<EntityBigMuck> ai = new AnimatedMonsterAttackGoal<>(this);
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
            return anim.is(SLAP, SPORE);
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.is(SPORE))
            return 1.7;
        return 0.9;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(SPORE)) {
            this.getNavigation().stop();
            if (this.attackPos == null) {
                Vec3 look = Vec3.directionFromRotation(0, this.yHeadRot).scale(1.3);
                this.attackPos = RayTraceUtils.rotatedVecs(look, new Vec3(0, 1, 0), -180, 135, 45);
            }
            if (anim.canAttack()) {
                ModSpells.SPORE_CIRCLE_SPELL.get().use(this);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 1 ? ModSpells.SPORE_CIRCLE_SPELL.get() : null))
                return;
            if (command == 1)
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
