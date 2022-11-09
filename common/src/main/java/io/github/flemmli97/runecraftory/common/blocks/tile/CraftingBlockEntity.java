package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerUpgrade;
import io.github.flemmli97.runecraftory.platform.ContainerBlockEntity;
import io.github.flemmli97.runecraftory.platform.SaveItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CraftingBlockEntity extends ContainerBlockEntity {

    private final SaveItemContainer inventory;
    private final EnumCrafting type;
    private int craftingIndex;

    public CraftingBlockEntity(BlockEntityType<?> blockEntityType, EnumCrafting type, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.type = type;
        this.inventory = new SaveItemContainer(this, this.type == EnumCrafting.COOKING || this.type == EnumCrafting.CHEM ? 6 : 8) {
            @Override
            public boolean canPlaceItem(int index, ItemStack stack) {
                return CraftingBlockEntity.this.isItemValid(index, stack);
            }
        };
    }

    @Override
    public Container getInventory() {
        return this.inventory;
    }

    public abstract boolean isItemValid(int slot, ItemStack stack);

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("tile.crafting." + this.type.getId());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory.load(nbt.getCompound("Inventory"));
        nbt.getInt("Index");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("Inventory", this.inventory.save());
        nbt.putInt("Index", this.craftingIndex);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowID, Inventory inventory, Player player) {
        return new ContainerCrafting(windowID, inventory, this);
    }

    public int craftingIndex() {
        return this.craftingIndex;
    }

    public void setIndex(int craftingIndex) {
        this.craftingIndex = craftingIndex;
    }

    public void resetIndex() {
        this.craftingIndex = 0;
    }

    public MenuProvider upgradeMenu() {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return CraftingBlockEntity.this.getDisplayName();
            }

            @Override
            public AbstractContainerMenu createMenu(int windowID, Inventory inventory, Player player) {
                return new ContainerUpgrade(windowID, inventory, CraftingBlockEntity.this);
            }
        };
    }

    public EnumCrafting craftingType() {
        return this.type;
    }
}
