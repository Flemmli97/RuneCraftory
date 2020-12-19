package com.flemmli97.runecraftory.common.inventory.container;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.blocks.tile.TileCrafting;
import com.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import com.flemmli97.runecraftory.common.inventory.DummyInventory;
import com.flemmli97.runecraftory.common.inventory.InvWrapperPlayer;
import com.flemmli97.runecraftory.common.registry.ModContainer;
import com.flemmli97.runecraftory.common.utils.CraftingUtils;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class ContainerCrafting extends Container {

    private List<SextupleRecipe> matchingRecipes;
    private int matchingRecipesIndex;
    private final InvWrapperPlayer craftingInv;
    private final EnumCrafting type;
    private final DummyInventory outPutInv;

    public ContainerCrafting(int p_i50099_1_, PlayerInventory p_i50099_2_) {
        this(p_i50099_1_, p_i50099_2_, null);
    }

    public ContainerCrafting(int p_i50105_2_, PlayerInventory playerInv, TileCrafting tile) {
        super(ModContainer.craftingContainer.get(), p_i50105_2_);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }
        if(tile != null && playerInv.player instanceof ServerPlayerEntity) {
            this.craftingInv = InvWrapperPlayer.create(tile, playerInv.player);
            this.type = tile.craftingType();
        }
        else{
            this.type = EnumCrafting.COOKING;
            this.craftingInv = InvWrapperPlayer.create(new EmptyHandler(){
                @Override
                public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                    return true;
                }
            }, playerInv.player);
        }
        for (int i = 0; i < 3; ++i) {
            this.addSlot(new Slot(this.craftingInv, i, 20 + i * 18, 26));
        }
        for (int i = 0; i < 3; ++i) {
            this.addSlot(new Slot(this.craftingInv, i + 3, 20 + i * 18, 44));
        }
        this.outPutInv = new DummyInventory(new ItemStackHandler());
        this.addSlot(new CraftingOutputSlot(this.outPutInv, playerInv.player, 0, 20 * 18, 84));
    }

    @Override
    public boolean canInteractWith(PlayerEntity p_75145_1_) {
        return true;
    }

    public void updateCraftingOutput(){
        this.matchingRecipes = getRecipes(this.craftingInv, this.type);
        this.matchingRecipesIndex = 0;
        this.updateCraftingSlot();
    }

    public void updateCraftingSlot(){
        if(this.matchingRecipes != null && this.matchingRecipesIndex < this.matchingRecipes.size()) {
            ItemStack stack = this.matchingRecipes.get(0).getCraftingResult(this.craftingInv);
            this.outPutInv.setInventorySlotContents(0, stack);
            if(this.craftingInv.getPlayer() instanceof ServerPlayerEntity)
                ((ServerPlayerEntity) this.craftingInv.getPlayer()).connection.sendPacket(new SSetSlotPacket(this.windowId, 0, stack));
        }
    }

    public boolean canIncrease(){
        return this.matchingRecipes != null && this.matchingRecipesIndex < this.matchingRecipes.size()-1;
    }

    public boolean canDecrease(){
        return this.matchingRecipesIndex > 0;
    }

    public void increase(){
        if(this.canIncrease()) {
            this.matchingRecipesIndex++;
            this.updateCraftingSlot();
        }
    }

    public void decrease(){
        if(this.canDecrease()) {
            this.matchingRecipesIndex--;
            this.updateCraftingSlot();
        }
    }

    public static List<SextupleRecipe> getRecipes(InvWrapperPlayer inv, EnumCrafting type){
        if(inv.getPlayer() instanceof ServerPlayerEntity)
            return inv.getPlayer().getServer().getRecipeManager().getRecipes(CraftingUtils.getType(type), inv, ((ServerPlayerEntity) inv.getPlayer()).getServerWorld());
        return Lists.newArrayList();
    }
}
