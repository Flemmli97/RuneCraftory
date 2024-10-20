package io.github.flemmli97.runecraftory.common.entities.npc;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.datapack.ConversationContext;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.SimpleEffect;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCDataManager;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import io.github.flemmli97.runecraftory.common.entities.ai.AvoidWhenNotFollowing;
import io.github.flemmli97.runecraftory.common.entities.ai.LookAtAliveGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.LookAtInteractingPlayerGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.RandomLookGoalAlive;
import io.github.flemmli97.runecraftory.common.entities.ai.StayGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCFindPOI;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCFollowGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCWanderGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.NPCAttackActions;
import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeatureContainer;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SizeFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.entities.npc.job.ShopState;
import io.github.flemmli97.runecraftory.common.entities.pathing.NPCWalkNodeEvaluator;
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
import io.github.flemmli97.runecraftory.common.registry.ModActivities;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.TeleportUtils;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.NPCHandler;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.common.world.family.FamilyEntry;
import io.github.flemmli97.runecraftory.common.world.family.FamilyHandler;
import io.github.flemmli97.runecraftory.integration.simplequest.NPCQuest;
import io.github.flemmli97.runecraftory.integration.simplequest.ProgressState;
import io.github.flemmli97.runecraftory.integration.simplequest.SimpleQuestIntegration;
import io.github.flemmli97.runecraftory.mixin.AttributeMapAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.api.item.IAOEWeapon;
import io.github.flemmli97.tenshilib.api.item.IExtendedWeapon;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
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
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EntityNPCBase extends AgeableMob implements Npc, IBaseMob, IAnimated {

    public static final float PATH_FIND_LENGTH = 100;

    private static final EntityDataAccessor<Boolean> PLAY_DEATH_STATE = SynchedEntityData.defineId(EntityNPCBase.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SHOP_SYNC = SynchedEntityData.defineId(EntityNPCBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> MALE = SynchedEntityData.defineId(EntityNPCBase.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> BEHAVIOUR_DATA = SynchedEntityData.defineId(EntityNPCBase.class, EntityDataSerializers.INT);

    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.MEETING_POINT,
            MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);

    public static final AnimatedAction[] ANIMS = PlayerModelAnimations.getAll().toArray(new AnimatedAction[0]);
    private final AnimationHandler<EntityNPCBase> animationHandler = new AnimationHandler<>(this, ANIMS);

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

    public NearestAttackableTargetGoal<Mob> targetMobs = new NearestAttackableTargetGoal<>(this, Mob.class, 5, true, true, this.targetPred);
    public NPCWanderGoal wander = new NPCWanderGoal(this);
    public HurtByTargetGoal hurt = new HurtByTargetGoal(this);

    private final LevelExpPair levelPair = new LevelExpPair();

    private NPCJob shop = ModNPCJobs.NONE.getSecond();

    public boolean ignoreInit;
    private NPCData data = NPCData.DEFAULT_DATA;
    private NPCData.NPCLook look = NPCData.NPCLook.DEFAULT_LOOK;
    public final NPCFeatureContainer lookFeatures = new NPCFeatureContainer();
    private NPCAttackActions attackActions = NPCAttackActions.DEFAULT;
    private Pair<EnumSeason, Integer> birthday = Pair.of(EnumSeason.SPRING, 1);
    //Will be used if data returns null for these values
    private final Map<String, TagKey<Item>> gift = new HashMap<>();
    private final Random dataRandom = new Random();

    private Activity activity = Activity.IDLE;

    private int foodBuffTick;
    private int playDeathTick;

    private final NPCRelationManager relationManager = new NPCRelationManager();
    private int procreationCooldown, procreationProgress;
    private Entity procreationEntity;

    private Behaviour behaviour = Behaviour.WANDER;

    private Player entityToFollow;
    private UUID entityToFollowUUID;

    private int sleepCooldown, tpCooldown;

    private final List<ServerPlayer> interactingPlayers = new ArrayList<>();
    private int interactionMoveCooldown;

    private final NPCSchedule schedule;

    private BlockPos prevRestriction = BlockPos.ZERO;
    private int prevRestrictionRadius = -1;

    public final DailyNPCUpdater updater = new DailyNPCUpdater(this);

    public final WeaponHandler weaponHandler = new WeaponHandler();

    public EntityNPCBase(EntityType<? extends EntityNPCBase> type, Level level) {
        super(type, level);
        this.levelPair.setLevel(LibConstants.BASE_LEVEL, LevelCalc::xpAmountForLevelUp);
        this.applyAttributes(true);
        if (!level.isClientSide)
            this.addGoal();
        this.schedule = new NPCSchedule(this, this.getRandom());
    }

    public static AttributeSupplier.Builder createAttributes(Collection<? extends RegistryEntrySupplier<Attribute>> atts) {
        AttributeSupplier.Builder map = Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.24)
                .add(Attributes.FOLLOW_RANGE, 32);
        if (atts != null)
            for (RegistryEntrySupplier<Attribute> att : atts)
                map.add(att.get());
        return map;
    }

    protected void applyAttributes(boolean regenHealth) {
        if (this.data != null && this.data.baseStats() != null) {
            this.data.baseStats().forEach((att, d) -> {
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
        inst = this.getAttribute(ModAttributes.DEFENCE.get());
        if (inst != null) {
            inst.setBaseValue(MobConfig.NPC_DEFENCE);
        }
        inst = this.getAttribute(ModAttributes.MAGIC.get());
        if (inst != null) {
            inst.setBaseValue(MobConfig.NPC_MAGIC_ATTACK);
        }
        inst = this.getAttribute(ModAttributes.MAGIC_DEFENCE.get());
        if (inst != null) {
            inst.setBaseValue(MobConfig.NPC_MAGIC_DEFENCE);
        }
    }

    public void recalcStatsFull() {
        this.applyAttributes(true);
    }

    public void addGoal() {
        this.targetSelector.addGoal(1, this.targetMobs);
        this.targetSelector.addGoal(0, this.hurt);

        this.goalSelector.addGoal(0, new NPCFindPOI(this));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(0, new StayGoal<>(this, StayGoal.CANSTAYNPC));
        this.goalSelector.addGoal(1, new AvoidWhenNotFollowing(this, LivingEntity.class, 8, 1.3, 1.2));
        this.goalSelector.addGoal(1, new LookAtInteractingPlayerGoal(this));
        this.goalSelector.addGoal(2, new LookAtAliveGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(3, new RandomLookGoalAlive(this));
        this.goalSelector.addGoal(2, new NPCAttackGoal<>(this));
        this.goalSelector.addGoal(3, new NPCFollowGoal(this, 1.15, 9, 3, 20));
        this.goalSelector.addGoal(4, this.wander);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PLAY_DEATH_STATE, false);
        this.entityData.define(SHOP_SYNC, 0);
        this.entityData.define(MALE, false);
        this.entityData.define(BEHAVIOUR_DATA, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (this.level.isClientSide) {
            if (key.equals(SHOP_SYNC)) {
                try {
                    this.shop = ModNPCJobs.getFromSyncID(this.entityData.get(SHOP_SYNC));
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
            if (key.equals(BEHAVIOUR_DATA)) {
                try {
                    this.behaviour = Behaviour.values()[this.entityData.get(BEHAVIOUR_DATA)];
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return Brain.provider(MEMORY_TYPES, ImmutableList.of());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        GroundPathNavigation nav = new GroundPathNavigation(this, level) {

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

    @Override
    public void tick() {
        if (this.firstTick) {
            if (this.getServer() != null) {
                NPCHandler handler = WorldHandler.get(this.getServer()).npcHandler;
                handler.addNPC(this);
                handler.playersToReset(this.getUUID()).forEach(pair -> this.relationManager.resetQuest(pair.getFirst(), pair.getSecond()));
            }
        }
        super.tick();
        if (this.playDeath()) {
            this.playDeathTick = Math.min(15, ++this.playDeathTick);
            if (!this.level.isClientSide && this.getHealth() > 0.02) {
                this.setPlayDeath(false);
            }
        } else {
            this.playDeathTick = Math.max(0, --this.playDeathTick);
        }
        if (!this.level.isClientSide) {
            if (this.tickCount % 10 == 0) {
                this.interactingPlayers.removeIf(p -> p.distanceToSqr(this) > 100);
            }
            --this.interactionMoveCooldown;
            this.updater.tick();
            this.updateActivity();
            this.foodBuffTick = Math.max(-1, --this.foodBuffTick);
            if (this.foodBuffTick == 0) {
                this.removeFoodEffect();
            }
            this.getAnimationHandler().runIfNotNull(this::handleAttack);
            --this.sleepCooldown;
            if (this.getSleepingPos().map(pos -> !pos.closerToCenterThan(this.position(), 1) || this.getActivity() != Activity.REST).orElse(false))
                this.stopSleeping();
        }
    }

    @Override
    public void aiStep() {
        this.updateSwingTime();
        super.aiStep();
        this.getAnimationHandler().tick();
        this.weaponHandler.tick(this);
        boolean teleported = false;
        if (this.level instanceof ServerLevel serverLevel) {
            if (this.behaviourState().following && --this.tpCooldown <= 0) {
                Player follow = this.followEntity();
                if (follow != null) {
                    serverLevel.getChunkSource().addRegionTicket(WorldUtils.ENTITY_LOADER, this.chunkPosition(), 3, this.chunkPosition());
                    if (follow.level.dimension() != this.level.dimension()) {
                        TeleportUtils.safeDimensionTeleport(this, (ServerLevel) follow.level, follow.blockPosition());
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
        }
        if (this.playDeath()) {
            this.playDeathTick = Math.min(15, ++this.playDeathTick);
            if (!this.level.isClientSide) {
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
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!(player instanceof ServerPlayer serverPlayer))
            return InteractionResult.CONSUME;
        if (this.isSleeping())
            return InteractionResult.CONSUME;
        if (this.getEntityToFollowUUID() != null && this.getEntityToFollowUUID().equals(serverPlayer.getUUID())) {
            EntityUtils.sendAttributesTo(this, serverPlayer);
        }
        Platform.INSTANCE.sendToClient(new S2CUpdateNPCData(this, this.relationManager.getFriendPointData(player.getUUID()).save()), serverPlayer);
        Platform.INSTANCE.sendToClient(new S2COpenNPCGui(this, serverPlayer), serverPlayer);
        this.interactWithPlayer(serverPlayer);
        this.lookAt(serverPlayer, 30, 30);
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return SpawnEgg.fromType(this.getType()).map(ItemStack::new).orElse(ItemStack.EMPTY);
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
            this.level.addParticle(particleData, this.getRandomX(1.0), this.getRandomY() + 1.0, this.getRandomZ(1.0), d, e, f);
        }
    }

    public void giftItem(Player player, ItemStack stack) {
        this.lookAt(player, 30, 30);
        int count = stack.getCount();
        SoundEvent sound = switch (stack.getUseAnimation()) {
            case DRINK -> stack.getDrinkingSound();
            case EAT -> stack.getEatingSound();
            default -> SoundEvents.NOTE_BLOCK_PLING;
        };
        if (stack.getItem() == ModItems.DIVORCE_PAPER.get()) {
            if (player instanceof ServerPlayer serverPlayer) {
                FamilyHandler families = FamilyHandler.get(this.getServer());
                FamilyEntry family = families.getOrCreateEntry(this);
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
        if (stack.getItem() == ModItems.LOVE_LETTER.get()) {
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
        if (stack.getItem() == ModItems.ENGAGEMENT_RING.get()) {
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
                        if (chance > 0 && this.relationManager.getCompletedQuests(player.getUUID()).containsAll(this.data.questHandler().requiredQuests()) &&
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
            WorldHandler handler = WorldHandler.get(serverPlayer.getServer());
            if (handler.currentSeason() == this.birthday.getFirst() && handler.date() == this.birthday.getSecond())
                mult = 3;
            serverPlayer.connection.send(new ClientboundSoundPacket(sound, SoundSource.NEUTRAL, player.getX(), player.getY(), player.getZ(), 0.7f, 1));
        }
        EquipmentSlot slot = ItemUtils.slotOf(stack);
        if (slot != EquipmentSlot.MAINHAND || ItemNBT.isWeapon(stack) || stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem) {
            ItemStack copy = stack.copy();
            copy.setCount(1);
            this.setItemSlot(slot, copy);
        }
        this.applyFoodEffect(stack);
        stack.setCount(count);
        if (player instanceof ServerPlayer serverPlayer) {
            NPCData.Gift gift = this.giftOf(stack);
            if (gift != null) {
                if (this.relationManager.getFriendPointData(player.getUUID()).giftXP(this.level, (int) (gift.xp() * mult)))
                    this.tellDialogue(serverPlayer, null, null, new TranslatableComponent(gift.responseKey()), List.of());
            } else {
                if (this.relationManager.getFriendPointData(player.getUUID()).giftXP(this.level, (int) (5 * mult)))
                    this.tellDialogue(serverPlayer, null, null, new TranslatableComponent(this.data.neutralGiftResponse()), List.of());
            }
        }
        stack.shrink(1);
    }

    public void talkTo(ServerPlayer player) {
        ConversationContext ctx = ConversationContext.TALK;
        NPCFriendPoints fp = this.relationManager.getFriendPointData(player.getUUID());
        boolean talkedTo = fp.talked();
        boolean doGreet = fp.talkTo(this.level, 15);
        if (!talkedTo) {
            ctx = ConversationContext.FIRST_TALK;
        } else if (doGreet) {
            ctx = ConversationContext.GREETING;
        }
        this.speak(player, ctx);
    }

    public void speak(ServerPlayer player, ConversationContext convCtx) {
        int heart = this.relationManager.getFriendPointData(player.getUUID()).points.getLevel();
        NPCData.ConversationSet conversations = this.data.getConversation(convCtx);
        LootContext ctx = new LootContext.Builder((ServerLevel) this.level).withRandom(this.random)
                .withParameter(LootContextParams.THIS_ENTITY, this)
                .withParameter(LootContextParams.ORIGIN, this.position())
                .withParameter(LootCtxParameters.UUID_CONTEXT, player.getUUID())
                .withLuck(player.getLuck()).create(LootCtxParameters.NPC_INTERACTION);
        List<Map.Entry<String, NPCData.Conversation>> filtered = conversations.conversations().entrySet().stream()
                .filter(c -> {
                    //Disable if player already has a quest from this npc
                    if (c.getValue().actions().stream().anyMatch(h -> h.action() == NPCData.ConversationAction.QUEST) &&
                            SimpleQuestIntegration.INST().questForExists(player, this) != null &&
                            !this.updater.alreadyAcceptedRandomquest(player))
                        return false;
                    return c.getValue().startingConversation() && c.getValue().test(heart, ctx);
                })
                .collect(Collectors.toList());
        Collections.shuffle(filtered, this.updater.getDailyRandom());
        int size = Math.min(filtered.size(), 2 + this.updater.getDailyRandom().nextInt(2)); //Select 2-3 random lines
        if (size > 0) {
            Map.Entry<String, NPCData.Conversation> randomLine = filtered.get(this.random.nextInt(size));
            this.tellDialogue(player, convCtx, randomLine.getKey(), randomLine.getValue());
        } else {
            Component dialog = conversations.fallbackKey().equals(NPCData.ConversationSet.DEFAULT.fallbackKey())
                    ? new TranslatableComponent(conversations.fallbackKey(), convCtx.key()) : new TranslatableComponent(conversations.fallbackKey());
            this.tellDialogue(player, convCtx, null, dialog, List.of());
        }
    }

    public void respondToQuest(ServerPlayer player, ResourceLocation quest) {
        SimpleQuestIntegration.INST().triggerNPCTalk(player, this);
        ProgressState result = SimpleQuestIntegration.INST().checkCompletionQuest(player, this);
        int questState = this.relationManager.questStateFor(player.getUUID(), quest);
        if (questState != -1) {
            if (result == ProgressState.COMPLETE) {
                this.relationManager.endQuest(player.getUUID(), quest);
                questState = -2;
            } else if (result == ProgressState.PARTIAL) {
                this.relationManager.advanceQuest(player.getUUID(), quest);
                questState++;
            }
        }
        int heart = this.relationManager.getFriendPointData(player.getUUID()).points.getLevel();
        NPCData.ConversationSet conversations = this.data.getFromQuest(quest, questState);
        LootContext ctx = new LootContext.Builder((ServerLevel) this.level).withRandom(this.random)
                .withParameter(LootContextParams.THIS_ENTITY, this)
                .withParameter(LootContextParams.ORIGIN, this.position())
                .withParameter(LootCtxParameters.UUID_CONTEXT, player.getUUID())
                .withLuck(player.getLuck()).create(LootCtxParameters.NPC_INTERACTION);
        List<Map.Entry<String, NPCData.Conversation>> filtered = conversations.conversations().entrySet().stream().filter(c -> c.getValue().startingConversation() && c.getValue().test(heart, ctx))
                .collect(Collectors.toList());
        Collections.shuffle(filtered, this.updater.getDailyRandom());
        int size = Math.min(filtered.size(), 2 + this.updater.getDailyRandom().nextInt(2)); //Select 2-3 random lines
        if (size > 0) {
            Map.Entry<String, NPCData.Conversation> randomLine = filtered.get(this.random.nextInt(size));
            this.tellDialogue(player, null, randomLine.getKey(), randomLine.getValue());
        } else {
            this.tellDialogue(player, null, null, new TranslatableComponent(conversations.fallbackKey()), List.of());
        }
        if (questState == -1)
            this.relationManager.advanceQuest(player.getUUID(), quest);
    }

    private void tellDialogue(ServerPlayer player, ConversationContext convCtx, String conversationID, NPCData.Conversation conversation) {
        List<Component> actions = conversation.actions().stream().map(e -> (Component) new TranslatableComponent(e.translationKey())).toList();
        this.tellDialogue(player, convCtx, conversationID, new TranslatableComponent(conversation.translationKey()), actions);
    }

    private void tellDialogue(ServerPlayer player, ConversationContext convCtx, String conversationID, Component component, List<Component> actions) {
        this.interactWithPlayer(player);
        Platform.INSTANCE.sendToClient(new S2CNpcDialogue(this.getId(), convCtx, conversationID, component, this.conversationData(player), actions), player);
    }

    private Map<String, Component> conversationData(ServerPlayer player) {
        Map<String, Component> map = new HashMap<>();
        map.put(PlaceHolderComponent.NPC, this.getDisplayName());
        map.put(PlaceHolderComponent.PLAYER, player.getDisplayName());
        this.randomGiftItem(15, Integer.MAX_VALUE).ifPresent(item -> map.put(PlaceHolderComponent.FAVORITE, item.getDescription()));
        this.randomGiftItem(7, 14).ifPresent(item -> map.put(PlaceHolderComponent.LIKE, item.getDescription()));
        this.randomGiftItem(-14, -1).ifPresent(item -> map.put(PlaceHolderComponent.DISLIKE, item.getDescription()));
        this.randomGiftItem(Integer.MIN_VALUE, -15).ifPresent(item -> map.put(PlaceHolderComponent.HATE, item.getDescription()));
        return map;
    }

    public void handleDialogueAction(ServerPlayer sender, ConversationContext convCtx, String conversationID, int actionIdx) {
        NPCData.ConversationSet conversations = this.data.getConversation(convCtx);
        NPCData.Conversation conversation = conversations.conversations().get(conversationID);
        if (conversation != null && actionIdx < conversation.actions().size()) {
            NPCData.ConversationActionHolder action = conversation.actions().get(actionIdx);
            if (action != null) {
                switch (action.action()) {
                    case ANSWER -> {
                        NPCData.Conversation answer = conversations.conversations().get(action.actionValue());
                        if (answer != null) {
                            this.relationManager.getFriendPointData(sender.getUUID())
                                    .answer(conversationID, action.friendXP());
                            this.tellDialogue(sender, convCtx, action.actionValue(), answer);
                        }
                    }
                    case QUEST ->
                            SimpleQuestIntegration.INST().acceptQuestRandom(sender, this, new ResourceLocation(action.actionValue()));
                }
            }
        }
    }

    public void closedDialogue(ServerPlayer sender) {
        this.decreaseInteractingPlayers(sender);
    }

    public void closedQuestDialogue(ServerPlayer sender) {
        this.closedDialogue(sender);
        ResourceLocation quest = SimpleQuestIntegration.INST().questForExists(sender, this);
        if (quest != null && this.relationManager.questStateFor(sender.getUUID(), quest) == -2)
            SimpleQuestIntegration.INST().submit(sender, this);
    }

    public void resetQuestProcess(ServerPlayer player, ResourceLocation quest) {
        this.relationManager.resetQuest(player.getUUID(), quest);
    }

    public void completeNPCQuest(ServerPlayer player, NPCQuest quest) {
        this.relationManager.completeQuest(player.getUUID(), quest.getOriginID());
    }

    public boolean canAcceptNPCQuest(ServerPlayer player, NPCQuest quest) {
        return this.relationManager.getCompletedQuests(player.getUUID()).containsAll(quest.parentQuests);
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
        if (this.getServer() != null) {
            FamilyEntry family = FamilyHandler.get(this.getServer())
                    .getOrCreateEntry(this);
            if (family.getPartner() != null && family.hasPlayerRelationShip())
                return this.level.getServer().getPlayerList().getPlayer(family.getPartner());
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
    public boolean doHurtTarget(Entity entity) {
        return attack(this, entity);
    }

    public static boolean attack(LivingEntity attacker, Entity target) {
        ItemStack stack = attacker.getMainHandItem();
        CustomDamage.Builder source = new CustomDamage.Builder(attacker).hurtResistant(0).element(ItemNBT.getElement(stack));
        return CombatUtils.mobAttack(attacker, target, source);
    }

    @Override
    public LevelExpPair level() {
        return this.levelPair;
    }

    @Override
    public void setLevel(int level) {
        this.level().setLevel(Mth.clamp(level, 1, LibConstants.MAX_MONSTER_LEVEL), LevelCalc::xpAmountForLevelUp);
        this.updateStatsToLevel();
    }

    public void increaseLevel() {
        this.level().setLevel(Mth.clamp(this.level().getLevel() + 1, 1, LibConstants.MAX_MONSTER_LEVEL), LevelCalc::xpAmountForLevelUp);
        this.updateStatsToLevel();
    }

    public void addXp(float amount) {
        boolean res = this.level().addXP(amount, LibConstants.MAX_MONSTER_LEVEL, LevelCalc::xpAmountForLevelUp, () -> {
        });
        Platform.INSTANCE.sendToTrackingAndSelf(S2CEntityLevelPkt.create(this), this);
        if (res)
            this.updateStatsToLevel();
    }

    public void updateStatsToLevel() {
        if (!this.level.isClientSide)
            Platform.INSTANCE.sendToTrackingAndSelf(S2CEntityLevelPkt.create(this), this);
        float preHealthDiff = this.getMaxHealth() - this.getHealth();
        ((AttributeMapAccessor) this.getAttributes()).getAttributes().forEach((att, inst) -> inst.removeModifier(LibConstants.ATTRIBUTE_LEVEL_MOD));
        if (this.data != null) {
            Map<Attribute, Double> gain = this.data.statIncrease() != null ? this.data.statIncrease() : NPCData.DEFAULT_GAIN;
            gain.forEach((att, val) -> {
                val *= 0.01;
                AttributeInstance inst = this.getAttribute(att);
                if (inst != null) {
                    float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
                    inst.addPermanentModifier(new AttributeModifier(LibConstants.ATTRIBUTE_LEVEL_MOD, RuneCraftory.MODID + ".levelMod", (this.level().getLevel() - 1) * val * multiplier, AttributeModifier.Operation.ADDITION));
                    if (att == Attributes.MAX_HEALTH)
                        this.setHealth(this.getMaxHealth() - preHealthDiff);
                }
            });
            return;
        }
        int levelOffset = 1;
        AttributeInstance inst = this.getAttribute(Attributes.MAX_HEALTH);
        if (inst != null) {
            float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.ATTRIBUTE_LEVEL_MOD, RuneCraftory.MODID + ".levelMod", (this.level().getLevel() - levelOffset) * MobConfig.NPC_HEALTH_GAIN * multiplier, AttributeModifier.Operation.ADDITION));
            this.setHealth(this.getMaxHealth() - preHealthDiff);
        }
        inst = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (inst != null) {
            float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.ATTRIBUTE_LEVEL_MOD, RuneCraftory.MODID + ".levelMod", (this.level().getLevel() - levelOffset) * MobConfig.NPC_ATTACK_GAIN * multiplier, AttributeModifier.Operation.ADDITION));
        }
        inst = this.getAttribute(ModAttributes.DEFENCE.get());
        if (inst != null) {
            float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.ATTRIBUTE_LEVEL_MOD, RuneCraftory.MODID + ".levelMod", (this.level().getLevel() - levelOffset) * MobConfig.NPC_DEFENCE_GAIN * multiplier, AttributeModifier.Operation.ADDITION));
        }
        inst = this.getAttribute(ModAttributes.MAGIC.get());
        if (inst != null) {
            float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.ATTRIBUTE_LEVEL_MOD, RuneCraftory.MODID + ".levelMod", (this.level().getLevel() - levelOffset) * MobConfig.NPC_MAGIC_ATTACK_GAIN * multiplier, AttributeModifier.Operation.ADDITION));
        }
        inst = this.getAttribute(ModAttributes.MAGIC_DEFENCE.get());
        if (inst != null) {
            float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.ATTRIBUTE_LEVEL_MOD, RuneCraftory.MODID + ".levelMod", (this.level().getLevel() - levelOffset) * MobConfig.NPC_MAGIC_DEFENCE_GAIN * multiplier, AttributeModifier.Operation.ADDITION));
        }
    }

    @Override
    public int friendPoints(UUID uuid) {
        return this.relationManager.getFriendPointData(uuid).points.getLevel();
    }

    public int talkCount(UUID uuid) {
        return this.relationManager.getFriendPointData(uuid).getTalkCount();
    }

    public void updateFriendPointsFrom(Player player, CompoundTag tag) {
        this.relationManager.getFriendPointData(player.getUUID()).load(tag);
    }

    @Override
    public int baseXP() {
        return 0;
    }

    @Override
    public int baseMoney() {
        return 0;
    }

    @Override
    public boolean applyFoodEffect(ItemStack stack) {
        if (this.level.isClientSide)
            return false;
        if (stack.getItem() == ModItems.OBJECT_X.get())
            ItemObjectX.applyEffect(this, stack);
        FoodProperties food = DataPackHandler.INSTANCE.foodManager().get(stack.getItem());
        if (food == null) {
            net.minecraft.world.food.FoodProperties mcFood = stack.getItem().getFoodProperties();
            this.eat(this.level, stack);
            if (mcFood != null) {
                this.heal(mcFood.getNutrition() * 0.5f);
                return true;
            }
            return false;
        }
        if (food.duration() > 0)
            this.removeFoodEffect();
        this.eat(this.level, stack);
        Pair<Map<Attribute, Double>, Map<Attribute, Double>> foodStats = ItemNBT.foodStats(stack);
        for (Map.Entry<Attribute, Double> entry : foodStats.getSecond().entrySet()) {
            AttributeInstance inst = this.getAttribute(entry.getKey());
            if (inst == null)
                continue;
            inst.removeModifier(LibConstants.FOOD_UUID_MULTI);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.FOOD_UUID_MULTI, "foodBuffMulti_" + entry.getKey().getDescriptionId(), entry.getValue(), AttributeModifier.Operation.MULTIPLY_BASE));
        }
        for (Map.Entry<Attribute, Double> entry : foodStats.getFirst().entrySet()) {
            AttributeInstance inst = this.getAttribute(entry.getKey());
            if (inst == null)
                continue;
            inst.removeModifier(LibConstants.FOOD_UUID);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.FOOD_UUID, "foodBuff_" + entry.getKey().getDescriptionId(), entry.getValue(), AttributeModifier.Operation.ADDITION));
        }
        if (food.duration() > 0)
            this.foodBuffTick = food.duration();
        EntityUtils.foodHealing(this, food.getHPGain());
        EntityUtils.foodHealing(this, this.getMaxHealth() * food.getHpPercentGain() * 0.01F);
        if (food.potionHeals() != null)
            for (MobEffect s : food.potionHeals()) {
                this.removeEffect(s);
            }
        if (food.potionApply() != null)
            for (SimpleEffect s : food.potionApply()) {
                this.addEffect(s.create());
            }
        return true;
    }

    @Override
    public void removeFoodEffect() {
        ((AttributeMapAccessor) this.getAttributes())
                .getAttributes().values().forEach(inst -> {
                    inst.removeModifier(LibConstants.FOOD_UUID);
                    inst.removeModifier(LibConstants.FOOD_UUID_MULTI);
                });
    }

    @Override
    public boolean onGivingItem(Player player, ItemStack stack) {
        this.giftItem(player, stack);
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void die(DamageSource cause) {
        RuneCraftory.LOGGER.info("NPC {} died, message: '{}'", this, cause.getLocalizedDeathMessage(this).getString());
        if (!this.level.isClientSide) {
            this.getAnimationHandler().setAnimation(null);
        }
        super.die(cause);
    }

    @Override
    public void setLevelCallback(EntityInLevelCallback levelCallback) {
        super.setLevelCallback(WorldUtils.wrappedCallbackFor(this, this::followEntity, levelCallback));
    }

    @Override
    public void remove(RemovalReason reason) {
        this.releasePOI(this.getBedPos());
        this.releasePOI(this.getWorkPlace());
        this.releasePOI(this.getMeetingPos());
        super.remove(reason);
        if (this.getServer() != null) {
            NPCHandler handler = WorldHandler.get(this.getServer()).npcHandler;
            if (reason.shouldDestroy() && this.data != null && this.data.unique() > 0)
                handler.removeUniqueNPC(this.getUUID(), this.data);
            handler.removeNPC(this, reason);
        }
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
    }

    public boolean playDeath() {
        return this.entityData.get(PLAY_DEATH_STATE);
    }

    public void setPlayDeath(boolean flag) {
        this.entityData.set(PLAY_DEATH_STATE, flag);
        if (flag) {
            Player partner;
            if (!this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)
                    && this.followEntity() == null && (partner = this.getPartner()) instanceof ServerPlayer)
                partner.sendMessage(this.getKnockoutMessage(), Util.NIL_UUID);
            this.level.getEntities(EntityTypeTest.forClass(Mob.class), this.getBoundingBox().inflate(32), e -> this.equals(e.getTarget()))
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
        if (source instanceof EntityDamageSource && source.getEntity() != null)
            return new TranslatableComponent("runecraftory.tamed.monster.knockout.by", this.getDisplayName(), this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ(), source.getEntity().getDisplayName());
        return new TranslatableComponent("runecraftory.tamed.monster.knockout", this.getDisplayName(), this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ());
    }

    public int getPlayDeathTick() {
        return this.playDeathTick;
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return super.canBeSeenAsEnemy() && !this.playDeath();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.playDeath() && source != DamageSource.OUT_OF_WORLD) {
            return false;
        }
        if (this.followEntity() != null && source.getEntity() != null) {
            Player follow = this.followEntity();
            if (follow.equals(source.getEntity()) || Platform.INSTANCE.getPlayerData(follow).map(d -> d.party.isPartyMember(source.getEntity())).orElse(false))
                return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (damageSrc != DamageSource.OUT_OF_WORLD) {
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
    protected boolean isImmobile() {
        return super.isImmobile() || this.isVehicle() || this.playDeath();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("MobLevel", this.level().save());
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

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        if (!this.ignoreInit)
            this.randomizeData(null, true);
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    public void handleAttack(AnimatedAction anim) {
        this.getNavigation().stop();
        if (anim.getTick() == 1 && this.getTarget() != null)
            this.lookAt(this.getTarget(), 360, 90);
    }

    public void npcAttack(Consumer<LivingEntity> cons) {
        this.attackableEntites().forEach(cons);
    }

    public List<LivingEntity> attackableEntites() {
        ItemStack held = this.getMainHandItem();
        if (held.getItem() instanceof IAOEWeapon weapon) {
            return CombatUtils.EntityAttack.create(this, CombatUtils.EntityAttack.circleTargets(this.getLookAngle(), Math.max(0, weapon.getFOV(this, held) - 20), 0))
                    .withTargetPredicate(this.hitPred)
                    .executeAttack();
        }
        LivingEntity target = this.getTarget();
        if (target == null)
            return List.of();
        double range = this.getMeleeAttackRangeSqr(target);
        if (this.distanceToSqr(target.getX(), target.getY(), target.getZ()) <= range)
            return List.of(target);
        return List.of();
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity target) {
        double reachSqr;
        ItemStack held = this.getMainHandItem();
        if (held.getItem() instanceof IAOEWeapon) {
            reachSqr = this.getAttributeValue(ModAttributes.ATTACK_RANGE.get()) - 0.3 + target.getBbWidth() * 0.5;
            reachSqr = reachSqr * reachSqr;
        } else if (held.getItem() instanceof IExtendedWeapon weapon) {
            float weaponRange = weapon.getRange(this, held);
            reachSqr = weaponRange + target.getBbWidth() * 0.5;
            reachSqr = reachSqr * reachSqr;
        } else
            reachSqr = super.getMeleeAttackRangeSqr(target);
        return reachSqr;
    }

    @Override
    public void startSleeping(BlockPos pos) {
        if (this.sleepCooldown <= 0)
            super.startSleeping(pos);
    }

    @Override
    public void stopSleeping() {
        super.stopSleeping();
        this.sleepCooldown = 60;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public AnimationHandler<?> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean hasRestriction() {
        return super.hasRestriction() && this.getEntityToFollowUUID() == null;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        SizeFeatureType.SizeFeature feat = this.lookFeatures.getFeature(ModNPCLooks.SIZE.get());
        if (feat != null)
            return super.getDimensions(pose).scale(feat.size);
        return super.getDimensions(pose);
    }

    public NPCJob getShop() {
        return this.shop;
    }

    public void setShop(NPCJob shop) {
        this.shop = shop;
        if (!this.level.isClientSide)
            this.entityData.set(SHOP_SYNC, ModNPCJobs.getSyncIDFrom(shop));
    }

    public boolean isShopDefined() {
        return !this.data.profession().isEmpty();
    }

    public boolean updateActivity() {
        if (this.tickCount % 20 == 0 && this.level instanceof ServerLevel serverLevel && this.interactingPlayers.isEmpty()) {
            Activity prev = this.activity;
            this.activity = this.getActivityForTime(serverLevel);
            if (this.activity == ModActivities.EARLYIDLE.get() && this.getBedPos() != null && this.getBedPos().dimension() == this.level.dimension()) {
                if (!this.getRestrictCenter().equals(this.getBedPos().pos())) {
                    this.prevRestriction = this.getRestrictCenter();
                    this.prevRestrictionRadius = (int) this.getRestrictRadius();
                    this.restrictTo(this.getBedPos().pos(), 10);
                }
            } else if (this.prevRestrictionRadius >= 0) {
                this.restrictTo(this.prevRestriction, this.prevRestrictionRadius);
                this.prevRestriction = BlockPos.ZERO;
                this.prevRestrictionRadius = -1;
            }
            return prev != this.activity;
        }
        return false;
    }

    public void syncActivity(CompoundTag tag) {
        if (this.level.isClientSide)
            this.schedule.load(tag);
    }

    public Activity getActivity() {
        return this.activity;
    }

    public Activity getActivityForTime(ServerLevel serverLevel) {
        return this.schedule.getActivity(serverLevel);
    }

    public NPCSchedule getSchedule() {
        return this.schedule;
    }

    public GlobalPos getWorkPlace() {
        return this.getBrain().getMemory(MemoryModuleType.JOB_SITE).orElse(null);
    }

    public void setWorkPlace(GlobalPos pos) {
        if (pos != null)
            this.level.broadcastEntityEvent(this, (byte) 15);
        this.getBrain().setMemory(MemoryModuleType.JOB_SITE, pos);
    }

    public GlobalPos getBedPos() {
        return this.getBrain().getMemory(MemoryModuleType.HOME).orElse(null);
    }

    public void setBedPos(GlobalPos pos) {
        if (pos != null)
            this.level.broadcastEntityEvent(this, (byte) 14);
        this.getBrain().setMemory(MemoryModuleType.HOME, pos);
    }

    public GlobalPos getMeetingPos() {
        return this.getBrain().getMemory(MemoryModuleType.MEETING_POINT).orElse(null);
    }

    public void setMeetingPos(GlobalPos pos) {
        this.getBrain().setMemory(MemoryModuleType.MEETING_POINT, pos);
    }

    public ShopState canTrade() {
        if (this.isBaby() || (!this.shop.hasShop && !this.shop.hasWorkSchedule))
            return ShopState.NOTWORKER;
        if (!this.shop.hasWorkSchedule)
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
            if (this.level.dimension() != pos.dimension())
                return false;
            return pos.pos().closerToCenterThan(this.position(), range);
        }
        return false;
    }

    public void releasePOI(GlobalPos globalPos) {
        if (globalPos == null)
            return;
        ServerLevel serverLevel = globalPos.dimension() != this.level.dimension() ? this.level.getServer().getLevel(globalPos.dimension()) : (ServerLevel) this.level;
        if (serverLevel == null) {
            return;
        }
        PoiManager poiManager = serverLevel.getPoiManager();
        if (poiManager.exists(globalPos.pos(), p -> true)) {
            poiManager.release(globalPos.pos());
            DebugPackets.sendPoiTicketCountPacket(serverLevel, globalPos.pos());
        }
    }

    public Player followEntity() {
        if (this.entityToFollowUUID != null) {
            if (this.entityToFollow == null || !this.entityToFollow.isAlive()) {
                if (this.level.isClientSide)
                    this.entityToFollow = this.level.getPlayerByUUID(this.entityToFollowUUID);
                else
                    this.entityToFollow = this.level.getServer().getPlayerList().getPlayer(this.entityToFollowUUID);
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

    public void setBehaviour(Behaviour behaviour) {
        this.entityData.set(BEHAVIOUR_DATA, behaviour.ordinal());
        this.behaviour = behaviour;
        if (!this.level.isClientSide)
            this.onSetBehaviour();
    }

    private void onSetBehaviour() {
        if (this.behaviourState().following) {
            if (this.followEntity() != null)
                Platform.INSTANCE.getPlayerData(this.followEntity()).ifPresent(d -> d.party.addPartyMember(this));
        } else {
            if (this.followEntity() != null)
                Platform.INSTANCE.getPlayerData(this.followEntity()).ifPresent(d -> d.party.removePartyMember(this));
            this.setTarget(null);
        }
        this.getNavigation().stop();
    }

    public Behaviour behaviourState() {
        return this.behaviour;
    }

    public boolean isStaying() {
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
        return this.interactingPlayers.get(this.interactingPlayers.size() - 1);
    }

    public boolean procreateWith(Entity other) {
        FamilyEntry family = this.getFamily();
        if (family == null || !family.getPartner().equals(other.getUUID()) || this.procreationProgress > 0)
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

    public void tryUpdateName(Component component) {
        if (this.data.name() == null)
            this.setCustomName(component);
    }

    public boolean hasDataName() {
        return this.data.name() != null;
    }

    public boolean canProcreate() {
        return this.procreationCooldown < 0;
    }

    private boolean spawnBaby() {
        if (!(this.level instanceof ServerLevel serverLevel))
            return false;
        EntityNPCBase baby = ModEntities.NPC.get().create(serverLevel, null, null, null, this.blockPosition(), MobSpawnType.BREEDING, false, false);
        if (baby == null)
            return false;
        baby.setBaby(true);
        List<ResourceLocation> childIDs = new ArrayList<>(this.data.possibleChildren());
        NPCDataManager manager = DataPackHandler.INSTANCE.npcDataManager();
        if (this.procreationEntity instanceof EntityNPCBase npc) {
            childIDs.addAll(npc.data.possibleChildren());
        }
        childIDs.removeIf(r -> !manager.has(r));
        if (childIDs.isEmpty()) {
            baby.randomizeData(null, true);
        } else {
            baby.setLevel(1);
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

    public boolean isMale() {
        return this.entityData.get(MALE);
    }

    public void setMale(boolean flag) {
        this.entityData.set(MALE, flag);
    }

    public NPCData.NPCLook getLook() {
        if (this.look == null) {
            if (this.data == NPCData.DEFAULT_DATA)
                this.look = NPCData.NPCLook.DEFAULT_LOOK;
            else {
                List<ResourceLocation> looks = this.data.look() == null ? List.of() : this.data.look().stream().filter(e -> e.gender() == NPCData.Gender.UNDEFINED || (e.gender() == NPCData.Gender.MALE) == this.isMale())
                        .map(NPCData.NPCLookId::id).toList();
                if (!looks.isEmpty())
                    this.look = DataPackHandler.INSTANCE.npcLookManager().get(looks.get(this.random.nextInt(looks.size())));
                else
                    this.look = DataPackHandler.INSTANCE.npcLookManager().getRandom(this.random, this.isMale());
            }
        }
        return this.look;
    }

    public NPCAttackActions getAttackActions() {
        if (this.attackActions == null) {
            if (this.data == NPCData.DEFAULT_DATA)
                this.attackActions = NPCAttackActions.DEFAULT;
            else {
                List<ResourceLocation> actions = this.data.combatActions();
                this.attackActions = DataPackHandler.INSTANCE.npcActionsManager().get(actions.isEmpty() ? null : actions.get(this.random.nextInt(actions.size())));
            }
        }
        return this.attackActions;
    }

    public Pair<EnumSeason, Integer> getBirthday() {
        if (this.birthday == null) {
            if (this.data == NPCData.DEFAULT_DATA)
                this.birthday = Pair.of(EnumSeason.SPRING, 1);
            else if (this.data.birthday() != null)
                this.birthday = this.data.birthday();
            else {
                EnumSeason randSeason = EnumSeason.values()[this.random.nextInt(EnumSeason.values().length)];
                int day = this.random.nextInt(30) + 1;
                this.birthday = Pair.of(randSeason, day);
            }
        }
        return this.birthday;
    }

    public void setClientLook(NPCData.NPCLook look) {
        if (this.level.isClientSide) {
            this.look = look;
            this.refreshDimensions();
        }
    }

    public NPCData.Gift giftOf(ItemStack stack) {
        for (Map.Entry<String, NPCData.Gift> e : this.data.giftItems().entrySet()) {
            TagKey<Item> tag = e.getValue().item() == null ? this.gift.computeIfAbsent(e.getKey(), s -> DataPackHandler.INSTANCE.giftManager().getRandomGift(NPCData.GiftType.ofXP(e.getValue().xp()), this.random)) : e.getValue().item();
            if (tag == null || stack.is(tag))
                return e.getValue();
        }
        return null;
    }

    public Optional<Item> randomGiftItem(int min, int max) {
        for (Map.Entry<String, NPCData.Gift> e : this.data.giftItems().entrySet()) {
            if (min > e.getValue().xp() || e.getValue().xp() < max)
                continue;
            TagKey<Item> tag = e.getValue().item() == null ? this.gift.computeIfAbsent(e.getKey(), s -> DataPackHandler.INSTANCE.giftManager().getRandomGift(NPCData.GiftType.ofXP(e.getValue().xp()), this.random)) : e.getValue().item();
            if (tag != null)
                return Registry.ITEM.getTag(tag).map(n -> {
                    List<Item> items = n.stream().map(Holder::value).toList();
                    if (items.isEmpty())
                        return null;
                    return items.get(this.updater.getDailyRandom().nextInt(items.size()));
                });
        }
        return Optional.empty();
    }

    public void openShopForPlayer(ServerPlayer player) {
        if (this.canTrade() == ShopState.OPEN) {
            this.interactWithPlayer(player);
            Platform.INSTANCE.getPlayerData(player).map(d -> {
                if (EntityNPCBase.this.getShop().hasShop) {
                    return d.getShop(this.getShop());
                }
                return null;
            }).ifPresent(shopList -> Platform.INSTANCE.openGuiMenu(player, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return new TranslatableComponent(EntityNPCBase.this.getShop().getTranslationKey());
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                    return new ContainerShop(i, inventory, new InventoryShop(EntityNPCBase.this, shopList));
                }
            }, buf -> {
                buf.writeInt(EntityNPCBase.this.getId());
                buf.writeInt(shopList.size());
                shopList.forEach(s -> buf.writeWithCodec(ItemStack.CODEC, s));
            }));
        }
    }

    public void randomizeData(@Nullable ResourceLocation job) {
        this.randomizeData(ModNPCJobs.getFromID(job), false);
    }

    public void randomizeData(NPCJob job, boolean overwrite) {
        if (this.getServer() != null) {
            this.setNPCData(DataPackHandler.INSTANCE.npcDataManager().getRandom(this.random, d ->
                    (d.profession().isEmpty() || d.profession().stream().anyMatch(j -> j.equals(job)))
                            && WorldHandler.get(this.getServer()).npcHandler.canAssignNPC(d), job == null ? null :
                    d -> d.profession().stream().anyMatch(j -> j.equals(job))), !overwrite);
            if (job != null)
                this.setShop(job);
        }
    }

    public ResourceLocation getDataID() {
        return DataPackHandler.INSTANCE.npcDataManager().getId(this.data);
    }

    public void setNPCData(NPCData data, boolean load) {
        if (this.getServer() != null) {
            if (this.data != null)
                WorldHandler.get(this.getServer()).npcHandler.removeUniqueNPC(this.getUUID(), this.data);
            WorldHandler.get(this.getServer()).npcHandler.addUniqueNPC(this.getUUID(), data);
        }
        this.data = data;
        this.dataRandom.setSeed(this.getUUID().hashCode());
        if (!load) {
            this.setShop(!this.data.profession().isEmpty() ? this.data.profession().get(this.dataRandom.nextInt(this.data.profession().size())) : ModNPCJobs.getRandomJob(this.random));
            this.setMale(this.data.gender() == NPCData.Gender.UNDEFINED ? this.random.nextBoolean() : this.data.gender() != NPCData.Gender.FEMALE);
            if (this.data.name() == null) {
                String name = DataPackHandler.INSTANCE.nameManager().getRandomFullName(this.random, this.isMale());
                if (name != null) {
                    this.setCustomName(new TextComponent(name));
                }
            } else {
                String name = this.data.name();
                if (this.data.surname() != null)
                    name += " " + this.data.surname();
                this.setCustomName(new TextComponent(name));
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
            this.lookFeatures.buildFromLooks(this, this.look.additionalFeatures().values());
        } else {
            // Apply non null things else
            if (this.data.look() != null && !this.data.look().isEmpty()) {
                this.look = null;
                this.getLook();
            }
            if (!this.data.profession().isEmpty() && !this.data.profession().contains(this.getShop()))
                this.setShop(!this.data.profession().isEmpty() ? this.data.profession().get(this.dataRandom.nextInt(this.data.profession().size())) : ModNPCJobs.getRandomJob(this.random));
            if (this.data.gender() != NPCData.Gender.UNDEFINED && (this.data.gender() == NPCData.Gender.MALE) != this.isMale())
                this.setMale(this.data.gender() == NPCData.Gender.UNDEFINED ? this.random.nextBoolean() : this.data.gender() != NPCData.Gender.FEMALE);
            if (this.data.name() != null) {
                String name = this.data.name();
                if (this.data.surname() != null)
                    name += " " + this.data.surname();
                this.setCustomName(new TextComponent(name));
            }
            if (this.data.birthday() != null) {
                this.birthday = null;
                this.getBirthday();
            }
            if (this.data.combatActions() != null && !this.data.combatActions().isEmpty())
                this.attackActions = null;
            if (data.schedule() != null)
                this.schedule.with(data.schedule());
        }
        this.applyAttributes(!load);
        if (this.level().getLevel() < this.data.baseLevel()) {
            this.setLevel(this.data.baseLevel());
        }
        this.refreshDimensions();
        if (!this.level.isClientSide)
            Platform.INSTANCE.sendToTrackingAndSelf(new S2CNPCLook(this.getId(), this.look, this.lookFeatures), this);
    }

    private CompoundTag saveNPCData() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Data", DataPackHandler.INSTANCE.npcDataManager().getId(this.data).toString());
        tag.putString("Look", DataPackHandler.INSTANCE.npcLookManager().getId(this.getLook()).toString());
        tag.putString("Profession", ModNPCJobs.getIDFrom(this.getShop()).toString());
        tag.putBoolean("Male", this.isMale());
        tag.putInt("BirthdayMonth", this.getBirthday().getFirst().ordinal());
        tag.putInt("Birthday", this.getBirthday().getSecond());
        tag.putString("Combat", DataPackHandler.INSTANCE.npcActionsManager().getId(this.getAttackActions()).toString());
        tag.put("Schedule", this.schedule.save());
        tag.put("LookFeatures", this.lookFeatures.save());
        return tag;
    }

    private void loadNpcData(CompoundTag tag) {
        NPCData data = DataPackHandler.INSTANCE.npcDataManager().get(new ResourceLocation(tag.getString("Data")));
        this.look = DataPackHandler.INSTANCE.npcLookManager().get(new ResourceLocation(tag.getString("Look")));
        this.setShop(ModNPCJobs.getFromID(new ResourceLocation(tag.getString("Profession"))));
        this.setMale(tag.getBoolean("Male"));
        try {
            EnumSeason month = EnumSeason.values()[tag.getInt("BirthdayMonth")];
            this.birthday = Pair.of(month, tag.getInt("Birthday"));
        } catch (IllegalArgumentException e) {
            this.getBirthday();
        }
        this.attackActions = DataPackHandler.INSTANCE.npcActionsManager().get(new ResourceLocation(tag.getString("Combat")));
        this.schedule.load(tag.getCompound("Schedule"));
        this.lookFeatures.read(tag.getCompound("LookFeatures"));
        this.setNPCData(data, true);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        Platform.INSTANCE.sendToClient(new S2CNPCLook(this.getId(), this.look, this.lookFeatures), player);
        Platform.INSTANCE.sendToClient(S2CEntityLevelPkt.create(this), player);
    }

    public enum Behaviour {

        WANDER("npc.interact.home", false),
        FOLLOW("npc.interact.follow", true),
        FOLLOW_DISTANCE("npc.interact.follow.distance", true),
        STAY("npc.interact.stay", true);

        public final String interactKey;

        public final boolean following;

        Behaviour(String interactKey, boolean following) {
            this.interactKey = interactKey;
            this.following = following;
        }
    }
}
