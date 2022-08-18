package io.github.flemmli97.runecraftory.client;

import net.minecraft.client.KeyMapping;

public class TriggerKeyBind extends KeyMapping {

    private int press;

    public TriggerKeyBind(String name, int defaultKey, String category) {
        super(name, defaultKey, category);
    }

    public boolean onPress() {
        if (this.isDown())
            return ++this.press == 1;
        else
            this.press = 0;
        return false;
    }
}
