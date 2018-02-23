package com.flemmli97.runecraftory.common.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.api.entities.IEntityAdvanced;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.entities.IRFNpc;
import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.api.items.IItemWearable;
import com.flemmli97.runecraftory.api.items.IRpUseItem;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.lib.CalculationConstants;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.lib.enums.EnumStatusEffect;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RFCalculations {

	//TODO

	public static int xpAmountForLevelUp(int level)
	{
		return level^2;
	}
	//TODO

	public static int xpAmountForSkills(int level)
	{
		return level^2;
	}
	//TODO

	public static float tamingMultiplerOnLevel(int levelDiff)
	{
		if(levelDiff<=0)
			levelDiff=1;
		float f = 1;///level;
		return Math.min(f, 1);
	}
	
	public static float scaleForVanilla(float damage)
	{
		if(damage<=100)
			damage=damage*0.03F;
		else if(damage<=1000)
			damage=damage*0.012F+3;
		else if(damage<=10000)
			damage=(float) (Math.sqrt((damage-1000)/10.0F)+15);
		else
			damage=(float) (Math.sqrt((damage-10000)/29.7517355371900826F)+45);
		return damage;
	}
	
	//TODO
	public static int xpFromLevel(int base, int level)
	{
		return base + base*level;
	}
	//TODO

	public static int moneyFromLevel(int base, int level)
	{
		return Math.round(base + base*level/50.0F);
	}
	//TODO

	public static int levelFromDistSpawn(World world, BlockPos currentPos)
	{
		int level = CalculationConstants.baseLevel;
		BlockPos spawn = world.getSpawnPoint();
		double dis =1;
		if(world.provider.getDimension()==0)
			dis= Math.sqrt(spawn.distanceSq(currentPos.getX(), spawn.getY(), currentPos.getZ()));
		else
			dis=Math.sqrt(spawn.distanceSq(currentPos.getX(), spawn.getY(), currentPos.getZ()))+200;
			dis*=1.2;
		return Math.round((float) (level+Math.max(1, (dis-250)*0.1F) ));
	}
	
	public static List<EntityLivingBase> calculateEntitiesFromLook(EntityPlayer player, float reach, float aoe)
	{
		List<EntityLivingBase> theList = new ArrayList<EntityLivingBase>();
		for(EntityLivingBase nearby : player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(reach)))
		{
			if (nearby != player && nearby.canBeCollidedWith() && player.canEntityBeSeen(nearby) && (aoe>27 ||isTargetInFrontOf(player, nearby, aoe*10)))
			{
				if (!theList.contains(nearby) && !nearby.isOnSameTeam(player) && nearby.getDistance(player)<=reach) 
				{
					theList.add(nearby);
				}
			}
		}
		return theList;
	}
	
	/**
	 * Gets entites in front of target			
	 */
	public static final boolean isTargetInFrontOf(EntityLivingBase player, EntityLivingBase target, float fov) {
		double dx = target.posX - player.posX;
		double dz = target.posZ - player.posZ;
		double dy = target.posY+target.height/2;
		double dis = player.getPositionEyes(1).squareDistanceTo(target.posX,dy,target.posZ);
		double heightDif= player.posY+player.getEyeHeight() - dy;
		if(heightDif==0)
			heightDif=0.001;
		if(dx==0 && dz==0)
			dx=0.001;
		while (player.rotationYaw > 360) { player.rotationYaw -= 360; }
		while (player.rotationYaw < -360) { player.rotationYaw += 360; }
		float yaw = (float)(Math.atan2(dz, dx) * 180.0D / Math.PI) - player.rotationYaw;
		yaw -= 90;
		float pitchFov = (float) ((Math.asin(heightDif/dis)* 180.0D / Math.PI));
		pitchFov-=32;
		while (yaw < -180) { yaw += 360; }
		while (yaw >= 180) { yaw -= 360; }
		return yaw < fov && yaw > -fov && (player.rotationPitch>=0 || pitchFov<player.rotationPitch);
	}
	
	public static EntityLivingBase calculateSingleEntityFromLook(EntityPlayer player, float reach)
	{
		EntityLivingBase result = null;
		if(player.world!=null)
		{
            Vec3d posVec = player.getPositionEyes(1);
            Vec3d look = player.getLook(1);
            Vec3d rangeVec = posVec.addVector(look.x * reach, look.y * reach, look.z * reach);
            List<EntityLivingBase> list = player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().expand(look.x * reach, look.y * reach, look.z * reach).expand(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
            {
                public boolean apply(@Nullable Entity entity)
                {
                    return entity != null && entity.canBeCollidedWith() && entity!=player;
                }
            }));
            for(int i = 0; i < list.size(); ++i)
            {
            		EntityLivingBase entity1 = list.get(i);
            		AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double)entity1.getCollisionBorderSize());
            		RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(posVec, rangeVec);
            		if (raytraceresult != null)
            		{
                    double d3 = posVec.distanceTo(raytraceresult.hitVec);

                    if (d3 < reach)
                    {
                    		result = entity1;
                    }
                }
            }
            if(result!=null)
            {
            		return result;
            }
		}
		return result;
	}
	
	/**Player Damage*/
	public static boolean doPlayerDamagePhys(EntityPlayer player, EntityLivingBase target)
	{
		IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
		ItemStack stack = player.getHeldItemMainhand();
		IRpUseItem item = (IRpUseItem) stack.getItem();
		if (target.canBeAttackedWithItem())
        {
            if (!target.hitByEntity(player))
            {
            		//=====Add damage
            		float damagePhys = cap.getStr();
            			damagePhys+=getAttributeValue(player, ItemStats.RFATTACK, null, null);
            		float coolDown = player.getCooldownTracker().getCooldown(stack.getItem(), 0);
            		if(coolDown>0)
            		{
            			return false;
            		}
            		if (damagePhys > 0.0F)
                {
            			player.getCooldownTracker().setCooldown(stack.getItem(), item.itemCoolDownTicks());
            			boolean faintChance = player.world.rand.nextInt(100)<getAttributeValue(player, ItemStats.RFFAINT, target, ItemStats.RFRESFAINT);
            			boolean critChance = player.world.rand.nextInt(100)<getAttributeValue(player, ItemStats.RFCRIT, target, ItemStats.RFRESCRIT);
            			boolean knockBackChance = player.world.rand.nextInt(100)<getAttributeValue(player, ItemStats.RFKNOCK, target, SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
            			int i = knockBackChance?2:0;

                    if (player.isSprinting())
                    {
                        player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F, 1.0F);
                        ++i;
                    }
                    double d1 = target.motionX;
                    double d2 = target.motionY;
                    double d3 = target.motionZ;
                    damagePhys = faintChance? Float.MAX_VALUE: (damagePhys+= player.world.rand.nextGaussian()*damagePhys/10.0F);
                    boolean ignoreArmor = critChance||faintChance;
                    if(!(target instanceof IEntityBase))
    					{
                    		damagePhys = RFCalculations.scaleForVanilla(damagePhys);
    					}
                    else
                    		damagePhys = faintChance?Float.MAX_VALUE:damagePhys;

                    boolean flag5 = target.attackEntityFrom(CustomDamage.doAttack(player, ItemNBT.getElement(stack), ignoreArmor?1:0), damagePhys);

                    if (flag5)
                    {
                        if (i > 0)
                        {
                            if (target instanceof EntityLivingBase)
                            {
                                ((EntityLivingBase)target).knockBack(player, (float)i * 0.5F, (double)MathHelper.sin(player.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                            }
                            else
                            {
                                target.addVelocity((double)(-MathHelper.sin(player.rotationYaw * 0.017453292F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(player.rotationYaw * 0.017453292F) * (float)i * 0.5F));
                            }

                            player.motionX *= 0.6D;
                            player.motionZ *= 0.6D;
                            player.setSprinting(false);
                        }
                        float drainPercent= getAttributeValue(player, ItemStats.RFDRAIN, target,ItemStats.RFRESDRAIN);
                        if(drainPercent>0)
                        		cap.regenHealth(player, drainPercent*damagePhys);
                                                
                        applyStatusEffects(player, target);
                        //============================
                        if (target instanceof EntityPlayerMP && target.velocityChanged)
                        {
                            ((EntityPlayerMP)target).connection.sendPacket(new SPacketEntityVelocity(target));
                            target.velocityChanged = false;
                            target.motionX = d1;
                            target.motionY = d2;
                            target.motionZ = d3;
                        }

                        if (critChance)
                        {
                            player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0F, 1.0F);
                            player.onCriticalHit(target);
                            player.onEnchantmentCritical(target);
                        }
                        else if(item.getWeaponType().getAOE()==0)
                        {
                            player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);
                        }
                        else
                        {
                            player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
                        }

                        player.setLastAttackedEntity(target);

                        if (target instanceof EntityLivingBase)
                        {
                            EnchantmentHelper.applyThornEnchantments((EntityLivingBase)target, player);
                        }
                        ItemStack beforeHitCopy = stack.copy();
                        stack.hitEntity(target, player);
                        if (stack.isEmpty())
                        {
                            net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, beforeHitCopy, EnumHand.MAIN_HAND);
                            player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                        }
                        if (!(target instanceof IEntityBase) && ItemNBT.getElement(stack)==EnumElement.FIRE && !target.isBurning())
                        {
                            target.setFire(8);
                        }
                        player.addExhaustion(0.1F);
                    }
                    else
                    {
                        player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundCategory(), 1.0F, 1.0F);
                    }
                    return true;
                }
            }
        }
		return false;
	}
	
	public static void applyStatusEffects(EntityLivingBase attackingEntity, EntityLivingBase target)
	{        
		if(target instanceof EntityPlayer || target instanceof IEntityAdvanced)
		{
	        boolean poisonChance = attackingEntity.world.rand.nextInt(100)<getAttributeValue(attackingEntity, ItemStats.RFPOISON, target, ItemStats.RFRESPOISON);
	        boolean sleepChance= attackingEntity.world.rand.nextInt(100)<getAttributeValue(attackingEntity, ItemStats.RFSLEEP, target, ItemStats.RFRESSLEEP);
	        boolean fatigueChance= attackingEntity.world.rand.nextInt(100)<getAttributeValue(attackingEntity, ItemStats.RFFAT, target, ItemStats.RFRESFAT);
	        boolean coldChance= attackingEntity.world.rand.nextInt(100)<getAttributeValue(attackingEntity, ItemStats.RFCOLD, target, ItemStats.RFRESCOLD);
	        boolean paraChance= attackingEntity.world.rand.nextInt(100)<getAttributeValue(attackingEntity, ItemStats.RFPARA, target, ItemStats.RFRESPARA);
	        boolean sealChance= attackingEntity.world.rand.nextInt(100)<getAttributeValue(attackingEntity, ItemStats.RFSEAL, target, ItemStats.RFRESSEAL);
	        boolean dizzyChance= attackingEntity.world.rand.nextInt(1000)<getAttributeValue(attackingEntity, ItemStats.RFDIZ, target, ItemStats.RFRESDIZ);
	        boolean stunChance= attackingEntity.world.rand.nextInt(100)<getAttributeValue(attackingEntity, ItemStats.RFSTUN, target, ItemStats.RFRESSTUN);
	        
	        if(poisonChance)
	        		if(target instanceof EntityPlayer)
	        			target.getCapability(PlayerCapProvider.PlayerCap, null).addStatus((EntityPlayer) target, EnumStatusEffect.POISON);
	        		else 
	        			((IEntityAdvanced) target).addStatus(EnumStatusEffect.POISON);
	        if(fatigueChance)
        			if(target instanceof EntityPlayer)
        				target.getCapability(PlayerCapProvider.PlayerCap, null).addStatus((EntityPlayer) target, EnumStatusEffect.FATIGUE);
        			else 
        				((IEntityAdvanced) target).addStatus(EnumStatusEffect.FATIGUE);
	        if(coldChance)
        			if(target instanceof EntityPlayer)
        				target.getCapability(PlayerCapProvider.PlayerCap, null).addStatus((EntityPlayer) target, EnumStatusEffect.COLD);
        			else 
        				((IEntityAdvanced) target).addStatus(EnumStatusEffect.COLD);
	        if(paraChance)
        			if(target instanceof EntityPlayer)
        				target.getCapability(PlayerCapProvider.PlayerCap, null).addStatus((EntityPlayer) target, EnumStatusEffect.PARALYSIS);
        			else 
        				((IEntityAdvanced) target).addStatus(EnumStatusEffect.PARALYSIS);
	        if(sealChance)
    				if(target instanceof EntityPlayer)
    					target.getCapability(PlayerCapProvider.PlayerCap, null).addStatus((EntityPlayer) target, EnumStatusEffect.SEAL);
    				else 
    					((IEntityAdvanced) target).addStatus(EnumStatusEffect.SEAL);
	        if(dizzyChance)
					target.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:nausea"), 80, 1, true, false));
	        if(stunChance||sleepChance)
	        {
				target.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:slowness"), 80, 4, true, false));
				target.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:jump_boost"), 80, 1, true, false));
	        }
		}
	}
	
	/**
	 * Gets the specific value for the attribute. Mainhand can't be null and has to be an IItemWearable
	 * @param entity The entity
	 * @param att The attribute to calculate
	 * @return
	 */
	public static float getAttributeValue(EntityLivingBase entity, IAttribute att, EntityLivingBase target, IAttribute resAtt)
	{
		float increase = 0;
		if(entity instanceof EntityPlayer)
		{
			if(entity.getHeldItemMainhand().getItem()instanceof IRpUseItem)
			{
				if(ItemNBT.statIncrease(entity.getHeldItemMainhand()).get(att)!=null)
				{
					increase += ItemNBT.statIncrease(entity.getHeldItemMainhand()).get(att).floatValue();
				}
			}
			for(ItemStack equip : entity.getArmorInventoryList())
			{
				if(!equip.isEmpty() && equip.getItem() instanceof IItemWearable)
				{
					if(ItemNBT.statIncrease(equip).get(att)!=null)
		        		{
						increase += ItemNBT.statIncrease(equip).get(att).floatValue();
		        		}
				}
			}
			if(entity.getHeldItemOffhand().getItem() instanceof IItemWearable && !(entity.getHeldItemOffhand().getItem() instanceof IRpUseItem))
			{
				if(ItemNBT.statIncrease(entity.getHeldItemOffhand()).get(att)!=null)
		    		{
					increase += ItemNBT.statIncrease(entity.getHeldItemOffhand()).get(att).floatValue();
		    		}
			}
		}
		else if(entity instanceof IEntityBase)
		{
			if(entity.getAttributeMap().getAttributeInstance(att)!=null)
			increase += entity.getAttributeMap().getAttributeInstance(att).getAttributeValue();
		}
		if(target instanceof IEntityBase)
		{
			if(target.getAttributeMap().getAttributeInstance(att)!=null)
			increase -= target.getAttributeMap().getAttributeInstance(att).getAttributeValue();
		}
		else if(target instanceof IRFNpc || target instanceof EntityPlayer)
		{
			if(target.getHeldItemMainhand().getItem()instanceof IRpUseItem)
			{
				if(ItemNBT.statIncrease(target.getHeldItemMainhand()).get(att)!=null)
				{
					increase -= ItemNBT.statIncrease(target.getHeldItemMainhand()).get(att).floatValue();
				}
			}
			for(ItemStack equip : target.getArmorInventoryList())
			{
				if(!equip.isEmpty() && equip.getItem() instanceof IItemWearable)
				{
					if(ItemNBT.statIncrease(equip).get(att)!=null)
	        			{
						increase -= ItemNBT.statIncrease(equip).get(att).floatValue();
	        			}
				}
			}
			if(!target.getHeldItemOffhand().isEmpty()&& target.getHeldItemOffhand().getItem() instanceof IItemWearable && !(target.getHeldItemOffhand().getItem() instanceof IRpUseItem))
			{
				if(ItemNBT.statIncrease(target.getHeldItemOffhand()).get(att)!=null)
		    		{
					increase -= ItemNBT.statIncrease(target.getHeldItemOffhand()).get(att).floatValue();
		    		}
			}
		}
		return increase;
	}
	
	public static void getPlayerDamageReduction(EntityPlayer player, DamageSource source, float amount)
	{
		float reduce = 0;
		IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
		if(!source.isDamageAbsolute() && !source.isUnblockable())
			if(source.isMagicDamage())
				reduce = capSync.getVit()*0.5F + (float)getAttributeValue(player, ItemStats.RFMAGICDEF, null, null);
			else
				reduce = capSync.getVit()*0.5F + (float)getAttributeValue(player, ItemStats.RFDEFENCE, null, null);
		amount = Math.max(0, amount-reduce);
		capSync.damage(player, source, amount);
	}
	
	public static float statIncreaseLevel(float baseValue, int level, boolean isBoss)
	{
		float scale = isBoss?1.2F:0.9F;
		float newValue = (float) (baseValue+baseValue/10.0*Math.max(1, level-10)*scale);
		return newValue;
	}
	
}
                
