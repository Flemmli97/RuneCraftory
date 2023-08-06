package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityFireball;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FireballSpell extends Spell {

    public final boolean big;

    public FireballSpell(boolean big) {
        this.big = big;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this.rpCost(), EnumSkills.FIRE))
            return false;
        EntityFireball ball = new EntityFireball(level, entity, this.big);
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            ball.shootAtEntity(mob.getTarget(), 1, 0, 0.2f);
        } else {
            ball.shoot(entity, entity.getXRot(), entity.getYRot(), 0, 1, 0);
        }
        ball.setDamageMultiplier(0.95f + lvl * 0.05f + (this.big ? 0.1f : 0));
        level.addFreshEntity(ball);
        if (entity instanceof Player player) {
            boolean cooldown = Platform.INSTANCE.getPlayerData(player).map(cap -> {
                if (this.big) {
                    cap.getWeaponHandler().setBigFireballSpellFlag(cap.getWeaponHandler().bigFireballSpellFlag() + 1, this.coolDown());
                    return cap.getWeaponHandler().bigFireballSpellFlag() >= 2;
                }
                cap.getWeaponHandler().setFireballSpellFlag(cap.getWeaponHandler().fireballSpellFlag() + 1, this.coolDown());
                return cap.getWeaponHandler().fireballSpellFlag() >= 3;
            }).orElse(false);
            if (cooldown)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                    if (this.big) {
                        data.getWeaponHandler().setBigFireballSpellFlag(0, -1);
                    } else {
                        data.getWeaponHandler().setFireballSpellFlag(0, -1);
                    }
                });
            return cooldown;
        }
        return true;
    }
}
