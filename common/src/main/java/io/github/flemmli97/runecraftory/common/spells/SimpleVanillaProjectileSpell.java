package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class SimpleVanillaProjectileSpell extends Spell {

    public static final ProjectileFactory WITHER_SKULL = (level, entity, direction) -> new WitherSkull(level, entity, direction.x, direction.y, direction.z);
    public static final ProjectileFactory GHAST_FIREBALL = (level, entity, direction) -> new LargeFireball(level, entity, direction.x, direction.y, direction.z, 1);
    public static final ProjectileFactory DRAGON_FIREBALL = (level, entity, direction) -> new DragonFireball(level, entity, direction.x, direction.y, direction.z);
    public static final ProjectileFactory SNOWBALL = (level, entity, direction) -> {
        Snowball snowball = new Snowball(level, entity);
        snowball.shoot(direction.x, direction.y, direction.z, 2.0F, 1.0F);
        Vec3 vec3 = entity.getDeltaMovement();
        snowball.setDeltaMovement(snowball.getDeltaMovement().add(vec3.x, entity.isOnGround() ? 0.0 : vec3.y, vec3.z));
        return snowball;
    };

    private final ProjectileFactory factory;
    private final SoundEvent sound;

    public SimpleVanillaProjectileSpell(ProjectileFactory factory, SoundEvent sound) {
        this.factory = factory;
        this.sound = sound;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Vec3 look;
        if (entity instanceof Mob mob && mob.getTarget() != null)
            look = new Vec3(mob.getTarget().getX() - entity.getX(), mob.getTarget().getY() - entity.getY(), mob.getTarget().getZ() - entity.getZ()).normalize();
        else
            look = entity.getLookAngle();
        Entity proj = this.factory.create(level, entity, look);
        proj.setPos(entity.getX(), entity.getEyeY(), entity.getZ());
        level.addFreshEntity(proj);
        playSound(entity, this.sound, 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }

    public interface ProjectileFactory {

        Entity create(ServerLevel level, LivingEntity entity, Vec3 direction);

    }
}
