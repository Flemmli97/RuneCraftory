package io.github.flemmli97.runecraftory.common.inventory.container;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CraftingOutputSlot extends Slot {

    private final PlayerContainerInv ingredientInv;
    private final ContainerCrafting craftingContainer;
    private int amountCrafted;
    private final int id;

    public CraftingOutputSlot(Container output, ContainerCrafting container, PlayerContainerInv ingredientInv, int id, int x, int y) {
        super(output, id, x, y);
        this.ingredientInv = ingredientInv;
        this.craftingContainer = container;
        this.id = id;
    }

    public ItemStack getStackToSync() {
        return this.container.getItem(this.id + 1);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.amountCrafted += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void onSwapCraft(int amount) {
        super.onSwapCraft(amount);
        this.amountCrafted += amount;
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        Player player = this.ingredientInv.getPlayer();
        if (this.amountCrafted > 0) {
            stack.onCraftedBy(player.level, player, this.amountCrafted);
            Platform.INSTANCE.craftingEvent(player, stack, this.ingredientInv);
            Platform.INSTANCE.getPlayerData(player).ifPresent(d -> d.onCrafted(player));
        }
        this.amountCrafted = 0;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        if (!(player instanceof ServerPlayer serverPlayer))
            return;
        NonNullList<ItemStack> remaining = this.craftingContainer.getCurrentRecipe() != null ? this.craftingContainer.getCurrentRecipe().getRemainingItems(this.ingredientInv) : NonNullList.withSize(0, ItemStack.EMPTY);
        if (this.craftingContainer.rpCost() >= 0)
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                data.decreaseRunePoints(player, this.craftingContainer.rpCost(), true);
                SextupleRecipe recipe = this.craftingContainer.getCurrentRecipe();
                if (recipe != null && !recipe.isSpecial() && !data.getRecipeKeeper().isUnlocked(recipe)) {
                    data.getRecipeKeeper().unlockRecipe(player, recipe);
                    this.craftingContainer.sendCraftingRecipesToClient(serverPlayer, data);
                }
                switch (this.craftingContainer.craftingType()) {
                    case FORGE ->
                            CraftingUtils.giveCraftingXPTo(serverPlayer, data, EnumSkills.FORGING, this.craftingContainer.getCurrentRecipe());
                    case ARMOR ->
                            CraftingUtils.giveCraftingXPTo(serverPlayer, data, EnumSkills.CRAFTING, this.craftingContainer.getCurrentRecipe());
                    case CHEM ->
                            CraftingUtils.giveCraftingXPTo(serverPlayer, data, EnumSkills.CHEMISTRY, this.craftingContainer.getCurrentRecipe());
                    case COOKING ->
                            CraftingUtils.giveCraftingXPTo(serverPlayer, data, EnumSkills.COOKING, this.craftingContainer.getCurrentRecipe());
                }
            });

        boolean refreshRecipe = false;
        for (int i = 0; i < remaining.size(); ++i) {
            ItemStack itemstack = this.ingredientInv.getItem(i);
            ItemStack remainingStack = remaining.get(i);
            if (!itemstack.isEmpty()) {
                this.ingredientInv.removeItem(i, 1);
                itemstack = this.ingredientInv.getItem(i);
                if (itemstack.isEmpty())
                    refreshRecipe = true;
            }

            if (!remainingStack.isEmpty()) {
                if (itemstack.isEmpty()) {
                    this.ingredientInv.setItem(i, remainingStack);
                } else if (ItemStack.isSame(itemstack, remainingStack) && ItemStack.tagMatches(itemstack, remainingStack)) {
                    remainingStack.grow(itemstack.getCount());
                    this.ingredientInv.setItem(i, remainingStack);
                } else if (!player.getInventory().add(remainingStack)) {
                    player.drop(remainingStack, false);
                }
            }
        }
        if (refreshRecipe)
            this.craftingContainer.slotsChanged(this.ingredientInv);
        super.onTake(player, stack);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.amountCrafted += Math.min(amount, this.getItem().getCount());
        }
        return super.remove(amount);
    }

    @Override
    public boolean mayPickup(Player player) {
        if (!GeneralConfig.useRP)
            return true;
        return (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(data -> data.getMaxRunePoints() >= this.craftingContainer.rpCost()).orElse(false));
    }
}
