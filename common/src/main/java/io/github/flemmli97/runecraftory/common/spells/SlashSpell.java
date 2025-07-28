package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.SlashResidueEntity;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class SlashSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        SlashResidueEntity slash = new SlashResidueEntity(level, entity);
        Vec3 pos = entity.position();
        Vec3 target = ProjectileUtils.getAimTarget(entity);
        Vec3 dir;
        if (target != null) {
            dir = target.subtract(pos).normalize().scale(1.2);
        } else {
            dir = entity.getLookAngle().scale(1.2);
        }
        slash.setPos(pos.x + dir.x, pos.y + Mth.clamp(dir.y, -0.3, 0.8), pos.z + dir.z);
        slash.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        slash.lookAt(EntityAnchorArgument.Anchor.FEET, entity.position());
        level.addFreshEntity(slash);
        return true;
    }
}
