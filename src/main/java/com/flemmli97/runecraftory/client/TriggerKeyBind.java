package com.flemmli97.runecraftory.client;

import net.minecraft.client.settings.KeyBinding;

public class TriggerKeyBind extends KeyBinding {

    private int press;

    public TriggerKeyBind(String name, int defaultKey, String category) {
        super(name, defaultKey, category);
    }

    public boolean onPress() {
        if(this.isPressed())
            return ++this.press == 1;
        else
            this.press = 0;
        return false;
    }
}
