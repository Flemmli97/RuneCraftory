package io.github.flemmli97.runecraftory.fabric.mixinhelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;

import java.util.List;
import java.util.function.Consumer;

public class LootModifier {

    public static void modify(LootTable table, List<ItemStack> list, LootContext ctx, ResourceLocation res, Consumer<ResourceLocation> cons) {
        if (ctx.getLevel() != null) {
            if (res == null) {
                LootTables tables = ctx.getLevel().getServer().getLootTables();
                for (ResourceLocation id : tables.getIds()) {
                    if (tables.get(id) == table)
                        res = id;
                }
                if (res != null) {
                    cons.accept(res);
                }
            }
            if (res != null) {
                //Do Stuff
            }
        }
    }
}
