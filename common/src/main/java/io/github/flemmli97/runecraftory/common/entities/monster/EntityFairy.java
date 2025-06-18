package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.NearestTargetHorizontal;
import io.github.flemmli97.runecraftory.common.entities.ai.control.FreeMoveControl;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.FloatingFlyNavigator;
import io.github.flemmli97.runecraftory.common.entities.utils.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class EntityFairy extends BaseMonster implements HealingPredicateEntity {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String LIGHT = BUILDER.add("light", AnimationsBuilder.definition(0.72).marker("attack", 0.32));
    public static final String WIND = BUILDER.add("wind", AnimationsBuilder.definition(0.72).marker("attack", 0.48));
    public static final String HEAL = BUILDER.add("heal", LIGHT);
    public static final String INTERACT = BUILDER.add("interact", LIGHT);
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();
    //
//    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityFairy>>> ATTACKS = List.of(
//            WeightedEntry.wrap(MonsterActionUtils.simpleRangedEvadingAction(WIND, 9, 2, 1, e -> 1), 8),
//            WeightedEntry.wrap(new GoalAttackAction<EntityFairy>(LIGHT)
//                    .cooldown(e -> e.animationCooldown(LIGHT))
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 3),
//            WeightedEntry.wrap(new GoalAttackAction<EntityFairy>(HEAL)
//                    .cooldown(e -> e.animationCooldown(HEAL))
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 2)
//    );
//    private static final List<WeightedEntry.Wrapper<IdleAction<EntityFairy>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 2),
//            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 1)
//    );
//
//    public final AnimatedAttackGoal<EntityFairy> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityFairy> animationHandler = new AnimationHandler<>(this, ANIMS);

    private final Predicate<LivingEntity> healingPredicate = e -> {
        if (this.getOwnerUUID() == null) {
            if (e instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null)
                return false;
            return e instanceof Enemy && e != this.getTarget();
        }
        if (e instanceof OwnableEntity ownable && this.getOwnerUUID().equals(ownable.getOwnerUUID()))
            return true;
        return this.getOwnerUUID().equals(e.getUUID());
    };

    public EntityFairy(EntityType<? extends EntityFairy> type, Level world) {
        super(type, world);
        this.setNoGravity(true);
        this.moveControl = new FreeMoveControl(this);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.3);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
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
    public void handleAttack(AnimationState anim) {
        if (anim.is(LIGHT)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                ModSpells.SHINE.get().use(this);
            }
        } else if (anim.is(WIND)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                ModSpells.DOUBLE_SONIC.get().use(this);
            }
        } else if (anim.is(HEAL)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                ModSpells.CURE_ALL.get().use(this);
            }
        }
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 1 ? ModSpells.SHINE.get() : ModSpells.DOUBLE_BULLET.get()))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(LIGHT);
            else
                this.getAnimationHandler().setAnimation(WIND);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_FAIRY_AMBIENT.get();
    }

    @Override
    public int animationCooldown(@Nullable String anim) {
        int diffAdd = this.difficultyCooldown();
        if (anim == null)
            return this.getRandom().nextInt(20) + 30 + diffAdd;
        return this.getRandom().nextInt(40) + 22 + diffAdd;
    }

    @Override
    public AnimationHandler<EntityFairy> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public Predicate<LivingEntity> healeableEntities() {
        return this.healingPredicate;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }
//
//    @Override
//    public Vec3 passengerOffset(Entity passenger) {
//        return new Vec3(0, 10 / 16d, -6 / 16d);
//    }
}