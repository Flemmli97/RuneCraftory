package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CraftingBlockEntity extends BlockEntity implements MenuProvider {

    public static final String DISPLAY_PREFIX = "runecraftory.container.crafting.";

    private final SaveItemContainer container;
    private final EnumCrafting type;
    private int craftingIndex;

    public CraftingBlockEntity(BlockEntityType<?> blockEntityType, EnumCrafting type, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.type = type;
        this.container = new SaveItemContainer(this, 6);
    }

    public Container getContainer() {
        return this.container;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(DISPLAY_PREFIX + this.type.getId());
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.container.load(tag.getCompound("Inventory"), provider);
        this.craftingIndex = tag.getInt("Index");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Inventory", this.container.save(provider));
        tag.putInt("Index", this.craftingIndex);
    }

    public void dropContents(Level level, BlockPos pos) {
        Containers.dropContents(level, pos, this.getContainer());
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
