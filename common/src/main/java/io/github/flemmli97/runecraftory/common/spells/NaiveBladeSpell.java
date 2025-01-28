package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class NaiveBladeSpell extends WeaponSpell {

    public NaiveBladeSpell() {
        super(ModAttackActions.NAIVE_BLADE, RunecraftoryTags.LONGSWORDS);
    }

    @Override
    public boolean canUse(ServerLevel world, LivingEntity entity, ItemStack stack) {
        if (entity instanceof Player player && Platform.INSTANCE.getPlayerData(player)
                .map(d -> d.getWeaponHandler().getCurrentAction() == this.useAction()).orElse(false))
            return false;
        return super.canUse(world, entity, stack);
    }
}