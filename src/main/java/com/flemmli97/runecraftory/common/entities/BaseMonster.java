package com.flemmli97.runecraftory.common.entities;

import com.flemmli97.runecraftory.common.config.MobConfig;
import com.flemmli97.runecraftory.common.config.values.EntityProperties;
import com.flemmli97.runecraftory.common.entities.monster.ai.HurtByTargetPredicate;
import com.flemmli97.runecraftory.common.lib.LibConstants;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.common.network.S2CAttackDebug;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.tenshilib.api.entity.IAnimated;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.item.SpawnEgg;
import com.google.common.collect.Lists;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.INPC;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
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
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class BaseMonster extends CreatureEntity implements IMob, IAnimated, IExtendedMob {

    private static final DataParameter<Optional<UUID>> owner = EntityDataManager.createKey(BaseMonster.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Integer> mobLevel = EntityDataManager.createKey(BaseMonster.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> levelXP = EntityDataManager.createKey(BaseMonster.class, DataSerializers.VARINT);
    private static final UUID attributeLevelMod = UUID.fromString("EC84560E-5266-4DC3-A4E1-388b97DBC0CB");
    private static final UUID foodUUID = UUID.fromString("87A55C28-8C8C-4BFF-AF5F-9972A38CCD9D");
    private static final UUID foodUUIDMulti = UUID.fromString("A05442AC-381B-49DF-B0FA-0136B454157B");
    private final EntityProperties prop;

    protected int tamingTick = -1;

    protected int feedTimeOut;
    private boolean doJumping = false;
    private int foodBuffTick;

    private AnimatedAction currentAnimation;

    public final Predicate<LivingEntity> attackPred = (e) -> {
        if (!e.equals(BaseMonster.this)) {
            if (BaseMonster.this.isTamed()) {
                return e == BaseMonster.this.getAttackTarget() || !(e instanceof BaseMonster) || !((BaseMonster) e).isTamed();
            }
            return e == BaseMonster.this.getAttackTarget() || e instanceof PlayerEntity || e instanceof INPC || (e instanceof BaseMonster && ((BaseMonster) e).isTamed());
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

    public NearestAttackableTargetGoal<PlayerEntity> targetPlayer = new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 5, true, true, player -> !BaseMonster.this.isTamed());//|| player != BaseMonster.this.getOwner());
    public NearestAttackableTargetGoal<VillagerEntity> targetNPC = new NearestAttackableTargetGoal<>(this, VillagerEntity.class, 5, true, true, mob -> !BaseMonster.this.isTamed());
    public NearestAttackableTargetGoal<MobEntity> targetMobs = new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, true, true, mob -> {
        if (BaseMonster.this.isTamed()) {
            return !(mob instanceof BaseMonster) || !((BaseMonster) mob).isTamed();
        }
        return mob instanceof BaseMonster && ((BaseMonster) mob).isTamed();
    });

    public SwimGoal swimGoal = new SwimGoal(this);
    public HurtByTargetPredicate hurt = new HurtByTargetPredicate(this, this.defendPred);

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
                if (att.getKey() == Attributes.GENERIC_MAX_HEALTH)
                    this.setHealth(this.getMaxHealth());
            }
        }
    }

    public static AttributeModifierMap.MutableAttribute createAttributes(Collection<RegistryObject<Attribute>> atts) {
        AttributeModifierMap.MutableAttribute map = MonsterEntity.createHostileAttributes().add(Attributes.GENERIC_MOVEMENT_SPEED, 0.2);
        if (atts != null)
            for (RegistryObject<Attribute> att : atts)
                map.add(att.get());
        return map;
    }

    public void addGoal() {
        this.targetSelector.addGoal(1, this.targetPlayer);
        this.targetSelector.addGoal(2, this.targetNPC);
        this.targetSelector.addGoal(3, this.targetMobs);
        this.targetSelector.addGoal(0, this.hurt);

        this.goalSelector.addGoal(0, new MoveTowardsRestrictionGoal(this, 1.0));
        this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1.0));
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
    }

    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance diff, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
        this.setEquipmentBasedOnDifficulty(diff);
        return data;
    }

    //=====Animation Stuff

    @Override
    public AnimatedAction getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(AnimatedAction anim) {
        this.currentAnimation = anim == null ? null : anim.create();
        IAnimated.sentToClient(this);
    }

    @Nullable
    public AnimatedAction getRandomAnimation(AnimationType type) {
        List<AnimatedAction> anims = Lists.newArrayList();
        for (AnimatedAction anim : this.getAnimations())
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
            return this.getRNG().nextInt(20) + 45 + diffAdd;
        return this.getRNG().nextInt(25) + 20 + diffAdd;
    }

    public int difficultyCooldown() {
        int diffAdd = 50;
        Difficulty diff = this.world.getDifficulty();
        if (this.world.getDifficulty() == Difficulty.HARD)
            diffAdd = 0;
        else if (diff == Difficulty.NORMAL)
            diffAdd = 25;
        return diffAdd;
    }

    @Override
    public NonNullList<ItemStack> tamingItem() {
        return null;
    }

    @Override
    public Map<ItemStack, Integer> dailyDrops() {
        return null;
    }

    @Override
    public float tamingChance() {
        return this.prop.tamingChance();
    }

    @Override
    public boolean isTamed() {
        return this.ownerUUID() != null;
    }

    @Override
    public boolean ridable() {
        return this.prop.ridable();
    }

    @Override
    public UUID ownerUUID() {
        return this.dataManager.get(owner).orElse(null);
    }

    @Override
    public PlayerEntity getOwner() {
        UUID uuid = this.ownerUUID();
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
        return false;
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
        float mult = Math.max((this.level() - LibConstants.baseLevel) * 0.05f, 0);
        float preHealthDiff = this.getMaxHealth() - this.getHealth();
        ModifiableAttributeInstance health = this.getAttribute(Attributes.GENERIC_MAX_HEALTH);
        health.removeModifier(attributeLevelMod);
        health.addPersistentModifier(new AttributeModifier(attributeLevelMod, "rf.levelMod", this.getAttributeBaseValue(Attributes.GENERIC_MAX_HEALTH) * mult, AttributeModifier.Operation.ADDITION));
        this.setHealth(this.getMaxHealth() - preHealthDiff);

        ModifiableAttributeInstance dmg = this.getAttribute(Attributes.GENERIC_ATTACK_DAMAGE);
        dmg.removeModifier(attributeLevelMod);
        dmg.addPersistentModifier(new AttributeModifier(attributeLevelMod, "rf.levelMod", this.getAttributeBaseValue(Attributes.GENERIC_ATTACK_DAMAGE) * mult, AttributeModifier.Operation.ADDITION));

        for (RegistryObject<Attribute> att : ModAttributes.ATTRIBUTES.getEntries()) {
            if (LevelCalc.shouldStatIncreaseWithLevel(att)) {
                ModifiableAttributeInstance inst = this.getAttribute(att.get());
                inst.removeModifier(attributeLevelMod);
                inst.addPersistentModifier(new AttributeModifier(attributeLevelMod, "rf.levelMod", this.getAttributeBaseValue(att.get()) * mult, AttributeModifier.Operation.ADDITION));
            }
        }
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
        if (!this.isPassenger() && (this.getMoveHelper() instanceof NewMoveController) && ((NewMoveController) this.getMoveHelper()).currentAction() != MovementController.Action.WAIT) {
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
        return this.getFlag(2);
    }

    public void setMoving(boolean flag) {
        this.setFlag(2, flag);
    }

    public void setDoJumping() {
        this.doJumping = true;
    }

    @Override
    public boolean handleFallDamage(float distance, float damageMultiplier) {
        if (!this.isFlyingEntity()) {
            super.handleFallDamage(distance, damageMultiplier);
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
            this.world.addParticle(particle, this.getX() + this.rand.nextFloat() * this.getWidth() * 2.0f - this.getWidth(), this.getY() + 0.5 + this.rand.nextFloat() * this.getHeight(), this.getZ() + this.rand.nextFloat() * this.getWidth() * 2.0f - this.getWidth(), d0, d2, d3);
        }
    }

    //=====Damage Logic

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!this.isInvulnerableTo(damageSrc)) {
            damageAmount = ForgeHooks.onLivingHurt(this, damageSrc, damageAmount);
            if (damageAmount <= 0.0f) {
                return;
            }
            damageAmount = this.reduceDamage(damageSrc, damageAmount);
            if (damageAmount != 0.0f) {
                float f1 = this.getHealth();
                this.setHealth(f1 - damageAmount);
                this.getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
            }
        }
    }

    protected float reduceDamage(DamageSource damageSrc, float damageAmount) {
        float reduce = 0.0f;
        //RFCalculations.elementalReduction(this, damageSrc, damageAmount);
        if (!damageSrc.isDamageAbsolute()) {
            if (!damageSrc.isUnblockable()) {
                if (damageSrc.isMagicDamage())
                    reduce = (float) this.getAttribute(ModAttributes.RF_MAGIC_DEFENCE.get()).getValue();
                else
                    reduce = (float) this.getAttribute(ModAttributes.RF_DEFENCE.get()).getValue();
            }
            if (this.isPotionActive(Effects.RESISTANCE) && damageSrc != DamageSource.OUT_OF_WORLD) {
                int i = (this.getActivePotionEffect(Effects.RESISTANCE).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = damageAmount * (float) j;
                damageAmount = f / 25.0F;
            }
        }
        float min = reduce > damageAmount * 2 ? 0 : 0.5f;
        return Math.max(min, damageAmount - reduce);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return CombatUtils.mobAttack(this, entity);
    }

    @Override
    public void takeKnockback(float strength, double xRatio, double zRatio) {
        super.takeKnockback(0, xRatio, zRatio);
    }


    //=====Combat stuff

    @Override
    protected void onDeathUpdate() {
        //if (!this.isTamed()) {
        if (this.deathTime == 0)
            this.playDeathAnimation();
        AnimatedAction anim = this.getAnimation();
        if (this.deathTime > 6 && anim != null && anim.getTick() < anim.getAttackTime())
            this.deathTime = 6;
        super.onDeathUpdate();
            /*if (this.deathTime == 5 && this.attackingPlayer != null) {
                IPlayer cap = this.attackingPlayer.getCapability(PlayerCapProvider.PlayerCap, null);
                cap.addXp(this.attackingPlayer, LevelCalc.xpFromLevel(this.baseXP(), this.level()));
                cap.setMoney(this.attackingPlayer, cap.getMoney() + LevelCalc.moneyFromLevel(this.baseMoney(), this.level()));
            }*/
        //}
    }

    protected void playDeathAnimation() {
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (source.getTrueSource() == null || this.canAttackFrom(source.getTrueSource().getBlockPos())) && super.attackEntityFrom(source, amount);
    }

    private boolean canAttackFrom(BlockPos pos) {
        return this.getMaximumHomeDistance() == -1.0f || this.getHomePosition().distanceSq(pos) < (this.getMaximumHomeDistance() + 5.0f) * (this.getMaximumHomeDistance() + 5.0f);
    }

    public double maxAttackRange(AnimatedAction anim) {
        return 1;
    }

    public void handleAttack(AnimatedAction anim) {
        if (this.isAnimOfType(anim, AnimationType.MELEE)) {
            this.getNavigator().clearPath();
            if (anim.canAttack()) {
                this.mobAttack(anim, this.getAttackTarget(), this::attackEntityAsMob);
            }
        }
    }

    public void mobAttack(AnimatedAction anim, LivingEntity target, Consumer<LivingEntity> cons) {
        AxisAlignedBB aabb = this.calculateAttackAABB(anim, this.getAttackTarget());
        this.world.getEntitiesWithinAABB(LivingEntity.class, aabb, this.attackPred).forEach(cons);
        PacketHandler.sendToAll(new S2CAttackDebug(aabb));
    }

    public AxisAlignedBB calculateAttackAABB(AnimatedAction anim, LivingEntity target) {
        double reach = this.maxAttackRange(anim) * 0.5 + this.getWidth() * 0.5;
        Vector3d dir;
        if (target != null) {
            reach = Math.min(reach, this.getDistance(target));
            dir = target.getPositionVec().subtract(this.getPositionVec()).normalize();
        } else {
            dir = Vector3d.fromPitchYaw(this.rotationPitch, this.rotationYaw);
        }
        Vector3d attackPos = this.getPositionVec().add(dir.scale(reach));
        return this.attackAABB(anim).offset(attackPos.x, this.getY(), attackPos.z);
    }

    public AxisAlignedBB attackAABB(AnimatedAction anim) {
        double range = this.maxAttackRange(anim) * 0.5;
        return new AxisAlignedBB(-range, 0, -range, range, this.getHeight(), range);
    }

    //=====Interaction

    @Override
    public boolean preventDespawn() {
        return super.preventDespawn() || this.isTamed();
    }

    @Override
    public void livingTick() {
        this.tickAnimation();
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
                AnimatedAction anim = this.getAnimation();
                if (anim != null)
                    this.handleAttack(anim);
            }
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return SpawnEgg.fromType(this.getType()).map(egg -> new ItemStack(egg)).orElse(ItemStack.EMPTY);
    }

    @Override
    protected ActionResultType interactMob(PlayerEntity player, Hand hand) {
        if (this.world.isRemote)
            return ActionResultType.PASS;
        ItemStack stack = player.getHeldItemMainhand();
        if (!stack.isEmpty() && player.isSneaking()) {
            if (false)//(stack.getItem() == ModItems.tame) {
                this.tameEntity(player);
            else if (!this.isTamed() && this.tamingTick == -1 && this.deathTime == 0) {
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
                if (this.rand.nextFloat() <= this.tamingChance() * rightItemMultiplier)//* LevelCalc.tamingMultiplerOnLevel(this.level()))
                    this.tameEntity(player);
                if (stack.getItem().isFood())
                    this.applyFoodEffect(stack);
                this.tamingTick = 100;
                this.world.setEntityState(this, (byte) 34);
            } else {
                //heal
                if (stack.getItem() == Items.STICK) {
                    System.out.println("untame");
                    this.setOwner(null); //for debugging

                    //} else if (stack.getItem() == ModItems.inspector) {
                    //open tamed gui
                } else if (this.feedTimeOut <= 0 && stack.getItem().isFood()) {
                    this.applyFoodEffect(stack);
                    this.feedTimeOut = 24000;
                }
            }
            return ActionResultType.SUCCESS;
        } else if (stack.isEmpty() && !this.world.isRemote && this.isTamed() && this.ridable()) {
            player.startRiding(this);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    protected void tameEntity(PlayerEntity owner) {
        this.setHomePosAndDistance(this.getBlockPos(), -1);
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
        this.targetSelector.removeGoal(this.targetNPC);
        this.targetSelector.removeGoal(this.targetMobs);
        super.addPassenger(passenger);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        this.targetSelector.addGoal(1, this.targetPlayer);
        this.targetSelector.addGoal(2, this.targetNPC);
        this.targetSelector.addGoal(3, this.targetMobs);
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
            this.rotationYaw = entitylivingbase.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entitylivingbase.rotationPitch * 0.5f;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            float strafing = entitylivingbase.moveStrafing * 0.5f;
            float forward = entitylivingbase.moveForward;
            if (forward <= 0.0f) {
                forward *= 0.25f;
            }
            if (this.doJumping && this.onGround && !this.isFlyingEntity()) {
                this.isAirBorne = true;
                this.jump();
                if (forward > 0.0f) {
                    float f = MathHelper.sin(this.rotationYaw * 0.017453292f);
                    float f2 = MathHelper.cos(this.rotationYaw * 0.017453292f);
                    this.setMotion(this.getMotion().add(-0.4f * f, 0, 0.4f * f2));
                }
            } else if (this.doJumping && this.isFlyingEntity()) {
                double motionY = Math.min(this.getMotion().y + 0.15, 1.5);
                this.setMotion(new Vector3d(this.getMotion().x, motionY, this.getMotion().z));
            }
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1f;
            if (this.canPassengerSteer()) {
                this.setAIMoveSpeed((float) this.getAttributeValue(Attributes.GENERIC_MOVEMENT_SPEED));
                this.setMoving(forward != 0 || strafing != 0);
                super.travel(new Vector3d(strafing, vec.y, forward));
            } else if (entitylivingbase instanceof PlayerEntity) {
                this.setMotion(Vector3d.ZERO);
            }
            if (this.onGround || this.isFlyingEntity()) {
                this.doJumping = false;
            }
            this.method_29242(this, false);
        } else {
            this.jumpMovementFactor = 0.02f;
            super.travel(vec);
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("MobLevel", this.level());
        if (this.isTamed())
            compound.putUniqueId("Owner", this.ownerUUID());

        compound.putBoolean("Out", this.dead);
        compound.putInt("FeedTime", this.feedTimeOut);
        if (!this.detachHome())
            compound.putIntArray("Home", new int[]{this.getHomePosition().getX(), this.getHomePosition().getY(), this.getHomePosition().getZ(), (int) this.getMaximumHomeDistance()});
        compound.putInt("FoodBuffTick", this.foodBuffTick);
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
    }
}
