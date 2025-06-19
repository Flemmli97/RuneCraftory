package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableDatas;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.network.S2CMobUpdate;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EntitySarcophagus extends BossMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String TELEPORT = BUILDER.add("teleport", AnimationsBuilder.definition(2.64)
            .marker("teleport_start_1", 0.2).marker("teleport_end_1", 0.44)
            .marker("teleport_start_2", 1.2).marker("teleport_end_2", 1.44)
            .marker("teleport_start_3", 2.2).marker("teleport_end_3", 2.44)
            .marker("teleport", 0.28, 1.28, 2.28));
    public static final String CHARGE = BUILDER.add("charge", AnimationsBuilder.definition(1.6)
            .marker("attack_start", 0.28).marker("attack_end", 1.48));
    public static final String BEAM = BUILDER.add("cast", AnimationsBuilder.definition(0.68).marker("attack", 0.48));
    public static final String BEAM_3X = BUILDER.add("cast_3x", AnimationsBuilder.definition(2.2).marker("attack", 0.48, 1.24, 2.0));
    public static final String FIRE_CIRCLE = BUILDER.add("circle_cast", AnimationsBuilder.definition(2.32).marker("attack", 0.4));
    public static final String WIND_CIRCLE = BUILDER.add("wind_circle", FIRE_CIRCLE);
    public static final String ICE_CIRCLE = BUILDER.add("ice_circle", FIRE_CIRCLE);
    public static final String EARTH_CIRCLE = BUILDER.add("earth_circle", FIRE_CIRCLE);
    public static final String LIGHT_2X = BUILDER.add("light_2x", BEAM);
    public static final String LIGHT_4X = BUILDER.add("light_4x", BEAM);
    public static final String SHINE = BUILDER.add("shine", BEAM);
    public static final String PRISM = BUILDER.add("prism", BEAM);
    public static final String MISSILE = BUILDER.add("missile", AnimationsBuilder.definition(1.04).marker("attack", 0.72));
    public static final String STARFALL = BUILDER.add("starfall", AnimationsBuilder.definition(8.2).marker("attack", 0.8)
            .marker("attack_start", 0.24).marker("attack_end", 7.96)
            .marker("teleport_start", 0.2).marker("teleport_end", 8.0));
    public static final String ANGRY = BUILDER.add("angry", AnimationsBuilder.definition(1.04));
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(10).infinite());
    public static final String INTERACT = BUILDER.add("interact", BEAM);
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimationState, EntitySarcophagus>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(TELEPORT, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("teleport_start_1") || anim.isAt("teleport_end_1")
                    || anim.isAt("teleport_start_2") || anim.isAt("teleport_end_2")
                    || anim.isAt("teleport_start_3") || anim.isAt("teleport_end_3")) {
                entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
            if (anim.isAt("teleport"))
                entity.teleportAround(8, 10);
        });
        b.put(CHARGE, (anim, entity) -> {
            if (anim.isPast("attack_start") && !anim.isPast("attack_end")) {
                if (entity.hitEntity == null)
                    entity.hitEntity = new ArrayList<>();
                if (entity.chargeMotion == null) {
                    entity.setChargeDirection(EntityUtils.getTargetDirection(entity, EntityAnchorArgument.Anchor.FEET, true)
                            .scale(0.3));
                }
                entity.setDeltaMovement(entity.chargeMotion.x(), entity.getDeltaMovement().y(), entity.chargeMotion.z());
                entity.mobAttack(anim, null, e -> {
                    if (!entity.hitEntity.contains(e) && CombatUtils.mobAttack(entity, e,
                            new CustomDamage.Builder(entity).hurtResistant(5).knock(CustomDamage.KnockBackType.BACK).knockAmount(2))) {
                        entity.hitEntity.add(e);
                    }
                });
                if (entity.tickCount % 5 == 0) {
                    entity.playSound(ModSounds.ENTITY_GENERIC_HEAVY_CHARGE.get(), 1, (entity.random.nextFloat() - entity.random.nextFloat()) * 0.2f + 1.0f);
                }
            } else {
                entity.setDeltaMovement(entity.getDeltaMovement().scale(0.6));
            }
        });
        b.put(BEAM, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.LIGHT_BEAM.get().use(entity);
        });
        b.put(BEAM_3X, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.LIGHT_BEAM.get().use(entity);
        });
        b.put(FIRE_CIRCLE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.FIRE_CIRCLE.get().use(entity);
        });
        b.put(WIND_CIRCLE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.WIND_CIRCLE.get().use(entity);
        });
        b.put(ICE_CIRCLE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.ICE_CIRCLE.get().use(entity);
        });
        b.put(EARTH_CIRCLE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.EARTH_CIRCLE.get().use(entity);
        });
        b.put(LIGHT_2X, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.EXPANDING_DOUBLE_LIGHT.get().use(entity);
        });
        b.put(LIGHT_4X, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.EXPANDING_QUAD_LIGHT.get().use(entity);
        });
        b.put(SHINE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.SHINE.get().use(entity);
        });
        b.put(PRISM, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.PRISM_LONG.get().use(entity);
        });
        b.put(MISSILE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack"))
                ModSpells.MISSILE_8X.get().use(entity);
        });
        b.put(STARFALL, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack_start")) {
                entity.starFallPre = entity.position();
                if (entity.hasRestriction()) {
                    Vec3 pos = Vec3.atCenterOf(entity.getRestrictCenter());
                    entity.teleportTo(pos.x, pos.y + 8, pos.z);
                } else if (entity.getTarget() != null) {
                    entity.teleportTo(entity.getTarget().getX(), entity.getTarget().getY() + 8, entity.getTarget().getZ());
                } else {
                    entity.teleportTo(entity.getX(), entity.getY() + 8, entity.getZ());
                }
                entity.starFallPos = entity.position();
                ModSpells.STARFALL_LONG.get().use(entity);
            }
            if (entity.starFallPos != null)
                entity.setPos(entity.starFallPos);
            if (anim.isAt("attack_end")) {
                entity.teleportTo(entity.starFallPre.x(), entity.starFallPre.y(), entity.starFallPre.z());
                entity.starFallPre = null;
                entity.starFallPos = null;
                entity.teleportAround(6, 12);
            }
        });
    });

    //    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntitySarcophagus>>> ATTACKS = List.of(
