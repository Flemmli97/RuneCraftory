package com.flemmli97.runecraftory.common.spells;

import com.flemmli97.runecraftory.api.Spell;
import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;

public class ArrowSpell extends Spell {

    @Override
    public void update(PlayerEntity player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayerEntity player) {

    }

    @Override
    public int coolDown() {
        return 1;
    }

    @Override
    public boolean use(ServerWorld world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        ArrowEntity arrowentity = new ArrowEntity(world, entity);
        float f = 1;
        if (stack.getItem() instanceof ItemStaffBase)
            f = BowItem.getArrowVelocity(72000 - entity.getItemInUseCount());
        arrowentity.setDirectionAndMovement(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, f * 1.5F, 1.0F);
        arrowentity.setDamage(CombatUtils.getAttributeValueRaw(entity, Attributes.ATTACK_DAMAGE) * 0.05 * level);
        arrowentity.setFire(ItemNBT.getElement(stack) == EnumElement.FIRE ? 200 : 0);
        arrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.DISALLOWED;
        world.addEntity(arrowentity);
        return true;
    }

    @Override
    public int rpCost() {
        return 5;
    }
}
