package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.entities.ai.RestrictedWaterAvoidingStrollGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.boss.ThunderboltAttackGoal;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class EntityThunderbolt extends BossMonster {

    public static final AnimatedAction BACK_KICK = new AnimatedAction(13, 7, "back_kick");
    public static final AnimatedAction LASER_X5 = new AnimatedAction(29, 25, "laser_x5");
    public static final AnimatedAction STOMP = new AnimatedAction(9, 6, "stomp");
    public static final AnimatedAction HORN_ATTACK = new AnimatedAction(9, 5, "horn_attack");
    public static final AnimatedAction BACK_KICK_HORN = AnimatedAction.copyOf(BACK_KICK, "back_kick_horn");
    public static final AnimatedAction CHARGE = new AnimatedAction(31, 9, "charge");
    public static final AnimatedAction CHARGE_2 = AnimatedAction.copyOf(CHARGE, "charge_2");
    public static final AnimatedAction CHARGE_3 = AnimatedAction.copyOf(CHARGE, "charge_3");
    public static final AnimatedAction LASER_AOE = AnimatedAction.copyOf(LASER_X5, "laser_aoe");
    public static final AnimatedAction LASER_KICK = new AnimatedAction(16, 6, "laser_kick");
    public static final AnimatedAction LASER_KICK_2 = AnimatedAction.copyOf(LASER_KICK, "laser_kick_2");
    public static final AnimatedAction WIND_BLADE = new AnimatedAction(15, 8, "wind_blade");
    public static final AnimatedAction LASER_KICK_3 = AnimatedAction.copyOf(LASER_KICK, "laser_kick_3");
    public static final AnimatedAction FEINT = new AnimatedAction(40, 18, "feint");
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(80, "defeat").marker(60).infinite().build();
    public static final AnimatedAction NEIGH = new AnimatedAction(24, 9, "neigh");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(STOMP, "interact");

    public static final ImmutableList<String> NON_CHOOSABLE_ATTACKS = ImmutableList.of(CHARGE_2.getID(), CHARGE_3.getID(), LASER_KICK_2.getID(), LASER_KICK_3.getID(), BACK_KICK_HORN.getID());
    private static final AnimatedAction[] ANIMATED_ACTIONS = new AnimatedAction[]{BACK_KICK, LASER_X5, STOMP, HORN_ATTACK, BACK_KICK_HORN, CHARGE, CHARGE_2, CHARGE_3,
            LASER_AOE, LASER_KICK, LASER_KICK_2, WIND_BLADE, LASER_KICK_3, FEINT, DEFEAT, NEIGH, INTERACT};
    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityThunderbolt>> ATTACK_HANDLER = createAnimationHandler(b -> {
        BiConsumer<AnimatedAction, EntityThunderbolt> kick = (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            if (anim.getTick() == 1 && entity.getTarget() != null) {
                entity.targetPosition = entity.getTarget().position();
            }
            if (anim.canAttack()) {
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
            if (anim.getTick() == 1 && entity.getTarget() != null) {
                entity.targetPosition = entity.getTarget().position();
            }
            if (anim.canAttack()) {
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
                    entity.attack.setIdleTime(1);
                }
            }
        });
        b.put(STOMP, (anim, entity) -> {
            if (anim.canAttack()) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        });
        b.put(WIND_BLADE, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.DOUBLE_SONIC.get().use(entity);
            }
        });
        b.put(LASER_X5, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.LASER5.get().use(entity);
            }
        });
        b.put(LASER_AOE, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.LASER_AOE.get().use(entity);
            }
        });
        BiConsumer<AnimatedAction, EntityThunderbolt> bigLaser = (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.BIG_LIGHTNING.get().use(entity);
            }
        };
        b.put(LASER_KICK, bigLaser);
        b.put(LASER_KICK_2, bigLaser);
        b.put(LASER_KICK_3, bigLaser);
        BiConsumer<AnimatedAction, EntityThunderbolt> charge = (anim, entity) -> {
            if ((anim.getTick() < anim.getLength() - 6 && anim.getTick() > anim.getAttackTime()) && !entity.chargeAttackSuccess) {
                if (entity.chargeMotion != null) {
                    entity.setDeltaMovement(entity.chargeMotion[0], entity.getDeltaMovement().y, entity.chargeMotion[2]);
                }
                entity.mobAttack(anim, null, e -> {
                    if (entity.doHurtTarget(e)) {
                        entity.chargeAttackSuccess = true;
                        entity.attack.setIdleTime(entity.animationCooldown(null));
                    }
                });
            }
        };
        b.put(CHARGE, charge);
        b.put(CHARGE_2, charge);
        b.put(CHARGE_3, charge);
        b.put(NEIGH, (anim, entity) -> {
            if (anim.canAttack())
                entity.playSound(ModSounds.ENTITY_THUNDERBOLT_NEIGH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        });
        b.put(FEINT, (anim, entity) -> {
            if (anim.canAttack())
                entity.playSound(ModSounds.ENTITY_THUNDERBOLT_NEIGH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        });
    });

    private static final EntityDataAccessor<Float> LOCKED_YAW = SynchedEntityData.defineId(EntityThunderbolt.class, EntityDataSerializers.FLOAT);

    public final ThunderboltAttackGoal<EntityThunderbolt> attack = new ThunderboltAttackGoal<>(this);
    private final AnimationHandler<EntityThunderbolt> animationHandler = new AnimationHandler<>(this, ANIMATED_ACTIONS)
            .setAnimationChangeCons(anim -> {
                if (!this.level.isClientSide) {
                    if (this.hornAttackSuccess && anim != null) {
                        this.hornAttackSuccess = false;
                    }
                    if (this.chargeAttackSuccess && anim != null)
                        this.chargeAttackSuccess = false;
                }
            });
    protected boolean feintedDeath, hornAttackSuccess, chargeAttackSuccess;
    private double[] chargeMotion;

    public EntityThunderbolt(EntityType<? extends BossMonster> type, Level world) {
        super(type, world);
        this.bossInfo.setColor(BossEvent.BossBarColor.BLUE);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
        this.maxUpStep = 1;
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.THUNDERBOLT_FIGHT.get());
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.31);
        super.applyAttributes();
    }

    @Override
    public void addGoal() {
        super.addGoal();
        this.goalSelector.removeGoal(this.wander);
        this.wander = new RestrictedWaterAvoidingStrollGoal(this, 0.6);
        this.goalSelector.addGoal(6, this.wander);
    }

    @Override
    public double sprintSpeedThreshold() {
        return 0.9;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (anim.is(FEINT, DEFEAT, NEIGH, INTERACT))
            return false;
        if (type == AnimationType.GENERICATTACK)
            return this.isEnraged() ? !anim.is(LASER_X5) : !anim.is(LASER_AOE) && !anim.is(LASER_KICK);
        return false;
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        if (anim != null)
            switch (anim.getID()) {
                case "laser_kick_2":
                case "laser_kick_3":
                case "charge":
                case "charge_2":
                case "charge_3":
                    return 1;
            }
        return super.animationCooldown(anim);
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
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(FEINT, DEFEAT))
            return;
        super.push(x, y, z);
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return this.getBbWidth() * 0.8;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            this.getNavigation().stop();
            this.getLookControl().setLookAt(target, 30.0f, 30.0f);
        }
        BiConsumer<AnimatedAction, EntityThunderbolt> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(STOMP)) {
            return this.getBoundingBox().inflate(1.5, -0.4, 1.5);
        } else
            return super.calculateAttackAABB(anim, target, grow);
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
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.HORSE_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HORSE_AMBIENT;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.8f;
    }

    @Override
    public double ridingSpeedModifier() {
        return 1.1;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOCKED_YAW, 0f);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.getHealth() > 0 && this.getAnimationHandler().isCurrent(DEFEAT) && !this.feintedDeath && !this.isTamed()) {
            AnimatedAction anim = this.getAnimationHandler().getAnimation();
            if (anim.getTick() > anim.getLength()) {
                this.feintedDeath = true;
                this.getAnimationHandler().setAnimation(FEINT);
            }
        }
        if (this.getAnimationHandler().isCurrent(CHARGE, CHARGE_2, CHARGE_3)) {
            this.setXRot(0);
            this.setYRot(this.entityData.get(LOCKED_YAW));
        }
        if (this.getAnimationHandler().isCurrent(FEINT, DEFEAT) && !this.isTamed()) {
            Vec3 delta = this.getDeltaMovement();
            this.setDeltaMovement(0, delta.y, 0);
            if (this.getAnimationHandler().getAnimation().is(DEFEAT)) {
                int tick = this.getAnimationHandler().getAnimation().getTick();
                if (tick < 40) {
                    if (tick % 10 == 0)
                        this.level.addParticle(new ColoredParticleData(ModParticles.BLINK.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
                                this.getX() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth()),
                                this.getY() + this.random.nextDouble() * (this.getBbHeight()),
                                this.getZ() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth()),
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D);
                } else if (tick < 80) {
                    if (tick % 2 == 0)
                        this.level.addParticle(new ColoredParticleData(ModParticles.BLINK.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
                                this.getX() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 2),
                                this.getY() + this.random.nextDouble() * (this.getBbHeight() + 1),
                                this.getZ() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 2),
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D);
                } else {
                    int amount = (tick - 80) / 10;
                    for (int i = 0; i < amount; i++) {
                        this.level.addParticle(new ColoredParticleData(ModParticles.BLINK.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
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
            this.bossInfo.setProgress((this.getHealth() - (this.getMaxHealth() * 0.3f)) / (this.getMaxHealth() * 0.7f));
        else
            this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    protected boolean checkRage() {
        if (this.getHealth() / this.getMaxHealth() < 0.3)
            return !this.feintedDeath;
        if (this.getHealth() / this.getMaxHealth() < 0.7)
            return !this.isEnraged();
        return false;
    }

    @Override
    public boolean isAlive() {
        return super.isAlive() && (this.getAnimationHandler() == null || !this.getAnimationHandler().isCurrent(FEINT, DEFEAT));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!state.getMaterial().isLiquid()) {
            BlockState blockstate = this.level.getBlockState(pos.above());
            SoundType soundtype = Platform.INSTANCE.getSoundType(state, this.level, pos, this);
            if (blockstate.is(Blocks.SNOW)) {
                soundtype = Platform.INSTANCE.getSoundType(blockstate, this.level, pos, this);
            }
            this.playSound(SoundEvents.HORSE_GALLOP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
        }
    }

    @Override
    public double getPassengersRidingOffset() {

        return this.getBbHeight() * 0.825D;
    }

    public void setChargeMotion(double[] charge) {
        this.chargeMotion = charge;
    }

    public double[] getChargeTo(AnimatedAction anim, Vec3 dir) {
        int length = anim.getLength() - anim.getAttackTime() - 6; //stop charging 6 ticks earlier
        Vec3 vec = dir.normalize().scale(7);
        return new double[]{vec.x / length, this.getY(), vec.z / length};
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    @Override
    public AnimationHandler<EntityThunderbolt> getAnimationHandler() {
        return this.animationHandler;
    }

    public boolean isAnimEqual(String prev, AnimatedAction other) {
        if (other == null)
            return true;
        if (prev.equals(CHARGE_2.getID()) || prev.equals(CHARGE_3.getID()))
            return other.getID().equals(CHARGE.getID());
        if (prev.equals(LASER_KICK_2.getID()) || prev.equals(LASER_KICK_3.getID()))
            return other.getID().equals(LASER_KICK.getID());
        if (prev.equals(BACK_KICK_HORN.getID()))
            return other.getID().equals(HORN_ATTACK.getID());
        return prev.equals(other.getID());
    }

    public AnimatedAction chainAnim(String prev) {
        return switch (prev) {
            case "laser_kick" -> LASER_KICK_2;
            case "laser_kick_2" -> this.feintedDeath ? LASER_KICK_3 : null;
            case "horn_attack" -> this.hornAttackSuccess ? BACK_KICK_HORN : null;
            case "charge" -> this.chargeAttackSuccess ? null : CHARGE_2;
            case "charge_2" -> this.isEnraged() && !this.chargeAttackSuccess ? CHARGE_3 : null;
            default -> null;
        };
    }

    public void lockYaw(float yaw) {
        this.entityData.set(LOCKED_YAW, yaw);
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public void playAngrySound() {
    }
}