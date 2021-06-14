package io.github.flemmli97.runecraftory.common.spells;

import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.server.ServerWorld;

public class DoubleWindBladeSpell extends Spell {

    @Override
    public void update(PlayerEntity player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayerEntity player) {
        player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> LevelCalc.levelSkill(player, cap, EnumSkills.FIRE, 1));
    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerWorld world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        boolean rp = !(entity instanceof PlayerEntity) || entity.getCapability(CapabilityInsts.PlayerCap).map(cap -> cap.decreaseRunePoints((PlayerEntity) entity, this.rpCost(), true)).orElse(false);
        if (rp) {
            for (int i = 0; i < 2; i++) {
                EntityWindBlade wind = new EntityWindBlade(world, entity);
                wind.setDamageMultiplier(1 + (level - 1) / 10);
                wind.shoot(entity, 0, entity.rotationYaw - (i == 0 ? 1 : -1) * 80, 0, 0.45f, 0);
                boolean set = false;
                if (entity instanceof MobEntity && ((MobEntity) entity).getAttackTarget() != null) {
                    wind.setTarget(((MobEntity) entity).getAttackTarget());
                    set = true;
                } else if (entity instanceof PlayerEntity) {
                    EntityRayTraceResult res = RayTraceUtils.calculateEntityFromLook(entity, 9);
                    if (res != null) {
                        wind.setTarget(res.getEntity());
                        set = true;
                    }
                }
                if (!set) {
                    RayTraceResult res = RayTraceUtils.entityRayTrace(entity, 8, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, false, false, null);
                    wind.setTarget(res.getHitVec());
                }
                world.addEntity(wind);
            }
            return true;
        }
        return false;
    }

    @Override
    public int rpCost() {
        return 10;
    }
}
