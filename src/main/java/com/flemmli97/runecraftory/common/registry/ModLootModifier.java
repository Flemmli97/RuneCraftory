package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.loot.CropLootModifier;
import com.flemmli97.runecraftory.common.loot.GiantLootCondition;
import com.flemmli97.runecraftory.common.loot.ItemLevelLootFunction;
import com.flemmli97.runecraftory.common.loot.MiningLootCondition;
import com.flemmli97.runecraftory.common.loot.VanillaDropCondition;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModLootModifier {

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> SERIALZER = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, RuneCraftory.MODID);

    public static final RegistryObject<CropLootModifier.Serializer> crop = SERIALZER.register("crop_modifier", CropLootModifier.Serializer::new);

    public static LootConditionType INT_CHECK;
    public static LootConditionType GIANTCROP;
    public static LootConditionType VANILLADROP;

    public static LootFunctionType ITEM_LEVEL;

    public static void register(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        INT_CHECK = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(RuneCraftory.MODID, "mining_check"), new LootConditionType(new MiningLootCondition.Serializer()));
        GIANTCROP = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(RuneCraftory.MODID, "crop_giant"), new LootConditionType(new GiantLootCondition.Serializer()));
        VANILLADROP = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(RuneCraftory.MODID, "drop_vanilla"), new LootConditionType(new VanillaDropCondition.Serializer()));

        ITEM_LEVEL = Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(RuneCraftory.MODID, "item_level"), new LootFunctionType(new ItemLevelLootFunction.Serializer()));
    }

}
