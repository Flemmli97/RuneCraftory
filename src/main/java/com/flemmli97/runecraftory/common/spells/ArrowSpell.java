package com.flemmli97.runecraftory.common.spells;

import com.flemmli97.runecraftory.api.Spell;
import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
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
        return 20;
    }

    @Override
    public boolean use(ServerWorld world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        ArrowEntity arrowentity = new ArrowEntity(world, entity);
        arrowentity.setProperties(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, 1.0F, 1.0F);
        arrowentity.setDamage(CombatUtils.getAttributeValue(entity, Attributes.GENERIC_ATTACK_DAMAGE, null) * 0.05 * level);
        arrowentity.setFire(ItemNBT.getElement(stack) == EnumElement.FIRE ? 200 : 0);
        world.addEntity(arrowentity);
        return true;
    }

    @Override
    public int rpCost() {
        return 5;
    }
}
