package io.github.flemmli97.runecraftory.common.inventory.container;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.blocks.tile.CraftingBlockEntity;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.crafting.SpecialSextupleRecipe;
import io.github.flemmli97.runecraftory.common.inventory.DummyInventory;
import io.github.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import io.github.flemmli97.runecraftory.common.network.S2CCraftingRecipes;
import io.github.flemmli97.runecraftory.common.registry.ModContainer;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.mixin.AbstractContainerMenuAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
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
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Needs multiplayer testing
 */
public class ContainerCrafting extends AbstractContainerMenu {

    private final PlayerContainerInv craftingInv;
    private final EnumCrafting type;
    private final DummyInventory outPutInv;
    private final CraftingBlockEntity tile;
    private final DataSlot rpCost;
    private List<SextupleRecipe> matchingRecipes;

    private List<Pair<Integer, ItemStack>> matchingRecipesClient = new ArrayList<>();
    private boolean updatedRecipes;
    private boolean init = true;
    private SextupleRecipe currentRecipe;

    public ContainerCrafting(int windowId, Inventory inv, FriendlyByteBuf data) {
        this(windowId, inv, getTile(inv.player.level, data));
    }

    public ContainerCrafting(int windowID, Inventory playerInv, CraftingBlockEntity tile) {
        super(ModContainer.craftingContainer.get(), windowID);
        this.outPutInv = new DummyInventory(new SimpleContainer(2));
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
        this.init = false;
    }

    public static List<SextupleRecipe> getRecipes(PlayerContainerInv inv, EnumCrafting type) {
        if (inv.getPlayer() instanceof ServerPlayer serverPlayer) {
            List<SextupleRecipe> recipes = serverPlayer.getServer().getRecipeManager().getRecipesFor(CraftingUtils.getType(type), inv, serverPlayer.getLevel());
            recipes.sort(Comparator.comparingInt(SextupleRecipe::getCraftingLevel));
            return recipes;
        }
        return new ArrayList<>();
    }

    public static CraftingBlockEntity getTile(Level world, FriendlyByteBuf buffer) {
        BlockEntity blockEntity = world.getBlockEntity(buffer.readBlockPos());
        if (blockEntity instanceof CraftingBlockEntity) {
            return (CraftingBlockEntity) blockEntity;
        }
        throw new IllegalStateException("Expected tile entity of type TileCrafting but got " + blockEntity);
    }

