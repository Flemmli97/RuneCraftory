package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class WindBladeCircle extends Spell {

    private final int amount;

    public WindBladeCircle(int amount) {
        this.amount = amount;
    }

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.WIND, 8));
    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true, EnumSkills.WIND)).orElse(false);
        if (!rp)
            return false;
        int angle = 360 / this.amount;
        for (int i = 0; i < this.amount; i++) {
            EntityWindBlade wind = new EntityWindBlade(level, entity);
            wind.setPos(wind.getX(), entity.getY() + entity.getBbHeight() * 0.4, wind.getZ());
            wind.setDamageMultiplier(1.1f + (lvl - 1) * 0.05f);
            wind.setPiercing();
            wind.shoot(entity, entity.getXRot(), entity.getYRot() + i * angle, 0, 0.35f, 0);
            level.addFreshEntity(wind);
        }
        return true;
    }

    @Override
    public int rpCost() {
        return 200;
    }
}