package io.github.flemmli97.runecraftory.common.inventory.container;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.blocks.tile.UpgradingCraftingBlockEntity;
import io.github.flemmli97.runecraftory.common.inventory.DummyInventory;
import io.github.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import io.github.flemmli97.runecraftory.common.registry.ModContainer;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
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

    private final PlayerContainerInv craftingInv;
    private final EnumCrafting type;
    private final DummyInventory outPutInv;
    private final DataSlot rpCost;

    public ContainerUpgrade(int windowId, Inventory inv, FriendlyByteBuf data) {
        this(windowId, inv, getTile(inv.player.level, data));
    }

    public ContainerUpgrade(int windowId, Inventory playerInv, UpgradingCraftingBlockEntity tile) {
        super(ModContainer.UPGRADE_CONTAINER.get(), windowId);
        this.outPutInv = new DummyInventory(new SimpleContainer(1));
        this.craftingInv = PlayerContainerInv.create(this, tile.getUpgradeInventory(), playerInv.player);
        this.type = tile.craftingType();
        this.addSlot(new UpgradeOutputSlot(this.outPutInv, this, this.craftingInv, 0, 116, 35));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
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

    public static UpgradingCraftingBlockEntity getTile(Level world, FriendlyByteBuf buffer) {
        BlockEntity blockEntity = world.getBlockEntity(buffer.readBlockPos());
        if (blockEntity instanceof UpgradingCraftingBlockEntity) {
            return (UpgradingCraftingBlockEntity) blockEntity;
        }
        throw new IllegalStateException("Expected tile entity of type TileCrafting but got " + blockEntity);
    }

    private void updateOutput() {
        if (this.craftingInv.getPlayer().level.isClientSide)
            return;
        int cost = CraftingUtils.upgradeCost(this.craftingType(), Platform.INSTANCE.getPlayerData(this.craftingInv.getPlayer()).orElseThrow(EntityUtils::playerDataException), this.craftingInv.getItem(0), this.craftingInv.getItem(1));
        if (cost >= 0) {
            this.outPutInv.setItem(0, CraftingUtils.getUpgradedStack(this.craftingInv.getItem(0), this.craftingInv.getItem(1), this.craftingType()));
        } else {
            this.outPutInv.setItem(0, ItemStack.EMPTY);
        }
        this.rpCost.set(cost);
    }

    public EnumCrafting craftingType() {
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
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotID == 0) {
                itemstack1.onCraftedBy(player.level, player, itemstack1.getCount());
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
    public void slotsChanged(Container inv) {
        if (inv == this.craftingInv) {
            this.updateOutput();
        }
        super.slotsChanged(inv);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
