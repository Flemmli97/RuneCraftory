package io.github.flemmli97.runecraftory.common.inventory.container;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.inventory.PlayerBoundCraftingContainer;
import io.github.flemmli97.runecraftory.common.recipes.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.recipes.SpecialSextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class CraftingOutputSlot extends Slot {

    private final PlayerBoundCraftingContainer ingredientInv;
    private final ContainerCrafting craftingContainer;
    private final int id;
    private int amountCrafted;
    private boolean prepareForSync;

    public CraftingOutputSlot(Container output, ContainerCrafting container, PlayerBoundCraftingContainer ingredientInv, int id, int x, int y) {
        super(output, id, x, y);
        this.ingredientInv = ingredientInv;
        this.craftingContainer = container;
        this.id = id;
    }

    public void setSyncState(boolean syncing) {
        this.prepareForSync = syncing;
    }

    @Override
    public ItemStack getItem() {
        return this.prepareForSync ? this.container.getItem(this.id + 1) : super.getItem();
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
            stack.onCraftedBy(player.level(), player, this.amountCrafted);
            Platform.INSTANCE.craftingEvent(player, stack, this.ingredientInv);
            Platform.INSTANCE.getPlayerData(player).onCrafted(player);
        }
        this.amountCrafted = 0;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        if (!(player instanceof ServerPlayer serverPlayer))
            return;
        NonNullList<ItemStack> remaining = this.craftingContainer.getSelected() != null ? this.craftingContainer.getSelected().value().getRemainingItems(this.ingredientInv) : NonNullList.withSize(0, ItemStack.EMPTY);
        if (this.craftingContainer.runepointCost() >= 0) {
            PlayerData data = Platform.INSTANCE.getPlayerData(player);
            data.useRunePoints(this.craftingContainer.runepointCost(), true);
            RecipeHolder<? extends SextupleRecipe> recipe = this.craftingContainer.getSelected();
            if (recipe != null && !(recipe.value() instanceof SpecialSextupleRecipe) && !data.getRecipeKeeper().isUnlocked(recipe)) {
                data.getRecipeKeeper().unlockRecipe(player, recipe);
                this.craftingContainer.sendCraftingRecipesToClient(serverPlayer, data);
            }
            switch (this.craftingContainer.craftingType()) {
                case FORGE ->
                        CraftingUtils.giveCraftingXPTo(data, EnumSkills.FORGING, this.craftingContainer.getSelected().value());
                case ACCESSORY_WORKBENCH ->
                        CraftingUtils.giveCraftingXPTo(data, EnumSkills.CRAFTING, this.craftingContainer.getSelected().value());
                case CHEMISTRY_SET ->
                        CraftingUtils.giveCraftingXPTo(data, EnumSkills.CHEMISTRY, this.craftingContainer.getSelected().value());
                case COOKING_TABLE ->
                        CraftingUtils.giveCraftingXPTo(data, EnumSkills.COOKING, this.craftingContainer.getSelected().value());
            }
        }
        if (ItemNBT.usedLightOre(stack))
            ModCriteria.LIGHT_ORE.get().trigger(serverPlayer);
        switch (this.craftingContainer.craftingType()) {
            case FORGE -> ModCriteria.FORGING.get().trigger(serverPlayer);
            case ACCESSORY_WORKBENCH -> ModCriteria.CRAFTING.get().trigger(serverPlayer);
            case CHEMISTRY_SET -> ModCriteria.MEDICINE.get().trigger(serverPlayer);
            case COOKING_TABLE -> ModCriteria.COOKING.get().trigger(serverPlayer);
        }
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
                } else if (ItemStack.isSameItemSameComponents(itemstack, remainingStack)) {
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
        if (!GeneralConfig.useRp)
            return true;
        return (player.isCreative() || Platform.INSTANCE.getPlayerData(player).getMaxRunePoints() >= this.craftingContainer.runepointCost());
    }
}
