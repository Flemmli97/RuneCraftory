package com.flemmli97.runecraftory.common.inventory.container;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.blocks.tile.TileCrafting;
import com.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import com.flemmli97.runecraftory.common.inventory.DummyInventory;
import com.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import com.flemmli97.runecraftory.common.registry.ModContainer;
import com.flemmli97.runecraftory.common.utils.CraftingUtils;
import com.google.common.collect.Lists;
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
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class ContainerCrafting extends Container {

    private List<SextupleRecipe> matchingRecipes;
    private SextupleRecipe currentRecipe;
    private int matchingRecipesIndex;
    private final PlayerContainerInv craftingInv;
    private final EnumCrafting type;
    private final DummyInventory outPutInv;

    public ContainerCrafting(int windowId, PlayerInventory inv, PacketBuffer data) {
        this(windowId, inv, getTile(inv.player.world, data));
    }

    public ContainerCrafting(int p_i50105_2_, PlayerInventory playerInv, TileCrafting tile) {
        super(ModContainer.craftingContainer.get(), p_i50105_2_);
        this.outPutInv = new DummyInventory(new ItemStackHandler());
        this.craftingInv = PlayerContainerInv.create(this, tile, playerInv.player);
        this.type = tile.craftingType();
        this.addSlot(new CraftingOutputSlot(this.outPutInv, this, this.craftingInv, 0, 116, 34));
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
        }
        for (int i = 0; i < 3; ++i) {
            this.addSlot(new Slot(this.craftingInv, i + 3, 20 + i * 18, 44));
        }
        this.updateCraftingOutput();
    }

    public EnumCrafting craftingType() {
        return this.type;
    }

    @Override
    public boolean canInteractWith(PlayerEntity p_75145_1_) {
        return true;
    }

    @Override
    public void onCraftMatrixChanged(IInventory p_75130_1_) {
        if (p_75130_1_ == this.craftingInv)
            this.updateCraftingOutput();
        super.onCraftMatrixChanged(p_75130_1_);
    }

    public void updateCraftingOutput() {
        if (this.craftingInv.getPlayer().world.isRemote)
            return;
        this.matchingRecipes = getRecipes(this.craftingInv, this.type);
        this.matchingRecipesIndex = 0;
        this.updateCraftingSlot();
    }

    private void updateCraftingSlot() {
        ItemStack stack;
        if (this.matchingRecipes != null && this.matchingRecipesIndex < this.matchingRecipes.size()) {
            this.currentRecipe = this.matchingRecipes.get(this.matchingRecipesIndex);
            stack = this.currentRecipe.getCraftingResult(this.craftingInv);
        } else {
            stack = ItemStack.EMPTY;
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
        return this.matchingRecipes != null && this.matchingRecipesIndex < this.matchingRecipes.size() - 1;
    }

    public boolean canDecrease() {
        return this.matchingRecipesIndex > 0;
    }

    public void increase() {
        if (this.canIncrease()) {
            this.matchingRecipesIndex++;
            this.updateCraftingSlot();
        }
    }

    public void decrease() {
        if (this.canDecrease()) {
            this.matchingRecipesIndex--;
            this.updateCraftingSlot();
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(p_82846_2_);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (p_82846_2_ == 0) {
                itemstack1.onCrafting(p_82846_1_.world, p_82846_1_, itemstack1.getCount());
                if (!this.mergeItemStack(itemstack1, 1, 36, false))
                    return ItemStack.EMPTY;
                slot.onSlotChange(itemstack1, itemstack);
            } else if (p_82846_2_ < 37) {
                if (!this.mergeItemStack(itemstack1, 37, this.inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 1, 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(p_82846_1_, itemstack1);
            if (p_82846_2_ == 0) {
                p_82846_1_.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }

    public static List<SextupleRecipe> getRecipes(PlayerContainerInv inv, EnumCrafting type) {
        if (inv.getPlayer() instanceof ServerPlayerEntity) {


            return inv.getPlayer().getServer().getRecipeManager().getRecipes(CraftingUtils.getType(type), inv, ((ServerPlayerEntity) inv.getPlayer()).getServerWorld());

        }
        return Lists.newArrayList();
    }

    private static TileCrafting getTile(World world, PacketBuffer buffer) {
        TileEntity tile = world.getTileEntity(buffer.readBlockPos());
        if (tile instanceof TileCrafting) {
            return (TileCrafting) tile;
        }
        throw new IllegalStateException("Expected tile entity of type TileCrafting but got " + tile);
    }
}
