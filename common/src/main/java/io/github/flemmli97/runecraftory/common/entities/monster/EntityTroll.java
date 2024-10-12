package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;

public class EntityTroll extends BaseMonster {

    public static final AnimatedAction PUNCH = new AnimatedAction(16, 10, "punch");
    public static final AnimatedAction DOUBLE_PUNCH = new AnimatedAction(16, 10, "double_fist_punch");
    public static final AnimatedAction SLAM = new AnimatedAction(16, 10, "fist_slam");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(PUNCH, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{PUNCH, DOUBLE_PUNCH, SLAM, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityTroll>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(PUNCH, e -> 1), 1),
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(DOUBLE_PUNCH, e -> 1), 1),
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(SLAM, e -> 1), 1)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityTroll>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1)), 3),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(12, 4)), 1)
    );

    public final AnimatedAttackGoal<EntityTroll> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
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
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.is(SLAM))
            return this.getBbWidth() + 4;
        return 1.6;
    }

    @Override
    public void mobAttack(AnimatedAction anim, LivingEntity target, Consumer<LivingEntity> cons) {
        super.mobAttack(anim, target, cons);
        if (anim.is(SLAM)) {
            Platform.INSTANCE.sendToTrackingAndSelf(new S2CScreenShake(10, 2), this);
            this.level.playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE, this.getSoundSource(), 1.0f, 1.0f);
        }
    }

    @Override
    public CustomDamage.Builder damageSourceAttack() {
        CustomDamage.Builder source = super.damageSourceAttack();
        if (this.getAnimationHandler().isCurrent(SLAM, DOUBLE_PUNCH))
            source.withChangedAttribute(ModAttributes.STUN.get(), 30);
        return source;
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

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 37 / 16d, -6 / 16d);
    }
}
