package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMissile;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class MissileSpell extends Spell {

    private final int amount;

    public MissileSpell(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        int left = this.amount / 2;
        Vec3 side = new Vec3(entity.getLookAngle().x, 0, entity.getLookAngle().z).yRot(90).normalize();
        for (int i = 0; i < left; i++) {
            EntityMissile missile = new EntityMissile(level, entity);
            missile.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.85f));
            missile.setPos(missile.getX() + side.x() + entity.getRandom().nextGaussian() * 0.1, missile.getY(), missile.getZ() + side.z() + entity.getRandom().nextGaussian() * 0.1);
            missile.shoot(entity, 0, entity.getYRot(), 0, 0.18f, 8);
            if (entity instanceof Mob mob && mob.getTarget() != null) {
                missile.setTarget(mob.getTarget());
            } else if (entity instanceof Player) {
                EntityHitResult res = calculateEntityFromLook(entity, 16);
                if (res != null) {
                    missile.setTarget(res.getEntity());
                }
            }
            level.addFreshEntity(missile);
        }
        int right = this.amount / left;
        for (int i = 0; i < right; i++) {
            EntityMissile missile = new EntityMissile(level, entity);
            missile.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.9f));
            missile.setPos(missile.getX() - side.x() + entity.getRandom().nextGaussian() * 0.1, missile.getY(), missile.getZ() - side.z() + entity.getRandom().nextGaussian() * 0.1);
            missile.shoot(entity, 0, entity.getYRot(), 0, 0.18f, 8);
            if (entity instanceof Mob mob && mob.getTarget() != null) {
                missile.setTarget(mob.getTarget());
            } else if (entity instanceof Player) {
                EntityHitResult res = calculateEntityFromLook(entity, 16);
                if (res != null) {
                    missile.setTarget(res.getEntity());
                }
            }
            level.addFreshEntity(missile);
        }
        playSound(entity, ModSounds.SPELL_GENERIC_FIRE_BALL.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }

    public static EntityHitResult calculateEntityFromLook(LivingEntity entity, float reach) {
        Vec3 pos = entity.getEyePosition(1);
        Vec3 dir = entity.getViewVector(1);
        Vec3 scaledDir = dir.scale(reach);
        EntityHitResult result = RayTraceUtils.rayTraceEntities(entity.level, entity, pos, pos.add(scaledDir), entity.getBoundingBox().expandTowards(scaledDir).inflate(1), (t) -> EntitySelector.NO_SPECTATORS.test(t) && t.isPickable(), e -> e.getPickRadius() + 1);
        if (result != null) {
            Vec3 loc = result.getLocation();
            double dist = pos.distanceToSqr(loc);
            if (dist <= reach * reach)
                return result;
        }
        return null;
    }
}
