package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.blocks.tile.CropBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.tile.FarmBlockEntity;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class FarmlandInfo extends GuiComponent {

    private static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/farmland_view.png");
    private final Minecraft mc;
    private final int sizeX = 100, sizeY = 100;

    public FarmlandInfo(Minecraft mc) {
        this.mc = mc;
    }

    public void render(PoseStack stack) {
        if (!EntityUtils.shouldShowFarmlandView(this.mc.player))
            return;
        HitResult res = this.mc.hitResult;
        if (res == null || res.getType() != HitResult.Type.BLOCK)
            return;
        BlockHitResult blockHitResult = (BlockHitResult) res;
        BlockState blockState = this.mc.level.getBlockState(blockHitResult.getBlockPos());
        BlockEntity entity = this.mc.level.getBlockEntity(blockHitResult.getBlockPos());
        FarmBlockEntity farmBlock = null;
        CropBlockEntity cropBlock = null;
        if (blockState.getBlock() instanceof CropBlock) {
            entity = this.mc.level.getBlockEntity(blockHitResult.getBlockPos().below());
        } else if (entity instanceof CropBlockEntity) {
            cropBlock = (CropBlockEntity) entity;
            entity = this.mc.level.getBlockEntity(blockHitResult.getBlockPos().below());
        }
        if (entity instanceof FarmBlockEntity)
            farmBlock = (FarmBlockEntity) entity;
        if (farmBlock == null && cropBlock == null)
            return;

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, texturepath);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        int sX = 100;
        int sY = (cropBlock != null ? 40 : 0) + (farmBlock != null ? 50 : 0);
        int xPos = ClientConfig.farmlandPosition.positionX(this.mc.getWindow().getGuiScaledWidth(), sX, ClientConfig.farmlandX);
        int yPos = ClientConfig.farmlandPosition.positionY(this.mc.getWindow().getGuiScaledHeight(), sY, ClientConfig.farmlandY);
        this.blit(stack, xPos, yPos, 0, 0, sX - 5, sY - 5);
        this.blit(stack, xPos + sX - 5, yPos, 150 - 5, 0, 5, sY - 5);
        this.blit(stack, xPos, yPos + sY - 5, 0, 150 - 5, sX - 5, 5);
        this.blit(stack, xPos + sX - 5, yPos + sY - 5, 150 - 5, 150 - 5, 5, 5);
        RenderSystem.defaultBlendFunc();
        yPos += 5;
        xPos += 5;
        if (cropBlock != null) {
            MutableComponent growth = new TextComponent(cropBlock.growthPercent() + "%");
            if (cropBlock.growthPercent() == 100)
                growth.withStyle(ChatFormatting.GREEN);
            this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.crop.growth", growth), xPos, yPos, 0x000000);
            this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.crop.level", cropBlock.level()), xPos, yPos + 10, 0x000000);
            MutableComponent giant = new TextComponent(cropBlock.giantProgress() + "%");
            if (cropBlock.giantProgress() == 100)
                giant.withStyle(ChatFormatting.GREEN);
            this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.crop.giant", giant), xPos, yPos + 10 * 2, 0x000000);
            yPos += 10 * 4;
        }
        if (farmBlock != null) {
            MutableComponent growth = new TextComponent(this.formattedValue(farmBlock.growth()));
            if (farmBlock.growth() <= 0.5)
                growth.withStyle(ChatFormatting.RED);
            this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.speed", growth), xPos, yPos, 0x000000);
            MutableComponent health = new TextComponent(farmBlock.health() + "");
            if (farmBlock.health() <= 10)
                health.withStyle(ChatFormatting.RED);
            this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.health", health), xPos, yPos + 10, 0x000000);
            this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.level", this.formattedValue(farmBlock.level())), xPos, yPos + 10 * 2, 0x000000);
            this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.giant", this.formattedValue(farmBlock.giantBonus())), xPos, yPos + 10 * 3, 0x000000);
        }
    }

    private String formattedValue(float f) {
        return String.format("%.2f", f);
    }
}