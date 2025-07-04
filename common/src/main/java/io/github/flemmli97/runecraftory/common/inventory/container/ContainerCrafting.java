package io.github.flemmli97.runecraftory.common.inventory.container;

import io.github.flemmli97.runecraftory.api.enums.CraftingType;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.blocks.entity.CraftingBlockEntity;
import io.github.flemmli97.runecraftory.common.inventory.PlayerBoundCraftingContainer;
import io.github.flemmli97.runecraftory.common.inventory.WrappedContainer;
import io.github.flemmli97.runecraftory.common.network.S2CCraftingRecipes;
import io.github.flemmli97.runecraftory.common.recipes.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.recipes.SpecialSextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModMenuTypes;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ContainerCrafting extends AbstractContainerMenu {

    private final PlayerBoundCraftingContainer craftingInv;
    private final CraftingType type;
    private final WrappedContainer output;
    private final CraftingBlockEntity blockEntity;
    private final DataSlot runePointCost;
    private final CraftingOutputSlot outputSlot;

    private final IngredientsCache cache;
    private List<RecipeHolder<? extends SextupleRecipe>> matchingRecipes;
    private ClientRecipeResult matchingRecipesClient = new ClientRecipeResult(0, List.of());
    private RecipeHolder<? extends SextupleRecipe> selected;

    public ContainerCrafting(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, getTile(inv.player.level(), pos));
    }

    public ContainerCrafting(int windowID, Inventory playerInv, CraftingBlockEntity blockEntity) {
        super(ModMenuTypes.CRAFTING_CONTAINER.get(), windowID);
        this.output = new WrappedContainer(new SimpleContainer(2));
        this.craftingInv = PlayerBoundCraftingContainer.create(this, blockEntity.getContainer(), playerInv.player);
        this.cache = new IngredientsCache(this.craftingInv);
        this.blockEntity = blockEntity;
        this.type = blockEntity.craftingType();
        this.addSlot(this.outputSlot = new CraftingOutputSlot(this.output, this, this.craftingInv, 0, 116, 35));
        for (int hotbar = 0; hotbar < 9; ++hotbar) {
            this.addSlot(new Slot(playerInv, hotbar, 8 + hotbar * 18, 142));
        }
        for (int column = 0; column < 3; ++column) {
            for (int row = 0; row < 9; ++row) {
                this.addSlot(new Slot(playerInv, row + column * 9 + 9, 8 + row * 18, 84 + column * 18));
            }
        }
        for (int i = 0; i < 3; ++i) {
            this.addSlot(new Slot(this.craftingInv, i, 20 + i * 18, 26));
            this.addSlot(new Slot(this.craftingInv, i + 3, 20 + i * 18, 44));
        }
        this.addDataSlot(this.runePointCost = DataSlot.standalone());
    }

    public static CraftingBlockEntity getTile(Level world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CraftingBlockEntity) {
            return (CraftingBlockEntity) blockEntity;
        }
        throw new IllegalStateException("Expected tile entity of type TileCrafting but got " + blockEntity);
    }

    @Override
    public void setSynchronizer(ContainerSynchronizer synchronizer) {
        this.updateCraftingOutput(true);
        super.setSynchronizer(synchronizer);
    }

    public static List<RecipeHolder<SextupleRecipe>> getRecipes(PlayerBoundCraftingContainer inv, CraftingType type) {
        if (inv.getPlayer() instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getServer().getRecipeManager()
                    .getRecipesFor(CraftingUtils.getType(type), inv, serverPlayer.serverLevel())
                    .stream()
                    .filter(h -> SextupleRecipe.getCraftingOutput(inv, h) != null)
                    .sorted(Comparator.comparingInt(r -> r.value().getCraftingLevel()))
                    .toList();
        }
        return new ArrayList<>();
    }

    public CraftingType craftingType() {
        return this.type;
    }

    public void updateCraftingOutput(boolean init) {
        if (this.craftingInv.getPlayer().level().isClientSide)
            return;
        if (this.cache.changedAndUpdate()) {
            this.matchingRecipes = new ArrayList<>();
            this.matchingRecipes.addAll(getRecipes(this.craftingInv, this.type));
            if (this.matchingRecipes.isEmpty()) {
                RecipeHolder<SpecialSextupleRecipe> recipe = switch (this.type) {
                    case ACCESSORY_WORKBENCH, FORGE -> SpecialSextupleRecipe.SCRAP.get();
                    case CHEMISTRY_SET -> SpecialSextupleRecipe.OBJECT_X.get();
                    case COOKING_TABLE -> SpecialSextupleRecipe.FAILED_DISH.get();
                };
                if (recipe.value().matches(this.craftingInv, this.craftingInv.getPlayer().level()))
                    this.matchingRecipes.add(recipe);
            }
            if (!init)
                this.blockEntity.resetIndex();
        }
        this.updateCraftingSlot(init, true);
    }

    private void updateCraftingSlot(boolean init, boolean recipeChanged) {
        ItemStack trueOutput;
        ItemStack clientOutput;
        if (this.matchingRecipes != null && !this.matchingRecipes.isEmpty()) {
            if (recipeChanged) {
                if (this.selected != null) {
                    int i;
                    for (i = this.matchingRecipes.size() - 1; i > 0; i--) {
                        if (this.selected.equals(this.matchingRecipes.get(i)))
                            break;
                    }
                    this.blockEntity.setIndex(i);
                } else if (!init || (this.blockEntity.craftingIndex() >= this.matchingRecipes.size()))
                    this.blockEntity.resetIndex();
            }
            this.selected = this.matchingRecipes.get(this.blockEntity.craftingIndex());
            SextupleRecipe.RecipeOutput output = SextupleRecipe.getCraftingOutput(this.craftingInv, this.selected);
            if (output == null) {
                trueOutput = ItemStack.EMPTY;
                clientOutput = ItemStack.EMPTY;
                this.runePointCost.set(-1);
                this.selected = null;
            } else {
                this.runePointCost.set(CraftingUtils.craftingCost(this.type, Platform.INSTANCE.getPlayerData(this.craftingInv.getPlayer()), this.selected.value(), output.bonusItems(), output.clientResult().getItem() != ModItems.UNKNOWN.get()));
                trueOutput = output.serverResult();
                clientOutput = output.clientResult();
            }
        } else {
            trueOutput = ItemStack.EMPTY;
            clientOutput = ItemStack.EMPTY;
            this.runePointCost.set(-1);
            this.selected = null;
        }
        this.output.setItem(0, trueOutput);
        this.output.setItem(1, clientOutput);
        if (this.craftingInv.getPlayer() instanceof ServerPlayer player) {
            if (recipeChanged) {
                this.sendCraftingRecipesToClient(player, Platform.INSTANCE.getPlayerData(player));
            }
            player.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 0, clientOutput));
        }
    }

    public void sendCraftingRecipesToClient(ServerPlayer player, PlayerData data) {
        List<ItemStack> clientData = this.matchingRecipes.stream()
                .map(recipe -> recipe.value() instanceof SpecialSextupleRecipe || data.getRecipeKeeper().isUnlocked(recipe)
                        ? recipe.value().getResultItem(player.registryAccess()) : new ItemStack(ModItems.UNKNOWN.get())).toList();
        LoaderNetwork.INSTANCE.sendToPlayer(new S2CCraftingRecipes(new ClientRecipeResult(player.level().getGameTime(), clientData), this.selected == null ? 0 : this.matchingRecipes.indexOf(this.selected)), player);
    }

    public void updateCurrentRecipeIndex(int id) {
        id = Mth.clamp(id, 0, this.matchingRecipes != null ? this.matchingRecipes.size() - 1 : 0);
        this.blockEntity.setIndex(id);
        this.updateCraftingSlot(false, false);
    }

    public RecipeHolder<? extends SextupleRecipe> getSelected() {
        return this.selected;
    }

    public int runepointCost() {
        return this.runePointCost.get();
    }

    @Override
    public void broadcastChanges() {
        this.outputSlot.setSyncState(true);
        super.broadcastChanges();
        this.outputSlot.setSyncState(false);
    }

    @Override
    public void sendAllDataToRemote() {
        this.outputSlot.setSyncState(true);
        super.sendAllDataToRemote();
        this.outputSlot.setSyncState(false);
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
                Platform.INSTANCE.getPlayerData(player).onCrafted(player);
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
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void slotsChanged(Container container) {
        this.updateCraftingOutput(false);
        super.slotsChanged(container);
    }

    public ClientRecipeResult getMatchingRecipesClient() {
        return this.matchingRecipesClient;
    }

    public void setMatchingRecipesClient(ClientRecipeResult matchingRecipesClient) {
        this.matchingRecipesClient = matchingRecipesClient;
    }

    public record ClientRecipeResult(long lastChange, List<ItemStack> result) {

    }
}
