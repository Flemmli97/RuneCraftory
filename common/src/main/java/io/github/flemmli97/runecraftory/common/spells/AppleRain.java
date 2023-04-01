package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityAppleProjectile;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class AppleRain extends Spell {

    private final Type type;

    public AppleRain(Type type) {
        this.type = type;
    }

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true, EnumSkills.LIGHT)).orElse(false);
        if (!rp)
            return false;
        int apples = switch (this.type) {
            case NORMAL -> 24;
            case BIG -> 20;
            case LOTS -> 40;
        };
        for (int i = 0; i < apples; i++) {
            EntityAppleProjectile apple = new EntityAppleProjectile(level, entity);
            if (this.type == Type.BIG) {
                apple.setDamageMultiplier(1.5f + lvl * 0.05f);
                apple.withSizeInc(1);
            } else
                apple.setDamageMultiplier(1.25f + lvl * 0.05f);
            double x = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 8;
            double y = entity.getY() + entity.getBbHeight() + entity.getRandom().nextDouble() * 3;
            double z = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 8;
            apple.setPos(x, y, z);
            level.addFreshEntity(apple);
        }
        return true;
    }

    @Override
    public int rpCost() {
        return 10;
    }

    public enum Type {
        NORMAL,
        BIG,
        LOTS
    }
}
