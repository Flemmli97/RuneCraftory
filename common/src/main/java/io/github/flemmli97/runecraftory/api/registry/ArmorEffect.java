package io.github.flemmli97.runecraftory.api.registry;

import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class ArmorEffect {

    public static boolean hasArmorEffect(LivingEntity entity, Holder<ArmorEffect> effect) {
        return RunecraftoryAttachments.ENTITY_DATA.get().get(entity).hasArmorFlag(effect);
    }

    public static void runArmorEffectFor(ItemStack stack, Consumer<ArmorEffect> cons) {
        if (stack.isEmpty())
            return;
        if (stack.has(RuneCraftoryDataComponentTypes.ARMOR_EFFECT.get())) {
            stack.get(RuneCraftoryDataComponentTypes.ARMOR_EFFECT.get()).triggerEvent(stack, cons);
            return;
        }
        DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).flatMap(ItemStat::getArmorEffect).ifPresent(eff -> cons.accept(eff.value()));
    }

    public boolean canBeAppliedTo(ItemStack stack) {
        return true;
    }

    public void onStep(LivingEntity entity, ItemStack stack) {

    }

    public void onTick(LivingEntity entity, ItemStack stack) {

    }

    public void onEquip(LivingEntity entity, ItemStack stack) {

    }

    public void onRemove(LivingEntity entity, ItemStack stack) {

    }
}
