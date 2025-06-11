package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.StrafingRunner;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntityFlowerLily extends BaseMonster {

    public static final AnimatedAction LEAP = AnimatedAction.builder(0.88, "leap").marker("leap", 0.28).build();
    public static final AnimatedAction ATTACK = AnimatedAction.builder(0.56, "attack").marker("attack", 0.32).build();
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(ATTACK, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(0, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{LEAP, ATTACK, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityFlowerLily>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleRangedEvadingAction(ATTACK, 9, 2, 1, e -> 1), 2),
            WeightedEntry.wrap(MonsterActionUtils.<EntityFlowerLily>simpleMeleeAction(LEAP, e -> 1)
                    .withCondition(((goal, target, previous) -> goal.distanceToTargetSq < 4)), 5)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityFlowerLily>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 2),
            WeightedEntry.wrap(new IdleAction<>(() -> new StrafingRunner<>(16, 1)), 1)
    );

    public final AnimatedAttackGoal<EntityFlowerLily> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityFlowerLily> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityFlowerLily(EntityType<? extends EntityFlowerLily> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(LEAP)) {
            return new OrientedBoundingBox(this.attackBB(anim), this.getYRot(), 0, this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        double attackSize = this.getBbWidth() * 2.1;
        return new AABB(-attackSize, -0.2, -attackSize, attackSize, this.getBbHeight() + 0.2, attackSize);
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(LEAP)) {
            if (this.getDeltaMovement().lengthSqr() > 0.01)
                return this.getDeltaMovement();
            return null;
        }
        return super.directionToLookAt();
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(LEAP)) {
            this.getNavigation().stop();
            if (anim.isAt("leap")) {
                Vec3 vec32 = EntityUtils.getTargetDirection(this, EntityAnchorArgument.Anchor.FEET, true)
                        .scale(-1.8);
                this.setDeltaMovement(vec32.x, 0.15, vec32.z);
            }
        } else if (anim.is(ATTACK)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                this.rangedAttackSpell().use(this);
            }
        }
    }

    protected Spell rangedAttackSpell() {
        return ModSpells.DOUBLE_BULLET.get();
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 0 ? this.rangedAttackSpell() : null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(LEAP);
            else
                this.getAnimationHandler().setAnimation(ATTACK);
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(ModSounds.ENTITY_FLOWER_LILY_STEP.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
    }

    @Override
    public int animationCooldown(@Nullable AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown();
        if (anim == null)
            return this.getRandom().nextInt(20) + 30 + diffAdd;
        return this.getRandom().nextInt(40) + 25 + diffAdd;
    }

    @Override
    public AnimationHandler<EntityFlowerLily> getAnimationHandler() {
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
        return new Vec3(0, 14 / 16d, -4 / 16d);
    }
}
