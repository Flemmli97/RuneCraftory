package io.github.flemmli97.runecraftory.common.armoreffects;

import io.github.flemmli97.runecraftory.api.registry.ArmorEffect;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryArmorEffects;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SimpleAccessoryEffect extends ArmorEffect {

    @Override
    public boolean canBeAppliedTo(ItemStack stack) {
        return stack.is(RunecraftoryTags.Items.ACCESSORIES);
    }

    @Override
    public void onEquip(LivingEntity entity, ItemStack stack) {
        Platform.INSTANCE.getEntityData(entity).addArmorFlag(RuneCraftoryArmorEffects.ARMOR_EFFECTS.registry().wrapAsHolder(this));
    }

    @Override
    public void onRemove(LivingEntity entity, ItemStack stack) {
        Platform.INSTANCE.getEntityData(entity).removeArmorFlag(RuneCraftoryArmorEffects.ARMOR_EFFECTS.registry().wrapAsHolder(this));
    }
}
