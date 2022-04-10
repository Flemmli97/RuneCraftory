package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.api.entities.IEntityAdvanced;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.entities.IRFNpc;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.init.PotionRegistry;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.tenshilib.api.item.IAOEWeapon;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Random;

public class RFCalculations
{
	/**
	 * If daily occurence can update
	 */
    public static boolean canUpdateDaily(World world) 
    {
        return world.getGameRules().getBoolean("doDaylightCycle") && world.getWorldTime() % 24000 == 1;
    }
    
    /**
     * The player attack
     * @param player the attacking player
     * @param target the target
     * @param resetCooldown should the attack reset cooldown
     * @param playSound should the attack play sounds
     * @return if the attack was successful or not
     */
    public static boolean doPlayerAttack(EntityPlayer player, EntityLivingBase target, boolean resetCooldown, boolean playSound, boolean levelSkill) 
    {
        IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
        ItemStack stack = player.getHeldItemMainhand();
        IItemUsable item = (IItemUsable)stack.getItem();
        if (target.canBeAttackedWithItem() && !target.hitByEntity(player)) {
        	//Gather damage from player
            float damagePhys = getAttributeValue(player, ItemStatAttributes.RFATTACK, null, null);
            float coolDown = player.getCooldownTracker().getCooldown(stack.getItem(), 0.0f);
            //If cooldown isnt finished disable further processing. 
            if (coolDown > 0.0f) {
                return false;
            }
            if (damagePhys > 0.0f) {
                if (resetCooldown) {
                    player.getCooldownTracker().setCooldown(stack.getItem(), item.itemCoolDownTicks());
                }
                boolean faintChance = player.world.rand.nextInt(100) < getAttributeValue(player, ItemStatAttributes.RFFAINT, target, ItemStatAttributes.RFRESFAINT);
                boolean critChance = player.world.rand.nextInt(100) < getAttributeValue(player, ItemStatAttributes.RFCRIT, target, ItemStatAttributes.RFRESCRIT);
                boolean knockBackChance = player.world.rand.nextInt(100) < getAttributeValue(player, ItemStatAttributes.RFKNOCK, target, SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
                int i = knockBackChance ? 2 : 1;
                if (player.isSprinting()) {
                    player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0f, 1.0f);
                    ++i;
                }
                double d1 = target.motionX;
                double d2 = target.motionY;
                double d3 = target.motionZ;
                damagePhys = faintChance ? Float.MAX_VALUE : (damagePhys += (float)(player.world.rand.nextGaussian() * damagePhys / 10.0));
                boolean ignoreArmor = critChance || faintChance;
                //Scale damage for vanilla mobs
                if (!(target instanceof IEntityBase)) {
                    damagePhys = LevelCalc.scaleForVanilla(damagePhys);
                }
                else {
                    damagePhys = (faintChance ? Float.MAX_VALUE : damagePhys);
                }
                float knockback = i * 0.5f - 0.1f;
                if (target instanceof EntityMobBase) {
                    knockback *= 0.85f;
                }
                CustomDamage source = CustomDamage.attack(player, ItemNBT.getElement(stack), ignoreArmor ? CustomDamage.DamageType.IGNOREDEF : CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.VANILLA, knockback, 0);
                if (playerDamage(player, target, source, damagePhys, cap, stack)) 
                {
                	//Level skill on successful attack
                	if(levelSkill)
                		item.levelSkillOnHit(player);
                    if (i > 0) {
                        player.motionX *= 0.6;
                        player.motionZ *= 0.6;
                        player.setSprinting(false);
                    }
                    if (target instanceof EntityPlayerMP && target.velocityChanged) {
                        ((EntityPlayerMP)target).connection.sendPacket(new SPacketEntityVelocity(target));
                        target.velocityChanged = false;
                        target.motionX = d1;
                        target.motionY = d2;
                        target.motionZ = d3;
                    }
                    if (critChance) 
                    {
                        if (playSound) 
                        {
                            player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0f, 1.0f);
                        }
                        player.onCriticalHit(target);
                        player.onEnchantmentCritical(target);
                    }
                    else if (stack.getItem() instanceof IAOEWeapon && ((IAOEWeapon)stack.getItem()).getFOV() == 0.0f && playSound) 
                    {
                        player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0f, 1.0f);
                    }
                    else if (playSound) 
                    {
                        player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0f, 1.0f);
                    }
                }
                else if (playSound) 
                {
                    player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundCategory(), 1.0f, 1.0f);
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean playerDamage(EntityPlayer player, EntityLivingBase target, CustomDamage source, float damagePhys, IPlayer cap, ItemStack stack) 
    {
        boolean success = target.attackEntityFrom(source, damagePhys);
        spawnElementalParticle(target, source.getElement());
        if (success) 
        {
            if (target instanceof EntityLivingBase) 
            {
            	//Using custom knockback
                knockBack(target, source);
            }
            int drainPercent = getAttributeValue(player, ItemStatAttributes.RFDRAIN, target, ItemStatAttributes.RFRESDRAIN);
            if (drainPercent > 0f) 
            {
                cap.regenHealth(player, drainPercent * damagePhys);
            }
            applyStatusEffects(player, target);
            player.setLastAttackedEntity(target);
            if (target instanceof EntityLivingBase) {
                EnchantmentHelper.applyThornEnchantments(target, player);
            }
            ItemStack beforeHitCopy = stack.copy();
            stack.hitEntity(target, player);
            if (stack.isEmpty()) 
            {
                ForgeEventFactory.onPlayerDestroyItem(player, beforeHitCopy, EnumHand.MAIN_HAND);
                player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
            }
            applyAddEffect(ItemNBT.getElement(stack), target);
        }
        return success;
    }
    
