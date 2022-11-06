package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThiccLightningBolt;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BigLightningBoltSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {

    }

    @Override
    public int coolDown() {
        return 30;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true, EnumSkills.WIND)).orElse(false);
        if (!rp)
            return false;
        EntityThiccLightningBolt bolt = new EntityThiccLightningBolt(level, entity);
        bolt.setDamageMultiplier(0.95f + lvl * 0.05f);
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            bolt.shootAtEntity(mob.getTarget(), 0.15f, 0, 0);
        } else
            bolt.shoot(entity, 17, entity.getYRot(), 0, 0.15f, 0);
        level.addFreshEntity(bolt);
        return true;
    }

    @Override
    public int rpCost() {
        return 300;
    }
}
