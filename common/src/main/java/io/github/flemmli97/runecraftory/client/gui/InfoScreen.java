package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.gui.widgets.PageButton;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.network.C2SOpenInfo;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class InfoScreen extends EffectRenderingInventoryScreen<AbstractContainerMenu> {

    protected static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/skills_1.png");
    protected static final ResourceLocation bars = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");

    protected final PlayerData data;
    private final int textureX = 223;
    private final int textureY = 198;
    private final Component levelTxt = new TranslatableComponent("level");

    // Cause if we just simply check to apply the color it can go out of sync for a few render ticks since item is set first before attribute update packet arrives
    private int canUseAttack = -1;

    public InfoScreen(AbstractContainerMenu container, Inventory inv, Component name) {
        super(container, inv, name);
        this.data = Platform.INSTANCE.getPlayerData(inv.player).orElseThrow(EntityUtils::playerDataException);
    }

    @Override
    protected void init() {
        this.imageWidth = this.textureX;
        this.imageHeight = this.textureY;
        super.init();
        this.buttons();
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {

    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, this.texture());
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.textureX, this.textureY);
        int iconX = 122;
        int iconY = 58;
        this.blit(stack, this.leftPos + iconX, this.topPos + iconY, 226, 2, 8, 8);
        this.blit(stack, this.leftPos + iconX, this.topPos + iconY + 13, 226, 13, 8, 8);
        this.blit(stack, this.leftPos + iconX, this.topPos + iconY + 13 * 2, 226, 24, 8, 8);
        this.blit(stack, this.leftPos + iconX, this.topPos + iconY + 13 * 3, 226, 35, 8, 8);

        int healthWidth = Math.min(100, (int) (this.minecraft.player.getHealth() / this.minecraft.player.getMaxHealth() * 100.0));
        int runeWidth = Math.min(100, (int) (this.data.getRunePoints() / (float) this.data.getMaxRunePoints() * 100.0f));
        int exp = Math.min(100, (int) (this.data.getPlayerLevel().getXp() / (float) LevelCalc.xpAmountForLevelUp(this.data.getPlayerLevel().getLevel()) * 100.0f));
        RenderSystem.setShaderTexture(0, bars);
        int barX = 117;
        this.blit(stack, this.leftPos + barX, this.topPos + 20, 2, 51, healthWidth, 6);
        this.blit(stack, this.leftPos + barX, this.topPos + 30, 2, 58, runeWidth, 6);
        this.blit(stack, this.leftPos + barX, this.topPos + 41, 2, 66, exp, 9);
        ClientHandlers.drawCenteredScaledString(stack, this.font, (int) this.minecraft.player.getHealth() + "/" + (int) this.minecraft.player.getMaxHealth(), this.leftPos + barX + 50, this.topPos + 21f, 0.6f, 0xffffff);
        ClientHandlers.drawCenteredScaledString(stack, this.font, this.data.getRunePoints() + "/" + this.data.getMaxRunePoints(), this.leftPos + barX + 50, this.topPos + 31f, 0.6f, 0xffffff);
        this.minecraft.font.draw(stack, this.levelTxt, this.leftPos + 120, this.topPos + 42, 0);
        ClientHandlers.drawRightAlignedScaledString(stack, this.font, "" + this.data.getPlayerLevel().getLevel(), this.leftPos + barX + 99, this.topPos + 42, 1.0f, 0);
        ClientHandlers.drawRightAlignedScaledString(stack, this.font, "" + this.data.getMoney(), this.leftPos + 187, this.topPos + 9, 0.6f, 0);
        int statX = 216;
        int statY = 59;
        double att = CombatUtils.getAttributeValue(this.minecraft.player, Attributes.ATTACK_DAMAGE);
        MutableComponent mut = new TextComponent("" + (int) CombatUtils.getAttributeValue(this.minecraft.player, Attributes.ATTACK_DAMAGE));
        if (this.canUseAttack == -1) {
            this.canUseAttack = ItemNBT.isWeapon(this.minecraft.player.getMainHandItem()) ? 1 : 0;
        }
        if (this.canUseAttack == 0)
            mut.withStyle(ChatFormatting.DARK_RED);
        ClientHandlers.drawRightAlignedScaledString(stack, this.font, mut, this.leftPos + statX, this.topPos + statY, 1.0f, 0);
        ClientHandlers.drawRightAlignedScaledString(stack, this.font, "" + (int) CombatUtils.getAttributeValue(this.minecraft.player, ModAttributes.DEFENCE.get()), this.leftPos + statX, this.topPos + statY + 13, 1.0f, 0);
        ClientHandlers.drawRightAlignedScaledString(stack, this.font, "" + (int) CombatUtils.getAttributeValue(this.minecraft.player, ModAttributes.MAGIC.get()), this.leftPos + statX, this.topPos + statY + 13 * 2, 1.0f, 0);
        ClientHandlers.drawRightAlignedScaledString(stack, this.font, "" + (int) CombatUtils.getAttributeValue(this.minecraft.player, ModAttributes.MAGIC_DEFENCE.get()), this.leftPos + statX, this.topPos + statY + 13 * 3, 1.0f, 0);

        InventoryScreen.renderEntityInInventory(this.leftPos + 57, this.topPos + 76, 29, this.leftPos + 58 - mouseX, this.topPos + 26 - mouseY, this.minecraft.player);
    }

    protected void buttons() {
        this.addRenderableWidget(new PageButton(this.leftPos + 206, this.topPos + 5, new TextComponent(">"), b -> Platform.INSTANCE.sendToServer(new C2SOpenInfo(C2SOpenInfo.Type.SUB))));
        this.addRenderableWidget(new PageButton(this.leftPos + 193, this.topPos + 5, new TextComponent("<"), b -> {
            InventoryScreen inventory = new InventoryScreen(this.minecraft.player);
            ItemStack stack = this.minecraft.player.containerMenu.getCarried();
            this.minecraft.player.containerMenu.setCarried(ItemStack.EMPTY);
            this.minecraft.setScreen(inventory);
            this.minecraft.player.containerMenu.setCarried(stack);
            Platform.INSTANCE.sendToServer(new C2SOpenInfo(C2SOpenInfo.Type.INV));
        }));
    }

    protected ResourceLocation texture() {
        return texturepath;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    public void onAttributePkt() {
        this.canUseAttack = -1;
    }
}