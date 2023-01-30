package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientFarmlandHandler;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandDataContainer;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CropBlock;
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
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState blockState = this.mc.level.getBlockState(pos);
        boolean cropBlock = false;
        FarmlandDataContainer data = null;

        if (blockState.getBlock() instanceof CropBlock) {
            pos = pos.below();
            blockState = this.mc.level.getBlockState(pos);
            cropBlock = true;
        }
        if (FarmlandHandler.isFarmBlock(blockState))
            data = ClientFarmlandHandler.INSTANCE.getData(pos);
        if (data == null)
            return;

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, texturepath);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        int sY = 60 + (cropBlock ? 50 : 0);
        int xPos = ClientConfig.farmlandPosition.positionX(this.mc.getWindow().getGuiScaledWidth(), 100, ClientConfig.farmlandX);
        int yPos = ClientConfig.farmlandPosition.positionY(this.mc.getWindow().getGuiScaledHeight(), sY, ClientConfig.farmlandY);
        this.blit(stack, xPos, yPos, 0, 0, 100, sY - 5);
        this.blit(stack, xPos, yPos + sY - 5, 0, 100 - 5, 100, 5);
        RenderSystem.defaultBlendFunc();
        yPos += 5;
        xPos += 5;
        if (cropBlock) {
            MutableComponent growth = new TextComponent(data.ageProgress() + "%");
            if (data.ageProgress() == 100)
                growth.withStyle(ChatFormatting.GREEN);
            this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.crop.growth", growth), xPos, yPos, 0x000000);
            this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.crop.level", data.cropLevel()), xPos, yPos + 10, 0x000000);
            MutableComponent giant = new TextComponent(data.cropSizeProgress() + "%");
            if (data.cropSizeProgress() == 100)
                giant.withStyle(ChatFormatting.GREEN);
            this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.crop.giant", giant), xPos, yPos + 10 * 2, 0x000000);
            yPos += 10 * 4;
        }
        MutableComponent growth = new TextComponent(this.formattedValue(data.growth()));
        if (data.growth() <= 0.5)
            growth.withStyle(ChatFormatting.RED);
        this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.speed", growth), xPos, yPos, 0x000000);
        MutableComponent health = new TextComponent(data.health() + "");
        if (data.health() <= 10)
            health.withStyle(ChatFormatting.RED);
        this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.health", health), xPos, yPos + 10, 0x000000);
        this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.level", this.formattedValue(data.quality())), xPos, yPos + 10 * 2, 0x000000);
        this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.giant", this.formattedValue(data.size())), xPos, yPos + 10 * 3, 0x000000);
        this.mc.font.draw(stack, new TranslatableComponent("magnifying_glass.view.defence", this.formattedValue(data.defence())), xPos, yPos + 10 * 4, 0x000000);
    }

    private String formattedValue(float f) {
        return String.format("%.2f", f);
    }
}