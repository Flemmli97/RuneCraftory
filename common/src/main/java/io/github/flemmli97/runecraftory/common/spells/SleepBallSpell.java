package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityAmbrosiaSleep;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SleepBallSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {

    }

    @Override
    public int coolDown() {
        return 80;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true, EnumSkills.EARTH)).orElse(false);
        if (!rp)
            return false;
        for (int i = 0; i < 4; ++i) {
            double angle = i / 4.0 * Math.PI * 2.0 + Math.toRadians(entity.getYRot());
            double x = Math.cos(angle) * 1.3;
            double z = Math.sin(angle) * 1.3;
            EntityAmbrosiaSleep pollen = new EntityAmbrosiaSleep(level, entity);
            pollen.setDamageMultiplier(0.55f + lvl * 0.05f);
            pollen.setPos(entity.getX() + x, entity.getY() + 0.4, entity.getZ() + z);
            level.addFreshEntity(pollen);
        }
        return true;
    }

    @Override
    public int rpCost() {
        return 50;
    }
}
