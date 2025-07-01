package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.client.gui.widgets.SpriteResources;
import io.github.flemmli97.runecraftory.common.network.C2SOpenInfo;
import io.github.flemmli97.runecraftory.mixinhelper.GuiGraphicsExtension;
import io.github.flemmli97.tenshilib.client.gui.widget.TexturedButton;
import io.github.flemmli97.tenshilib.client.gui.widget.list.SelectableEntry;
import io.github.flemmli97.tenshilib.client.gui.widget.list.SelectableListWidget;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.Arrays;

public class InfoSubScreen extends InfoScreen {

    public static final ResourceLocation SKILL_ENTRY = RuneCraftory.modRes("widget/skill_entry");
    public static final ResourceLocation SKILL_ENTRY_HIGHLIGHTED = RuneCraftory.modRes("widget/skill_entry_highlighted");
    public static final ResourceLocation SKILL_ENTRY_EXPERIENCE = RuneCraftory.modRes("widget/skill_experience_bar");

    public InfoSubScreen(AbstractContainerMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTick, mouseX, mouseY);
    }

    @Override
    protected ResourceLocation texture() {
        return InfoScreen.SKILLS;
    }

    @Override
    protected void buttons() {
        this.addRenderableWidget(new TexturedButton(this.leftPos + 8, this.topPos + 103, 12, 12,
                Component.literal("<"), b -> LoaderNetwork.INSTANCE.sendToServer(new C2SOpenInfo(C2SOpenInfo.Action.MAIN)))
                .withSprite(SpriteResources.PAGE_BUTTON));
        this.addRenderableWidget(new SelectableListWidget(this.leftPos + 27, this.topPos + 124, 160, 65, this.font, Arrays.stream(EnumSkills.values())
                .<SelectableEntry>map(SkillListEntry::new).toList())
                .withPadding(1));
    }

    private class SkillListEntry implements SelectableEntry {

        private final EnumSkills skill;

        private SkillListEntry(EnumSkills skill) {
            this.skill = skill;
        }

        @Override
        public void updateDimensions(int width, int height) {
        }

        @Override
        public void render(SelectableListWidget widget, GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y, boolean selected, boolean hovered) {
            graphics.blitSprite(hovered ? SKILL_ENTRY_HIGHLIGHTED : SKILL_ENTRY, x, y, 160, 13);
            graphics.drawString(widget.getFont(), Component.translatable(this.skill.getTranslation()),
                    x + 5, y + 3, 0, false);
            GuiGraphicsExtension.drawRightAlignedString(graphics, widget.getFont(), "" + InfoSubScreen.this.data.getSkillLevel(this.skill).getLevel(),
                    x + 100, y + 3, 0, false);
            int progress = Math.min(50, (int) (InfoSubScreen.this.data.getSkillLevel(this.skill).getProgress() * 50));
            GuiUtils.drawBorderedBar(graphics, SKILL_ENTRY_EXPERIENCE, x + 104, y + 2, 52, 9,
                    progress, 1, 1);
        }

        @Override
        public boolean onClick(double relativeMouseX, double relativeMouseY, boolean selected) {
            return false;
        }
    }
}
