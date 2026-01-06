package io.github.flemmli97.runecraftory.client.render.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCLook;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.client.model.HumanoidBasedModel;
import io.github.flemmli97.runecraftory.client.model.HumanoidModelLocations;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.features.BlushFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.FaceFeaturesType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.HairFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.IndexedColorSettingType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.ModelFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeatureContainer;
import io.github.flemmli97.runecraftory.common.entities.npc.features.OutfitFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SimpleHatFeatureType;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.github.flemmli97.tenshilib.client.render.layer.ItemLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
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

public class NPCRender<T extends NPCEntity> extends MobRenderer<T, HumanoidBasedModel<T>> {

    private static final Map<String, PlayerSkinData> PLAYER_SKIN_TEXTURE_LOCATIONS = new HashMap<>();
    private static final Map<String, ResourceLocation> TEXTURE_LAYERS_LOCATIONS = new HashMap<>();
    public static final ResourceLocation EMPTY = RuneCraftory.modRes("textures/entity/npc/empty.png");

    public final NPCArmorLayer<T> armorLayer;
    public final List<NPCTextureLayer<T>> textureLayers = new ArrayList<>();

    private final HumanoidModel<T> internalHumanoid;
    private final Map<Pair<ResourceLocation, ResourceLocation>, NPCModelHolder> models = new HashMap<>();
    private NPCModelHolder current;

