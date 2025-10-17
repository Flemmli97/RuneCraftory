package io.github.flemmli97.runecraftory.neoforge.data.tags;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
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
                .add(RuneCraftoryAttributes.PARALYSIS.getKey(),
                        RuneCraftoryAttributes.POISON.getKey(),
                        RuneCraftoryAttributes.SEAL.getKey(),
                        RuneCraftoryAttributes.SLEEP.getKey(),
                        RuneCraftoryAttributes.FATIGUE.getKey(),
                        RuneCraftoryAttributes.COLD.getKey(),
                        RuneCraftoryAttributes.FAINT.getKey(),
                        RuneCraftoryAttributes.DRAIN.getKey());
        this.tag(RunecraftoryTags.Attributes.NON_INHERITABLE)
                .add(RuneCraftoryAttributes.WATER_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.EARTH_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.WIND_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.FIRE_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.DARK_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.LIGHT_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.LOVE_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.PARALYSIS_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.POISON_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.SEAL_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.SLEEP_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.FATIGUE_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.COLD_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.DIZZY_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.CRITICAL_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.STUN_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.FAINT_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.DRAIN_RESISTANCE.getKey());
        this.tag(RunecraftoryTags.Attributes.NON_INHERITABLE)
                .add(RuneCraftoryAttributes.ATTACK_SPEED.getKey(),
                        RuneCraftoryAttributes.ATTACK_RANGE.getKey(),
                        RuneCraftoryAttributes.ATTACK_WIDTH.getKey(),
                        RuneCraftoryAttributes.CHARGE_TIME.getKey());
        this.tag(RunecraftoryTags.Attributes.PERCENTAGE_DISPLAY)
                .add(RuneCraftoryAttributes.PARALYSIS.getKey(),
                        RuneCraftoryAttributes.POISON.getKey(),
                        RuneCraftoryAttributes.SEAL.getKey(),
                        RuneCraftoryAttributes.SLEEP.getKey(),
                        RuneCraftoryAttributes.FATIGUE.getKey(),
                        RuneCraftoryAttributes.COLD.getKey(),
                        RuneCraftoryAttributes.CRITICAL.getKey(),
                        RuneCraftoryAttributes.STUN.getKey(),
                        RuneCraftoryAttributes.FAINT.getKey(),
                        RuneCraftoryAttributes.DRAIN.getKey(),

                        RuneCraftoryAttributes.WATER_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.EARTH_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.WIND_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.FIRE_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.DARK_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.LIGHT_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.LOVE_RESISTANCE.getKey(),

                        RuneCraftoryAttributes.PARALYSIS_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.POISON_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.SEAL_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.SLEEP_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.FATIGUE_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.COLD_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.CRITICAL_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.STUN_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.FAINT_RESISTANCE.getKey(),
                        RuneCraftoryAttributes.DRAIN_RESISTANCE.getKey(),
                        Attributes.KNOCKBACK_RESISTANCE.unwrapKey().get());
        this.tag(RunecraftoryTags.Attributes.DISPLAY_IGNORED)
                .add(RuneCraftoryAttributes.ATTACK_SPEED.getKey(),
                        RuneCraftoryAttributes.ATTACK_RANGE.getKey(),
                        RuneCraftoryAttributes.ATTACK_WIDTH.getKey(),
                        RuneCraftoryAttributes.CHARGE_TIME.getKey(),
                        RuneCraftoryAttributes.HEALTH_GAIN.getKey(),
                        RuneCraftoryAttributes.RUNE_POINTS_GAIN.getKey());
        this.tag(RunecraftoryTags.Attributes.RANDOMIZABLE_ATTRIBUTES)
                .add(Attributes.MAX_HEALTH.unwrapKey().get(),
                        Attributes.ATTACK_DAMAGE.unwrapKey().get(),
                        RuneCraftoryAttributes.DEFENCE.getKey(),
                        RuneCraftoryAttributes.MAGIC_ATTACK.getKey(),
                        RuneCraftoryAttributes.MAGIC_DEFENCE.getKey());
    }
}