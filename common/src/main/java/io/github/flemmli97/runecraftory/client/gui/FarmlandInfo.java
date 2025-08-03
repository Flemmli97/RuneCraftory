package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientFarmlandHandler;
import io.github.flemmli97.runecraftory.common.blocks.TreeBaseBlock;
import io.github.flemmli97.runecraftory.common.blocks.util.Growable;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandDataContainer;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class FarmlandInfo {

    private static final ResourceLocation TEXTURE = RuneCraftory.modRes("hud/farmland_view");

    private final Minecraft mc;

    public FarmlandInfo(Minecraft mc) {
        this.mc = mc;
    }

    public static boolean shouldShowFarmlandView(LivingEntity entity) {
        ItemStack main = entity.getMainHandItem();
        ItemStack off = entity.getOffhandItem();
        return main.has(RuneCraftoryDataComponentTypes.MAGNIFYING_GLASS.get()) || off.has(RuneCraftoryDataComponentTypes.MAGNIFYING_GLASS.get());
    }

    public void render(GuiGraphics graphics) {
        if (!shouldShowFarmlandView(this.mc.player))
            return;
        HitResult res = this.mc.hitResult;
        if (res == null || res.getType() != HitResult.Type.BLOCK)
            return;
        BlockHitResult blockHitResult = (BlockHitResult) res;
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState blockState = this.mc.level.getBlockState(pos);
        boolean cropBlock = false;
        FarmlandDataContainer data = null;

        if (blockState.getBlock() instanceof BushBlock) {
            pos = pos.below();
            cropBlock = blockState.getBlock() instanceof Growable;
            blockState = this.mc.level.getBlockState(pos);
        }
        if (blockState.getBlock() instanceof TreeBaseBlock) {
            pos = pos.below();
            cropBlock = true;
            blockState = this.mc.level.getBlockState(pos);
        }
        if (FarmlandHandler.isFarmBlock(blockState))
            data = ClientFarmlandHandler.INSTANCE.getData(pos);
        if (data == null)
            return;

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        int sY = 61 + (cropBlock ? 40 : 0);
        int xPos = ClientConfig.farmlandPosition.positionX(this.mc.getWindow().getGuiScaledWidth(), 100, ClientConfig.farmlandX);
        int yPos = ClientConfig.farmlandPosition.positionY(this.mc.getWindow().getGuiScaledHeight(), sY, ClientConfig.farmlandY);
        graphics.blitSprite(TEXTURE, xPos, yPos, 100, sY);
        RenderSystem.defaultBlendFunc();
        yPos += 5;
        xPos += 5;
        if (cropBlock) {
            MutableComponent growth = Component.literal(data.ageProgress() + "%");
            if (data.ageProgress() == 100)
                growth.withStyle(ChatFormatting.GREEN);
            graphics.drawString(this.mc.font, Component.translatable("runecraftory.magnifying_glass.view.crop.growth", growth), xPos, yPos, 0x000000, false);
            yPos += 10;
            graphics.drawString(this.mc.font, Component.translatable("runecraftory.magnifying_glass.view.crop.level", data.cropLevel()), xPos, yPos, 0x000000, false);
            yPos += 10;
            MutableComponent giant = Component.literal(data.cropSizeProgress() + "%");
            if (data.cropSizeProgress() == 100)
                giant.withStyle(ChatFormatting.GREEN);
            graphics.drawString(this.mc.font, Component.translatable("runecraftory.magnifying_glass.view.crop.giant", giant), xPos, yPos, 0x000000, false);
            yPos += 10;
        }
        MutableComponent growth = Component.literal(this.formattedValue(data.growth()));
        if (data.growth() <= 0.5)
            growth.withStyle(ChatFormatting.RED);
        graphics.drawString(this.mc.font, Component.translatable("runecraftory.magnifying_glass.view.speed", growth), xPos, yPos, 0x000000, false);
        MutableComponent health = Component.literal(data.health() + "");
        if (data.health() <= 10)
            health.withStyle(ChatFormatting.RED);
        graphics.drawString(this.mc.font, Component.translatable("runecraftory.magnifying_glass.view.health", health), xPos, yPos + 10, 0x000000, false);
        graphics.drawString(this.mc.font, Component.translatable("runecraftory.magnifying_glass.view.level", this.formattedValue(data.quality())), xPos, yPos + 10 * 2, 0x000000, false);
        graphics.drawString(this.mc.font, Component.translatable("runecraftory.magnifying_glass.view.giant", this.formattedValue(data.size())), xPos, yPos + 10 * 3, 0x000000, false);
        graphics.drawString(this.mc.font, Component.translatable("runecraftory.magnifying_glass.view.defence", this.formattedValue(data.defence())), xPos, yPos + 10 * 4, 0x000000, false);
    }

    private String formattedValue(float f) {
        return String.format("%.2f", f);
    }
}