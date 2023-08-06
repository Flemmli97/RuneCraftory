package io.github.flemmli97.runecraftory.api;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.platform.registry.CustomRegistryEntry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public abstract class Spell extends CustomRegistryEntry<Spell> {

    public static boolean tryUseWithCost(LivingEntity entity, ItemStack stack, int cost, EnumSkills... skills) {
        return !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, cost, stack.getItem() instanceof ItemStaffBase, false, true, skills)).orElse(false);
    }

    public void update(Player player, ItemStack stack) {
    }

    public void levelSkill(ServerPlayer player) {
        Map<EnumSkills, Float> skillXp = DataPackHandler.SERVER_PACK.spellPropertiesManager().getPropertiesFor(this).skillXP;
        if (!skillXp.isEmpty()) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data ->
                    skillXp.forEach((skill, xp) -> LevelCalc.levelSkill(player, data, EnumSkills.DARK, xp)));
        }
    }

    public int coolDown() {
        return DataPackHandler.SERVER_PACK.spellPropertiesManager().getPropertiesFor(this).cooldown;
    }

    public int rpCost() {
        return DataPackHandler.SERVER_PACK.spellPropertiesManager().getPropertiesFor(this).rpCost;
    }

    public boolean use(LivingEntity entity) {
        if (entity.level instanceof ServerLevel serverLevel)
            return this.use(serverLevel, entity, ItemStack.EMPTY);
        return false;
    }

    public boolean use(LivingEntity entity, boolean ignoreSeal) {
        if (entity.level instanceof ServerLevel serverLevel)
            return this.use(serverLevel, entity, ItemStack.EMPTY, ignoreSeal);
        return false;
    }

    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack) {
        return this.use(world, entity, stack, false);
    }

    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, boolean ignoreSeal) {
        if (!ignoreSeal && EntityUtils.sealed(entity))
            return false;
        return this.use(world, entity, stack, 1, 1, 1);
    }

    public abstract boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level);

    @Override
    public String toString() {
        return this.getRegistryName().toString();
    }
}
