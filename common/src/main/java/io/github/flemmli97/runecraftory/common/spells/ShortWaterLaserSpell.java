package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ShortWaterLaserSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.WATER, 1));
    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), false, true, true, 1, EnumSkills.WATER)).orElse(false);
        if (rp) {
            EntityWaterLaser laser = new EntityWaterLaser(world, entity);
            laser.setMaxTicks(15);
            laser.setDamageMultiplier(1 + (level - 1) / 10);
            if (entity instanceof Mob mob && mob.getTarget() != null) {
                LivingEntity target = mob.getTarget();
                laser.setRotationTo(target.getX(), target.getEyeY(), target.getZ(), 0);
            }
            world.addFreshEntity(laser);
            return true;
        }
        return false;
    }

    @Override
    public int rpCost() {
        return 10;
    }
}
