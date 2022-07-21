package io.github.flemmli97.runecraftory.common.spells;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DoubleWaterLaserSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.WATER, 1));
    }

    @Override
    public int coolDown() {
        return 50;
    }

    @Override
    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), false, true, true, 1, EnumSkills.WATER)).orElse(false);
        if (rp) {
            for (int i = 0; i < 2; i++) {
                float posYawOff = (i == 0 ? -1 : 1) * 30;
                Vector3f vec = RayTraceUtils.rotatedAround(entity.getLookAngle(), Vector3f.YP, posYawOff);
                EntityWaterLaser laser = new EntityWaterLaser(world, entity);
                laser.setPos(laser.getX() + vec.x(), laser.getY() + vec.y(), laser.getZ() + vec.z());
                laser.setMaxTicks(entity instanceof Player ? 44 : 15);
                laser.setDamageMultiplier(0.95f + level * 0.5f);
                laser.setPositionYawOffset(posYawOff);
                if (entity instanceof Mob mob && mob.getTarget() != null) {
                    LivingEntity target = mob.getTarget();
                    laser.setRotationTo(target.getX(), target.getEyeY(), target.getZ(), 0);
                }
                world.addFreshEntity(laser);
            }
            return true;
        }
        return false;
    }

    @Override
    public int rpCost() {
        return 10;
    }
}
