package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityElementalBall;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class IceBallDropSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityElementalBall ball = new EntityElementalBall(level, entity, EnumElement.WATER);
        ball.setVariant(1);
        Vec3 target;
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            target = mob.getTarget().getEyePosition();
        } else {
            HitResult res = RayTraceUtils.entityRayTrace(entity, 10, ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, true, true, null);
            target = res instanceof EntityHitResult entityHitResult ? entityHitResult.getEntity().getEyePosition() : res.getLocation();
        }
        ball.setPos(target.x(), target.y() + 5, target.z());
        ball.withMaxLivingTicks(25);
        ball.setDeltaMovement(new Vec3(0, -1, 0).scale(0.3));
        ball.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1f));
        level.addFreshEntity(ball);
        playSound(entity, ModSounds.SPELL_GENERIC_FIRE_BALL.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
