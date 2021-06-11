package com.flemmli97.runecraftory.common.spells;

import com.flemmli97.runecraftory.api.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;

public class SnowballSpell extends Spell {

    @Override
    public void update(PlayerEntity player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayerEntity player) {

    }

    @Override
    public int coolDown() {
        return 5;
    }

    @Override
    public boolean use(ServerWorld world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        SnowballEntity snowball = new SnowballEntity(world, entity);
        snowball.setDirectionAndMovement(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, 2.0F, 1.0F);
        world.addEntity(snowball);
        return true;
    }

    @Override
    public int rpCost() {
        return 1;
    }
}
