package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.wisp.EntityWispBase;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.client.AnimationManager;
import io.github.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ModelWisp<T extends EntityWispBase> extends EntityModel<T> implements ExtendedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "wisp"), "main");

    protected final ModelPartHandler model;
    protected final BlockBenchAnimations anim;

    public ModelWisp(ModelPart root) {
        super(RenderType::entityTranslucentCull);
        this.model = new ModelPartHandler(root, "main");
        this.anim = AnimationManager.getInstance().getAnimation(new ResourceLocation(RuneCraftory.MODID, "wisp"));
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-4.5F, -4.5F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

        PartDefinition floaty = main.addOrReplaceChild("floaty", CubeListBuilder.create().texOffs(4, 23).addBox(9.0F, 2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition floaty2 = main.addOrReplaceChild("floaty2", CubeListBuilder.create().texOffs(0, 23).addBox(-8.0F, -5.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition floaty3 = main.addOrReplaceChild("floaty3", CubeListBuilder.create().texOffs(4, 20).addBox(9.0F, -5.0F, -7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition floaty4 = main.addOrReplaceChild("floaty4", CubeListBuilder.create().texOffs(0, 20).addBox(-9.0F, 2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition floaty5 = main.addOrReplaceChild("floaty5", CubeListBuilder.create().texOffs(0, 4).addBox(-2.0F, 0.0F, 7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition floaty6 = main.addOrReplaceChild("floaty6", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -8.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        //poseStack.scale(0.85f, 0.85f, 0.85f);
        //poseStack.translate(0, 0.2, 0);
        this.model.getMainPart().render(poseStack, buffer, 0xff00ff, packedOverlay, red, green, blue, 0.8f);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        this.model.getMainPart().visible = true;
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.deathTime <= 0) {
            this.anim.doAnimation(this, "iddle", entity.tickCount, partialTicks);
        }
        if (anim != null) {
            if (anim.getID().equals(EntityWispBase.vanish.getID())) {
                int tick = anim.getTick();
                if (tick < 10 || tick > 90)
                    this.model.getMainPart().visible = tick % 8 == 0;
                else if (tick < 20 || tick > 80)
                    this.model.getMainPart().visible = tick % 5 == 0;
                else if (tick < 40 || tick > 60)
                    this.model.getMainPart().visible = tick % 2 == 0;
                else
                    this.model.getMainPart().visible = false;
            } else
                this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
        }
    }

    @Override
    public ModelPartHandler getHandler() {
        return this.model;
    }
}