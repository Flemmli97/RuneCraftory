package io.github.flemmli97.runecraftory.neoforge.data.tags;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEffects;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class MobEffectTagGen extends TagsProvider<MobEffect> {

    public static final TagKey<MobEffect> CATACLYSM_BOSS_EFFECTIVE = TagKey.create(Registries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath("cataclysm", "effective_for_bosses"));

    public MobEffectTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, ExistingFileHelper existingFileHelper) {
        super(output, Registries.MOB_EFFECT, completableFuture, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(CATACLYSM_BOSS_EFFECTIVE)
                .add(RuneCraftoryEffects.SLEEP.getKey())
                .add(RuneCraftoryEffects.STUNNED.getKey());
    }
}
