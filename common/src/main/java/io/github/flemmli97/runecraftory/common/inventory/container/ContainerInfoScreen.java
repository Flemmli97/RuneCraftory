package io.github.flemmli97.runecraftory.common.inventory.container;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.inventory.DummyInventory;
import io.github.flemmli97.runecraftory.common.inventory.InventorySpells;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.registry.ModContainer;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class ContainerInfoScreen extends AbstractContainerMenu {

    private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET};
    private static final EquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    private final boolean main;

    public ContainerInfoScreen(int windowId, Inventory playerInventory, boolean main) {
        super(main ? ModContainer.infoContainer.get() : ModContainer.infoSubContainer.get(), windowId);
        this.main = main;
        InventorySpells playerSpells = Platform.INSTANCE.getPlayerData(playerInventory.player).map(PlayerData::getInv).orElse(null);
        if (playerSpells == null)
            return;
        DummyInventory iinv = new DummyInventory(playerSpells) {
            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public boolean stillValid(Player player) {
                return true;
            }
        };
        if (this.main) {
            for (int i = 0; i < 9; ++i) {
                this.addSlot(new Slot(playerInventory, i, 32 + i * 18, 175));
            }
            for (int y = 0; y < 3; ++y) {
                for (int i = 0; i < 9; ++i) {
                    this.addSlot(new Slot(playerInventory, i + (y + 1) * 9, 32 + i * 18, 117 + y * 18));
                }
            }
        }
        for (int k = 0; k < 4; ++k) {
            EquipmentSlot equipmentslottype = VALID_EQUIPMENT_SLOTS[k];
            this.addSlot(new Slot(playerInventory, 36 + (3 - k), 14, 13 + k * 18) {

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return Platform.INSTANCE.canEquip(stack, equipmentslottype, playerInventory.player);
                }

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, ARMOR_SLOT_TEXTURES[equipmentslottype.getIndex()]);
                }

                @Override
                public boolean mayPickup(Player player) {
                    ItemStack itemstack = this.getItem();
                    return (itemstack.isEmpty() || player.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.mayPickup(player);
                }
            });
        }
        this.addSlot(new Slot(playerInventory, 40, 49, 85) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
        for (int m = 0; m < 4; ++m) {
            this.addSlot(new Slot(iinv, m, 84, 13 + m * 18) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return playerSpells.canPlaceItem(this.index, stack);
                }
            });
        }
    }

    public static MenuProvider create() {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return new TranslatableComponent("runecraftory.container.info");
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
                return new TranslatableComponent("runecraftory.container.info.sub");
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
                EquipmentSlot slotType = LivingEntity.getEquipmentSlotForItem(itemstack);
                if (slotType.getType() == EquipmentSlot.Type.ARMOR && !this.slots.get(39 - slotType.getIndex()).hasItem()) {
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
