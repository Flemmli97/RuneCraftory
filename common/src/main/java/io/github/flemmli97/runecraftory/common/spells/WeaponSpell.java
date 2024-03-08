package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class WeaponSpell extends Spell {

    private final Supplier<AttackAction> attackAction;
    private final TagKey<Item> weapon;

    public WeaponSpell(Supplier<AttackAction> attackAction, TagKey<Item> weapon) {
        this.attackAction = attackAction;
        this.weapon = weapon;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean hasWeapon = entity.getMainHandItem().is(RunecraftoryTags.WEAPONS) || entity.getMainHandItem().is(RunecraftoryTags.TOOLS);
        if (!hasWeapon)
            return false;
        boolean correctWeapon = entity.getMainHandItem().is(this.weapon);
        rpUseMultiplier = correctWeapon ? rpUseMultiplier * 2 : rpUseMultiplier;
        return Spell.tryUseWithCost(entity, stack, this, rpUseMultiplier, true);
    }

    @Override
    public boolean canUse(ServerLevel world, LivingEntity entity, ItemStack stack) {
        return entity.getMainHandItem().is(RunecraftoryTags.WEAPONS) || entity.getMainHandItem().is(RunecraftoryTags.TOOLS);
    }

    @Override
    public AttackAction useAction() {
        return this.attackAction.get();
    }
}
