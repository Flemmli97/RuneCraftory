package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityCirclingBullet;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DoubleBulletSpell extends Spell {

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
        EntityCirclingBullet bullet = new EntityCirclingBullet(level, entity);
        bullet.setDamageMultiplier(0.8f + lvl * 0.05f);
        EntityCirclingBullet bullet2 = new EntityCirclingBullet(level, entity);
        bullet2.setDamageMultiplier(0.8f + lvl * 0.05f);
        bullet2.reverseMovement();
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            bullet.shootAtEntity(mob.getTarget(), 0.3f, 7 - level.getDifficulty().getId() * 2, 0);
            bullet2.shootAtEntity(mob.getTarget(), 0.3f, 7 - level.getDifficulty().getId() * 2, 0);
        } else {
            bullet.shootFromRotation(entity, entity.getXRot() + 5, entity.getYRot(), 0.0F, 0.3f, 1.0F);
            bullet2.shootFromRotation(entity, entity.getXRot() + 5, entity.getYRot(), 0.0F, 0.3f, 1.0F);
        }
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FISHING_BOBBER_THROW, entity.getSoundSource(), 1.0F, 1.0F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
        level.addFreshEntity(bullet);
        level.addFreshEntity(bullet2);
        return true;
    }

    @Override
    public int rpCost() {
        return 250;
    }
}
