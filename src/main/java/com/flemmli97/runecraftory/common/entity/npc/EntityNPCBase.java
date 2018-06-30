package com.flemmli97.runecraftory.common.entity.npc;

import java.util.Map;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.entity.EntityBossBase;
import com.flemmli97.runecraftory.common.lib.LibConstants;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class EntityNPCBase extends EntityVillager implements IEntityAdditionalSpawnData
{
    private Map<IAttribute, Double> baseValues;
    private Map<String, Integer> playerHearts;
    
    private static final DataParameter<Integer> level = EntityDataManager.createKey(EntityNPCBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> levelXP = EntityDataManager.createKey(EntityNPCBase.class, DataSerializers.VARINT);
    
    private String fatherUUID;
    private String motherUUID;
    private String[] childUUIDs;
    
    public EntityNPCBase(World worldIn) {
        super(worldIn);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0));
        this.tasks.addTask(3, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(level, LibConstants.baseLevel);
        this.dataManager.register(levelXP, 0);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        return true;
    }

    @Override
    public void onDeath(DamageSource cause) {
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
    }

    //=====Vanilla villager disabling
    
    @Override
    protected void onGrowingAdult() {
    }

    @Override
    public ITextComponent getDisplayName() {
        TextComponentString textcomponentstring = new TextComponentString(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));
        textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
        textcomponentstring.getStyle().setInsertion(this.getCachedUniqueIdString());
        return (ITextComponent)textcomponentstring;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
    }

    @Override
    public IEntityLivingData finalizeMobSpawn(DifficultyInstance diff, @Nullable IEntityLivingData data, boolean setprofession) {
        return data;
    }

    @Override
    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        return false;
    }

    @Override
    public boolean isFarmItemInInventory() {
        return false;
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
    }

    @Override
    public EntityVillager createChild(EntityAgeable ageable) {
        return null;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isTrading() ? SoundEvents.ENTITY_VILLAGER_TRADING : SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_VILLAGER;
    }

    @Override
    public void setProfession(int professionId) {
    }

    @Override
    public int getProfession() {
        return 0;
    }

    @Override
    public void setProfession(VillagerRegistry.VillagerProfession prof) {
    }

    @Override
    public VillagerRegistry.VillagerProfession getProfessionForge() {
        return VillagerRegistry.FARMER;
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
    }

    @Override
    public boolean isMating() {
        return false;
    }

    @Override
    public void setMating(boolean mating) {
    }

    @Override
    public void setPlaying(boolean playing) {
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void setRevengeTarget(@Nullable EntityLivingBase livingBase) {
        super.setRevengeTarget(livingBase);
        if (livingBase instanceof EntityPlayer) {
            if (this.isChild()) {
            }
            if (this.isEntityAlive()) {
                this.world.setEntityState(this, (byte)13);
            }
        }
    }

    @Override
    public void setCustomer(@Nullable EntityPlayer player) {
    }

    @Override
    @Nullable
    public EntityPlayer getCustomer() {
        return null;
    }

    @Override
    public boolean isTrading() {
        return false;
    }

    @Override
    public boolean getIsWillingToMate(boolean updateFirst) {
        return false;
    }

    @Override
    public void setIsWillingToMate(boolean isWillingToMate) {
    }

    @Override
    public void useRecipe(MerchantRecipe recipe) {
    }

    @Override
    public void verifySellingItem(ItemStack stack) {
        if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
            this.livingSoundTime = -this.getTalkInterval();
            this.playSound(stack.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Override
    @Nullable
    public MerchantRecipeList getRecipes(EntityPlayer player) {
        return null;
    }

    @Override
    public boolean isChild() {
        return this.getGrowingAge() < 0;
    }

    @Override
    public void setScaleForAge(boolean child) {
    }
}
