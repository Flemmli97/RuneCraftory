package io.github.flemmli97.runecraftory.api.enums;

import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public enum CraftingType {

    FORGE("forge", EnumSkills.FORGING, RunecraftoryTags.Items.FORGING_BLACKLIST),
    ACCESSORY_WORKBENCH("accessory_workbench", EnumSkills.CRAFTING, RunecraftoryTags.Items.ACCESSORY_BLACKLIST),
    CHEMISTRY_SET("chemistry_set", EnumSkills.CHEMISTRY, RunecraftoryTags.Items.CHEMISTRY_BLACKLIST),
    COOKING_TABLE("cooking_table", EnumSkills.COOKING, RunecraftoryTags.Items.COOKING_BLACKLIST);

    private final String id;
    public final EnumSkills skill;
    public final TagKey<Item> upgradeBlacklist;

    CraftingType(String translation, EnumSkills skill, TagKey<Item> upgradeBlacklist) {
        this.id = translation;
        this.skill = skill;
        this.upgradeBlacklist = upgradeBlacklist;
    }

    public String getId() {
        return this.id;
    }
}
