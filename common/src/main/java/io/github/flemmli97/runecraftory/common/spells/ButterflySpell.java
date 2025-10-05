package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.ButterflySummonerEntity;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ButterflySpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        ButterflySummonerEntity summoner = new ButterflySummonerEntity(level, entity);
        summoner.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        Vec3 target = ProjectileUtils.getAimTarget(entity);
        if (target == null) {
            target = entity.getEyePosition().add(Vec3.directionFromRotation(Mth.clamp(entity.getXRot(), -10, 10), entity.getYRot()).scale(5));
        }
        summoner.setTarget(target.x(), target.y(), target.z());
        level.addFreshEntity(summoner);
        return true;
    }
}
