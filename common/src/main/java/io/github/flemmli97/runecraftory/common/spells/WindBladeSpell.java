package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;

public class WindBladeSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.WIND, 6));
    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true, EnumSkills.WIND)).orElse(false);
        if (!rp)
            return false;
        EntityWindBlade wind = new EntityWindBlade(level, entity);
        wind.setDamageMultiplier(0.95f + lvl * 0.05f);
        wind.shoot(entity, 0, entity.getYRot(), 0, 0.45f, 0);
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            wind.setTarget(mob.getTarget());
        } else if (entity instanceof Player) {
            EntityHitResult res = RayTraceUtils.calculateEntityFromLook(entity, 9);
            if (res != null) {
                wind.setTarget(res.getEntity());
            }
        }
        level.addFreshEntity(wind);
        return true;
    }

    @Override
    public int rpCost() {
        return 10;
    }
}
