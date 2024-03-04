package io.github.flemmli97.runecraftory.forge.loot;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LootTableInjectModifier extends LootModifier {
    private final ResourceLocation table;

    public LootTableInjectModifier(LootItemCondition[] conditions, ResourceLocation table) {
        super(conditions);
        this.table = table;
    }

    @Override
    @NotNull
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        LootTable lootTable = context.getLootTable(this.table);
        lootTable.getRandomItemsRaw(context, generatedLoot::add);
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<LootTableInjectModifier> {

        @Override
        public LootTableInjectModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] lootConditions) {
            return new LootTableInjectModifier(lootConditions, new ResourceLocation(GsonHelper.getAsString(object, "loot_table")));
        }

        @Override
        public JsonObject write(LootTableInjectModifier modifier) {
            JsonObject json = this.makeConditions(modifier.conditions);
            json.addProperty("loot_table", modifier.table.toString());
            return json;
        }
    }
}
