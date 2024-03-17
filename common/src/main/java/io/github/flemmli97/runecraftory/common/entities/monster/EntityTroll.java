package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedMonsterAttackGoal;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class EntityTroll extends BaseMonster {

    public static final AnimatedAction PUNCH = new AnimatedAction(16, 10, "punch");
    public static final AnimatedAction DOUBLE_PUNCH = new AnimatedAction(16, 10, "double_fist_punch");
    public static final AnimatedAction SLAM = new AnimatedAction(16, 10, "fist_slam");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(PUNCH, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{PUNCH, DOUBLE_PUNCH, SLAM, INTERACT, SLEEP};

    public final AnimatedMonsterAttackGoal<EntityTroll> attack = new AnimatedMonsterAttackGoal<>(this);
    private final AnimationHandler<EntityTroll> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityTroll(EntityType<? extends EntityTroll> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.23);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.is(PUNCH, DOUBLE_PUNCH, SLAM);
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.is(SLAM))
            return this.getBbWidth() + 1;
        return 1.5;
    }

    @Override
    public void mobAttack(AnimatedAction anim, LivingEntity target, Consumer<LivingEntity> cons) {
        super.mobAttack(anim, target, cons);
        if (anim.is(SLAM)) {
            Platform.INSTANCE.sendToTrackingAndSelf(new S2CScreenShake(10, 0.75f), this);
            this.level.playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE, this.getSoundSource(), 1.0f, 1.0f);
        }
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(SLAM))
            return this.attackAABB(anim).inflate(grow, 0, grow).move(this.getX(), this.getY(), this.getZ());
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(SLAM);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(DOUBLE_PUNCH);
            else
                this.getAnimationHandler().setAnimation(PUNCH);
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    @Override
    public AnimationHandler<EntityTroll> getAnimationHandler() {
        return this.animationHandler;
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
