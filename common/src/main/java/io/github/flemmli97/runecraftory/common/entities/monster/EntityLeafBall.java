package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.NearestTargetHorizontal;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.EvadingRangedRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntityLeafBall extends BaseMonster {

    public static final AnimatedAction MELEE = new AnimatedAction(15, 6, "tackle");
    public static final AnimatedAction WIND = new AnimatedAction(15, 10, "wind_blade");
    public static final AnimatedAction SLEEP_ATTACK = new AnimatedAction(15, 10, "sleep_aura");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction STILL = AnimatedAction.builder(1, "still").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, WIND, SLEEP_ATTACK, INTERACT, STILL};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityLeafBall>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeActionInRange(MELEE, e -> 0.7f), 1),
            WeightedEntry.wrap(MonsterActionUtils.simpleRangedEvadingAction(WIND, 10, 4, 1, e -> 1), 3),
            WeightedEntry.wrap(MonsterActionUtils.simpleRangedEvadingAction(SLEEP_ATTACK, 7, 1, 1, e -> 1), 2)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityLeafBall>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new EvadingRangedRunner<>(10, 4, 1)), 2),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 3)
    );

    public final AnimatedAttackGoal<EntityLeafBall> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityLeafBall> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityLeafBall(EntityType<? extends EntityLeafBall> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
        this.setNoGravity(true);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.35f);
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
    public double maxAttackRange(AnimatedAction anim) {
        return 1.15;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(WIND)) {
            this.getNavigation().stop();
            if (anim.getTick() == 1 && this.getTarget() != null)
                this.lookAt(this.getTarget(), 360, 90);
            if (anim.canAttack()) {
                ModSpells.DOUBLE_SONIC.get().use(this);
            }
        } else if (anim.is(SLEEP_ATTACK)) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                ModSpells.SLEEP_AURA.get().use(this);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 0 ? ModSpells.DOUBLE_BULLET.get() : null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(MELEE);
            else
                this.getAnimationHandler().setAnimation(WIND);
        }
    }

    @Override
    public int animationCooldown(@Nullable AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown();
        if (anim == null)
            return this.getRandom().nextInt(20) + 30 + diffAdd;
        return this.getRandom().nextInt(40) + 25 + diffAdd;
    }

    @Override
    public AnimationHandler<EntityLeafBall> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return STILL;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 16 / 16d, -6 / 16d);
    }
}
