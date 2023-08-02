package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySleepAura;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SleepAuraSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
    }

    @Override
    public int coolDown() {
        return 30;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true, EnumSkills.DARK)).orElse(false);
        if (!rp)
            return false;
        EntitySleepAura aura = new EntitySleepAura(level, entity);
        aura.setDamageMultiplier(0.7f + lvl * 0.075f);
        level.addFreshEntity(aura);
        return true;
    }

    @Override
    public int rpCost() {
        return 30;
    }
}