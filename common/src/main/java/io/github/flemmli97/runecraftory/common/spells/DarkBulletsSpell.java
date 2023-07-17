package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.DelayedAttacker;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkBulletSummoner;
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

public class DarkBulletsSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
    }

    @Override
    public int coolDown() {
        return 25;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true, EnumSkills.DARK)).orElse(false);
        if (!rp)
            return false;
        EntityDarkBulletSummoner summoner = new EntityDarkBulletSummoner(level, entity);
        summoner.setDamageMultiplier(0.5f + lvl * 0.05f);
        Vec3 delayedPos;
        if (entity instanceof DelayedAttacker attacker && (delayedPos = attacker.targetPosition(summoner.position())) != null) {
            summoner.setTarget(delayedPos.x(), delayedPos.y(), delayedPos.z());
        } else if (entity instanceof Mob mob && mob.getTarget() != null) {
            summoner.setTarget(mob.getTarget().getX(), mob.getTarget().getY(), mob.getTarget().getZ());
        } else {
            Vec3 look = Vec3.directionFromRotation(entity.getXRot(), entity.getYRot()).scale(5);
            summoner.setTarget(entity.getX() + look.x(), entity.getEyeY() + look.y(), entity.getZ() + look.z());
        }
        level.addFreshEntity(summoner);
        return true;
    }

    @Override
    public int rpCost() {
        return 250;
    }
}
