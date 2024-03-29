package io.github.flemmli97.runecraftory.common.entities;

import com.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.IAnimated;
import com.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.config.values.EntityProperties;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.HurtByTargetPredicate;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.RiderAttackTargetGoal;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.network.PacketHandler;
import io.github.flemmli97.runecraftory.common.network.S2CAttackDebug;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.mixin.MoveControllerAccessor;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.INPC;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class BaseMonster extends CreatureEntity implements IMob, IAnimated, IExtendedMob {

    private static final DataParameter<Optional<UUID>> owner = EntityDataManager.createKey(BaseMonster.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Integer> mobLevel = EntityDataManager.createKey(BaseMonster.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> levelXP = EntityDataManager.createKey(BaseMonster.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> moveFlags = EntityDataManager.createKey(BaseMonster.class, DataSerializers.BYTE);

    private static final UUID attributeLevelMod = UUID.fromString("EC84560E-5266-4DC3-A4E1-388b97DBC0CB");
    private static final UUID foodUUID = UUID.fromString("87A55C28-8C8C-4BFF-AF5F-9972A38CCD9D");
    private static final UUID foodUUIDMulti = UUID.fromString("A05442AC-381B-49DF-B0FA-0136B454157B");
    private final EntityProperties prop;

    protected int tamingTick = -1;

    protected int feedTimeOut;
    private boolean doJumping = false;
    private int foodBuffTick;

    public final Predicate<LivingEntity> hitPred = (e) -> {
        if (e != this) {
            if (e.isPassenger(e))
                return false;
            if (e instanceof MobEntity && this == ((MobEntity) e).getAttackTarget())
                return true;
            Entity controller = this.getControllingPassenger();
            if (this.isTamed()) {
                if (e == this.getAttackTarget())
                    return true;
                UUID owner = EntityUtils.tryGetOwner(e);
                if (owner != null && (!this.attackOtherTamedMobs() || owner.equals(this.getOwnerUUID())))
                    return false;
                if (controller instanceof PlayerEntity)
                    return true;
                return e instanceof IMob || (e instanceof MobEntity && ((MobEntity) e).getAttackTarget() == this);
            }
            boolean riderTarget = false;
            if (controller != null) {
                if (controller instanceof BaseMonster)
                    return ((BaseMonster) this.getControllingPassenger()).hitPred.test(e);
                riderTarget = controller instanceof MobEntity ? e == ((MobEntity) controller).getAttackTarget() : false;
            }
            return riderTarget || e == this.getAttackTarget() || e instanceof INPC || EntityUtils.tryGetOwner(e) != null || e instanceof PlayerEntity;
        }
        return false;
    };
    public final Predicate<LivingEntity> targetPred = (e) -> {
        if (e != this) {
            if (e instanceof MobEntity && this == ((MobEntity) e).getAttackTarget())
                return true;
            if (this.isTamed()) {
                return e instanceof IMob;
            }
            return e instanceof INPC || EntityUtils.tryGetOwner(e) != null;
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

    public NearestAttackableTargetGoal<PlayerEntity> targetPlayer = new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 5, true, true, player -> !this.isTamed());//|| player != BaseMonster.this.getOwner());
    public NearestAttackableTargetGoal<MobEntity> targetMobs = new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, true, true, this.targetPred);

    public SwimGoal swimGoal = new SwimGoal(this);
    public RandomWalkingGoal wander = new RandomWalkingGoal(this, 1.0);

    public HurtByTargetPredicate hurt = new HurtByTargetPredicate(this, this.defendPred);

    //private Map<Attribute, Integer> attributeRandomizer = new HashMap<>();

    public BaseMonster(EntityType<? extends BaseMonster> type, World world) {
        super(type, world);
        this.moveController = new NewMoveController(this);
        this.prop = MobConfig.propertiesMap.getOrDefault(type.getRegistryName(), EntityProperties.defaultProp);
        this.applyAttributes();
        this.addGoal();
    }

    protected void applyAttributes() {
        for (Map.Entry<Attribute, Double> att : this.prop.getBaseValues().entrySet()) {
            ModifiableAttributeInstance inst = this.getAttribute(att.getKey());
            if (inst != null) {
                inst.setBaseValue(att.getValue());
                if (att.getKey() == Attributes.MAX_HEALTH)
                    this.setHealth(this.getMaxHealth());
            }
        }
    }

    public static AttributeModifierMap.MutableAttribute createAttributes(Collection<RegistryObject<Attribute>> atts) {
        AttributeModifierMap.MutableAttribute map = MonsterEntity.func_234295_eP_().createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.22);
        if (atts != null)
            for (RegistryObject<Attribute> att : atts)
                map.createMutableAttribute(att.get());
        return map;
    }

    public void addGoal() {
        this.targetSelector.addGoal(1, this.targetPlayer);
        this.targetSelector.addGoal(2, this.targetMobs);
        this.targetSelector.addGoal(0, this.hurt);
        this.targetSelector.addGoal(3, new RiderAttackTargetGoal(this, 15));

        this.goalSelector.addGoal(0, new MoveTowardsRestrictionGoal(this, 1.0));
        this.goalSelector.addGoal(2, this.wander);
        this.goalSelector.addGoal(3, this.swimGoal);
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 8.0f));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(owner, Optional.empty());
        this.dataManager.register(mobLevel, LibConstants.baseLevel);
        this.dataManager.register(levelXP, 0);
        this.dataManager.register(moveFlags, (byte) 0);
    }

    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance diff, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
        this.setEquipmentBasedOnDifficulty(diff);
        //for(Attribute att : this.prop.getAttributeGains().keySet())
        //    this.attributeRandomizer.put(att, this.rand.nextInt(5)-2);
        return data;
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
        return anims.get(this.getRNG().nextInt(anims.size()));
    }

    public abstract boolean isAnimOfType(AnimatedAction anim, AnimationType type);

    public int animationCooldown(@Nullable AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown();
        if (anim == null)
            return this.getRNG().nextInt(15) + 10 + diffAdd;
        return this.getRNG().nextInt(17) + 5 + diffAdd;
    }

    public int difficultyCooldown() {
        int diffAdd = 35;
        Difficulty diff = this.world.getDifficulty();
        if (this.world.getDifficulty() == Difficulty.HARD)
            diffAdd = 0;
        else if (diff == Difficulty.NORMAL)
            diffAdd = 15;
        return diffAdd;
    }

    private static final Collector<SimpleItemStackWrapper, ?, NonNullList<ItemStack>> itemCollector = Collector.of(NonNullList::create,
            (l, c) -> l.add(c.getStack()),
            (l, r) -> {
                l.addAll(r);
                return l;
            },
            Collector.Characteristics.IDENTITY_FINISH);

    @Override
    public NonNullList<ItemStack> tamingItem() {
        return Arrays.stream(this.prop.getTamingItem())
                .collect(itemCollector);
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
        return this.dataManager.get(owner).orElse(null);
    }

    @Override
    public PlayerEntity getOwner() {
        UUID uuid = this.getOwnerUUID();
        return uuid == null ? null : this.world.getPlayerByUuid(uuid);
    }

    @Override
    public void setOwner(PlayerEntity player) {
        if (player != null)
            this.dataManager.set(owner, Optional.of(player.getUniqueID()));
        else
            this.dataManager.set(owner, Optional.empty());
    }

    @Override
    public boolean isFlyingEntity() {
        return this.prop.flying();
    }

    @Override
    public int level() {
        return this.dataManager.get(mobLevel);
    }

    @Override
    public void setLevel(int level) {
        this.dataManager.set(mobLevel, Math.min(10000, level));
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
        /*this.removeFoodEffect();
        FoodProperties food = ItemFoodMap.get(stack);
        if (food == null)
            return;
        for (Map.Entry<Attribute, Float> entry : food.effectsMultiplier().entrySet()) {
            if (this.getAttributes().addTemporaryModifiers().getAttributeInstance(entry.getKey()) == null)
                this.getAttributeMap().registerAttribute(entry.getKey());
            double base = this.getAttributeMap().getAttributeInstance(entry.getKey()).getBaseValue();
            this.getAttributeMap().getAttributeInstance(entry.getKey()).applyModifier(new AttributeModifier(foodUUIDMulti, "foodBuffMulti " + entry.getKey().getName(), base * entry.getValue(), 0));
        }
        for (Map.Entry<Attribute, Integer> entry : food.effects().entrySet()) {
            if (this.getAttributeMap().getAttributeInstance(entry.getKey()) == null)
                this.getAttributeMap().registerAttribute(entry.getKey());
            this.getAttributeMap().getAttributeInstance(entry.getKey()).applyModifier(new AttributeModifier(foodUUID, "foodBuff " + entry.getKey().getName(), entry.getValue(), 0));
        }
        this.foodBuffTick = food.duration();
        if (!this.world.isRemote) {
            this.heal(food.getHPGain());
            this.heal(this.getMaxHealth() * food.getHpPercentGain() * 0.01F);
            PacketHandler.sendToAll(new PacketFoodUpdateEntity(this, food.effects(), food.effectsMultiplier(), food.duration()));
        }*/
    }

    @Override
    public void updateClientFoodEffect(Map<Attribute, Integer> stats, Map<Attribute, Float> multi, int duration) {
        /*this.removeFoodEffect();
        for (Entry<IAttribute, Float> entry : multi.entrySet()) {
            if (this.getAttributeMap().getAttributeInstance(entry.getKey()) == null)
                this.getAttributeMap().registerAttribute(entry.getKey());
            double base = this.getAttributeMap().getAttributeInstance(entry.getKey()).getBaseValue();
            this.getAttributeMap().getAttributeInstance(entry.getKey()).applyModifier(new AttributeModifier(foodUUIDMulti, "foodBuffMulti " + entry.getKey().getName(), base * entry.getValue(), 0));
        }
        for (Entry<IAttribute, Integer> entry : stats.entrySet()) {
            if (this.getAttributeMap().getAttributeInstance(entry.getKey()) == null)
                this.getAttributeMap().registerAttribute(entry.getKey());
            this.getAttributeMap().getAttributeInstance(entry.getKey()).applyModifier(new AttributeModifier(foodUUID, "foodBuff " + entry.getKey().getName(), entry.getValue(), 0));
        }
        this.foodBuffTick = duration;*/
    }

    @Override
    public void removeFoodEffect() {
        /*this.getAttributes().removeModifiers();
        for (IAttributeInstance ins : this.getAttributeMap().getAllAttributes()) {
            ins.removeModifier(foodUUID);
            ins.removeModifier(foodUUIDMulti);
        }*/
    }

    //=====Level Handling

    public void updateStatsToLevel() {
        float preHealthDiff = this.getMaxHealth() - this.getHealth();
        this.prop.getAttributeGains().forEach((att, val) -> {
            ModifiableAttributeInstance inst = this.getAttribute(att);
            if (inst != null) {
                inst.removeModifier(attributeLevelMod);
                float multiplier = 1;//this.attributeRandomizer.getOrDefault(att, 0) * 0.5f;
                inst.applyPersistentModifier(new AttributeModifier(attributeLevelMod, "rf.levelMod", (this.level() - 1) * val * (1 + multiplier), AttributeModifier.Operation.ADDITION));
                if (att == Attributes.MAX_HEALTH)
                    this.setHealth(this.getMaxHealth() - preHealthDiff);
            }
        });
    }
    /*

    public void increaseLevel() {
        this.dataManager.set(mobLevel, Math.min(10000, this.level() + 1));
        if (!this.world.isRemote) {
            this.entityLevelUp();
            PacketHandler.sendToAll(new PacketEntityLevelUp(this.getEntityId()));
        }
    }

    public int getLevelXp() {
        return this.dataManager.get(levelXP);
    }

    public void addXp(int amount) {
        int neededXP = LevelCalc.xpAmountForLevelUp(this.level());
        int xpToNextLevel = neededXP - this.getLevelXp();
        if (amount >= xpToNextLevel) {
            int diff = amount - xpToNextLevel;
            this.increaseLevel();
            this.addXp(diff);
        }
        else {
            this.dataManager.set(levelXP, amount);
        }
    }*/

    //=====Movement Handling

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        if (!this.isPassenger() &&
                ((MoveControllerAccessor) this.moveController).getAction() != MovementController.Action.WAIT) {
            this.setMoving(true);
        /*double d0 = this.getMoveHelper().getSpeed();
        if (d0 == 0.6D)
        {
            this.setSneaking(true);
            this.setSprinting(false);
        }
        else if (d0 == 1.33D)
        {
            this.setSneaking(false);
            this.setSprinting(true);
        }
        else
        {
            this.setSneaking(false);
            this.setSprinting(false);
        }*/
            this.updateMoveAnimation();
        } else {
            this.setMoving(false);
            this.setSneaking(false);
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
        this.dataManager.set(moveFlags, (byte) flag);
    }

    public byte getMoveFlag() {
        return this.dataManager.get(moveFlags);
    }

    public void setDoJumping(boolean jump) {
        this.doJumping = jump;
    }

    public boolean doJumping() {
        return this.doJumping;
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        if (!this.isFlyingEntity()) {
            super.onLivingFall(distance, damageMultiplier);
        }
        return false;
    }

    //=====Client
    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 10) {
            this.playTameEffect(true);
        } else if (id == 11) {
            this.playTameEffect(false);
        } else if (id == 34) {
            this.tamingTick = 120;
        }
        super.handleStatusUpdate(id);
    }

    private void playTameEffect(boolean play) {
        IParticleData particle = ParticleTypes.HEART;
        if (!play) {
            particle = ParticleTypes.SMOKE;
        }
        for (int i = 0; i < 7; ++i) {
            double d0 = this.rand.nextGaussian() * 0.02;
            double d2 = this.rand.nextGaussian() * 0.02;
            double d3 = this.rand.nextGaussian() * 0.02;
            this.world.addParticle(particle, this.getPosX() + this.rand.nextFloat() * this.getWidth() * 2.0f - this.getWidth(), this.getPosY() + 0.5 + this.rand.nextFloat() * this.getHeight(), this.getPosZ() + this.rand.nextFloat() * this.getWidth() * 2.0f - this.getWidth(), d0, d2, d3);
        }
    }

    //=====Damage Logic

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return CombatUtils.mobAttack(this, entity);
    }

    @Override
    public void applyKnockback(float strength, double xRatio, double zRatio) {
        super.applyKnockback(0, xRatio, zRatio);
    }


    //=====Combat stuff

    //TODO: Redo Death animation. if server lagging behind client finished animation
    @Override
    protected void onDeathUpdate() {
        //if (!this.isTamed()) {
        if (this.deathTime == 0) {
            this.playDeathAnimation();
        }
        ++this.deathTime;
        if (this.deathTime == (this.maxDeathTime() - 5) && this.attackingPlayer instanceof ServerPlayerEntity) {
            this.attackingPlayer.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                LevelCalc.addXP((ServerPlayerEntity) this.attackingPlayer, cap, LevelCalc.getMobXP(cap, this));
                cap.setMoney(this.attackingPlayer, cap.getMoney() + LevelCalc.getMoney(this.baseMoney(), this.level()));
            });
        }
        if (this.deathTime == this.maxDeathTime()) {
            this.remove(false); //Forge keep data until we revive player

            for (int i = 0; i < 20; ++i) {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.POOF, this.getPosXRandom(1.0D), this.getPosYRandom(), this.getPosZRandom(1.0D), d0, d1, d2);
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
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (source.getTrueSource() == null || this.canAttackFrom(source.getTrueSource().getPosition())) && super.attackEntityFrom(source, amount);
    }

    private boolean canAttackFrom(BlockPos pos) {
        return this.getMaximumHomeDistance() == -1.0f || this.getHomePosition().distanceSq(pos) < (this.getMaximumHomeDistance() * this.getMaximumHomeDistance());
    }

    public double maxAttackRange(AnimatedAction anim) {
        return 1;
    }

    public void handleAttack(AnimatedAction anim) {
        if (this.isAnimOfType(anim, AnimationType.MELEE)) {
            this.getNavigator().clearPath();
            if (anim.getTick() == 1 && this.getAttackTarget() != null)
                this.faceEntity(this.getAttackTarget(), 360, 90);
            if (anim.canAttack()) {
                this.mobAttack(anim, this.getAttackTarget(), this::attackEntityAsMob);
            }
        }
    }

    public void mobAttack(AnimatedAction anim, LivingEntity target, Consumer<LivingEntity> cons) {
        AxisAlignedBB aabb = this.calculateAttackAABB(anim, target);
        this.world.getEntitiesWithinAABB(LivingEntity.class, aabb, this.hitPred).forEach(cons);
        PacketHandler.sendToAll(new S2CAttackDebug(aabb));
    }

    public AxisAlignedBB calculateAttackAABB(AnimatedAction anim, LivingEntity target) {
        return this.calculateAttackAABB(anim, target, 0);
    }

    public AxisAlignedBB calculateAttackAABB(AnimatedAction anim, LivingEntity target, double grow) {
        double reach = this.maxAttackRange(anim) * 0.5 + this.getWidth() * 0.5;
        Vector3d dir;
        if (target != null && !this.isBeingRidden()) {
            reach = Math.min(reach, this.getDistance(target));
            dir = target.getPositionVec().subtract(this.getPositionVec()).normalize();
        } else {
            dir = Vector3d.fromPitchYaw(this.rotationPitch, this.rotationYaw);
        }
        Vector3d attackPos = this.getPositionVec().add(dir.scale(reach));
        return this.attackAABB(anim).grow(grow, 0, grow).offset(attackPos.x, attackPos.y, attackPos.z);
    }

    public AxisAlignedBB attackAABB(AnimatedAction anim) {
        double range = this.maxAttackRange(anim) * 0.5;
        return new AxisAlignedBB(-range, -0.02, -range, range, this.getHeight() + 0.02, range);
    }

    public abstract void handleRidingCommand(int command);

    //=====Interaction

    @Override
    public boolean preventDespawn() {
        return super.preventDespawn() || this.isTamed();
    }

    @Override
    public void livingTick() {
        this.getAnimationHandler().tick();
        if (!this.dead) {
            super.livingTick();
            if (!this.world.isRemote) {
                if (this.tamingTick > 0) {
                    --this.tamingTick;
                }
                if (this.tamingTick == 0) {
                    if (this.isTamed()) {
                        this.world.setEntityState(this, (byte) 10);
                    } else {
                        this.world.setEntityState(this, (byte) 11);
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
                this.getAnimationHandler().getAnimation().ifPresent(this::handleAttack);
            }
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return SpawnEgg.fromType(this.getType()).map(ItemStack::new).orElse(ItemStack.EMPTY);
    }

    @Override
    protected ActionResultType getEntityInteractionResult(PlayerEntity player, Hand hand) {
        if (this.world.isRemote)
            return ActionResultType.PASS;
        ItemStack stack = player.getHeldItem(hand);
        if (!stack.isEmpty() && player.isSneaking()) {
            if (stack.getItem() == ModItems.tame.get() && !this.isTamed()) {
                this.tameEntity(player);
            } else if (!this.isTamed()) {
                if (this.tamingTick == -1 && this.deathTime <= 0) {
                    boolean flag = false;
                    if (this.tamingItem() != null)
                        for (ItemStack item : this.tamingItem())
                            if (item.getItem() == stack.getItem()) {
                                flag = true;
                                break;
                            }
                    float rightItemMultiplier = flag ? 2 : 1F;
                    if (!player.isCreative())
                        stack.shrink(1);
                    if (this.rand.nextFloat() <= EntityUtils.tamingChance(this, rightItemMultiplier))
                        this.tameEntity(player);
                    if (stack.getItem().isFood())
                        this.applyFoodEffect(stack);
                    this.tamingTick = 100;
                    this.world.setEntityState(this, (byte) 34);
                }
            } else {
                if (stack.getItem() == Items.STICK) {
                    this.setOwner(null);
                } else if (stack.getItem() == ModItems.inspector.get()) {
                    //open tamed gui
                } else if (this.feedTimeOut <= 0 && stack.getItem().isFood()) {
                    this.applyFoodEffect(stack);
                    this.feedTimeOut = 24000;
                }
            }
            return ActionResultType.SUCCESS;
        } else if (stack.isEmpty() && !this.world.isRemote && player == this.getOwner() && this.ridable()) {
            player.startRiding(this);
            return ActionResultType.SUCCESS;
        } else {
            //Notify not owned this entity
        }
        return ActionResultType.FAIL;
    }

    protected void tameEntity(PlayerEntity owner) {
        this.setHomePosAndDistance(this.getPosition(), -1);
        this.setOwner(owner);
        this.navigator.clearPath();
        this.setAttackTarget(null);
        this.world.setEntityState(this, (byte) 10);
    }

    @Override
    public boolean canBeSteered() {
        return this.isTamed() && this.ridable();
    }

    @Override
    public boolean canPassengerSteer() {
        return this.canBeSteered() && !this.world.isRemote;
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
    protected boolean isMovementBlocked() {
        return super.isMovementBlocked() && this.isBeingRidden();
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public void travel(Vector3d vec) {
        if (this.isBeingRidden() && this.canBeSteered() && this.getControllingPassenger() instanceof LivingEntity) {
            LivingEntity entitylivingbase = (LivingEntity) this.getControllingPassenger();
            if (this.adjustRotFromRider(entitylivingbase)) {
                this.rotationYaw = entitylivingbase.rotationYaw;
                this.rotationPitch = entitylivingbase.rotationPitch * 0.5f;
            }
            this.prevRotationYaw = this.rotationYaw;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            float strafing = entitylivingbase.moveStrafing * 0.5f;
            float forward = entitylivingbase.moveForward;
            if (forward <= 0.0f) {
                forward *= 0.25f;
            }
            if (this.doJumping) {
                if (this.onGround && !this.isFlyingEntity()) {
                    this.isAirBorne = true;
                    this.jump();
                    if (forward > 0.0f) {
                        float f = MathHelper.sin(this.rotationYaw * 0.017453292f);
                        float f2 = MathHelper.cos(this.rotationYaw * 0.017453292f);
                        this.setMotion(this.getMotion().add(-0.4f * f, 0, 0.4f * f2));
                    }
                } else if (this.isFlyingEntity()) {
                    float speed = this.getAIMoveSpeed();
                    double motionY = Math.min(this.getMotion().y + speed, this.maxAscensionSpeed());
                    this.setMotion(new Vector3d(this.getMotion().x, motionY, this.getMotion().z));
                    if (forward > 0.0f) {
                        float f = MathHelper.sin(this.rotationYaw * 0.017453292f);
                        float f2 = MathHelper.cos(this.rotationYaw * 0.017453292f);
                        speed *= 0.5;
                        this.setMotion(this.getMotion().add(-speed * f, 0, speed * f2));
                    }
                }
            }
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1f;
            if (this.canPassengerSteer()) {
                this.setAIMoveSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.setMoving(forward != 0 || strafing != 0);
                forward *= this.ridingSpeedModifier();
                strafing *= this.ridingSpeedModifier();
                super.travel(new Vector3d(strafing, vec.y, forward));
            } else if (entitylivingbase instanceof PlayerEntity) {
                this.setMotion(Vector3d.ZERO);
            }
            if (this.onGround || this.isFlyingEntity()) {
                this.doJumping = false;
            }
            this.func_233629_a_(this, false);
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

    public void handleLandTravel(Vector3d vec) {
        this.jumpMovementFactor = 0.02f;
        super.travel(vec);
    }

    public void handleWaterTravel(Vector3d vec) {
        if (this.isBeingRidden() && this.canBeSteered() && this.getControllingPassenger() instanceof LivingEntity) {
            LivingEntity entitylivingbase = (LivingEntity) this.getControllingPassenger();
            if (this.adjustRotFromRider(entitylivingbase)) {
                this.rotationYaw = entitylivingbase.rotationYaw;
                this.rotationPitch = entitylivingbase.rotationPitch * 0.5f;
            }
            this.prevRotationYaw = this.rotationYaw;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            float strafing = entitylivingbase.moveStrafing * 0.5f;
            float forward = entitylivingbase.moveForward;
            double up = 0;
            if (forward <= 0.0f) {
                forward *= 0.25f;
            } else {
                up = Math.min(0, entitylivingbase.getLookVec().y + 0.45);
            }
            if (this.doJumping()) {
                up += Math.min(this.getMotion().y + 0.5, this.maxAscensionSpeed());
            }
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1f;
            if (this.canPassengerSteer()) {
                this.setAIMoveSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.setMoving(forward != 0 || strafing != 0);
                vec = new Vector3d(strafing, vec.y, forward);
            } else if (entitylivingbase instanceof PlayerEntity) {
                vec = Vector3d.ZERO;
            }
            vec = vec.add(0, up, 0);
            this.setDoJumping(false);
            this.func_233629_a_(this, false);
        }

        this.moveRelative(0.1F, vec);
        this.move(MoverType.SELF, this.getMotion());
        this.setMotion(this.getMotion().scale(0.9D));
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
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("MobLevel", this.level());
        if (this.isTamed())
            compound.putUniqueId("Owner", this.getOwnerUUID());

        compound.putBoolean("Out", this.dead);
        compound.putInt("FeedTime", this.feedTimeOut);
        if (this.detachHome())
            compound.putIntArray("Home", new int[]{this.getHomePosition().getX(), this.getHomePosition().getY(), this.getHomePosition().getZ(), (int) this.getMaximumHomeDistance()});
        compound.putInt("FoodBuffTick", this.foodBuffTick);
        //CompoundNBT genes = new CompoundNBT();
        //this.attributeRandomizer.forEach((att, val)->genes.putInt(att.getRegistryName().toString(), val));
        //compound.put("Genes", genes);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(mobLevel, compound.getInt("MobLevel"));
        if (compound.contains("Owner"))
            this.dataManager.set(owner, Optional.of(compound.getUniqueId("Owner")));
        this.feedTimeOut = compound.getInt("FeedTime");
        if (compound.contains("Home")) {
            int[] home = compound.getIntArray("Home");
            this.setHomePosAndDistance(new BlockPos(home[0], home[1], home[2]), home[3]);
        }
        this.dead = compound.getBoolean("Out");
        this.foodBuffTick = compound.getInt("FoodBuffTick");
        //CompoundNBT genes = compound.getCompound("Genes");
        //genes.keySet().forEach(key->this.attributeRandomizer.put(ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(key)), genes.getInt(key)));
    }
}
