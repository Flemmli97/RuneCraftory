package io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.DelayedAttacker;
import io.github.flemmli97.runecraftory.common.entities.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

public class EntityRafflesia extends BossMonster implements DelayedAttacker {

    private static final EntityDataAccessor<Optional<UUID>> HORSE_TAIL = SynchedEntityData.defineId(EntityRafflesia.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Optional<UUID>> FLOWER = SynchedEntityData.defineId(EntityRafflesia.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Optional<UUID>> PITCHER = SynchedEntityData.defineId(EntityRafflesia.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Direction> SPAWN_DIRECTION = SynchedEntityData.defineId(EntityRafflesia.class, EntityDataSerializers.DIRECTION);

    public static final AnimatedAction POISON_BREATH = new AnimatedAction(2.08, 0.64, "breath");
    public static final AnimatedAction POISON_BREATH_REV = AnimatedAction.copyOf(POISON_BREATH, "breath_2");
    public static final AnimatedAction PARA_BREATH = AnimatedAction.copyOf(POISON_BREATH, "paralysis_breath");
    public static final AnimatedAction PARA_BREATH_REV = AnimatedAction.copyOf(POISON_BREATH, "paralysis_breath_2");
    public static final AnimatedAction SLEEP_BREATH = AnimatedAction.copyOf(POISON_BREATH, "sleep_breath");
    public static final AnimatedAction SLEEP_BREATH_REV = AnimatedAction.copyOf(POISON_BREATH, "sleep_breath_2");
    public static final AnimatedAction WIND_BLADE_X8 = new AnimatedAction(0.88, 0.44, "casting");
    public static final AnimatedAction WIND_BLADE_X16 = AnimatedAction.copyOf(WIND_BLADE_X8, "wind_blade_x16");
    public static final AnimatedAction RESUMMON = AnimatedAction.copyOf(WIND_BLADE_X8, "resummon");
    public static final AnimatedAction STATUS_CIRCLE = AnimatedAction.copyOf(WIND_BLADE_X8, "status_circle");

    public static final AnimatedAction DEATH = AnimatedAction.builder(120, "death").infinite().build();
    public static final AnimatedAction ANGRY = AnimatedAction.copyOf(WIND_BLADE_X8, "roar");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(POISON_BREATH, "interact");

    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{POISON_BREATH, POISON_BREATH_REV, PARA_BREATH, PARA_BREATH_REV, SLEEP_BREATH, SLEEP_BREATH_REV,
            WIND_BLADE_X8, WIND_BLADE_X16, RESUMMON, STATUS_CIRCLE, DEATH, ANGRY, INTERACT};
    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityRafflesia>> ATTACK_HANDLER = createAnimationHandler(b -> {
        BiConsumer<AnimatedAction, EntityRafflesia> cons = (anim, entity) -> {
            if (anim.canAttack()) {
                entity.useAttack(anim);
            }
        };
        b.put(POISON_BREATH, cons);
        b.put(PARA_BREATH, cons);
        b.put(SLEEP_BREATH, cons);
        b.put(POISON_BREATH_REV, cons);
        b.put(PARA_BREATH_REV, cons);
        b.put(SLEEP_BREATH_REV, cons);
        b.put(WIND_BLADE_X8, cons);
        b.put(WIND_BLADE_X16, cons);
        b.put(RESUMMON, cons);
        b.put(STATUS_CIRCLE, cons);
    });

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityRafflesia>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityRafflesia>(POISON_BREATH)
                    .cooldown(e -> e.animationCooldown(POISON_BREATH))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityRafflesia>(POISON_BREATH_REV)
                    .cooldown(e -> e.animationCooldown(POISON_BREATH_REV))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityRafflesia>(PARA_BREATH)
                    .cooldown(e -> e.animationCooldown(PARA_BREATH))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityRafflesia>(PARA_BREATH_REV)
                    .cooldown(e -> e.animationCooldown(PARA_BREATH_REV))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityRafflesia>(SLEEP_BREATH)
                    .cooldown(e -> e.animationCooldown(SLEEP_BREATH))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityRafflesia>(SLEEP_BREATH_REV)
                    .cooldown(e -> e.animationCooldown(SLEEP_BREATH_REV))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityRafflesia>(WIND_BLADE_X8)
                    .cooldown(e -> e.animationCooldown(WIND_BLADE_X8))
                    .withCondition(((goal, target, previous) -> !goal.attacker.isEnraged()))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 4),
            WeightedEntry.wrap(new GoalAttackAction<EntityRafflesia>(WIND_BLADE_X16)
                    .cooldown(e -> e.animationCooldown(WIND_BLADE_X16))
                    .withCondition(((goal, target, previous) -> goal.attacker.isEnraged()))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 4),
            WeightedEntry.wrap(new GoalAttackAction<EntityRafflesia>(RESUMMON)
                    .cooldown(e -> e.animationCooldown(RESUMMON))
                    .withCondition(((goal, target, previous) -> goal.attacker.getHorseTail() == null || goal.attacker.getPitcher() == null || goal.attacker.getFlower() == null))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 2)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityRafflesia>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 1)
    );

