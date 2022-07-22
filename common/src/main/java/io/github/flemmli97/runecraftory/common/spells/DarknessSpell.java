package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkness;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DarknessSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.DARK, 1));
    }

    @Override
    public int coolDown() {
        return 50;
    }

    @Override
    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), false, true, true, 1, EnumSkills.DARK)).orElse(false);
        if (rp) {
            EntityDarkness darkness = new EntityDarkness(world, entity);
            darkness.setDamageMultiplier(1 + level * 0.1f);
            world.addFreshEntity(darkness);
            return true;
        }
        return false;
    }

    @Override
    public int rpCost() {
        return 10;
    }
}
