package io.github.flemmli97.runecraftory.common.spells;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TripleWaterLaserSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.WATER, 1.5f));
    }

    @Override
    public int coolDown() {
        return 50;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true, EnumSkills.WATER)).orElse(false);
        if (!rp)
            return false;
        for (int i = -1; i < 2; i++) {
            float posYawOff = i * 130;
            Vector3f vec = RayTraceUtils.rotatedAround(entity.getLookAngle(), Vector3f.YP, posYawOff);
            EntityWaterLaser laser = new EntityWaterLaser(level, entity);
            laser.setPos(laser.getX() + vec.x(), laser.getY() + vec.y(), laser.getZ() + vec.z());
            laser.setMaxTicks(entity instanceof Player ? 44 : 15);
            laser.setDamageMultiplier(0.95f + lvl * 0.05f);
            laser.setYawOffset(-i * 130);
            laser.setPositionYawOffset(posYawOff);
            level.addFreshEntity(laser);
        }
        return true;
    }

    @Override
    public int rpCost() {
        return 40;
    }
}
