package com.flemmli97.runecraftory.client.render;

import org.apache.logging.log4j.LogManager;

import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.client.models.ModelPlayerImproved;
import com.flemmli97.runecraftory.common.items.weapons.DualBladeBase;
import com.flemmli97.runecraftory.common.items.weapons.GloveBase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPlayerNew extends RenderPlayer
{
	private static RenderPlayerNew instanceSlim;
	private static RenderPlayerNew instance;

	public RenderPlayerNew(RenderManager renderManager, boolean useSmallArms) {
		super(renderManager, useSmallArms);
		this.mainModel=new ModelPlayerImproved(0, useSmallArms);
		this.layerRenderers.clear();
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerDeadmau5Head(this));
        this.addLayer(new LayerCape(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
        this.addLayer(new LayerElytra(this));
        this.addLayer(new LayerEntityOnShoulder(renderManager));
	}
	
	public static RenderPlayerNew instance(AbstractClientPlayer player)
	{
		if(player.getSkinType().equals("slim"))
		{
			if(instanceSlim==null)
				instanceSlim=new RenderPlayerNew(Minecraft.getMinecraft().getRenderManager(), true);
			return instanceSlim;
		}
		else
		{
			if(instance==null)
				instance=new RenderPlayerNew(Minecraft.getMinecraft().getRenderManager(), false);
			return instance;
		}
	}
	
    public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        //if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderPlayerEvent.Pre(entity, this, partialTicks, x, y, z))) return;
        if (!entity.isUser() || this.renderManager.renderViewEntity == entity)
        {
            double d0 = y;

            if (entity.isSneaking())
            {
                d0 = y - 0.125D;
            }

            this.setModelVisibilities(entity);
            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
            this.doRenderLivingBase(entity, x, d0, z, entityYaw, partialTicks);
            GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        }
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderPlayerEvent.Post(entity, this, partialTicks, x, y, z));
    }
	
    private void doRenderLivingBase(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<AbstractClientPlayer>(entity, this, partialTicks, x, y, z))) return;
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
        boolean shouldSit = entity.isRiding() && (entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
        this.mainModel.isRiding = shouldSit;
        this.mainModel.isChild = entity.isChild();

        try
        {
            float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
            float f1 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
            float f2 = f1 - f;

            if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase)
            {
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity.getRidingEntity();
                f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                f2 = f1 - f;
                float f3 = MathHelper.wrapDegrees(f2);

                if (f3 < -85.0F)
                {
                    f3 = -85.0F;
                }

                if (f3 >= 85.0F)
                {
                    f3 = 85.0F;
                }

                f = f1 - f3;

                if (f3 * f3 > 2500.0F)
                {
                    f += f3 * 0.2F;
                }

                f2 = f1 - f;
            }

            float f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            this.renderLivingAt(entity, x, y, z);
            float f8 = this.handleRotationFloat(entity, partialTicks);
            this.applyRotations(entity, f8, f, partialTicks);
            float f4 = this.prepareScale(entity, partialTicks);
            float f5 = 0.0F;
            float f6 = 0.0F;

            if (!entity.isRiding())
            {
                f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);

                if (entity.isChild())
                {
                    f6 *= 3.0F;
                }

                if (f5 > 1.0F)
                {
                    f5 = 1.0F;
                }
                f2 = f1 - f; // Forge: Fix MC-1207
            }

            GlStateManager.enableAlpha();
            this.mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
            this.mainModel.setRotationAngles(f6, f5, f8, f2, f7, f4, entity);

            if (this.renderOutlines)
            {
                boolean flag1 = this.setScoreTeamColor(entity);
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(this.getTeamColor(entity));

                if (!this.renderMarker)
                {
                    this.renderModel(entity, f6, f5, f8, f2, f7, f4);
                }

                if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator())
                {
                    this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
                }

                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();

                if (flag1)
                {
                    this.unsetScoreTeamColor();
                }
            }
            else
            {
                boolean flag = this.setDoRenderBrightness(entity, partialTicks);
                this.renderModel(entity, f6, f5, f8, f2, f7, f4);

                if (flag)
                {
                    this.unsetBrightness();
                }

                GlStateManager.depthMask(true);

                if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator())
                {
                    this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
                }
            }

            GlStateManager.disableRescaleNormal();
        }
        catch (Exception exception)
        {
        	LogManager.getLogger().error("Couldn't render entity", (Throwable)exception);
        }

        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        if (!this.renderOutlines)
        {
            this.renderName(entity, x, y, z);
        }
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<AbstractClientPlayer>(entity, this, partialTicks, x, y, z));
    }
    
    private void setModelVisibilities(AbstractClientPlayer clientPlayer)
    {
        ModelPlayerImproved modelplayer = (ModelPlayerImproved) this.mainModel;

        if (clientPlayer.isSpectator())
        {
            modelplayer.setVisible(false);
            modelplayer.bipedHead.showModel = true;
            modelplayer.bipedHeadwear.showModel = true;
        }
        else
        {
            ItemStack itemstack = clientPlayer.getHeldItemMainhand();
            ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
            modelplayer.setVisible(true);
            modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
            modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
            modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
            modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
            modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
            modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
            modelplayer.isSneak = clientPlayer.isSneaking();
            ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
            ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;
            modelplayer.armPose=ArmPosePlus.DEFAULT;
            if (!itemstack.isEmpty())
            {
                modelbiped$armpose = ModelBiped.ArmPose.ITEM;

                if (clientPlayer.getItemInUseCount() > 0)
                {
            		if(itemstack.getItem() instanceof IChargeable)
            		{
            	        if (itemstack.getItem() instanceof DualBladeBase || itemstack.getItem() instanceof GloveBase)
            	        {
            	        	modelbiped$armpose1=ModelBiped.ArmPose.ITEM;
            	        }
            	        EnumToolCharge action = ((IChargeable)itemstack.getItem()).chargeType(itemstack);
            	        switch(action)
    	                {
    						case CHARGECAN:modelplayer.armPose=ArmPosePlus.CHARGECAN;
    							break;
    						case CHARGEFISHING:modelplayer.armPose=ArmPosePlus.CHARGEFISHING;
    							break;
    						case CHARGEFIST:modelplayer.armPose=ArmPosePlus.CHARGEFIST;
    							break;
    						case CHARGELONG:modelplayer.armPose=ArmPosePlus.CHARGELONG;
    							break;
    						case CHARGESICKLE:modelplayer.armPose=ArmPosePlus.CHARGESICKLE;
    							break;
    						case CHARGESPEAR:modelplayer.armPose=ArmPosePlus.CHARGESPEAR;
    							break;
    						case CHARGESWORD:modelplayer.armPose=ArmPosePlus.CHARGESWORD;
    							break;
    						case CHARGEUPTOOL:modelplayer.armPose=ArmPosePlus.CHARGEUPTOOL;
    							break;
    						case CHARGEUPWEAPON:modelplayer.armPose=ArmPosePlus.CHARGEUPWEAPON;
    							break;
							case CHARGESEEDS:
								break;
    	                }
            		}
                	else
                	{
	                    EnumAction enumaction = itemstack.getItemUseAction();
	
	                    if (enumaction == EnumAction.BLOCK)
	                    {
	                        modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
	                    }
	                    else if (enumaction == EnumAction.BOW)
	                    {
	                        modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
	                    }
                	}
                }
            }

            if (!itemstack1.isEmpty())
            {
                modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

                if (clientPlayer.getItemInUseCount() > 0)
                {
                    EnumAction enumaction1 = itemstack1.getItemUseAction();

                    if (enumaction1 == EnumAction.BLOCK)
                    {
                        modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
                    }
                    else if (enumaction1 == EnumAction.BOW)
                    {
                        modelbiped$armpose1 = ModelBiped.ArmPose.BOW_AND_ARROW;
                    }
                }
            }

            if (clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT)
            {
                modelplayer.rightArmPose = modelbiped$armpose;
                modelplayer.leftArmPose = modelbiped$armpose1;
            }
            else
            {
                modelplayer.rightArmPose = modelbiped$armpose1;
                modelplayer.leftArmPose = modelbiped$armpose;
            }
        }
    }
    
}