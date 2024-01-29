package io.github.flemmli97.runecraftory.forge.loot;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModGlobalLootModifiers {

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, RuneCraftory.MODID);

    public static RegistryObject<GlobalLootModifierSerializer<LootTableInjectModifier>> TABLE_REFERENCE = MODIFIERS.register("loot_table_inject", LootTableInjectModifier.Serializer::new);
}
