package io.github.flemmli97.runecraftory.neoforge.data.tags;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDamageType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagGen extends TagsProvider<DamageType> {

    public DamageTypeTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, RuneCraftory.MODID, fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (ResourceKey<DamageType> type : RuneCraftoryDamageType.ATTACK_TYPES) {
            this.tag(type, DamageTypeTags.NO_KNOCKBACK, DamageTypeTags.PANIC_CAUSES, DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS);
        }
        this.projectiled(RuneCraftoryDamageType.PHYSICAL);
        this.projectiled(RuneCraftoryDamageType.IGNORE_DEFENCE, DamageTypeTags.BYPASSES_ARMOR);
        this.projectiled(RuneCraftoryDamageType.MAGIC, DamageTypeTags.BYPASSES_ARMOR, RunecraftoryTags.DamageTypes.IS_MAGIC);
        this.projectiled(RuneCraftoryDamageType.IGNORE_MAGIC_DEFENCE, DamageTypeTags.BYPASSES_ARMOR, RunecraftoryTags.DamageTypes.IS_MAGIC, RunecraftoryTags.DamageTypes.BYPASS_MAGIC,
                DamageTypeTags.BYPASSES_ENCHANTMENTS);

        this.tag(RuneCraftoryDamageType.TRUE_DAMAGE,
                DamageTypeTags.BYPASSES_ARMOR, RunecraftoryTags.DamageTypes.BYPASS_MAGIC,
                DamageTypeTags.BYPASSES_EFFECTS, DamageTypeTags.BYPASSES_RESISTANCE,
                DamageTypeTags.BYPASSES_ENCHANTMENTS, DamageTypeTags.BYPASSES_SHIELD);

        this.tag(RuneCraftoryDamageType.STRONG_POISON, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_EFFECTS,
                DamageTypeTags.BYPASSES_ENCHANTMENTS, DamageTypeTags.BYPASSES_SHIELD, DamageTypeTags.NO_KNOCKBACK);
        this.tag(RuneCraftoryDamageType.EXHAUST, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_EFFECTS,
                DamageTypeTags.BYPASSES_ENCHANTMENTS, DamageTypeTags.BYPASSES_SHIELD, DamageTypeTags.NO_KNOCKBACK);

        this.tag(Tags.DamageTypes.IS_POISON)
                .add(RuneCraftoryDamageType.STRONG_POISON);

        this.tag(Tags.DamageTypes.IS_MAGIC)
                .addTag(RunecraftoryTags.DamageTypes.IS_MAGIC);
    }

    @SafeVarargs
    protected final void tag(ResourceKey<DamageType> key, TagKey<DamageType>... tags) {
        for (TagKey<DamageType> tag : tags) {
            this.tag(tag).add(key);
        }
    }

    @SafeVarargs
    protected final void projectiled(ResourceKey<DamageType> key, TagKey<DamageType>... tags) {
        for (TagKey<DamageType> tag : tags) {
            this.tag(tag).add(key);
        }
        ResourceKey<DamageType> variant = RuneCraftoryDamageType.PROJECTILE_EQUIVALENT.get(key);
        if (variant != null) {
            for (TagKey<DamageType> tag : tags) {
                this.tag(tag).add(variant);
            }
            this.tag(DamageTypeTags.IS_PROJECTILE).add(variant);
        }
    }
}
