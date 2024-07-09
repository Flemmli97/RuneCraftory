package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.MobAttackExt;
import io.github.flemmli97.runecraftory.common.entities.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.pathing.StaticNavigator;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BiConsumer;

public class EntitySano extends BossMonster implements MobAttackExt {

    public static final AnimatedAction FIREBALL_3X = new AnimatedAction(40, 15, "fireball_3x");
    public static final AnimatedAction FIREBALL_BARRAGE = new AnimatedAction(90, 15, "fireball_barrage");
    public static final AnimatedAction EXPLOSION = new AnimatedAction(30, 15, "explosion");
    public static final AnimatedAction FIRE_BREATH = new AnimatedAction(40, 15, "fire_breath");
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(150, "defeat").marker(150).infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{FIREBALL_3X, FIREBALL_BARRAGE, EXPLOSION, FIRE_BREATH, DEFEAT};

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntitySano>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(FIREBALL_3X, (anim, entity) -> {
            if (anim.isAtTick(15) || anim.isAtTick(20) || anim.isAtTick(25))
                ModSpells.FIREBALL.get().use(entity);
        });
        b.put(FIREBALL_BARRAGE, (anim, entity) -> {
            if (anim.isAtTick(15))
                ModSpells.TRIPLE_FIRE_BALL.get().use(entity);
            if (anim.isAtTick(20))
                ModSpells.QUAD_FIRE_BALL.get().use(entity);
            if (anim.isAtTick(60))
                ModSpells.FIREBALL.get().use(entity);
            if (anim.isAtTick(65))
                ModSpells.QUAD_FIRE_BALL.get().use(entity);
            if (anim.isAtTick(70))
                ModSpells.DOUBLE_FIRE_BALL.get().use(entity);
        });
        b.put(EXPLOSION, (anim, entity) -> {
            if (anim.canAttack())
                ModSpells.EXPLOSION.get().use(entity);
        });
        b.put(FIRE_BREATH, (anim, entity) -> {
            if (anim.isAtTick(15) || anim.isAtTick(25)) {
                Vec3 dir = entity.getTarget() != null ? EntityUtils.getStraightProjectileTarget(entity.position(), entity.getTarget()).subtract(entity.position()).normalize() : entity.getLookAngle();
                dir = dir.scale(10).add(entity.random.nextDouble() * 3, 0, entity.random.nextDouble() * 3);
                entity.targetPos = entity.position().add(dir);
                ModSpells.FIRE_WALL.get().use(entity);
            }
        });
    });

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntitySano>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.<EntitySano>nonRepeatableAttack(FIREBALL_3X)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 3),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySano>nonRepeatableAttack(FIREBALL_BARRAGE)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 3),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySano>nonRepeatableAttack(EXPLOSION)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 3),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySano>nonRepeatableAttack(FIRE_BREATH)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 3)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntitySano>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<EntitySano>(DoNothingRunner::new)
                    .duration(e -> e.getRandom().nextInt(20) + 35), 1)
    );

    public final AnimatedAttackGoal<EntitySano> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntitySano> animationHandler = new AnimationHandler<>(this, ANIMS);
    private Vec3 targetPos;

    public EntitySano(EntityType<? extends EntitySano> type, Level world) {
        super(type, world);
        this.lookControl = new NonLookControl(this);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.SANO_UNO_FIGHT.get());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new StaticNavigator(this, level);
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void travel(Vec3 vec) {
        this.xxa = 0;
        this.yya = 0;
        this.zza = 0;
        this.setDeltaMovement(0, this.getDeltaMovement().y - 0.08, 0);
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        BiConsumer<AnimatedAction, EntitySano> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.WAVE.get()))
                    this.getAnimationHandler().setAnimation(FIRE_BREATH);
            } else if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.SLEEP_BALLS.get()))
                    this.getAnimationHandler().setAnimation(FIREBALL_BARRAGE);
            } else if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                this.getAnimationHandler().setAnimation(FIREBALL_3X);
        }
    }

    @Override
    public void playInteractionAnimation() {
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 30.5 / 16d, 6 / 16d).scale(2);
    }

    @Override
    public AnimationHandler<EntitySano> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public Vec3 targetPosition(Vec3 from) {
        return this.targetPos;
    }

    static class NonLookControl extends LookControl {

        public NonLookControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
        }
    }
}
