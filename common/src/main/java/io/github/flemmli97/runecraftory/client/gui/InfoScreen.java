package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.widgets.SpriteResources;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.network.C2SOpenInfo;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.mixinhelper.GuiGraphicsExtension;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.client.gui.widget.TexturedButton;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class InfoScreen extends EffectRenderingInventoryScreen<AbstractContainerMenu> {

    public static final ResourceLocation INVENTORY = RuneCraftory.modRes("textures/gui/container/info_inventory.png");
    public static final ResourceLocation SKILLS = RuneCraftory.modRes("textures/gui/container/info_skills.png");

    public static final ResourceLocation HEALTH = RuneCraftory.modRes("widget/health_bar");
    public static final ResourceLocation RUNEPOINTS = RuneCraftory.modRes("widget/runepoints_bar");
    public static final ResourceLocation EXPERIENCE = RuneCraftory.modRes("widget/experience_bar");

    protected final PlayerData data;

    private final Component levelTxt = Component.translatable("runecraftory.gui.level");

    public InfoScreen(AbstractContainerMenu container, Inventory inv, Component name) {
        super(container, inv, name);
        this.imageWidth = 213;
        this.imageHeight = 202;
        this.data = Platform.INSTANCE.getPlayerData(inv.player);
    }

    @Override
    protected void init() {
        super.init();
        this.buttons();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(this.texture(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int iconX = 110;
        int iconY = 64;
        graphics.blitSprite(SpriteResources.ATTACK_ICON, this.leftPos + iconX, this.topPos + iconY, 8, 8);
        graphics.blitSprite(SpriteResources.DEFENCE_ICON, this.leftPos + iconX, this.topPos + iconY + 13, 8, 8);
        graphics.blitSprite(SpriteResources.MAGIC_ATTACK_ICON, this.leftPos + iconX, this.topPos + iconY + 13 * 2, 8, 8);
        graphics.blitSprite(SpriteResources.MAGIC_DEFENCE_ICON, this.leftPos + iconX, this.topPos + iconY + 13 * 3, 8, 8);

        GuiGraphicsExtension.drawRightAlignedString(graphics, this.font, "" + this.data.getMoney(),
                this.leftPos + 194, this.topPos + 10, 0, false);

        int healthWidth = Math.min(100, (int) (this.data.player().getHealth() / this.data.player().getMaxHealth() * 100.0));
        int runeWidth = Math.min(100, (int) (this.data.getRunePoints() / (float) this.data.getMaxRunePoints() * 100.0f));
        int exp = Math.min(100, (int) (this.data.getPlayerLevel().getProgress() * 100.0f));
        int barX = 104;
        int barWidth = 102;
        GuiUtils.drawBorderedBar(graphics, HEALTH, this.leftPos + barX, this.topPos + 22, barWidth, 11, healthWidth, 1, 1);
        GuiUtils.drawBorderedBar(graphics, RUNEPOINTS, this.leftPos + barX, this.topPos + 36, barWidth, 11, runeWidth, 1, 1);
        GuiUtils.drawBorderedBar(graphics, EXPERIENCE, this.leftPos + barX, this.topPos + 50, barWidth, 11, exp, 1, 1);
        GuiGraphicsExtension.drawCenteredString(graphics, this.font, (int) this.data.player().getHealth() + "/" + (int) this.data.player().getMaxHealth(),
                this.leftPos + barX + (barWidth + 1) * 0.5f, this.topPos + 24, 0xffffff, false);
        GuiGraphicsExtension.drawCenteredString(graphics, this.font, this.data.getRunePoints() + "/" + this.data.getMaxRunePoints(),
                this.leftPos + barX + (barWidth + 1) * 0.5f, this.topPos + 38, 0xffffff, false);
        graphics.drawString(this.font, this.levelTxt, this.leftPos + 106, this.topPos + 52, 0, false);
        GuiGraphicsExtension.drawRightAlignedString(graphics, this.font, "" + this.data.getPlayerLevel().getLevel(),
                this.leftPos + barX + 99, this.topPos + 52, 0, false);

        float statX = 205 - 2;
        float statY = 61 + 3.5f;
        MutableComponent mut = Component.literal("" + (int) CombatUtils.getAttributeValue(this.data.player(), Attributes.ATTACK_DAMAGE));
        if (!ItemNBT.isWeapon(this.data.player().getMainHandItem())) {
            mut.withStyle(ChatFormatting.DARK_RED);
        }
        GuiGraphicsExtension.drawRightAlignedString(graphics, this.font, mut,
                this.leftPos + statX, this.topPos + statY, 0, false);
        GuiGraphicsExtension.drawRightAlignedString(graphics, this.font, "" + (int) CombatUtils.getAttributeValue(this.data.player(), RuneCraftoryAttributes.DEFENCE.asHolder()),
                this.leftPos + statX, this.topPos + statY + 13, 0, false);
        GuiGraphicsExtension.drawRightAlignedString(graphics, this.font, "" + (int) CombatUtils.getAttributeValue(this.data.player(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()),
                this.leftPos + statX, this.topPos + statY + 13 * 2, 0, false);
        GuiGraphicsExtension.drawRightAlignedString(graphics, this.font, "" + (int) CombatUtils.getAttributeValue(this.data.player(), RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder()),
                this.leftPos + statX, this.topPos + statY + 13 * 3, 0, false);

        LivingEntity entity = this.data.player();
        RenderUtils.renderScaledEntityGui(graphics, this.leftPos + 27, this.topPos + 9,
                49, 70, 30, 0.0625F, mouseX, mouseY, entity);
    }

    protected ResourceLocation texture() {
        return INVENTORY;
    }

    protected void buttons() {
        this.addRenderableWidget(new TexturedButton(this.leftPos + 8, this.topPos + 103, 12, 12,
                Component.literal("<"), b -> {
            InventoryScreen inventory = new InventoryScreen(this.data.player());
            ItemStack stack = this.data.player().containerMenu.getCarried();
            this.data.player().containerMenu.setCarried(ItemStack.EMPTY);
            this.minecraft.setScreen(inventory);
            this.data.player().containerMenu.setCarried(stack);
            LoaderNetwork.INSTANCE.sendToServer(new C2SOpenInfo(C2SOpenInfo.Action.INV));
        }).withSprite(SpriteResources.PAGE_BUTTON));
        this.addRenderableWidget(new TexturedButton(this.leftPos + 8 + 13, this.topPos + 103, 12, 12,
                Component.literal(">"), b -> LoaderNetwork.INSTANCE.sendToServer(new C2SOpenInfo(C2SOpenInfo.Action.SUB)))
                .withSprite(SpriteResources.PAGE_BUTTON));
    }
}