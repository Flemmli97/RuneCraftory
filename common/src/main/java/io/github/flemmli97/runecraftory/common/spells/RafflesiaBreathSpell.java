package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStatusBall;
import io.github.flemmli97.runecraftory.common.entities.misc.RafflesiaBreathSummoner;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class RafflesiaBreathSpell extends Spell {

    private final EntityStatusBall.Type type;

    public RafflesiaBreathSpell(EntityStatusBall.Type type) {
        this.type = type;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        RafflesiaBreathSummoner summoner = new RafflesiaBreathSummoner(level, entity, this.type);
        summoner.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.75f));
        Vec3 position = entity.position().add(0, entity.getBbHeight() * 0.5, 0);
        Vec3 target = ProjectileUtil.getAimTarget(entity);
        if (target == null) {
            target = position.add(entity.getLookAngle().scale(5));
        }
        summoner.setPos(position.x, position.y, position.z);
        summoner.setTarget(target.x, target.y, target.z);
        level.addFreshEntity(summoner);
        return true;
    }
}