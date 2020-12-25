package com.flemmli97.runecraftory.common.inventory.container;

import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.inventory.InventorySpells;
import com.flemmli97.runecraftory.common.registry.ModContainer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class ContainerInfoScreen extends Container {

    private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{PlayerContainer.EMPTY_BOOTS_SLOT_TEXTURE, PlayerContainer.EMPTY_LEGGINGS_SLOT_TEXTURE, PlayerContainer.EMPTY_CHESTPLATE_SLOT_TEXTURE, PlayerContainer.EMPTY_HELMET_SLOT_TEXTURE};
    private static final EquipmentSlotType[] VALID_EQUIPMENT_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};

    private final byte type;
    public ContainerInfoScreen(int windowId, PlayerInventory inv, PacketBuffer data) {
        this(windowId, inv, data.readByte());
    }

    public ContainerInfoScreen(int windowId, PlayerInventory playerInventory, byte type) {
        super(ModContainer.infoContainer.get(), windowId);
        this.type = type;
        InventorySpells playerSpells = playerInventory.player.getCapability(CapabilityInsts.PlayerCap).map(cap->cap.getInv()).orElse(null);
        if(playerSpells == null)
            return;
        RecipeWrapper iinv = new RecipeWrapper(playerSpells){
            @Override
            public int getInventoryStackLimit() {
                return 1;
            }

            @Override
            public boolean isUsableByPlayer(PlayerEntity player) {
                return true;
            }
        };
        if(type == 0) {
            for (int i = 0; i < 9; ++i) {
                this.addSlot(new Slot(playerInventory, i, 12 + i * 18, 163));
            }
            for (int y = 0; y < 3; ++y) {
                for (int i = 0; i < 9; ++i) {
                    this.addSlot(new Slot(playerInventory, i + (y + 1) * 9, 12 + i * 18, 105 + y * 18));
                }
            }
        }
        for(int k = 0; k < 4; ++k) {
            EquipmentSlotType equipmentslottype = VALID_EQUIPMENT_SLOTS[k];
            this.addSlot(new Slot(playerInventory, 36 + (3 - k), -6, -12 + k * 18) {

                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public boolean isItemValid(ItemStack p_75214_1_) {
                    return p_75214_1_.canEquip(equipmentslottype, playerInventory.player);
                }

                @Override
                public boolean canTakeStack(PlayerEntity p_82869_1_) {
                    ItemStack itemstack = this.getStack();
                    return !itemstack.isEmpty() && !p_82869_1_.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(p_82869_1_);
                }

                @Override
                @OnlyIn(Dist.CLIENT)
                public Pair<ResourceLocation, ResourceLocation> getBackgroundSprite() {
                    return Pair.of(PlayerContainer.BLOCK_ATLAS_TEXTURE, ARMOR_SLOT_TEXTURES[equipmentslottype.getIndex()]);
                }
            });
        }
        this.addSlot(new Slot(playerInventory, 40, 29, 60) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> getBackgroundSprite() {
                return Pair.of(PlayerContainer.BLOCK_ATLAS_TEXTURE, PlayerContainer.EMPTY_OFFHAND_ARMOR_SLOT);
            }
        });
        for (int m = 0; m < 4; ++m) {
            this.addSlot(new Slot(iinv, m, 64, -12 + m * 18) {
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return playerSpells.isItemValid(this.getSlotIndex(), stack);
                }
            });
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity p_75145_1_) {
        return true;
    }

    @Override
    public ItemStack slotClick(int p_184996_1_, int p_184996_2_, ClickType p_184996_3_, PlayerEntity p_184996_4_) {
        if(this.type == 0)
            return super.slotClick(p_184996_1_, p_184996_2_, p_184996_3_, p_184996_4_);
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity p_82846_1_, int p_82846_2_) {
        if(this.type == 0)
            return super.transferStackInSlot(p_82846_1_, p_82846_2_);
        return ItemStack.EMPTY;
    }

    public static INamedContainerProvider create(){
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("container.crafting");
            }

            @Override
            public Container createMenu(int windowID, PlayerInventory inv, PlayerEntity player) {
                return new ContainerInfoScreen(windowID, inv, (byte) 0);
            }
        };
    }
}
