package io.github.flemmli97.runecraftory.common.entities;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.EntityProperties;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.SimpleEffect;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.attachment.player.XpLevelHolder;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.FollowEntityEx;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetTargetFromRider;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.TendCrops;
import io.github.flemmli97.runecraftory.common.entities.data.MobUpdateHandler;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableDatas;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.common.entities.utils.DailyMonsterUpdater;
import io.github.flemmli97.runecraftory.common.entities.utils.ExtendedEntity;
import io.github.flemmli97.runecraftory.common.entities.utils.IExtendedMob;
import io.github.flemmli97.runecraftory.common.entities.utils.MobAttackExt;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveStateTracker;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveType;
import io.github.flemmli97.runecraftory.common.entities.utils.SleepingEntity;
import io.github.flemmli97.runecraftory.common.entities.utils.TargetableOpponent;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemObjectX;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.loot.LootCtxParameters;
import io.github.flemmli97.runecraftory.common.network.S2CAttackDebug;
import io.github.flemmli97.runecraftory.common.network.S2CEntityLevelPkt;
import io.github.flemmli97.runecraftory.common.network.S2CMobUpdate;
import io.github.flemmli97.runecraftory.common.network.S2COpenCompanionGui;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import io.github.flemmli97.runecraftory.common.quests.progress.TamingTracker;
import io.github.flemmli97.runecraftory.common.registry.ModActivities;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModMemoryTypes;
import io.github.flemmli97.runecraftory.common.spells.TeleportSpell;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import io.github.flemmli97.runecraftory.common.utils.TeleportUtils;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.BarnData;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.mixin.AttributeMapAccessor;
import io.github.flemmli97.runecraftory.mixin.CombatTrackerAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.AOEAttackEntity;
import io.github.flemmli97.tenshilib.common.entity.ai.MoveControllerPlus;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetMoveToRestriction;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class BaseMonster extends PathfinderMob implements Enemy, AnimatedEntity, IExtendedMob, ExtendedEntity, SleepingEntity, TargetableOpponent, AOEAttackEntity, MobUpdateHandler, MobAttackExt, SmartBrainOwner<BaseMonster> {

    public static final int MOVE_TICK_MAX = 4;

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Byte> MOVE_FLAGS = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> BEHAVIOUR_DATA = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> PLAY_DEATH_STATE = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> FRIEND_POINTS_SYNC = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.INT);

    public final Predicate<LivingEntity> targetPred = (e) -> {
        if (e != this) {
            if (this.getControllingPassenger() instanceof Player)
                return false;
            if (this.isTamed()) {
                return e instanceof Enemy && EntityUtils.canAttackOwned(e, false, true, this.targetPred);
            }
            if (e instanceof Player)
                return e.canBeSeenAsEnemy();
            if (e instanceof Mob mob && this == mob.getTarget())
                return true;
            return EntityUtils.canMonsterTargetNPC(e) || EntityUtils.canAttackOwned(e, false, false, entity -> entity instanceof Player ? entity.canBeSeenAsEnemy() : this.targetPred.test(entity));
        }
        return false;
    };
    public final Predicate<LivingEntity> hitPred = (e) -> {
        if (e != this) {
            if (this.hasPassenger(e) || !e.canBeSeenAsEnemy())
                return false;
            if (e instanceof Mob && this == ((Mob) e).getTarget())
                return true;
            Entity controller = this.getControllingPassenger();
            if (this.isTamed()) {
                UUID owner = EntityUtils.tryGetOwner(e);
                if (owner != null && (!this.attackOtherTamedMobs() || owner.equals(this.getOwnerUUID())))
                    return false;
                if (e == this.getTarget())
                    return true;
                if (controller instanceof Player)
                    return true;
                return e instanceof Enemy || (e instanceof Mob && ((Mob) e).getTarget() == this);
            }
            boolean riderTarget = false;
            if (controller != null) {
                if (controller instanceof BaseMonster baseMonster)
                    return baseMonster.hitPred.test(e);
                riderTarget = controller instanceof Mob mob && e == mob.getTarget();
            }
            return riderTarget || e == this.getTarget() || EntityUtils.canMonsterTargetNPC(e) || EntityUtils.canAttackOwned(e, false, false, this.targetPred) || e instanceof Player;
        }
        return false;
    };

    private final EntityProperties prop;
    private final float defaultScale;

    private final XpLevelHolder levelPair = new XpLevelHolder();
    private final XpLevelHolder friendlyPoints = new XpLevelHolder();
    private final DailyMonsterUpdater updater = new DailyMonsterUpdater(this);

    protected int tamingTick = -1;
    protected int feedTimeOut;
    private TargetPosition targetPosition;
    private BlockPos seedInventory, cropInventory;
    private int playDeathTick;

    //These 2 values are not getting saved intentionally to prevent stuff like player dying or running away
    private int brushCount, loveAttCount;
    private Runnable delayedTaming;
    private boolean doJumping = false;
    private int foodBuffTick;
    private Player owner;
    private int tpCooldown;
    private Behaviour behaviour = Behaviour.WANDER;
    private BarnData assignedBarn;
    private Pair<String, Runnable> scheduledAnimationHandling;

    private final MoveStateTracker moveStateTracker = new MoveStateTracker(MOVE_TICK_MAX, this::getMoveFlag);
    private boolean initAnim;

    public BaseMonster(EntityType<? extends BaseMonster> type, Level level) {
        super(type, level);
        this.moveControl = new MoveControllerPlus(this);
        // Client will get default value. This is intentional
        this.prop = DataPackHandler.INSTANCE.monsterPropertiesManager().getPropertiesFor(type);
        this.applyAttributes();
        this.defaultScale = (float) (type.getSpawnAABB(0, 0, 0).getYsize() / type.getHeight());
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder map = Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.23)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
        for (RegistryEntrySupplier<Attribute, ?> att : ModAttributes.ENTITY_ATTRIBUTES)
            map.add(att.asHolder());
        return map;
    }

    public EntityProperties getProp() {
        return this.prop;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SmoothGroundNavigation(this, level);
    }

    protected void applyAttributes() {
        for (Map.Entry<Holder<Attribute>, Double> att : this.prop.getBaseValues().entrySet()) {
            AttributeInstance inst = this.getAttribute(att.getKey());
            if (inst != null) {
                inst.setBaseValue(att.getValue());
                if (att.getKey() == Attributes.MAX_HEALTH)
                    this.setHealth(this.getMaxHealth());
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(OWNER_UUID, Optional.empty());
        builder.define(MOVE_FLAGS, (byte) 0);
        builder.define(BEHAVIOUR_DATA, 0);
        builder.define(PLAY_DEATH_STATE, false);
        builder.define(FRIEND_POINTS_SYNC, 1);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (this.level().isClientSide) {
            if (key.equals(BEHAVIOUR_DATA)) {
                try {
                    this.behaviour = Behaviour.values()[this.entityData.get(BEHAVIOUR_DATA)];
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
            //This case only happens during load. At that point we want to skip right to the end of the animation
            if (key.equals(PLAY_DEATH_STATE)) {
                if (this.entityData.get(PLAY_DEATH_STATE) && !this.getAnimationHandler().hasAnimation())
                    this.playDeathAnimation(true);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 10) {
            this.playTameEffect(true);
        } else if (id == 11) {
            this.playTameEffect(false);
        } else if (id == 34) {
            for (int i = 0; i < 5; ++i) {
                this.level().addParticle(ParticleTypes.ANGRY_VILLAGER, this.getX() - this.getBbWidth() * 0.25 + this.random.nextFloat() * this.getBbWidth() * 0.25f, this.getY() + this.getBbHeight() + this.random.nextFloat() * 0.3, this.getZ() - this.getBbWidth() * 0.25 + this.random.nextFloat() * this.getBbWidth() * 0.25f, 0, 0, 0);
            }
        } else if (id == 64) {
            this.level().addParticle(ParticleTypes.NOTE, true, this.getX(), this.getY() + this.getBbHeight() + 0.3, this.getZ(), 0, 0, 0);
        } else if (id == 65) {
            this.level().addParticle(ParticleTypes.HEART, true, this.getX(), this.getY() + this.getBbHeight() + 0.3, this.getZ(), 0, 0, 0);
        }
        super.handleEntityEvent(id);
    }

    @Override
    public void tick() {
        if (!this.initAnim) {
            this.getAnimationHandler().withChangeListener(anim -> {
                if (anim != null)
                    this.setupAttack(anim);
                return false;
            });
            this.initAnim = true;
        }
        super.tick();
        Vec3 lookDir = this.directionToLookAt();
        if (lookDir != null) {
            float[] yxRot = MathsHelper.YXRotFrom(lookDir);
            float[] clamp = this.targetLookClamp();
            this.setYRot(MathsHelper.rotlerp(this.getYRot(), yxRot[0], clamp[0]));
            this.setXRot(MathsHelper.rotlerp(this.getXRot(), yxRot[1], clamp[1]));
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();
        }
        this.moveStateTracker.tick();
        if (!this.level().isClientSide) {
            this.updater.tick();
            if (this.tamingTick > 0 || this.isNoAi()) {
                if (this.getMoveFlag() != MoveType.NONE) {
                    this.setMovingFlag(MoveType.NONE);
                    this.setDeltaMovement(Vec3.ZERO);
                }
                if (this.tamingTick > 0) {
                    --this.tamingTick;
                }
            }
            if (this.tamingTick == 0) {
                if (this.delayedTaming != null) {
                    this.delayedTaming.run();
                    this.delayedTaming = null;
                }
                this.tamingTick = -1;
            }
            if (this.feedTimeOut > 0) {
                --this.feedTimeOut;
            }
            this.foodBuffTick = Math.max(-1, --this.foodBuffTick);
            if (this.foodBuffTick == 0) {
                this.removeFoodEffect();
            }
            this.getAnimationHandler().runIfNotNull(this::handleAttack);
            if (this.assignedBarn != null && this.assignedBarn.isInvalidFor(this))
                this.assignedBarn = null;
            if (this.isTamed()) {
                if (this.assignedBarn == null) {
                    if (MobConfig.monsterNeedBarn && this.behaviourState() != Behaviour.STAY)
                        this.setBehaviour(Behaviour.STAY);
                }
            }
        } else {
            if (!this.playDeath() && TendCrops.cantTendToCropsAnymore(this) && this.behaviour == Behaviour.FARM && this.tickCount % 20 == 0)
                this.level().addParticle(ParticleTypes.ANGRY_VILLAGER, this.getX(), this.getY() + this.getBbHeight() + 0.3, this.getZ(), 0, 0, 0);
        }
        AnimationState animation = this.getAnimationHandler().getAnimation();
        if (animation == null) {
            this.targetPosition = null;
        }
        if (animation == null || (this.scheduledAnimationHandling != null && !this.scheduledAnimationHandling.getFirst().equals(animation.getID()))) {
            this.scheduledAnimationHandling = null;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.getAnimationHandler().tick();

        boolean teleported = false;
        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.behaviourState().following && --this.tpCooldown <= 0) {
                Player owner = this.getOwner();
                if (owner != null) {
                    serverLevel.getChunkSource().addRegionTicket(WorldUtils.ENTITY_LOADER, this.chunkPosition(), 3, this.chunkPosition());
                    if (owner.level().dimension() != this.level().dimension()) {
                        TeleportUtils.safeDimensionTeleport(this, (ServerLevel) owner.level(), owner.blockPosition());
                        teleported = true;
                        this.tpCooldown = 20;
                    } else if (owner.distanceToSqr(this) > 450) {
                        TeleportUtils.tryTeleportAround(this, owner);
                        teleported = true;
                        this.tpCooldown = 20;
                    }
                }
            }
        }
        if (this.playDeath()) {
            this.playDeathTick = Math.min(15, ++this.playDeathTick);
            if (!this.level().isClientSide) {
                if (teleported)
                    this.heal(1);
                if (this.getHealth() > 0.02)
                    this.setPlayDeath(false);
            }
        } else {
            this.playDeathTick = Math.max(0, --this.playDeathTick);
        }
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.tickBrain(this);
        if (this.tickCount % 10 == 0) {
            if (this.isStaying()) {
                BrainUtils.setMemory(this, ModMemoryTypes.STAYING.get(), Unit.INSTANCE);
            } else {
                BrainUtils.clearMemory(this, ModMemoryTypes.STAYING.get());
            }
        }
        if (!(this.getControllingPassenger() instanceof Player) && this.getMoveControl().operation != MoveControl.Operation.WAIT
                && this.getDeltaMovement().lengthSqr() > 0.0005) {
            double d0 = this.getMoveControl().getSpeedModifier();
            MoveType move;
            if (d0 > this.sprintSpeedThreshold()) {
                move = MoveType.RUN;
            } else if (d0 <= this.crouchSpeedThreshold()) {
                move = MoveType.SNEAK;
            } else {
                move = MoveType.WALK;
            }
            if (this.isImmobile())
                move = MoveType.NONE;
            this.setMovingFlag(move);
        } else {
            this.setMovingFlag(MoveType.NONE);
            this.setShiftKeyDown(false);
            this.setSprinting(false);
        }
    }

    public double crouchSpeedThreshold() {
        return 0.6;
    }

    public double sprintSpeedThreshold() {
        return 1;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("MobLevel", this.xpLevel().save());
        if (this.isTamed())
            compound.putUUID("Owner", this.getOwnerUUID());
        compound.putInt("Behaviour", this.behaviourState().ordinal());
        compound.putInt("FeedTime", this.feedTimeOut);
        if (this.hasRestriction())
            compound.putIntArray("Home", new int[]{this.getRestrictCenter().getX(), this.getRestrictCenter().getY(), this.getRestrictCenter().getZ(), (int) this.getRestrictRadius()});
        compound.putInt("FoodBuffTick", this.foodBuffTick);
        compound.put("FriendlyPoints", this.friendlyPoints.save());
        compound.put("DailyUpdater", this.updater.save());
        compound.putBoolean("PlayDeath", this.entityData.get(PLAY_DEATH_STATE));
        if (this.seedInventory != null) {
            compound.putIntArray("SeedInventory", new int[]{this.seedInventory.getX(), this.seedInventory.getY(), this.seedInventory.getZ()});
        }
        if (this.cropInventory != null) {
            compound.putIntArray("CropInventory", new int[]{this.cropInventory.getX(), this.cropInventory.getY(), this.cropInventory.getZ()});
        }
        if (this.assignedBarn != null && !this.assignedBarn.isInvalidFor(this))
            GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, this.assignedBarn.pos).resultOrPartial(RuneCraftory.LOGGER::error)
                    .ifPresent(t -> compound.put("AssignedBarnLocation", t));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.levelPair.read(compound.getCompound("MobLevel"));
        if (compound.contains("Owner"))
            this.entityData.set(OWNER_UUID, Optional.of(compound.getUUID("Owner")));
        this.feedTimeOut = compound.getInt("FeedTime");
        if (compound.contains("Home")) {
            int[] home = compound.getIntArray("Home");
            this.restrictTo(new BlockPos(home[0], home[1], home[2]), home[3]);
        }
        this.foodBuffTick = compound.getInt("FoodBuffTick");
        this.friendlyPoints.read(compound.getCompound("FriendlyPoints"));
        this.entityData.set(FRIEND_POINTS_SYNC, this.friendlyPoints.getLevel());
        this.updater.read(compound.getCompound("DailyUpdater"));
        this.setPlayDeath(compound.getBoolean("PlayDeath"));
        if (compound.contains("SeedInventory")) {
            int[] arr = compound.getIntArray("SeedInventory");
            if (arr.length == 3)
                this.setSeedInventory(new BlockPos(arr[0], arr[1], arr[2]));
        }
        if (compound.contains("CropInventory")) {
            int[] arr = compound.getIntArray("CropInventory");
            if (arr.length == 3)
                this.setCropInventory(new BlockPos(arr[0], arr[1], arr[2]));
        }
        if (compound.contains("AssignedBarnLocation")) {
            if (this.getServer() != null)
                GlobalPos.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, compound.get("AssignedBarnLocation")))
                        .resultOrPartial(RuneCraftory.LOGGER::error).ifPresent(p ->
                                this.assignedBarn = WorldHandler.get(this.getServer()).barnAt(p)
                        );
        }
        try {
            this.setBehaviour(Behaviour.values()[compound.getInt("Behaviour")], true);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    @Override
    public List<? extends ExtendedSensor<? extends BaseMonster>> getSensors() {
        return List.of(new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<BaseMonster>()
                        .setPredicate((target, entity) -> entity.targetPred.test(target))
                        .setScanRate(e -> 10),
                new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<? extends BaseMonster> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<BaseMonster>().startCondition(BaseMonster::canFloatInWater),
                new SetTargetFromRider<>(),
                new FollowEntityEx<BaseMonster, Player>()
                        .startFollowingWhen((e, f) -> e.behaviourState() == Behaviour.FOLLOW ? 8. : 12)
                        .ignoreIfTargetingTill(20)
                        .stopFollowingWithin((e, f) -> e.behaviourState() == Behaviour.FOLLOW ? 2. : 6)
                        .teleportToTargetAfter((e, f) -> e.behaviourState() == Behaviour.FOLLOW ? 20. : 24)
                        .following(BaseMonster::getOwner).speedMod(1.05f)
                        .speedMod(1.1f)
                        .startCondition(m -> m.behaviourState() == Behaviour.FOLLOW || m.behaviourState() == Behaviour.FOLLOW_DISTANCE),
                this.lookBehaviour(),
                new LookAtTarget<>().runFor(entity -> entity.getRandom().nextIntBetweenInclusive(40, 100))
                        .whenStopping(m -> BrainUtils.clearMemory(m, MemoryModuleType.LOOK_TARGET)));
    }

    protected ExtendedBehaviour<? extends BaseMonster> lookBehaviour() {
        return new AllApplicableBehaviours<BaseMonster>(
                new LookAtAttackTarget<>(),
                new OneRandomBehaviour<>(
                        new SetRandomLookTarget<>().lookChance(ConstantFloat.of(1)),
                        new SetPlayerLookTarget<>()
                ).startCondition(m -> m.getRandom().nextFloat() < 0.1 && !BrainUtils.hasMemory(m, MemoryModuleType.WALK_TARGET))
        ).startCondition(e -> !BrainUtils.hasMemory(e, MemoryModuleType.ATTACK_TARGET)
                && !e.isSleeping() && !e.playDeath());
    }

    @Override
    public BrainActivityGroup<? extends BaseMonster> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new MoveToWalkTarget<>(),
                new FirstApplicableBehaviour<>(
                        new TargetOrRetaliate<BaseMonster>(),
                        new SetMoveToRestriction<BaseMonster>(),
                        this.getWanderBehaviour().startCondition(m -> m.getRandom().nextInt(m.wanderChance()) == 0)
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public BrainActivityGroup<? extends BaseMonster> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<BaseMonster>(),
                new FirstApplicableBehaviour<>(
                        (ExtendedBehaviour<BaseMonster>) this.getCooldownAI()
                                .startCondition(e -> !e.getAnimationHandler().hasAnimation() && BrainUtils.hasMemory(this, MemoryModuleType.ATTACK_COOLING_DOWN))
                                .stopIf(e -> e.getAnimationHandler().hasAnimation() || !BrainUtils.hasMemory(this, MemoryModuleType.ATTACK_COOLING_DOWN)),
                        (ExtendedBehaviour<BaseMonster>) this.getCombatAI()
                ).startCondition(m -> m.getTarget() != null && m.isWithinRestriction(m.getTarget().blockPosition()))
        );
    }

    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return new Idle<>();
    }

    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return new Idle<>();
    }

    @Override
    public Map<Activity, BrainActivityGroup<? extends BaseMonster>> getAdditionalTasks() {
        Map<Activity, BrainActivityGroup<? extends BaseMonster>> map = new HashMap<>();
        map.put(ModActivities.STAY.get(), new BrainActivityGroup<BaseMonster>(ModActivities.STAY.get()).priority(20).behaviours(new Idle<>())
                .onlyStartWithMemoryStatus(ModMemoryTypes.STAYING.get(), MemoryStatus.VALUE_PRESENT));
        map.put(Activity.WORK, new BrainActivityGroup<BaseMonster>(Activity.WORK)
                .priority(20).behaviours(
                        new MoveToWalkTarget<>(),
                        new FirstApplicableBehaviour<>(
                                new SetMoveToRestriction<>(),
                                new TendCrops<>())
                ).onlyStartWithMemoryStatus(ModMemoryTypes.FARMING.get(), MemoryStatus.VALUE_PRESENT)
        );
        return map;
    }

    @Override
    public List<Activity> getActivityPriorities() {
        return ObjectArrayList.of(ModActivities.STAY.get(), Activity.WORK, Activity.FIGHT, Activity.IDLE);
    }

    protected ExtendedBehaviour<? extends BaseMonster> getWanderBehaviour() {
        return new SetRandomWalkTarget<>();
    }

    protected int wanderChance() {
        return this.behaviourState() == Behaviour.WANDER_HOME ? 40 : 120;
    }

    protected boolean canFloatInWater() {
        return true;
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    public boolean isMoving() {
        return this.getMoveFlag() != MoveType.NONE;
    }

    public float interpolatedMoveTick(float partialTicks) {
        return this.moveStateTracker.interpolatedMoveTick(partialTicks);
    }

    public float interpolatedMoveTickOf(MoveType moveType, float partialTicks) {
        return this.moveStateTracker.interpolatedMoveTickOf(moveType, partialTicks);
    }

    public MoveType getMoveFlag() {
        return MoveType.values()[this.entityData.get(MOVE_FLAGS)];
    }

    public void setMovingFlag(MoveType type) {
        this.entityData.set(MOVE_FLAGS, (byte) type.ordinal());
    }

    public Behaviour behaviourState() {
        return this.behaviour;
    }

    public boolean isStaying() {
        if (!this.isTamed()) {
            return false;
        }
        if (this.isInWaterOrBubble() && !this.canBreatheUnderwater()) {
            return false;
        }
        if (!this.onGround() && !this.isNoGravity()) {
            return false;
        }
        return this.behaviour == Behaviour.STAY;
    }

    public void setBehaviour(Behaviour behaviour) {
        this.setBehaviour(behaviour, false);
    }

    private void setBehaviour(Behaviour behaviour, boolean load) {
        this.entityData.set(BEHAVIOUR_DATA, behaviour.ordinal());
        this.behaviour = behaviour;
        if (!this.level().isClientSide)
            this.updateAI(false, load);
    }

    private void updateAI(boolean forced, boolean load) {
        if (forced || this.isTamed()) {
            this.getNavigation().stop();
            if (this.behaviourState() != Behaviour.FARM) {
                this.setSeedInventory(null);
                this.setCropInventory(null);
                if (this.level() instanceof ServerLevel serverLevel)
                    FarmlandHandler.get(serverLevel.getServer()).removeIrrigationPOI(serverLevel, this.getUUID());
            }
            BrainUtils.clearMemory(this, ModMemoryTypes.FARMING.get());
            switch (this.behaviourState()) {
                case WANDER_HOME -> {
                    if (this.getOwner() != null) {
                        if (this.findNearestBarn(load)) {
                            this.restrictToBasedOnBehaviour(null, load);
                            BlockPos pos = this.assignedBarn.pos.pos();
                            if (this.level().dimension() == this.assignedBarn.pos.dimension())
                                TeleportSpell.safeTeleportTo(this, pos.getX(), pos.getY(), pos.getZ());
                            else {
                                ServerLevel serverLevel = this.getServer().getLevel(this.assignedBarn.pos.dimension());
                                if (serverLevel != null)
                                    TeleportSpell.changeDimension(this, serverLevel, pos.getX(), pos.getY(), pos.getZ());
                            }
                        } else {
                            if (this.tickCount > 20)
                                this.getOwner().displayClientMessage(Component.translatable("runecraftory.monster.interact.barn.no.ext", this.getDisplayName(), this.blockPosition().toShortString()), false);
                            this.setBehaviour(Behaviour.WANDER);
                        }
                        Platform.INSTANCE.getPlayerData(this.getOwner()).party.removePartyMember(this);
                    }
                }
                case FOLLOW -> {
                    if (this.getOwner() != null) {
                        PlayerData data = Platform.INSTANCE.getPlayerData(this.getOwner());
                        boolean party = !data.party.isPartyFull() || data.party.isPartyMember(this);
                        if (party) {
                            this.clearRestriction();
                            data.party.addPartyMember(this);
                        }
                    }
                }
                case FOLLOW_DISTANCE -> {
                    this.clearRestriction();
                    if (this.getOwner() != null)
                        Platform.INSTANCE.getPlayerData(this.getOwner()).party.addPartyMember(this);
                }
                case STAY -> {
                    if (this.getOwner() != null)
                        Platform.INSTANCE.getPlayerData(this.getOwner()).party.addPartyMember(this);
                }
                case WANDER -> {
                    this.restrictToBasedOnBehaviour(this.blockPosition(), load);
                    if (this.getOwner() != null)
                        Platform.INSTANCE.getPlayerData(this.getOwner()).party.removePartyMember(this);
                }
                case FARM -> {
                    this.restrictToBasedOnBehaviour(this.blockPosition(), load);
                    BrainUtils.setMemory(this, ModMemoryTypes.FARMING.get(), Unit.INSTANCE);
                    BlockPos nearestInv = this.nearestBlockEntityWithInv();
                    if (this.getSeedInventory() == null)
                        this.setSeedInventory(nearestInv);
                    if (this.getCropInventory() == null)
                        this.setCropInventory(nearestInv);
                    if (this.getOwner() != null)
                        Platform.INSTANCE.getPlayerData(this.getOwner()).party.removePartyMember(this);
                }
            }
        }
    }

    public void restrictToBasedOnBehaviour(@Nullable BlockPos pos, boolean keepPos) {
        if (this.behaviourState() == Behaviour.WANDER_HOME && this.assignBarn()) {
            if (this.level().dimension() == this.assignedBarn.pos.dimension())
                this.restrictTo(this.assignedBarn.pos.pos(), 1);
        }
        if (pos == null)
            return;
        if (keepPos)
            pos = this.getRestrictCenter();
        if (this.behaviourState() == Behaviour.FARM) {
            this.restrictTo(pos, MobConfig.farmRadius + 3);
            if (this.level() instanceof ServerLevel serverLevel)
                FarmlandHandler.get(serverLevel.getServer()).addIrrigationPOI(serverLevel, this.getUUID(), this.blockPosition());
        } else if (this.behaviourState() == Behaviour.WANDER)
            this.restrictTo(pos, 9);
    }

    @Override
    public float getRestrictRadius() {
        if (this.behaviourState() == Behaviour.WANDER_HOME && this.assignedBarn != null && this.level().dimension() == this.assignedBarn.pos.dimension()) {
            return this.assignedBarn.getSize() + 1.5f;
        }
        return super.getRestrictRadius();
    }

    public void handleFreeTravel(Vec3 vec) {
        if (this.shouldFreezeTravel()) {
            this.xxa = 0;
            this.yya = 0;
            this.zza = 0;
            return;
        }
        if (this.getControllingPassenger() instanceof Player player) {
            this.handlePlayerInput(player, true, this::freeTravel);
        } else {
            this.freeTravel(vec);
        }
    }

    private void freeTravel(Vec3 vec) {
        boolean currentNophysics = this.noPhysics;
        if (!this.isAlive() || this.playDeath()) {
            this.noPhysics = false;
            vec = Vec3.ZERO;
        }
        this.moveRelative(this.getSpeed() * 0.2f, vec);
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.91));
        this.noPhysics = currentNophysics;
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getControllingPassenger() instanceof Player || this.playDeath() || this.tamingTick > 0;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.83F;
    }

    @Override
    public void travel(Vec3 vec) {
        if (this.shouldFreezeTravel()) {
            this.xxa = 0;
            this.yya = 0;
            this.zza = 0;
            return;
        }
        if (this.getControllingPassenger() instanceof Player player) {
            this.handlePlayerInput(player, this.isNoGravity(), this::handleLandTravel);
        } else {
            this.handleLandTravel(vec);
        }
    }

    public boolean shouldFreezeTravel() {
        return false;
    }

    protected void handlePlayerInput(Player player, boolean hovers, Consumer<Vec3> cons) {
        if (this.getAnimationHandler().hasAnimation()) {
            this.setMovingFlag(MoveType.NONE);
            this.setSprinting(false);
            cons.accept(Vec3.ZERO);
            this.calculateEntityAnimation(false);
            return;
        }
        if (!this.isControlledByLocalInstance()) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setDoJumping(false);
            this.calculateEntityAnimation(false);
            return;
        }
        if (!this.level().isClientSide) {
            if (this.adjustRotFromRider(player)) {
                this.setYRot(this.rotateClamped(this.getYRot(), player.getYRot(), this.getHeadRotSpeed() * 2));
                this.setXRot(this.rotateClamped(this.getXRot(), player.getXRot(), this.getMaxHeadXRot()));
            }
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.yBodyRot;
        }
        // For info: Vanilla speed has a constant 0.98 modifier
        double attrSpeed = !this.onGround() && this.getAttributes().hasAttribute(Attributes.FLYING_SPEED) ? this.getAttributeValue(Attributes.FLYING_SPEED) : this.getAttributeValue(Attributes.MOVEMENT_SPEED);
        float speed = (float) (attrSpeed / 1.3 * this.ridingSpeedModifier());
        float strafing = (player.xxa / 0.98f) * speed * 0.8f;
        if (player.xxa < 0)
            strafing *= -1;
        float forward = (player.zza / 0.98f) * speed;
        if (player.zza < 0)
            forward *= -0.5f;
        float vertical = 0;

        if (hovers && forward > 0) {
            vertical = (float) Math.min(0, player.getLookAngle().y + 0.45) * speed;
            if (player.getXRot() > 85)
                forward = 0;
            else if (vertical < 0)
                forward = (float) Math.sqrt(forward * forward - vertical * vertical);
        }

        if (this.doJumping()) {
            if (this.onGround() && !this.isFlyingEntity()) {
                this.hasImpulse = true;
                this.jumpFromGround();
                if (forward > 0.0f) {
                    float x = -Mth.sin(this.getYRot() * Mth.DEG_TO_RAD);
                    float z = Mth.cos(this.getYRot() * Mth.DEG_TO_RAD);
                    this.setDeltaMovement(this.getDeltaMovement().add(0.3 * x, 0, 0.3 * z));
                }
            } else if (this.isFlyingEntity()) {
                vertical = speed * 0.7f;
                if (this.getDeltaMovement().y() < vertical) {
                    this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.1, this.getDeltaMovement().z));
                }
            }
        }

        this.setSpeed(speed);
        MoveType type = forward > 0 ? MoveType.RUN : (forward != 0 || strafing != 0 ? MoveType.WALK : MoveType.NONE);
        this.setMovingFlag(type);
        this.setSprinting(type == MoveType.RUN);
        cons.accept(new Vec3(strafing, vertical, forward));

        this.setDoJumping(false);
        this.calculateEntityAnimation(false);
    }

    public void handleLandTravel(Vec3 vec) {
        if (!this.isAlive() || this.playDeath()) {
            vec = Vec3.ZERO;
        }
        super.travel(vec);
    }

    public void setDoJumping(boolean jump) {
        this.doJumping = jump;
    }

    public boolean adjustRotFromRider(LivingEntity rider) {
        return true;
    }

    private float rotateClamped(float current, float target, float maxChange) {
        float f = Mth.degreesDifference(current, target);
        float g = Mth.clamp(f, -maxChange, maxChange);
        return current + g;
    }

    public double ridingSpeedModifier() {
        return 1.3;
    }

    public boolean doJumping() {
        return this.doJumping;
    }

    protected Vec3 directionToLookAt() {
        return this.getAnimationHandler().hasAnimation() && this.targetPosition != null ? this.targetPosition
                .asVec(this.position()).subtract(this.position()) : null;
    }

    protected float[] targetLookClamp() {
        return new float[]{60, 30};
    }

    // "Disable" this as we don't use it and it will mess with the AI check
    @Override
    protected AABB getAttackBoundingBox() {
        return this.getBoundingBox().inflate(0.5);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return CombatUtils.mobAttack(this, entity, this.damageSourceAttack());
    }

    public DynamicDamage.Builder damageSourceAttack() {
        return new DynamicDamage.Builder(this).hurtResistant(5);
    }

    public int animationCooldown(@Nullable String anim) {
        int diffAdd = this.difficultyCooldown();
        if (anim == null)
            return this.getRandom().nextInt(20) + 25 + diffAdd;
        return this.getRandom().nextInt(20) + 15 + diffAdd;
    }

    public int difficultyCooldown() {
        int diffAdd = 12;
        Difficulty diff = this.level().getDifficulty();
        if (this.level().getDifficulty() == Difficulty.HARD)
            diffAdd = 0;
        else if (diff == Difficulty.NORMAL)
            diffAdd = 7;
        return diffAdd;
    }

    @Override
    public LivingEntity getTarget() {
        LivingEntity brainTarget = BrainUtils.getTargetOfEntity(this);
        return brainTarget != null ? brainTarget : super.getTarget();
    }

    public boolean attackOtherTamedMobs() {
        return false;
    }

    public void setupAttack(AnimationDefinition anim) {
        if (this.getTarget() != null) {
            this.setTargetPosition(this.getTarget());
        }
    }

    public void handleAttack(AnimationState anim) {
        if (anim.is(this.getInteractAnimation())) {
            if (this.scheduledAnimationHandling != null && anim.is(this.scheduledAnimationHandling.getFirst())
                    && anim.isAt("attack")) {
                this.scheduledAnimationHandling.getSecond().run();
            }
            return;
        }
        if (anim.is(this.getDeathAnimation(), this.getSleepAnimation()))
            return;
        this.getNavigation().stop();
        if (anim.isAt("attack")) {
            this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
        }
    }

    @Override
    public TargetPosition getTargetPosition() {
        return this.targetPosition;
    }

    public void setTargetPosition(LivingEntity target) {
        this.setTargetPosition(TargetPosition.of(target));
    }

    public void setTargetPosition(TargetPosition position) {
        this.targetPosition = position;
        if (!this.level().isClientSide)
            S2CMobUpdate.send(this, SyncableDatas.TARGET_POS, this.targetPosition);
    }

    @Nullable
    public Vec3 tryGetTargetPosition(LivingEntity target) {
        if (this.getTargetPosition() != null)
            return this.getTargetPosition()
                    .asVec(this.position());
        return target != null ? target.position() : null;
    }

    public void mobAttack(AnimationState anim, LivingEntity target, Consumer<LivingEntity> cons) {
        OrientedBoundingBox obb = this.calculateAttackAABB(anim, this.tryGetTargetPosition(target), 0);
        this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                entity -> this.hitPred.test(entity) && obb.intersects(entity.getBoundingBox())).forEach(cons);
        if (!this.level().isClientSide)
            S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
    }

    public boolean isInAttackBox(Entity entity, String animation) {
        OrientedBoundingBox aabb = this.prepareAttackBox(animation, entity, -0.15, false);
        return aabb.intersects(entity.getBoundingBox());
    }

    @Override
    public OrientedBoundingBox prepareAttackBox(String anim, Entity target, double grow, boolean debug) {
        OrientedBoundingBox obb = this.calculateAttackAABB(this.getAnimationHandler().createDefaulted(anim),
                target != null ? target.position() : null, grow);
        if (debug)
            S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTEMPT, this);
        return obb;
    }

    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, @Nullable Vec3 target, double grow) {
        float yRot = this.getYRot();
        float xRot = this.getXRot();
        if (this.getControllingPassenger() instanceof Player player) {
            yRot = player.getYRot();
            xRot = player.getXRot();
        } else if (target != null) {
            Vec3 dir = target.subtract(this.position()).normalize();
            float[] xYRot = MathsHelper.YXRotFrom(dir);
            yRot = xYRot[0];
            xRot = -xYRot[1];
        }
        double off = this.getBbHeight() * 0.5;
        return new OrientedBoundingBox(this.attackBB(anim)
                .inflate(grow, 0, grow)
                .move(0, -off, grow), yRot, Mth.clamp(xRot, -15, 15), this.position().add(0, off, 0));
    }

    public AABB attackBB(AnimationState anim) {
        double range = 1;
        return new AABB(-range * 0.5, -0.02, 0, range * 0.5, this.vehicleDependentHeight() + 0.02, range);
    }

    public final double vehicleDependentHeight() {
        double height = this.getBbHeight();
        Entity entity = this.getVehicle();
        if (entity != null) {
            height = this.getAttackBoundingBox().maxY - entity.getBoundingBox().minY;
        }
        return height;
    }

    public abstract void handleRidingCommand(int command);

    public boolean allowAnimation(@Nullable String prev, String other) {
        return true;
    }

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity livingBase) {
        if (this.isDeadOrDying() && this.deathTime > 0)
            return;
        super.setLastHurtByMob(livingBase);
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return super.canBeSeenAsEnemy() && !this.playDeath();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        if (this.isFlyingEntity() || this.getMoveControl() instanceof FlyingMoveControl) {
            return false;
        }
        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player player && player.getUUID().equals(this.getOwnerUUID())
                && !player.isShiftKeyDown()) {
            return false;
        }
        if (this.playDeath() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return false;
        return (source.getEntity() == null || this.canAttackFrom(source.getEntity().position())) && super.hurt(source, amount);
    }

    private boolean canAttackFrom(Vec3 pos) {
        if (this.isTamed())
            return true;
        boolean result = this.getRestrictRadius() == -1.0f || this.getRestrictCenter().distToCenterSqr(pos.x(), pos.y(), pos.z()) < (this.getRestrictRadius() * this.getRestrictRadius());
        if (!result) {
            this.level().playSound(null, this, SoundEvents.ANVIL_PLACE, this.getSoundSource(), 0.7f, 0.9f);
            if (this.level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 6; i++)
                    serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT, this.getRandomX(1.2), this.getRandomY(), this.getRandomZ(1.2), 0, 0, 0, 0, 0);
            }
        }
        return result;
    }

    @Override
    protected void actuallyHurt(DamageSource source, float damageAmount) {
        super.actuallyHurt(source, damageAmount);
        if (!this.isTamed() && source instanceof DynamicDamage dmg && dmg.getEntity() instanceof Player && dmg.getElement() == EnumElement.LOVE)
            this.loveAttCount = Math.min(100, this.loveAttCount + 1);
        if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && this.isTamed() && this.getHealth() <= 0) {
            this.setHealth(0.01f);
            this.setPlayDeath(true);
        }
    }

    @Override
    public boolean canBeAttackedBy(LivingEntity entity) {
        if (entity instanceof Mob m && entity.getType().is(RunecraftoryTags.EntityTypes.TAMED_MONSTER_IGNORE) && this.getTarget() != m && m.getLastHurtByMob() != this)
            return !this.isTamed();
        return true;
    }

    @Override
    public Predicate<LivingEntity> validTargetPredicate() {
        return this.hitPred;
    }

    @Override
    public void die(DamageSource cause) {
        if (!this.level().isClientSide) {
            if (this.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer)
                this.getOwner().displayClientMessage(this.getCombatTracker().getDeathMessage(), false);
            if (this.getServer() != null && this.getOwnerUUID() != null) {
                WorldHandler.get(this.getServer())
                        .removeMonsterFromPlayer(this.getOwnerUUID(), this);
                this.assignedBarn = null;
            }
            this.getAnimationHandler().setAnimation(null);
            List<CombatEntry> entries = ((CombatTrackerAccessor) this.getCombatTracker()).getEntries();
            Map<UUID, CombatRecord> merged = new HashMap<>();
            entries.forEach(e -> {
                if (e.source().getEntity() instanceof ServerPlayer player) {
                    merged.compute(player.getUUID(), (id, o) -> o == null ? new CombatRecord(player, e.source(), e.damage())
                            : new CombatRecord(player, e.source(), o.totalDamage() + e.damage())
                    );
                }
            });
            merged.values().forEach(rec -> this.onDeathDamageRecord(rec.player, rec.lastSource, rec.totalDamage));
            this.ejectPassengers();
        }
        super.die(cause);
    }

    @Override
    protected void tickDeath() {
        if (!this.level().isClientSide && this.deathTime == 0) {
            this.playDeathAnimation(false);
            this.getNavigation().stop();
        }
        ++this.deathTime;
        if (this.deathTime == (this.maxDeathTime() - 5)) {
            if (!this.level().isClientSide && this.getLastHurtByMob() != null) {
                LevelCalc.addXP(this.getLastHurtByMob(), this.baseXP(), this.baseMoney(), this.xpLevel().getLevel());
            }
        }
        if (this.deathTime >= this.maxDeathTime()) {
            if (!this.level().isClientSide)
                this.remove(RemovalReason.KILLED);
            for (int i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level().addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
            }
        }
    }

    public void onDeathDamageRecord(ServerPlayer player, DamageSource source, float damage) {
        if (damage > this.getMaxHealth() * 0.05) {
            Platform.INSTANCE.getPlayerData(player).increaseMobFrom(this);
        }
    }

    protected void playDeathAnimation(boolean load) {
        if (this.getDeathAnimation() != null) {
            this.getAnimationHandler().setAnimation(this.getDeathAnimation());
            if (load && this.level().isClientSide) {
                AnimationState anim = this.getAnimationHandler().getAnimation();
                while (!anim.done(0))
                    anim.tick();
            }
        }
    }

    public int maxDeathTime() {
        return 20;
    }

    public String getDeathAnimation() {
        return null;
    }

    public boolean playDeath() {
        return this.entityData.get(PLAY_DEATH_STATE);
    }

    public int getPlayDeathTick() {
        return this.playDeathTick;
    }

    public void setPlayDeath(boolean flag) {
        this.entityData.set(PLAY_DEATH_STATE, flag);
        if (flag) {
            if (!this.level().isClientSide && this.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer)
                this.getOwner().displayClientMessage(this.getKnockoutMessage(), false);
            this.level().getEntities(EntityTypeTest.forClass(Mob.class), this.getBoundingBox().inflate(32), e -> this.equals(e.getTarget()))
                    .forEach(m -> BrainUtils.setTargetOfEntity(m, null));
            this.getNavigation().stop();
            this.playDeathAnimation(false);
            this.setMovingFlag(MoveType.NONE);
            this.setShiftKeyDown(false);
            this.setSprinting(false);
            this.unRide();
        } else {
            this.getAnimationHandler().setAnimation(null);
        }
    }

    private Component getKnockoutMessage() {
        DamageSource source = this.getLastDamageSource();
        if (source != null && source.getEntity() != null)
            return Component.translatable("runecraftory.tamed.monster.knockout.by", this.getDisplayName(), this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ(), source.getEntity().getDisplayName());
        return Component.translatable("runecraftory.tamed.monster.knockout", this.getDisplayName(), this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ());
    }

    public boolean doStartRide(ServerPlayer player) {
        if (this.rideable()) {
            player.startRiding(this);
            return true;
        }
        player.displayClientMessage(Component.translatable("runecraftory.monster.interact.ride.no"), false);
        return false;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        this.getNavigation().stop();
        this.setDeltaMovement(Vec3.ZERO);
        this.setYya(0);
        this.setZza(0);
        this.setXxa(0);
        BrainUtils.setTargetOfEntity(this, null);
        super.addPassenger(passenger);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        if (passenger == this.getOwner())
            this.setBehaviour(Behaviour.FOLLOW);
        super.removePassenger(passenger);
    }

    @Override
    public boolean rideable() {
        return this.prop.rideable;
    }

    @Override
    public LivingEntity getControllingPassenger() {
        if (this.getFirstPassenger() instanceof Player player) {
            if (this.isTamed() && this.rideable())
                return player;
            return null;
        }
        return super.getControllingPassenger();
    }

    @Override
    public TagKey<Item> tamingItem() {
        return RunecraftoryTags.tamingTag(this.getType());
    }

    @Override
    public float tamingChance() {
        return this.prop.tamingChance;
    }

    @Override
    public boolean isTamed() {
        return this.getOwnerUUID() != null;
    }

    protected void tameEntity(Player owner) {
        if (!this.isAlive())
            return;
        this.restrictTo(this.blockPosition(), -1);
        this.setOwner(owner);
        this.navigation.stop();
        BrainUtils.setTargetOfEntity(this, null);
        this.level().broadcastEntityEvent(this, (byte) 10);
        this.updater.setLastUpdateDay(WorldUtils.day(this.level()));
        if (Platform.INSTANCE.getPlayerData(owner).party.isPartyFull())
            this.setBehaviour(Behaviour.WANDER);
        else
            this.setBehaviour(Behaviour.FOLLOW);
        this.level().getEntities(EntityTypeTest.forClass(Mob.class), this.getBoundingBox().inflate(32),
                e -> e != this && e instanceof OwnableEntity ownable && this.getOwnerUUID().equals(ownable.getOwnerUUID())
                        && e.getTarget() == this).forEach(e -> {
            BrainUtils.setTargetOfEntity(e, null);
            if (e.getLastHurtByMob() == this)
                e.setLastHurtByMob(null);
        });
        this.setLastHurtByMob(null);
        if (owner instanceof ServerPlayer serverPlayer) {
            PlayerData data = Platform.INSTANCE.getPlayerData(serverPlayer);
            data.entityStatsTracker.tameEntity(this);
            ModCriteria.TAME_MONSTER_TRIGGER.get().trigger(serverPlayer, this, data.entityStatsTracker);
            LevelCalc.levelSkill(data, EnumSkills.TAMING, 10);
            QuestHandler.getData(serverPlayer).trigger(TamingTracker.KEY, this);
        }
        if (this.getServer() != null) {
            this.assignBarn();
        }
    }

    protected float tamingMultiplier(ItemStack stack) {
        boolean flag = stack.is(this.tamingItem());
        return flag ? 2 : 1;
    }

    protected void untameEntity() {
        this.level().getEntities(EntityTypeTest.forClass(Mob.class), this.getBoundingBox().inflate(32),
                e -> e != this && e.getTarget() == this).forEach(e -> {
            BrainUtils.setTargetOfEntity(e, null);
            if (e.getLastHurtByMob() == this)
                e.setLastHurtByMob(null);
        });
        if (this.getServer() != null && this.getOwnerUUID() != null) {
            WorldHandler.get(this.getServer())
                    .removeMonsterFromPlayer(this.getOwnerUUID(), this);
            if (this.getOwner() != null) {
                Platform.INSTANCE.getPlayerData(this.getOwner()).party.removePartyMember(this);
            } else {
                WorldHandler.get(this.getServer()).toRemovePartyMember(this);
            }
            this.assignedBarn = null;
        }
        this.setOwner(null);
        this.setLastHurtByMob(null);
        if (this.playDeath())
            this.heal(this.getMaxHealth());
        this.setBehaviour(Behaviour.WANDER);
        this.updateAI(true, false);
        BrainUtils.setTargetOfEntity(this, null);
        this.getNavigation().stop();
    }

    private void playTameEffect(boolean play) {
        SimpleParticleType particle = ParticleTypes.HEART;
        int amount = 13;
        if (!play) {
            particle = ParticleTypes.SMOKE;
            amount += 9;
        }
        for (int i = 0; i < amount; ++i) {
            double d0 = this.random.nextGaussian() * 0.02;
            double d2 = this.random.nextGaussian() * 0.02;
            double d3 = this.random.nextGaussian() * 0.02;
            this.level().addParticle(particle, true, this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0f - this.getBbWidth(), this.getY() + 0.5 + this.random.nextFloat() * this.getBbHeight(), this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0f - this.getBbWidth(), d0, d2, d3);
        }
    }

    private boolean findNearestBarn(boolean load) {
        if (load)
            return this.assignBarn();
        BarnData nearest = WorldHandler.get(this.getServer()).findNearestFittingBarn(this, 5);
        if (nearest != null) {
            if (this.assignedBarn != null && this.assignedBarn != nearest) {
                this.assignedBarn.removeMonster(this);
            }
            this.assignedBarn = nearest;
            this.assignedBarn.addMonster(this, this.getProp().size);
            return true;
        } else
            return this.assignBarn();
    }

    public BarnData getAssignedBarn() {
        this.assignBarn();
        return this.assignedBarn;
    }

    private boolean assignBarn() {
        if (!this.isAlive())
            return false;
        if (this.assignedBarn == null || this.assignedBarn.isInvalidFor(this))
            this.assignedBarn = WorldHandler.get(this.getServer()).findFittingBarn(this);
        if (this.assignedBarn != null) {
            this.assignedBarn.addMonster(this, this.getProp().size);
            return true;
        }
        return false;
    }

    public BlockPos getSeedInventory() {
        if (this.seedInventory != null && !this.isWithinRestriction(this.seedInventory)) {
            return null;
        }
        return this.seedInventory;
    }

    public void setSeedInventory(BlockPos seedInventory) {
        this.seedInventory = seedInventory;
    }

    public BlockPos getCropInventory() {
        if (this.cropInventory != null && !this.isWithinRestriction(this.cropInventory)) {
            return null;
        }
        return this.cropInventory;
    }

    public void setCropInventory(BlockPos cropInventory) {
        this.cropInventory = cropInventory;
    }

    private BlockPos nearestBlockEntityWithInv() {
        BlockPos blockPos = this.getRestrictCenter();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int radius = (int) this.getRestrictRadius();
        for (int y = -1; y < 2; y++)
            for (int x = -radius; x <= radius; x++)
                for (int z = -radius; z <= radius; z++) {
                    mutableBlockPos.setWithOffset(blockPos, x, y, z);
                    if (Platform.INSTANCE.matchingInventory(this.level().getBlockEntity(mutableBlockPos), s -> true)) {
                        return mutableBlockPos.immutable();
                    }
                }
        return null;
    }

    public String getInteractAnimation() {
        return null;
    }

    public void runInteractHandling(Runnable runnable) {
        if (this.getInteractAnimation() != null) {
            this.getAnimationHandler().setAnimation(this.getInteractAnimation());
            this.scheduledAnimationHandling = Pair.of(this.getInteractAnimation(), runnable);
        } else {
            runnable.run();
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        boolean clientSide = this.level().isClientSide;
        ItemStack stack = player.getItemInHand(hand);
        if (this.isTamed()) {
            if (!player.getUUID().equals(this.getOwnerUUID())) {
                if (!clientSide)
                    player.displayClientMessage(Component.translatable("runecraftory.monster.interact.notowner"), false);
                return InteractionResult.sidedSuccess(clientSide);
            }
            if (player.isShiftKeyDown()) {
                if (stack.getItem() == Items.STICK) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        EntityUtils.playSoundForPlayer(serverPlayer, SoundEvents.VILLAGER_NO, 1, 1);
                        this.untameEntity();
                    }
                    return InteractionResult.sidedSuccess(clientSide);
                }
            }
            if (!clientSide && MobConfig.monsterNeedBarn && this.assignedBarn == null) {
                if (!this.assignBarn()) {
                    player.displayClientMessage(Component.translatable("runecraftory.monster.interact.barn.no", this.getDisplayName()), false);
                    return InteractionResult.CONSUME;
                }
            }
            if (stack.getItem() == ModItems.BRUSH.get()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    int day = WorldUtils.day(this.level());
                    if (this.updater.getLastUpdateBrush() == day)
                        return InteractionResult.PASS;
                    EntityUtils.playSoundForPlayer(serverPlayer, SoundEvents.HORSE_SADDLE, SoundSource.NEUTRAL, 0.7f, 1);
                    this.updater.setLastUpdateBrush(day);
                    this.onBrushing();
                    this.increaseFriendPoints(15);
                    this.level().broadcastEntityEvent(this, (byte) 64);
                    player.swing(hand);
                }
                return InteractionResult.sidedSuccess(clientSide);
            }
            if (hand == InteractionHand.MAIN_HAND && !this.playDeath()) {
                if (player.isShiftKeyDown()) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        EntityUtils.sendAttributesTo(this, serverPlayer);
                        LoaderNetwork.INSTANCE.sendToPlayer(new S2COpenCompanionGui(this, serverPlayer), serverPlayer);
                    }
                    return InteractionResult.sidedSuccess(clientSide);
                }
            }
            return InteractionResult.PASS;
        } else if (player.isShiftKeyDown() && !stack.isEmpty()) {
            if (stack.getItem() == ModItems.TAME.get()) {
                if (!clientSide)
                    this.tameEntity(player);
                return InteractionResult.sidedSuccess(clientSide);
            } else {
                if (stack.getItem() == ModItems.BRUSH.get() && this.isAlive()) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        if (this.tamingTick == -1)
                            return InteractionResult.PASS;
                        EntityUtils.playSoundForPlayer(serverPlayer, SoundEvents.HORSE_SADDLE, SoundSource.NEUTRAL, 0.7f, 1);
                        this.brushCount = Math.min(10, this.brushCount + 1);
                        this.tamingTick = 40;
                        this.level().broadcastEntityEvent(this, (byte) 64);
                    }
                    return InteractionResult.sidedSuccess(clientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    public void onBrushing() {
        Holder<Attribute> toIncrease = switch (this.random.nextInt(4)) {
            case 1 -> ModAttributes.DEFENCE.asHolder();
            case 2 -> ModAttributes.MAGIC_ATTACK.asHolder();
            case 3 -> ModAttributes.MAGIC_DEFENCE.asHolder();
            default -> Attributes.ATTACK_DAMAGE;
        };
        AttributeInstance inst = this.getAttribute(toIncrease);
        if (inst != null) {
            AttributeModifier mod = inst.getModifier(LibConstants.MONSTER_BRUSH_MODIFIER);
            double inc = 1;
            if (mod != null)
                inc += mod.amount();
            inst.removeModifier(LibConstants.MONSTER_BRUSH_MODIFIER);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.MONSTER_BRUSH_MODIFIER, inc, AttributeModifier.Operation.ADD_VALUE));
        }
    }

    @Override
    public XpLevelHolder xpLevel() {
        return this.levelPair;
    }

    @Override
    public void setXPLevel(int level) {
        this.levelPair.setLevel(Mth.clamp(level, 1, LibConstants.MAX_MONSTER_LEVEL), LevelCalc::xpAmountForLevelUp);
        this.updateStatsToLevel();
    }

    public void increaseLevel() {
        this.levelPair.setLevel(Mth.clamp(this.xpLevel().getLevel() + 1, 1, LibConstants.MAX_MONSTER_LEVEL), LevelCalc::xpAmountForLevelUp);
        this.updateStatsToLevel();
    }

    public void addXp(float amount) {
        XpLevelHolder pair = this.xpLevel();
        boolean res = pair.addXP(amount, LibConstants.MAX_MONSTER_LEVEL, LevelCalc::xpAmountForLevelUp, () -> {
        });
        LoaderNetwork.INSTANCE.sendToTracking(S2CEntityLevelPkt.create(this), this);
        if (res)
            this.updateStatsToLevel();
    }

    public void updateStatsToLevel() {
        if (!this.level().isClientSide)
            LoaderNetwork.INSTANCE.sendToTracking(S2CEntityLevelPkt.create(this), this);
        float preHealthDiff = this.getMaxHealth() - this.getHealth();
        this.prop.getAttributeGains().forEach((att, val) -> {
            AttributeInstance inst = this.getAttribute(att);
            if (inst != null) {
                val *= 0.01;
                inst.removeModifier(LibConstants.MONSTER_LEVEL_MODIFIER);
                float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
                if (att == Attributes.MAX_HEALTH) {
                    multiplier += LevelCalc.getMultiplierInterval(this.xpLevel().getLevel(), 20, 30, 0.15f) * 0.02f;
                } else {
                    multiplier += LevelCalc.getMultiplierInterval(this.xpLevel().getLevel(), 20, 30, 0) * 0.015f;
                }
                inst.addPermanentModifier(new AttributeModifier(LibConstants.MONSTER_LEVEL_MODIFIER, (this.xpLevel().getLevel() - 1) * val * multiplier, AttributeModifier.Operation.ADD_VALUE));
                if (att == Attributes.MAX_HEALTH)
                    this.setHealth(this.getMaxHealth() - preHealthDiff);
            }
        });
    }

    @Override
    public int friendPoints(UUID player) {
        if (player.equals(this.getOwnerUUID()))
            return this.entityData.get(FRIEND_POINTS_SYNC);
        return 0;
    }

    public void increaseFriendPoints(int xp) {
        boolean leveledUp = this.friendlyPoints.addXP(xp, 10, LevelCalc::friendPointsForNext, () -> this.entityData.set(FRIEND_POINTS_SYNC, this.friendlyPoints.getLevel()));
        if (leveledUp) {
            this.updateFriendPointAttributeBonus();
        }
    }

    private void updateFriendPointAttributeBonus() {
        List<Holder<Attribute>> increasable = List.of(Attributes.MAX_HEALTH, Attributes.ATTACK_DAMAGE,
                ModAttributes.DEFENCE.asHolder(), ModAttributes.MAGIC_ATTACK.asHolder(), ModAttributes.MAGIC_DEFENCE.asHolder());
        for (Holder<Attribute> att : increasable) {
            AttributeInstance inst = this.getAttribute(att);
            if (inst != null) {
                double inc = (this.friendlyPoints.getLevel() - 1) * 0.03;
                inst.removeModifier(LibConstants.FRIENDSHIP_MODIFIER);
                inst.addPermanentModifier(new AttributeModifier(LibConstants.FRIENDSHIP_MODIFIER, inc, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
        }
    }

    public void recalcStatsFull() {
        this.applyAttributes();
        this.updateStatsToLevel();
        this.updateFriendPointAttributeBonus();
    }

    @Override
    public int baseXP() {
        return this.prop.xp;
    }

    @Override
    public int baseMoney() {
        return this.prop.money;
    }

    @Override
    public boolean isFlyingEntity() {
        return this.prop.flying;
    }

    public void onDailyUpdate() {
        if (this.level() instanceof ServerLevel && this.isTamed() && !this.playDeath() && (!MobConfig.monsterNeedBarn || this.assignBarn())) {
            ResourceKey<LootTable> resourceLocation = this.dailyDropTable();
            this.dropAsDailyDrop(resourceLocation);
        }
    }

    public ResourceKey<LootTable> dailyDropTable() {
        ResourceKey<LootTable> def = this.getDefaultLootTable();
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(def.location().getNamespace(), def.location().getPath() + "_tamed_drops"));
    }

    protected void dropAsDailyDrop(ResourceKey<LootTable> resourceLocation) {
        LootTable lootTable = this.level().getServer().reloadableRegistries().getLootTable(resourceLocation);
        lootTable.getRandomItems(this.dailyDropContext().create(LootCtxParameters.MONSTER_INTERACTION), this::spawnAtLocation);
    }

    protected LootParams.Builder dailyDropContext() {
        LootParams.Builder builder = new LootParams.Builder((ServerLevel) this.level())
                .withParameter(LootContextParams.THIS_ENTITY, this)
                .withParameter(LootContextParams.ORIGIN, this.position());
        if (this.getOwnerUUID() != null)
            builder.withOptionalParameter(LootCtxParameters.UUID_CONTEXT, this.getOwnerUUID());
        return builder;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource source, DifficultyInstance difficulty) {
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        this.populateDefaultEquipmentSlots(this.getRandom(), difficulty);
        this.setXPLevel(Mth.clamp(this.xpLevel().getLevel(), this.prop.minLevel, LibConstants.MAX_MONSTER_LEVEL));
        return spawnData;
    }

    @Override
    public float getScale() {
        return super.getScale() * this.defaultScale;
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isTamed();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.isTamed();
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        LoaderNetwork.INSTANCE.sendToPlayer(S2CEntityLevelPkt.create(this), player);
    }

    @Override
    public void setLevelCallback(EntityInLevelCallback levelCallback) {
        super.setLevelCallback(WorldUtils.wrappedCallbackFor(this, this::getOwner, levelCallback));
    }

    @Override
    public boolean onGivingItem(Player player, ItemStack stack) {
        if (this.isTamed()) {
            if (!player.getUUID().equals(this.getOwnerUUID()))
                return false;
            if (this.hasPassenger(player))
                return false;
            if (this.feedTimeOut <= 0) {
                boolean favorite = stack.is(this.tamingItem());
                int count = stack.getCount();
                SoundEvent sound = switch (stack.getUseAnimation()) {
                    case DRINK -> stack.getDrinkingSound();
                    case EAT -> stack.getEatingSound();
                    default -> SoundEvents.NOTE_BLOCK_PLING.value();
                };
                boolean food = this.applyFoodEffect(stack);
                if (food || !this.playDeath()) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        EntityUtils.playSoundForPlayer(serverPlayer, sound, SoundSource.NEUTRAL, 0.7f, 1);
                        Platform.INSTANCE.getPlayerData(serverPlayer).getDailyUpdater().onGiveMonsterItem();
                    }
                    stack.setCount(count);
                    this.feedTimeOut = 7;
                    int day = WorldUtils.day(this.level());
                    if (this.updater.getLastUpdateFood() != day) {
                        this.updater.setLastUpdateFood(day);
                        this.increaseFriendPoints(favorite ? 50 : 30);
                        if (favorite)
                            this.level().broadcastEntityEvent(this, (byte) 65);
                        else
                            this.level().broadcastEntityEvent(this, (byte) 64);
                        DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).ifPresent(s -> s.getMonsterGiftIncrease().forEach((att, d) -> {
                            AttributeInstance inst = this.getAttribute(att);
                            if (inst != null) {
                                AttributeModifier mod = inst.getModifier(LibConstants.SHIELD_PENALTY);
                                double val = d;
                                if (mod != null) {
                                    val += mod.amount();
                                    inst.removeModifier(mod);
                                }
                                inst.addPermanentModifier(new AttributeModifier(LibConstants.SHIELD_PENALTY, val, AttributeModifier.Operation.ADD_VALUE));
                            }
                        }));
                    }

                    stack.shrink(1);
                }
                return true;
            }
        } else if (this.tamingTick == -1 && this.isAlive()) {
            SoundEvent sound = switch (stack.getUseAnimation()) {
                case DRINK -> stack.getDrinkingSound();
                case EAT -> stack.getEatingSound();
                default -> SoundEvents.NOTE_BLOCK_PLING.value();
            };
            if (player instanceof ServerPlayer serverPlayer)
                EntityUtils.playSoundForPlayer(serverPlayer, sound, SoundSource.NEUTRAL, 0.7f, 1);
            float rightItemMultiplier = this.tamingMultiplier(stack);
            int count = stack.getCount();
            this.applyFoodEffect(stack);
            if (count == stack.getCount() && !player.isCreative())
                stack.shrink(1);
            this.tamingTick = 60;
            float chance = EntityUtils.tamingChance(this, player, rightItemMultiplier, this.brushCount, this.loveAttCount);
            if (this.getServer() != null && (!MobConfig.monsterNeedBarn || WorldHandler.get(this.getServer()).findFittingBarn(this, player.getUUID()) != null))
                this.delayedTaming = () -> {
                    if (chance == 0)
                        this.level().broadcastEntityEvent(this, (byte) 34);
                    else if (this.random.nextFloat() < chance) {
                        this.tameEntity(player);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 11);
                    }
                };
            return true;
        }
        return false;
    }

    @Override
    public boolean applyFoodEffect(ItemStack stack) {
        if (this.level().isClientSide)
            return false;
        if (stack.getItem() == ModItems.OBJECT_X.get())
            ItemObjectX.applyEffect(this, stack);
        FoodProperties food = DataPackHandler.INSTANCE.foodManager().get(stack.getItem());
        if (food == null) {
            net.minecraft.world.food.FoodProperties mcFood = stack.get(DataComponents.FOOD);
            this.eat(this.level(), stack);
            if (mcFood != null) {
                this.heal(mcFood.nutrition() * 0.5f);
                return true;
            }
            return false;
        }
        this.eat(this.level(), stack);
        Pair<Map<Holder<Attribute>, Double>, Map<Holder<Attribute>, Double>> foodStats = ItemNBT.foodStats(stack);
        if (!foodStats.getFirst().isEmpty() || !foodStats.getSecond().isEmpty()) {
            this.removeFoodEffect();
            for (Map.Entry<Holder<Attribute>, Double> entry : foodStats.getSecond().entrySet()) {
                AttributeInstance inst = this.getAttribute(entry.getKey());
                if (inst == null)
                    continue;
                inst.removeModifier(LibConstants.FOOD_MODIFIER_MULTI);
                inst.addPermanentModifier(new AttributeModifier(LibConstants.FOOD_MODIFIER_MULTI, entry.getValue(), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
            for (Map.Entry<Holder<Attribute>, Double> entry : foodStats.getFirst().entrySet()) {
                AttributeInstance inst = this.getAttribute(entry.getKey());
                if (inst == null)
                    continue;
                inst.removeModifier(LibConstants.FOOD_MODIFIER);
                inst.addPermanentModifier(new AttributeModifier(LibConstants.FOOD_MODIFIER, entry.getValue(), AttributeModifier.Operation.ADD_VALUE));
            }
            this.foodBuffTick = food.duration();
        }
        EntityUtils.foodHealing(this, food.getHPGain());
        EntityUtils.foodHealing(this, this.getMaxHealth() * food.getHpPercentGain() * 0.01F);
        if (food.potionHeals() != null) {
            for (Holder<MobEffect> s : food.potionHeals()) {
                this.removeEffect(s);
            }
        }
        if (food.potionApply() != null) {
            for (SimpleEffect s : food.potionApply()) {
                this.addEffect(s.create());
            }
        }
        return true;
    }

    @Override
    public void removeFoodEffect() {
        ((AttributeMapAccessor) this.getAttributes())
                .getAttributes().values().forEach(inst -> {
                    inst.removeModifier(LibConstants.FOOD_MODIFIER);
                    inst.removeModifier(LibConstants.FOOD_MODIFIER_MULTI);
                });
    }

    @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    @Override
    public Player getOwner() {
        UUID uuid = this.getOwnerUUID();
        if (uuid != null) {
            if (this.owner == null || !this.owner.isAlive()) {
                if (this.level().isClientSide)
                    this.owner = this.level().getPlayerByUUID(uuid);
                else
                    this.owner = this.level().getServer().getPlayerList().getPlayer(uuid);
            }
        } else
            this.owner = null;
        return this.owner;
    }

    @Override
    public void setOwner(Player player) {
        if (player != null)
            this.entityData.set(OWNER_UUID, Optional.of(player.getUUID()));
        else
            this.entityData.set(OWNER_UUID, Optional.empty());
    }

    @Override
    public void onUpdate(SyncableEntityData.SyncedContainer<?> data) {
        data.runIf(SyncableDatas.TARGET_POS, pos -> this.targetPosition = pos);
    }

    @Override
    public void setSleepingPos(BlockPos pos) {
        super.setSleepingPos(pos);
        this.setSleeping(true);
    }

    @Override
    public void clearSleepingPos() {
        super.clearSleepingPos();
        this.setSleeping(false);
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.onSleeping(sleeping);
    }

    @Override
    public boolean hasSleepingAnimation() {
        return this.getSleepAnimation() != null;
    }

    public void onSleeping(boolean sleeping) {
        if (sleeping) {
            if (this.getSleepAnimation() != null) {
                this.getAnimationHandler().setAnimation(this.getSleepAnimation());
                if (this.firstTick && this.level().isClientSide) {
                    AnimationState anim = this.getAnimationHandler().getAnimation();
                    while (!anim.done(0))
                        anim.tick();
                }
            }
        } else
            this.getAnimationHandler().setAnimation(null);
    }

    public String getSleepAnimation() {
        return null;
    }

    public enum Behaviour {

        WANDER("runecraftory.monster.interact.wander", false),
        WANDER_HOME("runecraftory.monster.interact.home", false),
        FOLLOW("runecraftory.monster.interact.follow", true),
        FOLLOW_DISTANCE("runecraftory.monster.interact.follow.distance", true),
        STAY("runecraftory.monster.interact.stay", true),
        FARM("runecraftory.monster.interact.farm", false);

        public final String interactKey;

        public final boolean following;

        Behaviour(String interactKey, boolean following) {
            this.interactKey = interactKey;
            this.following = following;
        }
    }

    public record CombatRecord(ServerPlayer player, DamageSource lastSource, float totalDamage) {

    }
}
