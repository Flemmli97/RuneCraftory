package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.action.AttackAction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class GroundedAbilitySpell extends WeaponSpell {
    public GroundedAbilitySpell(Supplier<AttackAction> attackAction, TagKey<Item> weapon) {
        super(attackAction, weapon);
    }

    @Override
    public boolean canUse(ServerLevel world, LivingEntity entity, ItemStack stack) {
        return (entity.isOnGround() || entity.isNoGravity()) && super.canUse(world, entity, stack);
    }
}
