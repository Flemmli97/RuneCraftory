package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.fabric.loot.CropLootModifiers;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.LootTableID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(LootTable.class)
public abstract class LootTableMixin implements LootTableID {

    @Unique
    private ResourceLocation lootTableID;

    @ModifyVariable(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Ljava/util/List;", at = @At("RETURN"))
    private List<ItemStack> modify(List<ItemStack> list, LootContext ctx) {
        CropLootModifiers.modify((LootTable) (Object) this, list, ctx, this.getLootTableId());
        return list;
    }

    @Override
    public ResourceLocation getLootTableId() {
        return this.lootTableID;
    }

    @Override
    public void setLootTableId(ResourceLocation id) {
        this.lootTableID = id;
    }
}
