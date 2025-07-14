package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.ElementalCircleSummoner;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ElementalCircleSpell extends Spell {

    private final ItemElement element;

    public ElementalCircleSpell(ItemElement element) {
        this.element = element;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        ElementalCircleSummoner summoner = new ElementalCircleSummoner(level, entity, this.element);
        summoner.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1));
        Vec3 position = entity.position().add(0, entity.getBbHeight() * 0.4, 0);
        Vec3 target = ProjectileUtils.getAimTarget(entity);
        if (target == null) {
            position.add(entity.getLookAngle().scale(5));
        }
        summoner.setPos(position.x, position.y, position.z);
        summoner.setTarget(target.x, target.y, target.z);
        level.addFreshEntity(summoner);
        return true;
    }
}