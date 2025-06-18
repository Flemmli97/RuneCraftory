package io.github.flemmli97.runecraftory.common.blocks.entity;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerUpgrade;
import io.github.flemmli97.runecraftory.platform.SaveItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class UpgradingCraftingBlockEntity extends CraftingBlockEntity {

    private final SaveItemContainer upgradeContainer;

    public UpgradingCraftingBlockEntity(BlockEntityType<?> blockEntityType, EnumCrafting type, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, type, blockPos, blockState);
        this.upgradeContainer = new SaveItemContainer(this, 2) {
            @Override
            public boolean canPlaceItem(int index, ItemStack stack) {
                return UpgradingCraftingBlockEntity.this.isItemValid(index, stack);
            }
        };
    }

    public Container getUpgradeInventory() {
        return this.upgradeContainer;
    }

    public abstract boolean isItemValid(int slot, ItemStack stack);

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.upgradeContainer.load(nbt.getCompound("UpgradeInventory"), provider);
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.saveAdditional(nbt, provider);
        nbt.put("UpgradeInventory", this.upgradeContainer.save(provider));
    }

    @Override
    public void dropContents(Level level, BlockPos pos) {
        super.dropContents(level, pos);
        Containers.dropContents(level, pos, this.getUpgradeInventory());
    }

    public MenuProvider upgradeMenu() {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return UpgradingCraftingBlockEntity.this.getDisplayName();
            }

            @Override
            public AbstractContainerMenu createMenu(int windowID, Inventory inventory, Player player) {
                return new ContainerUpgrade(windowID, inventory, UpgradingCraftingBlockEntity.this);
            }
        };
    }
}
