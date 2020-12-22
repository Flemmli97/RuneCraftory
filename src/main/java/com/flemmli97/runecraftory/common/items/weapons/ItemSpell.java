package com.flemmli97.runecraftory.common.items.weapons;

import com.flemmli97.runecraftory.api.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Supplier;

public class ItemSpell extends Item {

    private final Supplier<? extends Spell> spell;

    public ItemSpell(Supplier<? extends Spell> spell, Properties properties) {
        super(properties);
        this.spell = spell;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!player.world.isRemote && player.getCooldownTracker().getCooldown(this, 0) <= 0 && this.spell.get().use((ServerWorld) world, player, player.getHeldItem(hand))) {
            player.getCooldownTracker().setCooldown(this, this.getSpell().coolDown());
            this.spell.get().levelSkill((ServerPlayerEntity) player);
            return ActionResult.success(player.getHeldItem(hand));
        }
        return ActionResult.fail(player.getHeldItem(hand));
    }

    public Spell getSpell() {
        return this.spell.get();
    }
}
