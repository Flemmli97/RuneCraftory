package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Mimic extends LeapingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String MELEE = BUILDER.add("attack", AnimationsBuilder.definition(0.6).marker("attack", 0.44));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
    public static final String LEAP = BUILDER.add("leap", AnimationsBuilder.definition(0.6)
            .marker("attack_start", 0.2).marker("attack_end", 0.48));
    public static final String THROW = BUILDER.add("throw", AnimationsBuilder.definition(0.6).marker("attack", 0.44));
    public static final String ARROW = BUILDER.add("arrow", THROW);
    public static final String CAST = BUILDER.add("cast", AnimationsBuilder.definition(0.6).marker("attack", 0.44));
    public static final String CLOSE = BUILDER.add("close", AnimationsBuilder.definition(0.32));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();
    private static final EntityDataAccessor<Boolean> AWAKE = SynchedEntityData.defineId(Mimic.class, EntityDataSerializers.BOOLEAN);
    private final AnimationHandler<Mimic> animationHandler = new AnimationHandler<>(this, ANIMS);
    private final List<ItemStack> throwables = List.of(
            new ItemStack(Items.APPLE),
            new ItemStack(RuneCraftoryItems.BATTLE_AXE.get()),
            new ItemStack(RuneCraftoryItems.STEEL_SWORD.get()),
            new ItemStack(RuneCraftoryItems.MUSHROOM.get())
    );
    private int sleepTick = -1;
    private boolean sleeping;

    public Mimic(EntityType<? extends Mimic> type, Level level) {
        super(type, level);
        this.moveControl = new JumpingMover(this);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(AWAKE, false);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
        super.applyAttributes();
    }

    @Override
    public List<? extends ExtendedSensor<? extends BaseMonster>> getSensors() {
        return List.of(new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<Mimic>()
                        .setRadius(5)
                        .setPredicate((target, entity) -> !entity.isAwake() && entity.targetPred.test(target))
                        .setScanRate(e -> 4),
                new NearbyLivingEntitySensor<Mimic>()
                        .setPredicate((target, entity) -> entity.isAwake() && entity.targetPred.test(target))
                        .setScanRate(e -> 10),
                new HurtBySensor<>());
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(MELEE).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<BaseMonster>().closeEnoughDist(MonsterBehaviourUtils.closeEnough(1))).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(4)
                .start(LEAP).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<BaseMonster>().min(2).max(7)).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(4)
                .start(LEAP).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(MonsterBehaviourUtils.ifFurtherThan(4))
                .prepare(new SetWalkTargetWithinDist<BaseMonster>().min(2).max(7)).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(5)
                .start(THROW).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(3)
                .start(ARROW).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<BaseMonster>().min(4).max(12)).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(3)
                .start(CAST).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<BaseMonster>().min(4).max(12)).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(3)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(6, new SetWalkTargetToAttackTarget<>(), MonsterBehaviourUtils.moveTo())
                .add(3, new SetRandomWalkTarget<>(), MonsterBehaviourUtils.moveTo()).build();
    }

    @Override
    protected ExtendedBehaviour<? extends BaseMonster> getWanderBehaviour() {
        return new Idle<>();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean ret = super.hurt(source, amount);
        if (ret && !this.sleeping)
            this.setAwake();
        return ret;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide) {
            if (this.getTarget() == null) {
                this.sleepTick--;
            }
            if (this.sleepTick == 0) {
                this.entityData.set(AWAKE, false);
                this.getAnimationHandler().setAnimation(CLOSE);
                this.getNavigation().stop();
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.isTamed() && !this.isAwake()) {
            BrainUtils.setTargetOfEntity(this, player);
            this.playSound(SoundEvents.CHEST_OPEN, this.getSoundVolume() * 0.5f, 1);
            return InteractionResult.CONSUME;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.6;
        double length = this.getBbWidth() * 1.8;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(THROW)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                ItemStack held = this.getMainHandItem();
                this.setItemSlot(EquipmentSlot.MAINHAND, this.throwables.get(this.random.nextInt(this.throwables.size())));
                RuneCraftorySpells.THROW_HAND_ITEM.get().use(this);
                this.setItemSlot(EquipmentSlot.MAINHAND, held);
            }
        } else if (anim.is(CAST)) {
            this.getNavigation().stop();
            if (anim.isAt("attack"))
                RuneCraftorySpells.WATER_LASER.get().use(this);
        } else if (anim.is(ARROW)) {
            this.getNavigation().stop();
            if (anim.isAt("attack"))
                RuneCraftorySpells.DOUBLE_ARROW.get().use(this);
        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    public AnimationHandler<? extends Mimic> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected boolean isLeapingAnim(String anim) {
        return anim.equals(LEAP);
    }

    @Override
    public double leapHeightMotion() {
        return 0.3;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? RuneCraftorySpells.THROW_HAND_ITEM.get() : null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(THROW);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(LEAP);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    public void onSleeping(boolean sleeping) {
        if (sleeping) {
            this.sleeping = true;
            if (this.isAwake()) {
                this.entityData.set(AWAKE, false);
                this.getAnimationHandler().setAnimation(CLOSE);
                this.getNavigation().stop();
            }
        } else
            this.sleeping = false;
    }

    @Override
    public void setTarget(@Nullable LivingEntity livingEntity) {
        super.setTarget(livingEntity);
        if (livingEntity != null && !this.sleeping) {
            this.setAwake();
        }
    }

    public void setAwake() {
        this.entityData.set(AWAKE, true);
        this.sleepTick = 200;
    }

    public boolean isAwake() {
        return this.entityData.get(AWAKE);
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isAwake();
    }

    @Override
    protected float getJumpPower() {
        if (this.getTarget() != null)
            return 0.24f * this.getBlockJumpFactor();
        return 0.36f * this.getBlockJumpFactor();
    }

    @Override
    public void jumpFromGround() {
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, this.getJumpPower(), vec3.z);
        this.hasImpulse = true;
    }

    private int getJumpDelay() {
        if (this.getTarget() != null)
            return this.random.nextInt(5) + 4;
        return this.random.nextInt(6) + 8;
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public boolean hasSleepingAnimation() {
        return true;
    }

    protected static class JumpingMover extends MoveControl {

        private final Mimic mimic;
        private int jumpDelay;

        public JumpingMover(Mimic mimic) {
            super(mimic);
            this.mimic = mimic;
        }

        @Override
        public void tick() {
            this.mob.setYHeadRot(this.mob.getYRot());
            this.mob.setYBodyRot(this.mob.getYRot());
            if (this.operation != MoveControl.Operation.MOVE_TO) {
                this.mob.setZza(0.0f);
                return;
            }
            this.mimic.setAwake();
            this.operation = MoveControl.Operation.WAIT;
            double dX = this.wantedX - this.mob.getX();
            double dZ = this.wantedZ - this.mob.getZ();
            float n = (float) (Mth.atan2(dZ, dX) * Mth.RAD_TO_DEG) - 90.0f;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), n, 90.0f));
            if (this.mob.onGround()) {
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                if (this.jumpDelay-- <= 0) {
                    this.jumpDelay = this.mimic.getJumpDelay();
                    this.mimic.getJumpControl().jump();
                    this.mimic.playSound(SoundEvents.CHEST_OPEN, this.mimic.getSoundVolume() * 0.5f, 1);
                } else {
                    this.mimic.xxa = 0.0f;
                    this.mimic.zza = 0.0f;
                    this.mob.setSpeed(0.0f);
                }
            } else {
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            }
        }
    }
}
