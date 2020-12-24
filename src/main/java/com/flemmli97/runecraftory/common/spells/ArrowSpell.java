package com.flemmli97.runecraftory.common.spells;

import com.flemmli97.runecraftory.api.Spell;
import com.flemmli97.runecraftory.common.utils.MobUtils;
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
    public boolean use(ServerWorld world, LivingEntity entity, ItemStack stack) {
        ArrowEntity arrowentity = new ArrowEntity(world, entity);
        arrowentity.setProperties(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, 3.0F, 1.0F);
        arrowentity.setDamage(MobUtils.getAttributeValue(entity, Attributes.GENERIC_ATTACK_DAMAGE, null));
        arrowentity.setFire(200);
        world.addEntity(arrowentity);
        return true;
    }

    @Override
    public int rpCost() {
        return 0;
    }
}
