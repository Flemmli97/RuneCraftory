package io.github.flemmli97.runecraftory.mixin;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(AbstractContainerMenu.class)
public interface AbstractContainerMenuAccessor {

    @Invoker("updateDataSlotListeners")
    void doUpdateDataSlotListeners(int i, int j);

    @Invoker("triggerSlotListeners")
    void doTriggerSlotListeners(int i, ItemStack itemStack, java.util.function.Supplier<ItemStack> supplier);

    @Invoker("synchronizeSlotToRemote")
    void doSynchronizeSlotToRemote(int i, ItemStack itemStack, java.util.function.Supplier<ItemStack> supplier);

    @Invoker("synchronizeDataSlotToRemote")
    void doSynchronizeDataSlotToRemote(int i, int j);

    @Invoker("synchronizeCarriedToRemote")
    void doSynchronizeCarriedToRemote();

    @Accessor("dataSlots")
    List<DataSlot> getDataSlots();

    @Accessor("remoteSlots")
    NonNullList<ItemStack> getRemoteSlots();

    @Accessor("remoteDataSlots")
    IntList getRemoteDataSlots();

    @Accessor("remoteCarried")
    ItemStack getRemoteCarried();

    @Accessor("synchronizer")
    ContainerSynchronizer getSynchronizer();
}
