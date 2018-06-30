package com.flemmli97.runecraftory.common.core.handler.capabilities;

import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.client.render.ArmPosePlus;
import com.flemmli97.runecraftory.client.render.EnumToolCharge;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.common.utils.RFCalculations;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

public class PlayerAnim implements IPlayerAnim
{
	private int ticker = 0;
	private int spearUse = 0;
	private int offHandTick;
	private EnumHand prevHand = EnumHand.MAIN_HAND;
	private WeaponSwing weapon;
	private int swings, timeSinceLastSwing;
	private ArmPosePlus armPose = ArmPosePlus.DEFAULT;
    private boolean usingGloves;
    private int gloveTick;
    
	private int spearTicker = 0;
    
    @Override
    public int animationTick() {
        return this.ticker;
    }
    
    @Override
    public void startAnimation(int tick) {
        this.ticker = tick;
    }
    
    @Override
    public boolean startGlove(EntityPlayer player) {
        if (this.usingGloves) {
            return false;
        }
        this.usingGloves = true;
        this.gloveTick = 60;
        return true;
    }
    
    private void updateGlove(EntityPlayer player) {
        --this.gloveTick;
        Vec3d look = player.getLookVec();
        Vec3d move = new Vec3d(look.x, 0.0, look.z).normalize().scale(0.4);
        player.motionX = move.x;
        player.motionZ = move.z;
        IPlayer cap = player.getCapability(CapabilityProvider.PlayerCapProvider.PlayerCap, null);
        for (EntityLivingBase e : player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(1.0))) 
        {
            if (e != player) 
            {
                float damagePhys = cap.getStr();
                cap.decreaseRunePoints(player, 2);
                cap.increaseSkill(EnumSkills.FIST, player, 5);
                damagePhys += RFCalculations.getAttributeValue((EntityLivingBase)player, (IAttribute)ItemStatAttributes.RFATTACK, null, null);
                if (!(e instanceof IEntityBase)) 
                {
                    damagePhys = LevelCalc.scaleForVanilla(damagePhys);
                }
                RFCalculations.playerDamage(player, e, CustomDamage.attack((EntityLivingBase)player, ItemNBT.getElement(player.getHeldItemMainhand()), CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.VANILLA, 1.0f, 20), damagePhys, cap, player.getHeldItemMainhand());
            }
        }
        if (this.gloveTick == 0) 
        {
            this.usingGloves = false;
        }
    }
    
    @Override
    public void update(EntityPlayer player) {
        this.ticker = Math.max(this.ticker--, 0);
        this.timeSinceLastSwing = Math.max(this.timeSinceLastSwing--, 0);
        if (this.timeSinceLastSwing == 0) {
            this.swings = 0;
        }
        if (this.usingGloves) 
        {
            this.updateGlove(player);
        }
        this.spearTicker = Math.max(this.spearTicker--, 0);
        this.offHandTick = Math.max(this.offHandTick--, 0);
        if (player.world.isRemote) {
            ItemStack heldMain = player.getHeldItemMainhand();
            if (heldMain.getItem() instanceof IChargeable) 
            {
                if (player.getItemInUseCount() > 0) 
                {
                    EnumToolCharge action = ((IChargeable)heldMain.getItem()).chargeType(heldMain);
                    switch (action) 
                    {
                        case CHARGECAN: this.armPose = ArmPosePlus.CHARGECAN;
                            break;
                        case CHARGEFISHING: this.armPose = ArmPosePlus.CHARGEFISHING;
                            break;
                        case CHARGEFIST: this.armPose = ArmPosePlus.CHARGEFIST;
                            break;
                        case CHARGELONG: this.armPose = ArmPosePlus.CHARGELONG;
                            break;
                        case CHARGESICKLE: this.armPose = ArmPosePlus.CHARGESICKLE;
                            break;
                        case CHARGESPEAR: this.armPose = ArmPosePlus.CHARGESPEAR;
                            break;
                        case CHARGESWORD: this.armPose = ArmPosePlus.CHARGESWORD;
                            break;
                        case CHARGEUPTOOL: this.armPose = ArmPosePlus.CHARGEUPTOOL;
                            break;
                        case CHARGEUPWEAPON: this.armPose = ArmPosePlus.CHARGEUPWEAPON;
                            break;
						case CHARGESEEDS:
							break;
                    }
                }
                else 
                {
                    this.armPose = ArmPosePlus.DEFAULT;
                }
            }
            else 
            {
                this.armPose = ArmPosePlus.DEFAULT;
            }
        }
    }
    
    @Override
    public boolean canUseSpear() {
        if (this.spearTicker > 0 && this.spearUse++ < 20) {
            return true;
        }
        this.spearUse = 0;
        this.spearTicker = 0;
        return false;
    }
    
    @Override
    public void startSpear() {
        this.spearTicker = 60;
    }
    
    @Override
    public int getSpearTick() {
        return this.spearTicker;
    }
    
    @Override
    public void disableOffHand() {
        this.offHandTick = 100;
    }
    
    @Override
    public boolean canUseOffHand() {
        return this.offHandTick == 0;
    }
    
    @Override
    public EnumHand getPrevSwung() {
        return this.prevHand;
    }
    
    @Override
    public void setPrevSwung(EnumHand hand) {
        this.prevHand = hand;
    }
    
    @Override
    public void startWeaponSwing(WeaponSwing swing, int delay) {
        if (this.weapon != swing) {
            this.swings = 0;
        }
        ++this.swings;
        this.timeSinceLastSwing = delay;
        this.weapon = swing;
    }
    
    @Override
    public boolean isAtUltimate() {
        return this.weapon.getMaxSwing() == this.swings;
    }
    
    @Override
    public ArmPosePlus currentArmPose() {
        return this.armPose;
    }
    
    @Override
    public void setArmPose(ArmPosePlus armPose) {
        this.armPose = armPose;
    }
}
