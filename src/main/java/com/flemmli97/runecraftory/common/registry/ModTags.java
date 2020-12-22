package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {

    public static Tags.IOptionalNamedTag<Item> bronze = forge("ingots/bronze");
    public static Tags.IOptionalNamedTag<Item> silver = forge("ingots/silver");
    public static Tags.IOptionalNamedTag<Item> platinum = forge("ingots/platinum");
    public static Tags.IOptionalNamedTag<Item> orichalcum = forge("gems/orichalcum");
    public static Tags.IOptionalNamedTag<Item> dragonic = forge("gems/dragonic");

    public static Tags.IOptionalNamedTag<Item> minerals = tag("mineral");

    private static Tags.IOptionalNamedTag<Item> tag(String name) {
        return ItemTags.createOptional(new ResourceLocation(RuneCraftory.MODID, name));
    }

    private static Tags.IOptionalNamedTag<Item> forge(String name) {
        return ItemTags.createOptional(new ResourceLocation("forge", name));
    }
}
