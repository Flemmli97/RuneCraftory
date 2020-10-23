package com.flemmli97.runecraftory.mobs.entity;

import com.flemmli97.runecraftory.lib.EnumElement;
import com.flemmli97.runecraftory.mobs.IBaseMob;
import com.flemmli97.runecraftory.mobs.config.MobConfig;
import com.flemmli97.runecraftory.mobs.libs.LibEntityConstants;
import com.flemmli97.runecraftory.registry.ModAttributes;
import com.flemmli97.runecraftory.mobs.spawning.GateSpawning;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
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
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.ICollisionReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class GateEntity extends MobEntity implements IBaseMob {

    private List<EntityType<?>> spawnList = Lists.newArrayList();
    private EnumElement type = EnumElement.NONE;
    public int rotate;
    private static final DataParameter<String> elementType = EntityDataManager.createKey(GateEntity.class, DataSerializers.STRING);
    private static final DataParameter<Integer> level = EntityDataManager.createKey(GateEntity.class, DataSerializers.VARINT);

    public GateEntity(EntityType<? extends GateEntity> type, World world) {
        super(type, world);
        if (world.isRemote) {
            this.rotate = ((world.rand.nextInt(2) == 0) ? 1 : -1);
        }
        this.updateAttributes();
    }

    @Override
    public int level() {
        return this.dataManager.get(level);
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
        return MobEntity.createMobAttributes().add(ModAttributes.RF_DEFENCE).add(ModAttributes.RF_MAGIC_DEFENCE);
    }

    private void updateAttributes() {
        this.getAttribute(Attributes.GENERIC_MAX_HEALTH).setBaseValue(MobConfig.gateHealth);
        this.getAttribute(ModAttributes.RF_DEFENCE).setBaseValue(MobConfig.gateDef);
        this.getAttribute(ModAttributes.RF_MAGIC_DEFENCE).setBaseValue(MobConfig.gateMDef);
        this.setHealth(this.getMaxHealth());
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(elementType, "none");
        this.dataManager.register(level, LibEntityConstants.baseLevel);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("levelMob", this.dataManager.get(level));
        for (int i = 0; i < this.spawnList.size(); ++i) {
            compound.putString("spawnList[" + i + "]", this.spawnList.get(i).toString());
        }
        compound.putInt("size", this.spawnList.size());
        compound.putString("element", this.type.getName());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("levelMob")) {
            this.dataManager.set(level, compound.getInt("levelMob"));
        }
        for (int i = 0; i < compound.getInt("size"); ++i) {
            EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(compound.getString("spawnList[" + i + "]")));
            if (type != null)
                this.spawnList.add(type);
        }
        for (EnumElement element : EnumElement.values()) {
            if (element.getName().equals(compound.getString("element"))) {
                this.type = element;
                this.dataManager.set(elementType, element.getName());
            }
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
    public void tick(){
        if (net.minecraftforge.common.ForgeHooks.onLivingUpdate(this)) return;
        if (!this.world.isRemote) {
            this.setFlag(6, this.isGlowing());
        }
        this.baseTick();
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
            if (nearby.size() <= 6) {
                for (int amount = 0; amount < randAmount; ++amount) {
                    double x = this.getX() + this.rand.nextInt(9) - 4.0;
                    double y = this.getY() + this.rand.nextInt(2) - 1.0;
                    double z = this.getZ() + this.rand.nextInt(9) - 4.0;
                    int entityLevel = this.dataManager.get(level);
                    int levelRand = Math.round(this.dataManager.get(level) + (this.rand.nextFloat() - 0.5f) * Math.round(entityLevel * 0.1f));
                    EntityType<?> type = this.spawnList.get(this.rand.nextInt(this.spawnList.size()));
                    Entity entity = type.create(this.world);
                    if (entity instanceof MobEntity) {
                        MobEntity mob = (MobEntity) entity;
                        BlockPos pos = new BlockPos(x, y, z);
                        if (this.world.getBlockState(pos.down()).isTopSolid(this.world, pos, entity, Direction.UP)) {
                            if (mob instanceof BaseMonster)
                                ((BaseMonster) mob).setLevel(levelRand);
                            entity.setPositionAndRotation(x, y, z, this.world.rand.nextFloat() * 360.0f, 0.0f);
                            if (ForgeEventFactory.canEntitySpawnSpawner(mob, this.world, (float) entity.getX(), (float) entity.getY(), (float) entity.getZ(), null)) {
                                mob.onInitialSpawn((IServerWorld) this.world, this.world.getDifficultyForLocation(mob.getBlockPos()), SpawnReason.SPAWNER, null, null);
                                mob.setHomePosAndDistance(this.getBlockPos(), 16);
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

    public static boolean canSpawnAt(EntityType<? extends GateEntity> type, IWorld world, SpawnReason reason, BlockPos pos, Random random){
        return world.getDifficulty() != Difficulty.PEACEFUL && canSpawnOn(type, world, reason, pos, random) && world.getEntitiesWithinAABB(GateEntity.class, new AxisAlignedBB(pos).grow(48.0)).size() < 2;
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

    protected float reduceDamage(DamageSource damageSrc, float damageAmount) {
        float reduce = 0.0f;
        if (!damageSrc.isDamageAbsolute() && !damageSrc.isUnblockable()) {
            if (damageSrc.isMagicDamage()) {
                reduce = (float) this.getAttribute(ModAttributes.RF_MAGIC_DEFENCE).getValue();
            }
            else {
                reduce = (float) this.getAttribute(ModAttributes.RF_DEFENCE).getValue();
            }
        }
        float min = reduce > damageAmount*2?0:0.5f;
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
    public boolean isCollidable(){
        return true;
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (key.equals(level)) {
            this.updateStatsToLevel();
        }
        super.notifyDataManagerChange(key);
    }

    private void updateStatsToLevel() {
        //this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(LevelCalc.initStatIncreaseLevel(ConfigHandler.MobConfigs.gateHealth, this.level(), false, 1.1f - this.world.rand.nextFloat() * 0.2f));
        //this.getAttributeMap().getAttributeInstance(ItemStatAttributes.RFDEFENCE).setBaseValue(LevelCalc.initStatIncreaseLevel(ConfigHandler.MobConfigs.gateDef, this.level(), false, 1.1f - this.world.rand.nextFloat() * 0.2f));
        //this.getAttributeMap().getAttributeInstance(ItemStatAttributes.RFMAGICDEF).setBaseValue(LevelCalc.initStatIncreaseLevel(ConfigHandler.MobConfigs.gateMDef, this.level(), false, 1.1f - this.world.rand.nextFloat() * 0.2f));
        this.setHealth(this.getMaxHealth());
    }

    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
        RegistryKey<Biome> key = this.world.method_31081(this.getBlockPos()).orElse(Biomes.PLAINS);
        this.spawnList.addAll(GateSpawning.pickRandomMobs(world.getWorld(), key, this.rand, this.rand.nextInt(4)+2, this.getBlockPos()));
        this.type = this.getType(world, key);
        //this.dataManager.set(level, LevelCalc.levelFromDistSpawn(this.world, this.getPosition()));
        this.dataManager.set(elementType, this.type.getName());
        this.setPosition(this.getX(), this.getY() + 1, this.getZ());
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

    public EnumElement entityElement() {
        return this.type;
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