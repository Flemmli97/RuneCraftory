package io.github.flemmli97.runecraftory.client.gui.widgets;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.NPCDialogueGui;
import io.github.flemmli97.tenshilib.client.gui.widget.TexturedButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

public class DialogueOptionButton extends TexturedButton {

    private static final WidgetSprites SPRITE = new WidgetSprites(RuneCraftory.modRes("hud/npc_dialogue"), RuneCraftory.modRes("hud/npc_dialogue_highlighted"));

    public static final int MAX_WIDTH = 100;

    private final List<FormattedCharSequence> text;
    private final int txtX;

    public DialogueOptionButton(int xCenter, int yBottom, Font font, Component text, OnPress press) {
        super(xCenter, yBottom, 0, 0, Component.empty(), press);
        this.withSprite(SPRITE);
        this.text = font.split(text, MAX_WIDTH);
        int txtWidth = this.text.size() == 1 ? font.width(text) : MAX_WIDTH;
        this.width = txtWidth + NPCDialogueGui.BORDER_SIZE * 2;
        this.height = this.text.size() * font.lineHeight + NPCDialogueGui.BORDER_SIZE * 2;
        this.setX(this.getX() - this.width / 2);
        this.setY(this.getY() - this.height);
        this.txtX = this.getX() + this.width / 2;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(graphics, mouseX, mouseY, partialTick);
        int y = 0;
        Font font = Minecraft.getInstance().font;
        int j = this.active ? 0xFFFFFF : 0xA0A0A0;
        for (FormattedCharSequence comp : this.text) {
            graphics.drawString(font, comp,
                    this.txtX - font.width(comp) / 2, this.getY() + NPCDialogueGui.BORDER_SIZE + y,
                    j | Mth.ceil(this.alpha * 255.0f) << 24, false);
            y += font.lineHeight;
        }
    }
}
