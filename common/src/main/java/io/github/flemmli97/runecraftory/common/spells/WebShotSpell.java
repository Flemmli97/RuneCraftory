package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpiderWeb;
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

public class WebShotSpell extends Spell {

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
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true)).orElse(false);
        if (!rp)
            return false;
        EntitySpiderWeb web = new EntitySpiderWeb(level, entity);
        web.setDamageMultiplier(0.9f + lvl * 0.05f);
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            web.shootAtEntity(mob.getTarget(), 1.3f, 7 - level.getDifficulty().getId() * 2, 0.2f);
        } else {
            web.shootFromRotation(entity, entity.getXRot() + 5, entity.getYRot(), 0.0F, 1.5F, 1.0F);
        }
        level.addFreshEntity(web);
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SKELETON_SHOOT, entity.getSoundSource(), 1.0f, 1.0F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
        return true;
    }

    @Override
    public int rpCost() {
        return 10;
    }
}
