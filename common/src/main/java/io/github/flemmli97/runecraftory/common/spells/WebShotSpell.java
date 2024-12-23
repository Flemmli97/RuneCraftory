package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpiderWeb;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class WebShotSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntitySpiderWeb web = new EntitySpiderWeb(level, entity);
        web.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.8f));
        ProjectileUtil.shoot(entity, web, 1.3f, entity instanceof Player ? 1 : 7 - level.getDifficulty().getId() * 2);
        level.addFreshEntity(web);
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SKELETON_SHOOT, entity.getSoundSource(), 1.0f, 1.0F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
        return true;
    }
}
