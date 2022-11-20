package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityFurniture;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FurnitureThrowSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {

    }

    @Override
    public int coolDown() {
        return 25;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true, EnumSkills.DARK)).orElse(false);
        if (!rp)
            return false;
        int furnitureAmount = entity.getRandom().nextInt(7) + 4;
        for (int i = 0; i < furnitureAmount; ++i) {
            EntityFurniture.Type randType = EntityFurniture.Type.values()[entity.getRandom().nextInt(EntityFurniture.Type.values().length)];
            EntityFurniture furniture = new EntityFurniture(level, entity, randType);
            furniture.setDamageMultiplier(1 + 0.05f * lvl);
            furniture.setNoGravity(true);
            double xRand = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 13;
            double yRand = entity.getY() + (entity.getRandom().nextDouble()) * 2;
            double zRand = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 13;
            furniture.setPos(xRand, yRand, zRand);
            if (entity instanceof Mob mob && mob.getTarget() != null) {
                furniture.shootAtPosition(mob.getTarget().getX(), mob.getTarget().getY() + mob.getTarget().getBbHeight() * 0.5, mob.getTarget().getZ(), 0.1f, 1.4f);
            } else {
                Vec3 look = entity.getLookAngle().scale(5);
                furniture.shootAtPosition(look.x(), look.y(), look.z(), 0.1f, 1.4f);
            }
            level.addFreshEntity(furniture);
        }
        return true;
    }

    @Override
    public int rpCost() {
        return 200;
    }
}
