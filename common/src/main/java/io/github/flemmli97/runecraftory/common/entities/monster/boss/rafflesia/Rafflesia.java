package io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.tenshilib.common.entity.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.TargetPosition;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
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
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

public class Rafflesia extends BossMonster {

    private static final EntityDataAccessor<Optional<UUID>> HORSE_TAIL = SynchedEntityData.defineId(Rafflesia.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Optional<UUID>> FLOWER = SynchedEntityData.defineId(Rafflesia.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Optional<UUID>> PITCHER = SynchedEntityData.defineId(Rafflesia.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Direction> SPAWN_DIRECTION = SynchedEntityData.defineId(Rafflesia.class, EntityDataSerializers.DIRECTION);

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String POISON_BREATH = BUILDER.add("breath", AnimationsBuilder.definition(1.96).marker("attack", 0.56));
    public static final String POISON_BREATH_REV = BUILDER.add("breath_2", POISON_BREATH);
    public static final String PARA_BREATH = BUILDER.add("paralysis_breath", POISON_BREATH);
    public static final String PARA_BREATH_REV = BUILDER.add("paralysis_breath_2", POISON_BREATH);
    public static final String SLEEP_BREATH = BUILDER.add("sleep_breath", POISON_BREATH);
    public static final String SLEEP_BREATH_REV = BUILDER.add("sleep_breath_2", POISON_BREATH);
    public static final String INTERACT = BUILDER.add("interact", POISON_BREATH);
    public static final String WIND_BLADE_X8 = BUILDER.add("casting", AnimationsBuilder.definition(1.24).marker("attack", 0.72));
    public static final String WIND_BLADE_X16 = BUILDER.add("wind_blade_x16", WIND_BLADE_X8);
    public static final String RESUMMON = BUILDER.add("resummon", WIND_BLADE_X8);
    public static final String STATUS_CIRCLE = BUILDER.add("status_circle", WIND_BLADE_X8);
    public static final String SPAWN = BUILDER.add("spawn", AnimationsBuilder.definition(2).marker("sound", 0.64));
    public static final String ANGRY = BUILDER.add("angry", AnimationsBuilder.definition(2).marker("sound", 0.64));
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(10).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimationState, Rafflesia>> ATTACK_HANDLER = createAnimationHandler(b -> {
        BiConsumer<AnimationState, Rafflesia> cons = (anim, entity) -> {
            if (anim.isAt("attack")) {
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
        BiConsumer<AnimationState, Rafflesia> trigger = (anim, entity) -> {
            if (anim.isAt("sound")) {
                entity.playRandomizedSound(RuneCraftorySounds.ENTITY_RAFFLESIA_ANGRY.get());
            }
        };
        b.put(SPAWN, trigger);
        b.put(ANGRY, trigger);
    });

    private boolean mirrorAttack;

    private final AnimationHandler<Rafflesia> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (!this.level().isClientSide) {
                    this.mirrorAttack = isMirrorAttack(anim);
                }
                return false;
            });

    private RafflesiaPart horseTailEntity;
    private RafflesiaPart flowerEntity;
    private RafflesiaPart pitcherEntity;
    private int summonCooldown = 100;

    public Rafflesia(EntityType<? extends Rafflesia> type, Level level) {
        super(type, level);
    }

    public static boolean isMirrorAttack(AnimationDefinition anim) {
        return anim != null && anim.is(POISON_BREATH_REV, PARA_BREATH_REV, SLEEP_BREATH_REV);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(RuneCraftoryEntities.RAFFLESIA.getID(), this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(RuneCraftorySounds.RAFFLESIA_FIGHT.get());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HORSE_TAIL, Optional.empty());
        builder.define(FLOWER, Optional.empty());
        builder.define(PITCHER, Optional.empty());
        builder.define(SPAWN_DIRECTION, Direction.NORTH);
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

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<Rafflesia>create()
                .start(MonsterBehaviourUtils.checkedAttack(POISON_BREATH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(6)
                .start(MonsterBehaviourUtils.checkedAttack(POISON_BREATH_REV)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(6)
                .start(MonsterBehaviourUtils.checkedAttack(PARA_BREATH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(6)
                .start(MonsterBehaviourUtils.checkedAttack(PARA_BREATH_REV)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(6)
                .start(MonsterBehaviourUtils.checkedAttack(SLEEP_BREATH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(6)
                .start(MonsterBehaviourUtils.checkedAttack(SLEEP_BREATH_REV)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(6)
                .start(MonsterBehaviourUtils.checkedAttack(WIND_BLADE_X8)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> !m.isEnraged())
                .end(6)
                .start(MonsterBehaviourUtils.checkedAttack(WIND_BLADE_X16)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(BossMonster::isEnraged)
                .end(6)
                .start(MonsterBehaviourUtils.checkedAttack(RESUMMON)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(Rafflesia::noPartsLeft)
                .end(7)
                .build();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide) {
            if (this.summonCooldown < 200 && this.getHorseTail() == null || this.getPitcher() == null || this.getFlower() == null) {
                this.summonCooldown = this.random.nextInt(200) + 300;
            }
            if (this.tickCount % 30 == 0) {
                this.level().getEntities(EntityTypeTest.forClass(LivingEntity.class),
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
    protected Vec3 directionToLookAt() {
        LivingEntity target = this.getTarget();
        if (target != null && !this.getAnimationHandler().hasAnimation()) {
            return target.getEyePosition().subtract(this.getEyePosition());
        }
        return super.directionToLookAt();
    }

    @Override
    protected float[] targetLookClamp() {
        return new float[]{30, 30};
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
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        this.entityData.set(SPAWN_DIRECTION, this.getDirection());
        this.respawnParts();
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    private boolean noPartsLeft() {
        return this.getHorseTail() == null || this.getPitcher() == null || this.getFlower() == null;
    }

    @Override
    public void setupAttack(AnimationDefinition anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            this.setTargetPosition(target);
        } else {
            this.setTargetPosition(TargetPosition.of(this.position().add(this.getLookAngle().scale(5))));
        }
    }

    public void useAttack(AnimationState anim) {
        if (anim.is(RESUMMON))
            this.respawnParts();
        if (anim.is(WIND_BLADE_X8))
            RuneCraftorySpells.WIND_CIRCLE_X8.get().use(this);
        if (anim.is(WIND_BLADE_X16))
            RuneCraftorySpells.WIND_CIRCLE_X16.get().use(this);
        if (anim.is(POISON_BREATH, POISON_BREATH_REV))
            RuneCraftorySpells.RAFFLESIA_POISON.get().use(this);
        if (anim.is(PARA_BREATH, PARA_BREATH_REV))
            RuneCraftorySpells.RAFFLESIA_PARA.get().use(this);
        if (anim.is(SLEEP_BREATH, SLEEP_BREATH_REV))
            RuneCraftorySpells.RAFFLESIA_SLEEP.get().use(this);
        if (anim.is(STATUS_CIRCLE))
            RuneCraftorySpells.RAFFLESIA_CIRCLE.get().use(this);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        BiConsumer<AnimationState, Rafflesia> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public int animationCooldown(String anim) {
        return super.animationCooldown(anim) + 5;
    }

    @Override
    public AnimationHandler<Rafflesia> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 1 ? RuneCraftorySpells.RAFFLESIA_POISON.get() : RuneCraftorySpells.RAFFLESIA_PARA.get()))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(POISON_BREATH);
            else
                this.getAnimationHandler().setAnimation(PARA_BREATH);
        }
    }

    private void respawnParts() {
        Direction dir = this.entityData.get(SPAWN_DIRECTION);
        if (this.getHorseTail() == null) {
            RafflesiaHorseTail horseTail = new RafflesiaHorseTail(this.level(), this);
            horseTail.setSpawnDirection(dir);
            horseTail.setPos(this.position().add(rotateVec(dir, horseTail.offset())));
            this.level().addFreshEntity(horseTail);
            this.entityData.set(HORSE_TAIL, Optional.of(horseTail.getUUID()));
        }
        if (this.getFlower() == null) {
            RafflesiaFlower flower = new RafflesiaFlower(this.level(), this);
            flower.setSpawnDirection(dir);
            flower.setPos(this.position().add(rotateVec(dir, flower.offset())));
            this.level().addFreshEntity(flower);
            this.entityData.set(FLOWER, Optional.of(flower.getUUID()));
        }
        if (this.getPitcher() == null) {
            RafflesiaPitcher pitcher = new RafflesiaPitcher(this.level(), this);
            pitcher.setSpawnDirection(dir);
            pitcher.setPos(this.position().add(rotateVec(dir, pitcher.offset())));
            this.level().addFreshEntity(pitcher);
            this.entityData.set(PITCHER, Optional.of(pitcher.getUUID()));
        }
        this.summonCooldown = this.random.nextInt(200) + 300;
    }

    public RafflesiaPart getHorseTail() {
        UUID uuid = this.entityData.get(HORSE_TAIL).orElse(null);
        if (uuid != null) {
            if (this.horseTailEntity == null) {
                this.horseTailEntity = EntityUtils.findFromUUID(RafflesiaPart.class, this.level(), uuid);
            } else if (this.horseTailEntity.isRemoved()) {
                this.horseTailEntity = null;
                this.entityData.set(HORSE_TAIL, Optional.empty());
            }
        } else
            this.horseTailEntity = null;
        return this.horseTailEntity;
    }

    public static Vec3 rotateVec(Direction dir, Vec3 v) {
        return switch (dir) {
            case NORTH -> v.multiply(-1, 1, -1);
            case EAST -> new Vec3(v.z(), v.y(), -v.x());
            case WEST -> new Vec3(-v.z(), v.y(), -v.x());
            default -> v;
        };
    }

    public RafflesiaPart getFlower() {
        UUID uuid = this.entityData.get(FLOWER).orElse(null);
        if (uuid != null) {
            if (this.flowerEntity == null) {
                this.flowerEntity = EntityUtils.findFromUUID(RafflesiaPart.class, this.level(), uuid);
            } else if (this.flowerEntity.isRemoved()) {
                this.flowerEntity = null;
                this.entityData.set(FLOWER, Optional.empty());
            }
        } else
            this.flowerEntity = null;
        return this.flowerEntity;
    }

    public RafflesiaPart getPitcher() {
        UUID uuid = this.entityData.get(PITCHER).orElse(null);
        if (uuid != null) {
            if (this.pitcherEntity == null) {
                this.pitcherEntity = EntityUtils.findFromUUID(RafflesiaPart.class, this.level(), uuid);
            } else if (this.pitcherEntity.isRemoved()) {
                this.pitcherEntity = null;
                this.entityData.set(PITCHER, Optional.empty());
            }
        } else
            this.pitcherEntity = null;
        return this.pitcherEntity;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public Direction getSpawnDirection() {
        return this.entityData.get(SPAWN_DIRECTION);
    }

    public boolean mirrorAttack() {
        return this.mirrorAttack;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return RuneCraftorySounds.ENTITY_RAFFLESIA_DEATH.get();
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getSleepAnimation() {
        return DEFEAT;
    }

    @Override
    public String getSpawnAnimation() {
        return SPAWN;
    }

    @Override
    public String getAngryAnimation() {
        return ANGRY;
    }

    @Override
    public String getDeathAnimation() {
        return DEFEAT;
    }
}
