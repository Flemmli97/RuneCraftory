package io.github.flemmli97.runecraftory.common.inventory.container;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.inventory.WrappedContainer;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.registry.ModMenuTypes;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class ContainerInfoScreen extends AbstractContainerMenu {

    public static final String TITLE = "runecraftory.container.info";
    public static final String TITLE_SUB = "runecraftory.container.info.sub";
    private static final Map<EquipmentSlot, ResourceLocation> ARMOR_SLOT_TEXTURES = Map.of(
            EquipmentSlot.HEAD, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET,
            EquipmentSlot.CHEST, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE,
            EquipmentSlot.LEGS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
            EquipmentSlot.FEET, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS);
    private static final EquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    private final boolean main;

    public ContainerInfoScreen(int windowId, Inventory playerInventory, boolean main) {
        super(main ? ModMenuTypes.INFO_CONTAINER.get() : ModMenuTypes.INFO_SUB_CONTAINER.get(), windowId);
        this.main = main;
        if (this.main) {
            for (int hotbar = 0; hotbar < 9; ++hotbar) {
                this.addSlot(new Slot(playerInventory, hotbar, 27 + hotbar * 18, 178));
            }
            for (int column = 0; column < 3; ++column) {
                for (int row = 0; row < 9; ++row) {
                    this.addSlot(new Slot(playerInventory, row + (column + 1) * 9, 27 + row * 18, 120 + column * 18));
                }
            }
        }
        for (int k = 0; k < 4; ++k) {
            EquipmentSlot equipmentslottype = VALID_EQUIPMENT_SLOTS[k];
            this.addSlot(new ArmorSlot(playerInventory, playerInventory.player, equipmentslottype, 36 + (3 - k), 9, 9 + k * 18, ARMOR_SLOT_TEXTURES.get(equipmentslottype)));
        }
        this.addSlot(new Slot(playerInventory, 40, 27, 81) {
            @Override
            public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
                playerInventory.player.onEquipItem(EquipmentSlot.OFFHAND, oldStack, newStack);
                super.setByPlayer(newStack, oldStack);
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
        WrappedContainer spellWrapped = new WrappedContainer(Platform.INSTANCE.getPlayerData(playerInventory.player).getInv());
        for (int m = 0; m < 4; ++m) {
            this.addSlot(new Slot(spellWrapped, m, 78, 9 + m * 18) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return spellWrapped.canPlaceItem(this.index, stack);
                }
            });
        }
    }

    public static MenuProvider create() {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable(TITLE);
            }

            @Override
            public AbstractContainerMenu createMenu(int windowID, Inventory inv, Player player) {
                return new ContainerInfoScreen(windowID, inv, true);
            }
        };
    }

    public static MenuProvider createSub() {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable(TITLE_SUB);
            }

            @Override
            public AbstractContainerMenu createMenu(int windowID, Inventory inv, Player player) {
                return new ContainerInfoScreen(windowID, inv, false);
            }
        };
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotID) {
        if (this.main) {
            ItemStack itemstack = ItemStack.EMPTY;
            Slot slot = this.slots.get(slotID);
            if (slot != null && slot.hasItem()) {
                ItemStack itemstack1 = slot.getItem();
                itemstack = itemstack1.copy();
                Equipable equipable = Equipable.get(itemstack);
                EquipmentSlot slotType = equipable != null ? equipable.getEquipmentSlot() : null;
                if (slotType != null && slotType.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && !this.slots.get(39 - slotType.getIndex()).hasItem()) {
                    int i = 39 - slotType.getIndex();
                    if (!this.moveItemStackTo(itemstack1, i, i + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotType == EquipmentSlot.OFFHAND && !this.slots.get(40).hasItem()) {
                    if (!this.moveItemStackTo(itemstack1, 40, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack.getItem() instanceof ItemSpell && slotID < 41 && !this.moveItemStackTo(itemstack1, 41, 45, false)) {
                    return ItemStack.EMPTY;
                } else if (slotID >= 0 && slotID < 9) {
                    if (!this.moveItemStackTo(itemstack1, 9, 36, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
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
        return ItemStack.EMPTY;
    }

    @Override
    public void clicked(int slot, int mode, ClickType type, Player player) {
        if (this.main)
            super.clicked(slot, mode, type, player);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
