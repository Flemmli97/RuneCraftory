package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.world.GateSpawning;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class GateEntity extends MobEntity implements IBaseMob {

    private List<EntityType<?>> spawnList = new ArrayList<>();
    private EnumElement type = EnumElement.NONE;
    public int rotate, clientParticles;
    public boolean clientParticleFlag;
    private static final DataParameter<String> elementType = EntityDataManager.createKey(GateEntity.class, DataSerializers.STRING);
    private static final DataParameter<Integer> element = EntityDataManager.createKey(GateEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> level = EntityDataManager.createKey(GateEntity.class, DataSerializers.VARINT);
    private static final UUID attributeLevelMod = UUID.fromString("EC84560E-5266-4DC3-A4E1-388b97DBC0CB");

    public GateEntity(EntityType<? extends GateEntity> type, World world) {
        super(type, world);
        if (world.isRemote) {
            this.rotate = ((world.rand.nextInt(2) == 0) ? 1 : -1);
        }
        this.updateAttributes();
        this.setNoGravity(true);
    }

    @Override
    public int level() {
        return this.dataManager.get(level);
    }

    @Override
    public void setLevel(int lvl) {
        this.dataManager.set(level, Math.min(10000, lvl));
    }

    public EnumElement getElement() {
        return this.type;
    }

    public String elementName() {
        return this.dataManager.get(elementType);
    }

    @Override
    public int baseXP() {
        return MobConfig.gateXP;
    }

    @Override
    public int baseMoney() {
        return MobConfig.gateMoney;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(ModAttributes.RF_DEFENCE.get()).createMutableAttribute(ModAttributes.RF_MAGIC_DEFENCE.get());
    }

    private void updateAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(MobConfig.gateHealth);
        this.getAttribute(ModAttributes.RF_DEFENCE.get()).setBaseValue(MobConfig.gateDef);
        this.getAttribute(ModAttributes.RF_MAGIC_DEFENCE.get()).setBaseValue(MobConfig.gateMDef);
        this.setHealth(this.getMaxHealth());
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(elementType, "none");
        this.dataManager.register(level, LibConstants.baseLevel);
        this.dataManager.register(element, 0);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("MobLevel", this.dataManager.get(level));
        ListNBT list = new ListNBT();
        this.spawnList.forEach(type -> list.add(StringNBT.valueOf(type.getRegistryName().toString())));
        compound.put("Spawns", list);
        compound.putString("Element", this.type.toString());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("MobLevel")) {
            this.dataManager.set(level, compound.getInt("MobLevel"));
        }
        compound.getList("Spawns", Constants.NBT.TAG_STRING)
                .forEach(nbt -> this.spawnList.add(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(nbt.getString()))));
        String el = compound.getString("Element");
        try {
            this.type = EnumElement.valueOf(el);
            this.dataManager.set(elementType, this.type.getTranslation());
            this.dataManager.set(element, this.type.ordinal());
        } catch (IllegalArgumentException e) {
            RuneCraftory.logger.error("Unable to set element type for gate entity {}", this);
        }
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
    }

    @Override
    public void heal(float healAmount) {
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public HandSide getPrimaryHand() {
        return HandSide.RIGHT;
    }

    @Override
    public void tick() {
        if (net.minecraftforge.common.ForgeHooks.onLivingUpdate(this)) return;
        if (!this.world.isRemote) {
            this.setFlag(6, this.isGlowing());
        } else {
            this.clientParticles += 10;
            this.clientParticleFlag = true;
        }
        this.baseTick();
        if (this.newPosRotationIncrements > 0) {
            double d0 = this.getPosX() + (this.interpTargetX - this.getPosX()) / (double) this.newPosRotationIncrements;
            double d2 = this.getPosY() + (this.interpTargetY - this.getPosY()) / (double) this.newPosRotationIncrements;
            double d4 = this.getPosZ() + (this.interpTargetZ - this.getPosZ()) / (double) this.newPosRotationIncrements;
            double d6 = MathHelper.wrapDegrees(this.interpTargetYaw - (double) this.rotationYaw);
            this.rotationYaw = (float) ((double) this.rotationYaw + d6 / (double) this.newPosRotationIncrements);
            this.rotationPitch = (float) ((double) this.rotationPitch + (this.interpTargetPitch - (double) this.rotationPitch) / (double) this.newPosRotationIncrements);
            --this.newPosRotationIncrements;
            this.setPosition(d0, d2, d4);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        } else if (!this.isServerWorld()) {
            this.setMotion(this.getMotion().scale(0.98D));
        }
        if (this.rand.nextInt(MobConfig.spawnChance) == 0 && this.world.getDifficulty() != Difficulty.PEACEFUL) {
            this.spawnMobs();
        }
    }

    private void spawnMobs() {
        if (this.world.isRemote)
            return;
        if (!this.spawnList.isEmpty()) {
            int randAmount = this.rand.nextInt(4) + 1;
            List<Entity> nearby = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().grow(18), entity -> GateEntity.this.spawnList.contains(entity.getType()));
            if (nearby.size() <= 3) {
                for (int amount = 0; amount < randAmount; ++amount) {
                    double x = this.getPosX() + this.rand.nextInt(9) - 4.0;
                    double y = this.getPosY() + this.rand.nextInt(2) - 1.0;
                    double z = this.getPosZ() + this.rand.nextInt(9) - 4.0;
                    int entityLevel = this.dataManager.get(level);
                    int levelRand = Math.round(this.dataManager.get(level) + (this.rand.nextFloat() - 0.5f) * Math.round(entityLevel * 0.1f));
                    EntityType<?> type = this.spawnList.get(this.rand.nextInt(this.spawnList.size()));
                    Entity entity = type.create(this.world);
                    if (entity instanceof MobEntity) {
                        MobEntity mob = (MobEntity) entity;
                        BlockPos pos = new BlockPos(x, y, z);
                        boolean notSolid;
                        while ((notSolid = !this.world.getBlockState(pos.down()).isTopSolid(this.world, pos, entity, Direction.UP)) && pos.distanceSq(x, y, z, true) < 16)
                            pos = pos.down();
                        if (!notSolid) {
                            if (mob instanceof BaseMonster)
                                ((BaseMonster) mob).setLevel(levelRand);
                            entity.setPositionAndRotation(x, y, z, this.world.rand.nextFloat() * 360.0f, 0.0f);
                            if (ForgeEventFactory.canEntitySpawnSpawner(mob, this.world, (float) entity.getPosX(), (float) entity.getPosY(), (float) entity.getPosZ(), null) && this.world.hasNoCollisions(mob)) {
                                mob.onInitialSpawn((IServerWorld) this.world, this.world.getDifficultyForLocation(mob.getPosition()), SpawnReason.SPAWNER, null, null);
                                ModifiableAttributeInstance follow = mob.getAttribute(Attributes.FOLLOW_RANGE);
                                mob.setHomePosAndDistance(this.getPosition(), (int) Math.max(16, follow != null ? follow.getValue() * 0.75 : 0));
                                this.world.addEntity(entity);
                                mob.spawnExplosionParticle();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnReason reason) {
        return true;//this.world.getDifficulty() != Difficulty.PEACEFUL && super.canSpawn(world, reason) && this.world.getEntitiesWithinAABB(GateEntity.class, this.getBoundingBox().grow(48.0)).size() < 2;
    }

    public static boolean canSpawnAt(EntityType<? extends GateEntity> type, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && GateSpawning.hasSpawns(world, pos) && canSpawnOn(type, world, reason, pos, random) && world.getEntitiesWithinAABB(GateEntity.class, new AxisAlignedBB(pos).grow(48.0)).size() < 2;
    }

    @Override
    protected void onDeathUpdate() {
        /*if (this.deathTime == 5 && this.attackingPlayer != null) {
            IPlayer cap = this.attackingPlayer.getCapability(PlayerCapProvider.PlayerCap, null);
            cap.addXp(this.attackingPlayer, LevelCalc.xpFromLevel(this.baseXP(), this.level()));
            cap.setMoney(this.attackingPlayer, cap.getMoney() + LevelCalc.xpFromLevel(this.baseMoney(), this.level()));
        }*/
        super.onDeathUpdate();
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
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

    @Override
    public void applyKnockback(float strength, double x, double z) {
    }

    protected float reduceDamage(DamageSource damageSrc, float damageAmount) {
        float reduce = 0.0f;
        if (!damageSrc.isDamageAbsolute() && !damageSrc.isUnblockable()) {
            if (damageSrc.isMagicDamage()) {
                reduce = (float) this.getAttribute(ModAttributes.RF_MAGIC_DEFENCE.get()).getValue();
            } else {
                reduce = (float) this.getAttribute(ModAttributes.RF_DEFENCE.get()).getValue();
            }
        }
        float min = reduce > damageAmount * 2 ? 0 : 0.5f;
        return Math.max(min, damageAmount - reduce);
    }

    @Override
    protected LootContext.Builder getLootContextBuilder(boolean useLuck, DamageSource src) {
        return super.getLootContextBuilder(useLuck, src);
    }

    /*@Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
        int max = 0;
        float luck = 0.0f;
        if (source.getTrueSource() instanceof EntityPlayer) {
            luck = RFCalculations.getAttributeValue((EntityPlayer) source.getTrueSource(), ItemStatAttributes.RFRLUCK, null, null);
        }
        while (luck >= 1.0f) {
            luck -= 0.8;
            --max;
        }
        float dropChance = 0.25F;
        ItemStack drop = ItemStack.EMPTY;
        int maxDrop = 2;
        switch (this.type) {
            case DARK:
                maxDrop = 2;
                drop = new ItemStack(ModItems.crystalDark);
                dropChance = 0.1F;
                break;
            case EARTH:
                maxDrop = 2;
                drop = new ItemStack(ModItems.crystalEarth);
                dropChance = 0.25F;
                break;
            case FIRE:
                maxDrop = 2;
                drop = new ItemStack(ModItems.crystalFire);
                dropChance = 0.25F;
                break;
            case LIGHT:
                maxDrop = 2;
                drop = new ItemStack(ModItems.crystalLight);
                dropChance = 0.1F;
                break;
            case LOVE:
                maxDrop = 1;
                drop = new ItemStack(ModItems.crystalLove);
                dropChance = 0.05F;
                break;
            case WATER:
                maxDrop = 2;
                drop = new ItemStack(ModItems.crystalWater);
                dropChance = 0.3F;
                break;
            case WIND:
                maxDrop = 2;
                drop = new ItemStack(ModItems.crystalWind);
                dropChance = 0.3F;
                break;
            case NONE:
                break;
        }
        while (max < maxDrop)
        {
            if (this.rand.nextFloat() < dropChance + luck)
            {
                ItemUtils.spawnItemAtEntity(this, drop);
            }
            ++max;
        }
    }*/

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean func_241845_aY() {
        return true;
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (key.equals(level)) {
            this.updateStatsToLevel();
        }
        if (key.equals(element)) {
            this.type = EnumElement.values()[this.dataManager.get(element)];
        }
        super.notifyDataManagerChange(key);
    }

    private void updateStatsToLevel() {
        this.getAttribute(Attributes.MAX_HEALTH).applyPersistentModifier(new AttributeModifier(attributeLevelMod, "rf.levelMod", (this.level() - 1) * MobConfig.gateHealthGain, AttributeModifier.Operation.ADDITION));
        this.getAttribute(ModAttributes.RF_DEFENCE.get()).applyPersistentModifier(new AttributeModifier(attributeLevelMod, "rf.levelMod", (this.level() - 1) * MobConfig.gateDefGain, AttributeModifier.Operation.ADDITION));
        this.getAttribute(ModAttributes.RF_MAGIC_DEFENCE.get()).applyPersistentModifier(new AttributeModifier(attributeLevelMod, "rf.levelMod", (this.level() - 1) * MobConfig.gateMDefGain, AttributeModifier.Operation.ADDITION));
        this.setHealth(this.getMaxHealth());
    }

    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
        RegistryKey<Biome> key = this.world.func_242406_i(this.getPosition()).orElse(Biomes.PLAINS);
        this.spawnList.addAll(GateSpawning.pickRandomMobs(world.getWorld(), key, this.rand, this.rand.nextInt(4) + 2, this.getPosition()));
        this.type = this.getType(world, key);
        this.dataManager.set(level, LevelCalc.levelFromPos(this.world, this.getPosition()));
        this.dataManager.set(elementType, this.type.getTranslation());
        this.dataManager.set(element, this.type.ordinal());
        this.setPosition(this.getPosX(), this.getPosY() + 1, this.getPosZ());
        this.updateStatsToLevel();
        return data;
    }

    private EnumElement getType(IServerWorld world, RegistryKey<Biome> key) {
        EnumElement element = EnumElement.values()[this.world.rand.nextInt(EnumElement.values().length)];
        if (BiomeDictionary.getTypes(key).contains(BiomeDictionary.Type.PLAINS) && this.world.rand.nextFloat() < 0.5) {
            element = EnumElement.NONE;
        } else if (BiomeDictionary.getTypes(key).contains(BiomeDictionary.Type.FOREST) && this.world.rand.nextFloat() < 0.5) {
            element = EnumElement.WIND;
        } else if (BiomeDictionary.getTypes(key).contains(BiomeDictionary.Type.HOT) && this.world.rand.nextFloat() < 0.5) {
            element = EnumElement.FIRE;
        } else if (BiomeDictionary.getTypes(key).contains(BiomeDictionary.Type.MOUNTAIN) && this.world.rand.nextFloat() < 0.5) {
            element = EnumElement.WIND;
        } else if (BiomeDictionary.getTypes(key).contains(BiomeDictionary.Type.OCEAN) && this.world.rand.nextFloat() < 0.5) {
            element = EnumElement.WATER;
        } else if (BiomeDictionary.getTypes(key).contains(BiomeDictionary.Type.SANDY) && this.world.rand.nextFloat() < 0.5) {
            element = EnumElement.EARTH;
        } else if (BiomeDictionary.getTypes(key).contains(BiomeDictionary.Type.MAGICAL)) {
            if (this.world.rand.nextFloat() < 0.4) {
                element = EnumElement.LIGHT;
            } else if (this.world.rand.nextFloat() < 0.2) {
                element = EnumElement.LOVE;
            }
        } else if (BiomeDictionary.getTypes(key).contains(BiomeDictionary.Type.SPOOKY) && this.world.rand.nextFloat() < 0.4) {
            element = EnumElement.DARK;
        }
        if (BiomeDictionary.getTypes(key).contains(BiomeDictionary.Type.END)) {
            if (this.world.rand.nextFloat() < 0.3) {
                element = EnumElement.DARK;
            } else if (this.world.rand.nextFloat() < 0.3) {
                element = EnumElement.LIGHT;
            }
        }
        return element;
    }

    @Override
    public void applyFoodEffect(ItemStack stack) {
    }

    @Override
    public void removeFoodEffect() {
    }

    @Override
    public void updateClientFoodEffect(Map<Attribute, Integer> stats, Map<Attribute, Float> multi, int duration) {
    }
}