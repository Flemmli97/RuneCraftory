package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDamageType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.concurrent.CompletableFuture;

public class DamageTypeGen extends JsonCodecProvider<DamageType> {

    public DamageTypeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, PackOutput.Target.DATA_PACK, Registries.DAMAGE_TYPE.location().getPath(), PackType.SERVER_DATA, DamageType.DIRECT_CODEC,
                lookupProvider, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void gather() {
        for (ResourceKey<DamageType> types : RuneCraftoryDamageType.ATTACK_TYPES) {
            this.unconditional(types.location(), new DamageType(
                    types.location().toLanguageKey(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1f, DamageEffects.HURT));
        }

        this.unconditional(RuneCraftoryDamageType.EXHAUST.location(), new DamageType(
                RuneCraftoryDamageType.EXHAUST.location().toLanguageKey(), DamageScaling.NEVER, 0.1f, DamageEffects.HURT));
        this.unconditional(RuneCraftoryDamageType.STRONG_POISON.location(), new DamageType(
                RuneCraftoryDamageType.STRONG_POISON.location().toLanguageKey(), DamageScaling.NEVER, 0.2f, DamageEffects.HURT));
    }
}
