package io.github.flemmli97.runecraftory.common.inventory.container;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CraftingOutputSlot extends Slot {

    private int amountCrafted;
    private final PlayerContainerInv ingredientInv;
    private final ContainerCrafting container;

    public CraftingOutputSlot(IInventory output, ContainerCrafting container, PlayerContainerInv ingredientInv, int id, int x, int y) {
        super(output, id, x, y);
        this.ingredientInv = ingredientInv;
        this.container = container;
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.amountCrafted += amount;
        this.onCrafting(stack);
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        PlayerEntity player = this.ingredientInv.getPlayer();
        if (this.amountCrafted > 0) {
            stack.onCrafting(player.world, player, this.amountCrafted);
            net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent(player, stack, this.inventory);
        }
        this.amountCrafted = 0;
    }

    @Override
    protected void onSwapCraft(int amount) {
        super.onSwapCraft(amount);
        this.amountCrafted += amount;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(amount, this.getStack().getCount());
        }
        return super.decrStackSize(amount);
    }

    @Override
    public boolean canTakeStack(PlayerEntity player) {
        if (player.world.isRemote)
            return true;
        return (player.isCreative() || player.getCapability(CapabilityInsts.PlayerCap).map(cap -> cap.getMaxRunePoints() >= this.container.rpCost()).orElse(false));
    }

    @Override
    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
        this.onCrafting(stack);
        if (player.world.isRemote)
            return ItemStack.EMPTY;
        NonNullList<ItemStack> remaining = this.container.getCurrentRecipe() != null ? this.container.getCurrentRecipe().getRemainingItems(this.ingredientInv) : NonNullList.withSize(0, ItemStack.EMPTY);
        if (this.container.rpCost() >= 0)
            player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                cap.decreaseRunePoints(player, this.container.rpCost(), true);
                if (this.container.getCurrentRecipe() != null)
                    switch (this.container.craftingType()) {
                        case FORGE:
                            LevelCalc.levelSkill((ServerPlayerEntity) player, cap, EnumSkills.FORGING, 1 + Math.min(0, this.container.getCurrentRecipe().getCraftingLevel() - cap.getSkillLevel(EnumSkills.FORGING)[0]) * 0.5f);
                            break;
                        case ARMOR:
                            LevelCalc.levelSkill((ServerPlayerEntity) player, cap, EnumSkills.CRAFTING, 1 + Math.min(0, this.container.getCurrentRecipe().getCraftingLevel() - cap.getSkillLevel(EnumSkills.CRAFTING)[0]) * 0.5f);
                            break;
                        case CHEM:
                            LevelCalc.levelSkill((ServerPlayerEntity) player, cap, EnumSkills.CHEMISTRY, 1 + Math.min(0, this.container.getCurrentRecipe().getCraftingLevel() - cap.getSkillLevel(EnumSkills.CHEMISTRY)[0]) * 0.5f);
                            break;
                        case COOKING:
                            LevelCalc.levelSkill((ServerPlayerEntity) player, cap, EnumSkills.COOKING, 1 + Math.min(0, this.container.getCurrentRecipe().getCraftingLevel() - cap.getSkillLevel(EnumSkills.COOKING)[0]) * 0.5f);
                            break;
                    }
            });

        boolean refreshRecipe = false;
        for (int i = 0; i < remaining.size(); ++i) {
            ItemStack itemstack = this.ingredientInv.getStackInSlot(i);
            ItemStack remainingStack = remaining.get(i);
            if (!itemstack.isEmpty()) {
                this.ingredientInv.decrStackSize(i, 1);
                itemstack = this.ingredientInv.getStackInSlot(i);
                if (itemstack.isEmpty())
                    refreshRecipe = true;
            }

            if (!remainingStack.isEmpty()) {
                if (itemstack.isEmpty()) {
                    this.ingredientInv.setInventorySlotContents(i, remainingStack);
                } else if (ItemStack.areItemsEqual(itemstack, remainingStack) && ItemStack.areItemStackTagsEqual(itemstack, remainingStack)) {
                    remainingStack.grow(itemstack.getCount());
                    this.ingredientInv.setInventorySlotContents(i, remainingStack);
                } else if (!player.inventory.addItemStackToInventory(remainingStack)) {
                    player.dropItem(remainingStack, false);
                }
            }
        }
        if (refreshRecipe)
            this.container.onCraftMatrixChanged(this.ingredientInv);
        return super.onTake(player, stack);
    }
}
