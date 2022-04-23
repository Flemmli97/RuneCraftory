package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

public class ArrowSpell extends Spell {

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
    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        Arrow arrowentity = new Arrow(world, entity);
        float f = 1;
        if (stack.getItem() instanceof ItemStaffBase)
            f = BowItem.getPowerForTime(72000 - entity.getUseItemRemainingTicks());
        arrowentity.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, f * 1.5F, 1.0F);
        arrowentity.setBaseDamage(CombatUtils.getAttributeValueRaw(entity, Attributes.ATTACK_DAMAGE) * 0.05 * level);
        arrowentity.setSecondsOnFire(ItemNBT.getElement(stack) == EnumElement.FIRE ? 200 : 0);
        arrowentity.pickup = AbstractArrow.Pickup.DISALLOWED;
        world.addFreshEntity(arrowentity);
        return true;
    }

    @Override
    public int rpCost() {
        return 5;
    }
}
