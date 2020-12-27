package com.flemmli97.runecraftory.client.gui;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.client.gui.widgets.PageButton;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.network.C2SOpenInfo;
import com.flemmli97.runecraftory.network.PacketHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class InfoSubScreen extends InfoScreen {

    private static final ResourceLocation page2 = new ResourceLocation(RuneCraftory.MODID, "textures/gui/skills_2.png");
    private static final ResourceLocation pageEnd = new ResourceLocation(RuneCraftory.MODID, "textures/gui/skills_3.png");
    private int page;
    private final int maxPages = EnumSkills.values().length / 12;

    public InfoSubScreen(Container container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    protected void buttons() {
        if (this.page < this.maxPages)
            this.addButton(new PageButton(this.guiLeft + 206, this.guiTop + 5, new StringTextComponent(">"), b -> {
                this.page++;
                this.init(this.client, this.width, this.height);
            }));
        this.addButton(new PageButton(this.guiLeft + 193, this.guiTop + 5, new StringTextComponent("<"), b -> {
            if (this.page == 0)
                PacketHandler.sendToServer(new C2SOpenInfo(C2SOpenInfo.Type.MAIN));
            else {
                this.page--;
                this.init(this.client, this.width, this.height);
            }
        }));
    }

    @Override
    protected void drawBackground(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(stack, partialTicks, mouseX, mouseY);
        for (int i = 0; i < 6; ++i) {
            int index = i + this.page * 12;
            if (index < EnumSkills.values().length) {
                EnumSkills skill = EnumSkills.values()[index];
                int skillXP = (int) (this.cap.getSkillLevel(skill)[1] / (float) LevelCalc.xpAmountForSkills(this.cap.getSkillLevel(skill)[0]) * 96.0f);
                this.client.getTextureManager().bindTexture(bars);
                this.drawTexture(stack, this.guiLeft + 9, this.guiTop + 117 + 13 * i, 2, 80, skillXP, 9);
                this.client.fontRenderer.draw(stack, new TranslationTextComponent(skill.getTranslation()), this.guiLeft + 11, this.guiTop + 118 + 13 * i, 0xffffff);
                this.drawRightAlignedScaledString(stack, "" + this.cap.getSkillLevel(skill)[0], this.guiLeft + 104, this.guiTop + 118 + 13 * i, 1.0f, 0xffffff);
            }
            index = i + 6 + this.page * 12;
            if (index < EnumSkills.values().length) {
                EnumSkills skill2 = EnumSkills.values()[i + 6 + this.page * 12];
                int skillXP2 = (int) (this.cap.getSkillLevel(skill2)[1] / (float) LevelCalc.xpAmountForSkills(this.cap.getSkillLevel(skill2)[0]) * 96.0f);
                this.client.getTextureManager().bindTexture(bars);
                this.drawTexture(stack, this.guiLeft + 119, this.guiTop + 117 + 13 * i, 2, 80, skillXP2, 9);
                this.client.fontRenderer.draw(stack, new TranslationTextComponent(skill2.getTranslation()), this.guiLeft + 121, this.guiTop + 118 + 13 * i, 0xffffff);
                this.drawRightAlignedScaledString(stack, "" + this.cap.getSkillLevel(skill2)[0], this.guiLeft + 214, this.guiTop + 118 + 13 * i, 1.0f, 0xffffff);
            }
        }
    }

    @Override
    protected ResourceLocation texture() {
        if (this.page == this.maxPages)
            return pageEnd;
        return page2;
    }
}
