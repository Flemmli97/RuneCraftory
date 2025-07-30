package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.FurnitureEntity;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FurnituresRender extends EntityRenderer<FurnitureEntity> {

    public static final ResourceLocation LOC_CHAIR = RuneCraftory.modRes("entity/chair");
    public static final ResourceLocation LOC_CHIPSQUEEK_PLUSH = RuneCraftory.modRes("entity/chipsqueek_plush");
    public static final ResourceLocation LOC_WOOLY_PLUSH = RuneCraftory.modRes("entity/wooly_plush");

    private static final ResourceLocation TEX_CHAIR = RuneCraftory.modRes("textures/entity/projectile/chair.png");
    private static final ResourceLocation TEX_WOOLY = RuneCraftory.modRes("textures/entity/projectile/wooly_plush.png");
    private static final ResourceLocation TEX_CHIPSQUEEK = RuneCraftory.modRes("textures/entity/projectile/chipsqueek_plush.png");

    private final BlockState barrel = Blocks.BARREL.defaultBlockState();
    private final BlockState anvil = Blocks.ANVIL.defaultBlockState();
    private final ModelPart chestLid;
    private final ModelPart chestBottom;
    private final ModelPart chestLock;

    private final ReloadableCache<ModelPartsContainer> chair;
    private final ReloadableCache<ModelPartsContainer> woolyPlush;
    private final ReloadableCache<ModelPartsContainer> chipSqueekPlush;

    public FurnituresRender(EntityRendererProvider.Context ctx) {
        super(ctx);
        ModelPart modelPart = ctx.bakeLayer(ModelLayers.CHEST);
        this.chestLid = modelPart.getChild("bottom");
        this.chestBottom = modelPart.getChild("lid");
        this.chestLock = modelPart.getChild("lock");

        this.chair = GeoModelManager.getInstance().getModel(LOC_CHAIR);
        this.woolyPlush = GeoModelManager.getInstance().getModel(LOC_WOOLY_PLUSH);
        this.chipSqueekPlush = GeoModelManager.getInstance().getModel(LOC_CHIPSQUEEK_PLUSH);
    }

    @Override
    public void render(FurnitureEntity entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.scale(1.2f, 1.2f, 1.2f);
        stack.mulPose(Axis.YP.rotationDegrees(entity.getRandomRotationOffset()));
        switch (entity.getFurnitureType()) {
            case BARREL -> this.renderBlockModel(this.barrel, stack, buffer, packedLight);
            case ANVIL -> this.renderBlockModel(this.anvil, stack, buffer, packedLight);
            case CHEST -> {
                stack.scale(-1.0f, -1.0f, 1.0f);
                stack.translate(0.5, -1.5, -0.5);
                this.renderModel(stack, Sheets.CHEST_LOCATION.buffer(buffer, RenderType::entityCutout), packedLight, this.chestBottom, this.chestLid, this.chestLock);
            }
            case CHAIR -> this.renderModel(stack, this.simpleConsumer(buffer, TEX_CHAIR), packedLight, this.chair);
            case WOOLYPLUSH ->
                    this.renderModel(stack, this.simpleConsumer(buffer, TEX_WOOLY), packedLight, this.woolyPlush);
            case CHIPSQUEEKPLUSH ->
                    this.renderModel(stack, this.simpleConsumer(buffer, TEX_CHIPSQUEEK), packedLight, this.chipSqueekPlush);
        }
        stack.popPose();
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(FurnitureEntity entity) {
        return null;
    }

    private void renderBlockModel(BlockState state, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.translate(-0.5, 0, -0.5);
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        dispatcher.renderSingleBlock(state, stack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
    }

    private void renderModel(PoseStack stack, VertexConsumer ivertexbuilder, int packedLight, ModelPart... parts) {
        stack.scale(-1, -1, 1);
        stack.translate(0.0, -1.5, 0.0);
        for (ModelPart part : parts)
            part.render(stack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, CommonColors.WHITE);
    }

    @SafeVarargs
    private void renderModel(PoseStack stack, VertexConsumer ivertexbuilder, int packedLight, ReloadableCache<ModelPartsContainer>... parts) {
        stack.scale(-1, -1, 1);
//        stack.translate(0.0, -1.5, 0.0);
        for (ReloadableCache<ModelPartsContainer> part : parts)
            part.get().getRoot().render(stack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, CommonColors.WHITE);
    }

    private VertexConsumer simpleConsumer(MultiBufferSource buffer, ResourceLocation tex) {
        return buffer.getBuffer(RenderType.entityCutout(tex));
    }
}
