package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.SimpleEffect;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.config.values.EntityProperties;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.ai.FollowOwnerGoalMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.HurtByTargetPredicate;
import io.github.flemmli97.runecraftory.common.entities.ai.LookAtAliveGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.RandomLookGoalAlive;
import io.github.flemmli97.runecraftory.common.entities.ai.RiderAttackTargetGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.StayGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.TendCropsGoal;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemObjectX;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.network.S2CAttackDebug;
import io.github.flemmli97.runecraftory.common.network.S2COpenCompanionGui;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.mixin.AttributeMapAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.config.ItemTagWrapper;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class BaseMonster extends PathfinderMob implements Enemy, IAnimated, IExtendedMob, RandomAttackSelectorMob, ExtendedEntity {

    private static final EntityDataAccessor<Optional<UUID>> owner = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> entityLevel = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> levelXP = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Byte> moveFlags = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> behaviourData = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> playDeathState = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> friendPointsSync = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.INT);

    public final Predicate<LivingEntity> targetPred = (e) -> {
        if (e != this) {
            if (this.getControllingPassenger() instanceof Player)
                return false;
            if (this.isTamed()) {
                return e instanceof Enemy && EntityUtils.tryGetOwner(e) == null;
            }
            if (e instanceof Mob mob && this == mob.getTarget())
                return true;
            return EntityUtils.canMonsterTargetNPC(e) || EntityUtils.tryGetOwner(e) != null;
        }
        return false;
    };
    public final Predicate<LivingEntity> defendPred = (e) -> {
        if (e != this) {
            if (this.getControllingPassenger() instanceof Player)
                return false;
            if (this.isTamed())
                return !this.getOwnerUUID().equals(EntityUtils.tryGetOwner(e));
            return true;
        }
        return false;
    };
    private final EntityProperties prop;
    public NearestAttackableTargetGoal<Player> targetPlayer = this.createTargetGoalPlayer();//|| player != BaseMonster.this.getOwner());
    public NearestAttackableTargetGoal<Mob> targetMobs = this.createTargetGoalMobs();
    public FloatGoal swimGoal = new FloatGoal(this);
    public RandomStrollGoal wander = new WaterAvoidingRandomStrollGoal(this, 1.0);
    public HurtByTargetPredicate hurt = new HurtByTargetPredicate(this, this.defendPred);

    public TendCropsGoal farm = new TendCropsGoal(this);
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
            return riderTarget || e == this.getTarget() || EntityUtils.canMonsterTargetNPC(e) || EntityUtils.tryGetOwner(e) != null || e instanceof Player;
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

    private Behaviour behaviour = Behaviour.WANDER;

    private final LevelExpPair friendlyPoints = new LevelExpPair();
    private Map<ItemStack, Integer> dailyDrops;

    private final DailyMonsterUpdater updater = new DailyMonsterUpdater(this);

    /**
     * For movement animation interpolation
     */
    private int moveTick;

    public static final int moveTickMax = 5;

    public BaseMonster(EntityType<? extends BaseMonster> type, Level level) {
        super(type, level);
        this.moveControl = new NewMoveController(this);
        this.prop = MobConfig.propertiesMap.getOrDefault(PlatformUtils.INSTANCE.entities().getIDFrom(type), EntityProperties.defaultProp);
        this.applyAttributes();
        if (!level.isClientSide)
            this.addGoal();
    }

    public static AttributeSupplier.Builder createAttributes(Collection<? extends RegistryEntrySupplier<Attribute>> atts) {
        AttributeSupplier.Builder map = Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.22);
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
        this.goalSelector.addGoal(1, new FollowOwnerGoalMonster(this, 1.05, 9, 2, 20));
        this.goalSelector.addGoal(2, new LookAtAliveGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0));
        this.goalSelector.addGoal(6, this.wander);
        this.goalSelector.addGoal(7, new RandomLookGoalAlive(this));
    }

    private void updateAI(boolean forced) {
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
            }
            switch (this.behaviourState()) {
                case STAY -> {
                    this.goalSelector.addGoal(6, this.wander);
                    this.targetSelector.removeGoal(this.targetPlayer);
                    this.targetSelector.removeGoal(this.targetMobs);
                    this.targetSelector.removeGoal(this.hurt);
                }
                case WANDER -> {
                    this.restrictToBasedOnBehaviour(this.blockPosition());
                    this.goalSelector.addGoal(6, this.wander);
                }
                case FOLLOW -> {
                    this.clearRestriction();
                    this.goalSelector.removeGoal(this.wander);
                }
                case FARM -> {
                    this.restrictToBasedOnBehaviour(this.blockPosition());
                    this.goalSelector.addGoal(3, this.farm);

                    this.targetSelector.removeGoal(this.targetPlayer);
                    this.targetSelector.removeGoal(this.targetMobs);
                    this.targetSelector.removeGoal(this.hurt);

                    this.goalSelector.removeGoal(this.wander);

                    BlockPos nearestInv = this.nearestBlockEntityWithInv();
                    this.setSeedInventory(nearestInv);
                    this.setCropInventory(nearestInv);
                }
            }
        }
    }

    public void restrictToBasedOnBehaviour(BlockPos pos) {
        if (this.behaviourState() == Behaviour.FARM)
            this.restrictTo(pos, MobConfig.farmRadius + 3);
        else if (this.behaviourState() == Behaviour.WANDER)
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
        this.entityData.define(owner, Optional.empty());
        this.entityData.define(entityLevel, LibConstants.baseLevel);
        this.entityData.define(levelXP, 0f);
        this.entityData.define(moveFlags, (byte) 0);
        this.entityData.define(behaviourData, 0);
        this.entityData.define(playDeathState, false);
        this.entityData.define(friendPointsSync, 1);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (this.level.isClientSide) {
            if (key == behaviourData) {
                try {
                    this.behaviour = Behaviour.values()[this.entityData.get(behaviourData)];
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
            //This case only happens during load. At that point we want to skip right to the end of the animation
            if (key == playDeathState) {
                if (this.entityData.get(playDeathState) && !this.getAnimationHandler().hasAnimation())
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
            for (int i = 0; i < 4; ++i) {
                this.level.addParticle(ParticleTypes.ANGRY_VILLAGER, this.getX() - this.getBbWidth() * 0.25 + this.random.nextFloat() * this.getBbWidth() * 0.25f, this.getY() + this.getBbHeight() + this.random.nextFloat() * 0.3, this.getZ() - this.getBbWidth() * 0.25 + this.random.nextFloat() * this.getBbWidth() * 0.25f, 0, 0, 0);
            }
        } else if (id == 64) {
            this.level.addParticle(ParticleTypes.NOTE, this.getX(), this.getY() + this.getBbHeight() + 0.3, this.getZ(), 0, 0, 0);
        } else if (id == 65) {
            this.level.addParticle(ParticleTypes.HEART, this.getX(), this.getY() + this.getBbHeight() + 0.3, this.getZ(), 0, 0, 0);
        }
        super.handleEntityEvent(id);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getMoveFlag() != 0) {
            this.moveTick = Math.min(moveTickMax, ++this.moveTick);
        } else {
            this.moveTick = Math.max(0, --this.moveTick);
        }
        if (!this.level.isClientSide) {
            this.updater.tick();
            if (this.tamingTick > 0) {
                --this.tamingTick;
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
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.getAnimationHandler().tick();
        if (this.playDeath()) {
            this.playDeathTick = Math.min(15, ++this.playDeathTick);
            if (!this.level.isClientSide && this.getHealth() > 0.02) {
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
        compound.putBoolean("PlayDeath", this.entityData.get(playDeathState));
        if (this.seedInventory != null) {
            compound.putIntArray("SeedInventory", new int[]{this.seedInventory.getX(), this.seedInventory.getY(), this.seedInventory.getZ()});
        }
        if (this.cropInventory != null) {
            compound.putIntArray("CropInventory", new int[]{this.cropInventory.getX(), this.cropInventory.getY(), this.cropInventory.getZ()});
        }
        //CompoundTag genes = new CompoundTag();
        //this.attributeRandomizer.forEach((att, val)->genes.putInt(att.getRegistryName().toString(), val));
        //compound.put("Genes", genes);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.levelPair.read(compound.getCompound("MobLevel"));
        this.entityData.set(entityLevel, this.levelPair.getLevel());
        this.entityData.set(levelXP, this.levelPair.getXp());
        if (compound.contains("Owner"))
            this.entityData.set(owner, Optional.of(compound.getUUID("Owner")));
        try {
            this.setBehaviour(Behaviour.values()[compound.getInt("Behaviour")]);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        this.feedTimeOut = compound.getInt("FeedTime");
        if (compound.contains("Home")) {
            int[] home = compound.getIntArray("Home");
            this.restrictTo(new BlockPos(home[0], home[1], home[2]), home[3]);
        }
        this.foodBuffTick = compound.getInt("FoodBuffTick");
        this.friendlyPoints.read(compound.getCompound("FriendlyPoints"));
        this.entityData.set(friendPointsSync, this.friendlyPoints.getLevel());
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
        if (!this.isPassenger() && this.getMoveControl().operation != MoveControl.Operation.WAIT
                && this.getDeltaMovement().lengthSqr() > 0.007) {
            this.setMoving(true);
            double d0 = this.getMoveControl().getSpeedModifier();
            if (d0 > 1) {
                this.setShiftKeyDown(false);
                this.setSprinting(true);
            } else if (d0 >= 0.6D) {
                this.setShiftKeyDown(true);
                this.setSprinting(false);
            } else {
                this.setShiftKeyDown(false);
                this.setSprinting(false);
            }
            this.updateMoveAnimation();
        } else {
            this.setMoving(false);
            this.setShiftKeyDown(false);
            this.setSprinting(false);
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.populateDefaultEquipmentSlots(difficulty);
        //for(Attribute att : this.prop.getAttributeGains().keySet())
        //    this.attributeRandomizer.put(att, this.rand.nextInt(5)-2);
        return spawnData;
    }

    @Override
    public boolean canBeControlledByRider() {
        return this.isTamed() && this.ridable();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.level.isClientSide)
            return InteractionResult.PASS;
        ItemStack stack = player.getItemInHand(hand);
        if (this.isTamed()) {
            if (!player.getUUID().equals(this.getOwnerUUID())) {
                player.sendMessage(new TranslatableComponent("monster.interact.notowner"), Util.NIL_UUID);
                return InteractionResult.CONSUME;
            }
            if (stack.getItem() == ModItems.inspector.get() && hand == InteractionHand.MAIN_HAND) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundUpdateAttributesPacket(this.getId(), ((AttributeMapAccessor) this.getAttributes())
                            .getAttributes().values()));
                    Platform.INSTANCE.sendToClient(new S2COpenCompanionGui(this), serverPlayer);
                }
                return InteractionResult.SUCCESS;
            } else if (stack.getItem() == ModItems.brush.get()) {
                int day = WorldUtils.day(this.level);
                if (this.updater.getLastUpdateBrush() == day)
                    return InteractionResult.PASS;
                if (player instanceof ServerPlayer serverPlayer)
                    serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.HORSE_SADDLE, SoundSource.NEUTRAL, player.getX(), player.getY(), player.getZ(), 0.7f, 1));
                this.updater.setLastUpdateBrush(day);
                this.increaseFriendPoints(15);
                this.level.broadcastEntityEvent(this, (byte) 64);
                player.swing(hand);
                return InteractionResult.SUCCESS;
            }
            if (player.isShiftKeyDown()) {
                if (stack.getItem() == Items.STICK) {
                    if (player instanceof ServerPlayer serverPlayer)
                        serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.VILLAGER_NO, SoundSource.NEUTRAL, player.getX(), player.getY(), player.getZ(), 1, 1));
                    this.untameEntity();
                    return InteractionResult.SUCCESS;
                } else if (this.feedTimeOut <= 0) {
                    boolean favorite = this.tamingItem().match(stack);
                    ItemStack stack1 = null;
                    //The item will get consumed even if player is in creative so we add it back
                    if (player.isCreative())
                        stack1 = stack.copy();
                    if (this.applyFoodEffect(stack)) {
                        if (player instanceof ServerPlayer serverPlayer) {
                            serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, player.getX(), player.getY(), player.getZ(), 0.7f, 1));
                            Platform.INSTANCE.getPlayerData(serverPlayer)
                                    .ifPresent(data -> data.getDailyUpdater().onGiveMonsterItem(serverPlayer));
                        }
                        if (favorite)
                            this.level.broadcastEntityEvent(this, (byte) 65);
                        else
                            this.level.broadcastEntityEvent(this, (byte) 64);
                        //this.feedTimeOut = 24000;
                        this.feedTimeOut = 20;
                        int day = WorldUtils.day(this.level);
                        if (this.updater.getLastUpdateFood() != day) {
                            this.updater.setLastUpdateFood(day);
                            this.increaseFriendPoints(favorite ? 50 : 35);
                        }
                        if (stack1 != null)
                            player.setItemInHand(hand, stack1);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            if (hand == InteractionHand.MAIN_HAND && !this.playDeath()) {
                if (player.isShiftKeyDown()) {
                    this.setBehaviour(this.behaviourState().next());
                    if (player instanceof ServerPlayer serverPlayer)
                        serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.PLAYERS, player.getX(), player.getY(), player.getZ(), 0.4f, 0.4f));
                    player.sendMessage(new TranslatableComponent(this.behaviourState().interactKey), Util.NIL_UUID);
                    return InteractionResult.SUCCESS;
                } else if (this.ridable() && this.behaviourState() != Behaviour.FARM) {
                    player.startRiding(this);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        } else if (player.isShiftKeyDown() && !stack.isEmpty()) {
            if (stack.getItem() == ModItems.tame.get()) {
                this.tameEntity(player);
                return InteractionResult.SUCCESS;
            } else {
                if (this.tamingTick == -1 && this.isAlive()) {
                    if (stack.getItem() == ModItems.brush.get()) {
                        if (player instanceof ServerPlayer serverPlayer)
                            serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.HORSE_SADDLE, SoundSource.NEUTRAL, player.getX(), player.getY(), player.getZ(), 0.7f, 1));
                        this.brushCount = Math.min(10, this.brushCount + 1);
                        this.tamingTick = 40;
                        this.level.broadcastEntityEvent(this, (byte) 64);
                        return InteractionResult.SUCCESS;
                    }
                    if (player instanceof ServerPlayer serverPlayer)
                        serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, player.getX(), player.getY(), player.getZ(), 0.7f, 1));
                    float rightItemMultiplier = this.tamingMultiplier(stack);
                    int count = stack.getCount();
                    this.applyFoodEffect(stack);
                    if (count == stack.getCount() && !player.isCreative())
                        stack.shrink(1);
                    this.tamingTick = 100;
                    float chance = EntityUtils.tamingChance(this, player, rightItemMultiplier, this.brushCount, this.loveAttCount);
                    this.delayedTaming = () -> {
                        if (chance == 0)
                            this.level.broadcastEntityEvent(this, (byte) 34);
                        else if (this.random.nextFloat() < chance) {
                            this.tameEntity(player);
                        } else {
                            this.level.broadcastEntityEvent(this, (byte) 11);
                        }
                    };
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public void increaseFriendPoints(int xp) {
        this.friendlyPoints.addXP(xp, 10, LevelCalc::friendPointsForNext, () -> this.entityData.set(friendPointsSync, this.friendlyPoints.getLevel()));
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
            return this.getRandom().nextInt(15) + 10 + diffAdd;
        return this.getRandom().nextInt(17) + 5 + diffAdd;
    }

    public int difficultyCooldown() {
        int diffAdd = 35;
        Difficulty diff = this.level.getDifficulty();
        if (this.level.getDifficulty() == Difficulty.HARD)
            diffAdd = 0;
        else if (diff == Difficulty.NORMAL)
            diffAdd = 15;
        return diffAdd;
    }

    @Override
    public ItemTagWrapper tamingItem() {
        return this.prop.getTamingItem();
    }

    @Override
    public Map<ItemStack, Integer> dailyDrops() {
        if (this.dailyDrops == null)
            this.dailyDrops = this.prop.dailyDrops().entrySet().stream()
                    .collect(Collectors.toMap(e -> e.getKey().getStack(), Map.Entry::getValue));
        ;
        return this.dailyDrops;
    }

    @Override
    public float tamingChance() {
        return this.prop.tamingChance();
    }

    @Override
    public boolean isTamed() {
        return this.getOwnerUUID() != null;
    }

    @Override
    public boolean ridable() {
        return this.prop.ridable();
    }

    public boolean attackOtherTamedMobs() {
        return false;
    }

    //=====Level Handling

    @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(owner).orElse(null);
    }

    @Override
    public Player getOwner() {
        UUID uuid = this.getOwnerUUID();
        return uuid == null ? null : this.level.getPlayerByUUID(uuid);
    }

    @Override
    public void setOwner(Player player) {
        if (player != null)
            this.entityData.set(owner, Optional.of(player.getUUID()));
        else
            this.entityData.set(owner, Optional.empty());
    }

    @Override
    public boolean isFlyingEntity() {
        return this.prop.flying();
    }

    @Override
    public LevelExpPair level() {
        this.levelPair.setLevel(this.entityData.get(entityLevel));
        this.levelPair.setXp(this.entityData.get(levelXP));
        return this.levelPair;
    }

    @Override
    public void setLevel(int level) {
        this.entityData.set(entityLevel, Mth.clamp(level, 1, LibConstants.maxMonsterLevel));
        this.updateStatsToLevel();
    }

    @Override
    public int baseXP() {
        return this.prop.getXp();
    }

    @Override
    public int baseMoney() {
        return this.prop.getMoney();
    }

    @Override
    public boolean applyFoodEffect(ItemStack stack) {
        if (this.level.isClientSide)
            return false;
        if (stack.getItem() == ModItems.objectX.get())
            ItemObjectX.applyEffect(this, stack);
        this.removeFoodEffect();
        FoodProperties food = DataPackHandler.getFoodStat(stack.getItem());
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
        for (Map.Entry<Attribute, Double> entry : food.effectsMultiplier().entrySet()) {
            AttributeInstance inst = this.getAttribute(entry.getKey());
            if (inst == null)
                continue;
            inst.removeModifier(LibConstants.foodUUIDMulti);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.foodUUIDMulti, "foodBuffMulti_" + entry.getKey().getDescriptionId(), entry.getValue(), AttributeModifier.Operation.MULTIPLY_BASE));
        }
        for (Map.Entry<Attribute, Double> entry : food.effects().entrySet()) {
            AttributeInstance inst = this.getAttribute(entry.getKey());
            if (inst == null)
                continue;
            inst.removeModifier(LibConstants.foodUUID);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.foodUUID, "foodBuff_" + entry.getKey().getDescriptionId(), entry.getValue(), AttributeModifier.Operation.ADDITION));
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
                    inst.removeModifier(LibConstants.foodUUID);
                    inst.removeModifier(LibConstants.foodUUIDMulti);
                });
    }

    public void updateStatsToLevel() {
        float preHealthDiff = this.getMaxHealth() - this.getHealth();
        this.prop.getAttributeGains().forEach((att, val) -> {
            AttributeInstance inst = this.getAttribute(att);
            if (inst != null) {
                inst.removeModifier(LibConstants.attributeLevelMod);
                float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
                inst.addPermanentModifier(new AttributeModifier(LibConstants.attributeLevelMod, "rf.levelMod", (this.level().getLevel() - 1) * val * multiplier, AttributeModifier.Operation.ADDITION));
                if (att == Attributes.MAX_HEALTH)
                    this.setHealth(this.getMaxHealth() - preHealthDiff);
            }
        });
    }

    public void increaseLevel() {
        this.entityData.set(entityLevel, Math.min(GeneralConfig.maxLevel, this.level().getLevel() + 1));
        this.updateStatsToLevel();
    }

    public void addXp(float amount) {
        LevelExpPair pair = this.level();
        boolean res = pair.addXP(amount, LibConstants.maxMonsterLevel, LevelCalc::xpAmountForLevelUp, () -> {
        });
        this.entityData.set(entityLevel, pair.getLevel());
        this.entityData.set(levelXP, pair.getXp());
        if (res)
            this.updateStatsToLevel();
    }

    public void updateMoveAnimation() {
    }

    public LevelExpPair getFriendlyPoints() {
        return this.friendlyPoints;
    }

    @Override
    public int friendPoints(Player player) {
        if (player.getUUID().equals(this.getOwnerUUID()))
            return this.entityData.get(friendPointsSync);
        return 0;
    }

    //=====Damage Logic

    public int moveTick() {
        return this.moveTick;
    }

    public float interpolatedMoveTick(float partialTicks) {
        return Mth.clamp((this.moveTick + (this.getMoveFlag() != 0 ? partialTicks : -partialTicks)) / (float) moveTickMax, 0, 1);
    }

    public void setMoving(boolean flag) {
        this.setMoveFlag(flag ? 1 : 0);
    }

    //=====Combat stuff

    public byte getMoveFlag() {
        return this.entityData.get(moveFlags);
    }

    public void setMoveFlag(int flag) {
        this.entityData.set(moveFlags, (byte) flag);
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
            this.level.addParticle(particle, this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0f - this.getBbWidth(), this.getY() + 0.5 + this.random.nextFloat() * this.getBbHeight(), this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0f - this.getBbWidth(), d0, d2, d3);
        }
    }

    @Override
    public void die(DamageSource cause) {
        if (!this.level.isClientSide) {
            if (this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer)
                this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage(), Util.NIL_UUID);
            this.getAnimationHandler().setAnimation(null);
        }
        super.die(cause);
    }

    @Override
    protected void tickDeath() {
        if (this.deathTime == 0) {
            this.playDeathAnimation(false);
            this.getNavigation().stop();
        }
        ++this.deathTime;
        if (this.deathTime == (this.maxDeathTime() - 5)) {
            if (!this.level.isClientSide && this.getLastHurtByMob() != null)
                LevelCalc.addXP(this.getLastHurtByMob(), this.baseXP(), this.baseMoney(), this.level().getLevel());
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
        return this.entityData.get(playDeathState);
    }

    public void setPlayDeath(boolean flag) {
        this.entityData.set(playDeathState, flag);
        if (flag) {
            this.getNavigation().stop();
            this.playDeathAnimation(false);
            this.setMoving(false);
            this.setShiftKeyDown(false);
            this.setSprinting(false);
            this.unRide();
        } else {
            this.getAnimationHandler().setAnimation(null);
        }
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

    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        super.knockback(0, xRatio, zRatio);
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
        return super.isImmobile() || this.isVehicle() || this.playDeath();
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.83F;
    }

    @Override
    public void travel(Vec3 vec) {
        if (this.shouldFreezeTravel())
            return;
        if (this.isVehicle() && this.canBeControlledByRider() && this.getControllingPassenger() instanceof LivingEntity entitylivingbase) {
            if (!this.level.isClientSide) {
                if (this.adjustRotFromRider(entitylivingbase)) {
                    this.setYRot(this.rotateClamped(this.getYRot(), entitylivingbase.getYRot(), this.getHeadRotSpeed()));
                    this.setXRot(entitylivingbase.getXRot() * 0.5f);
                }
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
            }
            float strafing = entitylivingbase.xxa * 0.5f;
            float forward = entitylivingbase.zza;
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
                    float speed = this.getSpeed();
                    double motionY = Math.min(this.getDeltaMovement().y + speed, this.maxAscensionSpeed());
                    this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, motionY, this.getDeltaMovement().z));
                    if (forward > 0.0f) {
                        float f = Mth.sin(this.getYRot() * 0.017453292f);
                        float f2 = Mth.cos(this.getYRot() * 0.017453292f);
                        speed *= 0.5;
                        this.setDeltaMovement(this.getDeltaMovement().add(-speed * f, 0, speed * f2));
                    }
                }
            }
            this.flyingSpeed = this.getSpeed() * 0.1f;
            if (this.isControlledByLocalInstance()) {
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.7f);
                this.setMoving(forward != 0 || strafing != 0);
                this.setSprinting(forward > 0);
                forward *= this.ridingSpeedModifier();
                strafing *= this.ridingSpeedModifier();
                super.travel(new Vec3(strafing, vec.y, forward));
            } else if (entitylivingbase instanceof Player) {
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
        if (this.isVehicle() && this.canBeControlledByRider() && this.getControllingPassenger() instanceof LivingEntity entitylivingbase
                && !this.getAnimationHandler().hasAnimation()) {
            if (!this.level.isClientSide) {
                if (this.adjustRotFromRider(entitylivingbase)) {
                    this.setYRot(this.rotateClamped(this.getYRot(), entitylivingbase.getYRot(), this.getHeadRotSpeed()));
                    this.setXRot(entitylivingbase.getXRot() * 0.5f);
                }
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
            }
            float strafing = entitylivingbase.xxa * 0.5f;
            float forward = entitylivingbase.zza;
            double vert = 0;
            if (forward <= 0.0f) {
                forward *= 0.25f;
            } else {
                vert = Math.min(0, entitylivingbase.getLookAngle().y + 0.45);
                if (entitylivingbase.getXRot() > 85)
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
                this.setMoving(forward != 0 || strafing != 0);
                this.setSprinting(forward > 0);
                forward *= this.ridingSpeedModifier();
                strafing *= this.ridingSpeedModifier();
                vec = new Vec3(strafing * this.getSpeed(), vec.y, forward * this.getSpeed());
            } else if (entitylivingbase instanceof Player) {
                vec = Vec3.ZERO;
            }
            vec = vec.add(0, vert, 0);
            this.setDoJumping(false);
            this.calculateEntityAnimation(this, false);
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

    private boolean canAttackFrom(Vec3 pos) {
        return this.getRestrictRadius() == -1.0f || this.getRestrictCenter().distToCenterSqr(pos.x(), pos.y(), pos.z()) < (this.getRestrictRadius() * this.getRestrictRadius());
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1;
    }

    public void handleAttack(AnimatedAction anim) {
        if (this.isAnimOfType(anim, AnimationType.MELEE)) {
            this.getNavigation().stop();
            if (anim.getTick() == 1 && this.getTarget() != null)
                this.lookAt(this.getTarget(), 360, 90);
            if (anim.canAttack()) {
                this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
            }
        }
    }

    public void mobAttack(AnimatedAction anim, LivingEntity target, Consumer<LivingEntity> cons) {
        AABB aabb = this.calculateAttackAABB(anim, target);
        this.level.getEntitiesOfClass(LivingEntity.class, aabb, this.hitPred).forEach(cons);
        if (this.getServer() != null)
            Platform.INSTANCE.sendToAll(new S2CAttackDebug(aabb), this.getServer());
    }

    public AABB calculateAttackAABB(AnimatedAction anim, LivingEntity target) {
        return this.calculateAttackAABB(anim, target, 0.2);
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, LivingEntity target, double grow) {
        double reach = this.maxAttackRange(anim) * 0.5 + this.getBbWidth() * 0.5;
        Vec3 dir;
        if (target != null && !this.isVehicle()) {
            reach = Math.min(reach, this.distanceTo(target));
            dir = target.position().subtract(this.position()).normalize();
        } else {
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

    /**
     * 0: Move, 1: Follow, 2: Stay
     */
    public void setBehaviour(Behaviour behaviour) {
        this.entityData.set(behaviourData, behaviour.ordinal());
        this.behaviour = behaviour;
        if (!this.level.isClientSide)
            this.updateAI(false);
    }

    /**
     * 0: Move, 1: Follow, 2: Stay
     */
    public Behaviour behaviourState() {
        return this.behaviour;
    }

    protected float tamingMultiplier(ItemStack stack) {
        boolean flag = this.tamingItem().match(stack);
        return flag ? 2 : 1;
    }

    protected void tameEntity(Player owner) {
        this.restrictTo(this.blockPosition(), -1);
        this.setOwner(owner);
        this.navigation.stop();
        this.setTarget(null);
        this.level.broadcastEntityEvent(this, (byte) 10);
        this.updater.setLastUpdateDay(WorldUtils.day(this.level));
        this.setBehaviour(Behaviour.FOLLOW);
        this.level.getEntities(EntityTypeTest.forClass(Mob.class), this.getBoundingBox().inflate(32),
                e -> e instanceof OwnableEntity ownable && this.getOwnerUUID().equals(ownable.getOwnerUUID())
                        && e.getTarget() == this).forEach(e -> e.setTarget(null));
        if (owner instanceof ServerPlayer serverPlayer)
            Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> LevelCalc.levelSkill(serverPlayer, data, EnumSkills.TAMING, 10));
    }

    protected void untameEntity() {
        this.setOwner(null);
        if (this.playDeath())
            this.heal(this.getMaxHealth());
        this.setBehaviour(Behaviour.WANDER);
        this.updateAI(true);
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
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
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
        if (entity instanceof AbstractGolem g && this.getTarget() != g && g.getLastHurtByMob() != this)
            return !this.isTamed();
        return true;
    }

    public enum Behaviour {

        WANDER("monster.interact.move"),
        FOLLOW("monster.interact.follow"),
        STAY("monster.interact.sit"),
        FARM("monster.interact.farm");

        public final String interactKey;

        Behaviour(String interactKey) {
            this.interactKey = interactKey;
        }

        Behaviour next() {
            return switch (this) {
                case WANDER -> FOLLOW;
                case FOLLOW -> STAY;
                default -> WANDER;
            };
        }

        Behaviour nextAddition() {
            return switch (this) {
                case WANDER -> FOLLOW;
                case FOLLOW -> STAY;
                case STAY -> FARM;
                case FARM -> WANDER;
            };
        }
    }
}
