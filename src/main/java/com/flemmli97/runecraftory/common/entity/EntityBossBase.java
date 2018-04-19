package com.flemmli97.runecraftory.common.entity;

import java.util.Set;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.api.items.IItemBase;
import com.flemmli97.runecraftory.api.items.IRFFood;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public abstract class EntityBossBase extends EntityMobBase{

    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS));
    private static final DataParameter<Boolean> enraged = EntityDataManager.<Boolean>createKey(EntityMobBase.class, DataSerializers.BOOLEAN);

	protected EntityBossBase(World world, boolean ridable, int baseXP, int baseMoney,
			boolean isFlyingEntity) {
		super(world, ridable, baseXP, baseMoney, isFlyingEntity);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);;

	}

	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(enraged, false);
    }
	
	public boolean isEnraged()
	{
		return this.dataManager.get(enraged);
	}
	
	public void setEnraged(boolean flag)
	{
		this.dataManager.set(enraged, flag);
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if(!this.world.isRemote)
		{
			/*List<EntityPlayer> list = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(10));
			for(EntityPlayer player : list)
			{
				EntityPlayerMP playerMP = (EntityPlayerMP) player;
				if(!bossInfo.getPlayers().contains(playerMP))
					bossInfo.addPlayer(playerMP);
			}
			for(EntityPlayerMP player : bossInfo.getPlayers())
			{
				if(player.getDistanceSq(this)>100)
					bossInfo.removePlayer(player);
			}*/
			this.updateplayers();
			bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
		}
	}
	
    private void updateplayers()
    {
        Set<EntityPlayerMP> set = Sets.<EntityPlayerMP>newHashSet();

        for (EntityPlayerMP entityplayermp : this.world.getEntitiesWithinAABB(EntityPlayerMP.class, this.getEntityBoundingBox().grow(10)))
        {
            this.bossInfo.addPlayer(entityplayermp);
            set.add(entityplayermp);
        }

        Set<EntityPlayerMP> set1 = Sets.newHashSet(this.bossInfo.getPlayers());
        set1.removeAll(set);

        for (EntityPlayerMP entityplayermp1 : set1)
        {
            this.bossInfo.removePlayer(entityplayermp1);
        }
    }
    
	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItemMainhand();
		if(!stack.isEmpty() && player.isSneaking() && !this.world.isRemote)
		{
			if(stack.getItem()==Items.GOLDEN_APPLE)
			{
				this.getAttributeMap().getAttributeInstance(ItemStats.RFATTACK).setBaseValue(999999);
				return true;
			}
			if(stack.getItem()==ModItems.tame)
			{
				this.tameEntity(player);
			}
			else if(!this.isTamed() && this.tamingTick==-1 && this.deathTime==0)
			{
				boolean flag=false;
				if(this.tamingItem()!=null)
					for(ItemStack item : this.tamingItem())
						if(item.getItem()== stack.getItem() && item.getMetadata()==stack.getMetadata())
							flag=true;
				if(!player.capabilities.isCreativeMode)
					stack.shrink(1);
				if(this.rand.nextFloat()<=this.tamingChance()*LevelCalc.tamingMultiplerOnLevel(this.level()) && flag)
				{
					this.tameEntity(player);
				}
				if(stack.getItem() instanceof IRFFood)
				{
					//heal
				}
				this.tamingTick=60;
                this.world.setEntityState(this, (byte)34);
			}
			else
			{
				//heal
				if(stack.getItem()==Items.STICK)
				{
					System.out.println("untame");
					this.dataManager.set(isTamed, false); //for debugging

				}
				else if(stack.getItem()==ModItems.inspector)
				{
					//open tamed gui
				}
				else if(feedTimeOut<=0 && stack.getItem() instanceof IRFFood || stack.getItem() instanceof IItemBase)
				{
					//food effect
					
					this.feedTimeOut=24000;
				}
				
			}
			return true;
		}
		else if(stack.isEmpty() && !this.world.isRemote && this.isTamed() && this.ridable())
		{
			player.startRiding(this);
			return true;
		}
		return false;
	}
	
	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		
	}
	
	/**
     * Add the given player to the list of players tracking this entity. For instance, a player may track a boss in
     * order to view its associated boss bar.
     */
    public void addTrackingPlayer(EntityPlayerMP player)
    {
        super.addTrackingPlayer(player);
    }

    /**
     * Removes the given player from the list of players tracking this entity. See {@link Entity#addTrackingPlayer} for
     * more information on tracking.
     */
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);
    }

    //========Unused for bosses, since they have their customized own ai
	@Override
	public int getAttackTimeFromPattern(byte pattern) {
		return 0;
	}

	@Override
	public int attackFromPattern() {
		return 0;
	}

	@Override
	public int maxAttackPatterns() {
		return 0;
	}
	@Override
	public void setDead()
    {
        this.isDead = true;
        for (EntityPlayerMP entityplayermp1 : Sets.newHashSet(this.bossInfo.getPlayers()))
        {
            this.bossInfo.removePlayer(entityplayermp1);
        }
    }
}
