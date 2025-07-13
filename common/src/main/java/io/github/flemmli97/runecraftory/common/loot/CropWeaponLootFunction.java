package io.github.flemmli97.runecraftory.common.loot;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.recipes.CraftingType;
import io.github.flemmli97.runecraftory.common.registry.ModDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.ModLootRegistries;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandData;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandHandler;
import net.minecraft.core.BlockPos;
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

    public static final MapCodec<CropWeaponLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance)
                    .apply(instance, CropWeaponLootFunction::new)
    );

    private CropWeaponLootFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }

    @Override
    public LootItemFunctionType<CropWeaponLootFunction> getType() {
        return ModLootRegistries.CROP_WEAPON_FUNCTION.get();
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext ctx) {
        if (!ItemNBT.shouldHaveStats(stack))
            return stack;
        boolean equipment = stack.is(RunecraftoryTags.Items.EQUIPMENT) && stack.getItem() instanceof ShieldItem;
        int level = 1;
        if (ctx.hasParam(LootCtxParameters.ITEM_LEVEL_CONTEXT))
            level = ctx.getParam(LootCtxParameters.ITEM_LEVEL_CONTEXT);
        else {
            if (ctx.hasParam(LootContextParams.BLOCK_STATE) && ctx.hasParam(LootContextParams.ORIGIN)) {
                BlockPos blockPos = BlockPos.containing(ctx.getParam(LootContextParams.ORIGIN));
                level = FarmlandHandler.get(ctx.getLevel().getServer())
                        .getData(ctx.getLevel(), blockPos)
                        .map(FarmlandData::getCropLevel).orElse(1);
            }
        }
        List<Pair<ItemStack, ItemStat>> base = DataPackHandler.INSTANCE.itemStatManager()
                .all(s -> !s.is(stack.getItem()) && equipment ? s.getItem() instanceof ShieldItem : s.is(RunecraftoryTags.Items.UPGRADABLE_HELD));
        if (!base.isEmpty()) {
            stack.set(ModDataComponentTypes.LIGHT_ORE.get(), true);
            ItemNBT.addUpgradeItem(stack, base.get(ctx.getRandom().nextInt(base.size())).getFirst(), true, equipment ? CraftingType.ACCESSORY_WORKBENCH : CraftingType.FORGE);
        }
        List<Pair<ItemStack, ItemStat>> bonus = DataPackHandler.INSTANCE.itemStatManager()
                .all(s -> !s.is(RunecraftoryTags.Items.WEAPONS) && !s.is(RunecraftoryTags.Items.EQUIPMENT));
        int bonusAmount = ctx.getRandom().nextInt(3) + 1;
        for (int i = 0; i < bonusAmount; i++)
            ItemNBT.addUpgradeItem(stack, bonus.get(ctx.getRandom().nextInt(bonus.size())).getFirst(), true, equipment ? CraftingType.ACCESSORY_WORKBENCH : CraftingType.FORGE);
        for (int i = 1; i < level; i++) {
            ItemNBT.addUpgradeItem(stack, bonus.get(ctx.getRandom().nextInt(bonus.size())).getFirst(), false, equipment ? CraftingType.ACCESSORY_WORKBENCH : CraftingType.FORGE);
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
}
