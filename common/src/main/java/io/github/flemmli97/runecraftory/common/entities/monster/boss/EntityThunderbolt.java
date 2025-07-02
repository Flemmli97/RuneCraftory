package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableDatas;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.network.S2CMobUpdate;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class EntityThunderbolt extends BossMonster {

    private static final float RANGE_THRESHOLD = 0.7f;
    private static final float FEINT_THRESHOLD = 0.35f;

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String BACK_KICK = BUILDER.add("back_kick", AnimationsBuilder.definition(0.64).marker("attack", 0.32));
    public static final String BACK_KICK_HORN = BUILDER.add("back_kick_horn", BACK_KICK);
    public static final String LASER_X5 = BUILDER.add("laser_x5", AnimationsBuilder.definition(1.44).marker("attack", 1.2));
    public static final String LASER_AOE = BUILDER.add("laser_aoe", LASER_X5);
    public static final String STOMP = BUILDER.add("stomp", AnimationsBuilder.definition(0.44).marker("attack", 0.28));
    public static final String INTERACT = BUILDER.add("interact", STOMP);
    public static final String HORN_ATTACK = BUILDER.add("horn_attack", AnimationsBuilder.definition(0.44).marker("attack", 0.24));
    public static final String CHARGE = BUILDER.add("charge", AnimationsBuilder.definition(1.64)
            .marker("attack_start", 0.44).marker("attack_end", 1.12));
    public static final String CHARGE_2 = BUILDER.add("charge_2", CHARGE);
    public static final String CHARGE_3 = BUILDER.add("charge_3", CHARGE);
    public static final String LASER_KICK = BUILDER.add("laser_kick", AnimationsBuilder.definition(1.2).marker("attack", 0.32));
    public static final String LASER_KICK_2 = BUILDER.add("laser_kick_2", LASER_KICK);
    public static final String LASER_KICK_3 = BUILDER.add("laser_kick_3", LASER_KICK);
    public static final String WIND_BLADE = BUILDER.add("wind_blade", AnimationsBuilder.definition(0.72).marker("attack", 0.36));
    public static final String FEINT = BUILDER.add("feint", AnimationsBuilder.definition(2).marker("neigh", 0.96));
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(10).infinite());
    public static final String NEIGH = BUILDER.add("neigh", AnimationsBuilder.definition(1.16).marker("neigh", 0.48));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimationState, EntityThunderbolt>> ATTACK_HANDLER = createAnimationHandler(b -> {
        BiConsumer<AnimationState, EntityThunderbolt> kick = (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            if (anim.isAt("attack")) {
                entity.mobAttack(anim, target, e -> {
                    if (entity.doHurtTarget(e)) {
                        e.setDeltaMovement(0, 0.05, 0);
                        e.hasImpulse = true;
                        e.knockback(0.8f, entity.getX() - e.getX(), entity.getZ() - e.getZ());
                    }
                });
            }
        };
        b.put(BACK_KICK, kick);
        b.put(BACK_KICK_HORN, kick);
        b.put(HORN_ATTACK, (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            if (anim.isAt("attack")) {
                AtomicBoolean bool = new AtomicBoolean(false);
                entity.mobAttack(anim, target, e -> {
                    if (entity.doHurtTarget(e)) {
                        if (!bool.get())
                            bool.set(true);
                        e.setDeltaMovement(0, 0.65, 0);
                        e.hasImpulse = true;
                    }
                });
                if (bool.get() && !entity.isVehicle()) {
                    entity.hornAttackSuccess = true;
                }
            }
        });
        b.put(STOMP, (anim, entity) -> {
            if (anim.isAt("attack")) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        });
        b.put(WIND_BLADE, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.DOUBLE_SONIC.get().use(entity);
            }
        });
        b.put(LASER_X5, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.LASER5.get().use(entity);
            }
        });
        b.put(LASER_AOE, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.LASER_AOE.get().use(entity);
            }
        });
        BiConsumer<AnimationState, EntityThunderbolt> bigLaser = (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.BIG_LIGHTNING.get().use(entity);
            }
        };
        b.put(LASER_KICK, bigLaser);
        b.put(LASER_KICK_2, bigLaser);
        b.put(LASER_KICK_3, bigLaser);
        BiConsumer<AnimationState, EntityThunderbolt> charge = (anim, entity) -> {
            if (entity.chargeMotion == null) {
                entity.setChargeDirection(EntityUtils.getTargetDirection(entity, EntityAnchorArgument.Anchor.FEET, true)
                        .scale(2));
            }
            if (anim.isAt("attack_start")) {
                entity.setDeltaMovement(entity.chargeMotion.x(), 0.2, entity.chargeMotion.z());
            }
            if (anim.isPast("attack_start") && !anim.isPast("attack_end") && !entity.chargeAttackSuccess) {
                entity.mobAttack(anim, null, e -> {
                    if (entity.doHurtTarget(e)) {
                        entity.chargeAttackSuccess = true;
                        entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
                    }
                });
            }
        };
        b.put(CHARGE, charge);
        b.put(CHARGE_2, charge);
        b.put(CHARGE_3, charge);
        b.put(NEIGH, (anim, entity) -> {
            if (anim.isAt("neigh"))
                entity.playSound(ModSounds.ENTITY_THUNDERBOLT_NEIGH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        });
        b.put(FEINT, (anim, entity) -> {
            if (anim.isAt("neigh"))
                entity.playSound(ModSounds.ENTITY_THUNDERBOLT_NEIGH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        });
    });

    private final AnimationHandler<EntityThunderbolt> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (!this.level().isClientSide) {
                    this.setChargeDirection(null);
                    if (anim != null) {
                        if (anim.is(CHARGE, CHARGE_2, CHARGE_3))
                            this.chargeAttackSuccess = false;
                        if (anim.is(HORN_ATTACK))
                            this.hornAttackSuccess = false;
                    }
                }
                return false;
            });
    protected boolean feintedDeath, hornAttackSuccess, chargeAttackSuccess;
    private Vec3 chargeMotion;

    public EntityThunderbolt(EntityType<? extends BossMonster> type, Level world) {
        super(type, world);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.THUNDERBOLT_FIGHT.get());
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.31);
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(Attributes.STEP_HEIGHT.value().getDefaultValue() + 1);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<EntityThunderbolt>create()
                .start(MonsterBehaviourUtils.checkedAttack(BACK_KICK)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> !m.feintedDeath)
                .prepare(new SetWalkTargetToAttackTarget<EntityThunderbolt>().speedMod((e, t) -> 1.2f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(LASER_X5)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> !m.isEnraged() && !m.feintedDeath)
                .prepare(new SetWalkTargetToAttackTarget<EntityThunderbolt>().closeEnoughDist((e, t) -> 4).speedMod((e, t) -> 1.2f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(STOMP)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> !m.feintedDeath)
                .prepare(new SetWalkTargetToAttackTarget<EntityThunderbolt>().speedMod((e, t) -> 1.2f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(11)
                .start(MonsterBehaviourUtils.checkedAttack(AnimationPlayHolder.<EntityThunderbolt>builder(HORN_ATTACK)
                        .start(BACK_KICK, m -> m.hornAttackSuccess).build())).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> !m.feintedDeath)
                .prepare(new SetWalkTargetToAttackTarget<EntityThunderbolt>().speedMod((e, t) -> 1.2f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(9)
                .start(MonsterBehaviourUtils.checkedAttack(AnimationPlayHolder.<EntityThunderbolt>builder(CHARGE)
                        .start(CHARGE_2, m -> !m.chargeAttackSuccess).build())
                ).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> !m.feintedDeath)
                .prepare(new SetWalkTargetToAttackTarget<EntityThunderbolt>().closeEnoughDist((e, t) -> 6)
                        .speedMod((e, t) -> 1.2f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(9)
                .start(MonsterBehaviourUtils.checkedAttack(LASER_AOE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> m.isEnraged() && !m.feintedDeath)
                .prepare(new SetWalkTargetToAttackTarget<EntityThunderbolt>().closeEnoughDist((e, t) -> 4).speedMod((e, t) -> 1.2f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(8)
                .start(MonsterBehaviourUtils.checkedAttack(AnimationPlayHolder.<EntityThunderbolt>builder(LASER_KICK)
                        .start(LASER_KICK_2).build())).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> m.isEnraged() && !m.feintedDeath)
                .prepare(new SetWalkTargetToAttackTarget<EntityThunderbolt>().speedMod((e, t) -> 1.2f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(8)
                .start(MonsterBehaviourUtils.checkedAttack(WIND_BLADE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> m.isEnraged() && !m.feintedDeath)
                .prepare(new SetWalkTargetToAttackTarget<EntityThunderbolt>().closeEnoughDist((e, t) -> 7).speedMod((e, t) -> 1.2f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(7)
                .start(MonsterBehaviourUtils.checkedAttack(WIND_BLADE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> m.isEnraged() && !m.feintedDeath && (m.getTarget() != null && m.getTarget().getY() - m.getY() > 4))
                .prepare(new SetWalkTargetToAttackTarget<EntityThunderbolt>().closeEnoughDist((e, t) -> 4).speedMod((e, t) -> 1.2f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                // After feinting death only use those below
                .start(MonsterBehaviourUtils.checkedAttack(LASER_AOE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(EntityThunderbolt::afterFeint)
                .prepare(new SetWalkTargetToAttackTarget<EntityThunderbolt>().closeEnoughDist((e, t) -> 4).speedMod((e, t) -> 1.2f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(8)
                .start(MonsterBehaviourUtils.checkedAttack(AnimationPlayHolder.<EntityThunderbolt>builder(CHARGE)
                        .start(CHARGE_2, m -> !m.chargeAttackSuccess).chain(CHARGE_3).build())
                ).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(EntityThunderbolt::afterFeint)
                .prepare(new SetWalkTargetToAttackTarget<EntityThunderbolt>().closeEnoughDist((e, t) -> 6)
                        .speedMod((e, t) -> 1.2f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(11)
                .start(MonsterBehaviourUtils.checkedAttack(AnimationPlayHolder.<EntityThunderbolt>builder(LASER_KICK)
                        .start(LASER_KICK_2).chain(LASER_KICK_3).build())).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(EntityThunderbolt::afterFeint)
                .prepare(new SetWalkTargetToAttackTarget<EntityThunderbolt>().speedMod((e, t) -> 1.2f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(12)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(7, new SetWalkTargetToAttackTarget<BaseMonster>().speedMod((e, t) -> 1.1f), new MoveToWalkTarget<>())
                .add(10, new StrafeTarget<BaseMonster>().strafeDistance(8)).build();
    }

    @Override
    protected ExtendedBehaviour<? extends BaseMonster> getWanderBehaviour() {
        return new SetRandomWalkTarget<BaseMonster>().speedModifier(0.7f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Feint", this.feintedDeath);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.feintedDeath = compound.getBoolean("Feint");
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        if (flag && !load) {
            if (!this.isEnraged()) {
                this.getAnimationHandler().setAnimation(NEIGH);
                this.getNavigation().stop();
            } else {
                this.getAnimationHandler().setAnimation(DEFEAT);
                this.getNavigation().stop();
                this.bossInfo.setProgress(0);
            }
        }
        super.setEnraged(flag, load);
    }

    @Override
    protected void updateBossBar() {
        if (!this.feintedDeath)
            this.bossInfo.setProgress((this.getHealth() - (this.getMaxHealth() * FEINT_THRESHOLD)) / (this.getMaxHealth() * (1 - FEINT_THRESHOLD)));
        else
            this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    protected boolean checkRage() {
        if (this.getHealth() / this.getMaxHealth() < FEINT_THRESHOLD)
            return !this.feintedDeath;
        if (this.getHealth() / this.getMaxHealth() < RANGE_THRESHOLD)
            return !this.isEnraged();
        return false;
    }

    @Override
    protected void fullyHeal() {
        super.fullyHeal();
        this.feintedDeath = false;
    }

    private boolean afterFeint() {
        return !this.isTamed() && this.isEnraged() && this.feintedDeath;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.getHealth() > 0 && this.getAnimationHandler().isCurrent(DEFEAT) && !this.feintedDeath && !this.isTamed()) {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim.done(0)) {
                this.feintDeath();
            }
        }
        if (this.getAnimationHandler().isCurrent(FEINT, DEFEAT) && !this.isTamed()) {
            Vec3 delta = this.getDeltaMovement();
            this.setDeltaMovement(0, delta.y, 0);
            if (this.getAnimationHandler().getAnimation().is(DEFEAT)) {
                int tick = (int) this.getAnimationHandler().getAnimation().getTick(1);
                if (tick < 40) {
                    if (tick % 10 == 0)
                        this.level().addParticle(new ColoredParticleData(ModParticles.BLINK.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
                                this.getX() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth()),
                                this.getY() + this.random.nextDouble() * (this.getBbHeight()),
                                this.getZ() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth()),
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D);
                } else if (tick < 80) {
                    if (tick % 2 == 0)
                        this.level().addParticle(new ColoredParticleData(ModParticles.BLINK.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
                                this.getX() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 2),
                                this.getY() + this.random.nextDouble() * (this.getBbHeight() + 1),
                                this.getZ() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 2),
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D);
                } else {
                    int amount = (tick - 80) / 10;
                    for (int i = 0; i < amount; i++) {
                        this.level().addParticle(new ColoredParticleData(ModParticles.BLINK.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
                                this.getX() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 3),
                                this.getY() + this.random.nextDouble() * (this.getBbHeight() + 1),
                                this.getZ() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 3),
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D);
                    }
                }
            }
        }
    }

    @Override
    public double sprintSpeedThreshold() {
        return 0.9;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrent(FEINT, DEFEAT, NEIGH))) && super.hurt(source, amount);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(FEINT, DEFEAT);
    }

    @Override
    public double ridingSpeedModifier() {
        return 1.5;
    }

    protected void feintDeath() {
        this.feintedDeath = true;
        this.getAnimationHandler().setAnimation(FEINT);
        STAT_INCREASE.forEach(att -> {
            AttributeInstance inst = this.getAttribute(att.get());
            inst.removeModifier(STAT_INCREASE_ID);
            inst.addPermanentModifier(new AttributeModifier(STAT_INCREASE_ID, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        });
    }

    @Override
    public boolean isAlive() {
        return super.isAlive() && (this.getAnimationHandler() == null || !this.getAnimationHandler().isCurrent(FEINT, DEFEAT));
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(FEINT, DEFEAT))
            return;
        super.push(x, y, z);
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(CHARGE, CHARGE_2, CHARGE_3)) {
            return this.chargeMotion;
        }
        return super.directionToLookAt();
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.is(STOMP)) {
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(1.7, -0.4, 1.7), this.getYRot(), 0, this.position());
        } else if (anim.is(CHARGE, CHARGE_2, CHARGE_3)) {
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow + 1), this.getYRot(), 0, this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.6;
        double length = this.getBbWidth() * 1.5;
        if (anim.is(HORN_ATTACK)) {
            width = this.getBbWidth() * 1.3;
            length = this.getBbWidth() * 1.8;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public int animationCooldown(String anim) {
        int cooldown = super.animationCooldown(anim);
        if (anim != null && this.feintedDeath) {
            if (anim.equals(LASER_KICK) || anim.equals(LASER_AOE) || anim.equals(CHARGE))
                cooldown += 40;
        }
        return cooldown;
    }

    @Override
    public void handleAttack(AnimationState anim) {
        BiConsumer<AnimationState, EntityThunderbolt> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AnimationHandler<EntityThunderbolt> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? ModSpells.LASER5.get() : null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(LASER_X5);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(STOMP);
            else
                this.getAnimationHandler().setAnimation(HORN_ATTACK);
        }
    }

    @Override
    public boolean allowAnimation(String prev, String other) {
        if (this.feintedDeath)
            return true;
        return super.allowAnimation(prev, other);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.HORSE_HURT;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.8f;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!state.liquid()) {
            BlockState blockstate = this.level().getBlockState(pos.above());
            SoundType soundtype = Platform.INSTANCE.getSoundType(state, this.level(), pos, this);
            if (blockstate.is(Blocks.SNOW)) {
                soundtype = Platform.INSTANCE.getSoundType(blockstate, this.level(), pos, this);
            }
            this.playSound(SoundEvents.HORSE_GALLOP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
        }
    }

    @Override
    public void playAngrySound() {
    }

    protected void setChargeDirection(Vec3 moveDirection) {
        this.chargeMotion = moveDirection;
        S2CMobUpdate.send(this, SyncableDatas.VEC_3, this.chargeMotion);
    }

    @Override
    public void onUpdate(SyncableEntityData.SyncedContainer<?> data) {
        super.onUpdate(data);
        data.runIf(SyncableDatas.VEC_3, motion -> this.chargeMotion = motion);
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getDeathAnimation() {
        return DEFEAT;
    }
}