package io.github.flemmli97.runecraftory.common.entities;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.EntityProperties;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.SimpleEffect;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.ai.FollowOwnerGoalMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.HurtByTargetPredicate;
import io.github.flemmli97.runecraftory.common.entities.ai.LookAtAliveGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.RandomLookGoalAlive;
import io.github.flemmli97.runecraftory.common.entities.ai.RestrictedWaterAvoidingStrollGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.RiderAttackTargetGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.StayGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.TendCropsGoal;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemObjectX;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.loot.LootCtxParameters;
import io.github.flemmli97.runecraftory.common.network.S2CAttackDebug;
import io.github.flemmli97.runecraftory.common.network.S2CEntityLevelPkt;
import io.github.flemmli97.runecraftory.common.network.S2COpenCompanionGui;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.spells.TeleportSpell;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.TeleportUtils;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.BarnData;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.integration.simplequest.SimpleQuestIntegration;
import io.github.flemmli97.runecraftory.mixin.AttributeMapAccessor;
import io.github.flemmli97.runecraftory.mixin.CombatTrackerAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
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
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class BaseMonster extends PathfinderMob implements Enemy, IAnimated, IExtendedMob, RandomAttackSelectorMob, ExtendedEntity, SleepingEntity, TargetableOpponent {

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
    public final Predicate<LivingEntity> defendPred = (e) -> {
        if (e != this) {
            if (this.getControllingPassenger() instanceof Player)
                return false;
            if (this.isTamed())
                return !e.getUUID().equals(this.getOwnerUUID()) && !this.getOwnerUUID().equals(EntityUtils.tryGetOwner(e));
            return true;
        }
        return false;
    };
    private final EntityProperties prop;
    public NearestAttackableTargetGoal<Player> targetPlayer = this.createTargetGoalPlayer();//|| player != BaseMonster.this.getOwner());
    public NearestAttackableTargetGoal<Mob> targetMobs = this.createTargetGoalMobs();
    public FloatGoal swimGoal = new FloatGoal(this);
    public FollowOwnerGoalMonster followOwnerGoal = new FollowOwnerGoalMonster(this, 1.05, 9, 2, 20);
    public RandomStrollGoal wander = new RestrictedWaterAvoidingStrollGoal(this, 1.0);
    public HurtByTargetPredicate hurt = new HurtByTargetPredicate(this, this.defendPred);

    public TendCropsGoal farm = new TendCropsGoal(this);
    protected Vec3 targetPosition;
    private BlockPos seedInventory, cropInventory;

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

    private int playDeathTick;

    private final LevelExpPair levelPair = new LevelExpPair();

    protected int tamingTick = -1;
    //These 2 values are not getting saved intentionally to prevent stuff like player dying or running away
    private int brushCount, loveAttCount;
    private Runnable delayedTaming;
    protected int feedTimeOut;
    private boolean doJumping = false;
    private int foodBuffTick;
    private Player owner;

    private int tpCooldown;
    private Behaviour behaviour = Behaviour.WANDER;

    private final LevelExpPair friendlyPoints = new LevelExpPair();

    private final DailyMonsterUpdater updater = new DailyMonsterUpdater(this);

    private BarnData assignedBarn;

    /**
     * For movement animation interpolation
     */
    private int moveTick;
    private final int moveRandom = this.random.nextInt(10);

    public static final int MOVE_TICK_MAX = 3;

    public BaseMonster(EntityType<? extends BaseMonster> type, Level level) {
        super(type, level);
        this.moveControl = new NewMoveController(this);
        //Client will get default value. This is intentional
        this.prop = DataPackHandler.INSTANCE.monsterPropertiesManager().getPropertiesFor(type);
        this.applyAttributes();
        if (!level.isClientSide) {
            this.addGoal();
        }
    }

    public static AttributeSupplier.Builder createAttributes(Collection<? extends RegistryEntrySupplier<Attribute>> atts) {
        AttributeSupplier.Builder map = Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.23)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
        if (atts != null)
            for (RegistryEntrySupplier<Attribute> att : atts)
                map.add(att.get());
        return map;
    }

    //private Map<Attribute, Integer> attributeRandomizer = new HashMap<>();

    protected void applyAttributes() {
        for (Map.Entry<Attribute, Double> att : this.prop.getBaseValues().entrySet()) {
            AttributeInstance inst = this.getAttribute(att.getKey());
            if (inst != null) {
                inst.setBaseValue(att.getValue());
                if (att.getKey() == Attributes.MAX_HEALTH)
                    this.setHealth(this.getMaxHealth());
            }
        }
    }

    public void addGoal() {
        this.targetSelector.addGoal(1, this.targetPlayer);
        this.targetSelector.addGoal(2, this.targetMobs);
        this.targetSelector.addGoal(0, this.hurt);
        this.targetSelector.addGoal(3, new RiderAttackTargetGoal(this, 15));

        this.goalSelector.addGoal(0, this.swimGoal);
        this.goalSelector.addGoal(0, new StayGoal<>(this, StayGoal.CANSTAYMONSTER));
        this.goalSelector.addGoal(1, this.followOwnerGoal);
        this.goalSelector.addGoal(2, new LookAtAliveGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1));
        this.goalSelector.addGoal(6, this.wander);
        this.goalSelector.addGoal(7, new RandomLookGoalAlive(this));
    }

    @Override
    protected void updateControlFlags() {
        boolean flag1 = !(this.getVehicle() instanceof Boat);
        this.goalSelector.setControlFlag(Goal.Flag.MOVE, true);
        this.goalSelector.setControlFlag(Goal.Flag.JUMP, flag1);
        this.goalSelector.setControlFlag(Goal.Flag.LOOK, true);
    }

    private void updateAI(boolean forced, boolean load) {
        if (forced || this.isTamed()) {
            this.getNavigation().stop();
            if (this.behaviourState() != Behaviour.FARM) {
                this.seedInventory = null;
                this.cropInventory = null;
                if (this.behaviourState() != Behaviour.STAY) {
                    this.targetSelector.addGoal(1, this.targetPlayer);
                    this.targetSelector.addGoal(2, this.targetMobs);
                    this.targetSelector.addGoal(0, this.hurt);
                }
                this.goalSelector.removeGoal(this.farm);
                if (this.level instanceof ServerLevel serverLevel)
                    FarmlandHandler.get(serverLevel.getServer()).removeIrrigationPOI(serverLevel, this.getUUID());
            }
            this.wander.setInterval(120);
            switch (this.behaviourState()) {
                case WANDER_HOME -> {
                    if (this.getOwner() != null) {
                        if (this.findNearestBarn(load)) {
                            this.restrictToBasedOnBehaviour(null, load);
                            BlockPos pos = this.assignedBarn.pos.pos();
                            if (this.level.dimension() == this.assignedBarn.pos.dimension())
                                TeleportSpell.safeTeleportTo(this, pos.getX(), pos.getY(), pos.getZ());
                            else {
                                ServerLevel serverLevel = this.getServer().getLevel(this.assignedBarn.pos.dimension());
                                if (serverLevel != null)
                                    TeleportSpell.changeDimension(this, serverLevel, pos.getX(), pos.getY(), pos.getZ());
                            }
                        } else {
                            if (this.tickCount > 20)
                                this.getOwner().sendMessage(new TranslatableComponent("runecraftory.monster.interact.barn.no.ext", this.getDisplayName(), this.blockPosition()), Util.NIL_UUID);
                            this.setBehaviour(Behaviour.WANDER);
                        }
                        this.goalSelector.addGoal(6, this.wander);
                        this.wander.setInterval(40);
                        Platform.INSTANCE.getPlayerData(this.getOwner()).ifPresent(d -> d.party.removePartyMember(this));
                    }
                }
                case FOLLOW -> {
                    boolean party = this.getOwner() == null || Platform.INSTANCE.getPlayerData(this.getOwner())
                            .map(d -> !d.party.isPartyFull() || d.party.isPartyMember(this)).orElse(false);
                    if (party) {
                        this.clearRestriction();
                        this.goalSelector.removeGoal(this.wander);
                        if (this.getOwner() != null)
                            Platform.INSTANCE.getPlayerData(this.getOwner()).ifPresent(d -> d.party.addPartyMember(this));
                    }
                }
                case FOLLOW_DISTANCE -> {
                    this.clearRestriction();
                    this.goalSelector.removeGoal(this.wander);
                    if (this.getOwner() != null)
                        Platform.INSTANCE.getPlayerData(this.getOwner()).ifPresent(d -> d.party.addPartyMember(this));
                }
                case STAY -> {
                    this.goalSelector.addGoal(6, this.wander);
                    this.targetSelector.removeGoal(this.targetPlayer);
                    this.targetSelector.removeGoal(this.targetMobs);
                    this.targetSelector.removeGoal(this.hurt);
                    if (this.getOwner() != null)
                        Platform.INSTANCE.getPlayerData(this.getOwner()).ifPresent(d -> d.party.addPartyMember(this));
                }
                case WANDER -> {
                    this.restrictToBasedOnBehaviour(this.blockPosition(), load);
                    this.goalSelector.addGoal(6, this.wander);
                    if (this.getOwner() != null)
                        Platform.INSTANCE.getPlayerData(this.getOwner()).ifPresent(d -> d.party.removePartyMember(this));
                }
                case FARM -> {
                    this.restrictToBasedOnBehaviour(this.blockPosition(), load);
                    this.goalSelector.addGoal(3, this.farm);

                    this.targetSelector.removeGoal(this.targetPlayer);
                    this.targetSelector.removeGoal(this.targetMobs);
                    this.targetSelector.removeGoal(this.hurt);

                    this.goalSelector.removeGoal(this.wander);

                    BlockPos nearestInv = this.nearestBlockEntityWithInv();
                    this.setSeedInventory(nearestInv);
                    this.setCropInventory(nearestInv);
                    if (this.getOwner() != null)
                        Platform.INSTANCE.getPlayerData(this.getOwner()).ifPresent(d -> d.party.removePartyMember(this));
                }
            }
        }
    }

    public void restrictToBasedOnBehaviour(@Nullable BlockPos pos, boolean keepPos) {
        if (this.behaviourState() == Behaviour.WANDER_HOME && this.assignBarn()) {
            if (this.level.dimension() == this.assignedBarn.pos.dimension())
                this.restrictTo(this.assignedBarn.pos.pos(), this.assignedBarn.getSize() + 1);
        }
        if (pos == null)
            return;
        if (keepPos)
            pos = this.getRestrictCenter();
        if (this.behaviourState() == Behaviour.FARM) {
            this.restrictTo(pos, MobConfig.farmRadius + 3);
            if (this.level instanceof ServerLevel serverLevel)
                FarmlandHandler.get(serverLevel.getServer()).addIrrigationPOI(serverLevel, this.getUUID(), this.blockPosition());
        } else if (this.behaviourState() == Behaviour.WANDER)
            this.restrictTo(pos, 9);
    }

    private BlockPos nearestBlockEntityWithInv() {
        BlockPos blockPos = this.getRestrictCenter();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int radius = (int) this.getRestrictRadius();
        for (int y = -1; y < 2; y++)
            for (int x = -radius; x <= radius; x++)
                for (int z = -radius; z <= radius; z++) {
                    mutableBlockPos.setWithOffset(blockPos, x, y, z);
                    if (Platform.INSTANCE.matchingInventory(this.level.getBlockEntity(mutableBlockPos), s -> true)) {
                        return mutableBlockPos.immutable();
                    }
                }
        return null;
    }

    protected NearestAttackableTargetGoal<Player> createTargetGoalPlayer() {
        return new NearestAttackableTargetGoal<>(this, Player.class, 5, true, true, player -> !this.isTamed());
    }

    protected NearestAttackableTargetGoal<Mob> createTargetGoalMobs() {
        return new NearestAttackableTargetGoal<>(this, Mob.class, 5, true, true, this.targetPred);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(MOVE_FLAGS, (byte) 0);
        this.entityData.define(BEHAVIOUR_DATA, 0);
        this.entityData.define(PLAY_DEATH_STATE, false);
        this.entityData.define(FRIEND_POINTS_SYNC, 1);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (this.level.isClientSide) {
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

    //=====Client
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 10) {
            this.playTameEffect(true);
        } else if (id == 11) {
            this.playTameEffect(false);
        } else if (id == 34) {
            for (int i = 0; i < 5; ++i) {
                this.level.addParticle(ParticleTypes.ANGRY_VILLAGER, this.getX() - this.getBbWidth() * 0.25 + this.random.nextFloat() * this.getBbWidth() * 0.25f, this.getY() + this.getBbHeight() + this.random.nextFloat() * 0.3, this.getZ() - this.getBbWidth() * 0.25 + this.random.nextFloat() * this.getBbWidth() * 0.25f, 0, 0, 0);
            }
        } else if (id == 64) {
            this.level.addParticle(ParticleTypes.NOTE, true, this.getX(), this.getY() + this.getBbHeight() + 0.3, this.getZ(), 0, 0, 0);
        } else if (id == 65) {
            this.level.addParticle(ParticleTypes.HEART, true, this.getX(), this.getY() + this.getBbHeight() + 0.3, this.getZ(), 0, 0, 0);
        }
        super.handleEntityEvent(id);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getMoveFlag() != MoveType.NONE) {
            this.moveTick = Math.min(MOVE_TICK_MAX, ++this.moveTick);
        } else {
            this.moveTick = Math.max(0, --this.moveTick);
        }
        if (!this.level.isClientSide) {
            this.updater.tick();
            if (this.tamingTick > 0) {
                --this.tamingTick;
                if (this.getMoveFlag() != MoveType.NONE) {
                    this.setMovingFlag(MoveType.NONE);
                    this.setDeltaMovement(Vec3.ZERO);
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
            if (this.getAnimationHandler().getAnimation() == null)
                this.targetPosition = null;
            if (this.assignedBarn != null && this.assignedBarn.isInvalidFor(this))
                this.assignedBarn = null;
            if (this.isTamed()) {
                if (this.assignedBarn == null) {
                    if (MobConfig.monsterNeedBarn && this.behaviourState() != Behaviour.STAY)
                        this.setBehaviour(Behaviour.STAY);
                }
            }
        } else {
            if (!this.playDeath() && TendCropsGoal.cantTendToCropsAnymore(this) && this.behaviour == Behaviour.FARM && this.tickCount % 20 == 0)
                this.level.addParticle(ParticleTypes.ANGRY_VILLAGER, this.getX(), this.getY() + this.getBbHeight() + 0.3, this.getZ(), 0, 0, 0);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.getAnimationHandler().tick();

        boolean teleported = false;
        if (this.level instanceof ServerLevel serverLevel) {
            if (this.behaviourState().following && --this.tpCooldown <= 0) {
                Player owner = this.getOwner();
                if (owner != null) {
                    serverLevel.getChunkSource().addRegionTicket(WorldUtils.ENTITY_LOADER, this.chunkPosition(), 3, this.chunkPosition());
                    if (owner.level.dimension() != this.level.dimension()) {
                        TeleportUtils.safeDimensionTeleport(this, (ServerLevel) owner.level, owner.blockPosition());
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
            if (!this.level.isClientSide) {
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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("MobLevel", this.level().save());
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
        //CompoundTag genes = new CompoundTag();
        //this.attributeRandomizer.forEach((att, val)->genes.putInt(att.getRegistryName().toString(), val));
        //compound.put("Genes", genes);
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
                this.seedInventory = new BlockPos(arr[0], arr[1], arr[2]);
        }
        if (compound.contains("CropInventory")) {
            int[] arr = compound.getIntArray("CropInventory");
            if (arr.length == 3)
                this.cropInventory = new BlockPos(arr[0], arr[1], arr[2]);
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
        //CompoundTag genes = compound.getCompound("Genes");
        //genes.keySet().forEach(key->this.attributeRandomizer.put(ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(key)), genes.getInt(key)));
    }

    //=====Animation Stuff

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isTamed();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.isTamed();
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        if (!this.canBeControlledByRider() && this.getMoveControl().operation != MoveControl.Operation.WAIT
                && this.getDeltaMovement().lengthSqr() > 0.004) {
            double d0 = this.getMoveControl().getSpeedModifier();
            if (d0 > this.sprintSpeedThreshold()) {
                this.setMovingFlag(MoveType.RUN);
            } else if (d0 <= this.crouchSpeedThreshold()) {
                this.setMovingFlag(MoveType.SNEAK);
            } else {
                this.setMovingFlag(MoveType.WALK);
            }
            this.updateMoveAnimation();
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
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.populateDefaultEquipmentSlots(difficulty);
        this.setLevel(Mth.clamp(this.level().getLevel(), this.prop.minLevel, LibConstants.MAX_MONSTER_LEVEL));
        //for(Attribute att : this.prop.getAttributeGains().keySet())
        //    this.attributeRandomizer.put(att, this.rand.nextInt(5)-2);
        return spawnData;
    }

    @Override
    public boolean canBeControlledByRider() {
        return this.isTamed() && this.rideable() && this.getControllingPassenger() instanceof Player;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        boolean clientSide = this.level.isClientSide;
        ItemStack stack = player.getItemInHand(hand);
        if (this.isTamed()) {
            if (!player.getUUID().equals(this.getOwnerUUID())) {
                if (!clientSide)
                    player.sendMessage(new TranslatableComponent("runecraftory.monster.interact.notowner"), Util.NIL_UUID);
                return InteractionResult.sidedSuccess(clientSide);
            }
            if (player.isShiftKeyDown()) {
                if (stack.getItem() == Items.STICK) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.VILLAGER_NO, SoundSource.NEUTRAL, player.getX(), player.getY(), player.getZ(), 1, 1));
                        this.untameEntity();
                    }
                    return InteractionResult.sidedSuccess(clientSide);
                }
            }
            if (!clientSide && MobConfig.monsterNeedBarn && this.assignedBarn == null) {
                if (!this.assignBarn()) {
                    player.sendMessage(new TranslatableComponent("runecraftory.monster.interact.barn.no", this.getDisplayName()), Util.NIL_UUID);
                    return InteractionResult.CONSUME;
                }
            }
            if (stack.getItem() == ModItems.BRUSH.get()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    int day = WorldUtils.day(this.level);
                    if (this.updater.getLastUpdateBrush() == day)
                        return InteractionResult.PASS;
                    serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.HORSE_SADDLE, SoundSource.NEUTRAL, player.getX(), player.getY(), player.getZ(), 0.7f, 1));
                    this.updater.setLastUpdateBrush(day);
                    this.onBrushing();
                    this.increaseFriendPoints(15);
                    this.level.broadcastEntityEvent(this, (byte) 64);
                    player.swing(hand);
                }
                return InteractionResult.sidedSuccess(clientSide);
            }
            if (hand == InteractionHand.MAIN_HAND && !this.playDeath()) {
                if (player.isShiftKeyDown()) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        EntityUtils.sendAttributesTo(this, serverPlayer);
                        Platform.INSTANCE.sendToClient(new S2COpenCompanionGui(this, serverPlayer), serverPlayer);
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
                        serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.HORSE_SADDLE, SoundSource.NEUTRAL, player.getX(), player.getY(), player.getZ(), 0.7f, 1));
                        this.brushCount = Math.min(10, this.brushCount + 1);
                        this.tamingTick = 40;
                        this.level.broadcastEntityEvent(this, (byte) 64);
                    }
                    return InteractionResult.sidedSuccess(clientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    public void increaseFriendPoints(int xp) {
        boolean leveledUp = this.friendlyPoints.addXP(xp, 10, LevelCalc::friendPointsForNext, () -> this.entityData.set(FRIEND_POINTS_SYNC, this.friendlyPoints.getLevel()));
        if (leveledUp) {
            this.updateFriendPointAttributeBonus();
        }
    }

    private void updateFriendPointAttributeBonus() {
        List<Attribute> increasable = List.of(Attributes.MAX_HEALTH, Attributes.ATTACK_DAMAGE,
                ModAttributes.DEFENCE.get(), ModAttributes.MAGIC.get(), ModAttributes.MAGIC_DEFENCE.get());
        for (Attribute att : increasable) {
            AttributeInstance inst = this.getAttribute(att);
            if (inst != null) {
                double inc = (this.friendlyPoints.getLevel() - 1) * 0.03;
                inst.removeModifier(LibConstants.ATTRIBUTE_FRIEND_MOD);
                inst.addPermanentModifier(new AttributeModifier(LibConstants.ATTRIBUTE_FRIEND_MOD, "rf.friend.mod", inc, AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }
    }

    public void onBrushing() {
        Attribute toIncrease = switch (this.random.nextInt(4)) {
            case 1 -> ModAttributes.DEFENCE.get();
            case 2 -> ModAttributes.MAGIC.get();
            case 3 -> ModAttributes.MAGIC_DEFENCE.get();
            default -> Attributes.ATTACK_DAMAGE;
        };
        AttributeInstance inst = this.getAttribute(toIncrease);
        if (inst != null) {
            AttributeModifier mod = inst.getModifier(LibConstants.ATTRIBUTE_BRUSH_MOD);
            double inc = 1;
            if (mod != null)
                inc += mod.getAmount();
            inst.removeModifier(LibConstants.ATTRIBUTE_BRUSH_MOD);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.ATTRIBUTE_BRUSH_MOD, "rf.brush.mod", inc, AttributeModifier.Operation.ADDITION));
        }
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return this.canBeControlledByRider() && !this.level.isClientSide;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return CombatUtils.mobAttack(this, entity);
    }

    @Override
    public ItemStack getPickResult() {
        return SpawnEgg.fromType(this.getType()).map(ItemStack::new).orElse(ItemStack.EMPTY);
    }

    @Override
    @Nullable
    public AnimatedAction getRandomAnimation(AnimationType type) {
        List<AnimatedAction> anims = new ArrayList<>();
        for (AnimatedAction anim : this.getAnimationHandler().getAnimations())
            if (this.isAnimOfType(anim, type))
                anims.add(anim);
        if (anims.isEmpty())
            return null;
        return anims.get(this.getRandom().nextInt(anims.size()));
    }

    @Override
    public abstract boolean isAnimOfType(AnimatedAction anim, AnimationType type);

    @Override
    public int animationCooldown(@Nullable AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown();
        if (anim == null)
            return this.getRandom().nextInt(20) + 25 + diffAdd;
        return this.getRandom().nextInt(20) + 15 + diffAdd;
    }

    public int difficultyCooldown() {
        int diffAdd = 15;
        Difficulty diff = this.level.getDifficulty();
        if (this.level.getDifficulty() == Difficulty.HARD)
            diffAdd = 0;
        else if (diff == Difficulty.NORMAL)
            diffAdd = 8;
        return diffAdd;
    }

    @Override
    public TagKey<Item> tamingItem() {
        return RunecraftoryTags.tamingTag(this.getType());
    }

    public ResourceLocation dailyDropTable() {
        ResourceLocation def = this.getDefaultLootTable();
        return new ResourceLocation(def.getNamespace(), def.getPath() + "_tamed_drops");
    }

    @Override
    public float tamingChance() {
        return this.prop.tamingChance;
    }

    @Override
    public boolean isTamed() {
        return this.getOwnerUUID() != null;
    }

    @Override
    public boolean rideable() {
        return this.prop.rideable;
    }

    public boolean attackOtherTamedMobs() {
        return false;
    }

    public EntityProperties getProp() {
        return this.prop;
    }

    //=====Level Handling

    @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    @Override
    public Player getOwner() {
        UUID uuid = this.getOwnerUUID();
        if (uuid != null) {
            if (this.owner == null || !this.owner.isAlive()) {
                if (this.level.isClientSide)
                    this.owner = this.level.getPlayerByUUID(uuid);
                else
                    this.owner = this.level.getServer().getPlayerList().getPlayer(uuid);
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
    public boolean isFlyingEntity() {
        return this.prop.flying;
    }

    @Override
    public LevelExpPair level() {
        return this.levelPair;
    }

    @Override
    public void setLevel(int level) {
        this.levelPair.setLevel(Mth.clamp(level, 1, LibConstants.MAX_MONSTER_LEVEL), LevelCalc::xpAmountForLevelUp);
        this.updateStatsToLevel();
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        Platform.INSTANCE.sendToClient(S2CEntityLevelPkt.create(this), player);
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
    public boolean applyFoodEffect(ItemStack stack) {
        if (this.level.isClientSide)
            return false;
        if (stack.getItem() == ModItems.OBJECT_X.get())
            ItemObjectX.applyEffect(this, stack);
        this.removeFoodEffect();
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
                    default -> SoundEvents.NOTE_BLOCK_PLING;
                };
                boolean food = this.applyFoodEffect(stack);
                if (food || !this.playDeath()) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundSoundPacket(sound, SoundSource.NEUTRAL, player.getX(), player.getY(), player.getZ(), 0.7f, 1));
                        Platform.INSTANCE.getPlayerData(serverPlayer)
                                .ifPresent(data -> data.getDailyUpdater().onGiveMonsterItem(serverPlayer));
                    }
                    stack.setCount(count);
                    this.feedTimeOut = 7;
                    int day = WorldUtils.day(this.level);
                    if (this.updater.getLastUpdateFood() != day) {
                        this.updater.setLastUpdateFood(day);
                        this.increaseFriendPoints(favorite ? 50 : 30);
                        if (favorite)
                            this.level.broadcastEntityEvent(this, (byte) 65);
                        else
                            this.level.broadcastEntityEvent(this, (byte) 64);
                        DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).ifPresent(s -> s.getMonsterGiftIncrease().forEach((att, d) -> {
                            AttributeInstance inst = this.getAttribute(att);
                            if (inst != null) {
                                AttributeModifier mod = inst.getModifier(LibConstants.MONSTER_ITEM_BONUS);
                                double val = d;
                                if (mod != null) {
                                    val += mod.getAmount();
                                    inst.removeModifier(mod);
                                }
                                inst.addPermanentModifier(new AttributeModifier(LibConstants.MONSTER_ITEM_BONUS, "item_bonus_" + att.getDescriptionId(), val, AttributeModifier.Operation.ADDITION));
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
                default -> SoundEvents.NOTE_BLOCK_PLING;
            };
            if (player instanceof ServerPlayer serverPlayer)
                serverPlayer.connection.send(new ClientboundSoundPacket(sound, SoundSource.NEUTRAL, player.getX(), player.getY(), player.getZ(), 0.7f, 1));
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
                        this.level.broadcastEntityEvent(this, (byte) 34);
                    else if (this.random.nextFloat() < chance) {
                        this.tameEntity(player);
                    } else {
                        this.level.broadcastEntityEvent(this, (byte) 11);
                    }
                };
            return true;
        }
        return false;
    }

    public void recalcStatsFull() {
        this.applyAttributes();
        this.updateStatsToLevel();
        this.updateFriendPointAttributeBonus();
    }

    public void updateStatsToLevel() {
        if (!this.level.isClientSide)
            Platform.INSTANCE.sendToTrackingAndSelf(S2CEntityLevelPkt.create(this), this);
        float preHealthDiff = this.getMaxHealth() - this.getHealth();
        this.prop.getAttributeGains().forEach((att, val) -> {
            AttributeInstance inst = this.getAttribute(att);
            if (inst != null) {
                val *= 0.01;
                inst.removeModifier(LibConstants.ATTRIBUTE_LEVEL_MOD);
                float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
                inst.addPermanentModifier(new AttributeModifier(LibConstants.ATTRIBUTE_LEVEL_MOD, "rf.levelMod", (this.level().getLevel() - 1) * val * multiplier, AttributeModifier.Operation.ADDITION));
                if (att == Attributes.MAX_HEALTH)
                    this.setHealth(this.getMaxHealth() - preHealthDiff);
            }
        });
    }

    public void increaseLevel() {
        this.levelPair.setLevel(Mth.clamp(this.level().getLevel() + 1, 1, LibConstants.MAX_MONSTER_LEVEL), LevelCalc::xpAmountForLevelUp);
        this.updateStatsToLevel();
    }

    public void addXp(float amount) {
        LevelExpPair pair = this.level();
        boolean res = pair.addXP(amount, LibConstants.MAX_MONSTER_LEVEL, LevelCalc::xpAmountForLevelUp, () -> {
        });
        Platform.INSTANCE.sendToTrackingAndSelf(S2CEntityLevelPkt.create(this), this);
        if (res)
            this.updateStatsToLevel();
    }

    public void updateMoveAnimation() {
    }

    public LevelExpPair getFriendlyPoints() {
        return this.friendlyPoints;
    }

    @Override
    public int friendPoints(UUID player) {
        if (player.equals(this.getOwnerUUID()))
            return this.entityData.get(FRIEND_POINTS_SYNC);
        return 0;
    }

    public void onDailyUpdate() {
        if (this.level instanceof ServerLevel && this.isTamed() && !this.playDeath() && (!MobConfig.monsterNeedBarn || this.assignBarn())) {
            ResourceLocation resourceLocation = this.dailyDropTable();
            this.dropAsDailyDrop(resourceLocation);
        }
    }

    protected void dropAsDailyDrop(ResourceLocation resourceLocation) {
        LootTable lootTable = this.level.getServer().getLootTables().get(resourceLocation);
        lootTable.getRandomItems(this.dailyDropContext().create(LootCtxParameters.MONSTER_INTERACTION), this::spawnAtLocation);
    }

    protected LootContext.Builder dailyDropContext() {
        LootContext.Builder builder = new LootContext.Builder((ServerLevel) this.level)
                .withRandom(this.random)
                .withParameter(LootContextParams.THIS_ENTITY, this)
                .withParameter(LootContextParams.ORIGIN, this.position());
        if (this.getOwnerUUID() != null)
            builder.withOptionalParameter(LootCtxParameters.UUID_CONTEXT, this.getOwnerUUID());
        return builder;
    }

    //=====Movement

    public int moveTick() {
        return this.moveTick;
    }

    public float interpolatedMoveTick(float partialTicks) {
        return this.moveRandom + Mth.clamp((this.moveTick + (this.getMoveFlag() != MoveType.NONE ? partialTicks : -partialTicks)) / (float) MOVE_TICK_MAX, 0, 1);
    }

    public void setMovingFlag(MoveType type) {
        this.entityData.set(MOVE_FLAGS, (byte) type.ordinal());
    }

    public MoveType getMoveFlag() {
        return MoveType.values()[this.entityData.get(MOVE_FLAGS)];
    }

    public void setDoJumping(boolean jump) {
        this.doJumping = jump;
    }

    public boolean doJumping() {
        return this.doJumping;
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
            this.level.addParticle(particle, true, this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0f - this.getBbWidth(), this.getY() + 0.5 + this.random.nextFloat() * this.getBbHeight(), this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0f - this.getBbWidth(), d0, d2, d3);
        }
    }

    //=====Combat stuff

    @Override
    public void die(DamageSource cause) {
        if (!this.level.isClientSide) {
            if (this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer)
                this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage(), Util.NIL_UUID);
            if (this.getServer() != null && this.getOwnerUUID() != null) {
                WorldHandler.get(this.getServer())
                        .removeMonsterFromPlayer(this.getOwnerUUID(), this);
                this.assignedBarn = null;
            }
            this.getAnimationHandler().setAnimation(null);
            List<CombatEntry> entries = ((CombatTrackerAccessor) this.getCombatTracker()).getEntries();
            Map<UUID, CombatRecord> merged = new HashMap<>();
            entries.forEach(e -> {
                if (e.getAttacker() instanceof ServerPlayer player) {
                    merged.compute(player.getUUID(), (id, o) -> o == null ? new CombatRecord(player, e.getSource(), e.getDamage())
                            : new CombatRecord(player, e.getSource(), o.totalDamage() + e.getDamage())
                    );
                }
            });
            merged.values().forEach(rec -> this.onDeathDamageRecord(rec.player, rec.lastSource, rec.totalDamage));
        }
        super.die(cause);
    }

    public void onDeathDamageRecord(ServerPlayer player, DamageSource source, float damage) {
        if (damage > this.getMaxHealth() * 0.05) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(d -> d.increaseMobFrom(player, this));
        }
    }

    @Override
    protected void tickDeath() {
        if (!this.level.isClientSide && this.deathTime == 0) {
            this.playDeathAnimation(false);
            this.getNavigation().stop();
        }
        ++this.deathTime;
        if (this.deathTime == (this.maxDeathTime() - 5)) {
            if (!this.level.isClientSide && this.getLastHurtByMob() != null) {
                LevelCalc.addXP(this.getLastHurtByMob(), this.baseXP(), this.baseMoney(), this.level().getLevel());
            }
        }
        if (this.deathTime >= this.maxDeathTime()) {
            if (!this.level.isClientSide)
                this.remove(RemovalReason.KILLED);
            for (int i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
            }
        }
    }

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity livingBase) {
        if (this.isDeadOrDying() && this.deathTime > 0)
            return;
        super.setLastHurtByMob(livingBase);
    }

    public boolean playDeath() {
        return this.entityData.get(PLAY_DEATH_STATE);
    }

    public void setPlayDeath(boolean flag) {
        this.entityData.set(PLAY_DEATH_STATE, flag);
        if (flag) {
            if (!this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer)
                this.getOwner().sendMessage(this.getKnockoutMessage(), Util.NIL_UUID);
            this.level.getEntities(EntityTypeTest.forClass(Mob.class), this.getBoundingBox().inflate(32), e -> this.equals(e.getTarget()))
                    .forEach(m -> m.setTarget(null));
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
    public boolean isNoGravity() {
        return super.isNoGravity() && !this.playDeath();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player player && player.getUUID().equals(this.getOwnerUUID())
                && !player.isShiftKeyDown()) {
            return false;
        }
        if (this.playDeath() && source != DamageSource.OUT_OF_WORLD)
            return false;
        return (source.getEntity() == null || this.canAttackFrom(source.getEntity().position())) && super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.isTamed() && damageSrc instanceof CustomDamage dmg && dmg.getEntity() instanceof Player && dmg.getElement() == EnumElement.LOVE)
            this.loveAttCount = Math.min(100, this.loveAttCount + 1);
        if (damageSrc != DamageSource.OUT_OF_WORLD && this.isTamed() && this.getHealth() <= 0) {
            this.setHealth(0.01f);
            this.setPlayDeath(true);
        }
    }

    //=====Movement Handling

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        if (this.isFlyingEntity() || this.getMoveControl() instanceof FlyingMoveControl) {
            return false;
        }
        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.canBeControlledByRider() || this.playDeath() || this.tamingTick > 0;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.83F;
    }

    @Override
    public void travel(Vec3 vec) {
        if (this.shouldFreezeTravel())
            return;
        if (this.canBeControlledByRider() && this.getControllingPassenger() instanceof Player player) {
            if (!this.level.isClientSide) {
                if (this.adjustRotFromRider(player)) {
                    this.setYRot(this.rotateClamped(this.getYRot(), player.getYRot(), this.getHeadRotSpeed() * 2));
                    this.setXRot(this.rotateClamped(this.getXRot(), player.getXRot(), this.getMaxHeadXRot()));
                }
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
            }
            double strafing = player.xxa * 0.5f;
            double forward = player.zza;
            if (forward <= 0.0f) {
                forward *= 0.25f;
            }
            if (this.getAnimationHandler().hasAnimation()) {
                strafing = 0;
                forward = 0;
            }
            if (this.doJumping) {
                if (this.onGround && !this.isFlyingEntity()) {
                    this.hasImpulse = true;
                    this.jumpFromGround();
                    if (forward > 0.0f) {
                        float f = Mth.sin(this.getYRot() * 0.017453292f);
                        float f2 = Mth.cos(this.getYRot() * 0.017453292f);
                        this.setDeltaMovement(this.getDeltaMovement().add(-0.4f * f, 0, 0.4f * f2));
                    }
                } else if (this.isFlyingEntity()) {
                    float speed = this.getSpeed() * 0.5f;
                    double motionY = Math.min(this.getDeltaMovement().y + speed, this.maxAscensionSpeed());
                    this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, motionY, this.getDeltaMovement().z));
                    if (forward > 0.0f) {
                        float f = Mth.sin(this.getYRot() * 0.017453292f);
                        float f2 = Mth.cos(this.getYRot() * 0.017453292f);
                        speed *= 0.1;
                        this.setDeltaMovement(this.getDeltaMovement().add(-speed * f, 0, speed * f2));
                    }
                }
            }
            this.flyingSpeed = this.getSpeed() * 0.1f;
            if (this.isControlledByLocalInstance()) {
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.7f);
                MoveType type = forward > 0 ? MoveType.RUN : (forward != 0 || strafing != 0 ? MoveType.WALK : MoveType.NONE);
                this.setMovingFlag(type);
                this.setSprinting(type == MoveType.RUN);
                forward *= this.ridingSpeedModifier();
                strafing *= this.ridingSpeedModifier();
                super.travel(new Vec3(strafing, vec.y, forward));
            } else if (player instanceof Player) {
                this.setDeltaMovement(Vec3.ZERO);
            }
            if (this.onGround || this.isFlyingEntity()) {
                this.doJumping = false;
            }
            this.calculateEntityAnimation(this, false);
        } else {
            this.handleLandTravel(vec);
        }
    }

    public void handleLandTravel(Vec3 vec) {
        this.flyingSpeed = 0.02f;
        super.travel(vec);
    }

    public void handleNoGravTravel(Vec3 vec) {
        if (this.isVehicle()) {
            if (this.canBeControlledByRider() && this.getControllingPassenger() instanceof Player player
                    && !this.getAnimationHandler().hasAnimation()) {
                if (!this.level.isClientSide) {
                    if (this.adjustRotFromRider(player)) {
                        this.setYRot(this.rotateClamped(this.getYRot(), player.getYRot(), this.getHeadRotSpeed() * 2));
                        this.setXRot(this.rotateClamped(this.getXRot(), player.getXRot(), this.getMaxHeadXRot()));
                    }
                    this.yBodyRot = this.getYRot();
                    this.yHeadRot = this.yBodyRot;
                }
                float strafing = player.xxa * 0.5f;
                float forward = player.zza;
                double vert = 0;
                if (forward <= 0.0f) {
                    forward *= 0.25f;
                } else {
                    vert = Math.min(0, player.getLookAngle().y + 0.45);
                    if (player.getXRot() > 85)
                        forward = 0;
                    else if (vert < 0)
                        forward = (float) Math.sqrt(forward * forward - vert * vert);
                }
                if (this.doJumping()) {
                    vert += Math.min(this.getDeltaMovement().y + 0.5, this.maxAscensionSpeed());
                }
                this.flyingSpeed = this.getSpeed() * 0.1f;
                if (this.isControlledByLocalInstance()) {
                    float attVal = (float) (this.getAttribute(Attributes.FLYING_SPEED) != null ? this.getAttribute(Attributes.FLYING_SPEED).getValue() : this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    this.setSpeed(attVal * 1.15f);
                    MoveType type = forward > 0 ? MoveType.RUN : (forward != 0 || strafing != 0 ? MoveType.WALK : MoveType.NONE);
                    this.setMovingFlag(type);
                    this.setSprinting(type == MoveType.RUN);
                    forward *= this.ridingSpeedModifier();
                    strafing *= this.ridingSpeedModifier();
                    vec = new Vec3(strafing * this.getSpeed(), vec.y, forward * this.getSpeed());
                } else {
                    vec = Vec3.ZERO;
                }
                vec = vec.add(0, vert, 0);
                this.setDoJumping(false);
                this.calculateEntityAnimation(this, false);
            }
        }

        this.moveRelative(0.1F, vec);
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
    }

    private float rotateClamped(float current, float target, float maxChange) {
        float f = Mth.degreesDifference(current, target);
        float g = Mth.clamp(f, -maxChange, maxChange);
        return current + g;
    }

    public double ridingSpeedModifier() {
        return 0.85;
    }

    /**
     * @return For flying entities: The max speed for flying up
     */
    public double maxAscensionSpeed() {
        return this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 2;
    }

    public boolean shouldFreezeTravel() {
        return false;
    }

    //=====Interaction

    public int maxDeathTime() {
        return 20;
    }

    protected void playDeathAnimation(boolean load) {
        if (this.getDeathAnimation() != null) {
            this.getAnimationHandler().setAnimation(this.getDeathAnimation());
            if (load && this.level.isClientSide) {
                AnimatedAction anim = this.getAnimationHandler().getAnimation();
                while (anim.getTick() < anim.getLength())
                    anim.tick();
            }
        }
    }

    public AnimatedAction getDeathAnimation() {
        return null;
    }

    @Override
    public void setSleepingPos(BlockPos pos) {
        super.setSleepingPos(pos);
        this.onSleeping(true);
    }

    @Override
    public void clearSleepingPos() {
        super.clearSleepingPos();
        this.onSleeping(false);
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
                if (this.firstTick && this.level.isClientSide) {
                    AnimatedAction anim = this.getAnimationHandler().getAnimation();
                    while (anim.getTick() < anim.getLength())
                        anim.tick();
                }
            }
        } else
            this.getAnimationHandler().setAnimation(null);
    }

    public AnimatedAction getSleepAnimation() {
        return null;
    }

    public void lookAtNow(Entity entity, float maxYRotIncrease, float maxXRotIncrease) {
        super.lookAt(entity, maxYRotIncrease, maxXRotIncrease);
        this.yHeadRot = this.getYRot();
        this.yBodyRot = this.yHeadRot;
    }

    private boolean canAttackFrom(Vec3 pos) {
        if (this.isTamed())
            return true;
        boolean result = this.getRestrictRadius() == -1.0f || this.getRestrictCenter().distToCenterSqr(pos.x(), pos.y(), pos.z()) < (this.getRestrictRadius() * this.getRestrictRadius());
        if (!result) {
            this.level.playSound(null, this, SoundEvents.ANVIL_PLACE, this.getSoundSource(), 0.7f, 0.9f);
            if (this.level instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 6; i++)
                    serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT, this.getRandomX(1.2), this.getRandomY(), this.getRandomZ(1.2), 0, 0, 0, 0, 0);
            }
        }
        return result;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.15;
    }

    public void handleAttack(AnimatedAction anim) {
        if (this.isAnimOfType(anim, AnimationType.MELEE)) {
            this.getNavigation().stop();
            if (anim.getTick() == 1 && this.getTarget() != null) {
                this.lookAtNow(this.getTarget(), 360, 90);
                this.targetPosition = this.getTarget().position();
            }
            if (anim.canAttack()) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
                this.targetPosition = null;
            }
        }
    }

    public void mobAttack(AnimatedAction anim, LivingEntity target, Consumer<LivingEntity> cons) {
        AABB aabb = this.calculateAttackAABB(anim, this.targetPosition != null || target == null ? this.targetPosition : target.position(), 0.2);
        this.level.getEntitiesOfClass(LivingEntity.class, aabb, this.hitPred).forEach(cons);
        if (this.getServer() != null)
            Platform.INSTANCE.sendToAll(new S2CAttackDebug(aabb), this.getServer());
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        double reach = this.maxAttackRange(anim) * 0.5 + this.getBbWidth();
        Vec3 dir;
        if (target != null && !this.canBeControlledByRider()) {
            reach = Math.min(reach, this.position().distanceTo(target));
            dir = target.subtract(this.position()).normalize();
        } else {
            if (this.getControllingPassenger() instanceof Player player)
                dir = player.getLookAngle();
            else
                dir = Vec3.directionFromRotation(this.getXRot(), this.getYRot());
        }
        Vec3 attackPos = this.position().add(dir.scale(reach));
        return this.attackAABB(anim).inflate(grow, 0, grow).move(attackPos.x, attackPos.y, attackPos.z);
    }

    public AABB attackAABB(AnimatedAction anim) {
        double range = this.maxAttackRange(anim) * 0.5;
        return new AABB(-range, -0.02, -range, range, this.getBbHeight() + 0.02, range);
    }

    public abstract void handleRidingCommand(int command);

    public boolean isStaying() {
        return this.behaviour == Behaviour.STAY;
    }

    public void setBehaviour(Behaviour behaviour) {
        this.setBehaviour(behaviour, false);
    }

    private void setBehaviour(Behaviour behaviour, boolean load) {
        this.entityData.set(BEHAVIOUR_DATA, behaviour.ordinal());
        this.behaviour = behaviour;
        if (!this.level.isClientSide)
            this.updateAI(false, load);
    }

    public Behaviour behaviourState() {
        return this.behaviour;
    }

    public boolean doStartRide(ServerPlayer player) {
        if (this.rideable()) {
            player.startRiding(this);
            return true;
        }
        player.sendMessage(new TranslatableComponent("runecraftory.monster.interact.ride.no"), Util.NIL_UUID);
        return false;
    }

    protected float tamingMultiplier(ItemStack stack) {
        boolean flag = stack.is(this.tamingItem());
        return flag ? 2 : 1;
    }

    protected void tameEntity(Player owner) {
        if (!this.isAlive())
            return;
        this.restrictTo(this.blockPosition(), -1);
        this.setOwner(owner);
        this.navigation.stop();
        this.setTarget(null);
        this.level.broadcastEntityEvent(this, (byte) 10);
        this.updater.setLastUpdateDay(WorldUtils.day(this.level));
        if (Platform.INSTANCE.getPlayerData(owner).map(d -> d.party.isPartyFull()).orElse(true))
            this.setBehaviour(Behaviour.WANDER);
        else
            this.setBehaviour(Behaviour.FOLLOW);
        this.level.getEntities(EntityTypeTest.forClass(Mob.class), this.getBoundingBox().inflate(32),
                e -> e != this && e instanceof OwnableEntity ownable && this.getOwnerUUID().equals(ownable.getOwnerUUID())
                        && e.getTarget() == this).forEach(e -> {
            e.setTarget(null);
            if (e.getLastHurtByMob() == this)
                e.setLastHurtByMob(null);
        });
        this.setLastHurtByMob(null);
        if (owner instanceof ServerPlayer serverPlayer) {
            Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> {
                data.entityStatsTracker.tameEntity(this);
                ModCriteria.TAME_MONSTER_TRIGGER.trigger(serverPlayer, this, data.entityStatsTracker);
                LevelCalc.levelSkill(serverPlayer, data, EnumSkills.TAMING, 10);
            });
            SimpleQuestIntegration.INST().triggerTaming(serverPlayer, this);
        }
        if (this.getServer() != null) {
            this.assignBarn();
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

    private boolean assignBarn() {
        if (this.assignedBarn == null || this.assignedBarn.isInvalidFor(this))
            this.assignedBarn = WorldHandler.get(this.getServer()).findFittingBarn(this);
        if (this.assignedBarn != null) {
            this.assignedBarn.addMonster(this, this.getProp().size);
            return true;
        }
        return false;
    }

    public BarnData getAssignedBarn() {
        this.assignBarn();
        return this.assignedBarn;
    }

    protected void untameEntity() {
        this.level.getEntities(EntityTypeTest.forClass(Mob.class), this.getBoundingBox().inflate(32),
                e -> e != this && e.getTarget() == this).forEach(e -> {
            e.setTarget(null);
            if (e.getLastHurtByMob() == this)
                e.setLastHurtByMob(null);
        });
        if (this.getServer() != null && this.getOwnerUUID() != null) {
            WorldHandler.get(this.getServer())
                    .removeMonsterFromPlayer(this.getOwnerUUID(), this);
            if (this.getOwner() != null)
                Platform.INSTANCE.getPlayerData(this.getOwner()).ifPresent(d -> d.party.removePartyMember(this));
            else
                WorldHandler.get(this.getServer())
                        .toRemovePartyMember(this);
            this.assignedBarn = null;
        }
        this.setOwner(null);
        this.setLastHurtByMob(null);
        if (this.playDeath())
            this.heal(this.getMaxHealth());
        this.setBehaviour(Behaviour.WANDER);
        this.updateAI(true, false);
        this.setTarget(null);
        this.getNavigation().stop();
    }

    public BlockPos getSeedInventory() {
        if (this.seedInventory != null && !this.isWithinRestriction(this.seedInventory)) {
            return null;
        }
        return this.seedInventory;
    }

    public BlockPos getCropInventory() {
        if (this.cropInventory != null && !this.isWithinRestriction(this.cropInventory)) {
            return null;
        }
        return this.cropInventory;
    }

    public void setSeedInventory(BlockPos seedInventory) {
        this.seedInventory = seedInventory;
    }

    public void setCropInventory(BlockPos cropInventory) {
        this.cropInventory = cropInventory;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        this.getNavigation().stop();
        this.setDeltaMovement(Vec3.ZERO);
        this.setYya(0);
        this.setZza(0);
        this.setXxa(0);
        this.setTarget(null);
        super.addPassenger(passenger);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        if (passenger == this.getOwner())
            this.setBehaviour(Behaviour.FOLLOW);
        super.removePassenger(passenger);
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getFirstPassenger();
    }

    @Override
    public boolean rideableUnderWater() {
        return this.canBreatheUnderwater();
    }

    public boolean adjustRotFromRider(LivingEntity rider) {
        return true;
    }

    public abstract void playInteractionAnimation();

    @Override
    public boolean canBeAttackedBy(LivingEntity entity) {
        if (entity instanceof Mob m && entity.getType().is(RunecraftoryTags.TAMED_MONSTER_IGNORE) && this.getTarget() != m && m.getLastHurtByMob() != this)
            return !this.isTamed();
        return true;
    }

    @Override
    public void setLevelCallback(EntityInLevelCallback levelCallback) {
        super.setLevelCallback(WorldUtils.wrappedCallbackFor(this, this::getOwner, levelCallback));
    }

    @Override
    public Predicate<LivingEntity> validTargetPredicate() {
        return this.hitPred;
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

    public enum MoveType {
        NONE,
        WALK,
        RUN,
        SNEAK
    }

    public record CombatRecord(ServerPlayer player, DamageSource lastSource, float totalDamage) {
    }
}
