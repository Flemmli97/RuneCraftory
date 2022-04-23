package io.github.flemmli97.runecraftory.common.inventory.container;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.blocks.tile.CraftingBlockEntity;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.inventory.DummyInventory;
import io.github.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import io.github.flemmli97.runecraftory.common.registry.ModContainer;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Needs multiplayer testing
 */
public class ContainerCrafting extends AbstractContainerMenu {

    private List<SextupleRecipe> matchingRecipes;
    private SextupleRecipe currentRecipe;
    private final PlayerContainerInv craftingInv;
    private final EnumCrafting type;
    private final DummyInventory outPutInv;
    private final CraftingBlockEntity tile;
    private final DataSlot rpCost;

    public ContainerCrafting(int windowId, Inventory inv, FriendlyByteBuf data) {
        this(windowId, inv, getTile(inv.player.level, data));
    }

    public ContainerCrafting(int windowID, Inventory playerInv, CraftingBlockEntity tile) {
        super(ModContainer.craftingContainer.get(), windowID);
        this.outPutInv = new DummyInventory(new SimpleContainer(1));
        this.craftingInv = PlayerContainerInv.create(this, tile.getInventory(), playerInv.player);
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
        this.addDataSlot(this.rpCost = DataSlot.standalone());
        this.initCraftingMatrix(this.craftingInv);
    }

    public EnumCrafting craftingType() {
        return this.type;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void slotsChanged(Container inv) {
        if (inv == this.craftingInv)
            this.updateCraftingOutput(false);
        super.slotsChanged(inv);
    }

    private void initCraftingMatrix(Container inv) {
        if (inv == this.craftingInv)
            this.updateCraftingOutput(true);
    }

    public void updateCraftingOutput(boolean init) {
        if (this.craftingInv.getPlayer().level.isClientSide)
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
            this.rpCost.set(CraftingUtils.craftingCost(this.type, Platform.INSTANCE.getPlayerData(this.craftingInv.getPlayer()).orElseThrow(EntityUtils::playerDataException), this.currentRecipe));
            stack = this.currentRecipe.assemble(this.craftingInv);
        } else {
            stack = ItemStack.EMPTY;
            this.rpCost.set(-1);
            this.currentRecipe = null;
        }
        this.outPutInv.setItem(0, stack);
        if (this.craftingInv.getPlayer() instanceof ServerPlayer)
            ((ServerPlayer) this.craftingInv.getPlayer()).connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 0, stack));
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
    public ItemStack quickMoveStack(Player player, int slotID) {
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
    public void removed(Player entity) {
        super.removed(entity);
    }

    public static List<SextupleRecipe> getRecipes(PlayerContainerInv inv, EnumCrafting type) {
        if (inv.getPlayer() instanceof ServerPlayer serverPlayer)
            return inv.getPlayer().getServer().getRecipeManager().getRecipesFor(CraftingUtils.getType(type), inv, serverPlayer.getLevel());
        return new ArrayList<>();
    }

    public static CraftingBlockEntity getTile(Level world, FriendlyByteBuf buffer) {
        BlockEntity blockEntity = world.getBlockEntity(buffer.readBlockPos());
        if (blockEntity instanceof CraftingBlockEntity) {
            return (CraftingBlockEntity) blockEntity;
        }
        throw new IllegalStateException("Expected tile entity of type TileCrafting but got " + blockEntity);
    }
}
