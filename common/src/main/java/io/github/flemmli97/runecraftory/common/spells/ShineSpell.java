package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityLightBall;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ShineSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.LIGHT, 1));
    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), false, true, true, 1, EnumSkills.WIND)).orElse(false);
        if (rp) {
            List<Entity> lights = world.getEntities(entity, entity.getBoundingBox().inflate(4), e -> e instanceof EntityLightBall light && light.getOwner() == entity);
            lights.forEach(e -> e.remove(Entity.RemovalReason.KILLED));
            EntityLightBall.createQuadLights(world, entity, false, 0.9f + level * 0.1f);
            return true;
        }
        return false;
    }

    @Override
    public int rpCost() {
        return 15;
    }
}
