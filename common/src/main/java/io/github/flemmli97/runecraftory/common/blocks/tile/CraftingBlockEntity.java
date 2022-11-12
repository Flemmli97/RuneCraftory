package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import io.github.flemmli97.runecraftory.platform.SaveItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CraftingBlockEntity extends BlockEntity implements MenuProvider {

    private final SaveItemContainer inventory;
    private final EnumCrafting type;
    private int craftingIndex;

    public CraftingBlockEntity(BlockEntityType<?> blockEntityType, EnumCrafting type, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.type = type;
        this.inventory = new SaveItemContainer(this, 6);
    }

    public Container getInventory() {
        return this.inventory;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("tile.crafting." + this.type.getId());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory.load(nbt.getCompound("Inventory"));
        this.craftingIndex = nbt.getInt("Index");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("Inventory", this.inventory.save());
        nbt.putInt("Index", this.craftingIndex);
    }

    public void dropContents(Level level, BlockPos pos) {
        Containers.dropContents(level, pos, this.getInventory());
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

    public EnumCrafting craftingType() {
        return this.type;
    }
}
