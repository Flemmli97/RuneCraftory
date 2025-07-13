package io.github.flemmli97.runecraftory.common.inventory.container;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.blocks.entity.UpgradingCraftingBlockEntity;
import io.github.flemmli97.runecraftory.common.inventory.PlayerBoundCraftingContainer;
import io.github.flemmli97.runecraftory.common.inventory.WrappedContainer;
import io.github.flemmli97.runecraftory.common.recipes.CraftingType;
import io.github.flemmli97.runecraftory.common.registry.ModMenuTypes;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ContainerUpgrade extends AbstractContainerMenu {

    private final PlayerBoundCraftingContainer craftingInv;
    private final CraftingType type;
    private final WrappedContainer output;
    private final DataSlot rpCost;

    public ContainerUpgrade(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, getTile(inv.player.level(), pos));
    }

    public ContainerUpgrade(int windowId, Inventory playerInv, UpgradingCraftingBlockEntity tile) {
        super(ModMenuTypes.UPGRADE_CONTAINER.get(), windowId);
        this.output = new WrappedContainer(new SimpleContainer(1), this::slotsChanged);
        this.craftingInv = PlayerBoundCraftingContainer.create(this, tile.getUpgradeInventory(), playerInv.player);
        this.type = tile.craftingType();
        this.addSlot(new UpgradeOutputSlot(this.output, this, this.craftingInv, 0, 116, 35));
        for (int hotbar = 0; hotbar < 9; ++hotbar) {
            this.addSlot(new Slot(playerInv, hotbar, 8 + hotbar * 18, 142));
        }
        for (int column = 0; column < 3; ++column) {
            for (int row = 0; row < 9; ++row) {
                this.addSlot(new Slot(playerInv, row + column * 9 + 9, 8 + row * 18, 84 + column * 18));
            }
        }
        this.addSlot(new Slot(this.craftingInv, 0, 20, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return this.container.canPlaceItem(0, stack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.craftingInv, 1, 56, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return this.container.canPlaceItem(1, stack);
            }
        });
        this.addDataSlot(this.rpCost = DataSlot.standalone());
        this.updateOutput();
    }

    public static UpgradingCraftingBlockEntity getTile(Level world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof UpgradingCraftingBlockEntity) {
            return (UpgradingCraftingBlockEntity) blockEntity;
        }
        throw new IllegalStateException("Expected tile entity of type TileCrafting but got " + blockEntity);
    }

    private void updateOutput() {
        if (this.craftingInv.getPlayer().level().isClientSide)
            return;
        Pair<Integer, ItemStack> res = CraftingUtils.getUpgradeResult(this.craftingInv.getItem(0), Platform.INSTANCE.getPlayerData(this.craftingInv.getPlayer()),
                this.craftingInv.getItem(1), this.craftingType());
        this.output.setItem(0, res.getSecond());
        this.rpCost.set(res.getFirst());
    }

    public CraftingType craftingType() {
        return this.type;
    }

    public int rpCost() {
        return this.rpCost.get();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotID) {
        if (!player.isAlive())
            return ItemStack.EMPTY;
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotID);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotID == 0) {
                itemstack1.onCraftedBy(player.level(), player, itemstack1.getCount());
                if (!this.moveItemStackTo(itemstack1, 1, 37, false))
                    return ItemStack.EMPTY;
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (slotID < 37) {
                if (!this.moveItemStackTo(itemstack1, 37, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            if (slotID == 0) {
                player.drop(itemstack1, false);
            }
        }
        return itemstack;
    }

    @Override
    public void slotsChanged(Container container) {
        this.updateOutput();
        super.slotsChanged(container);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