//            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(TELEPORT)
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 10),
//            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(CHARGE)
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 8),
//            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(BEAM)
//                    .withCondition((goal, target, previous) -> !goal.attacker.isEnraged() && goal.attacker.allowAnimation(previous, BEAM))
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 8),
//            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>enragedBossAttack(BEAM_3X)
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 8),
//            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(FIRE_CIRCLE)
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 4),
//            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(WIND_CIRCLE)
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 4),
//            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(ICE_CIRCLE)
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 4),
//            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(EARTH_CIRCLE)
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 4),
//            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(LIGHT_2X)
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 8),
//            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>enragedBossAttack(LIGHT_4X)
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 8),
//            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(SHINE)
//                    .withCondition((goal, target, previous) -> !goal.attacker.isEnraged() && goal.attacker.allowAnimation(previous, SHINE))
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 7),
//            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>enragedBossAttack(PRISM)
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 7),
//            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(MISSILE)
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 8),
//            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>enragedBossAttack(STARFALL)
//                    .withCondition(((goal, target, previous) -> goal.attacker.isEnraged() && goal.attacker.starfallCooldown <= 0))
//                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 10)
//    );
//    private static final List<WeightedEntry.Wrapper<IdleAction<EntitySarcophagus>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new DoNothingRunner<>(true)), 1)
//    );
//
//    public final AnimatedAttackGoal<EntitySarcophagus> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntitySarcophagus> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        this.hitEntity = null;
//        if (anim != null) {
//            this.teleported = TELEPORT.is(anim);
//            if (!TELEPORT.is(anim))
//                this.previousAttack = anim.getID();
//            if (CHARGE.is(anim)) {
//                this.chargeMotion = null;
//                this.prevStepHeight = this.maxUpStep;
//                this.maxUpStep = 1 + this.maxUpStep;
//            }
//            if (STARFALL.is(anim)) {
//                this.gravityPre = this.isNoGravity();
//                this.starfallCooldown = 240 + this.getRandom().nextInt(600);
//                this.setNoGravity(true);
//            }
//        } else {
//            if (this.prevStepHeight != -1) {
//                this.maxUpStep = this.prevStepHeight;
//                this.prevStepHeight = -1;
//            }
//            if (this.getAnimationHandler().isCurrent(STARFALL)) {
//                this.setNoGravity(this.gravityPre);
//            }
//        }
        return false;
    });

    private Vec3 chargeMotion, starFallPre, starFallPos;
    protected List<LivingEntity> hitEntity;
    private boolean teleported, gravityPre;
    private String previousAttack = "";
    private int starfallCooldown;
    private float prevStepHeight = -1;

    public EntitySarcophagus(EntityType<? extends EntitySarcophagus> type, Level world) {
        super(type, world);
//        this.maxUpStep = 1;
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.SARCOPHAGUS_FIGHT.get());
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.26);
        super.applyAttributes();
    }

