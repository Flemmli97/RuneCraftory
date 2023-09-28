package io.github.flemmli97.runecraftory.client.render;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
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

import java.util.HashMap;
import java.util.Map;

public class RenderNPC<T extends EntityNPCBase> extends MobRenderer<T, PlayerModel<T>> {

    private final Map<String, PlayerSkin> textureLocations = new HashMap<>();

    private final PlayerModel<T> def;
    private final PlayerModel<T> slim;

    public RenderNPC(EntityRendererProvider.Context ctx) {
        super(ctx, new PlayerModel<>(ctx.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
        this.def = this.model;
        this.slim = new PlayerModel<>(ctx.bakeLayer(ModelLayers.PLAYER_SLIM), true);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        String skin = entity.getLook().playerSkin();
        if (skin != null) {
            String skinMeta = this.textureLocations.computeIfAbsent(skin, s -> new PlayerSkin(skin)).getSkinMeta();
            if (skinMeta.equals("slim"))
                this.model = this.slim;
            else
                this.model = this.def;
        } else
            this.model = this.def;
        this.setModelProperties(entity);
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
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
        String skin = entity.getLook().playerSkin();
        if (skin != null) {
            return this.textureLocations.computeIfAbsent(skin, s -> new PlayerSkin(skin)).getLocation();
        }
        return entity.getLook().texture();
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
