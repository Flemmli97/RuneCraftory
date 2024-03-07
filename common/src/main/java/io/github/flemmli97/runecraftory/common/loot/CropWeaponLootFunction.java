package io.github.flemmli97.runecraftory.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.lib.LibNBT;
import io.github.flemmli97.runecraftory.common.registry.ModLootRegistries;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandData;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class CropWeaponLootFunction extends LootItemConditionalFunction {

    private CropWeaponLootFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    public LootItemFunctionType getType() {
        return ModLootRegistries.CROP_WEAPON_FUNCTION.get();
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext ctx) {
        if (!ItemNBT.shouldHaveStats(stack))
            return stack;
        boolean equipment = stack.is(RunecraftoryTags.EQUIPMENT) && stack.getItem() instanceof ShieldItem;
        int level = 1;
        if (ctx.hasParam(LootCtxParameters.ITEM_LEVEL_CONTEXT))
            level = ctx.getParam(LootCtxParameters.ITEM_LEVEL_CONTEXT);
        else {
            if (ctx.hasParam(LootContextParams.BLOCK_STATE) && ctx.hasParam(LootContextParams.ORIGIN)) {
                BlockPos blockPos = new BlockPos(ctx.getParam(LootContextParams.ORIGIN));
                level = FarmlandHandler.get(ctx.getLevel().getServer())
                        .getData(ctx.getLevel(), blockPos)
                        .map(FarmlandData::getCropLevel).orElse(1);
            }
        }
        List<Pair<ItemStack, ItemStat>> base = DataPackHandler.INSTANCE.itemStatManager()
                .all(s -> !s.is(stack.getItem()) && equipment ? s.getItem() instanceof ShieldItem : s.is(RunecraftoryTags.UPGRADABLE_HELD));
        if (!base.isEmpty()) {
            CompoundTag tag = ItemNBT.getItemNBT(stack);
            if (tag == null)
                tag = new CompoundTag();
            tag.putBoolean(LibNBT.LIGHTORETAG, true);
            CompoundTag stackTag = stack.getOrCreateTag();
            stackTag.put(RuneCraftory.MODID, tag);
            ItemNBT.addUpgradeItem(stack, base.get(ctx.getRandom().nextInt(base.size())).getFirst(), true, equipment ? EnumCrafting.ARMOR : EnumCrafting.FORGE);
        }
        List<Pair<ItemStack, ItemStat>> bonus = DataPackHandler.INSTANCE.itemStatManager()
                .all(s -> !s.is(RunecraftoryTags.WEAPONS) && !s.is(RunecraftoryTags.EQUIPMENT));
        int bonusAmount = ctx.getRandom().nextInt(3) + 1;
        for (int i = 0; i < bonusAmount; i++)
            ItemNBT.addUpgradeItem(stack, bonus.get(ctx.getRandom().nextInt(bonus.size())).getFirst(), true, equipment ? EnumCrafting.ARMOR : EnumCrafting.FORGE);
        for (int i = 1; i < level; i++) {
            ItemNBT.addUpgradeItem(stack, bonus.get(ctx.getRandom().nextInt(bonus.size())).getFirst(), false, equipment ? EnumCrafting.ARMOR : EnumCrafting.FORGE);
        }
        return stack;
    }

    public static class Builder extends LootItemConditionalFunction.Builder<CropWeaponLootFunction.Builder> {

        @Override
        protected CropWeaponLootFunction.Builder getThis() {
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new CropWeaponLootFunction(this.getConditions());
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<CropWeaponLootFunction> {

        @Override
        public void serialize(JsonObject obj, CropWeaponLootFunction func, JsonSerializationContext context) {
            super.serialize(obj, func, context);
        }

        @Override
        public CropWeaponLootFunction deserialize(JsonObject obj, JsonDeserializationContext ctx, LootItemCondition[] conditions) {
            return new CropWeaponLootFunction(conditions);
        }
    }
}
