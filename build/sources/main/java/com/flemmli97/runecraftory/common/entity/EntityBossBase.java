package com.flemmli97.runecraftory.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
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
        this.bossInfo.addPlayer(player);
    }

    /**
     * Removes the given player from the list of players tracking this entity. See {@link Entity#addTrackingPlayer} for
     * more information on tracking.
     */
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
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
    
    
}
