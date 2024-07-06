package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.DelayedAttacker;
import io.github.flemmli97.runecraftory.common.entities.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MoveToTargetAttackRunner;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPollen;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.StrafingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.TimedWrappedRunner;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BiConsumer;

public class EntityAmbrosia extends BossMonster implements DelayedAttacker {

    public static final AnimatedAction KICK_1 = new AnimatedAction(12, 6, "kick_1");
    public static final AnimatedAction KICK_2 = new AnimatedAction(12, 6, "kick_2");
    public static final AnimatedAction KICK_3 = new AnimatedAction(16, 6, "kick_3");
    public static final AnimatedAction BUTTERFLY = new AnimatedAction(45, 5, "butterfly");
    public static final AnimatedAction WAVE = new AnimatedAction(45, 5, "wave");
    public static final AnimatedAction SLEEP = new AnimatedAction(15, 5, "sleep");
    public static final AnimatedAction POLLEN = new AnimatedAction(15, 5, "pollen");
    public static final AnimatedAction POLLEN_2 = AnimatedAction.copyOf(POLLEN, "pollen_2");
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(204, "defeat").marker(150).infinite().build();
    public static final AnimatedAction ANGRY = new AnimatedAction(48, 0, "angry");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(KICK_1, "interact");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{KICK_1, BUTTERFLY, WAVE, SLEEP, POLLEN, POLLEN_2, KICK_2, KICK_3, DEFEAT, ANGRY, INTERACT};

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityAmbrosia>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(BUTTERFLY, (anim, entity) -> {
            if (entity.targetPosition == null && entity.getTarget() != null) {
                LivingEntity target = entity.getTarget();
                entity.setAiVarHelper(new Vec3(target.getX(), target.getEyeY() - target.getBbHeight() * 0.5, target.getZ()));
            }
            if (anim.canAttack()) {
                ModSpells.BUTTERFLY.get().use(entity);
            }
        });
        BiConsumer<AnimatedAction, EntityAmbrosia> kick = (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            if (target != null) {
                entity.getNavigation().moveTo(target, 1.0);
            }
            if (anim.canAttack()) {
                entity.mobAttack(anim, target, entity::doHurtTarget);
            }
        };
        b.put(KICK_1, kick);
        b.put(KICK_2, kick);
        b.put(KICK_3, kick);
        b.put(SLEEP, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.SLEEP_BALLS.get().use(entity);
        });
        b.put(WAVE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.WAVE.get().use(entity);
        });
        BiConsumer<AnimatedAction, EntityAmbrosia> pollenHandler = (anim, entity) -> {
            if (entity.moveDirection == null) {
                Vec3 dir = entity.getTarget() != null ? entity.getTarget().position().subtract(entity.position()) : entity.getLookAngle();
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(5);
                int length = anim.getLength();
                entity.moveDirection = new Vec3(dir.x / length, 0, dir.z / length);
            }
            entity.setDeltaMovement(entity.moveDirection);
            if (anim.canAttack() && !EntityUtils.sealed(entity)) {
                entity.getNavigation().stop();
                EntityPollen pollen = new EntityPollen(entity.level, entity);
                pollen.setPos(pollen.getX(), pollen.getY() + 0.5, pollen.getZ());
                entity.level.addFreshEntity(pollen);
            }
        };
        b.put(POLLEN, pollenHandler);
        b.put(POLLEN_2, pollenHandler);
    });

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityAmbrosia>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.<EntityAmbrosia>nonRepeatableAttack(BUTTERFLY)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(4.5, 1.1, 6), e -> 40 + e.getRandom().nextInt(10))), 3),
            WeightedEntry.wrap(MonsterActionUtils.<EntityAmbrosia>nonRepeatableAttack(KICK_1)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 50 + e.getRandom().nextInt(10))), 2),
            WeightedEntry.wrap(MonsterActionUtils.<EntityAmbrosia>nonRepeatableAttack(SLEEP)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.2, 2), e -> 50 + e.getRandom().nextInt(10))), 3),
            WeightedEntry.wrap(MonsterActionUtils.<EntityAmbrosia>nonRepeatableAttack(WAVE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1, 1.5), e -> 20 + e.getRandom().nextInt(10))), 3),
            WeightedEntry.wrap(MonsterActionUtils.<EntityAmbrosia>nonRepeatableAttack(POLLEN)
                    .withCondition((goal, target, previous) -> goal.attacker.isEnraged() && !goal.attacker.isAnimEqual(previous, POLLEN))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1, 2), e -> 45 + e.getRandom().nextInt(10))), 3)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityAmbrosia>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new StrafingRunner<>(7, 1, 0.2f)), 1)
    );

    public final AnimatedAttackGoal<EntityAmbrosia> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityAmbrosia> animationHandler = new AnimationHandler<>(this, ANIMS).setAnimationChangeFunc(anim -> {
        if (!this.level.isClientSide && anim == null) {
            boolean chain = !this.commanded;
            this.moveDirection = null;
            this.commanded = false;
            if (chain) {
                AnimatedAction chainAnim = this.chainAnim(this.getAnimationHandler().getAnimation());
                if (chainAnim != null) {
                    this.getAnimationHandler().setAnimation(chainAnim);
                    return true;
                }
            }
        }
        return false;
    });

    private boolean commanded;
    private Vec3 moveDirection;

    public EntityAmbrosia(EntityType<? extends EntityAmbrosia> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.AMBROSIA_FIGHT.get());
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.29);
        super.applyAttributes();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrent(WAVE, ANGRY))) && super.hurt(source, amount);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(ANGRY, DEFEAT);
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(POLLEN, ANGRY, DEFEAT))
            return;
        super.push(x, y, z);
    }

    @Override
    public boolean shouldFreezeTravel() {
        return this.getAnimationHandler().isCurrent(WAVE);
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            if (!anim.is(POLLEN))
                this.lookAt(target, 180.0f, 50.0f);
        }
        BiConsumer<AnimatedAction, EntityAmbrosia> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(POLLEN)) {
            return this.getBoundingBox().inflate(2.0);
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.WAVE.get()))
                    this.getAnimationHandler().setAnimation(WAVE);
            } else if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.SLEEP_BALLS.get()))
                    this.getAnimationHandler().setAnimation(SLEEP);
            } else if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                this.getAnimationHandler().setAnimation(KICK_1);
            this.commanded = true;
        }
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    public void setAiVarHelper(Vec3 aiVarHelper) {
        this.targetPosition = aiVarHelper;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 29 / 16d, -5 / 16d);
    }

    @Override
    public AnimationHandler<EntityAmbrosia> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean isAnimEqual(String prev, AnimatedAction other) {
        if (other == null)
            return false;
        if (prev.equals(POLLEN_2.getID()))
            return POLLEN.is(other);
        if (prev.equals(KICK_3.getID()))
            return KICK_1.is(other);
        return prev.equals(other.getID());
    }

    public AnimatedAction chainAnim(AnimatedAction anim) {
        if (anim == null)
            return null;
        return switch (anim.getID()) {
            case "kick_1" -> KICK_2;
            case "kick_2" -> KICK_3;
            case "pollen" -> POLLEN_2;
            default -> null;
        };
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public Vec3 targetPosition(Vec3 from) {
        return this.targetPosition;
    }
}
