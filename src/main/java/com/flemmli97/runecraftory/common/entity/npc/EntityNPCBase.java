package com.flemmli97.runecraftory.common.entity.npc;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.lib.LibConstants;
import com.flemmli97.runecraftory.common.world.village.NPCVillage;
import com.flemmli97.runecraftory.common.world.village.NPCVillageHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityNPCBase extends EntityAgeable implements IEntityAdditionalSpawnData, INpc
{
    private Map<IAttribute, Double> baseValues;
    private Map<UUID, Integer> playerHearts;
    
    private static final DataParameter<Integer> level = EntityDataManager.createKey(EntityNPCBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> levelXP = EntityDataManager.createKey(EntityNPCBase.class, DataSerializers.VARINT);
    
    private UUID fatherUUID;
    private UUID motherUUID;
    private UUID[] childUUIDs;
    
    private UUID village;
    private int villageCheck;
    
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
	protected void updateAITasks()
    {
    	if(this.villageCheck<0) {
    		this.villageCheck=100+this.rand.nextInt(50);
    		if(this.village==null) {
    			NPCVillage vill = NPCVillageHandler.get(this.world).nearestFrom(this.getPosition(), 64);
    			if(vill!=null)
    				this.village=vill.getUUID();
    		}
    		else if(NPCVillageHandler.get(this.world).getFromUUID(this.village)==null){
    			this.village=null;
    		}
    	}
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

    @Override
    public void handleStatusUpdate(byte id) {
    	if (id == 12)
        {
            this.spawnParticles(EnumParticleTypes.HEART);
        }
        else if (id == 13)
        {
            this.spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
        }
        else if (id == 14)
        {
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }
    
    @SideOnly(Side.CLIENT)
    private void spawnParticles(EnumParticleTypes particleType)
    {
        for (int i = 0; i < 5; ++i)
        {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.world.spawnParticle(particleType, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 1.0D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
        }
    }

    @Override
    public EntityNPCBase createChild(EntityAgeable ageable) {
        return null;
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
}
