package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableDatas;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.network.S2CMobUpdate;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EntityGrimoire extends BossMonster {

    private static final List<Vector3d> CIRCLE_PARTICLE_MOTION = MathUtils.rotatedVecs(new Vector3d(0.25, 0, 0), new Vector3d(0, 1, 0), -180, 175, 5);

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String TAIL_SWIPE = BUILDER.add("tail_swipe", AnimationsBuilder.definition(0.84).marker("attack", 0.48));
    public static final String INTERACT = BUILDER.add("interact", TAIL_SWIPE);
    public static final String BITE = BUILDER.add("bite", AnimationsBuilder.definition(0.8).marker("attack", 0.44));
    public static final String GUST = BUILDER.add("gust", AnimationsBuilder.definition(1.96).marker("attack", 0.32));
    public static final String CHARGE = BUILDER.add("charge", AnimationsBuilder.definition(1.72).infinite()
            .marker("charge_start", 0.16).marker("charge_end", 1.6));
    public static final String CHARGE_LAND = BUILDER.add("charge_land", AnimationsBuilder.definition(0.48).marker("attack", 0.16));
    public static final String WIND_BREATH = BUILDER.add("wind_breath", AnimationsBuilder.definition(1.36).marker("attack", 0.44));
    public static final String TORNADO = BUILDER.add("tornado", AnimationsBuilder.definition(1.24).marker("attack", 0.4));
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(10).infinite());
    public static final String ANGRY = BUILDER.add("angry", AnimationsBuilder.definition(1.44).infinite());
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimationState, EntityGrimoire>> ATTACK_HANDLER = createAnimationHandler(b -> {
        BiConsumer<AnimationState, EntityGrimoire> melee = (anim, entity) -> {
            if (anim.isAt("attack")) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        };
        b.put(TAIL_SWIPE, melee);
        b.put(BITE, melee);
        b.put(GUST, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.GUST_ROCKS.get().use(entity);
            }
        });
        b.put(WIND_BREATH, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.WIND_BLADE_BARRAGE.get().use(entity);
            }
        });
        b.put(TORNADO, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.TORNADO.get().use(entity);
            }
        });
        b.put(CHARGE, (anim, entity) -> {
            if (anim.isPast("charge_start") && !anim.isPast("charge_end")) {
                if (entity.hitEntity == null) {
                    entity.hitEntity = new ArrayList<>();
                }
                if (entity.moveDirection == null) {
                    entity.setMoveDirection(EntityUtils.getTargetDirection(entity, EntityAnchorArgument.Anchor.FEET, true)
                            .scale(0.35));
                }
                entity.setDeltaMovement(entity.moveDirection);
                entity.mobAttack(anim, null, e -> {
                    if (!entity.hitEntity.contains(e) && CombatUtils.mobAttack(entity, e,
                            new DynamicDamage.Builder(entity).hurtResistant(5).knock(DynamicDamage.KnockBackType.BACK).knockAmount(2))) {
                        entity.hitEntity.add(e);
                    }
                });
            }
            if (anim.isPast("charge_end")) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(0, -0.06, 0));
                if (entity.getDeltaMovement().y < -0.72) {
                    entity.setDeltaMovement(entity.getDeltaMovement().x, -0.72, entity.getDeltaMovement().z);
                }
                if (entity.onGround()) {
                    entity.getAnimationHandler().setAnimation(CHARGE_LAND);
                }
                // Stuck check. Or e.g. if in water
                if (anim.isPast(6) && (!entity.getBlockStateOn().is(Blocks.AIR) || !entity.getBlockStateOn().is(Blocks.AIR))) {
                    entity.getAnimationHandler().setAnimation(CHARGE_LAND);
                }
            }
        });
        b.put(CHARGE_LAND, (anim, entity) -> {
            if (anim.isAt("attack")) {
                DynamicDamage.Builder source = new DynamicDamage.Builder(entity).noKnockback().element(ItemElement.WIND).hurtResistant(5);
                entity.mobAttack(anim, entity.getTarget(), e -> CombatUtils.mobAttack(entity, e, source));
                S2CScreenShake.sendAround(entity, 24, 4, 3);
                entity.level().playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), entity.getSoundSource(), 1.0f, 0.9f);
                entity.level().broadcastEntityEvent(entity, (byte) 66);
            }
        });
    });

    private final AnimationHandler<EntityGrimoire> animationHandler = new AnimationHandler<>(this, ANIMS).withChangeListener(anim -> {
        if (anim == null || anim.is(CHARGE)) {
            this.hitEntity = null;
        }
        if (!this.level().isClientSide && anim == null) {
            boolean chain = !this.commanded;
            this.setMoveDirection(null);
            this.commanded = false;
            if (chain) {
                if (this.isEnraged() && this.getAnimationHandler().isCurrent(BITE)) {
                    this.getAnimationHandler().setAnimation(TAIL_SWIPE);
                    return true;
                }
            }
        }
        return false;
    });
    protected List<LivingEntity> hitEntity;
    private boolean commanded;
    private Vec3 moveDirection;

    public EntityGrimoire(EntityType<? extends EntityGrimoire> type, Level world) {
        super(type, world);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.GRIMOIRE_FIGHT.get());
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.29);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BossMonster>create()
                .start(MonsterBehaviourUtils.checkedAttack(TAIL_SWIPE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<BossMonster>().speedMod((e, t) -> 1.1f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(AnimationPlayHolder.<BossMonster>builder(BITE)
                        .start(TAIL_SWIPE, BossMonster::isEnraged).build())).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<BossMonster>().speedMod((e, t) -> 1.1f))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(GUST)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<BossMonster>().min(4).max(8))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(9)
                .start(MonsterBehaviourUtils.checkedAttack(CHARGE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<BossMonster>().speedMod((e, t) -> 1.1f)
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(6)))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(8)
                .start(MonsterBehaviourUtils.checkedAttack(WIND_BREATH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<BossMonster>().min(3).max(8))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(8)
                .start(MonsterBehaviourUtils.checkedAttack(TORNADO)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(BossMonster::isEnraged)
                .prepare(new SetWalkTargetWithinDist<BossMonster>().min(4).max(7))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(1, new SetWalkTargetToAttackTarget<>(), new MoveToWalkTarget<>()).build();
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 66) {
            for (Vector3d vec : CIRCLE_PARTICLE_MOTION) {
                this.level().addParticle(new ColoredParticleData(ModParticles.WIND.get(), 67 / 255F, 163 / 255F, 65 / 255F, 1, 0.4f), this.getX(), this.getY() + 0.2, this.getZ(), vec.x(), vec.y(), vec.z());
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return (!this.getAnimationHandler().isCurrent(ANGRY)) && super.hurt(source, amount);
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
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(ANGRY, DEFEAT, CHARGE))
            return;
        super.push(x, y, z);
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(CHARGE, CHARGE_LAND)) {
            return this.moveDirection;
        }
        return super.directionToLookAt();
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.is(CHARGE_LAND))
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this).inflate(1.2, 0.1, 1.2), this.getYRot(), 0, this.position());
        if (anim.is(CHARGE)) {
            double width = this.getBbWidth();
            double speed = Math.max(width, this.getDeltaMovement().length() - width);
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow + 0.5, 0.1, grow + 0.5).expandTowards(0, 0, speed), this.getYRot(), 0, this.position());
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.4;
        double length = this.getBbWidth() * 1.5;
        if (anim.is(TAIL_SWIPE)) {
            width = this.getBbWidth() * 1.5;
            length = this.getBbWidth() * 1.45;
            return new AABB(-width * 0.65, -0.02, 0, width * 0.35, this.getBbHeight() + 0.02, length);
        }
        if (anim.is(BITE)) {
            width = this.getBbWidth() * 1.1;
            length = this.getBbWidth() * 1.4;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        BiConsumer<AnimationState, EntityGrimoire> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AnimationHandler<EntityGrimoire> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.WIND_BLADE_BARRAGE.get()))
                    this.getAnimationHandler().setAnimation(WIND_BREATH);
            } else if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                    this.getAnimationHandler().setAnimation(TAIL_SWIPE);
            } else if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                this.getAnimationHandler().setAnimation(BITE);
            this.commanded = true;
        }
    }

    @Override
    public boolean allowAnimation(String prev, String other) {
        if (prev != null && prev.equals(BITE))
            return !this.isEnraged() || !TAIL_SWIPE.equals(other);
        return super.allowAnimation(prev, other);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    protected void setMoveDirection(Vec3 moveDirection) {
        this.moveDirection = moveDirection;
        S2CMobUpdate.send(this, SyncableDatas.VEC_3, this.moveDirection);
    }

    @Override
    public void onUpdate(SyncableEntityData.SyncedContainer<?> data) {
        super.onUpdate(data);
        data.runIf(SyncableDatas.VEC_3, motion -> this.moveDirection = motion);
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }
}
