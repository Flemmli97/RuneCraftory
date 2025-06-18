package io.github.flemmli97.runecraftory.forge.data.tags;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.concurrent.CompletableFuture;

public class AttributeTagGen extends TagsProvider<Attribute> {

    public AttributeTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.ATTRIBUTE, lookupProvider, RuneCraftory.MODID, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(RunecraftoryTags.Attributes.WEAPON_ONLY)
                .add(ModAttributes.PARALYSIS.getKey(),
                        ModAttributes.POISON.getKey(),
                        ModAttributes.SEAL.getKey(),
                        ModAttributes.SLEEP.getKey(),
                        ModAttributes.FATIGUE.getKey(),
                        ModAttributes.COLD.getKey(),
                        ModAttributes.FAINT.getKey(),
                        ModAttributes.DRAIN.getKey());
        this.tag(RunecraftoryTags.Attributes.NON_INHERITABLE)
                .add(ModAttributes.WATER_RESISTANCE.getKey(),
                        ModAttributes.EARTH_RESISTANCE.getKey(),
                        ModAttributes.WIND_RESISTANCE.getKey(),
                        ModAttributes.FIRE_RESISTANCE.getKey(),
                        ModAttributes.DARK_RESISTANCE.getKey(),
                        ModAttributes.LIGHT_RESISTANCE.getKey(),
                        ModAttributes.LOVE_RESISTANCE.getKey(),
                        ModAttributes.PARALYSIS_RESISTANCE.getKey(),
                        ModAttributes.POISON_RESISTANCE.getKey(),
                        ModAttributes.SEAL_RESISTANCE.getKey(),
                        ModAttributes.SLEEP_RESISTANCE.getKey(),
                        ModAttributes.FATIGUE_RESISTANCE.getKey(),
                        ModAttributes.COLD_RESISTANCE.getKey(),
                        ModAttributes.DIZZY_RESISTANCE.getKey(),
                        ModAttributes.CRITICAL_RESISTANCE.getKey(),
                        ModAttributes.STUN_RESISTANCE.getKey(),
                        ModAttributes.FAINT_RESISTANCE.getKey(),
                        ModAttributes.DRAIN_RESISTANCE.getKey());
        this.tag(RunecraftoryTags.Attributes.NON_INHERITABLE)
                .add(ModAttributes.ATTACK_SPEED.getKey(),
                        ModAttributes.ATTACK_RANGE.getKey(),
                        ModAttributes.ATTACK_WIDTH.getKey(),
                        ModAttributes.CHARGE_TIME.getKey());
        this.tag(RunecraftoryTags.Attributes.PERCENTAGE_DISPLAY)
                .add(ModAttributes.PARALYSIS.getKey(),
                        ModAttributes.POISON.getKey(),
                        ModAttributes.SEAL.getKey(),
                        ModAttributes.SLEEP.getKey(),
                        ModAttributes.FATIGUE.getKey(),
                        ModAttributes.COLD.getKey(),
                        ModAttributes.CRITICAL.getKey(),
                        ModAttributes.STUN.getKey(),
                        ModAttributes.FAINT.getKey(),
                        ModAttributes.DRAIN.getKey(),
                        ModAttributes.KNOCKOUT.getKey(),

                        ModAttributes.WATER_RESISTANCE.getKey(),
                        ModAttributes.EARTH_RESISTANCE.getKey(),
                        ModAttributes.WIND_RESISTANCE.getKey(),
                        ModAttributes.FIRE_RESISTANCE.getKey(),
                        ModAttributes.DARK_RESISTANCE.getKey(),
                        ModAttributes.LIGHT_RESISTANCE.getKey(),
                        ModAttributes.LOVE_RESISTANCE.getKey(),

                        ModAttributes.PARALYSIS_RESISTANCE.getKey(),
                        ModAttributes.POISON_RESISTANCE.getKey(),
                        ModAttributes.SEAL_RESISTANCE.getKey(),
                        ModAttributes.SLEEP_RESISTANCE.getKey(),
                        ModAttributes.FATIGUE_RESISTANCE.getKey(),
                        ModAttributes.COLD_RESISTANCE.getKey(),
                        ModAttributes.CRITICAL_RESISTANCE.getKey(),
                        ModAttributes.STUN_RESISTANCE.getKey(),
                        ModAttributes.FAINT_RESISTANCE.getKey(),
                        ModAttributes.DRAIN_RESISTANCE.getKey(),
                        Attributes.KNOCKBACK_RESISTANCE.unwrapKey().get());
        this.tag(RunecraftoryTags.Attributes.DISPLAY_IGNORED)
                .add(ModAttributes.ATTACK_SPEED.getKey(),
                        ModAttributes.ATTACK_RANGE.getKey(),
                        ModAttributes.HEALTH_GAIN.getKey(),
                        ModAttributes.RUNE_POINTS_GAIN.getKey());
    }
}