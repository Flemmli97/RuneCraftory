package com.flemmli97.runecraftory.common.spells;

import com.flemmli97.runecraftory.api.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public class WitherSkullSpell extends Spell {

    @Override
    public void update(PlayerEntity player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayerEntity player) {

    }

    @Override
    public int coolDown() {
        return 15;
    }

    @Override
    public boolean use(ServerWorld world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount) {
        Vector3d look = entity.getLookVec();
        WitherSkullEntity skull = new WitherSkullEntity(world, entity, look.x, look.y, look.z);
        skull.setPosition(entity.getX(), entity.getEyeY(), entity.getZ());
        world.addEntity(skull);
        return true;
    }

    @Override
    public int rpCost() {
        return 1;
    }
}
