package io.github.flemmli97.runecraftory.client.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RenderNPC<T extends EntityNPCBase> extends MobRenderer<T, PlayerModel<T>> {

    private static final Map<String, PlayerSkin> TEXTURE_LOCATIONS = new HashMap<>();
    private static final Object2BooleanMap<ResourceLocation> MISSING = new Object2BooleanOpenHashMap<>();

    private final PlayerModel<T> def;
    public final PlayerModel<T> slim;

    public RenderNPC(EntityRendererProvider.Context ctx) {
        super(ctx, new PlayerModel<>(ctx.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
        this.def = this.model;
        this.slim = new PlayerModel<>(ctx.bakeLayer(ModelLayers.PLAYER_SLIM), true);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
        this.addLayer(new ItemInHandLayer<>(this));
    }

    public static ResourceLocation getTextureFromLook(NPCData.NPCLook look) {
        String skin = look.playerSkin();
        if (skin != null) {
            return TEXTURE_LOCATIONS.computeIfAbsent(skin, s -> new PlayerSkin(skin)).getLocation();
        }
        if (MISSING.computeIfAbsent(look.texture(), s -> {
            if (look.texture() == null)
                return true;
            try (AbstractTexture text = new SimpleTexture(look.texture())) {
                text.load(Minecraft.getInstance().getResourceManager());
                return false;
            } catch (IOException exception) {
                RuneCraftory.LOGGER.warn("Failed to load texture: {}", look.texture(), exception);
            }
            return true;
        }))
            return NPCData.NPCLook.DEFAULT_SKIN;
        return look.texture();
    }

    public static boolean renderForTooltip(int x, int y, float scale, PlayerModel<?> model, String skin, ResourceLocation texture) {
        if (skin == null && texture == null)
            return false;
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.translate(x, y, 1950.0);
        poseStack.scale(1.0f, 1.0f, -1.0f);
        RenderSystem.applyModelViewMatrix();
        PoseStack poseStack2 = new PoseStack();
        poseStack2.translate(0.0, 0.0, 1000.0);
        poseStack2.scale(scale, scale, scale);
        Lighting.setupForEntityInInventory();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderType renderType = model.renderType(texture != null ? texture : TEXTURE_LOCATIONS.computeIfAbsent(skin, s -> new PlayerSkin(skin)).getLocation());
        model.head.render(poseStack2, bufferSource.getBuffer(renderType), 0xff00ff, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        model.hat.render(poseStack2, bufferSource.getBuffer(renderType), 0xff00ff, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        bufferSource.endBatch();
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
        return true;
    }

    @Override
    protected boolean shouldShowName(T entity) {
        return false;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.def;
        String skin = entity.getLook().playerSkin();
        if (skin != null) {
            String skinMeta = TEXTURE_LOCATIONS.computeIfAbsent(skin, s -> new PlayerSkin(skin)).getSkinMeta();
            if (skinMeta.equals("slim"))
                this.model = this.slim;
            else
                this.model = this.def;
        }
        for (NPCFeature feature : entity.getLook().additionalFeatures()) {
            NPCFeatureRenderers.get(feature).onSetup(feature, this, entity);
        }
        this.setModelProperties(entity);
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        for (NPCFeature feature : entity.getLook().additionalFeatures()) {
            NPCFeatureRenderers.get(feature).onSetup(feature, this, entity);
        }
    }

    private void setModelProperties(EntityNPCBase npc) {
        PlayerModel<T> playerModel = this.getModel();
        playerModel.setAllVisible(true);
        playerModel.crouching = npc.isCrouching();
        HumanoidModel.ArmPose main = getArmPose(npc, InteractionHand.MAIN_HAND);
        HumanoidModel.ArmPose off = getArmPose(npc, InteractionHand.OFF_HAND);
        if (npc.getMainArm() == HumanoidArm.RIGHT) {
            playerModel.rightArmPose = main;
            playerModel.leftArmPose = off;
        } else {
            playerModel.rightArmPose = off;
            playerModel.leftArmPose = main;
        }
    }

    private static HumanoidModel.ArmPose getArmPose(EntityNPCBase npc, InteractionHand hand) {
        ItemStack itemStack = npc.getItemInHand(hand);
        if (itemStack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        }
        if (npc.getUsedItemHand() == hand && npc.getUseItemRemainingTicks() > 0) {
            UseAnim useAnim = itemStack.getUseAnimation();
            if (useAnim == UseAnim.BLOCK) {
                return HumanoidModel.ArmPose.BLOCK;
            }
            if (useAnim == UseAnim.BOW) {
                return HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
            if (useAnim == UseAnim.SPEAR) {
                return HumanoidModel.ArmPose.THROW_SPEAR;
            }
            if (useAnim == UseAnim.CROSSBOW && hand == npc.getUsedItemHand()) {
                return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
            }
            if (useAnim == UseAnim.SPYGLASS) {
                return HumanoidModel.ArmPose.SPYGLASS;
            }
        } else if (!npc.swinging && itemStack.is(Items.CROSSBOW) && CrossbowItem.isCharged(itemStack)) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        }
        return HumanoidModel.ArmPose.ITEM;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return getTextureFromLook(entity.getLook());
    }

    @Override
    protected void setupRotations(T entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entity, stack, ageInTicks, rotationYaw, partialTicks);
        if (entity.getPlayDeathTick() > 0) {
            float partial = partialTicks - 1;
            float f = (entity.getPlayDeathTick() + (entity.playDeath() ? partial : -partial)) / 20.0f * 1.6f;
            if ((f = Mth.sqrt(f)) > 1.0f) {
                f = 1.0f;
            }
            stack.translate(0, f * 0.1, -f * entity.getBbHeight() * 0.5);
            stack.mulPose(Vector3f.XP.rotationDegrees(f * this.getFlipDegrees(entity)));
        }
    }

    @Override
    protected void scale(T livingEntity, PoseStack matrixStack, float partialTickTime) {
        matrixStack.scale(0.9375f, 0.9375f, 0.9375f);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(T entity, boolean invis, boolean translucent, boolean glowing) {
        if (entity.getPlayDeathTick() > 0 && !entity.playDeath()) {
            if (entity.getPlayDeathTick() > 8) {
                if (entity.getPlayDeathTick() % 2 == 0)
                    return null;
            }
            if (entity.getPlayDeathTick() % 3 == 0)
                return null;
        }
        return super.getRenderType(entity, invis, translucent, glowing);
    }

    public void setModel(PlayerModel<T> model) {
        this.model = model;
    }

    static class PlayerSkin {

        private GameProfile gameProfile;

        private ResourceLocation location = DefaultPlayerSkin.getDefaultSkin();
        private String skinMeta = "";

        private boolean pendingTextures;

        public PlayerSkin(String name) {
            SkullBlockEntity.updateGameprofile(new GameProfile(null, name), prof -> this.gameProfile = prof);
        }

        public ResourceLocation getLocation() {
            this.registerTextures();
            return this.location;
        }

        public String getSkinMeta() {
            this.registerTextures();
            return this.skinMeta;
        }

        protected void registerTextures() {
            synchronized (this) {
                if (!this.pendingTextures && this.gameProfile != null) {
                    this.pendingTextures = true;
                    Minecraft.getInstance().getSkinManager().registerSkins(this.gameProfile, (type, resourceLocation, minecraftProfileTexture) -> {
                        if (type == MinecraftProfileTexture.Type.SKIN) {
                            String metadata = minecraftProfileTexture.getMetadata("model");
                            if (metadata == null) {
                                metadata = "default";
                            }
                            this.location = resourceLocation;
                            this.skinMeta = metadata;
                        }
                    }, true);
                }
            }
        }
    }
}
