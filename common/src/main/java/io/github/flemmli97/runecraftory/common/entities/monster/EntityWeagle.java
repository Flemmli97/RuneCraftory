package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.AirWanderGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.NearestTargetHorizontal;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.StrafingRunner;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EntityWeagle extends BaseMonster {

    public static final AnimatedAction GALE = new AnimatedAction(19, 5, "gale");
    public static final AnimatedAction PECK = new AnimatedAction(11, 4, "peck");
    public static final AnimatedAction SWOOP = new AnimatedAction(14, 4, "swoop");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(PECK, "interact");
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(2, "defeat").infinite().build();
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{GALE, PECK, SWOOP, INTERACT, DEFEAT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityWeagle>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeActionInRange(PECK, e -> 1), 1),
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeActionInRange(SWOOP, e -> 1), 1),
            WeightedEntry.wrap(MonsterActionUtils.simpleRangedEvadingAction(GALE, 8, 4, 1, e -> 1), 2)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityWeagle>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 2),
            WeightedEntry.wrap(new IdleAction<>(() -> new StrafingRunner<>(16, 5)), 1)
    );

    public final AnimatedAttackGoal<EntityWeagle> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);

    protected List<LivingEntity> hitEntity;
    private final AnimationHandler<EntityWeagle> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeCons(anim -> this.hitEntity = null);

    public EntityWeagle(EntityType<? extends BaseMonster> type, Level world) {
        super(type, world);
        this.goalSelector.removeGoal(this.wander);
        this.goalSelector.addGoal(6, this.wander = new AirWanderGoal(this));
        this.goalSelector.addGoal(2, this.attack);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
    }

    @Override
    protected NearestAttackableTargetGoal<Player> createTargetGoalPlayer() {
        return new NearestTargetHorizontal<>(this, Player.class, 5, true, true, player -> !this.isTamed());
    }

    @Override
    protected NearestAttackableTargetGoal<Mob> createTargetGoalMobs() {
        return new NearestTargetHorizontal<>(this, Mob.class, 5, true, true, this.targetPred);
    }

    @Override
    public void travel(Vec3 vec) {
        if (this.isEffectiveAi() && this.isVehicle() && this.canBeControlledByRider() && this.getControllingPassenger() instanceof LivingEntity) {
            this.handleNoGravTravel(vec);
        } else {
            super.travel(vec);
        }
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.5;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(GALE)) {
            if (anim.canAttack()) {
                ModSpells.GUST_SPELL.get().use(this);
            }
        } else if (anim.is(SWOOP)) {
            if (this.hitEntity == null)
                this.hitEntity = new ArrayList<>();
            if (anim.getTick() == 1 && this.getTarget() != null) {
                this.targetPosition = this.getTarget().position();
            }
            Vec3 target = this.targetPosition != null || this.getTarget() == null ? this.targetPosition : this.getTarget().position();
            Vec3 dir;
            if (target != null) {
                dir = target.subtract(this.position()).normalize();
            } else {
                dir = this.getLookAngle();
            }
            dir = new Vec3(dir.x(), -1.5, dir.z()).normalize().scale(0.5);
            if (anim.getTick() > anim.getAttackTime() && anim.getLength() - anim.getTick() > 3) {
                this.setDeltaMovement(dir.scale(0.55));
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
            } else {
                this.setDeltaMovement(dir.multiply(-0.3, -0.225, -0.3));
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public int animationCooldown(@Nullable AnimatedAction anim) {
        if (anim != null && anim.is(GALE)) {
            int diffAdd = this.difficultyCooldown();
            return this.getRandom().nextInt(40) + 20 + diffAdd;
        }
        return super.animationCooldown(anim);
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(SWOOP))
            return super.calculateAttackAABB(anim, target, grow).move(this.getDeltaMovement());
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? ModSpells.GUST_SPELL.get() : null))
                return;
            switch (command) {
                case 2 -> this.getAnimationHandler().setAnimation(GALE);
                case 1 -> this.getAnimationHandler().setAnimation(SWOOP);
                default -> this.getAnimationHandler().setAnimation(PECK);
            }
        }
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    protected void onFlap() {
        this.playSound(ModSounds.ENTITY_WEAGLE_FLAP.get(), this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
    }

    @Override
    public AnimationHandler<EntityWeagle> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected void checkFallDamage(double dist, boolean groundLogic, BlockState state, BlockPos pos) {
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return SLEEP;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 16 / 16d, -1 / 16d);
    }
}
