package com.flemmli97.runecraftory.common.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.core.handler.capabilities.CapabilityProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.config.MobConfig;
import com.flemmli97.runecraftory.common.init.GateSpawning;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibConstants;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityGate extends EntityLiving implements IEntityBase
{
    private int baseXP = 10;
    private int baseMoney = 4;
    private List<String> spawnList = new ArrayList<String>();
    private EnumElement type = EnumElement.NONE;
    public int rotate;
    private static final DataParameter<String> elementType = EntityDataManager.createKey(EntityGate.class, DataSerializers.STRING);
    private static final DataParameter<Integer> level = EntityDataManager.createKey(EntityGate.class, DataSerializers.VARINT);

    public EntityGate(World world) {
        super(world);
        this.setSize(1.0f, 1.0f);
        if (world.isRemote) {
            this.rotate = ((world.rand.nextInt(2) == 0) ? 1 : -1);
        }
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
        return this.baseXP;
    }

    @Override
    public int baseMoney() {
        return this.baseMoney;
    }

    @Override
    public Map<ItemStack, Float> getDrops() {
        return null;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        this.getAttributeMap().registerAttribute(ItemStatAttributes.RFDEFENCE).setBaseValue(MobConfig.gateDef);
        this.getAttributeMap().registerAttribute(ItemStatAttributes.RFMAGICDEF).setBaseValue(MobConfig.gateHealth);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(elementType, "none");
        this.dataManager.register(level, LibConstants.baseLevel);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("levelMob", (int)this.dataManager.get(level));
        for (int i = 0; i < this.spawnList.size(); ++i) {
            compound.setString("spawnList[" + i + "]", (String)this.spawnList.get(i));
        }
        compound.setInteger("size", this.spawnList.size());
        compound.setString("element", this.type.getName());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("levelMob")) {
            this.dataManager.set(level, compound.getInteger("levelMob"));
        }
        for (int i = 0; i < compound.getInteger("size"); ++i) {
            this.spawnList.add(compound.getString("spawnList[" + i + "]"));
        }
        for (EnumElement element : EnumElement.values()) {
            if (element.getName().equals(compound.getString("element"))) {
                this.type = element;
                this.dataManager.set(elementType, element.getName());
            }
        }
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
    }

    @Override
    public void heal(float healAmount) {
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.RIGHT;
    }

    @Override
    public void onLivingUpdate() {
        if (this.rand.nextInt(100) < 1 && this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
            this.spawnMobs();
        }
    }
    
    private void spawnMobs() {
        if (this.spawnList != null && !this.spawnList.isEmpty()) {
            int randAmount = this.rand.nextInt(4) + 1;
            List<Entity> nearby = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(12.0), new Predicate<Entity>() {
                public boolean apply(Entity entity) {
                    return entity instanceof IEntityBase && !(entity instanceof EntityGate);
                }
            });
            if (nearby.size() <= 6) 
            {
                for (int amount = 0; amount < randAmount; ++amount) {
                    double x = this.posX + this.rand.nextInt(9) - 4.0;
                    double y = this.posY + this.rand.nextInt(2) - 1.0;
                    double z = this.posZ + this.rand.nextInt(9) - 4.0;
                    int entityLevel = (int)this.dataManager.get(level);
                    int levelPerc = Math.round(entityLevel * 0.1f);
                    int levelRand = Math.round(entityLevel + (this.rand.nextFloat() - 0.5f) * levelPerc);
                    EntityMobBase mob = GateSpawning.entityFromString(this.world, this.spawnList.get(this.rand.nextInt(this.spawnList.size())));
                    this.doSpawnEntity(mob, x, y, z, levelRand);
                }
            }
        }
    }
    
    private void doSpawnEntity(EntityMobBase mob, double x, double y, double z, int level) {
        if (mob != null) {
            BlockPos pos = new BlockPos(x, y, z);
            if (this.world.getBlockState(pos.down()).isSideSolid((IBlockAccess)this.world, pos, EnumFacing.UP) && this.world.isAirBlock(pos.up()) && this.world.isAirBlock(pos.up(2))) {
                mob.setLevel(level);
                mob.setPositionAndRotation(x, y, z, this.world.rand.nextFloat() * 360.0f, 0.0f);
                if (!ForgeEventFactory.doSpecialSpawn((EntityLiving)mob, this.world, (float)mob.posX, (float)mob.posY, (float)mob.posZ)) {
                    mob.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos((Entity)mob)), (IEntityLivingData)null);
                    mob.setHomePosAndDistance(this.getPosition(), 16);
                    if (!this.world.isRemote) {
                        this.world.spawnEntity((Entity)mob);
                    }
                    if (this.world.isRemote) {
                        mob.spawnExplosionParticle();
                    }
                }
            }
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.world.getDifficulty() != EnumDifficulty.PEACEFUL && super.getCanSpawnHere() && this.world.getEntitiesWithinAABB(EntityGate.class, this.getEntityBoundingBox().grow(48.0)).size() < 2;
    }

    @Override
    protected void onDeathUpdate() {
        ++this.deathTime;
        if (this.deathTime == 5 && this.attackingPlayer != null) {
            IPlayer cap = this.attackingPlayer.getCapability(CapabilityProvider.PlayerCapProvider.PlayerCap, null);
            cap.addXp(this.attackingPlayer, LevelCalc.xpFromLevel(this.baseXP(), this.level()));
            cap.setMoney(this.attackingPlayer, cap.getMoney() + LevelCalc.xpFromLevel(this.baseMoney(), this.level()));
        }
        super.onDeathUpdate();
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!this.isEntityInvulnerable(damageSrc)) {
            damageAmount = ForgeHooks.onLivingHurt((EntityLivingBase)this, damageSrc, damageAmount);
            if (damageAmount <= 0.0f) {
                return;
            }
            damageAmount = this.reduceDamage(damageSrc, damageAmount);
            if (damageSrc != DamageSource.OUT_OF_WORLD) {
                damageAmount *= (float)LibConstants.DAMAGESCALE;
            }
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
                reduce = (float)this.getEntityAttribute(ItemStatAttributes.RFMAGICDEF).getAttributeValue();
            }
            else {
                reduce = (float)this.getEntityAttribute(ItemStatAttributes.RFDEFENCE).getAttributeValue();
            }
        }
        return Math.max(0.0f, damageAmount - reduce);
    }

    @Override
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
    			drop = new ItemStack(ModItems.crystal, 1, 5);
    			dropChance = 0.1F;
                break;
            case EARTH:
                maxDrop = 2;
    			drop = new ItemStack(ModItems.crystal, 1, 1);
    			dropChance = 0.25F;
                break;
            case FIRE:
                maxDrop = 2;
    			drop = new ItemStack(ModItems.crystal, 1, 2);
    			dropChance = 0.25F;
                break;
            case LIGHT:
                maxDrop = 2;
    			drop = new ItemStack(ModItems.crystal, 1, 4);
    			dropChance = 0.1F;
                break;
            case LOVE:
                maxDrop = 1;
    			drop = new ItemStack(ModItems.crystal, 1, 6);
    			dropChance = 0.05F;
    			break;            
    		case WATER:
                maxDrop = 2;
    			drop = new ItemStack(ModItems.crystal, 1, 0);
    			dropChance = 0.3F;
                break;
            case WIND:
                maxDrop = 2;
    			drop = new ItemStack(ModItems.crystal, 1, 3);
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
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return entityIn.getEntityBoundingBox();
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (key.equals(level)) {
            this.updateStatsToLevel();
        }
        super.notifyDataManagerChange(key);
    }

    private void updateStatsToLevel() {
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(LevelCalc.initStatIncreaseLevel(MobConfig.gateHealth, this.level(), false, true, 1.1f - this.world.rand.nextFloat() * 0.2f));
        this.getAttributeMap().getAttributeInstance(ItemStatAttributes.RFDEFENCE).setBaseValue(LevelCalc.initStatIncreaseLevel(MobConfig.gateDef, this.level(), false, false, 1.1f - this.world.rand.nextFloat() * 0.2f));
        this.getAttributeMap().getAttributeInstance(ItemStatAttributes.RFMAGICDEF).setBaseValue(LevelCalc.initStatIncreaseLevel(MobConfig.gateMDef, this.level(), false, false, 1.1f - this.world.rand.nextFloat() * 0.2f));
        this.setHealth(this.getMaxHealth());
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        Biome biome = this.world.getBiome(this.getPosition());
        List<String> list = GateSpawning.getSpawningListFromBiome(biome);
        for (int counter = (list.size() < 3) ? list.size() : 3; counter > 0; --counter) {
            String randEnt = GateSpawning.getSpawningListFromBiome(biome).get(this.rand.nextInt(GateSpawning.getSpawningListFromBiome(biome).size()));
            if (!this.spawnList.contains(randEnt)) {
                this.spawnList.add(randEnt);
            }
        }
        this.dataManager.set(level, LevelCalc.levelFromDistSpawn(this.world, this.getPosition()));
        this.type = this.getType(biome);
        this.dataManager.set(elementType, this.type.getName());
        ++this.posY;
        this.updateStatsToLevel();
        return livingdata;
    }
    
    private EnumElement getType(Biome biome) {
        EnumElement element = EnumElement.values()[this.world.rand.nextInt(EnumElement.values().length)];
        if (BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.PLAINS) && this.world.rand.nextFloat() < 0.5) {
            element = EnumElement.NONE;
        }
        else if (BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.FOREST) && this.world.rand.nextFloat() < 0.5) {
            element = EnumElement.WIND;
        }
        else if (BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.HOT) && this.world.rand.nextFloat() < 0.5) {
            element = EnumElement.FIRE;
        }
        else if (BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.MOUNTAIN) && this.world.rand.nextFloat() < 0.5) {
            element = EnumElement.WIND;
        }
        else if (BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.OCEAN) && this.world.rand.nextFloat() < 0.5) {
            element = EnumElement.WATER;
        }
        else if (BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.SANDY) && this.world.rand.nextFloat() < 0.5) {
            element = EnumElement.EARTH;
        }
        else if (BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.MAGICAL)) {
            if (this.world.rand.nextFloat() < 0.4) {
                element = EnumElement.LIGHT;
            }
            else if (this.world.rand.nextFloat() < 0.2) {
                element = EnumElement.LOVE;
            }
        }
        else if (BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.SPOOKY) && this.world.rand.nextFloat() < 0.4) {
            element = EnumElement.DARK;
        }
        if (biome == Biomes.SKY) {
            if (this.world.rand.nextFloat() < 0.3) {
                element = EnumElement.DARK;
            }
            else if (this.world.rand.nextFloat() < 0.3) {
                element = EnumElement.LIGHT;
            }
        }
        return element;
    }
    
    public EnumElement entityElement() {
        return this.type;
    }
}