//    @Override
//    public void addGoal() {
//        super.addGoal();
//        this.goalSelector.removeGoal(this.wander);
//        this.wander = new RestrictedWaterAvoidingStrollGoal(this, 0.6);
//        this.goalSelector.addGoal(6, this.wander);
//    }

    @Override
    public double sprintSpeedThreshold() {
        return 0.9;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return (!this.getAnimationHandler().hasAnimation() || (!this.getAnimationHandler().isCurrent(DEFEAT, ANGRY) && !this.isTeleporting())) && super.hurt(source, amount);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(ANGRY, DEFEAT);
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        if (this.isTeleporting())
            return false;
        return super.shouldRender(x, y, z);
    }

    private boolean isTeleporting() {
        AnimationState anim = this.getAnimationHandler().getAnimation();
//        if (TELEPORT.is(anim)) {
//            if (anim.isPast("teleport_start_1") && !anim.isPast("teleport_end_1"))
//                return true;
//            if (anim.isPast("teleport_start_2") && !anim.isPast("teleport_end_2"))
//                return true;
//            return anim.isPast("teleport_start_3") && !anim.isPast("teleport_end_3");
//        } else if (STARFALL.is(anim)) {
//            return anim.isPast("teleport_start") && !anim.isPast("teleport_end");
//        }
        return false;
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(ANGRY, DEFEAT, TELEPORT, STARFALL))
            return;
        super.push(x, y, z);
    }

    @Override
    public int animationCooldown(String anim) {
        if (TELEPORT.equals(anim))
            return 7 + this.getRandom().nextInt(6);
        return super.animationCooldown(anim);
    }

    @Override
    public String getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.equals(CHARGE)) {
            double width = this.getBbWidth();
            double speed = Math.max(width, this.getDeltaMovement().length() - width);
            float rotY = -Mth.wrapDegrees((float) (Mth.atan2(this.getDeltaMovement().x(), this.getDeltaMovement().z()) * Mth.RAD_TO_DEG));
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow + 0.4, 0.1, grow + 0.4).expandTowards(0, 0, speed), rotY, 0, this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            this.getNavigation().stop();
            this.getLookControl().setLookAt(target, 60.0f, 30.0f);
        }
        BiConsumer<AnimationState, EntitySarcophagus> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.FIRE_CIRCLE.get()))
                    this.getAnimationHandler().setAnimation(FIRE_CIRCLE);
            } else if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.MISSILE_8X.get()))
                    this.getAnimationHandler().setAnimation(MISSILE);
            } else {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.LIGHT_BEAM.get()))
                    this.getAnimationHandler().setAnimation(BEAM);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            --this.starfallCooldown;
        }
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(CHARGE)) {
            if (this.getAnimationHandler().getAnimation().isPast("attack_start"))
                return this.chargeMotion;
            return null;
        }
        return super.directionToLookAt();
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

//    @Override
//    public Vec3 passengerOffset(Entity passenger) {
//        return new Vec3(0, 29.5 / 16d, -8 / 16d);
//    }

    @Override
    public AnimationHandler<EntitySarcophagus> getAnimationHandler() {
        return this.animationHandler;
    }
//
//    @Override
//    public boolean allowAnimation(String prev, AnimatedAction other) {
//        if (!this.teleported)
//            return TELEPORT.is(other);
//        return !TELEPORT.is(other) && (!this.previousAttack.equals(other.getID()));
//    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    private void teleportAround(int range, int yRange) {
        Vec3 pos;
        if (this.hasRestriction())
            pos = Vec3.atCenterOf(this.getRestrictCenter());
        else if (this.getTarget() != null)
            pos = this.getTarget().position();
        else
            pos = this.position();
        for (int i = 0; i < 10; i++) {
            double x = pos.x() + (this.getRandom().nextDouble() * 2 - 1) * range;
            double y = pos.y() + 4;
            double z = pos.z() + (this.getRandom().nextDouble() * 2 - 1) * range;
            if (this.hasRestriction() && this.getRestrictCenter().distToCenterSqr(x, y, z) > this.getRestrictRadius() * this.getRestrictRadius()) {
                continue;
            }
            if (this.teleport(x, y, z, yRange))
                return;
        }
    }

    private boolean teleport(double x, double y, double z, int yRange) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y - 1, z);
        ChunkAccess chunk = this.level().getChunk(pos);
        while (yRange > 0 && pos.getY() > this.level().getMinBuildHeight() && !chunk.getBlockState(pos).blocksMotion()) {
            pos.move(Direction.DOWN);
            yRange--;
            y--;
        }
        BlockState blockState = chunk.getBlockState(pos);
        if (!blockState.blocksMotion()) {
            return false;
        }
        Vec3 current = this.position();
        this.teleportTo(x, y, z);
        if (!this.level().noCollision(this) || this.level().containsAnyLiquid(this.getBoundingBox())) {
            this.teleportTo(current.x(), current.y(), current.z());
            return false;
        }
        return true;
    }

    protected void setChargeDirection(Vec3 moveDirection) {
        this.chargeMotion = moveDirection;
        S2CMobUpdate.send(this, SyncableDatas.MOTION_DIR, this.chargeMotion);
    }

    @Override
    public void onUpdate(SyncableEntityData.SyncedContainer<?> data) {
        super.onUpdate(data);
        data.runIf(SyncableDatas.MOTION_DIR, motion -> this.chargeMotion = motion);
    }
}