package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityFurniture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class RenderFurnitures extends EntityRenderer<EntityFurniture> {

    public static final ModelLayerLocation LOC_CHAIR = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "chair"), "main");
    public static final ModelLayerLocation LOC_CHIPSQUEEK_PLUSH = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "chipsqueek_plush"), "main");
    public static final ModelLayerLocation LOC_WOOLY_PLUSH = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "wooly_plush"), "main");

    private static final ResourceLocation texChair = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/chair.png");
    private static final ResourceLocation texWooly = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/wooly_plush.png");
    private static final ResourceLocation texChipsqueek = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/chipsqueek_plush.png");

    private final BlockState barrel = Blocks.BARREL.defaultBlockState();
    private final BlockState anvil = Blocks.ANVIL.defaultBlockState();
    private final ModelPart chestLid;
    private final ModelPart chestBottom;
    private final ModelPart chestLock;

    private final ModelPart chair;
    private final ModelPart woolyPlush;
    private final ModelPart chipSqueekPlush;

    public RenderFurnitures(EntityRendererProvider.Context ctx) {
        super(ctx);
        ModelPart modelPart = ctx.bakeLayer(ModelLayers.CHEST);
        this.chestLid = modelPart.getChild("bottom");
        this.chestBottom = modelPart.getChild("lid");
        this.chestLock = modelPart.getChild("lock");

        this.chair = ctx.bakeLayer(LOC_CHAIR);
        this.woolyPlush = ctx.bakeLayer(LOC_CHIPSQUEEK_PLUSH);
        this.chipSqueekPlush = ctx.bakeLayer(LOC_WOOLY_PLUSH);
    }

    public static LayerDefinition chairLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -3.1667F, -6.8333F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(16, 28).addBox(3.0F, -1.1667F, -5.8333F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 28).addBox(-5.0F, -1.1667F, -5.8333F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).addBox(3.0F, -1.1667F, 2.1667F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 14).addBox(-5.0F, -1.1667F, 2.1667F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-6.0F, -15.1667F, 3.1667F, 12.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.1667F, 0.8333F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static LayerDefinition chipSqueekPlushLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 10).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(18, 19).addBox(-2.0F, -4.0F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 19).addBox(1.0F, -4.0F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(6, 19).addBox(1.0F, -1.0F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 19).addBox(-2.0F, -1.0F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, -10.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(4, 22).addBox(-2.0F, -11.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(1.0F, -11.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(16, 10).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.6545F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public static LayerDefinition woolyPlushLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 10).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 22).addBox(-2.0F, -4.0F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(6, 22).addBox(1.0F, -4.0F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(1.0F, -1.0F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 19).addBox(-2.0F, -1.0F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, -10.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(10, 19).addBox(-1.0F, -2.0F, 2.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 19).addBox(0.0F, 0.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -9.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition bone3 = bone.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(16, 10).addBox(-3.0F, 0.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -9.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void render(EntityFurniture entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.scale(1.2f, 1.2f, 1.2f);
        switch (entity.getFurnitureType()) {
            case BARREL -> this.renderBlockModel(this.barrel, stack, buffer, packedLight);
            case ANVIL -> this.renderBlockModel(this.anvil, stack, buffer, packedLight);
            case CHEST ->
                    this.renderModel(stack, buffer, packedLight, Sheets.CHEST_LOCATION.texture(), this.chestBottom, this.chestLid, this.chestLock);
            case CHAIR -> this.renderModel(stack, buffer, packedLight, texChair, this.chair);
            case PAINTING -> {

            }
            case WOOLYPLUSH -> this.renderModel(stack, buffer, packedLight, texWooly, this.woolyPlush);
            case CHIPSQUEEKPLUSH -> this.renderModel(stack, buffer, packedLight, texChipsqueek, this.chipSqueekPlush);
        }
        /*
        float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        float partialLivingTicks = (float)entity.tickCount + partialTicks;
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F + yaw));
        stack.mulPose(Vector3f.XP.rotationDegrees(pitch));
        VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(stack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);*/
        stack.popPose();
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFurniture entity) {
        return null;
    }

    private void renderBlockModel(BlockState state, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.translate(-0.5, 0, -0.5);
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = dispatcher.getBlockModel(state);
        dispatcher.getModelRenderer().renderModel(stack.last(), buffer.getBuffer(Sheets.solidBlockSheet()), state, model, 0.0f, 0.0f, 0.0f, packedLight, OverlayTexture.NO_OVERLAY);
    }

    private void renderModel(PoseStack stack, MultiBufferSource buffer, int packedLight, ResourceLocation tex, ModelPart... parts) {
        stack.mulPose(Vector3f.XP.rotationDegrees(180));
        VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.entityCutout(tex));
        for (ModelPart part : parts)
            part.render(stack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
