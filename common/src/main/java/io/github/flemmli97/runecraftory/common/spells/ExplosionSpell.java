package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityExplosionSpell;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class ExplosionSpell extends Spell {

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.FIRE, 10));
    }

    @Override
    public int coolDown() {
        return 25;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityExplosionSpell spell = new EntityExplosionSpell(level, entity);
        spell.setDamageMultiplier(1.1f + (lvl - 1) * 0.05f);
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            spell.shootAtEntity(mob.getTarget(), 1.3f, 0, 0);
        } else {
            spell.shootFromRotation(entity, entity.getXRot() + 5, entity.getYRot(), 0.0F, 1.5F, 1.0F);
        }
        level.addFreshEntity(spell);
        return true;
    }

    @Override
    public int rpCost() {
        return 45;
    }
}
