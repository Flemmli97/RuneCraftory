package io.github.flemmli97.runecraftory.api;

import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.platform.registry.CustomRegistryEntry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class Spell extends CustomRegistryEntry<Spell> {

    public abstract void update(Player player, ItemStack stack);

    public abstract void levelSkill(ServerPlayer player);

    public abstract int coolDown();

    public boolean use(LivingEntity entity) {
        if(entity.level instanceof ServerLevel serverLevel)
            return this.use(serverLevel, entity, ItemStack.EMPTY, 1, 1, 1);
        return false;
    }

    public boolean use(ServerLevel world, LivingEntity entity) {
        return this.use(world, entity, ItemStack.EMPTY, 1, 1, 1);
    }

    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack) {
        if (EntityUtils.sealed(entity))
            return false;
        return this.use(world, entity, stack, 1, 1, 1);
    }

    public abstract boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level);

    public abstract int rpCost();

    @Override
    public String toString() {
        return this.getRegistryName().toString();
    }
}