    public final AnimatedAttackGoal<EntityRafflesia> attack2 = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private boolean mirrorAttack;

    private final AnimationHandler<EntityRafflesia> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeCons(anim -> {
                if (!this.level.isClientSide) {
                    this.mirrorAttack = isMirrorAttack(anim);
                }
            });

    private EntityRafflesiaPart horseTailEntity;
    private EntityRafflesiaPart flowerEntity;
    private EntityRafflesiaPart pitcherEntity;
    private int summonCooldown = 100;

    public EntityRafflesia(EntityType<? extends EntityRafflesia> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack2);
    }

    public static Vec3 rotateVec(Direction dir, Vec3 v) {
        return switch (dir) {
            case NORTH -> v.multiply(-1, 1, -1);
            case EAST -> new Vec3(v.z(), v.y(), -v.x());
            case WEST -> new Vec3(-v.z(), v.y(), -v.x());
            default -> v;
        };
    }

    public static boolean isMirrorAttack(AnimatedAction anim) {
        return anim != null && anim.is(POISON_BREATH_REV, PARA_BREATH_REV, SLEEP_BREATH_REV);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.RAFFLESIA_FIGHT.get());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigation(this, level) {
            @Nullable
            @Override
            protected Path createPath(Set<BlockPos> targets, int regionOffset, boolean offsetUpward, int accuracy, float followRange) {
                return null;
            }
        };
    }

    public void useAttack(AnimatedAction anim) {
        if (anim.is(RESUMMON))
            this.respawnParts();
        if (anim.is(WIND_BLADE_X8))
            ModSpells.WIND_CIRCLE_X8.get().use(this);
        if (anim.is(WIND_BLADE_X16))
            ModSpells.WIND_CIRCLE_X16.get().use(this);
        if (anim.is(POISON_BREATH, POISON_BREATH_REV))
            ModSpells.RAFFLESIA_POISON.get().use(this);
        if (anim.is(PARA_BREATH, PARA_BREATH_REV))
            ModSpells.RAFFLESIA_PARA.get().use(this);
        if (anim.is(SLEEP_BREATH, SLEEP_BREATH_REV))
            ModSpells.RAFFLESIA_SLEEP.get().use(this);
        if (anim.is(STATUS_CIRCLE))
            ModSpells.RAFFLESIA_CIRCLE.get().use(this);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
        super.applyAttributes();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HORSE_TAIL, Optional.empty());
        this.entityData.define(FLOWER, Optional.empty());
        this.entityData.define(PITCHER, Optional.empty());
        this.entityData.define(SPAWN_DIRECTION, Direction.NORTH);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.entityData.get(HORSE_TAIL).ifPresent(uuid -> compound.putUUID("HeadBones", uuid));
        this.entityData.get(FLOWER).ifPresent(uuid -> compound.putUUID("Flower", uuid));
        this.entityData.get(PITCHER).ifPresent(uuid -> compound.putUUID("Pitcher", uuid));
        compound.putInt("SpawnDirection", this.entityData.get(SPAWN_DIRECTION).ordinal());
        compound.putInt("SummonCooldown", this.summonCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("HorseTail"))
            this.entityData.set(HORSE_TAIL, Optional.of(compound.getUUID("HorseTail")));
        if (compound.hasUUID("Flower"))
            this.entityData.set(FLOWER, Optional.of(compound.getUUID("Flower")));
        if (compound.hasUUID("Pitcher"))
            this.entityData.set(PITCHER, Optional.of(compound.getUUID("Pitcher")));
        try {
            this.entityData.set(SPAWN_DIRECTION, Direction.values()[compound.getInt("SpawnDirection")]);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        this.summonCooldown = compound.getInt("SummonCooldown");
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.entityData.set(SPAWN_DIRECTION, this.getDirection());
        this.respawnParts();
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level.isClientSide) {
            LivingEntity target = this.getTarget();
            if (target != null && !this.getAnimationHandler().hasAnimation()) {
                this.getLookControl().setLookAt(target, 30.0f, 30.0f);
            }
            if (this.summonCooldown < 200 && this.getHorseTail() == null || this.getPitcher() == null || this.getFlower() == null) {
                this.summonCooldown = this.random.nextInt(200) + 300;
            }
            if (this.tickCount % 30 == 0) {
                this.level.getEntities(EntityTypeTest.forClass(LivingEntity.class),
                                this.getBoundingBox().inflate(0.3).move(0, this.getBbHeight(), 0),
                                e -> e != this && this.targetPred.test(e))
                        .forEach(e -> {
                            Vec3 dir = e.position().subtract(this.position());
                            boolean none = dir.x() == 0 && dir.z() == 0;
                            dir = new Vec3(none ? 1 : dir.x(), 0, dir.z()).normalize().scale(1.2);
                            e.setDeltaMovement(e.getDeltaMovement().add(dir));
                            e.hurtMarked = true;
                        });
            }
        }
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            if (anim.getTick() == 1) {
                this.yHeadRot = (float) (Mth.atan2(target.getZ() - this.getZ(), target.getX() - this.getX()) * (180 / Math.PI)) - 90;
                this.yBodyRot = this.yHeadRot;
                this.targetPosition = target.position();
            }
        } else
            this.targetPosition = null;
        BiConsumer<AnimatedAction, EntityRafflesia> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    public Direction getSpawnDirection() {
        return this.entityData.get(SPAWN_DIRECTION);
    }

    private void respawnParts() {
        Direction dir = this.entityData.get(SPAWN_DIRECTION);
        if (this.getHorseTail() == null) {
            EntityRafflesiaHorseTail horseTail = new EntityRafflesiaHorseTail(this.level, this);
            horseTail.setSpawnDirection(dir);
            horseTail.setPos(this.position().add(rotateVec(dir, horseTail.offset())));
            this.level.addFreshEntity(horseTail);
            this.entityData.set(HORSE_TAIL, Optional.of(horseTail.getUUID()));
        }
        if (this.getFlower() == null) {
            EntityRafflesiaFlower flower = new EntityRafflesiaFlower(this.level, this);
            flower.setSpawnDirection(dir);
            flower.setPos(this.position().add(rotateVec(dir, flower.offset())));
            this.level.addFreshEntity(flower);
            this.entityData.set(FLOWER, Optional.of(flower.getUUID()));
        }
        if (this.getPitcher() == null) {
            EntityRafflesiaPitcher pitcher = new EntityRafflesiaPitcher(this.level, this);
            pitcher.setSpawnDirection(dir);
            pitcher.setPos(this.position().add(rotateVec(dir, pitcher.offset())));
            this.level.addFreshEntity(pitcher);
            this.entityData.set(PITCHER, Optional.of(pitcher.getUUID()));
        }
        this.summonCooldown = this.random.nextInt(200) + 300;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !(this.getAnimationHandler().isCurrent(ANGRY)) && super.hurt(source, amount);
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return DEATH;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 1 ? ModSpells.RAFFLESIA_POISON.get() : ModSpells.RAFFLESIA_PARA.get()))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(POISON_BREATH);
            else
                this.getAnimationHandler().setAnimation(PARA_BREATH);
        }
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public AnimationHandler<EntityRafflesia> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return DEATH;
    }

    public EntityRafflesiaPart getHorseTail() {
        UUID uuid = this.entityData.get(HORSE_TAIL).orElse(null);
        if (uuid != null) {
            if (this.horseTailEntity == null) {
                this.horseTailEntity = EntityUtil.findFromUUID(EntityRafflesiaPart.class, this.level, uuid);
            } else if (this.horseTailEntity.isRemoved()) {
                this.horseTailEntity = null;
                this.entityData.set(HORSE_TAIL, Optional.empty());
            }
        } else
            this.horseTailEntity = null;
        return this.horseTailEntity;
    }

    public EntityRafflesiaPart getFlower() {
        UUID uuid = this.entityData.get(FLOWER).orElse(null);
        if (uuid != null) {
            if (this.flowerEntity == null) {
                this.flowerEntity = EntityUtil.findFromUUID(EntityRafflesiaPart.class, this.level, uuid);
            } else if (this.flowerEntity.isRemoved()) {
                this.flowerEntity = null;
                this.entityData.set(FLOWER, Optional.empty());
            }
        } else
            this.flowerEntity = null;
        return this.flowerEntity;
    }

    public EntityRafflesiaPart getPitcher() {
        UUID uuid = this.entityData.get(PITCHER).orElse(null);
        if (uuid != null) {
            if (this.pitcherEntity == null) {
                this.pitcherEntity = EntityUtil.findFromUUID(EntityRafflesiaPart.class, this.level, uuid);
            } else if (this.pitcherEntity.isRemoved()) {
                this.pitcherEntity = null;
                this.entityData.set(PITCHER, Optional.empty());
            }
        } else
            this.pitcherEntity = null;
        return this.pitcherEntity;
    }

    @Override
    public Vec3 targetPosition(Vec3 from) {
        return this.targetPosition;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ENTITY_RAFFLESIA_DEATH.get();
    }

    @Override
    public void playAngrySound() {
        this.playSound(ModSounds.ENTITY_RAFFLESIA_ANGRY.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
    }

    public boolean mirrorAttack() {
        return this.mirrorAttack;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 43 / 16d, 3.5 / 16d);
    }
}
