package io.github.flemmli97.runecraftory.forge.data.tags;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModDamageType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagGen extends TagsProvider<DamageType> {

    public DamageTypeTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, RuneCraftory.MODID, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (ResourceKey<DamageType> type : ModDamageType.ATTACK_TYPES) {
            this.tag(type, DamageTypeTags.NO_KNOCKBACK, DamageTypeTags.PANIC_CAUSES, DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS);
        }
        this.projectiled(ModDamageType.PHYSICAL);
        this.projectiled(ModDamageType.IGNORE_DEFENCE, DamageTypeTags.BYPASSES_ARMOR);
        this.projectiled(ModDamageType.MAGIC, DamageTypeTags.BYPASSES_ARMOR, RunecraftoryTags.DamageTypes.IS_MAGIC);
        this.projectiled(ModDamageType.IGNORE_MAGIC_DEFENCE, DamageTypeTags.BYPASSES_ARMOR, RunecraftoryTags.DamageTypes.IS_MAGIC, RunecraftoryTags.DamageTypes.BYPASS_MAGIC,
                DamageTypeTags.BYPASSES_ENCHANTMENTS);

        this.tag(ModDamageType.TRUE_DAMAGE,
                DamageTypeTags.BYPASSES_ARMOR, RunecraftoryTags.DamageTypes.BYPASS_MAGIC,
                DamageTypeTags.BYPASSES_EFFECTS, DamageTypeTags.BYPASSES_RESISTANCE,
                DamageTypeTags.BYPASSES_ENCHANTMENTS, DamageTypeTags.BYPASSES_SHIELD);

        this.tag(ModDamageType.STRONG_POISON, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_EFFECTS,
                DamageTypeTags.BYPASSES_ENCHANTMENTS, DamageTypeTags.BYPASSES_SHIELD, DamageTypeTags.NO_KNOCKBACK);
        this.tag(ModDamageType.EXHAUST, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_EFFECTS,
                DamageTypeTags.BYPASSES_ENCHANTMENTS, DamageTypeTags.BYPASSES_SHIELD, DamageTypeTags.NO_KNOCKBACK);
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
        ResourceKey<DamageType> variant = ModDamageType.PROJECTILE_EQUIVALENT.get(key);
        if (variant != null) {
            for (TagKey<DamageType> tag : tags) {
                this.tag(tag).add(variant);
            }
            this.tag(DamageTypeTags.IS_PROJECTILE).add(variant);
        }
    }
}
