package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySlashResidue;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class SlashSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntitySlashResidue slash = new EntitySlashResidue(level, entity);
        Vec3 pos = entity.position();
        Vec3 dir = entity instanceof Mob mob && mob.getTarget() != null ? mob.getTarget().position().subtract(pos).normalize().scale(1.1)
                : entity.getLookAngle().scale(1.1);
        slash.setPos(pos.x + dir.x, pos.y + Mth.clamp(dir.y, -0.3, 0.8), pos.z + dir.z);
        slash.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.85f));
        slash.lookAt(EntityAnchorArgument.Anchor.FEET, entity.position());
        level.addFreshEntity(slash);
        return true;
    }
}
