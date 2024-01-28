package io.github.flemmli97.runecraftory.common.inventory;

import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.platform.SaveItemContainer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class InventorySpells extends SaveItemContainer {

    private ItemStack inUseStack;

    public InventorySpells() {
        super(4);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return stack.getItem() instanceof ItemSpell;
    }

    public void useSkill(ServerPlayer player, int index) {
        ItemStack stack = this.getItem(index);
        if (stack.getItem() instanceof ItemSpell itemSpell) {
            itemSpell.useSpell(player, stack);
            this.inUseStack = stack;
        }
    }

    public void onRelease() {
        this.inUseStack = ItemStack.EMPTY;
    }

    public ItemStack getInUseStack() {
        return this.inUseStack;
    }

    public void dropItemsAt(LivingEntity entity) {
        if (!entity.level.isClientSide) {
            for (ItemStack stack : this.stacks) {
                if (stack.isEmpty())
                    continue;
                entity.spawnAtLocation(stack);
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
