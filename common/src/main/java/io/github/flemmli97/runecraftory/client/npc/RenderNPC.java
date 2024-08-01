package io.github.flemmli97.runecraftory.client.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.features.BlushFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.HairFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.IndexedColorSettingType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeatureContainer;
import io.github.flemmli97.runecraftory.common.entities.npc.features.OutfitFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SimpleHatFeatureType;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderNPC<T extends EntityNPCBase> extends MobRenderer<T, PlayerModel<T>> {

    private static final Map<String, PlayerSkin> PLAYER_SKIN_TEXTURE_LOCATIONS = new HashMap<>();
    private static final Map<String, ResourceLocation> TEXTURE_LAYERS_LOCATIONS = new HashMap<>();
    public static final ResourceLocation EMPTY = new ResourceLocation(RuneCraftory.MODID, "textures/entity/npc/empty.png");

    public final NPCArmorLayer<T, PlayerModel<T>, HumanoidModel<T>> armorLayer, armorLayerSlim;
    public final List<NPCTextureLayer<T, PlayerModel<T>, PlayerModel<T>>> textureLayers = new ArrayList<>();

    public RenderNPC(EntityRendererProvider.Context ctx) {
        super(ctx, new PlayerModel<>(ctx.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
        this.addLayer(this.armorLayer = new NPCArmorLayer<>(this, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
        this.addLayer(this.armorLayerSlim = new NPCArmorLayer<>(this, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)), new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR))));
        this.addLayer(new ItemInHandLayer<>(this));
        for (NPCTextureLayer.LayerType layerType : NPCTextureLayer.LayerType.values()) {
            if (layerType == NPCTextureLayer.LayerType.SKIN_LAYER)
                this.textureLayers.add(new NPCTextureLayer<>(this, this.model, new PlayerModel<>(ctx.bakeLayer(ModelLayers.PLAYER_SLIM), true), layerType));
            else
                this.textureLayers.add(new NPCTextureLayer<>(this, new PlayerModel<>(ctx.bakeLayer(layerType.location), false), new PlayerModel<>(ctx.bakeLayer(layerType.slimeLocation), true), layerType));
        }
        this.textureLayers.forEach(this::addLayer);
    }

    public static boolean isSlim(EntityNPCBase npc) {
        String skin = npc.getLook().playerSkin();
        if (skin != null) {
            String skinMeta = PLAYER_SKIN_TEXTURE_LOCATIONS.computeIfAbsent(skin, s -> new PlayerSkin(skin)).getSkinMeta();
            return skinMeta.equals("slim");
        }
        return npc.lookFeatures.view.containsKey(ModNPCLooks.SLIM.get());
    }

    public static ResourceLocation getTextureFromLook(EntityNPCBase npc, NPCTextureLayer.LayerType type) {
        NPCData.NPCLook look = npc.getLook();
        if (type == NPCTextureLayer.LayerType.SKIN_LAYER) {
            String skin = look.playerSkin();
            if (skin != null) {
                return PLAYER_SKIN_TEXTURE_LOCATIONS.computeIfAbsent(skin, s -> new PlayerSkin(skin)).getLocation();
            }
        } else if (look.playerSkin() != null) {
            // Ignore other layers if using a player skin
            return EMPTY;
        }
        boolean slim = isSlim(npc);
        if (type == NPCTextureLayer.LayerType.HAT_LAYER && npc.hasItemInSlot(EquipmentSlot.HEAD))
            return EMPTY;
        return getTextureFromLook(npc.lookFeatures, slim, type);
    }

    public static ResourceLocation getTextureFromLook(NPCFeatureContainer features, boolean slim, NPCTextureLayer.LayerType type) {
        ResourceLocation texture = switch (type) {
            case SKIN_LAYER -> {
                IndexedColorSettingType.IndexedColorFeature feat = features.getFeature(ModNPCLooks.SKIN.get());
                int num = 0;
                if (feat != null)
                    num = feat.index;
                String location = String.format("textures/entity/npc/skin/%s%s.png", slim ? "slim_" : "", num);
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RenderNPC::modLoc);
            }
            case IRIS_LAYER -> {
                IndexedColorSettingType.IndexedColorFeature feat = features.getFeature(ModNPCLooks.IRIS.get());
                int num = 0;
                if (feat != null)
                    num = feat.index;
                String location = String.format("textures/entity/npc/eye/iris_%s.png", num);
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RenderNPC::modLoc);
            }
            case SCLERA_LAYER -> {
                IndexedColorSettingType.IndexedColorFeature feat = features.getFeature(ModNPCLooks.SCLERA.get());
                int num = 0;
                if (feat != null)
                    num = feat.index;
                String location = String.format("textures/entity/npc/eye/sclera_%s.png", num);
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RenderNPC::modLoc);
            }
            case EYEBROWS_LAYER -> {
                IndexedColorSettingType.IndexedColorFeature feat = features.getFeature(ModNPCLooks.EYEBROWS.get());
                if (feat == null || feat.index == 0)
                    yield null;
                int num = feat.index;
                String location = String.format("textures/entity/npc/eye/eyebrows_%s.png", num);
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RenderNPC::modLoc);
            }
            case BLUSH_LAYER -> {
                BlushFeatureType.BlushFeature feat = features.getFeature(ModNPCLooks.BLUSH.get());
                if (feat == null || !feat.blush)
                    yield null;
                String location = "textures/entity/npc/misc/blush.png";
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RenderNPC::modLoc);
            }
            case OUTFIT_LAYER -> {
                OutfitFeatureType.OutfitFeature feat = features.getFeature(ModNPCLooks.OUTFIT.get());
                String location = String.format("textures/entity/npc/outfit/generic%s_0.png", slim ? "_slim" : "");
                if (feat != null)
                    location = String.format("textures/entity/npc/outfit/%s%s_%s.png", feat.type, slim ? "_slim" : "", feat.index);
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RenderNPC::modLoc);
            }
            case HAIR_LAYER -> {
                HairFeatureType.HairFeature feat = features.getFeature(ModNPCLooks.HAIR.get());
                if (feat == null)
                    yield null;
                String location = String.format("textures/entity/npc/hair/%s_%s.png", feat.type, feat.index);
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RenderNPC::modLoc);
            }
            case HAT_LAYER -> {
                SimpleHatFeatureType.SimpleHatFeature feat = features.getFeature(ModNPCLooks.HAT.get());
                if (feat == null || feat.hat.isEmpty())
                    yield null;
                String location = String.format("textures/entity/npc/misc/%s.png", feat.hat);
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RenderNPC::modLoc);
            }
        };
        return texture == null ? EMPTY : texture;
    }

    private static ResourceLocation modLoc(String s) {
        return new ResourceLocation(RuneCraftory.MODID, s);
    }

    public static boolean renderForTooltip(PoseStack stack, int x, int y, @Nullable String skin, List<Pair<Integer, ResourceLocation>> textures) {
        if (skin == null && textures == null)
            return false;
        int sizeX = 16;
        int sizeY = 16;
        if (skin != null) {
            ResourceLocation res = PLAYER_SKIN_TEXTURE_LOCATIONS.computeIfAbsent(skin, s -> new PlayerSkin(skin)).getLocation();
            RenderSystem.setShaderTexture(0, res);
            GuiComponent.blit(stack, x, y, sizeX, sizeY, 8.0f, 8, 8, 8, 64, 64);
            RenderSystem.enableBlend();
            GuiComponent.blit(stack, x, y, sizeX, sizeY, 40.0F, 8, 8, 8, 64, 64);
            RenderSystem.disableBlend();
        } else {
            for (Pair<Integer, ResourceLocation> layer : textures) {
                int color = layer.getFirst();
                float a = (float) (color >> 24 & 0xFF) / 255.0f;
                float r = (float) (color >> 16 & 0xFF) / 255.0f;
                float g = (float) (color >> 8 & 0xFF) / 255.0f;
                float b = (float) (color & 0xFF) / 255.0f;
                RenderSystem.setShaderColor(r, g, b, a);
                RenderSystem.setShaderTexture(0, layer.getSecond());
                GuiComponent.blit(stack, x, y, sizeX, sizeY, 8.0f, 8, 8, 8, 64, 64);
                RenderSystem.enableBlend();
                GuiComponent.blit(stack, x, y, sizeX, sizeY, 40.0F, 8, 8, 8, 64, 64);
                RenderSystem.disableBlend();
            }
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
        return true;
    }

    @Override
    protected boolean shouldShowName(T entity) {
        return false;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        boolean slim = isSlim(entity);
        this.armorLayer.setRender(!slim);
        this.armorLayerSlim.setRender(slim);
        for (NPCFeature feature : entity.lookFeatures.view.values()) {
            NPCFeatureRenderers.get(feature).onSetup(feature, this, entity, stack);
        }
        this.setModelProperties(entity);
        super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
        for (NPCFeature feature : entity.lookFeatures.view.values()) {
            NPCFeatureRenderers.get(feature).render(feature, entity, entityYaw, partialTicks, stack, buffer, packedLight);
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
        return getTextureFromLook(entity, NPCTextureLayer.LayerType.SKIN_LAYER);
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
        for (NPCFeature feature : entity.lookFeatures.view.values()) {
            NPCFeatureRenderers.get(feature).transformStack(feature, this, entity, stack, partialTicks);
        }
    }

    @Override
    protected void scale(T livingEntity, PoseStack matrixStack, float partialTickTime) {
        matrixStack.scale(0.9375f, 0.9375f, 0.9375f);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(T entity, boolean invis, boolean translucent, boolean glowing) {
        // Rendered by the layers since otherwise we can't color it
        return null;
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
