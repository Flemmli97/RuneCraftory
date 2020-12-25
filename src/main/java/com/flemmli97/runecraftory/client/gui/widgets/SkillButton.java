package com.flemmli97.runecraftory.client.gui.widgets;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class SkillButton extends Button {

    public SkillButton(int x, int y, IPressable press) {
        super(x, y, 24, 24, StringTextComponent.EMPTY, press);
    }
}
