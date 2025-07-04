package io.github.flemmli97.runecraftory.common.inventory.container;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class IngredientsCache {

    private final Container container;
    private final ItemStack[] lastCache;

    public IngredientsCache(Container container) {
        this.container = container;
        this.lastCache = new ItemStack[this.container.getContainerSize()];
    }

    public boolean changedAndUpdate() {
        boolean changed = false;
        for (int i = 0; i < this.container.getContainerSize(); i++) {
            if (this.lastCache[i] == null || !ItemStack.isSameItemSameComponents(this.lastCache[i], this.container.getItem(i))) {
                changed = true;
                break;
            }
        }
        for (int i = 0; i < this.container.getContainerSize(); i++) {
            this.lastCache[i] = this.container.getItem(i).copy();
        }
        return changed;
    }
}
