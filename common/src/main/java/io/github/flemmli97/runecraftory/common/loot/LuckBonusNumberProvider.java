package io.github.flemmli97.runecraftory.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.ModLootRegistries;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public record LuckBonusNumberProvider(NumberProvider base, float luck) implements NumberProvider {

    public static final MapCodec<LuckBonusNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(NumberProviders.CODEC.fieldOf("base").forGetter(LuckBonusNumberProvider::base),
                    Codec.FLOAT.fieldOf("luck").forGetter(LuckBonusNumberProvider::luck)
            ).apply(instance, LuckBonusNumberProvider::new)
    );

    @Override
    public float getFloat(LootContext lootContext) {
        return this.base.getFloat(lootContext) + this.luck * lootContext.getLuck();
    }

    @Override
    public LootNumberProviderType getType() {
        return ModLootRegistries.LUCK_BOOSTED.get();
    }
}
