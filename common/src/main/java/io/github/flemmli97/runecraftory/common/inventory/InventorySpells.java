package io.github.flemmli97.runecraftory.common.inventory;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.platform.SaveItemContainer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class InventorySpells extends SaveItemContainer {

    public InventorySpells() {
        super(4);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return stack.getItem() instanceof ItemSpell;
    }

    public void useSkill(ServerPlayer player, int index) {
        ItemStack stack = this.getItem(index);
        if (stack.getItem() instanceof ItemSpell && player.getCooldowns().getCooldownPercent(stack.getItem(), 0.0f) <= 0.0f) {
            Spell spell = ((ItemSpell) stack.getItem()).getSpell();
            if (spell.use(player.getLevel(), player, stack)) {
                player.getCooldowns().addCooldown(stack.getItem(), spell.coolDown());
                spell.levelSkill(player);
            }
        }
    }

    public void dropItemsAt(LivingEntity entity) {
        if (!entity.level.isClientSide) {
            for (ItemStack stack : this.stacks) {
                ItemEntity item = new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), stack);
                item.setPickUpDelay(0);
                entity.level.addFreshEntity(item);
            }
        }
        this.stacks.clear();
    }

    public void update(Player player) {
        for (ItemStack stack : this.stacks) {
            if (stack.getItem() instanceof ItemSpell)
                ((ItemSpell) stack.getItem()).getSpell().update(player, stack);
        }
    }
}
