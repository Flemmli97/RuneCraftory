package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class DoubleWindBladeSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.FIRE, 1));
    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), false, true, true, 1, EnumSkills.WIND)).orElse(false);
        if (rp) {
            for (int i = 0; i < 2; i++) {
                EntityWindBlade wind = new EntityWindBlade(world, entity);
                wind.setDamageMultiplier(1 + (level - 1) / 10);
                wind.shoot(entity, 0, entity.getYRot() - (i == 0 ? 1 : -1) * 80, 0, 0.45f, 0);
                boolean set = false;
                if (entity instanceof Mob mob && mob.getTarget() != null) {
                    wind.setTarget(mob.getTarget());
                    set = true;
                } else if (entity instanceof Player) {
                    EntityHitResult res = RayTraceUtils.calculateEntityFromLook(entity, 9);
                    if (res != null) {
                        wind.setTarget(res.getEntity());
                        set = true;
                    }
                }
                if (!set) {
                    HitResult res = RayTraceUtils.entityRayTrace(entity, 8, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, false, false, null);
                    wind.setTarget(res.getLocation());
                }
                world.addFreshEntity(wind);
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
