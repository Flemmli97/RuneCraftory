package io.github.flemmli97.runecraftory.common.entities.misc;

import com.flemmli97.tenshilib.common.entity.EntityBeam;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityThunderboltBeam extends EntityBeam {

    public EntityThunderboltBeam(EntityType<? extends EntityThunderboltBeam> type, World world) {
        super(type, world);
    }

    public EntityThunderboltBeam(World world, LivingEntity shooter) {
        super(ModEntities.lightningBeam.get(), world, shooter);
    }

    @Override
    public float getRange() {
        return 9;
    }

    @Override
    public float radius() {
        return 0.5f;
    }

    @Override
    public boolean piercing() {
        return true;
    }

    @Override
    public int livingTickMax() {
        return 25;
    }

    @Override
    public int attackCooldown() {
        return this.livingTickMax();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isRemote) {
            Vector3d pos = this.getPositionVec();
            Vector3d dir = this.hitVec.subtract(pos);
            for (double d = 0; d < 1; d += 0.05) {
                Vector3d scaleD = dir.scale(d).add(pos);
                this.world.addParticle(new ColoredParticleData(ModParticles.staticLight.get(), 34 / 255F, 34 / 255F, 230 / 255F, 0.6f, 0.06f), scaleD.getX(), scaleD.getY(), scaleD.getZ(), 0, 0, 0);
            }
        }
    }

    @Override
    public void onImpact(EntityRayTraceResult res) {
        CombatUtils.damage(this.getOwner(), res.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(20).element(EnumElement.WIND).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()), null);
    }
}