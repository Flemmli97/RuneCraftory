package io.github.flemmli97.runecraftory.fabric.mixinhelper;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.ArrayList;
import java.util.List;

public class ItemStackAttributeHelper {

    public static ItemAttributeModifiers modifyData(ItemStack stack, ItemAttributeModifiers data) {
        List<ItemAttributeModifiers.Entry> entries = new ArrayList<>(data.modifiers());
        boolean[] modified = {false};
        ItemNBT.modifyAttribute(stack, entry -> {
            entries.add(entry);
            modified[0] = true;
        }, entry -> {
            entries.remove(entry);
            modified[0] = true;
        });
        if (modified[0]) {
            return new ItemAttributeModifiers(ImmutableList.copyOf(entries), data.showInTooltip());
        }
        return data;
    }
}
