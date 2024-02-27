package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPollenPuff;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class PollenPuffSpell extends Spell {

    private static final Vec3[] DIRS = dirs(16);

    private static Vec3[] dirs(int amount) {
        Vec3[] arr = new Vec3[amount];
        Vec3 dir = new Vec3(2, 1, 0).normalize();
        float step = 360f / amount;
        for (int i = 0; i < amount; i++)
            arr[i] = MathUtils.rotate(MathUtils.normalY, dir, i * step);
        return arr;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        for (Vec3 dir : DIRS) {
            EntityPollenPuff puff = new EntityPollenPuff(level, entity);
            puff.setPos(puff.getX(), entity.getY() + entity.getBbHeight() * 0.2, puff.getZ());
            puff.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.6f));
            puff.shoot(dir.x(), dir.y(), dir.z(), 0.23f, 0);
            level.addFreshEntity(puff);
        }
        playSound(entity, ModSounds.SPELL_GENERIC_POOF.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
