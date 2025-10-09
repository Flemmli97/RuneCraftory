package io.github.flemmli97.runecraftory.common.entities.npc;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.calendar.Season;
import io.github.flemmli97.runecraftory.api.datapack.ConversationContext;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.SimpleEffect;
import io.github.flemmli97.runecraftory.api.datapack.npc.ConversationSet;
import io.github.flemmli97.runecraftory.api.datapack.npc.GiftData;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCLook;
import io.github.flemmli97.runecraftory.api.registry.NPCProfession;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.attachment.player.XpLevelHolder;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCDataManager;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.FollowEntityEx;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetWalkTargetFromMemory;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.AcquirePOITask;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.InvalidatePOITask;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.LookAtInteractingPlayer;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.NPCAttackActions;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.OpenDoors;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.SetRainShelterTarget;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.SetWalkAroundPoiTarget;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.SleepInBed;
import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeatureContainer;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SizeFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.profession.ShopState;
import io.github.flemmli97.runecraftory.common.entities.pathing.NPCWalkNodeEvaluator;
import io.github.flemmli97.runecraftory.common.entities.utils.IBaseMob;
import io.github.flemmli97.runecraftory.common.entities.utils.MobAttackExt;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveStateHolder;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveStateTracker;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveType;
import io.github.flemmli97.runecraftory.common.entities.utils.TargetableOpponent;
import io.github.flemmli97.runecraftory.common.inventory.InventoryShop;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerShop;
import io.github.flemmli97.runecraftory.common.items.BabySpawnEgg;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemObjectX;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.loot.LootCtxParameters;
import io.github.flemmli97.runecraftory.common.network.S2CEntityLevelPkt;
import io.github.flemmli97.runecraftory.common.network.S2CNPCLook;
import io.github.flemmli97.runecraftory.common.network.S2CNpcDialogue;
import io.github.flemmli97.runecraftory.common.network.S2COpenNPCGui;
import io.github.flemmli97.runecraftory.common.network.S2CUpdateNPCData;
import io.github.flemmli97.runecraftory.common.quests.NPCQuest;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import io.github.flemmli97.runecraftory.common.quests.progress.NPCTalkTracker;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryActivities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCProfessions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemComponentUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.TeleportUtils;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.data.Calendar;
import io.github.flemmli97.runecraftory.common.world.data.NPCHandler;
import io.github.flemmli97.runecraftory.common.world.data.RunecraftorySavedData;
import io.github.flemmli97.runecraftory.common.world.data.family.FamilyEntry;
import io.github.flemmli97.runecraftory.common.world.data.family.FamilyHandler;
import io.github.flemmli97.runecraftory.mixin.AttributeMapAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.simplequests_api.quest.QuestState;
import io.github.flemmli97.tenshilib.common.entity.ai.TargetPosition;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetMoveToRestriction;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedMobDataHandler;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.common.registry.TenshilibSyncableEntityDatas;
import io.github.flemmli97.tenshilib.common.utils.TypedResource;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
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
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.InvalidateMemory;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.AvoidEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.schedule.SmartBrainSchedule;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NPCEntity extends AgeableMob implements Npc, IBaseMob, AnimatedEntity, TargetableOpponent, MobAttackExt,
        SmartBrainOwner<NPCEntity>, SyncedMobDataHandler, MoveStateHolder {

    public static final float PATH_FIND_LENGTH = 100;

    private static final EntityDataAccessor<Boolean> PLAY_DEATH_STATE = SynchedEntityData.defineId(NPCEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MALE = SynchedEntityData.defineId(NPCEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> BEHAVIOUR_DATA = SynchedEntityData.defineId(NPCEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> MOVE_FLAGS = SynchedEntityData.defineId(NPCEntity.class, EntityDataSerializers.BYTE);

    public static final TypedResource<TargetPosition> TARGET_POSITION = new TypedResource<>(RuneCraftory.modRes("target_position"));

    public final Predicate<LivingEntity> targetPred = (e) -> {
        if (e != this) {
            if (this.followEntity() == null)
                return false;
            if (e instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null)
                return false;
            if (e instanceof Mob && this == ((Mob) e).getTarget())
                return true;
            return e instanceof Enemy;
        }
        return false;
    };
    public final Predicate<LivingEntity> hitPred = (e) -> {
        if (e != this) {
            if (e == this.getTarget() || (e instanceof Mob mob && this == mob.getTarget()))
                return true;
            if (this.hasPassenger(e) || !e.canBeSeenAsEnemy())
                return false;
            Player follow = this.followEntity();
            if (follow != null && e instanceof OwnableEntity ownable && follow.getUUID().equals(ownable.getOwnerUUID()))
                return false;
            return e instanceof Enemy;
        }
        return false;
    };
    private final AnimationHandler<NPCEntity> animationHandler = new AnimationHandler<>(this, PlayerModelAnimations.ANIMS)
            .withChangeListener(anim -> {
                if (!this.level().isClientSide) {
                    if (anim != null) {
                        if (this.getTarget() != null) {
                            this.lookAt(this.getTarget(), 360, 90);
                            this.setTargetPosition(TargetPosition.of(this.getTarget()));
                        }
                    } else {
                        this.setTargetPosition(null);
                    }
                }
                return false;
            });

    private final SyncedDataContainer<NPCEntity> syncedDataContainer = SyncedDataContainer.builder(this)
            .define(TARGET_POSITION, TenshilibSyncableEntityDatas.TARGET_POS.get(), null).build();

    private final XpLevelHolder levelPair = new XpLevelHolder();

    private NPCProfession profession = RuneCraftoryNPCProfessions.NONE.get();
    private ReloadableHolder<NPCData> data = NPCData.DEFAULT;
    private ReloadableHolder<NPCLook> look = NPCLook.DEFAULT;
    public final NPCFeatureContainer lookFeatures = new NPCFeatureContainer();
    private ReloadableHolder<NPCAttackActions> attackActions;
    private Pair<Season, Integer> birthday = Pair.of(Season.SPRING, 1);
    private Map<String, ReloadableHolder<GiftData>> gifts;
    private final Random dataRandom = new Random();

    private int foodBuffTick;
    private int playDeathTick;

    private final NPCRelationManager relationManager = new NPCRelationManager();
    private int procreationCooldown, procreationProgress;
    private Entity procreationEntity;

    private Behaviour behaviour = Behaviour.WANDER;

    private Player entityToFollow;
    private UUID entityToFollowUUID;

    private int tpCooldown;

    private final List<ServerPlayer> interactingPlayers = new ArrayList<>();
    private int interactionMoveCooldown;

    private final NPCSchedule schedule = new NPCSchedule(this, this.getRandom());

    public final DailyNPCUpdater updater = new DailyNPCUpdater(this);

    public final AttackActionHandler weaponHandler = new EntityWeaponHandler<>(this);

    private final MoveStateTracker moveStateTracker = new MoveStateTracker(this, 5, MOVE_FLAGS, this::calculateMoveType);

    public NPCEntity(EntityType<? extends NPCEntity> type, Level level) {
        super(type, level);
        this.levelPair.setLevel(LibConstants.BASE_LEVEL, LevelCalc::xpAmountForLevelUp);
        this.applyAttributes(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder map = Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 32);
        for (RegistryEntrySupplier<Attribute, ?> att : RuneCraftoryAttributes.ENTITY_ATTRIBUTES)
            map.add(att.asHolder());
        for (RegistryEntrySupplier<Attribute, ?> att : RuneCraftoryAttributes.PLAYER_ATTRIBUTES)
            map.add(att.asHolder());
        map.add(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), 1.5);
        return map;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        GroundPathNavigation nav = new SmoothGroundNavigation(this, level) {

            @Override
            @Nullable
            protected Path createPath(Set<BlockPos> targets, int regionOffset, boolean offsetUpward, int accuracy) {
                return this.createPath(targets, regionOffset, offsetUpward, accuracy, PATH_FIND_LENGTH); //Increased pathfinding range
            }

            @Override
            protected PathFinder createPathFinder(int i) {
                this.nodeEvaluator = new NPCWalkNodeEvaluator();
                this.nodeEvaluator.setCanPassDoors(true);
                return new PathFinder(this.nodeEvaluator, i);
            }
        };
        nav.setCanOpenDoors(true);
        return nav;
    }

    protected void applyAttributes(boolean regenHealth) {
        if (this.data != null && this.data.value().baseStats() != null) {
            this.data.value().baseStats().forEach((att, d) -> {
                AttributeInstance inst = this.getAttribute(att);
                if (inst != null) {
                    inst.setBaseValue(d);
                    if (regenHealth && att == Attributes.MAX_HEALTH)
                        this.setHealth(this.getMaxHealth());
                }
            });
            return;
        }
        AttributeInstance inst = this.getAttribute(Attributes.MAX_HEALTH);
        if (inst != null) {
            inst.setBaseValue(MobConfig.NPC_HEALTH);
            if (regenHealth)
                this.setHealth(this.getMaxHealth());
        }
        inst = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (inst != null) {
            inst.setBaseValue(MobConfig.NPC_ATTACK);
        }
        inst = this.getAttribute(RuneCraftoryAttributes.DEFENCE.asHolder());
        if (inst != null) {
            inst.setBaseValue(MobConfig.NPC_DEFENCE);
        }
        inst = this.getAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder());
        if (inst != null) {
            inst.setBaseValue(MobConfig.NPC_MAGIC_ATTACK);
        }
        inst = this.getAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder());
        if (inst != null) {
            inst.setBaseValue(MobConfig.NPC_MAGIC_DEFENCE);
        }
    }

    public void recalcStatsFull() {
        this.applyAttributes(true);
        this.updateStatsToLevel();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PLAY_DEATH_STATE, false);
        builder.define(MALE, false);
        builder.define(BEHAVIOUR_DATA, 0);
        builder.define(MOVE_FLAGS, (byte) 0);
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
        }
    }

    @Override
    public SyncedDataContainer<?> getDataContainer() {
        return this.syncedDataContainer;
    }

    @Override
    public List<? extends ExtendedSensor<? extends NPCEntity>> getSensors() {
        return List.of(new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<NPCEntity>()
                        .setPredicate((target, entity) -> entity.targetPred.test(target))
                        .setScanRate(e -> 10),
                new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<? extends NPCEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<NPCEntity>(),
                new InvalidatePOITask<>(MemoryModuleType.HOME, PoiTypes.HOME, NPCEntity::releaseBedPoi)
                        .isStillValid(AcquirePOITask.bedPredicate()),
                new AcquirePOITask<NPCEntity>(MemoryModuleType.HOME, PoiTypes.HOME).canAquire(AcquirePOITask.bedPredicate()
                                .and(AcquirePOITask.withinRangeOf(NPCEntity::getWorkPlace)))
                        .onAqcuire(e -> e.level().broadcastEntityEvent(this, (byte) 14)),
                new InvalidatePOITask<>(MemoryModuleType.JOB_SITE,
                        m -> this.getProfession().matches(m),
                        NPCEntity::releaseWorkplacePoi),
                new AcquirePOITask<NPCEntity>(MemoryModuleType.JOB_SITE, m -> this.getProfession().matches(m)).canAquire(AcquirePOITask.withinRangeOf(NPCEntity::getBedPos))
                        .onAqcuire(e -> e.level().broadcastEntityEvent(this, (byte) 15))
                        .startCondition(m -> m.getProfession().hasPoi()),
                new InvalidatePOITask<>(MemoryModuleType.MEETING_POINT, PoiTypes.MEETING, NPCEntity::releaseMeetingPoi),
                new AcquirePOITask<NPCEntity>(MemoryModuleType.MEETING_POINT, PoiTypes.MEETING),
                new InvalidateMemory<>(MemoryModuleType.HIDING_PLACE).invalidateIf((e, p) -> !e.level().isRaining()),
                new FollowEntityEx<NPCEntity, Player>()
                        .startFollowingWhen((e, f) -> e.behaviourState() == NPCEntity.Behaviour.FOLLOW ? 8. : 12)
                        .ignoreIfTargetingTill(20)
                        .stopFollowingWithin((e, f) -> e.behaviourState() == NPCEntity.Behaviour.FOLLOW ? 2. : 6)
                        .teleportToTargetAfter((e, f) -> e.behaviourState() == NPCEntity.Behaviour.FOLLOW ? 20. : 24)
                        .following(NPCEntity::followEntity).speedMod(1.05f)
                        .speedMod(1.1f)
                        .startCondition(m -> m.behaviourState() == NPCEntity.Behaviour.FOLLOW || m.behaviourState() == NPCEntity.Behaviour.FOLLOW_DISTANCE),
                new OpenDoors<>(),
                new AvoidEntity<NPCEntity>().avoiding(p -> p == BrainUtils.getMemory(this, MemoryModuleType.HURT_BY_ENTITY))
                        .speedModifier(1.2f).startCondition(m -> m.followEntity() == null),
                this.lookBehaviour(),
                new LookAtTarget<>().runFor(entity -> entity.getRandom().nextIntBetweenInclusive(40, 100))
                        .whenStopping(m -> BrainUtils.clearMemory(m, MemoryModuleType.LOOK_TARGET))
        );
    }

    protected ExtendedBehaviour<? extends NPCEntity> lookBehaviour() {
        return new AllApplicableBehaviours<>(
                new LookAtInteractingPlayer(),
                new LookAtAttackTarget<>(),
                new OneRandomBehaviour<>(
                        new SetRandomLookTarget<>().lookChance(ConstantFloat.of(1)),
                        new SetPlayerLookTarget<>()
                ).startCondition(m -> m.getRandom().nextFloat() < 0.1 && !BrainUtils.hasMemory(m, MemoryModuleType.WALK_TARGET))
        ).startCondition(e -> !BrainUtils.hasMemory(e, MemoryModuleType.ATTACK_TARGET)
                && !e.isSleeping() && !e.playDeath());
    }

    @Override
    public BrainActivityGroup<? extends NPCEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                MonsterBehaviourUtils.moveTo(),
                new FirstApplicableBehaviour<>(
                        new SetRainShelterTarget<>(),
                        new TargetOrRetaliate<NPCEntity>(),
                        new SetMoveToRestriction<NPCEntity>(),
                        new SetRandomWalkTarget<>().startCondition(m -> m.getRandom().nextInt(120) == 0)
                )
        );
    }

    @Override
    public BrainActivityGroup<? extends NPCEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<NPCEntity>(),
                this.getAttackActions().value().create()
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Activity, BrainActivityGroup<? extends NPCEntity>> getAdditionalTasks() {
        Map<Activity, BrainActivityGroup<? extends NPCEntity>> map = new HashMap<>();
        map.put(RuneCraftoryActivities.STAY.get(), new BrainActivityGroup<NPCEntity>(RuneCraftoryActivities.STAY.get()).priority(20)
                .onlyStartWithMemoryStatus(RuneCraftoryMemoryTypes.STAYING.get(), MemoryStatus.VALUE_PRESENT)
                .behaviours(new Idle<>()));
        map.put(Activity.REST, new BrainActivityGroup<NPCEntity>(Activity.REST).priority(15)
                .onlyStartWithMemoryStatus(MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT)
                .behaviours(new SleepInBed<>(), new SetWalkTargetFromMemory<>(MemoryModuleType.HOME, NPCEntity::releaseBedPoi), MonsterBehaviourUtils.moveTo()));
        map.put(RuneCraftoryActivities.EARLY_IDLE.get(), new BrainActivityGroup<NPCEntity>(RuneCraftoryActivities.EARLY_IDLE.get()).priority(15)
                .onlyStartWithMemoryStatus(MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT)
                .behaviours(new SetWalkTargetFromMemory<>(MemoryModuleType.HOME, NPCEntity::releaseBedPoi)
                        .closeEnough(10), MonsterBehaviourUtils.moveTo(), new SetWalkAroundPoiTarget<>(MemoryModuleType.HOME, 10).startCondition(m -> m.getRandom().nextInt(120) == 0)));
        map.put(Activity.WORK, new BrainActivityGroup<NPCEntity>(Activity.WORK).priority(15)
                .onlyStartWithMemoryStatus(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT)
                .behaviours(new SetWalkTargetFromMemory<>(MemoryModuleType.JOB_SITE, NPCEntity::releaseWorkplacePoi)
                        .closeEnough(3), MonsterBehaviourUtils.moveTo(), new SetWalkAroundPoiTarget<>(MemoryModuleType.JOB_SITE, 3).startCondition(m -> m.getRandom().nextInt(120) == 0)));
        map.put(Activity.MEET, new BrainActivityGroup<NPCEntity>(Activity.MEET).priority(15)
                .onlyStartWithMemoryStatus(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT)
                .behaviours(new SetRainShelterTarget<>(),
                        new AllApplicableBehaviours<>(new SetWalkTargetFromMemory<>(MemoryModuleType.MEETING_POINT, NPCEntity::releaseMeetingPoi).closeEnough(8),
                                new SetWalkAroundPoiTarget<>(MemoryModuleType.MEETING_POINT, 8).startCondition(m -> m.getRandom().nextInt(60) == 0)
                        ).startCondition(e -> !e.level().isRaining()),
                        MonsterBehaviourUtils.moveTo()));
        return map;
    }

    @Override
    public SmartBrainSchedule getSchedule() {
        // Returns null on init so this is why
        return NPCSchedule.forBrain(() -> this.schedule);
    }

    @Override
    public List<Activity> getActivityPriorities() {
        return ObjectArrayList.of(RuneCraftoryActivities.STAY.get(), Activity.FIGHT, Activity.IDLE);
    }

    @Override
    public Set<Activity> getScheduleIgnoringActivities() {
        return ObjectArraySet.of(Activity.FIGHT, RuneCraftoryActivities.STAY.get());
    }

    @SuppressWarnings("unchecked")
    protected void refreshBrain(ServerLevel serverLevel) {
        Brain<NPCEntity> brain = (Brain<NPCEntity>) this.getBrain();
        brain.stopAll(serverLevel, this);
        CompoundTag memories = this.saveMemories();
        NbtOps nbtOps = NbtOps.INSTANCE;
        this.brain = this.makeBrain(new Dynamic<>(nbtOps, nbtOps.createMap(ImmutableMap.of(nbtOps.createString("memories"), nbtOps.emptyMap()))));
        this.readMemories(memories);
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this, true);
    }

    @Override
    public float interpolatedMoveTick(float partialTicks) {
        return this.moveStateTracker.interpolatedMoveTick(partialTicks);
    }

    @Override
    public float interpolatedMoveTickOf(MoveType moveType, float partialTicks) {
        return this.moveStateTracker.interpolatedMoveTickOf(moveType, partialTicks);
    }

    public MoveType calculateMoveType() {
        if (this.getControllingPassenger() instanceof Player || !this.walkAnimation.isMoving()) {
            return MoveType.NONE;
        }
        if (this.isImmobile())
            return MoveType.NONE;
        double d0 = this.getMoveControl().getSpeedModifier();
        MoveType move;
        if (d0 > 1 || this.getTarget() != null) {
            move = MoveType.RUN;
        } else if (d0 <= 0.6) {
            move = MoveType.SNEAK;
        } else {
            move = MoveType.WALK;
        }
        return move;
    }

    public void setBehaviour(Behaviour behaviour) {
        this.entityData.set(BEHAVIOUR_DATA, behaviour.ordinal());
        this.behaviour = behaviour;
        if (!this.level().isClientSide)
            this.onSetBehaviour();
    }

    private void onSetBehaviour() {
        if (this.behaviourState().following) {
            if (this.followEntity() != null)
                Platform.INSTANCE.getPlayerData(this.followEntity()).party.addPartyMember(this);
        } else {
            if (this.followEntity() != null)
                Platform.INSTANCE.getPlayerData(this.followEntity()).party.removePartyMember(this);
            this.setTarget(null);
        }
        this.getNavigation().stop();
    }

    public Behaviour behaviourState() {
        return this.behaviour;
    }

    public boolean isStaying() {
        if (this.isInWaterOrBubble() || !this.onGround()) {
            return false;
        }
        return !this.interactingPlayers.isEmpty() || this.interactionMoveCooldown > 0 || this.behaviourState() == Behaviour.STAY;
    }

    public void interactWithPlayer(ServerPlayer player) {
        this.interactingPlayers.add(player);
        this.getNavigation().stop();
    }

    public void decreaseInteractingPlayers(ServerPlayer player) {
        this.interactingPlayers.remove(player);
        this.interactionMoveCooldown = 40;
    }

    public ServerPlayer getLastInteractedPlayer() {
        if (this.interactingPlayers.isEmpty())
            return null;
        return this.interactingPlayers.getLast();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("BrainMemories", this.saveMemories());
        compound.put("MobLevel", this.xpLevel().save());
        compound.putInt("FoodBuffTick", this.foodBuffTick);
        compound.putBoolean("PlayDeath", this.entityData.get(PLAY_DEATH_STATE));
        compound.put("RelationManager", this.relationManager.save());
        if (this.entityToFollowUUID != null)
            compound.putUUID("EntityToFollow", this.entityToFollowUUID);
        compound.putInt("Behaviour", this.behaviourState().ordinal());
        compound.put("NPCData", this.saveNPCData());
        compound.put("DailyUpdater", this.updater.save());
        compound.putInt("ProcreationCooldown", this.procreationCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.readMemories(compound.getCompound("BrainMemories"));
        this.levelPair.read(compound.getCompound("MobLevel"));
        this.foodBuffTick = compound.getInt("FoodBuffTick");
        this.setPlayDeath(compound.getBoolean("PlayDeath"));
        this.relationManager.load(compound.getCompound("RelationManager"));
        if (compound.hasUUID("EntityToFollow"))
            this.entityToFollowUUID = compound.getUUID("EntityToFollow");
        try {
            this.setBehaviour(Behaviour.values()[compound.getInt("Behaviour")]);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        if (compound.contains("NPCData"))
            this.loadNpcData(compound.getCompound("NPCData"));
        this.updater.read(compound.getCompound("DailyUpdater"));
        this.procreationCooldown = compound.getInt("ProcreationCooldown");
    }

    protected CompoundTag saveMemories() {
        CompoundTag tag = new CompoundTag();
        GlobalPos bed = this.getBedPos();
        if (bed != null)
            tag.put("BedPos", GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, bed).getOrThrow());
        GlobalPos workPlace = this.getWorkPlace();
        if (workPlace != null)
            tag.put("WorkPos", GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, workPlace).getOrThrow());
        GlobalPos meeting = this.getMeetingPos();
        if (meeting != null)
            tag.put("MeetingPos", GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, meeting).getOrThrow());
        return tag;
    }

    protected void readMemories(CompoundTag tag) {
        GlobalPos.CODEC.parse(NbtOps.INSTANCE, tag.get("BedPos"))
                .result().ifPresent(pos -> BrainUtils.setMemory(this, MemoryModuleType.HOME, pos));
        GlobalPos.CODEC.parse(NbtOps.INSTANCE, tag.get("WorkPos"))
                .result().ifPresent(pos -> BrainUtils.setMemory(this, MemoryModuleType.JOB_SITE, pos));
        GlobalPos.CODEC.parse(NbtOps.INSTANCE, tag.get("MeetingPos"))
                .result().ifPresent(pos -> BrainUtils.setMemory(this, MemoryModuleType.MEETING_POINT, pos));
    }

    private CompoundTag saveNPCData() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Data", this.data.id().toString());
        tag.putString("Look", this.getLook().id().toString());
        tag.put("Profession", RuneCraftoryNPCProfessions.PROFESSIONS.registry().byNameCodec().encodeStart(NbtOps.INSTANCE, this.getProfession()).getOrThrow());
        tag.putBoolean("Male", this.isMale());
        tag.putInt("BirthdayMonth", this.getBirthday().getFirst().ordinal());
        tag.putInt("Birthday", this.getBirthday().getSecond());
        tag.putString("Combat", this.getAttackActions().id().toString());
        tag.put("Schedule", this.schedule.save());
        tag.put("LookFeatures", this.lookFeatures.save(this.registryAccess()));
        CompoundTag gifts = new CompoundTag();
        this.gifts.forEach((s, g) -> gifts.putString(s, g.id().toString()));
        tag.put("GiftData", gifts);
        return tag;
    }

    private void loadNpcData(CompoundTag tag) {
        ReloadableHolder<NPCData> data = DataPackHandler.INSTANCE.npcDataManager().get(ResourceLocation.parse(tag.getString("Data")));
        this.look = DataPackHandler.INSTANCE.npcLookManager().get(ResourceLocation.parse(tag.getString("Look")));
        this.setProfession(RuneCraftoryNPCProfessions.PROFESSIONS.registry().byNameCodec().parse(NbtOps.INSTANCE, tag.get("Profession"))
                .getOrThrow());
        this.setMale(tag.getBoolean("Male"));
        try {
            Season month = Season.values()[tag.getInt("BirthdayMonth")];
            this.birthday = Pair.of(month, tag.getInt("Birthday"));
        } catch (IllegalArgumentException e) {
            this.getBirthday();
        }
        this.attackActions = DataPackHandler.INSTANCE.npcActionsManager().get(ResourceLocation.parse(tag.getString("Combat")));
        this.schedule.load(tag.getCompound("Schedule"));
        try {
            this.lookFeatures.read(tag.get("LookFeatures"), this.registryAccess());
        } catch (Exception e) {
            this.lookFeatures.buildFromLooks(this, this.look.value());
        }
        CompoundTag gifts = tag.getCompound("GiftData");
        ImmutableMap.Builder<String, ReloadableHolder<GiftData>> b = ImmutableMap.builder();
        gifts.getAllKeys().forEach(key -> {
            ReloadableHolder<GiftData> giftData = DataPackHandler.INSTANCE.giftManager().get(ResourceLocation.parse(gifts.getString(key)));
            if (giftData != null)
                b.put(key, giftData);
        });
        this.gifts = b.build();
        this.setNPCData(data, true);
    }

    @Override
    public void tick() {
        if (this.firstTick) {
            if (this.getServer() != null) {
                NPCHandler handler = RunecraftorySavedData.get(this.getServer()).npcHandler;
                handler.addNPC(this);
                handler.playersToReset(this.getUUID()).forEach(pair -> this.relationManager.resetQuest(pair.getFirst(), pair.getSecond()));
            }
        }
        super.tick();
    }

    @Override
    public void aiStep() {
        this.updateSwingTime();
        super.aiStep();
        this.getAnimationHandler().tick();
        this.weaponHandler.tick();
        boolean teleported = false;
        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.behaviourState().following && --this.tpCooldown <= 0) {
                Player follow = this.followEntity();
                if (follow != null) {
                    serverLevel.getChunkSource().addRegionTicket(WorldUtils.ENTITY_LOADER, this.chunkPosition(), 3, this.chunkPosition());
                    if (!follow.level().dimension().equals(this.level().dimension())) {
                        TeleportUtils.safeDimensionTeleport(this, (ServerLevel) follow.level(), follow.blockPosition());
                        teleported = true;
                        this.tpCooldown = 20;
                    } else if (follow.distanceToSqr(this) > 450) {
                        TeleportUtils.tryTeleportAround(this, follow);
                        teleported = true;
                        this.tpCooldown = 20;
                    }
                }
            }
            --this.procreationCooldown;
            if (this.procreationProgress > 0) {
                this.getNavigation().stop();
                this.procreationProgress--;
                if (this.tickCount % 10 == 0)
                    serverLevel.sendParticles(ParticleTypes.HEART, this.getX(), this.getY() + this.getBbHeight(), this.getZ(),
                            0, 0, 0, 0, 0);
                if (this.procreationProgress == 0) {
                    if (this.spawnBaby()) {
                        this.procreationCooldown = MobConfig.procreationCooldown;
                    }
                    this.procreationEntity = null;
                }
            }
            if (this.tickCount % 10 == 0) {
                this.interactingPlayers.removeIf(p -> p.distanceToSqr(this) > 100);
            }
            --this.interactionMoveCooldown;
            this.updater.tick();
            this.foodBuffTick = Math.max(-1, --this.foodBuffTick);
            if (this.foodBuffTick == 0) {
                this.removeFoodEffect();
            }
            this.getAnimationHandler().runIfNotNull(this::handleAttack);
        }
        this.moveStateTracker.tick();
        if (this.playDeath()) {
            this.playDeathTick = Math.min(15, ++this.playDeathTick);
            if (!this.level().isClientSide) {
                if (teleported) {
                    this.heal(1);
                }
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
        if (this.lookFeatures.updateLooks(this, this.getLook().value())) {
            LoaderNetwork.INSTANCE.sendToTracking(new S2CNPCLook(this.getId(), this.look, this.lookFeatures), this);
            for (int i = 0; i < 20; ++i) {
                AdvancedParticleContainer.make(ParticleTypes.POOF)
                        .addData(new ScaleData(0.15f))
                        .addData(new MotionData(this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01, this.random.nextGaussian() * 0.01))
                        .add(this.level(), this.getRandomX(0.9), this.getRandomY(), this.getRandomZ(0.9));
            }
            this.playSound(RuneCraftorySounds.ENTITY_NPC_CHANGE.get());
        }
        if (this.tickCount % 10 == 0) {
            if (this.isStaying()) {
                BrainUtils.setMemory(this, RuneCraftoryMemoryTypes.STAYING.get(), Unit.INSTANCE);
            } else {
                BrainUtils.clearMemory(this, RuneCraftoryMemoryTypes.STAYING.get());
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 14) {
            this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
        } else if (id == 15) {
            this.addParticlesAroundSelf(ParticleTypes.END_ROD);
        } else
            super.handleEntityEvent(id);
    }

    protected void addParticlesAroundSelf(ParticleOptions particleData) {
        for (int i = 0; i < 5; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.level().addParticle(particleData, this.getRandomX(1.0), this.getRandomY() + 1.0, this.getRandomZ(1.0), d, e, f);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!(player instanceof ServerPlayer serverPlayer) || player.getItemInHand(hand).is(RuneCraftoryItems.DEBUG.get()))
            return InteractionResult.PASS;
        if (this.isSleeping())
            return InteractionResult.CONSUME;
        if (this.getEntityToFollowUUID() != null && this.getEntityToFollowUUID().equals(serverPlayer.getUUID())) {
            EntityUtils.sendAttributesTo(this, serverPlayer);
        }
        LoaderNetwork.INSTANCE.sendToPlayer(new S2CUpdateNPCData(this, this.relationManager.getFriendPointData(player.getUUID()).save()), serverPlayer);
        LoaderNetwork.INSTANCE.sendToPlayer(new S2COpenNPCGui(this, serverPlayer), serverPlayer);
        this.interactWithPlayer(serverPlayer);
        this.lookAt(serverPlayer, 30, 30);
        return InteractionResult.CONSUME;
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        if (this.getServer() != null) {
            this.getFamily().updateName(this);
            RunecraftorySavedData.get(this.getServer())
                    .npcHandler.addNPC(this);
        }
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return SpawnEgg.fromType(this.getType()).map(ItemStack::new).orElse(ItemStack.EMPTY);
    }

    @Override
    public boolean onGivingItem(Player player, ItemStack stack) {
        this.giftItem(player, stack);
        return true;
    }

    public void giftItem(Player player, ItemStack stack) {
        this.lookAt(player, 30, 30);
        int count = stack.getCount();
        SoundEvent sound = switch (stack.getUseAnimation()) {
            case DRINK -> stack.getDrinkingSound();
            case EAT -> stack.getEatingSound();
            default -> SoundEvents.NOTE_BLOCK_PLING.value();
        };
        if (stack.getItem() == RuneCraftoryItems.DIVORCE_PAPER.get()) {
            if (player instanceof ServerPlayer serverPlayer) {
                FamilyEntry family = this.getFamily();
                if (player.getUUID().equals(family.getPartner())) {
                    this.speak(serverPlayer, ConversationContext.DIVORCE);
                    family.updateRelationship(FamilyEntry.Relationship.NONE, null);
                    this.relationManager.getFriendPointData(player.getUUID())
                            .points.addXP(-2000, 10, LevelCalc::friendPointsForNext, () -> {
                            });
                } else {
                    this.speak(serverPlayer, ConversationContext.DIVORCE_ERROR);
                }
                stack.shrink(1);
            }
            return;
        }
        if (stack.getItem() == RuneCraftoryItems.LOVE_LETTER.get()) {
            if (player instanceof ServerPlayer serverPlayer) {
                FamilyHandler families = FamilyHandler.get(this.getServer());
                FamilyEntry playerEntry = families.getOrCreateEntry(serverPlayer);
                FamilyEntry family = families.getOrCreateEntry(this);
                boolean success = false;
                if (playerEntry.getRelationship() != FamilyEntry.Relationship.NONE
                        || family.getRelationship() != FamilyEntry.Relationship.NONE) {
                    this.speak(serverPlayer, ConversationContext.DATING_DENY);
                } else {
                    float chance = this.friendPoints(player) >= 7 ? 0.33f * (this.friendPoints(player) - 6) : 0;
                    if (chance > 0 && this.updater.getDailyRandom().nextFloat() < chance) {
                        this.speak(serverPlayer, ConversationContext.DATING_ACCEPT);
                        family.updateRelationship(FamilyEntry.Relationship.DATING, player.getUUID());
                        success = true;
                    } else
                        this.speak(serverPlayer, ConversationContext.DATING_DENY);
                }
                if (!success) {
                    player.addItem(stack);
                } else
                    stack.shrink(1);
            }
            return;
        }
        if (stack.getItem() == RuneCraftoryItems.ENGAGEMENT_RING.get()) {
            if (player instanceof ServerPlayer serverPlayer) {
                FamilyHandler families = FamilyHandler.get(this.getServer());
                FamilyEntry playerEntry = families.getOrCreateEntry(serverPlayer);
                FamilyEntry family = families.getOrCreateEntry(this);
                boolean success = false;
                if ((playerEntry.getRelationship() != FamilyEntry.Relationship.NONE && !this.getUUID().equals(playerEntry.getPartner()))
                        || family.getPartner() == null
                        || !family.getPartner().equals(player.getUUID())) {
                    this.speak(serverPlayer, ConversationContext.MARRIAGE_DENY);
                } else {
                    if (family.getRelationship() == FamilyEntry.Relationship.DATING) {
                        float chance = this.friendPoints(player) >= 10 ? 0.2f * (this.friendPoints(player) - 9) : 0;
                        if (chance > 0 && this.relationManager.getCompletedQuests(player.getUUID()).containsAll(this.data.value().questHandler().requiredQuests()) &&
                                this.updater.getDailyRandom().nextFloat() < chance) {
                            this.speak(serverPlayer, ConversationContext.MARRIAGE_ACCEPT);
                            family.updateRelationship(FamilyEntry.Relationship.MARRIED, player.getUUID());
                            success = true;
                            this.procreationCooldown = MobConfig.initialProcreationCooldown;
                        } else
                            this.speak(serverPlayer, ConversationContext.MARRIAGE_DENY);
                    }
                }
                if (!success) {
                    player.addItem(stack);
                } else
                    stack.shrink(1);
            }
            return;
        }
        float mult = 1;
        if (player instanceof ServerPlayer serverPlayer) {
            Calendar calendar = Calendar.get(serverPlayer.level());
            if (calendar.currentSeason() == this.birthday.getFirst() && calendar.date().date() == this.birthday.getSecond())
                mult = 3;
            EntityUtils.playSoundForPlayer(serverPlayer, sound, SoundSource.NEUTRAL, 0.7f, 1);
        }
        Equipable equipable = Equipable.get(stack);
        EquipmentSlot slot = equipable != null ? equipable.getEquipmentSlot() : null;
        if (slot != EquipmentSlot.MAINHAND || ItemComponentUtils.isWeapon(stack) || stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem) {
            ItemStack copy = stack.copy();
            copy.setCount(1);
            this.setItemSlot(slot == null ? EquipmentSlot.MAINHAND : slot, copy);
        }
        this.applyFoodEffect(stack);
        stack.setCount(count);
        if (player instanceof ServerPlayer serverPlayer) {
            NPCData.Gift gift = this.giftOf(stack);
            if (gift != null) {
                if (this.relationManager.getFriendPointData(player.getUUID()).giftXP(this.level(), (int) (gift.xp() * mult)))
                    this.tellDialogue(serverPlayer, null, null, Component.translatable(gift.responseKey()), List.of());
            } else {
                if (this.relationManager.getFriendPointData(player.getUUID()).giftXP(this.level(), (int) (5 * mult)))
                    this.tellDialogue(serverPlayer, null, null, Component.translatable(this.data.value().neutralGiftResponse()), List.of());
            }
        }
        stack.shrink(1);
    }

    public void talkTo(ServerPlayer player) {
        ConversationContext ctx = ConversationContext.TALK;
        NPCFriendPoints fp = this.relationManager.getFriendPointData(player.getUUID());
        boolean talkedTo = fp.talked();
        boolean doGreet = fp.talkTo(this.level(), 15);
        if (!talkedTo) {
            ctx = ConversationContext.FIRST_TALK;
        } else if (doGreet) {
            ctx = ConversationContext.GREETING;
        }
        this.speak(player, ctx);
    }

    public void speak(ServerPlayer player, ConversationContext convCtx) {
        int heart = this.relationManager.getFriendPointData(player.getUUID()).points.getLevel();
        ConversationSet conversations = this.data.value().getConversation(convCtx);
        LootParams ctx = new LootParams.Builder((ServerLevel) this.level())
                .withParameter(LootContextParams.THIS_ENTITY, this)
                .withParameter(LootContextParams.ORIGIN, this.position())
                .withParameter(LootCtxParameters.UUID_CONTEXT, player.getUUID())
                .withLuck(player.getLuck()).create(LootCtxParameters.NPC_INTERACTION);
        LootContext lootContext = new LootContext.Builder(ctx).create(Optional.empty());
        List<Map.Entry<String, ConversationSet.Conversation>> filtered = conversations.conversations().entrySet().stream()
                .filter(c -> {
                    //Disable if player already has a quest from this npc
                    if (c.getValue().actions().stream().anyMatch(h -> h.action() == ConversationSet.ConversationAction.QUEST) &&
                            QuestHandler.questForExists(player, this) != null &&
                            !this.updater.alreadyAcceptedRandomquest(player))
                        return false;
                    return c.getValue().startingConversation() && c.getValue().test(heart, lootContext);
                })
                .collect(Collectors.toList());
        Collections.shuffle(filtered, this.updater.getDailyRandom());
        int size = Math.min(filtered.size(), 2 + this.updater.getDailyRandom().nextInt(2)); //Select 2-3 random lines
        if (size > 0) {
            Map.Entry<String, ConversationSet.Conversation> randomLine = filtered.get(this.random.nextInt(size));
            this.tellDialogue(player, convCtx, randomLine.getKey(), randomLine.getValue());
        } else {
            Component dialog = conversations.missing() != null
                    ? Component.translatable(conversations.fallbackKey(), conversations.missing()) : Component.translatable(conversations.fallbackKey());
            this.tellDialogue(player, convCtx, null, dialog, List.of());
        }
    }

    public void respondToQuest(ServerPlayer player, ResourceLocation quest) {
        QuestHandler.getData(player).trigger(NPCTalkTracker.KEY, this);
        QuestState result = QuestHandler.checkCompletionQuest(player, this);
        int questState = this.relationManager.questStateFor(player.getUUID(), quest);
        QuestConversationContext questCtx = QuestConversationContext.NOT_STARTED;
        if (questState != NPCRelationManager.QUEST_NOT_STARTED) {
            questCtx = QuestConversationContext.IN_PROGRESS;
            if (result == QuestState.COMPLETE) {
                this.relationManager.endQuest(player.getUUID(), quest);
                questState = NPCRelationManager.QUEST_COMPLETED;
                questCtx = QuestConversationContext.COMPLETED;
            } else if (result == QuestState.PARTIAL_COMPLETE) {
                questState = this.relationManager.advanceQuest(player.getUUID(), quest);
                questCtx = QuestConversationContext.NOT_STARTED;
            }
        } else
            this.relationManager.advanceQuest(player.getUUID(), quest);
        int heart = this.relationManager.getFriendPointData(player.getUUID()).points.getLevel();
        ConversationSet conversations = this.data.value().getFromQuest(quest, questCtx, questState);
        LootParams ctx = new LootParams.Builder((ServerLevel) this.level())
                .withParameter(LootContextParams.THIS_ENTITY, this)
                .withParameter(LootContextParams.ORIGIN, this.position())
                .withParameter(LootCtxParameters.UUID_CONTEXT, player.getUUID())
                .withLuck(player.getLuck()).create(LootCtxParameters.NPC_INTERACTION);
        LootContext lootContext = new LootContext.Builder(ctx).create(Optional.empty());
        List<Map.Entry<String, ConversationSet.Conversation>> filtered = conversations.conversations().entrySet().stream().filter(c -> c.getValue().startingConversation() && c.getValue().test(heart, lootContext))
                .collect(Collectors.toList());
        Collections.shuffle(filtered, this.updater.getDailyRandom());
        int size = Math.min(filtered.size(), 2 + this.updater.getDailyRandom().nextInt(2)); //Select 2-3 random lines
        if (size > 0) {
            Map.Entry<String, ConversationSet.Conversation> randomLine = filtered.get(this.random.nextInt(size));
            this.tellDialogue(player, null, randomLine.getKey(), randomLine.getValue());
        } else {
            Component dialog = conversations.missing() != null
                    ? Component.translatable(conversations.fallbackKey(), conversations.missing()) : Component.translatable(conversations.fallbackKey());
            this.tellDialogue(player, null, null, dialog, List.of());
        }
    }

    private void tellDialogue(ServerPlayer player, ConversationContext convCtx, String conversationID, ConversationSet.Conversation conversation) {
        List<Component> actions = conversation.actions().stream().map(e -> (Component) Component.translatable(e.translationKey())).toList();
        this.tellDialogue(player, convCtx, conversationID, Component.translatable(conversation.translationKey()), actions);
    }

    private void tellDialogue(ServerPlayer player, ConversationContext convCtx, String conversationID, Component component, List<Component> actions) {
        this.interactWithPlayer(player);
        LoaderNetwork.INSTANCE.sendToPlayer(new S2CNpcDialogue(this.getId(), convCtx, conversationID, component, this.conversationData(player), actions), player);
    }

    private Map<String, Component> conversationData(ServerPlayer player) {
        Map<String, Component> map = new HashMap<>();
        map.put(PlaceHolderComponent.NPC, this.getDisplayName());
        map.put(PlaceHolderComponent.PLAYER, player.getDisplayName());
        this.randomGiftContext(15, Integer.MAX_VALUE).ifPresent(comp -> map.put(PlaceHolderComponent.FAVORITE, comp));
        this.randomGiftContext(7, 14).ifPresent(comp -> map.put(PlaceHolderComponent.LIKE, comp));
        this.randomGiftContext(-14, -1).ifPresent(comp -> map.put(PlaceHolderComponent.DISLIKE, comp));
        this.randomGiftContext(Integer.MIN_VALUE, -15).ifPresent(comp -> map.put(PlaceHolderComponent.HATE, comp));
        return map;
    }

    public void handleDialogueAction(ServerPlayer sender, ConversationContext convCtx, String conversationID, int actionIdx) {
        ConversationSet conversations = this.data.value().getConversation(convCtx);
        ConversationSet.Conversation conversation = conversations.conversations().get(conversationID);
        if (conversation != null && actionIdx < conversation.actions().size()) {
            ConversationSet.ConversationActionHolder action = conversation.actions().get(actionIdx);
            if (action != null) {
                switch (action.action()) {
                    case ANSWER -> {
                        ConversationSet.Conversation answer = conversations.conversations().get(action.actionValue());
                        if (answer != null) {
                            this.relationManager.getFriendPointData(sender.getUUID())
                                    .answer(conversationID, action.friendXP());
                            this.tellDialogue(sender, convCtx, action.actionValue(), answer);
                        }
                    }
                    case QUEST ->
                            QuestHandler.acceptQuestRandom(sender, this, ResourceLocation.parse(action.actionValue()));
                }
            }
        }
    }

    public void closedDialogue(ServerPlayer sender) {
        this.decreaseInteractingPlayers(sender);
    }

    public void closedQuestDialogue(ServerPlayer sender) {
        this.closedDialogue(sender);
        ResourceLocation quest = QuestHandler.questForExists(sender, this);
        if (quest != null && this.relationManager.questStateFor(sender.getUUID(), quest) == -2)
            QuestHandler.getData(sender).submit(this);
    }

    public void resetQuestProcess(ServerPlayer player, ResourceLocation quest) {
        this.relationManager.resetQuest(player.getUUID(), quest);
    }

    public void completeNPCQuest(ServerPlayer player, NPCQuest quest) {
        this.relationManager.completeQuest(player.getUUID(), quest.getOriginID());
    }

    public boolean canAcceptNPCQuest(ServerPlayer player, NPCQuest quest) {
        return this.relationManager.getCompletedQuests(player.getUUID()).containsAll(quest.neededParentQuests);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.playDeath() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        if (this.followEntity() != null && source.getEntity() != null) {
            Player follow = this.followEntity();
            if (follow.equals(source.getEntity()) || Platform.INSTANCE.getPlayerData(follow).party.isPartyMember(source.getEntity()))
                return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(DamageSource source, float damageAmount) {
        super.actuallyHurt(source, damageAmount);
        if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            FamilyEntry family;
            if (this.getHealth() <= 0 && (this.followEntity() != null
                    || ((family = this.getFamily()) != null && family.getPartner() != null && FamilyHandler.get(this.getServer())
                    .getFamily(family.getPartner()).map(FamilyEntry::isPlayer).orElse(false)))) {
                this.setHealth(0.01f);
                this.setPlayDeath(true);
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return attack(this, entity);
    }

    public static boolean attack(LivingEntity attacker, Entity target) {
        ItemStack stack = attacker.getMainHandItem();
        DynamicDamage.Builder source = new DynamicDamage.Builder(attacker).hurtResistant(0).element(ItemComponentUtils.getElement(stack));
        return CombatUtils.mobAttack(attacker, target, source);
    }

    @Override
    protected AABB getAttackBoundingBox() {
        double reach = this.getAttributeValue(RuneCraftoryAttributes.ATTACK_RANGE.asHolder()) - (Math.sqrt(2.04) - 0.6);
        return super.getAttackBoundingBox().inflate(reach);
    }

    public void handleAttack(AnimationState anim) {
        this.getNavigation().stop();
    }

    @Override
    public AnimationHandler<?> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public Predicate<LivingEntity> validTargetPredicate() {
        return this.hitPred;
    }

    public void setTargetPosition(TargetPosition target) {
        this.syncedDataContainer.set(TARGET_POSITION, target);
    }

    @Override
    public TargetPosition getTargetPosition() {
        return this.syncedDataContainer.get(TARGET_POSITION);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        LoaderNetwork.INSTANCE.sendToPlayer(new S2CNPCLook(this.getId(), this.look, this.lookFeatures), player);
        LoaderNetwork.INSTANCE.sendToPlayer(S2CEntityLevelPkt.create(this), player);
    }

    @Override
    public void setLevelCallback(EntityInLevelCallback levelCallback) {
        super.setLevelCallback(WorldUtils.wrappedCallbackFor(this, this::followEntity, levelCallback));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        this.randomizeData(null, true);
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public void die(DamageSource cause) {
        RuneCraftory.LOGGER.info("NPC {} died, message: '{}'", this, cause.getLocalizedDeathMessage(this).getString());
        if (!this.level().isClientSide) {
            this.getAnimationHandler().setAnimation(null);
        }
        super.die(cause);
    }

    @Override
    public void remove(RemovalReason reason) {
        this.releaseBedPoi();
        this.releaseWorkplacePoi();
        this.releaseMeetingPoi();
        super.remove(reason);
        if (this.getServer() != null) {
            NPCHandler handler = RunecraftorySavedData.get(this.getServer()).npcHandler;
            if (reason.shouldDestroy() && this.data != null && this.data.value().unique() > 0)
                handler.removeUniqueNPC(this.getUUID(), this.data);
            handler.removeNPC(this, reason);
        }
    }

    public boolean playDeath() {
        return this.entityData.get(PLAY_DEATH_STATE);
    }

    public void setPlayDeath(boolean flag) {
        this.entityData.set(PLAY_DEATH_STATE, flag);
        if (flag) {
            Player partner;
            if (!this.level().isClientSide && this.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)
                    && this.followEntity() == null && (partner = this.getPartner()) instanceof ServerPlayer)
                partner.displayClientMessage(this.getKnockoutMessage(), false);
            this.level().getEntities(EntityTypeTest.forClass(Mob.class), this.getBoundingBox().inflate(32), e -> this.equals(e.getTarget()))
                    .forEach(m -> m.setTarget(null));
            this.getNavigation().stop();
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

    public int getPlayDeathTick() {
        return this.playDeathTick;
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return super.canBeSeenAsEnemy() && !this.playDeath();
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.isVehicle() || this.playDeath();
    }

    @Override
    public XpLevelHolder xpLevel() {
        return this.levelPair;
    }

    @Override
    public void setXPLevel(int level) {
        this.xpLevel().setLevel(Mth.clamp(level, 1, LibConstants.MAX_MONSTER_LEVEL), LevelCalc::xpAmountForLevelUp);
        this.updateStatsToLevel();
    }

    public void increaseLevel() {
        this.xpLevel().setLevel(Mth.clamp(this.xpLevel().getLevel() + 1, 1, LibConstants.MAX_MONSTER_LEVEL), LevelCalc::xpAmountForLevelUp);
        this.updateStatsToLevel();
    }

    public void addXp(float amount) {
        boolean res = this.xpLevel().addXP(amount, LibConstants.MAX_MONSTER_LEVEL, LevelCalc::xpAmountForLevelUp, () -> {
        });
        LoaderNetwork.INSTANCE.sendToTracking(S2CEntityLevelPkt.create(this), this);
        if (res)
            this.updateStatsToLevel();
    }

    public void updateStatsToLevel() {
        if (!this.level().isClientSide)
            LoaderNetwork.INSTANCE.sendToTracking(S2CEntityLevelPkt.create(this), this);
        float preHealthDiff = this.getMaxHealth() - this.getHealth();
        ((AttributeMapAccessor) this.getAttributes()).getAttributes()
                .forEach((att, inst) -> inst.removeModifier(LibConstants.MONSTER_LEVEL_MODIFIER));
        if (this.data != null) {
            Map<Holder<Attribute>, Double> gain = this.data.value().statIncrease() != null ? this.data.value().statIncrease() : NPCData.DEFAULT_GAIN;
            gain.forEach((att, val) -> {
                val *= 0.01;
                AttributeInstance inst = this.getAttribute(att);
                if (inst != null) {
                    float multiplier = 1;
                    if (att == Attributes.MAX_HEALTH) {
                        multiplier += LevelCalc.getMultiplierInterval(this.xpLevel().getLevel(), 20, 30, 0.12f) * 0.015f;
                    } else {
                        multiplier += LevelCalc.getMultiplierInterval(this.xpLevel().getLevel(), 20, 30, 0) * 0.01f;
                    }
                    inst.addPermanentModifier(new AttributeModifier(LibConstants.MONSTER_LEVEL_MODIFIER, (this.xpLevel().getLevel() - 1) * val * multiplier, AttributeModifier.Operation.ADD_VALUE));
                    if (att == Attributes.MAX_HEALTH)
                        this.setHealth(this.getMaxHealth() - preHealthDiff);
                }
            });
            return;
        }
        int levelOffset = 1;
        AttributeInstance inst = this.getAttribute(Attributes.MAX_HEALTH);
        if (inst != null) {
            float multiplier = 1;
            inst.addPermanentModifier(new AttributeModifier(LibConstants.MONSTER_LEVEL_MODIFIER, (this.xpLevel().getLevel() - levelOffset) * MobConfig.NPC_HEALTH_GAIN * multiplier, AttributeModifier.Operation.ADD_VALUE));
            this.setHealth(this.getMaxHealth() - preHealthDiff);
        }
        inst = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (inst != null) {
            float multiplier = 1;
            inst.addPermanentModifier(new AttributeModifier(LibConstants.MONSTER_LEVEL_MODIFIER, (this.xpLevel().getLevel() - levelOffset) * MobConfig.NPC_ATTACK_GAIN * multiplier, AttributeModifier.Operation.ADD_VALUE));
        }
        inst = this.getAttribute(RuneCraftoryAttributes.DEFENCE.asHolder());
        if (inst != null) {
            float multiplier = 1;
            inst.addPermanentModifier(new AttributeModifier(LibConstants.MONSTER_LEVEL_MODIFIER, (this.xpLevel().getLevel() - levelOffset) * MobConfig.NPC_DEFENCE_GAIN * multiplier, AttributeModifier.Operation.ADD_VALUE));
        }
        inst = this.getAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder());
        if (inst != null) {
            float multiplier = 1;
            inst.addPermanentModifier(new AttributeModifier(LibConstants.MONSTER_LEVEL_MODIFIER, (this.xpLevel().getLevel() - levelOffset) * MobConfig.NPC_MAGIC_ATTACK_GAIN * multiplier, AttributeModifier.Operation.ADD_VALUE));
        }
        inst = this.getAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder());
        if (inst != null) {
            float multiplier = 1;
            inst.addPermanentModifier(new AttributeModifier(LibConstants.MONSTER_LEVEL_MODIFIER, (this.xpLevel().getLevel() - levelOffset) * MobConfig.NPC_MAGIC_DEFENCE_GAIN * multiplier, AttributeModifier.Operation.ADD_VALUE));
        }
    }

    @Override
    public int friendPoints(UUID uuid) {
        return this.relationManager.getFriendPointData(uuid).points.getLevel();
    }

    public int talkCount(UUID uuid) {
        return this.relationManager.getFriendPointData(uuid).getTalkCount();
    }

    @Override
    public int baseXP() {
        return 0;
    }

    @Override
    public int baseMoney() {
        return 0;
    }

    /**
     * Gets the family of this npc. Returns null on client
     */
    @Nullable
    public FamilyEntry getFamily() {
        if (this.getServer() != null) {
            return FamilyHandler.get(this.getServer())
                    .getOrCreateEntry(this);
        }
        return null;
    }

    /**
     * The player partner if npc is in a relationship with a player
     */
    @Nullable
    public Player getPartner() {
        FamilyEntry family = this.getFamily();
        if (family != null) {
            if (family.getPartner() != null && family.hasPlayerRelationShip())
                return this.getServer().getPlayerList().getPlayer(family.getPartner());
        }
        return null;
    }

    public FamilyEntry.Relationship relationFor(UUID player) {
        FamilyEntry entry = this.getFamily();
        if (entry != null && player.equals(entry.getPartner()))
            return entry.getRelationship();
        return FamilyEntry.Relationship.NONE;
    }

    @Override
    public boolean applyFoodEffect(ItemStack stack) {
        if (this.level().isClientSide)
            return false;
        if (stack.getItem() == RuneCraftoryItems.OBJECT_X.get())
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
        Pair<Map<Holder<Attribute>, Double>, Map<Holder<Attribute>, Double>> foodStats = ItemComponentUtils.foodStats(stack);
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

    public NPCProfession getProfession() {
        return this.profession;
    }

    public void setProfession(NPCProfession profession) {
        this.profession = profession;
    }

    public void handleUpdatePacket(Player player, CompoundTag friendPoints, CompoundTag schedule, NPCProfession profession) {
        if (this.level().isClientSide) {
            this.relationManager.getFriendPointData(player.getUUID()).load(friendPoints);
            this.schedule.load(schedule);
            this.setProfession(profession);
        }
    }

    public Activity getActivity() {
        return this.getBrain().getActiveNonCoreActivity().orElse(null);
    }

    public NPCSchedule getNPCSchedule() {
        return this.schedule;
    }

    public GlobalPos getWorkPlace() {
        return this.getBrain().getMemory(MemoryModuleType.JOB_SITE).orElse(null);
    }

    public void releaseWorkplacePoi() {
        if (this.getProfession().hasPoi())
            this.releasePoi(MemoryModuleType.JOB_SITE, this.getProfession()::matches);
    }

    public GlobalPos getBedPos() {
        return this.getBrain().getMemory(MemoryModuleType.HOME).orElse(null);
    }

    public void releaseBedPoi() {
        this.releasePoi(MemoryModuleType.HOME, h -> h.is(PoiTypes.HOME));
    }

    public GlobalPos getMeetingPos() {
        return this.getBrain().getMemory(MemoryModuleType.MEETING_POINT).orElse(null);
    }

    public void releaseMeetingPoi() {
        this.releasePoi(MemoryModuleType.MEETING_POINT, h -> h.is(PoiTypes.MEETING));
    }

    public void releasePoi(MemoryModuleType<GlobalPos> memory, Predicate<Holder<PoiType>> test) {
        if (this.level() instanceof ServerLevel level) {
            MinecraftServer server = level.getServer();
            GlobalPos current = BrainUtils.getMemory(this, memory);
            BrainUtils.clearMemory(this, memory);
            if (current != null) {
                ServerLevel serverLevel = level.dimension().equals(current.dimension()) ? level : server.getLevel(current.dimension());
                if (serverLevel != null) {
                    PoiManager poiManager = serverLevel.getPoiManager();
                    Optional<Holder<PoiType>> opt = poiManager.getType(current.pos());
                    if (opt.isPresent() && test.test(opt.get())) {
                        poiManager.release(current.pos());
                        DebugPackets.sendPoiTicketCountPacket(serverLevel, current.pos());
                    }
                }
            }
        }
    }

    public ShopState canTrade() {
        if (this.isBaby() || (!this.profession.hasShop && !this.profession.hasWorkSchedule))
            return ShopState.NOTWORKER;
        if (!this.profession.hasWorkSchedule)
            return ShopState.OPEN;
        if (this.getWorkPlace() == null)
            return ShopState.NOWORKPLACE;
        if (this.getBedPos() == null)
            return ShopState.NOBED;
        if (this.getActivity() != Activity.WORK)
            return ShopState.CLOSED;
        if (!this.nearWorkPlace(this.getWorkPlace(), 4))
            return ShopState.TOOFAR;
        return ShopState.OPEN;
    }

    public boolean nearWorkPlace(GlobalPos pos, int range) {
        if (this.followEntity() == null) {
            if (this.level().dimension() != pos.dimension())
                return false;
            return pos.pos().closerToCenterThan(this.position(), range);
        }
        return false;
    }

    public Player followEntity() {
        if (this.entityToFollowUUID != null) {
            if (this.entityToFollow == null || !this.entityToFollow.isAlive()) {
                if (this.level().isClientSide)
                    this.entityToFollow = this.level().getPlayerByUUID(this.entityToFollowUUID);
                else
                    this.entityToFollow = this.level().getServer().getPlayerList().getPlayer(this.entityToFollowUUID);
            }
        }
        return this.entityToFollow;
    }

    public UUID getEntityToFollowUUID() {
        return this.entityToFollowUUID;
    }

    public boolean followEntity(ServerPlayer player) {
        if (player == null)
            this.setBehaviour(Behaviour.WANDER);
        if (player != null) {
            FamilyEntry family = this.getFamily();
            int points = this.friendPoints(player);
            float chance = points < 3 ? -0.1f + 0.1f * points : (0.5f + 0.15f * points);
            if (!player.isCreative()
                    && (family != null && family.hasPlayerRelationShip() && !family.getPartner().equals(player.getUUID()))
                    && this.updater.getDailyRandom().nextFloat() < chance) {
                this.speak(player, ConversationContext.FOLLOW_NO);
                return false;
            }
            this.speak(player, ConversationContext.FOLLOW_YES);
            this.entityToFollowUUID = player.getUUID();
        } else {
            if (this.followEntity() instanceof ServerPlayer serverPlayer)
                this.speak(serverPlayer, ConversationContext.FOLLOW_STOP);
            this.entityToFollowUUID = null;
        }
        this.entityToFollow = player;
        if (player != null)
            this.setBehaviour(Behaviour.FOLLOW);
        return true;
    }

    @Override
    public boolean hasRestriction() {
        return super.hasRestriction() && this.getEntityToFollowUUID() == null;
    }

    @Override
    public void stopSleeping() {
        super.stopSleeping();
        BrainUtils.setMemory(this, MemoryModuleType.LAST_WOKEN, this.level().getGameTime());
    }

    @Override
    public float getScale() {
        float size = super.getScale();
        SizeFeatureType.SizeFeature feat = this.lookFeatures.getFeature(RuneCraftoryNPCLooks.SIZE.get());
        if (feat != null)
            return size * feat.size();
        return size;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    public boolean procreateWith(Entity other) {
        FamilyEntry family = this.getFamily();
        if (family == null || !family.getPartner().equals(other.getUUID()) || this.procreationProgress > 0)
            return false;
        if (!other.getUUID().equals(family.getPartner()))
            return false;
        if (!this.canProcreate()) {
            if (other instanceof ServerPlayer player) {
                this.speak(player, ConversationContext.PROCREATION_COOLDOWN);
            }
            return false;
        }
        this.procreationProgress = 60;
        this.procreationEntity = other;
        return true;
    }

    public boolean canProcreate() {
        return this.procreationCooldown < 0;
    }

    private boolean spawnBaby() {
        if (!(this.level() instanceof ServerLevel serverLevel))
            return false;
        NPCEntity baby = RuneCraftoryEntities.NPC.get().create(serverLevel, null, this.blockPosition(), MobSpawnType.BREEDING, false, false);
        if (baby == null)
            return false;
        baby.setBaby(true);
        List<ResourceLocation> childIDs = new ArrayList<>(this.data.value().possibleChildren());
        NPCDataManager manager = DataPackHandler.INSTANCE.npcDataManager();
        if (this.procreationEntity instanceof NPCEntity npc) {
            childIDs.addAll(npc.data.value().possibleChildren());
        }
        childIDs.removeIf(r -> !manager.has(r));
        if (childIDs.isEmpty()) {
            baby.randomizeData(null, true);
        } else {
            baby.setXPLevel(1);
            baby.setNPCData(DataPackHandler.INSTANCE.npcDataManager().get(childIDs.get(this.random.nextInt(childIDs.size()))), false);
        }
        UUID father;
        UUID mother;
        if (this.isMale()) {
            father = this.getUUID();
            mother = this.getFamily().getPartner();
        } else {
            mother = this.getUUID();
            father = this.getFamily().getPartner();
        }
        if (this.procreationEntity instanceof ServerPlayer player) {
            player.addItem(BabySpawnEgg.createBabyFrom(baby, player.getName(), father, mother));
        } else {
            baby.getFamily().setFather(father);
            baby.getFamily().setMother(mother);
            serverLevel.addFreshEntity(baby);
        }
        return true;
    }

    public void tryUpdateName(Component component) {
        if (this.data.value().name() == null)
            this.setCustomName(component);
    }

    public Optional<String> getDataName() {
        return Optional.ofNullable(this.data.value().name());
    }

    public boolean isMale() {
        return this.entityData.get(MALE);
    }

    public void setMale(boolean flag) {
        this.entityData.set(MALE, flag);
    }

    public ReloadableHolder<NPCLook> getLook() {
        if (this.look == null) {
            if (this.data == NPCData.DEFAULT)
                this.look = NPCLook.DEFAULT;
            else {
                List<ResourceLocation> looks = this.data.value().look() == null ? List.of() : this.data.value().look().stream().filter(e -> e.gender() == NPCData.Gender.UNDEFINED || (e.gender() == NPCData.Gender.MALE) == this.isMale())
                        .map(NPCData.NPCLookId::id).toList();
                if (!looks.isEmpty())
                    this.look = DataPackHandler.INSTANCE.npcLookManager().get(looks.get(this.random.nextInt(looks.size())));
                else
                    this.look = DataPackHandler.INSTANCE.npcLookManager().getRandom(this.random, this.isMale());
            }
        }
        return this.look;
    }

    public ReloadableHolder<NPCAttackActions> getAttackActions() {
        if (this.attackActions == null) {
            if (this.data == null || this.data == NPCData.DEFAULT || this.data.value().combatActions() == null)
                this.attackActions = NPCAttackActions.DEFAULT;
            else {
                List<ResourceLocation> actions = this.data.value().combatActions();
                this.attackActions = DataPackHandler.INSTANCE.npcActionsManager().get(actions.isEmpty() ? null : actions.get(this.random.nextInt(actions.size())));
            }
        }
        return this.attackActions;
    }

    public Pair<Season, Integer> getBirthday() {
        if (this.birthday == null) {
            if (this.data == NPCData.DEFAULT)
                this.birthday = Pair.of(Season.SPRING, 1);
            else if (this.data.value().birthday() != null)
                this.birthday = this.data.value().birthday();
            else {
                Season randSeason = Season.values()[this.random.nextInt(Season.values().length)];
                int day = this.random.nextInt(30) + 1;
                this.birthday = Pair.of(randSeason, day);
            }
        }
        return this.birthday;
    }

    public void setClientLook(ReloadableHolder<NPCLook> look) {
        if (this.level().isClientSide) {
            this.look = look;
            this.refreshDimensions();
        }
    }

    public NPCData.Gift giftOf(ItemStack stack) {
        this.calcGifts();
        for (Map.Entry<String, NPCData.Gift> e : this.data.value().giftItems().entrySet()) {
            if (this.gifts.get(e.getKey()).value().is(stack))
                return e.getValue();
        }
        return null;
    }

    protected Optional<Component> randomGiftContext(int min, int max) {
        this.calcGifts();
        List<GiftData> gifts = new ArrayList<>();
        this.data.value().giftItems().forEach((key, gift) -> {
            if (gift.xp() >= min && gift.xp() <= max)
                gifts.add(this.gifts.get(key).value());
        });
        if (gifts.isEmpty())
            return Optional.empty();
        GiftData gift = gifts.get(this.updater.getDailyRandom().nextInt(gifts.size()));
        return Optional.of(Component.translatable(gift.translation(this.updater.getDailyRandom())));
    }

    private void calcGifts() {
        if (this.gifts == null) {
            ImmutableMap.Builder<String, ReloadableHolder<GiftData>> b = ImmutableMap.builder();
            this.data.value().giftItems().forEach((s, g) -> {
                ReloadableHolder<GiftData> giftData = g.giftID() == null ? DataPackHandler.INSTANCE.giftManager().getRandomGift(this.updater.getDailyRandom(), g.xp())
                        : DataPackHandler.INSTANCE.giftManager().get(g.giftID());
                if (giftData != null)
                    b.put(s, giftData);
            });
            this.gifts = b.build();
        }
    }

    public void openShopForPlayer(ServerPlayer player) {
        if (this.canTrade() == ShopState.OPEN && this.getProfession().hasShop) {
            this.interactWithPlayer(player);
            PlayerData data = Platform.INSTANCE.getPlayerData(player);
            NonNullList<ItemStack> shopList = data.getShop(this.getProfession());
            Platform.INSTANCE.openGuiMenu(player, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable(NPCEntity.this.getProfession().getTranslationKey());
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                    return new ContainerShop(i, inventory, new InventoryShop(NPCEntity.this, shopList));
                }
            }, buf -> ContainerShop.DATA_STREAM_CODEC.encode(buf, new ContainerShop.Data(NPCEntity.this.getId(), shopList)));
        }
    }

    public void randomizeData(@Nullable ResourceLocation res) {
        this.randomizeData(res == null ? null : RuneCraftoryNPCProfessions.PROFESSIONS.registry().get(res), false);
    }

    public void randomizeData(NPCProfession profession, boolean overwrite) {
        if (this.getServer() != null) {
            this.setNPCData(DataPackHandler.INSTANCE.npcDataManager().getRandom(this.random, d ->
                    (d.value().profession().isEmpty() || d.value().profession().stream().anyMatch(j -> j.equals(profession)))
                            && RunecraftorySavedData.get(this.getServer()).npcHandler.canAssignNPC(d), profession == null ? null :
                    d -> d.value().profession().stream().anyMatch(j -> j.equals(profession))), !overwrite);
            if (profession != null)
                this.setProfession(profession);
        }
    }

    public ResourceLocation getDataID() {
        return this.data.id();
    }

    public void setNPCData(ReloadableHolder<NPCData> holder, boolean load) {
        if (this.getServer() != null) {
            if (this.data != null)
                RunecraftorySavedData.get(this.getServer()).npcHandler.removeUniqueNPC(this.getUUID(), this.data);
            RunecraftorySavedData.get(this.getServer()).npcHandler.addUniqueNPC(this.getUUID(), holder);
        }
        this.data = holder;
        NPCData data = this.data.value();
        this.dataRandom.setSeed(this.getUUID().hashCode());
        if (!load) {
            this.releaseWorkplacePoi();
            this.setProfession(!data.profession().isEmpty() ? data.profession().get(this.dataRandom.nextInt(data.profession().size()))
                    : RuneCraftoryNPCProfessions.PROFESSIONS.registry().getRandom(this.random).map(Holder::value).get());
            this.setMale(data.gender() == NPCData.Gender.UNDEFINED ? this.random.nextBoolean() : data.gender() != NPCData.Gender.FEMALE);
            if (data.name() == null) {
                String name = DataPackHandler.INSTANCE.nameManager().getRandomFullName(this.random, this.isMale());
                if (name != null) {
                    this.setCustomName(Component.literal(name));
                }
            } else {
                String name = data.name();
                if (data.surname() != null)
                    name += " " + data.surname();
                this.setCustomName(Component.literal(name));
            }
            this.birthday = null;
            this.getBirthday();
            this.look = null;
            this.getLook();
            this.attackActions = null;
            this.getAttackActions();
            if (data.schedule() == null)
                this.schedule.load(new NPCSchedule(this, this.random).save());
            else
                this.schedule.with(data.schedule());
            this.lookFeatures.buildFromLooks(this, this.look.value());
            this.gifts = null;
            this.calcGifts();
        } else {
            // Apply non null things else
            if (data.look() != null && !data.look().isEmpty()) {
                this.look = null;
                this.getLook();
            }
            if (!data.profession().isEmpty() && !data.profession().contains(this.getProfession()))
                this.setProfession(!data.profession().isEmpty() ? data.profession().get(this.dataRandom.nextInt(data.profession().size()))
                        : RuneCraftoryNPCProfessions.PROFESSIONS.registry().getRandom(this.random).map(Holder::value).get());
            if (data.gender() != NPCData.Gender.UNDEFINED && (data.gender() == NPCData.Gender.MALE) != this.isMale())
                this.setMale(data.gender() == NPCData.Gender.UNDEFINED ? this.random.nextBoolean() : data.gender() != NPCData.Gender.FEMALE);
            if (data.name() != null) {
                String name = data.name();
                if (data.surname() != null)
                    name += " " + data.surname();
                this.setCustomName(Component.literal(name));
            }
            if (data.birthday() != null) {
                this.birthday = null;
                this.getBirthday();
            }
            if (data.combatActions() != null && !data.combatActions().isEmpty())
                this.attackActions = null;
            if (data.schedule() != null)
                this.schedule.with(data.schedule());
        }
        this.applyAttributes(!load);
        if (this.xpLevel().getLevel() < data.baseLevel()) {
            this.setXPLevel(data.baseLevel());
        }
        this.refreshBrain((ServerLevel) this.level());
        this.refreshDimensions();
        if (!this.level().isClientSide)
            LoaderNetwork.INSTANCE.sendToTracking(new S2CNPCLook(this.getId(), this.look, this.lookFeatures), this);
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    public enum Behaviour {

        WANDER("runecraftory.npc.interact.home", false),
        FOLLOW("runecraftory.npc.interact.follow", true),
        FOLLOW_DISTANCE("runecraftory.npc.interact.follow.distance", true),
        STAY("runecraftory.npc.interact.stay", true);

        public final String interactKey;

        public final boolean following;

        Behaviour(String interactKey, boolean following) {
            this.interactKey = interactKey;
            this.following = following;
        }
    }
}
