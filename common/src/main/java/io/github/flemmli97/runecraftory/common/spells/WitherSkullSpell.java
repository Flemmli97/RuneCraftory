package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class WitherSkullSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {

    }

    @Override
    public int coolDown() {
        return 15;
    }

    @Override
    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        Vec3 look = entity.getLookAngle();
        WitherSkull skull = new WitherSkull(world, entity, look.x, look.y, look.z);
        skull.setPos(entity.getX(), entity.getEyeY(), entity.getZ());
        world.addFreshEntity(skull);
        return true;
    }

    @Override
    public int rpCost() {
        return 1;
    }
}
