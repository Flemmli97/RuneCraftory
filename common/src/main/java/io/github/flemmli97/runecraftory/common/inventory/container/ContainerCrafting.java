package io.github.flemmli97.runecraftory.common.inventory.container;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.blocks.entity.CraftingBlockEntity;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.crafting.SpecialSextupleRecipe;
import io.github.flemmli97.runecraftory.common.inventory.PlayerBoundCraftingContainer;
import io.github.flemmli97.runecraftory.common.inventory.WrappedContainer;
import io.github.flemmli97.runecraftory.common.network.S2CCraftingRecipes;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModMenuTypes;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.mixin.AbstractContainerMenuAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
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
public class ContainerCrafting extends AbstractContainerMenu implements ContainerListener {

    private final PlayerBoundCraftingContainer craftingInv;
    private final EnumCrafting type;
    private final WrappedContainer output;
    private final CraftingBlockEntity blockEntity;
    private final DataSlot runePointCost;

    private List<RecipeHolder<? extends SextupleRecipe>> matchingRecipes;

    private List<ClientRecipeResult> matchingRecipesClient = new ArrayList<>();
    private boolean updatedRecipes;

    private RecipeHolder<? extends SextupleRecipe> currentRecipe;

    public ContainerCrafting(int windowId, Inventory inv, FriendlyByteBuf data) {
        this(windowId, inv, getTile(inv.player.level(), data));
    }

    public ContainerCrafting(int windowID, Inventory playerInv, CraftingBlockEntity blockEntity) {
        super(ModMenuTypes.CRAFTING_CONTAINER.get(), windowID);
        this.output = new WrappedContainer(new SimpleContainer(2));
        this.craftingInv = PlayerBoundCraftingContainer.create(blockEntity.getContainer(), playerInv.player);
        this.blockEntity = blockEntity;
        this.type = blockEntity.craftingType();
        this.addSlot(new CraftingOutputSlot(this.output, this, this.craftingInv, 0, 116, 35));
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
        this.addDataSlot(this.runePointCost = DataSlot.standalone());
        this.updateCraftingOutput(true);
        this.addSlotListener(this);
    }

    public static List<RecipeHolder<SextupleRecipe>> getRecipes(PlayerBoundCraftingContainer inv, EnumCrafting type) {
        if (inv.getPlayer() instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getServer().getRecipeManager()
                    .getRecipesFor(CraftingUtils.getType(type), inv, serverPlayer.serverLevel())
                    .stream()
                    .sorted(Comparator.comparingInt(r -> r.value().getCraftingLevel()))
                    .toList();
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

    public void updateCraftingOutput(boolean init) {
        if (this.craftingInv.getPlayer().level().isClientSide)
            return;
        if (this.craftingInv.refreshAndSet()) {
            this.matchingRecipes = new ArrayList<>();
            this.matchingRecipes.addAll(getRecipes(this.craftingInv, this.type));
            if (this.matchingRecipes.isEmpty()) {
                this.matchingRecipes = new ArrayList<>();
                RecipeHolder<SpecialSextupleRecipe> recipe = switch (this.type) {
                    case ARMOR, FORGE -> SpecialSextupleRecipe.SCRAP.get();
                    case CHEM -> SpecialSextupleRecipe.OBJECT_X.get();
                    case COOKING -> SpecialSextupleRecipe.FAILED_DISH.get();
                };
                if (recipe.value().matches(this.craftingInv, this.craftingInv.getPlayer().level()))
                    this.matchingRecipes.add(recipe);
            }
            this.updatedRecipes = true;
            if (!init)
                this.blockEntity.resetIndex();
        }
        this.updateCraftingSlot(init);
    }

    private void updateCraftingSlot(boolean init) {
        ItemStack trueOutput;
        ItemStack clientOutput;
        if (this.matchingRecipes != null && !this.matchingRecipes.isEmpty()) {
            if (this.updatedRecipes) {
                if (this.currentRecipe != null) {
                    int i;
                    for (i = this.matchingRecipes.size() - 1; i > 0; i--) {
                        if (this.currentRecipe.equals(this.matchingRecipes.get(i)))
                            break;
                    }
                    this.blockEntity.setIndex(i);
                } else if (!init || (this.blockEntity.craftingIndex() >= this.matchingRecipes.size()))
                    this.blockEntity.resetIndex();
            }
            this.currentRecipe = this.matchingRecipes.get(this.blockEntity.craftingIndex());
            SextupleRecipe.RecipeOutput output = SextupleRecipe.getCraftingOutput(this.craftingInv, this.currentRecipe);
            this.runePointCost.set(CraftingUtils.craftingCost(this.type, Platform.INSTANCE.getPlayerData(this.craftingInv.getPlayer()), this.currentRecipe.value(), output.bonusItems(), output.clientResult().getItem() != ModItems.UNKNOWN.get()));
            trueOutput = output.serverResult();
            clientOutput = output.clientResult();
        } else {
            trueOutput = ItemStack.EMPTY;
            clientOutput = ItemStack.EMPTY;
            this.runePointCost.set(-1);
            this.currentRecipe = null;
        }
        this.output.setItem(0, trueOutput);
        this.output.setItem(1, clientOutput);
        if (this.craftingInv.getPlayer() instanceof ServerPlayer player) {
            if (this.updatedRecipes) {
                this.sendCraftingRecipesToClient(player, Platform.INSTANCE.getPlayerData(player));
            }
            player.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 0, clientOutput));
        }
        this.updatedRecipes = false;
    }

    public void sendCraftingRecipesToClient(ServerPlayer player, PlayerData data) {
        List<Pair<Integer, ItemStack>> clientData = IntStream.range(0, this.matchingRecipes.size())
                .mapToObj(i -> {
                    RecipeHolder<SextupleRecipe> recipe = this.matchingRecipes.get(i);
                    return Pair.of(i, recipe.value() instanceof SpecialSextupleRecipe || data.getRecipeKeeper().isUnlocked(recipe) ? this.matchingRecipes.get(i).value().getResultItem() : new ItemStack(ModItems.UNKNOWN.get()));
                }).toList();
        if (!this.init)
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CCraftingRecipes(clientData, 0), player);
        else //The client wont have the gui open if it just got opened server side
            player.getServer().tell(new TickTask(1, () -> LoaderNetwork.INSTANCE.sendToPlayer(new S2CCraftingRecipes(clientData, this.currentRecipe == null ? 0 : this.matchingRecipes.indexOf(this.currentRecipe)), player)));
    }

    public RecipeHolder<? extends SextupleRecipe> getCurrentRecipe() {
        return this.currentRecipe;
    }

    public void updateCurrentRecipeIndex(int id) {
        id = Mth.clamp(id, 0, this.matchingRecipes != null ? this.matchingRecipes.size() - 1 : 0);
        this.blockEntity.setIndex(id);
        this.updateCraftingSlot();
    }

    public int runepointCost() {
        return this.runePointCost.get();
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

    public List<ClientRecipeResult> getMatchingRecipesClient() {
        return this.matchingRecipesClient;
    }

    public void setMatchingRecipesClient(List<ClientRecipeResult> matchingRecipesClient) {
        this.matchingRecipesClient = matchingRecipesClient;
    }

    @Override
    public void slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack) {
        this.updateCraftingOutput(false);
    }

    @Override
    public void dataChanged(AbstractContainerMenu containerMenu, int dataSlotIndex, int value) {
    }

    public record ClientRecipeResult(int idx, ItemStack result) {
    }
}