    public static void applyAddEffect(EnumElement element, EntityLivingBase target) 
    {
        if (!(target instanceof IEntityBase) && element == EnumElement.FIRE && !target.isBurning()) 
        {
            target.setFire(3);
        }
    }
    
    //TODO: Need to look more into stun and dizzy
    public static void applyStatusEffects(EntityLivingBase attackingEntity, EntityLivingBase target) 
    {
        if (target instanceof EntityPlayer || target instanceof IEntityAdvanced) {
            boolean poisonChance = attackingEntity.world.rand.nextInt(100) < getAttributeValue(attackingEntity, ItemStatAttributes.RFPOISON, target, ItemStatAttributes.RFRESPOISON);
            boolean sleepChance = attackingEntity.world.rand.nextInt(100) < getAttributeValue(attackingEntity, ItemStatAttributes.RFSLEEP, target, ItemStatAttributes.RFRESSLEEP);
            boolean fatigueChance = attackingEntity.world.rand.nextInt(100) < getAttributeValue(attackingEntity, ItemStatAttributes.RFFAT, target, ItemStatAttributes.RFRESFAT);
            boolean coldChance = attackingEntity.world.rand.nextInt(100) < getAttributeValue(attackingEntity, ItemStatAttributes.RFCOLD, target, ItemStatAttributes.RFRESCOLD);
            boolean paraChance = attackingEntity.world.rand.nextInt(100) < getAttributeValue(attackingEntity, ItemStatAttributes.RFPARA, target, ItemStatAttributes.RFRESPARA);
            boolean sealChance = attackingEntity.world.rand.nextInt(100) < getAttributeValue(attackingEntity, ItemStatAttributes.RFSEAL, target, ItemStatAttributes.RFRESSEAL);
            boolean dizzyChance = attackingEntity.world.rand.nextInt(1000) < getAttributeValue(attackingEntity, ItemStatAttributes.RFDIZ, target, ItemStatAttributes.RFRESDIZ);
            boolean stunChance = attackingEntity.world.rand.nextInt(100) < getAttributeValue(attackingEntity, ItemStatAttributes.RFSTUN, target, ItemStatAttributes.RFRESSTUN);
            if (poisonChance) {
                target.addPotionEffect(new PotionEffect(PotionRegistry.poison));
            }
            if (fatigueChance) {
                target.addPotionEffect(new PotionEffect(PotionRegistry.fatigue));
            }
            if (coldChance) {
                target.addPotionEffect(new PotionEffect(PotionRegistry.cold));
            }
            if (paraChance) {
                target.addPotionEffect(new PotionEffect(PotionRegistry.paralysis));
            }
            if (sealChance) {
                target.addPotionEffect(new PotionEffect(PotionRegistry.seal));
            }
            if (dizzyChance) {
                target.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:nausea"), 80, 1, true, false));
            }
            if (stunChance) {
                target.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:slowness"), 80, 4, true, false));
                target.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:jump_boost"), 80, 1, true, false));
            }
            if (sleepChance) {
                target.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:blindness"), 80, 1, true, false));
                target.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("runecraftory:sleep"), 80, 1, true, false));
            }
        }
    }
    
    public static int getAttributeValue(EntityLivingBase entity, IAttribute att, EntityLivingBase target, IAttribute resAtt) 
    {
        int increase = 0;
        if (entity instanceof EntityPlayer) 
        {
        	IPlayer cap = entity.getCapability(PlayerCapProvider.PlayerCap, null);
        	increase+=cap.getAttributeValue(att);
        }
        else if(entity instanceof IRFNpc)
        {
        	increase+=((IRFNpc)entity).getAttributeValue(att);
        }
        else if (entity instanceof IEntityBase && entity.getAttributeMap().getAttributeInstance(att) != null) 
        {
            increase += (int)entity.getAttributeMap().getAttributeInstance(att).getAttributeValue();
        }
        
        if (target instanceof IEntityBase && target.getAttributeMap().getAttributeInstance(resAtt) != null) 
        {
            increase -= (int)target.getAttributeMap().getAttributeInstance(resAtt).getAttributeValue();
        }
        else if(target instanceof IRFNpc)
        {
        	increase-=((IRFNpc)target).getAttributeValue(att);
        }
        else if(target instanceof EntityPlayer) 
        {
        	IPlayer cap = target.getCapability(PlayerCapProvider.PlayerCap, null);
        	increase-=cap.getAttributeValue(att);
        }
        return increase;
    }
    
    public static float getPlayerDamageAfterReduction(EntityPlayer player, DamageSource source, float amount) 
    {
        float reduce = 0.0f;
        if (!source.isDamageAbsolute())
        {
        	if(!source.isUnblockable())
        	{
	            if (source.isMagicDamage()) 
	                reduce = getAttributeValue(player, ItemStatAttributes.RFMAGICDEF, null, null);
	            else 
	                reduce = getAttributeValue(player, ItemStatAttributes.RFDEFENCE, null, null);
        	}
            if (player.isPotionActive(MobEffects.RESISTANCE) && source != DamageSource.OUT_OF_WORLD)
            {
                int i = (player.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = amount * (float)j;
                amount = f / 25.0F;
            }
        }
        return Math.max(0.0f, amount - reduce);
    }
    
    public static float elementalReduction(EntityLivingBase entity, DamageSource source, float amount) 
    {
        if (source instanceof CustomDamage && ((CustomDamage)source).getElement() != EnumElement.NONE) 
        {
            EnumElement element = ((CustomDamage)source).getElement();
            int percent = 0;
            switch (element) 
            {
                case DARK:
                    percent = getAttributeValue(entity, ItemStatAttributes.RFRESDARK, null, null);
                    break;
                case EARTH:
                    percent = getAttributeValue(entity, ItemStatAttributes.RFRESEARTH, null, null);
                    break;
                case FIRE:
                    percent = getAttributeValue(entity, ItemStatAttributes.RFRESFIRE, null, null);
                    break;
                case LIGHT:
                    percent = getAttributeValue(entity, ItemStatAttributes.RFRESLIGHT, null, null);
                    break;
                case LOVE:
                    percent = getAttributeValue(entity, ItemStatAttributes.RFRESLOVE, null, null);
                    break;
                case WATER:
                    percent = getAttributeValue(entity, ItemStatAttributes.RFRESWATER, null, null);
                    break;
                case WIND:
                    percent = getAttributeValue(entity, ItemStatAttributes.RFRESWIND, null, null);
                    break;
				case NONE:
					break;
            }
            if (percent < 0) {
                amount *= 1.0f + Math.abs(percent) / 100.0f;
            }
            else if (percent > 100) {
                amount *= -((percent - 100) / 100.0f);
            }
            else {
                amount *= 1.0f - percent / 100.0f;
            }
        }
        return amount;
    }
    
    public static boolean attackEntity(Entity target, CustomDamage source, float amount)
    {
        if (target instanceof EntityLivingBase) {
            knockBack((EntityLivingBase) target, source);
        }
        if (!(source.getTrueSource() instanceof EntityPlayer) && target.world.getDifficulty() == EnumDifficulty.PEACEFUL && target instanceof EntityPlayer) {
            return false;
        }
        spawnElementalParticle(target, source.getElement());
        return target.attackEntityFrom((DamageSource)source, amount);
    }
    
    public static void spawnElementalParticle(Entity target, EnumElement element) 
    {
        if (target.world instanceof WorldServer)
        {
            int color = 0xFFFFFF;
            switch (element) 
            {
                case DARK:
                    color = 0x1B133F;
                    break;
                case EARTH:
                    color = 0x8C680F;
                    break;
                case FIRE:
                    color = 0xC40707;
                    break;
                case LIGHT:
                    color = 0xFFFF47;
                    break;
                case LOVE:
                    color = 0xF783DA;
                    break;
                case WATER:
                    color = 0x2A6FDD;
                    break;
                case WIND:
                    color = 0x21A51A;
                    break;
				default:
					break;
            }
            double r = (color >> 16 & 0xFF) / 255.0;
            double g = (color >> 8 & 0xFF) / 255.0;
            double b = (color >> 0 & 0xFF) / 255.0;
            Random rand = new Random();
            for (int i = 0; i < 7; ++i) {
                ((WorldServer)target.world).spawnParticle(EnumParticleTypes.SPELL_MOB, target.posX + (rand.nextDouble() - 0.5) * target.width, target.posY + 0.3 + rand.nextDouble() * target.height, target.posZ + (rand.nextDouble() - 0.5) * target.width, 0, r, g, b, 1);
            }
        }
    }
    
    public static void knockBack(EntityLivingBase entity, CustomDamage source) 
    {
        Entity attacker = source.getTrueSource();
        float strength = source.knockAmount();
        double xRatio = 0.0;
        double zRatio = 0.0;
        double yRatio = strength;
        if (attacker instanceof EntityLivingBase) 
        {
            switch (source.getKnockBackType()) 
            {
                case BACK:
                    Vec3d distVec = entity.getPositionVector().subtract(attacker.getPositionVector()).normalize();
                    xRatio = -distVec.x;
                    zRatio = -distVec.z;
                    break;
                case VANILLA:
                    xRatio = MathHelper.sin(attacker.rotationYaw * 0.017453292f);
                    zRatio = -MathHelper.cos(attacker.rotationYaw * 0.017453292f);
                    break;
				case UP:
					break;
            }
        }
        //If knockback type is not vanilla, entity will always take knockback
        if (source.getKnockBackType() != CustomDamage.KnockBackType.VANILLA || entity.getRNG().nextDouble() >= entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue()) 
        {
            entity.isAirBorne = true;
            if (xRatio != 0.0 || zRatio != 0.0) 
            {
                float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
                entity.motionX /= 2.0;
                entity.motionZ /= 2.0;
                entity.motionX -= xRatio / f * strength;
                entity.motionZ -= zRatio / f * strength;
            }
            if (source.getKnockBackType() != CustomDamage.KnockBackType.UP) 
            {
                if (entity.onGround) 
                {
                    entity.motionY /= 2.0;
                    entity.motionY += strength;
                    if (entity.motionY > 0.4000000059604645) 
                    {
                        entity.motionY = 0.4000000059604645;
                    }
                }
            }
            else if (yRatio != 0.0) 
            {
                entity.motionY += yRatio;
            }
        }
    }
}