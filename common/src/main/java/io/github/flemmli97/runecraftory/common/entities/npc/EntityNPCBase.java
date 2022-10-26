package io.github.flemmli97.runecraftory.common.entities.npc;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.SimpleEffect;
import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import io.github.flemmli97.runecraftory.common.entities.ai.AvoidWhenNotFollowing;
import io.github.flemmli97.runecraftory.common.entities.ai.LookAtAliveGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.NPCFindPOI;
import io.github.flemmli97.runecraftory.common.entities.ai.NPCFollowGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.NPCWanderGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.RandomLookGoalAlive;
import io.github.flemmli97.runecraftory.common.entities.ai.StayGoal;
import io.github.flemmli97.runecraftory.common.inventory.InventoryShop;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerShop;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemObjectX;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.network.S2CAttackDebug;
import io.github.flemmli97.runecraftory.common.network.S2COpenNPCGui;
import io.github.flemmli97.runecraftory.common.network.S2CUpdateNPCData;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.mixin.AttributeMapAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EntityNPCBase extends AgeableMob implements Npc, IBaseMob, IAnimated {

    private static final EntityDataAccessor<Integer> entityLevel = SynchedEntityData.defineId(EntityNPCBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> levelXP = SynchedEntityData.defineId(EntityNPCBase.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> playDeathState = SynchedEntityData.defineId(EntityNPCBase.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> shopSync = SynchedEntityData.defineId(EntityNPCBase.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> personalitySync = SynchedEntityData.defineId(EntityNPCBase.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> male = SynchedEntityData.defineId(EntityNPCBase.class, EntityDataSerializers.BOOLEAN);

    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.MEETING_POINT);

    private final AnimationHandler<EntityNPCBase> animationHandler = new AnimationHandler<>(this, AnimatedAction.vanillaAttackOnly);

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
            LivingEntity follow = this.followEntity();
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

    private EnumShop shop = EnumShop.NONE;
    private EnumNPCPersonality personality = EnumNPCPersonality.ALL;

    private Activity activity = Activity.IDLE;

    private int foodBuffTick;
    private int playDeathTick;

    private final Map<UUID, NPCFriendPoints> playerHearts = new HashMap<>();
    private UUIDNameMapper fatherUUID;
    private UUIDNameMapper motherUUID;
    private UUIDNameMapper[] childUUIDs;

    private LivingEntity entityToFollow;
    private UUID entityToFollowUUID;

    private int sleepCooldown;

    private boolean isStaying;
    private Set<Integer> interactingPlayers = new HashSet<>();

    private final NPCSchedule schedule;

    public EntityNPCBase(EntityType<? extends EntityNPCBase> type, Level level) {
        super(type, level);
        this.applyAttributes();
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

    protected void applyAttributes() {
        AttributeInstance inst = this.getAttribute(Attributes.MAX_HEALTH);
        if (inst != null) {
            inst.setBaseValue(MobConfig.npcHealth);
            this.setHealth(this.getMaxHealth());
        }
        inst = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (inst != null) {
            inst.setBaseValue(MobConfig.npcAttack);
        }
        inst = this.getAttribute(ModAttributes.RF_DEFENCE.get());
        if (inst != null) {
            inst.setBaseValue(MobConfig.npcDefence);
        }
        inst = this.getAttribute(ModAttributes.RF_MAGIC.get());
        if (inst != null) {
            inst.setBaseValue(MobConfig.npcMagicAttack);
        }
        inst = this.getAttribute(ModAttributes.RF_MAGIC_DEFENCE.get());
        if (inst != null) {
            inst.setBaseValue(MobConfig.npcMagicDefence);
        }
    }

    public void addGoal() {
        this.targetSelector.addGoal(1, this.targetMobs);
        this.targetSelector.addGoal(0, this.hurt);

        this.goalSelector.addGoal(0, new NPCFindPOI(this));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new StayGoal<>(this, StayGoal.CANSTAYNPC));
        this.goalSelector.addGoal(0, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(1, new RandomLookGoalAlive(this));
        this.goalSelector.addGoal(1, new AvoidWhenNotFollowing(this, LivingEntity.class, 8, 1.3, 1.2));
        this.goalSelector.addGoal(2, new LookAtAliveGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1, true));
        this.goalSelector.addGoal(3, new NPCFollowGoal(this, 1.05, 9, 3));
        this.goalSelector.addGoal(4, this.wander);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(entityLevel, LibConstants.baseLevel);
        this.entityData.define(levelXP, 0f);
        this.entityData.define(playDeathState, false);
        this.entityData.define(shopSync, (byte) 0);
        this.entityData.define(personalitySync, (byte) 0);
        this.entityData.define(male, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (this.level.isClientSide) {
            if (key == shopSync) {
                try {
                    this.shop = EnumShop.values()[this.entityData.get(shopSync)];
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
            if (key == personalitySync) {
                try {
                    this.personality = EnumNPCPersonality.values()[this.entityData.get(personalitySync)];
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
        GroundPathNavigation nav = new GroundPathNavigation(this, level);
        nav.setCanOpenDoors(true);
        return nav;
    }

    @Override
    public void tick() {
        this.getAnimationHandler().tick();
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
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!(player instanceof ServerPlayer serverPlayer))
            return InteractionResult.CONSUME;
        if (this.isSleeping())
            return InteractionResult.CONSUME;
        if (this.getEntityToFollowUUID() != null && this.getEntityToFollowUUID().equals(serverPlayer.getUUID())) {
            serverPlayer.connection.send(new ClientboundUpdateAttributesPacket(this.getId(), ((AttributeMapAccessor) this.getAttributes())
                    .getAttributes().values()));
        }
        Platform.INSTANCE.sendToClient(new S2CUpdateNPCData(this, this.getFriendPointData(serverPlayer).save()), serverPlayer);
        Platform.INSTANCE.sendToClient(new S2COpenNPCGui(this, serverPlayer), serverPlayer);
        this.interactingPlayers.add(serverPlayer.getId());
        return InteractionResult.CONSUME;
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
        boolean react = this.playerHearts.computeIfAbsent(player.getUUID(), uuid -> new NPCFriendPoints())
                .talkTo(this.level, 15);
        if (!player.isCreative())
            stack.shrink(1);
    }

    public void talkTo(Player player) {
        boolean doGreet = this.playerHearts.computeIfAbsent(player.getUUID(), uuid -> new NPCFriendPoints())
                .talkTo(this.level, 15);
        player.sendMessage(new TranslatableComponent("npc.generic.greet", player.getName()), Util.NIL_UUID);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return CombatUtils.mobAttack(this, entity);
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

    public void updateStatsToLevel() {
        float preHealthDiff = this.getMaxHealth() - this.getHealth();
        AttributeInstance inst = this.getAttribute(Attributes.MAX_HEALTH);
        if (inst != null) {
            inst.removeModifier(LibConstants.attributeLevelMod);
            float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.attributeLevelMod, "rf.levelMod", (this.level().getLevel() - 1) * MobConfig.npcHealthGain * multiplier, AttributeModifier.Operation.ADDITION));
            this.setHealth(this.getMaxHealth() - preHealthDiff);
        }
        inst = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (inst != null) {
            inst.removeModifier(LibConstants.attributeLevelMod);
            float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.attributeLevelMod, "rf.levelMod", (this.level().getLevel() - 1) * MobConfig.npcAttackGain * multiplier, AttributeModifier.Operation.ADDITION));
        }
        inst = this.getAttribute(ModAttributes.RF_DEFENCE.get());
        if (inst != null) {
            inst.removeModifier(LibConstants.attributeLevelMod);
            float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.attributeLevelMod, "rf.levelMod", (this.level().getLevel() - 1) * MobConfig.npcDefenceGain * multiplier, AttributeModifier.Operation.ADDITION));
        }
        inst = this.getAttribute(ModAttributes.RF_MAGIC.get());
        if (inst != null) {
            inst.removeModifier(LibConstants.attributeLevelMod);
            float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.attributeLevelMod, "rf.levelMod", (this.level().getLevel() - 1) * MobConfig.npcMagicAttackGain * multiplier, AttributeModifier.Operation.ADDITION));
        }
        inst = this.getAttribute(ModAttributes.RF_MAGIC_DEFENCE.get());
        if (inst != null) {
            inst.removeModifier(LibConstants.attributeLevelMod);
            float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
            inst.addPermanentModifier(new AttributeModifier(LibConstants.attributeLevelMod, "rf.levelMod", (this.level().getLevel() - 1) * MobConfig.npcMagicDefenceGain * multiplier, AttributeModifier.Operation.ADDITION));
        }
    }

    private NPCFriendPoints getFriendPointData(Player player) {
        return this.playerHearts.computeIfAbsent(player.getUUID(), uuid -> new NPCFriendPoints());
    }

    @Override
    public int friendPoints(Player player) {
        return this.playerHearts.computeIfAbsent(player.getUUID(), uuid -> new NPCFriendPoints()).points.getLevel();
    }

    public void updateFriendPointsFrom(Player player, CompoundTag tag) {
        this.playerHearts.computeIfAbsent(player.getUUID(), uuid -> new NPCFriendPoints()).load(tag);
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

    @Override
    public void die(DamageSource cause) {
        if (!this.level.isClientSide) {
            this.getAnimationHandler().setAnimation(null);
        }
        super.die(cause);
    }

    @Override
    public void remove(RemovalReason reason) {
        this.releasePOI(this.getBedPos());
        this.releasePOI(this.getWorkPlace());
        this.releasePOI(this.getMeetingPos());
        super.remove(reason);
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
    }

    public boolean playDeath() {
        return this.entityData.get(playDeathState);
    }

    public void setPlayDeath(boolean flag) {
        this.entityData.set(playDeathState, flag);
        if (flag) {
            this.getNavigation().stop();
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
    public boolean hurt(DamageSource source, float amount) {
        if (this.playDeath() && source != DamageSource.OUT_OF_WORLD)
            return false;
        return super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (damageSrc != DamageSource.OUT_OF_WORLD && this.getHealth() <= 0 && this.followEntity() != null) {
            this.setHealth(0.01f);
            this.setPlayDeath(true);
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
        compound.putInt("Shop", this.getShop().ordinal());
        compound.putInt("Personality", this.getPersonality().ordinal());
        compound.putInt("FoodBuffTick", this.foodBuffTick);
        compound.putBoolean("PlayDeath", this.entityData.get(playDeathState));

        CompoundTag heartsTag = new CompoundTag();
        this.playerHearts.forEach((uuid, hearts) -> heartsTag.put(uuid.toString(), hearts.save()));
        compound.put("PlayerHearts", heartsTag);

        if (this.entityToFollowUUID != null)
            compound.putUUID("EntityToFollow", this.entityToFollowUUID);

        compound.put("Schedule", this.schedule.save());

        compound.putBoolean("Staying", this.isStaying);

        compound.putBoolean("Male", this.isMale());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.levelPair.read(compound.getCompound("MobLevel"));
        this.entityData.set(entityLevel, this.levelPair.getLevel());
        this.entityData.set(levelXP, this.levelPair.getXp());
        try {
            this.setShop(EnumShop.values()[compound.getInt("Shop")]);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            this.setPersonality(EnumNPCPersonality.values()[compound.getInt("Personality")]);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        this.foodBuffTick = compound.getInt("FoodBuffTick");
        this.setPlayDeath(compound.getBoolean("PlayDeath"));

        CompoundTag heartsTag = compound.getCompound("PlayerHearts");
        heartsTag.getAllKeys().forEach(key -> {
            NPCFriendPoints points = new NPCFriendPoints();
            points.load(heartsTag.getCompound(key));
            this.playerHearts.put(UUID.fromString(key), points);
        });

        if (compound.hasUUID("EntityToFollow"))
            this.entityToFollowUUID = compound.getUUID("EntityToFollow");
        this.schedule.load(compound.getCompound("Schedule"));
        this.stayHere(compound.getBoolean("Staying"));
        this.setMale(compound.getBoolean("Male"));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.COMMAND || reason == MobSpawnType.SPAWN_EGG || reason == MobSpawnType.SPAWNER || reason == MobSpawnType.DISPENSER || reason == MobSpawnType.NATURAL) {
            this.randomizeData();
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    public void handleAttack(AnimatedAction anim) {
        this.getNavigation().stop();
        if (anim.getTick() == 1 && this.getTarget() != null)
            this.lookAt(this.getTarget(), 360, 90);
        if (anim.canAttack()) {
            this.mobAttack(anim, this.getTarget(), this::doHurtTarget);
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

    public double maxAttackRange(AnimatedAction anim) {
        return 1;
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

    public EnumShop getShop() {
        return this.shop;
    }

    public void setShop(EnumShop shop) {
        this.shop = shop;
        if (!this.level.isClientSide)
            this.entityData.set(shopSync, (byte) shop.ordinal());
    }

    public EnumNPCPersonality getPersonality() {
        return this.personality;
    }

    public void setPersonality(EnumNPCPersonality personality) {
        this.personality = personality;
        if (!this.level.isClientSide)
            this.entityData.set(personalitySync, (byte) personality.ordinal());
    }

    public boolean updateActivity() {
        if (this.tickCount % 20 == 0 && this.level instanceof ServerLevel serverLevel) {
            Activity prev = this.activity;
            this.activity = this.getActivityForTime(serverLevel);
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
        if (this.shop == EnumShop.NONE)
            return ShopState.NOTWORKER;
        if (this.shop == EnumShop.RANDOM)
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

    public LivingEntity followEntity() {
        if (this.entityToFollowUUID != null) {
            if (this.entityToFollow == null || !this.entityToFollow.isAlive())
                this.entityToFollow = EntityUtil.findFromUUID(LivingEntity.class, this.level, this.entityToFollowUUID);
        }
        return this.entityToFollow;
    }

    public UUID getEntityToFollowUUID() {
        return this.entityToFollowUUID;
    }

    public void followEntity(LivingEntity entity) {
        this.stayHere(false);
        this.entityToFollow = entity;
        if (entity != null)
            this.entityToFollowUUID = entity.getUUID();
        else
            this.entityToFollowUUID = null;
    }

    public void followAtDistance(LivingEntity entity) {
        this.followEntity(entity);
    }

    public boolean isStaying() {
        return this.interactingPlayers.size() > 0 || this.isStaying;
    }

    public void stayHere(boolean flag) {
        this.isStaying = flag;
    }

    public void decreaseInteractingPlayers(Player player) {
        this.interactingPlayers.remove(player.getId());
    }

    public boolean isMale() {
        return this.entityData.get(male);
    }

    public void setMale(boolean flag) {
        this.entityData.set(male, flag);
    }

    public void openShopForPlayer(ServerPlayer player) {
        if (this.canTrade() == ShopState.OPEN) {
            this.interactingPlayers.add(player.getId());
            Platform.INSTANCE.getPlayerData(player).map(d -> {
                if (EntityNPCBase.this.getShop() != EnumShop.NONE)
                    return d.getShop(EntityNPCBase.this.getShop());
                return null;
            }).ifPresent(shopList -> {
                shopList.addAll(DataPackHandler.getDefaultItems(EntityNPCBase.this.getShop()));
                Platform.INSTANCE.openGuiMenu(player, new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return new TranslatableComponent(EntityNPCBase.this.getShop().translationKey);
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
                });
            });
        }
    }

    public void randomizeData() {
        this.setPersonality(EnumNPCPersonality.values()[this.random.nextInt(EnumNPCPersonality.values().length)]);
        this.setShop(EnumShop.values()[this.random.nextInt(EnumShop.values().length)]);
        this.schedule.load(new NPCSchedule(this, this.random).save());
        this.setMale(this.random.nextBoolean());
    }
}