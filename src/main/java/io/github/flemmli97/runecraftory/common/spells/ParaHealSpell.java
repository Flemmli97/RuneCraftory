package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.world.server.ServerWorld;

public class ParaHealSpell extends Spell {

    @Override
    public void update(PlayerEntity player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayerEntity player) {

    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerWorld world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        boolean rp = !(entity instanceof PlayerEntity) || entity.getCapability(CapabilityInsts.PlayerCap).map(cap -> cap.decreaseRunePoints((PlayerEntity) entity, this.rpCost(), false)).orElse(false);
        if (rp) {
            if (level >= 10) {

            }
            if (level >= 7) {

            }
            if (level >= 3) {

            }
            entity.removePotionEffect(Effects.SLOWNESS);
            entity.removePotionEffect(ModEffects.paralysis.get());
            return true;
        }
        return false;
    }

    @Override
    public int rpCost() {
        return 10;
    }
}
