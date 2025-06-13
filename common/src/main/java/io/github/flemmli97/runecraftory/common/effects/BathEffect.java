package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BathEffect extends MobEffect {

    public BathEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        EntityData entityData = Platform.INSTANCE.getEntityData(living);
        if (living.updateFluidHeightAndDoFluidPushing(RunecraftoryTags.HOT_SPRING_FLUID, 0.014)) {
            entityData.setEnteredBath(true);
            living.heal(living.getMaxHealth() * 0.04f);
            if (living instanceof ServerPlayer player) {
                PlayerData data = Platform.INSTANCE.getPlayerData(player);
                data.refreshRunePoints(Math.max(1, (int) (data.getMaxRunePoints() * 0.03f)));
                LevelCalc.levelSkill(data, EnumSkills.BATH, 2f);
            }
        } else if (entityData.enteredBath()) {
            entityData.setEnteredBath(false);
            living.removeEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(this));
        }
        return super.applyEffectTick(living, amplifier);
    }
}
