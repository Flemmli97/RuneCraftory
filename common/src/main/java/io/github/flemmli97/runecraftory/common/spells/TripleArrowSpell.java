package io.github.flemmli97.runecraftory.common.spells;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class TripleArrowSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {
    }

    @Override
    public void levelSkill(ServerPlayer player) {
    }

    @Override
    public int coolDown() {
        return 1;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true)).orElse(false);
        if (!rp)
            return false;
        Arrow arrowentity = new Arrow(level, entity);
        float f = 1;
        if (stack.getItem() instanceof ItemStaffBase)
            f = BowItem.getPowerForTime(72000 - entity.getUseItemRemainingTicks());
        arrowentity.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, f * 1.5F, 1.0F);
        arrowentity.setBaseDamage(1.7 + CombatUtils.getAttributeValue(entity, Attributes.ATTACK_DAMAGE) * 0.05 * lvl);
        arrowentity.setSecondsOnFire(ItemNBT.getElement(stack) == EnumElement.FIRE ? 200 : 0);
        arrowentity.pickup = AbstractArrow.Pickup.DISALLOWED;
        level.addFreshEntity(arrowentity);

        Vec3 dir = entity.getLookAngle();
        Vec3 up = entity.getUpVector(1);
        for (float y = -15; y <= 15; y += 30) {
            Quaternion quaternion = new Quaternion(new Vector3f(up), y, true);
            Vector3f newDir = new Vector3f(dir);
            newDir.transform(quaternion);
            Arrow arrowO = new Arrow(level, entity);
            arrowO.setBaseDamage(1.7 + CombatUtils.getAttributeValue(entity, Attributes.ATTACK_DAMAGE) * 0.05 * lvl);
            arrowO.shoot(newDir.x(), newDir.y(), newDir.z(), f * 1.5F, 1.0F);
            level.addFreshEntity(arrowO);
        }
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, entity.getSoundSource(), 1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + f * 0.5f);
        return true;
    }

    @Override
    public int rpCost() {
        return 10;
    }
}
