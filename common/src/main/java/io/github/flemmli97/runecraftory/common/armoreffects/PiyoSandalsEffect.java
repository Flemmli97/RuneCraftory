package io.github.flemmli97.runecraftory.common.armoreffects;

import io.github.flemmli97.runecraftory.api.registry.ArmorEffect;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class PiyoSandalsEffect extends ArmorEffect {

    @Override
    public boolean canBeAppliedTo(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armor && armor.getSlot() == EquipmentSlot.FEET;
    }

    @Override
    public void onStep(LivingEntity entity, ItemStack stack) {
        entity.playSound(ModSounds.PLAYER_ARMOR_PIYO_CHIRP.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
    }
}
