package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.gui.widgets.PageButton;
import io.github.flemmli97.runecraftory.common.network.C2SOpenInfo;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class InfoSubScreen extends InfoScreen {

    private static final ResourceLocation page2 = new ResourceLocation(RuneCraftory.MODID, "textures/gui/skills_2.png");
    private static final ResourceLocation pageEnd = new ResourceLocation(RuneCraftory.MODID, "textures/gui/skills_3.png");
    private final int maxPages = EnumSkills.values().length / 12;
    private int page;

    public InfoSubScreen(AbstractContainerMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(stack, partialTicks, mouseX, mouseY);
        for (int i = 0; i < 6; ++i) {
            int index = i + this.page * 12;
            if (index < EnumSkills.values().length) {
                EnumSkills skill = EnumSkills.values()[index];
                int skillXP = (int) (this.data.getSkillLevel(skill).getXp() / (float) LevelCalc.xpAmountForSkillLevelUp(skill, this.data.getSkillLevel(skill).getLevel()) * 96.0f);
                RenderSystem.setShaderTexture(0, bars);
                this.blit(stack, this.leftPos + 9, this.topPos + 117 + 13 * i, 2, 80, skillXP, 9);
                this.minecraft.font.draw(stack, new TranslatableComponent(skill.getTranslation()), this.leftPos + 11, this.topPos + 118 + 13 * i, 0xffffff);
                ClientHandlers.drawRightAlignedScaledString(stack, this.font, "" + this.data.getSkillLevel(skill).getLevel(), this.leftPos + 104, this.topPos + 118 + 13 * i, 1.0f, 0xffffff);
            }
            index = i + 6 + this.page * 12;
            if (index < EnumSkills.values().length) {
                EnumSkills skill2 = EnumSkills.values()[i + 6 + this.page * 12];
                int skillXP2 = (int) (this.data.getSkillLevel(skill2).getXp() / (float) LevelCalc.xpAmountForSkillLevelUp(skill2, this.data.getSkillLevel(skill2).getLevel()) * 96.0f);
                RenderSystem.setShaderTexture(0, bars);
                this.blit(stack, this.leftPos + 119, this.topPos + 117 + 13 * i, 2, 80, skillXP2, 9);
                this.minecraft.font.draw(stack, new TranslatableComponent(skill2.getTranslation()), this.leftPos + 121, this.topPos + 118 + 13 * i, 0xffffff);
                ClientHandlers.drawRightAlignedScaledString(stack, this.font, "" + this.data.getSkillLevel(skill2).getLevel(), this.leftPos + 214, this.topPos + 118 + 13 * i, 1.0f, 0xffffff);
            }
        }
    }

    @Override
    protected void buttons() {
        if (this.page < this.maxPages)
            this.addRenderableWidget(new PageButton(this.leftPos + 206, this.topPos + 5, new TextComponent(">"), b -> {
                this.page++;
                this.init(this.minecraft, this.width, this.height);
            }));
        this.addRenderableWidget(new PageButton(this.leftPos + 193, this.topPos + 5, new TextComponent("<"), b -> {
            if (this.page == 0)
                Platform.INSTANCE.sendToServer(new C2SOpenInfo(C2SOpenInfo.Type.MAIN));
            else {
                this.page--;
                this.init(this.minecraft, this.width, this.height);
            }
        }));
    }

    @Override
    protected ResourceLocation texture() {
        if (this.page == this.maxPages)
            return pageEnd;
        return page2;
    }
}
