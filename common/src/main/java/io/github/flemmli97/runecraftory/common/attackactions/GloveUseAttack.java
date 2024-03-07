package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GloveUseAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        return new AnimatedAction(27 + 1, 4, "glove_use");
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (!anim.isPastTick(0.16))
            return;
        Vec3 look = entity.getLookAngle();
        Vec3 move = new Vec3(look.x, 0.0, look.z).normalize()
                .scale(entity.isOnGround() ? 0.5 : 0.3).add(0, entity.getDeltaMovement().y, 0);
        entity.setDeltaMovement(move);
        if (!entity.level.isClientSide && anim.getTickRaw() % (4 * anim.getSpeed()) == 0) {
            List<LivingEntity> list = entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(1.0));
            boolean flag = false;
            for (LivingEntity e : list) {
                if (e != entity) {
                    if (entity instanceof Player player)
                        flag = CombatUtils.playerAttackWithItem(player, e, stack, 0.5f, false, false);
                    else if (entity instanceof Mob mob)
                        flag = mob.doHurtTarget(e);
                }
            }
            if (flag && entity instanceof ServerPlayer serverPlayer) {
                Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> LevelCalc.levelSkill(serverPlayer, data, EnumSkills.DUAL, 2));
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        entity.maxUpStep += 0.5;
    }

    @Override
    public void onEnd(LivingEntity entity, WeaponHandler handler) {
        entity.maxUpStep -= 0.5;
    }

    @Override
    public Pose getPose(LivingEntity entity, WeaponHandler handler) {
        if (handler.getCurrentAnim() == null)
            return null;
        if (handler.getCurrentAnim().isPastTick(0.2) && !handler.getCurrentAnim().isPastTick(1.16))
            return Pose.SPIN_ATTACK;
        return null;
    }

    @Override
    public boolean disableItemSwitch() {
        return false;
    }
}
