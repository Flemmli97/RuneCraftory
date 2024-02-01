package io.github.flemmli97.runecraftory.common.inventory.container;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.job.EnumShopResult;
import io.github.flemmli97.runecraftory.common.inventory.InventoryShop;
import io.github.flemmli97.runecraftory.common.network.S2CShopResponses;
import io.github.flemmli97.runecraftory.common.registry.ModContainer;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class ContainerShop extends AbstractContainerMenu {

    private final InventoryShop invShop;
    private final DataSlot price;
    private final DataSlot next;
    private final DataSlot prev;

    public ContainerShop(int windowID, Inventory playerInv, FriendlyByteBuf buffer) {
        this(windowID, playerInv, read(playerInv.player.level, buffer));
    }

    public ContainerShop(int windowID, Inventory playerInv, InventoryShop invShop) {
        super(ModContainer.shopContainer.get(), windowID);
        this.invShop = invShop;
        if (this.invShop == null)
            throw new IllegalStateException("Tried creating a shop container but shop inventory was null");
        for (int y = 0; y < 5; ++y) {
            for (int x = 0; x < 6; ++x) {
                this.addSlot(new Slot(this.invShop, x + y * 5, 13 + x * 21, 28 + y * 21) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }

                    @Override
                    public boolean mayPickup(Player player) {
                        return false;
                    }
                });
            }
        }
        this.addSlot(new Slot(this.invShop, InventoryShop.shopSize, 200, 144) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public boolean mayPickup(Player player) {
                return false;
            }
        });
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 13 + j * 18, 135 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInv, k, 13 + k * 18, 193));
        }
        this.addDataSlot(this.price = DataSlot.standalone());
        this.addDataSlot(this.next = DataSlot.standalone());
        this.addDataSlot(this.prev = DataSlot.standalone());
        this.next.set(this.invShop.hasNext() ? 1 : 0);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index <= InventoryShop.shopSize) {
            return ItemStack.EMPTY;
        }
        Slot slot = this.slots.get(index);
        ItemStack slotItem = slot.getItem();
        ItemStack slotCopy = slotItem.copy();
        if (index < 58) {
            if (!this.moveItemStackTo(slotItem, 58, 67, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(slotItem, 31, 58, false)) {
            return ItemStack.EMPTY;
        }
        if (slotItem.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (slotItem.getCount() == slotCopy.getCount()) {
            return ItemStack.EMPTY;
        }
        return slotCopy;
    }

    @Override
    public void clicked(int slot, int mouse, ClickType clickType, Player player) {
        if (slot > InventoryShop.shopSize || slot < 0) {
            super.clicked(slot, mouse, clickType, player);
            return;
        }
        if (slot == InventoryShop.shopSize) {
            Slot shopOutput = this.getSlot(InventoryShop.shopSize);
            if (shopOutput.hasItem() && player instanceof ServerPlayer serverPlayer) {
                EnumShopResult res = ItemUtils.buyItem(player, this.invShop.npc, shopOutput.getItem().copy());
                Component txt = switch (res) {
                    case NOMONEY -> new TranslatableComponent("npc.shop.money.no");
                    case NOSPACE -> new TranslatableComponent("npc.shop.inventory.full");
                    case SUCCESS -> new TranslatableComponent("npc.shop.success");
                };
                Platform.INSTANCE.sendToClient(new S2CShopResponses(txt), serverPlayer);
                if (res == EnumShopResult.SUCCESS)
                    shopOutput.set(ItemStack.EMPTY);
            }
            return;
        }
        if (clickType == ClickType.QUICK_MOVE || clickType == ClickType.PICKUP || clickType == ClickType.PICKUP_ALL) {
            ItemStack clickedStack = this.getSlot(slot).getItem();
            if (clickedStack.isEmpty())
                return;
            Slot shopOutput = this.getSlot(InventoryShop.shopSize);
            int count = clickType == ClickType.QUICK_MOVE ? 10 : 1;
            if (mouse == 1)
                count = -count;
            boolean changed = false;
            if (!shopOutput.hasItem() || !ItemStack.isSameItemSameTags(clickedStack, shopOutput.getItem())) {
                if (count > 0) {
                    ItemStack copy = clickedStack.copy();
                    copy.setCount(count);
                    shopOutput.set(copy);
                    changed = true;
                }
            } else {
                shopOutput.getItem().setCount(Math.min(shopOutput.getItem().getMaxStackSize() * 36, shopOutput.getItem().getCount() + count));
                changed = true;
            }
            if (changed) {
                if (shopOutput.hasItem())
                    this.price.set(ItemUtils.getBuyPrice(shopOutput.getItem()) * shopOutput.getItem().getCount());
                else
                    this.price.set(0);
            }
            this.broadcastChanges();
        }
    }

    public int getCurrentCost() {
        return this.price.get();
    }

    public EntityNPCBase getShopOwner() {
        return this.invShop.npc;
    }

    private static InventoryShop read(Level level, FriendlyByteBuf buf) {
        Entity entity = level.getEntity(buf.readInt());
        if (entity instanceof EntityNPCBase npc) {
            NonNullList<ItemStack> list = NonNullList.create();
            int size = buf.readInt();
            for (int i = 0; i < size; i++)
                list.add(buf.readWithCodec(ItemStack.CODEC));
            return new InventoryShop(npc, list);
        }
        return null;
    }

    public void prev() {
        this.invShop.prev();
        this.broadcastChanges();
        this.next.set(this.invShop.hasPrev() ? 1 : 0);
    }

    public void next() {
        this.invShop.next();
        this.broadcastChanges();
        this.next.set(this.invShop.hasNext() ? 1 : 0);
    }

    public boolean hasNext() {
        return this.next.get() == 1;
    }

    public boolean hasPrev() {
        return this.prev.get() == 1;
    }
}