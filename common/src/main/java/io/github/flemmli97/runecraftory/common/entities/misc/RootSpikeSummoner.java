package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class RootSpikeSummoner extends ProjectileSummonHelperEntity {

    public RootSpikeSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public RootSpikeSummoner(Level level, LivingEntity caster) {
        super(ModEntities.ROOT_SPIKE_SUMMONER.get(), level, caster);
        this.maxLivingTicks = 30;
    }

    @Override
    protected void summonProjectiles() {
        if (this.ticksExisted % 10 != 0 || this.getOwner() == null)
            return;
        EntitySpike spike = new EntitySpike(this.level, this.getOwner(), 0, 10, EntitySpike.SpikeType.ROOT);
        Vec3 targetPos = null;
        if (this.getOwner() instanceof Mob mob) {
            Entity target = EntityUtils.ownedProjectileTarget(mob, 14);
            if (target != null)
                targetPos = target.position();
        } else if (this.getOwner() instanceof Player player) {
            HitResult result = RayTraceUtils.entityRayTrace(player, 12, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, true, false, e -> e instanceof LivingEntity);
            if (result != null) {
                targetPos = result.getLocation();
            }
        }
        if (targetPos != null) {
            spike.setPos(targetPos);
        }
        this.level.addFreshEntity(spike);
    }
}
