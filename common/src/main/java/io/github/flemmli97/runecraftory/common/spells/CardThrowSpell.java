package io.github.flemmli97.runecraftory.common.spells;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityCards;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class CardThrowSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        for (Vector3f vec : RayTraceUtils.rotatedVecs(entity.getLookAngle(), new Vec3(0, 1, 0), -80, 80, 10)) {
            EntityCards cards = new EntityCards(level, entity, entity.getRandom().nextInt(8));
            cards.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1f));
            cards.shoot(vec.x(), vec.y(), vec.z(), 1.5f, 0);
            level.addFreshEntity(cards);
        }
        return true;
    }
}
