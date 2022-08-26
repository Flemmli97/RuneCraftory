package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.SimpleEffect;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.config.values.EntityProperties;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.FollowOwnerGoalMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.HurtByTargetPredicate;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.LookAtAliveGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.RandomLookGoalAlive;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.RiderAttackTargetGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.StayGoal;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemObjectX;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.network.S2CAttackDebug;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
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
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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

public abstract class BaseMonster extends PathfinderMob implements Enemy, IAnimated, IExtendedMob {

    private static final EntityDataAccessor<Optional<UUID>> owner = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> mobLevel = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> levelXP = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> moveFlags = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> behaviour = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.BYTE);

    private static final UUID attributeLevelMod = UUID.fromString("EC84560E-5266-4DC3-A4E1-388b97DBC0CB");
    private static final UUID foodUUID = UUID.fromString("87A55C28-8C8C-4BFF-AF5F-9972A38CCD9D");
    private static final UUID foodUUIDMulti = UUID.fromString("A05442AC-381B-49DF-B0FA-0136B454157B");
    public final Predicate<LivingEntity> targetPred = (e) -> {
        if (e != this) {
            if (this.getControllingPassenger() instanceof Player)
                return false;
            if (this.isTamed()) {
                return e instanceof Enemy && EntityUtils.tryGetOwner(e) == null;
            }
            if (e instanceof Mob && this == ((Mob) e).getTarget())
                return true;
            return e instanceof Npc || EntityUtils.tryGetOwner(e) != null;
        }
        return false;
    };
    public final Predicate<LivingEntity> defendPred = (e) -> {
        if (e != this) {
            if (this.getControllingPassenger() instanceof Player)
                return false;
            if (this.isTamed())
                return this.getOwnerUUID().equals(EntityUtils.tryGetOwner(e));
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
    public final Predicate<LivingEntity> hitPred = (e) -> {
        if (e != this) {
            if (this.hasPassenger(e) || !e.canBeSeenAsEnemy())
                return false;
            if (e instanceof Mob && this == ((Mob) e).getTarget())
                return true;
            Entity controller = this.getControllingPassenger();
            if (this.isTamed()) {
                if (e == this.getTarget())
                    return true;
                UUID owner = EntityUtils.tryGetOwner(e);
                if (owner != null && (!this.attackOtherTamedMobs() || owner.equals(this.getOwnerUUID())))
                    return false;
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
            return riderTarget || e == this.getTarget() || e instanceof Npc || EntityUtils.tryGetOwner(e) != null || e instanceof Player;
        }
        return false;
    };
    protected int tamingTick = -1;
    private Runnable delayedTaming;
    protected int feedTimeOut;
    private boolean doJumping = false;
    private int foodBuffTick;

    private int[] friendlyPoints = {0, 0};
    private Map<ItemStack, Integer> dailyDrops;

    private final DailyMonsterUpdater updater = new DailyMonsterUpdater(this);

    public BaseMonster(EntityType<? extends BaseMonster> type, Level world) {
        super(type, world);
        this.moveControl = new NewMoveController(this);
        this.prop = MobConfig.propertiesMap.getOrDefault(PlatformUtils.INSTANCE.entities().getIDFrom(type), EntityProperties.defaultProp);
        this.applyAttributes();
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
        this.goalSelector.addGoal(0, new StayGoal(this));
        this.goalSelector.addGoal(1, new RandomLookGoalAlive(this));
        this.goalSelector.addGoal(2, new LookAtAliveGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(3, new FollowOwnerGoalMonster(this, 1.05, 9, 3));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0));
        this.goalSelector.addGoal(6, this.wander);
    }

    private void updateAI() {
        if (this.isTamed()) {
            switch (this.behaviourState()) {
                case 0 -> {
                    this.restrictTo(this.blockPosition(), 9);
                    this.goalSelector.addGoal(6, this.wander);
                }
                case 1 -> {
                    this.clearRestriction();
                    this.goalSelector.addGoal(6, this.wander);
                }
                case 2 -> this.goalSelector.removeGoal(this.wander);
            }
        }
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
        this.entityData.define(mobLevel, LibConstants.baseLevel);
        this.entityData.define(levelXP, 0);
        this.entityData.define(moveFlags, (byte) 0);
        this.entityData.define(behaviour, (byte) 0);
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
        }
        super.handleEntityEvent(id);
    }

    @Override
    public void tick() {
        this.getAnimationHandler().tick();
        super.tick();
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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MobLevel", this.level());
        if (this.isTamed())
            compound.putUUID("Owner", this.getOwnerUUID());
        compound.putInt("Behaviour", this.behaviourState());
        compound.putBoolean("Out", this.dead);
        compound.putInt("FeedTime", this.feedTimeOut);
        if (this.hasRestriction())
            compound.putIntArray("Home", new int[]{this.getRestrictCenter().getX(), this.getRestrictCenter().getY(), this.getRestrictCenter().getZ(), (int) this.getRestrictRadius()});
        compound.putInt("FoodBuffTick", this.foodBuffTick);
        compound.putIntArray("FriendlyPoints", this.friendlyPoints);
        compound.put("DailyUpdater", this.updater.save());
        //CompoundTag genes = new CompoundTag();
        //this.attributeRandomizer.forEach((att, val)->genes.putInt(att.getRegistryName().toString(), val));
        //compound.put("Genes", genes);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(mobLevel, compound.getInt("MobLevel"));
        if (compound.contains("Owner"))
            this.entityData.set(owner, Optional.of(compound.getUUID("Owner")));
        this.setBehaviour(compound.getInt("Behaviour"));
        this.feedTimeOut = compound.getInt("FeedTime");
        if (compound.contains("Home")) {
            int[] home = compound.getIntArray("Home");
            this.restrictTo(new BlockPos(home[0], home[1], home[2]), home[3]);
        }
        this.dead = compound.getBoolean("Out");
        this.foodBuffTick = compound.getInt("FoodBuffTick");
        this.friendlyPoints = compound.getIntArray("FriendlyPoints");
        if (this.friendlyPoints.length != 2)
            this.friendlyPoints = new int[]{0, 0};
        this.updater.read(compound.getCompound("DailyUpdater"));
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
        if (this.isTamed() && !this.getOwnerUUID().equals(player.getUUID())) {
            player.sendMessage(new TranslatableComponent("monster.interact.notowner"), Util.NIL_UUID);
            return InteractionResult.CONSUME;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (hand == InteractionHand.MAIN_HAND) {
            if (stack.isEmpty() && player.getUUID().equals(this.getOwnerUUID())) {
                if (player.isShiftKeyDown()) {
                    this.setBehaviour((byte) (this.behaviourState() + 1));
                    if (player instanceof ServerPlayer serverPlayer)
                        serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.PLAYERS, player.getX(), player.getY(), player.getZ(), 0.5f, 0.4f));
                    switch (this.behaviourState()) {
                        case 0 -> player.sendMessage(new TranslatableComponent("monster.interact.move"), Util.NIL_UUID);
                        case 1 -> player.sendMessage(new TranslatableComponent("monster.interact.follow"), Util.NIL_UUID);
                        case 2 -> player.sendMessage(new TranslatableComponent("monster.interact.sit"), Util.NIL_UUID);
                    }
                    return InteractionResult.SUCCESS;
                } else if (this.ridable()) {
                    player.startRiding(this);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        if (player.isShiftKeyDown() && !stack.isEmpty()) {
            if (!this.isTamed()) {
                if (stack.getItem() == ModItems.tame.get()) {
                    this.tameEntity(player);
                    return InteractionResult.CONSUME;
                } else {
                    if (this.tamingTick == -1 && this.isAlive()) {
                        if (player instanceof ServerPlayer serverPlayer)
                            serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, player.getX(), player.getY(), player.getZ(), 0.7f, 1));
                        float rightItemMultiplier = this.tamingMultiplier(stack);
                        int count = stack.getCount();
                        this.applyFoodEffect(stack);
                        if (count == stack.getCount() && !player.isCreative())
                            stack.shrink(1);
                        this.tamingTick = 100;
                        float chance = EntityUtils.tamingChance(this, player, rightItemMultiplier);
                        this.delayedTaming = () -> {
                            if (chance == 0)
                                this.level.broadcastEntityEvent(this, (byte) 34);
                            else if (this.random.nextFloat() < chance) {
                                this.tameEntity(player);
                            } else {
                                this.level.broadcastEntityEvent(this, (byte) 11);
                            }
                        };
                        return InteractionResult.CONSUME;
                    }
                }
            } else {
                if (stack.getItem() == Items.STICK) {
                    this.setOwner(null);
                    if (player instanceof ServerPlayer serverPlayer)
                        serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.VILLAGER_NO, SoundSource.NEUTRAL, player.getX(), player.getY(), player.getZ(), 1, 1));
                    return InteractionResult.CONSUME;
                } else if (stack.getItem() == ModItems.inspector.get()) {
                    //open tamed gui
                } else if (this.feedTimeOut <= 0 && this.applyFoodEffect(stack)) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, player.getX(), player.getY(), player.getZ(), 0.7f, 1));
                        Platform.INSTANCE.getPlayerData(serverPlayer)
                                .ifPresent(data -> data.getDailyUpdater().onGiveMonsterItem(serverPlayer));
                    }
                    //this.feedTimeOut = 24000;
                    this.feedTimeOut = 20;
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
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

    public abstract boolean isAnimOfType(AnimatedAction anim, AnimationType type);

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
    public int level() {
        return this.entityData.get(mobLevel);
    }

    @Override
    public void setLevel(int level) {
        this.entityData.set(mobLevel, Mth.clamp(level, 1, LibConstants.maxMonsterLevel));
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
        this.eat(this.level, stack);
        if (stack.getItem() == ModItems.objectX.get())
            ItemObjectX.applyEffect(this, stack);
        this.removeFoodEffect();
        FoodProperties food = DataPackHandler.getFoodStat(stack.getItem());
        if (food == null) {
            net.minecraft.world.food.FoodProperties mcFood = stack.getItem().getFoodProperties();
            if (mcFood != null) {
                this.heal(mcFood.getNutrition() * 0.5f);
                return true;
            }
            return false;
        }
        for (Map.Entry<Attribute, Double> entry : food.effectsMultiplier().entrySet()) {
            AttributeInstance inst = this.getAttribute(entry.getKey());
            if (inst == null)
                continue;
            inst.removeModifier(foodUUIDMulti);
            inst.addPermanentModifier(new AttributeModifier(foodUUIDMulti, "foodBuffMulti_" + entry.getKey().getDescriptionId(), entry.getValue(), AttributeModifier.Operation.MULTIPLY_BASE));
        }
        for (Map.Entry<Attribute, Double> entry : food.effects().entrySet()) {
            AttributeInstance inst = this.getAttribute(entry.getKey());
            if (inst == null)
                continue;
            inst.removeModifier(foodUUID);
            inst.addPermanentModifier(new AttributeModifier(foodUUID, "foodBuff_" + entry.getKey().getDescriptionId(), entry.getValue(), AttributeModifier.Operation.ADDITION));
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
                    inst.removeModifier(foodUUID);
                    inst.removeModifier(foodUUIDMulti);
                });
    }

    public void updateStatsToLevel() {
        float preHealthDiff = this.getMaxHealth() - this.getHealth();
        this.prop.getAttributeGains().forEach((att, val) -> {
            AttributeInstance inst = this.getAttribute(att);
            if (inst != null) {
                inst.removeModifier(attributeLevelMod);
                float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0);
                inst.addPermanentModifier(new AttributeModifier(attributeLevelMod, "rf.levelMod", (this.level() - 1) * val * multiplier, AttributeModifier.Operation.ADDITION));
                if (att == Attributes.MAX_HEALTH)
                    this.setHealth(this.getMaxHealth() - preHealthDiff);
            }
        });
    }

    public void increaseLevel() {
        this.entityData.set(mobLevel, Math.min(GeneralConfig.maxLevel, this.level() + 1));
        this.updateStatsToLevel();
    }

    public int getLevelXp() {
        return this.entityData.get(levelXP);
    }

    public void addXp(int amount) {
        int neededXP = LevelCalc.xpAmountForLevelUp(this.level());
        int xpToNextLevel = neededXP - this.getLevelXp();
        if (amount >= xpToNextLevel) {
            int diff = amount - xpToNextLevel;
            this.increaseLevel();
            this.addXp(diff);
        } else {
            this.entityData.set(levelXP, amount);
        }
    }

    public void updateMoveAnimation() {
    }

    /**
     * @return int[] in form of {level, xp}
     */
    public int[] getFriendlyPoints() {
        return this.friendlyPoints;
    }

    //=====Damage Logic

    public boolean isMoving() {
        return this.getMoveFlag() != 0;
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
        //if (!this.isTamed()) {
        if (this.deathTime == 0) {
            this.playDeathAnimation();
            this.getNavigation().stop();
        }
        ++this.deathTime;
        if (this.deathTime == (this.maxDeathTime() - 5) && this.lastHurtByPlayer instanceof ServerPlayer) {
            Platform.INSTANCE.getPlayerData(this.lastHurtByPlayer).ifPresent(data -> {
                LevelCalc.addXP((ServerPlayer) this.lastHurtByPlayer, data, LevelCalc.getMobXP(data, this));
                data.setMoney(this.lastHurtByPlayer, data.getMoney() + LevelCalc.getMoney(this.baseMoney(), this.level()));
            });
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
        //}
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player player && player.getUUID().equals(this.getOwnerUUID())
                && !player.isShiftKeyDown()) {
            return false;
        }
        return (source.getEntity() == null || this.canAttackFrom(source.getEntity().blockPosition())) && super.hurt(source, amount);
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
        return super.isImmobile() || this.isVehicle();
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
            if (this.adjustRotFromRider(entitylivingbase)) {
                this.setYRot(entitylivingbase.getYRot());
                this.setXRot(entitylivingbase.getXRot() * 0.5f);
            }
            this.yRotO = this.getYRot();
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.yBodyRot;
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
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.setMoving(forward != 0 || strafing != 0);
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

    public boolean shouldFreezeTravel() {
        return false;
    }

    //=====Interaction

    public int maxDeathTime() {
        return 20;
    }

    protected void playDeathAnimation() {
    }

    private boolean canAttackFrom(BlockPos pos) {
        return this.getRestrictRadius() == -1.0f || this.getRestrictCenter().distSqr(pos) < (this.getRestrictRadius() * this.getRestrictRadius());
    }

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
        return this.entityData.get(behaviour) == 2;
    }

    /**
     * 0: Move, 1: Follow, 2: Stay
     */
    public void setBehaviour(int flag) {
        byte b = (byte) (flag % 3);
        this.entityData.set(behaviour, b);
        if (!this.level.isClientSide)
            this.updateAI();
    }

    /**
     * 0: Move, 1: Follow, 2: Stay
     */
    public byte behaviourState() {
        return this.entityData.get(behaviour);
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
        this.setBehaviour(1);
        if (owner instanceof ServerPlayer serverPlayer)
            Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> LevelCalc.levelSkill(serverPlayer, data, EnumSkills.TAMING, 10));
    }

    @Override
    protected void addPassenger(Entity passenger) {
        this.getNavigation().stop();
        this.setDeltaMovement(Vec3.ZERO);
        this.setYya(0);
        this.setZza(0);
        this.setXxa(0);
        super.addPassenger(passenger);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        if (passenger == this.getOwner())
            this.setBehaviour(1);
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

    public void handleLandTravel(Vec3 vec) {
        this.flyingSpeed = 0.02f;
        super.travel(vec);
    }

    public void handleNoGravTravel(Vec3 vec) {
        if (this.isVehicle() && this.canBeControlledByRider() && this.getControllingPassenger() instanceof LivingEntity entitylivingbase
                && !this.getAnimationHandler().hasAnimation()) {
            if (this.adjustRotFromRider(entitylivingbase)) {
                this.setYRot(entitylivingbase.getYRot());
                this.setXRot(entitylivingbase.getXRot() * 0.5f);
            }
            this.yRotO = this.getYRot();
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.yBodyRot;
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
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.setMoving(forward != 0 || strafing != 0);
                vec = new Vec3(strafing, vec.y, forward);
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

    public double ridingSpeedModifier() {
        return 0.85;
    }

    /**
     * @return For flying entities: The max speed for flying up
     */
    public double maxAscensionSpeed() {
        return this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 2;
    }

}
