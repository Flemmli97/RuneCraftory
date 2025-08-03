package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.CardsEntity;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class CardThrowSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Vec3 target = ProjectileUtils.getAimTarget(entity);
        Vec3 dir;
        if (target != null) {
            dir = target.subtract(entity.getEyePosition());
        } else {
            dir = entity.getLookAngle();
        }
        float[] yxRot = MathsHelper.YXRotFrom(dir);
        Vec3 up = Vec3.directionFromRotation(yxRot[1] + 90, yxRot[0]);
        for (Vector3d vec : MathUtils.rotatedVecs(new Vector3d(dir.x(), dir.y(), dir.z()), new Vector3d(up.x(), up.y(), up.z()), -50, 50, 10)) {
            CardsEntity cards = new CardsEntity(level, entity, entity.getRandom().nextInt(8));
            cards.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
            cards.shoot(vec.x(), vec.y(), vec.z(), 1.5f, 0);
            level.addFreshEntity(cards);
        }
        return true;
    }
}
