package com.flemmli97.runecraftory.common.inventory.container;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.blocks.tile.TileCrafting;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import com.flemmli97.runecraftory.common.inventory.DummyInventory;
import com.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import com.flemmli97.runecraftory.common.registry.ModContainer;
import com.flemmli97.runecraftory.common.utils.CraftingUtils;
import com.flemmli97.runecraftory.common.utils.EntityUtils;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Needs multiplayer testing
 */
public class ContainerCrafting extends Container {

    private List<SextupleRecipe> matchingRecipes;
    private SextupleRecipe currentRecipe;
    private final PlayerContainerInv craftingInv;
    private final EnumCrafting type;
    private final DummyInventory outPutInv;
    private final TileCrafting tile;
    private final IntReferenceHolder rpCost;

    public ContainerCrafting(int windowId, PlayerInventory inv, PacketBuffer data) {
        this(windowId, inv, getTile(inv.player.world, data));
    }

    public ContainerCrafting(int windowID, PlayerInventory playerInv, TileCrafting tile) {
        super(ModContainer.craftingContainer.get(), windowID);
        this.outPutInv = new DummyInventory(new ItemStackHandler());
        this.craftingInv = PlayerContainerInv.create(this, tile, playerInv.player);
        this.tile = tile;
        this.type = tile.craftingType();
        this.addSlot(new CraftingOutputSlot(this.outPutInv, this, this.craftingInv, 0, 116, 35));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }
        for (int i = 0; i < 3; ++i) {
            this.addSlot(new Slot(this.craftingInv, i, 20 + i * 18, 26));
            this.addSlot(new Slot(this.craftingInv, i + 3, 20 + i * 18, 44));
        }
        this.trackInt(this.rpCost = IntReferenceHolder.single());
        this.initCraftingMatrix(this.craftingInv);
    }

    public EnumCrafting craftingType() {
        return this.type;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inv) {
        if (inv == this.craftingInv)
            this.updateCraftingOutput(false);
        super.onCraftMatrixChanged(inv);
    }

    private void initCraftingMatrix(IInventory inv) {
        if (inv == this.craftingInv)
            this.updateCraftingOutput(true);
    }

    public void updateCraftingOutput(boolean init) {
        if (this.craftingInv.getPlayer().world.isRemote)
            return;
        if (this.craftingInv.refreshAndSet()) {
            this.matchingRecipes = getRecipes(this.craftingInv, this.type);
            if (!init)
                this.tile.resetIndex();
        }
        this.updateCraftingSlot();
    }

    private void updateCraftingSlot() {
        ItemStack stack;
        if (this.matchingRecipes != null && this.matchingRecipes.size() > 0) {
            if (this.tile.craftingIndex() >= this.matchingRecipes.size())
                this.tile.resetIndex();
            this.currentRecipe = this.matchingRecipes.get(this.tile.craftingIndex());
            this.rpCost.set(ItemUtils.craftingCost(this.type, this.craftingInv.getPlayer().getCapability(CapabilityInsts.PlayerCap).orElseThrow(EntityUtils::capabilityException), this.currentRecipe));
            stack = this.currentRecipe.getCraftingResult(this.craftingInv);
        } else {
            stack = ItemStack.EMPTY;
            this.rpCost.set(-1);
            this.currentRecipe = null;
        }
        this.outPutInv.setInventorySlotContents(0, stack);
        if (this.craftingInv.getPlayer() instanceof ServerPlayerEntity)
            ((ServerPlayerEntity) this.craftingInv.getPlayer()).connection.sendPacket(new SSetSlotPacket(this.windowId, 0, stack));
    }

    public SextupleRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }

    public boolean canIncrease() {
        return this.matchingRecipes != null && this.tile.craftingIndex() < this.matchingRecipes.size() - 1;
    }

    public boolean canDecrease() {
        return this.tile.craftingIndex() > 0;
    }

    public void increase() {
        if (this.canIncrease()) {
            this.tile.increaseIndex();
        } else {
            this.tile.resetIndex();
        }
        this.updateCraftingSlot();
    }

    public void decrease() {
        if (this.canDecrease()) {
            this.tile.decreaseIndex();
        } else if (this.matchingRecipes != null) {
            while (this.tile.craftingIndex() < this.matchingRecipes.size())
                this.tile.increaseIndex();
        }
        this.updateCraftingSlot();
    }

    public int rpCost() {
        return this.rpCost.get();
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotID) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotID);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (slotID == 0) {
                itemstack1.onCrafting(player.world, player, itemstack1.getCount());
                if (!this.mergeItemStack(itemstack1, 1, 37, false))
                    return ItemStack.EMPTY;
                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotID < 37) {
                if (!this.mergeItemStack(itemstack1, 37, this.inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(player, itemstack1);
            if (slotID == 0) {
                player.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(PlayerEntity entity) {
        super.onContainerClosed(entity);
    }

    public static List<SextupleRecipe> getRecipes(PlayerContainerInv inv, EnumCrafting type) {
        if (inv.getPlayer() instanceof ServerPlayerEntity)
            return inv.getPlayer().getServer().getRecipeManager().getRecipes(CraftingUtils.getType(type), inv, ((ServerPlayerEntity) inv.getPlayer()).getServerWorld());
        return new ArrayList<>();
    }

    public static TileCrafting getTile(World world, PacketBuffer buffer) {
        TileEntity tile = world.getTileEntity(buffer.readBlockPos());
        if (tile instanceof TileCrafting) {
            return (TileCrafting) tile;
        }
        throw new IllegalStateException("Expected tile entity of type TileCrafting but got " + tile);
    }
}
