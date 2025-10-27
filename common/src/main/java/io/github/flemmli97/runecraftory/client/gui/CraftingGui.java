package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import io.github.flemmli97.runecraftory.common.network.C2SSelectRecipeCrafting;
import io.github.flemmli97.runecraftory.common.recipes.CraftingType;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.mixinhelper.GuiGraphicsExtension;
import io.github.flemmli97.tenshilib.client.gui.widget.list.SelectableEntry;
import io.github.flemmli97.tenshilib.client.gui.widget.list.SelectableListWidget;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CraftingGui extends AbstractContainerScreen<ContainerCrafting> {

    private static final ResourceLocation FORGING = RuneCraftory.modRes("textures/gui/container/forging.png");
    private static final ResourceLocation CRAFTING = RuneCraftory.modRes("textures/gui/container/crafting.png");
    private static final ResourceLocation COOKING = RuneCraftory.modRes("textures/gui/container/cooking.png");
    private static final ResourceLocation CHEMISTRY = RuneCraftory.modRes("textures/gui/container/chemistry.png");

    public static final ResourceLocation RUNEPOINTS = RuneCraftory.modRes("widget/runepoints_bar");
    public static final ResourceLocation RUNEPOINTS_BORDER = RuneCraftory.modRes("widget/runepoints_bar_border");

    public static final ResourceLocation SCROLLBAR = RuneCraftory.modRes("widget/scrollbar");
    public static final ResourceLocation SCROLLBAR_DISABLED = RuneCraftory.modRes("widget/scrollbar_disabled");

    protected final PlayerData data;
    private final Skills skill;

    private SelectableListWidget recipes;
    private long lastChange;

    public CraftingGui(ContainerCrafting container, Inventory inv, Component name) {
        super(container, inv, name);
        this.imageWidth = 209;
        this.imageHeight = 166;
        this.data = RunecraftoryAttachments.PLAYER_DATA.get().get(inv.player);
        this.skill = this.menu.craftingType().skill;
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        this.setupRecipes();
    }

    protected void setupRecipes() {
        if (this.recipes != null)
            this.removeWidget(this.recipes);
        List<SelectableEntry> entries = new ArrayList<>();
        for (int i = 0; i < this.menu.getMatchingRecipesClient().result().size(); i++) {
            entries.add(new RecipeOutputEntry(i));
        }
        this.addRenderableWidget(this.recipes = new SelectableListWidget(this.leftPos + 173, this.topPos + 13, 29, 140, this.font, entries)
                .setEntryHeight(20, 0)
                .scrollbar(new SelectableListWidget.Scrollbar(SCROLLBAR, SCROLLBAR_DISABLED, 6, 27, 1, 0)));
        this.lastChange = this.menu.getMatchingRecipesClient().lastChange();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.menu.getMatchingRecipesClient().lastChange() != this.lastChange) {
            this.setupRecipes();
        }
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        ResourceLocation texture;
        texture = switch (this.menu.craftingType()) {
            case FORGE -> FORGING;
            case ACCESSORY_WORKBENCH -> CRAFTING;
            case CHEMISTRY_SET -> CHEMISTRY;
            case COOKING_TABLE -> COOKING;
        };
        graphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (this.menu.runepointCost() >= 0) {
            int rpMax = this.data.getMaxRunePoints();
            MutableComponent cost = Component.literal("" + this.menu.runepointCost());
            if (rpMax < this.menu.runepointCost() && !this.minecraft.player.isCreative()) {
                cost = Component.translatable("runecraftory.gui.crafting.rpMax.missing").withStyle(ChatFormatting.DARK_RED);
            }
            GuiGraphicsExtension.drawCenteredString(graphics, this.font, cost, this.leftPos + 123, this.topPos + 20, 0, false);
        }
        int xPos = this.leftPos;
        int yPos = this.topPos - 13;
        int barWidth = 102;
        int runeWidth = Math.min(100, (int) (this.data.getRunePoints() / (float) this.data.getMaxRunePoints() * 100.0f));
        graphics.blitSprite(RUNEPOINTS_BORDER, xPos, yPos, barWidth, 11);
        GuiUtils.drawBorderedBar(graphics, RUNEPOINTS,
                xPos, yPos, barWidth, 11, runeWidth, 1, 1);
        GuiGraphicsExtension.drawCenteredString(graphics, this.font, this.data.getRunePoints() + "/" + this.data.getMaxRunePoints(),
                xPos + (barWidth + 1) * 0.5f, yPos + 2, 0xffffff, false);

        graphics.drawString(this.font, Component.translatable("runecraftory.gui.display.level", this.data.getSkillLevel(this.skill).getLevel()),
                this.leftPos + this.titleLabelX + this.font.width(this.title) + 6, this.topPos + this.titleLabelY, 0x404040, false);
    }

    public CraftingType type() {
        return this.menu.craftingType();
    }

    public void setSelectedRecipe(int index) {
        this.recipes.select(index, false);
    }

    private class RecipeOutputEntry implements SelectableEntry {

        private static final ResourceLocation BUTTON = ResourceLocation.withDefaultNamespace("widget/button");
        private static final ResourceLocation HIGHLIGHTED = ResourceLocation.withDefaultNamespace("widget/button_highlighted");

        private final int index;

        private RecipeOutputEntry(int index) {
            this.index = index;
        }

        @Override
        public void updateDimensions(int width, int height) {
        }

        @Override
        public void render(SelectableListWidget widget, GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y, boolean selected, boolean hovered) {
            if (this.index < CraftingGui.this.menu.getMatchingRecipesClient().result().size()) {
                graphics.blitSprite(hovered || selected ? HIGHLIGHTED : BUTTON, x, y, 22, 20);
                ItemStack stack = CraftingGui.this.menu.getMatchingRecipesClient().result().get(this.index);
                graphics.renderItem(stack, x + 3, y + 2);
                if (CraftingGui.this.menu.getCarried().isEmpty() && hovered) {
                    graphics.renderTooltip(widget.getFont(), CraftingGui.this.getTooltipFromContainerItem(stack), stack.getTooltipImage(), mouseX, mouseY);
                }
            }
        }

        @Override
        public boolean onClick(double relativeMouseX, double relativeMouseY, boolean selected) {
            LoaderNetwork.INSTANCE.sendToServer(new C2SSelectRecipeCrafting(this.index));
            return true;
        }
    }
}