    public EnumCrafting craftingType() {
        return this.type;
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
            if (this.matchingRecipes.isEmpty()) {
                this.matchingRecipes = new ArrayList<>();
                SpecialSextupleRecipe recipe = switch (this.type) {
                    case ARMOR, FORGE -> SpecialSextupleRecipe.scrap.get();
                    case CHEM -> SpecialSextupleRecipe.objectX.get();
                    case COOKING -> SpecialSextupleRecipe.failedDish.get();
                };
                if (recipe.matches(this.craftingInv, this.craftingInv.getPlayer().level))
                    this.matchingRecipes.add(recipe);
            }
            this.updatedRecipes = true;
            if (!init)
                this.tile.resetIndex();
        }
        this.updateCraftingSlot();
    }

    private void updateCraftingSlot() {
        ItemStack trueOutput;
        ItemStack clientOutput;
        if (this.matchingRecipes != null && this.matchingRecipes.size() > 0) {
            if (this.updatedRecipes) {
                if (this.currentRecipe != null) {
                    int i;
                    for (i = this.matchingRecipes.size() - 1; i > 0; i--) {
                        if (this.currentRecipe.equals(this.matchingRecipes.get(i)))
                            break;
                    }
                    this.tile.setIndex(i);
                } else if (!this.init || (this.tile.craftingIndex() >= this.matchingRecipes.size()))
                    this.tile.resetIndex();
            }
            this.currentRecipe = this.matchingRecipes.get(this.tile.craftingIndex());
            SextupleRecipe.RecipeOutput output = this.currentRecipe.getCraftingOutput(this.craftingInv);
            this.rpCost.set(CraftingUtils.craftingCost(this.type, Platform.INSTANCE.getPlayerData(this.craftingInv.getPlayer()).orElseThrow(EntityUtils::playerDataException), this.currentRecipe, output.bonusItems(), output.clientResult().getItem() == ModItems.unknown.get()));
            trueOutput = output.serverResult();
            clientOutput = output.clientResult();
        } else {
            trueOutput = ItemStack.EMPTY;
            clientOutput = ItemStack.EMPTY;
            this.rpCost.set(-1);
            this.currentRecipe = null;
        }
        this.outPutInv.setItem(0, trueOutput);
        this.outPutInv.setItem(1, clientOutput);
        if (this.craftingInv.getPlayer() instanceof ServerPlayer player) {
            if (this.updatedRecipes) {
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> this.sendCraftingRecipesToClient(player, data));
            }
            player.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 0, clientOutput));
        }
        this.updatedRecipes = false;
    }

    public void sendCraftingRecipesToClient(ServerPlayer player, PlayerData data) {
        List<Pair<Integer, ItemStack>> clientData = IntStream.range(0, this.matchingRecipes.size())
                .mapToObj(i -> {
                    SextupleRecipe recipe = this.matchingRecipes.get(i);
                    return Pair.of(i, data.getRecipeKeeper().isUnlockedForCrafting(recipe) ? this.matchingRecipes.get(i).getResultItem() : new ItemStack(ModItems.unknown.get()));
                }).toList();
        if (!this.init)
            Platform.INSTANCE.sendToClient(new S2CCraftingRecipes(clientData, 0), player);
        else //The client wont have the gui open if it just got opened server side
            player.getServer().tell(new TickTask(1, () -> Platform.INSTANCE.sendToClient(new S2CCraftingRecipes(clientData, this.currentRecipe == null ? 0 : this.matchingRecipes.indexOf(this.currentRecipe)), player)));
    }

    public SextupleRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }

    public void updateCurrentRecipeIndex(int id) {
        id = Mth.clamp(id, 0, this.matchingRecipes != null ? this.matchingRecipes.size() - 1 : 0);
        this.tile.setIndex(id);
        this.updateCraftingSlot();

    }

    public int rpCost() {
        return this.rpCost.get();
    }

    @Override
    public void broadcastChanges() {
        int i;
        AbstractContainerMenuAccessor acc = (AbstractContainerMenuAccessor) this;
        for (i = 0; i < this.slots.size(); ++i) {
            Slot slot = this.slots.get(i);
            ItemStack itemStack = slot instanceof CraftingOutputSlot outputSlot ? outputSlot.getStackToSync() : this.slots.get(i).getItem();
            Supplier<ItemStack> supplier = Suppliers.memoize(itemStack::copy);
            acc.doTriggerSlotListeners(i, itemStack, supplier);
            acc.doSynchronizeSlotToRemote(i, itemStack, supplier);
        }
        acc.doSynchronizeCarriedToRemote();
        for (i = 0; i < acc.getDataSlots().size(); ++i) {
            DataSlot dataSlot = acc.getDataSlots().get(i);
            int j = dataSlot.get();
            if (dataSlot.checkAndClearUpdateFlag()) {
                acc.doUpdateDataSlotListeners(i, j);
            }
            acc.doSynchronizeDataSlotToRemote(i, j);
        }
    }

    @Override
    public void sendAllDataToRemote() {
        AbstractContainerMenuAccessor acc = (AbstractContainerMenuAccessor) this;
        for (int i = 0; i < this.slots.size(); ++i) {
            Slot slot = this.slots.get(i);
            ItemStack itemStack = slot instanceof CraftingOutputSlot outputSlot ? outputSlot.getStackToSync() : this.slots.get(i).getItem();
            this.setRemoteSlot(i, itemStack.copy());
        }
        this.setRemoteCarried(this.getCarried());
        for (int i = 0; i < acc.getDataSlots().size(); ++i) {
            acc.getRemoteDataSlots().set(i, acc.getDataSlots().get(i).get());
        }
        if (acc.getSynchronizer() != null) {
            acc.getSynchronizer().sendInitialData(this, acc.getRemoteSlots(), acc.getRemoteCarried(), acc.getRemoteDataSlots().toIntArray());
        }
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
    public void removed(Player entity) {
        super.removed(entity);
    }

    @Override
    public void slotsChanged(Container inv) {
        if (inv == this.craftingInv)
            this.updateCraftingOutput(false);
        super.slotsChanged(inv);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public List<Pair<Integer, ItemStack>> getMatchingRecipesClient() {
        return this.matchingRecipesClient;
    }

    public void setMatchingRecipesClient(List<Pair<Integer, ItemStack>> matchingRecipesClient) {
        this.matchingRecipesClient = matchingRecipesClient;
    }
}
