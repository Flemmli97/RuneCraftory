package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class GroundedAbilitySpell extends WeaponSpell {

    public GroundedAbilitySpell(Supplier<? extends AttackAction> attackAction, TagKey<Item> weapon) {
        super(attackAction, weapon);
    }

    @Override
    public boolean canUse(ServerLevel level, LivingEntity entity, ItemStack stack) {
        return (entity.onGround() || entity.isNoGravity()) && super.canUse(level, entity, stack);
    }
}
