package io.github.flemmli97.runecraftory.common.recipes;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public enum CraftingType {

    FORGE("forge", Skills.FORGING, RunecraftoryTags.Items.FORGING_BLACKLIST),
    ACCESSORY_WORKBENCH("accessory_workbench", Skills.CRAFTING, RunecraftoryTags.Items.ACCESSORY_BLACKLIST),
    CHEMISTRY_SET("chemistry_set", Skills.CHEMISTRY, RunecraftoryTags.Items.CHEMISTRY_BLACKLIST),
    COOKING_TABLE("cooking_table", Skills.COOKING, RunecraftoryTags.Items.COOKING_BLACKLIST);

    private final String id;
    public final Skills skill;
    public final TagKey<Item> upgradeBlacklist;

    CraftingType(String translation, Skills skill, TagKey<Item> upgradeBlacklist) {
        this.id = translation;
        this.skill = skill;
        this.upgradeBlacklist = upgradeBlacklist;
    }

    public String getId() {
        return this.id;
    }
}
