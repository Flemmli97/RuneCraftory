package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPollenPuff;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class PollenPuffSpell extends Spell {

    private static final Vec3[] DIRS = dirs(16);

    private static Vec3[] dirs(int amount) {
        Vec3[] arr = new Vec3[amount];
        Vec3 dir = new Vec3(2, 1, 0).normalize();
        float step = 360f / amount;
        for (int i = 0; i < amount; i++)
            arr[i] = MathUtils.rotate(MathUtils.normalY, dir, i * step);
        return arr;
    }

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
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true, EnumSkills.EARTH)).orElse(false);
        if (!rp)
            return false;
        for (Vec3 dir : DIRS) {
            EntityPollenPuff puff = new EntityPollenPuff(level, entity);
            puff.setPos(puff.getX(), entity.getY() + entity.getBbHeight() * 0.2, puff.getZ());
            puff.setDamageMultiplier(0.45f + lvl * 0.05f);
            puff.shoot(dir.x(), dir.y(), dir.z(), 0.23f, 0);
            level.addFreshEntity(puff);
        }
        return true;
    }

    @Override
    public int rpCost() {
        return 100;
    }
}
