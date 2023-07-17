package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.DelayedAttacker;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class WaterLaserSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.WATER, 6));
    }

    @Override
    public int coolDown() {
        return 40;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true, EnumSkills.WATER)).orElse(false);
        if (!rp)
            return false;
        EntityWaterLaser laser = new EntityWaterLaser(level, entity);
        laser.setMaxTicks(entity instanceof Player ? 44 : 15);
        laser.setDamageMultiplier(0.95f + lvl * 0.05f);
        if (entity instanceof Mob mob) {
            Vec3 delayedPos;
            if (entity instanceof DelayedAttacker attacker && (delayedPos = attacker.targetPosition(laser.position())) != null) {
                laser.setRotationTo(delayedPos.x(), delayedPos.y(), delayedPos.z(), 0);
            } else if (mob.getTarget() != null) {
                LivingEntity target = mob.getTarget();
                laser.setRotationTo(target, 0);
            }
        }
        level.addFreshEntity(laser);
        return true;
    }

    @Override
    public int rpCost() {
        return 10;
    }
}
