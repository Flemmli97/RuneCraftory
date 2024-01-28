package io.github.flemmli97.runecraftory.client;

import net.minecraft.client.KeyMapping;

public class TriggerKeyBind extends KeyMapping {

    private int press;
    private boolean isPressing;
    private boolean lastState;

    public TriggerKeyBind(String name, int defaultKey, String category) {
        super(name, defaultKey, category);
    }

    public KeyState onPress() {
        KeyState state = KeyState.NONE;
        if (this.isDown()) {
            boolean first = ++this.press == 1;
            if (first)
                state = KeyState.PRESSING;
        } else {
            this.press = 0;
            if (this.lastState)
                state = KeyState.RELEASE;
        }
        this.lastState = this.isDown();
        return state;
    }

    public enum KeyState {
        NONE,
        PRESSING,
        RELEASE
    }
}
