package io.github.flemmli97.runecraftory.common.entities.misc.summoners;

import io.github.flemmli97.runecraftory.common.entities.misc.ProjectileSummonHelperEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.SporeEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.List;

public class SporeCircleSummoner extends ProjectileSummonHelperEntity {

    private List<Vector3d> attackPos;

    public SporeCircleSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public SporeCircleSummoner(Level level, LivingEntity caster) {
        super(RuneCraftoryEntities.SPORE_CIRCLE_SUMMONER.get(), level, caster);
        Vec3 look = Vec3.directionFromRotation(0, caster.yHeadRot).scale(1.3);
        this.attackPos = MathUtils.rotatedVecs(new Vector3d(look.x(), look.y(), look.z()), new Vector3d(0, 1, 0), -180, 135, 45);
        this.maxLivingTicks = 26;
    }

    @Override
    protected void summonProjectiles() {
        if (this.ticksExisted % 3 != 0 || this.getOwner() == null || this.attackPos == null)
            return;
        int i = (this.ticksExisted / 3) - 1;
        if (i < this.attackPos.size()) {
            Vector3d vec = this.attackPos.get(i);
            SporeEntity spore = new SporeEntity(this.level(), this.getOwner());
            spore.setPos(spore.getX() + vec.x(), spore.getY() + 0.4, spore.getZ() + vec.z());
            this.level().addFreshEntity(spore);
        }
    }
}
