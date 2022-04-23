package io.github.flemmli97.runecraftory.common.entities;

import com.google.common.collect.ImmutableSet;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
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
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.network.S2CAttackDebug;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
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
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class BaseMonster extends PathfinderMob implements Enemy, IAnimated, IExtendedMob {

    private static final EntityDataAccessor<Optional<UUID>> owner = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> mobLevel = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> levelXP = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> moveFlags = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> staying = SynchedEntityData.defineId(BaseMonster.class, EntityDataSerializers.BOOLEAN);

    private static final UUID attributeLevelMod = UUID.fromString("EC84560E-5266-4DC3-A4E1-388b97DBC0CB");
    private static final UUID foodUUID = UUID.fromString("87A55C28-8C8C-4BFF-AF5F-9972A38CCD9D");
    private static final UUID foodUUIDMulti = UUID.fromString("A05442AC-381B-49DF-B0FA-0136B454157B");
    private final EntityProperties prop;

    protected int tamingTick = -1;

    protected int feedTimeOut;
    private boolean doJumping = false;
    private int foodBuffTick;
    private Set<Attribute> foodAtts;

    public final Predicate<LivingEntity> hitPred = (e) -> {
        if (e != this) {
            if (this.hasPassenger(e))
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
    public final Predicate<LivingEntity> targetPred = (e) -> {
        if (e != this) {
            if (e instanceof Mob && this == ((Mob) e).getTarget())
                return true;
            if (this.isTamed()) {
                return e instanceof Enemy;
            }
            return e instanceof Npc || EntityUtils.tryGetOwner(e) != null;
        }
        return false;
    };

    public final Predicate<LivingEntity> defendPred = (e) -> {
        if (!e.equals(BaseMonster.this)) {
            if (BaseMonster.this.isTamed()) {
                return !e.equals(BaseMonster.this.getOwner());
            }
            return true;
        }
        return false;
    };

    public NearestAttackableTargetGoal<Player> targetPlayer = new NearestAttackableTargetGoal<>(this, Player.class, 5, true, true, player -> !this.isTamed());//|| player != BaseMonster.this.getOwner());
    public NearestAttackableTargetGoal<Mob> targetMobs = new NearestAttackableTargetGoal<>(this, Mob.class, 5, true, true, this.targetPred);

    public FloatGoal swimGoal = new FloatGoal(this);
    public RandomStrollGoal wander = new WaterAvoidingRandomStrollGoal(this, 1.0);

    public HurtByTargetPredicate hurt = new HurtByTargetPredicate(this, this.defendPred);

    //private Map<Attribute, Integer> attributeRandomizer = new HashMap<>();

    public BaseMonster(EntityType<? extends BaseMonster> type, Level world) {
        super(type, world);
        this.moveControl = new NewMoveController(this);
        this.prop = MobConfig.propertiesMap.getOrDefault(PlatformUtils.INSTANCE.entities().getIDFrom(type), EntityProperties.defaultProp);
        this.applyAttributes();
        this.addGoal();
    }

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

    public static AttributeSupplier.Builder createAttributes(Collection<? extends RegistryEntrySupplier<Attribute>> atts) {
        AttributeSupplier.Builder map = Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.22);
        if (atts != null)
            for (RegistryEntrySupplier<Attribute> att : atts)
                map.add(att.get());
        return map;
    }

    public void addGoal() {
        this.targetSelector.addGoal(1, this.targetPlayer);
        this.targetSelector.addGoal(2, this.targetMobs);
        this.targetSelector.addGoal(0, this.hurt);
        this.targetSelector.addGoal(3, new RiderAttackTargetGoal(this, 15));

        this.goalSelector.addGoal(0, new StayGoal(this));
        this.goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 1.0));
        this.goalSelector.addGoal(3, this.wander);
        this.goalSelector.addGoal(4, this.swimGoal);
        this.goalSelector.addGoal(5, new RandomLookGoalAlive(this));
        this.goalSelector.addGoal(6, new LookAtAliveGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(7, new FollowOwnerGoalMonster(this, 1, 16, 3));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(owner, Optional.empty());
        this.entityData.define(mobLevel, LibConstants.baseLevel);
        this.entityData.define(levelXP, 0);
        this.entityData.define(moveFlags, (byte) 0);
        this.entityData.define(staying, false);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.populateDefaultEquipmentSlots(difficulty);
        //for(Attribute att : this.prop.getAttributeGains().keySet())
        //    this.attributeRandomizer.put(att, this.rand.nextInt(5)-2);
        return spawnData;
    }

    //=====Animation Stuff

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
        return this.prop.dailyDrops().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getStack(), Map.Entry::getValue));
    }

    @Override
    public float tamingChance() {
        return this.prop.tamingChance();
    }

    @Override
    public boolean isTamed() {
        return this.getOwnerUUID() != null;
    }

    public boolean attackOtherTamedMobs() {
        return false;
    }

    @Override
    public boolean ridable() {
        return this.prop.ridable();
    }

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
        this.entityData.set(mobLevel, Math.min(10000, level));
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
    public void applyFoodEffect(ItemStack stack) {
        if (this.level.isClientSide)
            return;
        this.removeFoodEffect();
        FoodProperties food = DataPackHandler.getFoodStat(stack.getItem());
        if (food == null)
            return;
        ImmutableSet.Builder<Attribute> builder = new ImmutableSet.Builder<>();
        for (Map.Entry<Attribute, Double> entry : food.effectsMultiplier().entrySet()) {
            AttributeInstance inst = this.getAttribute(entry.getKey());
            if (inst == null)
                continue;
            inst.removeModifier(foodUUIDMulti);
            inst.addPermanentModifier(new AttributeModifier(foodUUIDMulti, "foodBuffMulti_" + entry.getKey().getDescriptionId(), entry.getValue(), AttributeModifier.Operation.MULTIPLY_BASE));
            builder.add(entry.getKey());
        }
        for (Map.Entry<Attribute, Double> entry : food.effects().entrySet()) {
            AttributeInstance inst = this.getAttribute(entry.getKey());
            if (inst == null)
                continue;
            inst.removeModifier(foodUUID);
            inst.addPermanentModifier(new AttributeModifier(foodUUID, "foodBuff_" + entry.getKey().getDescriptionId(), entry.getValue(), AttributeModifier.Operation.ADDITION));
            builder.add(entry.getKey());
        }
        this.foodBuffTick = food.duration();
        this.heal(food.getHPGain());
        this.heal(this.getMaxHealth() * food.getHpPercentGain() * 0.01F);
    }

    @Override
    public void removeFoodEffect() {
        ((AttributeMapAccessor) this.getAttributes())
                .getAttributes().values().forEach(inst -> {
                    inst.removeModifier(foodUUID);
                    inst.removeModifier(foodUUIDMulti);
                });
    }

    //=====Level Handling

    public void updateStatsToLevel() {
        float preHealthDiff = this.getMaxHealth() - this.getHealth();
        this.prop.getAttributeGains().forEach((att, val) -> {
            AttributeInstance inst = this.getAttribute(att);
            if (inst != null) {
                inst.removeModifier(attributeLevelMod);
                float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0) * 0.5f;
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

    //=====Movement Handling

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

    public void updateMoveAnimation() {
    }

    public boolean isMoving() {
        return this.getMoveFlag() != 0;
    }

    public void setMoving(boolean flag) {
        this.setMoveFlag(flag ? 1 : 0);
    }

    public void setMoveFlag(int flag) {
        this.entityData.set(moveFlags, (byte) flag);
    }

    public byte getMoveFlag() {
        return this.entityData.get(moveFlags);
    }

    public void setDoJumping(boolean jump) {
        this.doJumping = jump;
    }

    public boolean doJumping() {
        return this.doJumping;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        if (!this.isFlyingEntity()) {
            super.causeFallDamage(fallDistance, multiplier, source);
        }
        return false;
    }

    //=====Client
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 10) {
            this.playTameEffect(true);
        } else if (id == 11) {
            this.playTameEffect(false);
        } else if (id == 34) {
            this.tamingTick = 120;
        }
        super.handleEntityEvent(id);
    }

    private void playTameEffect(boolean play) {
        SimpleParticleType particle = ParticleTypes.HEART;
        if (!play) {
            particle = ParticleTypes.SMOKE;
        }
        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02;
            double d2 = this.random.nextGaussian() * 0.02;
            double d3 = this.random.nextGaussian() * 0.02;
            this.level.addParticle(particle, this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0f - this.getBbWidth(), this.getY() + 0.5 + this.random.nextFloat() * this.getBbHeight(), this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0f - this.getBbWidth(), d0, d2, d3);
        }
    }

    //=====Damage Logic

    @Override
    public boolean doHurtTarget(Entity entity) {
        return CombatUtils.mobAttack(this, entity);
    }

    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        super.knockback(0, xRatio, zRatio);
    }

    //=====Combat stuff

    //TODO: Redo Death animation. if server lagging behind client finished animation
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

    public int maxDeathTime() {
        return 20;
    }

    protected void playDeathAnimation() {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return (source.getEntity() == null || this.canAttackFrom(source.getEntity().blockPosition())) && super.hurt(source, amount);
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
        return this.calculateAttackAABB(anim, target, 0);
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

    //=====Interaction

    public void setStaying(boolean flag) {
        this.entityData.set(staying, flag);
    }

    public boolean isStaying() {
        return this.entityData.get(staying);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.isTamed();
    }

    @Override
    public void tick() {
        this.getAnimationHandler().tick();
        super.tick();
        if (!this.level.isClientSide) {
            if (this.tamingTick > 0) {
                --this.tamingTick;
            }
            if (this.tamingTick == 0) {
                if (this.isTamed()) {
                    this.level.broadcastEntityEvent(this, (byte) 10);
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 11);
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
    public ItemStack getPickResult() {
        return SpawnEgg.fromType(this.getType()).map(ItemStack::new).orElse(ItemStack.EMPTY);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.level.isClientSide)
            return InteractionResult.PASS;
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && player.isShiftKeyDown()) {
            if (stack.getItem() == ModItems.tame.get() && !this.isTamed()) {
                this.tameEntity(player);
            } else if (!this.isTamed()) {
                if (this.tamingTick == -1 && this.isAlive()) {
                    float rightItemMultiplier = this.tamingMultiplier(stack);
                    if (rightItemMultiplier == 0)
                        return InteractionResult.PASS;
                    if (!player.isCreative())
                        stack.shrink(1);
                    if (this.random.nextFloat() <= EntityUtils.tamingChance(this, rightItemMultiplier))
                        this.tameEntity(player);
                    if (stack.getItem().isEdible())
                        this.applyFoodEffect(stack);
                    this.tamingTick = 100;
                    this.level.broadcastEntityEvent(this, (byte) 34);
                }
            } else {
                if (stack.getItem() == Items.STICK) {
                    this.setOwner(null);
                } else if (stack.getItem() == ModItems.inspector.get()) {
                    //open tamed gui
                } else if (this.feedTimeOut <= 0 && stack.getItem().isEdible()) {
                    this.applyFoodEffect(stack);
                    this.feedTimeOut = 24000;
                }
            }
            return InteractionResult.SUCCESS;
        } else if (stack.isEmpty() && player.isShiftKeyDown()) {
            this.setStaying(!this.isStaying());
            if (player instanceof ServerPlayer serverPlayer)
                serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.PLAYERS, player.getX(), player.getY(), player.getZ(), 1, 1));
            if (this.isStaying()) {
                player.sendMessage(new TranslatableComponent("monster.interact.sit"), Util.NIL_UUID);
            } else
                player.sendMessage(new TranslatableComponent("monster.interact.follow"), Util.NIL_UUID);
        } else if (stack.isEmpty() && !this.level.isClientSide && player == this.getOwner() && this.ridable()) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        } else {
            //Notify not owned this entity
        }
        return InteractionResult.PASS;
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
    }

    @Override
    public boolean canBeControlledByRider() {
        return this.isTamed() && this.ridable();
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return this.canBeControlledByRider() && !this.level.isClientSide;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        this.targetSelector.removeGoal(this.targetPlayer);
        this.targetSelector.removeGoal(this.targetMobs);
        super.addPassenger(passenger);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        this.targetSelector.addGoal(1, this.targetPlayer);
        this.targetSelector.addGoal(2, this.targetMobs);
        super.removePassenger(passenger);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() && this.isVehicle() && this.tamingTick <= 0;
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public void travel(Vec3 vec) {
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

    @Override
    protected float getWaterSlowDown() {
        return 0.83F;
    }

    public boolean adjustRotFromRider(LivingEntity rider) {
        return true;
    }

    public void handleLandTravel(Vec3 vec) {
        this.flyingSpeed = 0.02f;
        super.travel(vec);
    }

    public void handleWaterTravel(Vec3 vec) {
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
            double up = 0;
            if (forward <= 0.0f) {
                forward *= 0.25f;
            } else {
                up = Math.min(0, entitylivingbase.getLookAngle().y + 0.45);
            }
            if (this.doJumping()) {
                up += Math.min(this.getDeltaMovement().y + 0.5, this.maxAscensionSpeed());
            }
            this.flyingSpeed = this.getSpeed() * 0.1f;
            if (this.isControlledByLocalInstance()) {
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.setMoving(forward != 0 || strafing != 0);
                vec = new Vec3(strafing, vec.y, forward);
            } else if (entitylivingbase instanceof Player) {
                vec = Vec3.ZERO;
            }
            vec = vec.add(0, up, 0);
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

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MobLevel", this.level());
        if (this.isTamed())
            compound.putUUID("Owner", this.getOwnerUUID());

        compound.putBoolean("Out", this.dead);
        compound.putInt("FeedTime", this.feedTimeOut);
        if (this.hasRestriction())
            compound.putIntArray("Home", new int[]{this.getRestrictCenter().getX(), this.getRestrictCenter().getY(), this.getRestrictCenter().getZ(), (int) this.getRestrictRadius()});
        compound.putInt("FoodBuffTick", this.foodBuffTick);
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
        this.feedTimeOut = compound.getInt("FeedTime");
        if (compound.contains("Home")) {
            int[] home = compound.getIntArray("Home");
            this.restrictTo(new BlockPos(home[0], home[1], home[2]), home[3]);
        }
        this.dead = compound.getBoolean("Out");
        this.foodBuffTick = compound.getInt("FoodBuffTick");
        //CompoundTag genes = compound.getCompound("Genes");
        //genes.keySet().forEach(key->this.attributeRandomizer.put(ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(key)), genes.getInt(key)));
    }
}
