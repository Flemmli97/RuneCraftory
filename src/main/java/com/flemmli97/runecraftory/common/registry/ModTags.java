package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
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

    public static Tags.IOptionalNamedTag<Block> farmlandTill = blockForge("farmland_tillable");
    public static Tags.IOptionalNamedTag<Block> sickleDestroyable = blockForge("sickle_destroyable");
    public static Tags.IOptionalNamedTag<Block> hammerFlattenable = blockForge("hammer_flattenable");
    public static Tags.IOptionalNamedTag<Block> hammerBreakable = blockForge("hammer_breakable");
    public static Tags.IOptionalNamedTag<Block> herbs = blockForge("herbs");

    private static Tags.IOptionalNamedTag<Item> tag(String name) {
        return ItemTags.createOptional(new ResourceLocation(RuneCraftory.MODID, name));
    }

    private static Tags.IOptionalNamedTag<Item> forge(String name) {
        return ItemTags.createOptional(new ResourceLocation("forge", name));
    }

    private static Tags.IOptionalNamedTag<Block> blockForge(String name) {
        return BlockTags.createOptional(new ResourceLocation("forge", name));
    }
}
