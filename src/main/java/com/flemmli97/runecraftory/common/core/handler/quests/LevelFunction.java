package com.flemmli97.runecraftory.common.core.handler.quests;

import java.util.Random;

import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class LevelFunction extends LootFunction
{
    private final RandomValueRange countRange;

    public LevelFunction(LootCondition[] conditionsIn, RandomValueRange countRangeIn)
    {
        super(conditionsIn);
        this.countRange = countRangeIn;
    }

    @Override
    public ItemStack apply(ItemStack stack, Random rand, LootContext context)
    {
        return ItemNBT.getLeveledItem(stack, this.countRange.generateInt(rand));
    }

    public static class Serializer extends LootFunction.Serializer<LevelFunction>
        {
            public Serializer()
            {
                super(new ResourceLocation(LibReference.MODID,"level"), LevelFunction.class);
            }

            @Override
            public void serialize(JsonObject object, LevelFunction functionClazz, JsonSerializationContext serializationContext)
            {
                object.add("level", serializationContext.serialize(functionClazz.countRange));
            }

            @Override
            public LevelFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn)
            {
                return new LevelFunction(conditionsIn, (RandomValueRange)JsonUtils.deserializeClass(object, "level", deserializationContext, RandomValueRange.class));
            }
        }
}