    public NPCRender(EntityRendererProvider.Context ctx) {
        super(ctx, new HumanoidBasedModel<>(), 0.5f);
        this.internalHumanoid = new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER));
        this.addLayer(this.armorLayer = new NPCArmorLayer<>(this, new RenderLayerParent<>() {
            @Override
            public HumanoidModel<T> getModel() {
                return NPCRender.this.internalHumanoid;
            }

            @Override
            public ResourceLocation getTextureLocation(T entity) {
                return NPCRender.this.getTextureLocation(entity);
            }
        }, ctx));
        this.addLayer(new ItemLayer<>(this, ctx.getItemInHandRenderer()));
        this.current = this.getModelHolder(HumanoidModelLocations.DEFAULT_LOCATION);
        this.getModelHolder(HumanoidModelLocations.DEFAULT_LOCATION_SLIM);
        for (NPCTextureLayer.LayerType layerType : NPCTextureLayer.LayerType.values()) {
            if (layerType.modelType == null)
                continue;
            if (layerType == NPCTextureLayer.LayerType.IRIS_LAYER)
                this.textureLayers.add(new NPCFaceLayer<>(this));
            else
                this.textureLayers.add(new NPCTextureLayer<>(this, layerType));
        }
        this.textureLayers.forEach(this::addLayer);
        this.addLayer(new NPCFeatureRenderLayer<>(this));
    }

    public static boolean isSlim(NPCEntity npc) {
        NPCLook look = npc.getLook().value();
        if (look == NPCLook.DEFAULT.value()) {
            return DefaultPlayerSkin.get(npc.getUUID())
                    .model() == PlayerSkin.Model.SLIM;
        }
        String skin = look.playerSkin();
        if (skin != null) {
            PlayerSkin.Model skinMeta = PLAYER_SKIN_TEXTURE_LOCATIONS.computeIfAbsent(skin, s -> new PlayerSkinData(skin)).getSkinMeta();
            return skinMeta == PlayerSkin.Model.SLIM;
        }
        return npc.lookFeatures.contains(RuneCraftoryNPCLooks.SLIM.get());
    }

    public static ResourceLocation getTextureFromLook(NPCEntity npc, NPCTextureLayer.LayerType type, @Nullable String subType) {
        NPCLook look = npc.getLook().value();
        if (type == NPCTextureLayer.LayerType.SKIN_LAYER) {
            if (look == NPCLook.DEFAULT.value()) {
                return DefaultPlayerSkin.get(npc.getUUID())
                        .texture();
            }
            String skin = look.playerSkin();
            if (skin != null) {
                return PLAYER_SKIN_TEXTURE_LOCATIONS.computeIfAbsent(skin, s -> new PlayerSkinData(skin)).getLocation();
            }
        } else if (look.playerSkin() != null || look == NPCLook.DEFAULT.value()) {
            // Ignore other layers if using a player skin
            return EMPTY;
        }
        boolean slim = isSlim(npc);
        if (type == NPCTextureLayer.LayerType.HAT_LAYER && npc.hasItemInSlot(EquipmentSlot.HEAD))
            return EMPTY;
        return getTextureFromLook(npc.lookFeatures, slim, type, subType);
    }

    public static ResourceLocation getTextureFromLook(NPCFeatureContainer features, boolean slim, NPCTextureLayer.LayerType type, @Nullable String subType) {
        ModelFeatureType.ModelFeature modelFeature = features.getFeature(RuneCraftoryNPCLooks.MODEL.get());
        String prefix = null;
        if (modelFeature != null) {
            prefix = modelFeature.model().map(ModelFeatureType.ModelData::layerPrefix).orElse(null);
            ResourceLocation texture = modelFeature.model().flatMap(ModelFeatureType.ModelData::texture).orElse(null);
            if (texture != null) {
                if (type != NPCTextureLayer.LayerType.SKIN_LAYER) {
                    return EMPTY;
                }
                return TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(texture.toString(), res -> texture.withPath(s -> "textures/" + s + ".png"));
            }
        }
        ResourceLocation texture = switch (type) {
            case SKIN_LAYER -> {
                IndexedColorSettingType.IndexedColorFeature feat = features.getFeature(RuneCraftoryNPCLooks.SKIN.get());
                int num = 0;
                if (feat != null)
                    num = feat.index();
                String location = formatTextureTemplate("textures/entity/npc/skin/", prefix, slim ? "slim" : "", num);
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RuneCraftory::modRes);
            }
            case IRIS_LAYER -> {
                FaceFeaturesType.FaceFeatures feat = features.getFeature(RuneCraftoryNPCLooks.FACE.get());
                int num = 0;
                if (feat != null) {
                    num = feat.iris().index();
                    if (subType != null) {
                        subType = feat.expressionTexture(features, subType, FaceFeaturesType.ExpressionType.IRIS);
                    }
                }
                String location = formatTextureTemplate("textures/entity/npc/eye/iris_", prefix, num, subType);
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RuneCraftory::modRes);
            }
            case SCLERA_LAYER -> {
                FaceFeaturesType.FaceFeatures feat = features.getFeature(RuneCraftoryNPCLooks.FACE.get());
                int num = 0;
                if (feat != null) {
                    num = feat.sclera().index();
                    if (subType != null) {
                        subType = feat.expressionTexture(features, subType, FaceFeaturesType.ExpressionType.SCLERA);
                    }
                }
                String location = formatTextureTemplate("textures/entity/npc/eye/sclera_", prefix, num, subType);
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RuneCraftory::modRes);
            }
            case EYEBROWS_LAYER -> {
                FaceFeaturesType.FaceFeatures feat = features.getFeature(RuneCraftoryNPCLooks.FACE.get());
                int num = 0;
                if (feat != null) {
                    num = feat.eyebrow().index();
                    if (subType != null) {
                        subType = feat.expressionTexture(features, subType, FaceFeaturesType.ExpressionType.EYEBROWS);
                    }
                }
                String location = formatTextureTemplate("textures/entity/npc/eye/eyebrows_", prefix, num, subType);
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RuneCraftory::modRes);
            }
            case BLUSH_LAYER -> {
                BlushFeatureType.BlushFeature feat = features.getFeature(RuneCraftoryNPCLooks.BLUSH.get());
                if (feat == null || !feat.blush())
                    yield null;
                String location = formatTextureTemplate("textures/entity/npc/misc/blush", prefix);
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RuneCraftory::modRes);
            }
            case OUTFIT_LAYER -> {
                OutfitFeatureType.OutfitFeature feat = features.getFeature(RuneCraftoryNPCLooks.OUTFIT.get());
                String location = String.format("textures/entity/npc/outfit/generic%s_0.png", slim ? "_slim" : "");
                if (feat != null)
                    location = formatTextureTemplate("textures/entity/npc/outfit/", feat.outfit(), slim ? "slim" : "", feat.index());
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RuneCraftory::modRes);
            }
            case HAIR_LAYER -> {
                HairFeatureType.HairFeature feat = features.getFeature(RuneCraftoryNPCLooks.HAIR.get());
                if (feat == null)
                    yield null;
                String location = formatTextureTemplate("textures/entity/npc/hair/", prefix, feat.hair(), feat.index());
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RuneCraftory::modRes);
            }
            case HAT_LAYER -> {
                SimpleHatFeatureType.SimpleHatFeature feat = features.getFeature(RuneCraftoryNPCLooks.HAT.get());
                if (feat == null || feat.hat().isEmpty())
                    yield null;
                String location = formatTextureTemplate("textures/entity/npc/misc/", feat.hat());
                yield TEXTURE_LAYERS_LOCATIONS.computeIfAbsent(location, RuneCraftory::modRes);
            }
        };
        return texture == null ? EMPTY : texture;
    }

    private static String formatTextureTemplate(String format, Object... args) {
        StringBuilder formatBuilder = new StringBuilder(format);
        List<Object> formatArgs = new ArrayList<>();
        int idx = 0;
        for (Object arg : args) {
            if (arg == null) {
                continue;
            }
            String argRep = arg.toString();
            if (argRep.isEmpty()) {
                continue;
            }
            formatBuilder.append("%s");
            formatArgs.add((idx != 0 ? "_" : "") + argRep);
            idx++;
        }
        return String.format(formatBuilder.append(".png").toString(), formatArgs.toArray());
    }

    public static boolean renderForTooltip(GuiGraphics graphics, int x, int y, @Nullable String skin, List<Pair<Integer, ResourceLocation>> textures) {
        if (skin == null && textures == null)
            return false;
        int sizeX = 16;
        int sizeY = 16;
        if (skin != null) {
            ResourceLocation res = PLAYER_SKIN_TEXTURE_LOCATIONS.computeIfAbsent(skin, s -> new PlayerSkinData(skin)).getLocation();
            graphics.blit(res, x, y, sizeX, sizeY, 8.0f, 8, 8, 8, 64, 64);
            RenderSystem.enableBlend();
            graphics.blit(res, x, y, sizeX, sizeY, 40.0F, 8, 8, 8, 64, 64);
            RenderSystem.disableBlend();
        } else {
            for (Pair<Integer, ResourceLocation> layer : textures) {
                int color = layer.getFirst();
                float a = (float) (color >> 24 & 0xFF) / 255.0f;
                float r = (float) (color >> 16 & 0xFF) / 255.0f;
                float g = (float) (color >> 8 & 0xFF) / 255.0f;
                float b = (float) (color & 0xFF) / 255.0f;
                RenderSystem.setShaderColor(r, g, b, a);
                graphics.blit(layer.getSecond(), x, y, sizeX, sizeY, 8.0f, 8, 8, 8, 64, 64);
                RenderSystem.enableBlend();
                graphics.blit(layer.getSecond(), x, y, sizeX, sizeY, 40.0F, 8, 8, 8, 64, 64);
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
    public void render(T entity, float entityYaw, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        boolean slim = isSlim(entity);
        this.updateModelFromEntity(entity, slim);
        this.getModel().setDelegate(this.internalHumanoid);
        this.armorLayer.setSlim(slim);
        this.setModelProperties(entity);
        for (NPCFeature feature : entity.lookFeatures) {
            NPCFeatureRenderers.get(feature).onSetup(feature, this, entity, stack);
        }
        super.render(entity, entityYaw, partialTick, stack, buffer, packedLight);
    }

    protected void updateModelFromEntity(T entity, boolean slim) {
        this.current = this.getModelHolder(this.getModelLocation(entity, slim));
        this.model = this.current.get(NPCTextureLayer.ModelType.SKIN_LAYER);
    }

    protected Pair<ResourceLocation, ResourceLocation> getModelLocation(T entity, boolean slim) {
        ModelFeatureType.ModelFeature modelFeature = entity.lookFeatures.getFeature(RuneCraftoryNPCLooks.MODEL.get());
        ResourceLocation model = slim ? HumanoidModelLocations.DEFAULT_LOCATION_SLIM : HumanoidModelLocations.DEFAULT_LOCATION;
        ResourceLocation animation = null;
        if (modelFeature != null) {
            ModelFeatureType.ModelData modelData = modelFeature.model().orElse(null);
            if (modelData != null) {
                model = modelData.model();
            }
            if (modelFeature.animation().isPresent()) {
                animation = modelFeature.animation().get();
            }
        }
        return Pair.of(model, animation);
    }

    private NPCModelHolder getModelHolder(ResourceLocation model) {
        return this.getModelHolder(Pair.of(model, null));
    }

    protected NPCModelHolder getModelHolder(Pair<ResourceLocation, ResourceLocation> location) {
        return this.models.computeIfAbsent(location, key -> new NPCModelHolder(location));
    }

    protected NPCModelHolder getCurrent() {
        return this.current;
    }

    private void setModelProperties(NPCEntity npc) {
        HumanoidBasedModel<T> currentModel = this.getModel();
        currentModel.setAllVisible(true);
        currentModel.crouching = npc.isCrouching();
        HumanoidModel.ArmPose main = getArmPose(npc, InteractionHand.MAIN_HAND);
        HumanoidModel.ArmPose off = getArmPose(npc, InteractionHand.OFF_HAND);
        if (main.isTwoHanded()) {
            off = npc.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
        }
        if (npc.getMainArm() == HumanoidArm.RIGHT) {
            currentModel.rightArmPose = main;
            currentModel.leftArmPose = off;
        } else {
            currentModel.rightArmPose = off;
            currentModel.leftArmPose = main;
        }
    }

    private static HumanoidModel.ArmPose getArmPose(NPCEntity npc, InteractionHand hand) {
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
        return getTextureFromLook(entity, NPCTextureLayer.LayerType.SKIN_LAYER, null);
    }

    @Override
    protected void setupRotations(T entity, PoseStack stack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, stack, bob, yBodyRot, partialTick, scale);
        if (entity.getPlayDeathTick() > 0) {
            float partial = partialTick - 1;
            float f = (entity.getPlayDeathTick() + (entity.playDeath() ? partial : -partial)) / 20.0f * 1.6f;
            if ((f = Mth.sqrt(f)) > 1.0f) {
                f = 1.0f;
            }
            stack.translate(0, f * 0.1, -f * entity.getBbHeight() * 0.5);
            stack.mulPose(Axis.XP.rotationDegrees(f * this.getFlipDegrees(entity)));
        }
        for (NPCFeature feature : entity.lookFeatures) {
            NPCFeatureRenderers.get(feature).transformStack(feature, this, entity, stack, partialTick);
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

    static class PlayerSkinData {

        private GameProfile gameProfile;

        private ResourceLocation location = DefaultPlayerSkin.getDefaultTexture();
        private PlayerSkin.Model skinMeta = net.minecraft.client.resources.PlayerSkin.Model.SLIM;

        private boolean pendingTextures;

        public PlayerSkinData(String name) {
            SkullBlockEntity.fetchGameProfile(name)
                    .thenAccept(prof -> prof.ifPresent(p -> this.gameProfile = p));
        }

        public ResourceLocation getLocation() {
            this.registerTextures();
            return this.location;
        }

        public PlayerSkin.Model getSkinMeta() {
            this.registerTextures();
            return this.skinMeta;
        }

        protected void registerTextures() {
            synchronized (this) {
                if (!this.pendingTextures && this.gameProfile != null) {
                    this.pendingTextures = true;
                    Minecraft.getInstance().getSkinManager().getOrLoad(this.gameProfile)
                            .thenAccept(skin -> {
                                this.location = skin.texture();
                                this.skinMeta = skin.model();
                            });
                }
            }
        }
    }
}
