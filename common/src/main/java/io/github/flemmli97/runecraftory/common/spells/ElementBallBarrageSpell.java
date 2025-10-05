package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.ElementBallBarrageSummoner;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ElementBallBarrageSpell extends Spell {

    private final ItemElement element;

    public ElementBallBarrageSpell(ItemElement element) {
        this.element = element;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        ElementBallBarrageSummoner summoner = new ElementBallBarrageSummoner(level, entity, this.element);
        summoner.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        Vec3 eye = entity.getEyePosition();
        Vec3 target = ProjectileUtils.getAimTarget(entity);
        if (target == null) {
            target = eye.add(entity.getViewVector(1).scale(5));
        }
        summoner.setPos(eye.x, eye.y, eye.z);
        summoner.setTarget(target.x, target.y, target.z);
        level.addFreshEntity(summoner);
        return true;
    }
}
