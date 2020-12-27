package com.flemmli97.runecraftory.common.inventory.container;

import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.inventory.InventorySpells;
import com.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import com.flemmli97.runecraftory.common.registry.ModContainer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class ContainerInfoScreen extends Container {

    private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{PlayerContainer.EMPTY_BOOTS_SLOT_TEXTURE, PlayerContainer.EMPTY_LEGGINGS_SLOT_TEXTURE, PlayerContainer.EMPTY_CHESTPLATE_SLOT_TEXTURE, PlayerContainer.EMPTY_HELMET_SLOT_TEXTURE};
    private static final EquipmentSlotType[] VALID_EQUIPMENT_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};

    private final boolean main;

    public ContainerInfoScreen(int windowId, PlayerInventory playerInventory, boolean main) {
        super(main ? ModContainer.infoContainer.get() : ModContainer.infoSubContainer.get(), windowId);
        this.main = main;
        InventorySpells playerSpells = playerInventory.player.getCapability(CapabilityInsts.PlayerCap).map(cap -> cap.getInv()).orElse(null);
        if (playerSpells == null)
            return;
        RecipeWrapper iinv = new RecipeWrapper(playerSpells) {
            @Override
            public int getInventoryStackLimit() {
                return 1;
            }

            @Override
            public boolean isUsableByPlayer(PlayerEntity player) {
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
            EquipmentSlotType equipmentslottype = VALID_EQUIPMENT_SLOTS[k];
            this.addSlot(new Slot(playerInventory, 36 + (3 - k), 14, 13 + k * 18) {

                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public boolean isItemValid(ItemStack stack) {
                    return stack.canEquip(equipmentslottype, playerInventory.player);
                }

                @Override
                public boolean canTakeStack(PlayerEntity player) {
                    ItemStack itemstack = this.getStack();
                    return !itemstack.isEmpty() && !player.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(player);
                }

                @Override
                @OnlyIn(Dist.CLIENT)
                public Pair<ResourceLocation, ResourceLocation> getBackgroundSprite() {
                    return Pair.of(PlayerContainer.BLOCK_ATLAS_TEXTURE, ARMOR_SLOT_TEXTURES[equipmentslottype.getIndex()]);
                }
            });
        }
        this.addSlot(new Slot(playerInventory, 40, 49, 85) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> getBackgroundSprite() {
                return Pair.of(PlayerContainer.BLOCK_ATLAS_TEXTURE, PlayerContainer.EMPTY_OFFHAND_ARMOR_SLOT);
            }
        });
        for (int m = 0; m < 4; ++m) {
            this.addSlot(new Slot(iinv, m, 84, 13 + m * 18) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return playerSpells.isItemValid(this.getSlotIndex(), stack);
                }
            });
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack slotClick(int slot, int mode, ClickType type, PlayerEntity player) {
        if (this.main)
            return super.slotClick(slot, mode, type, player);
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotID) {
        if (this.main) {
            ItemStack itemstack = ItemStack.EMPTY;
            Slot slot = this.inventorySlots.get(slotID);
            if (slot != null && slot.getHasStack()) {
                ItemStack itemstack1 = slot.getStack();
                itemstack = itemstack1.copy();
                EquipmentSlotType slotType = MobEntity.getSlotForItemStack(itemstack);
                if (slotType.getSlotType() == EquipmentSlotType.Group.ARMOR && !this.inventorySlots.get(39 - slotType.getIndex()).getHasStack()) {
                    int i = 39 - slotType.getIndex();
                    if (!this.mergeItemStack(itemstack1, i, i + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotType == EquipmentSlotType.OFFHAND && !this.inventorySlots.get(40).getHasStack()) {
                    if (!this.mergeItemStack(itemstack1, 40, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack.getItem() instanceof ItemSpell && slotID < 41 && !this.mergeItemStack(itemstack1, 41, 45, false)) {
                    return ItemStack.EMPTY;
                } else if (slotID >= 0 && slotID < 9) {
                    if (!this.mergeItemStack(itemstack1, 9, 36, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.mergeItemStack(itemstack1, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }

                if (itemstack1.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
                } else {
                    slot.onSlotChanged();
                }

                if (itemstack1.getCount() == itemstack.getCount()) {
                    return ItemStack.EMPTY;
                }

                ItemStack itemstack2 = slot.onTake(player, itemstack1);
                if (slotID == 0) {
                    player.dropItem(itemstack2, false);
                }
            }

            return itemstack;
        }
        return ItemStack.EMPTY;
    }

    public static INamedContainerProvider create() {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("container.info");
            }

            @Override
            public Container createMenu(int windowID, PlayerInventory inv, PlayerEntity player) {
                return new ContainerInfoScreen(windowID, inv, true);
            }
        };
    }

    public static INamedContainerProvider createSub() {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("container.info.sub");
            }

            @Override
            public Container createMenu(int windowID, PlayerInventory inv, PlayerEntity player) {
                return new ContainerInfoScreen(windowID, inv, false);
            }
        };
    }
}
