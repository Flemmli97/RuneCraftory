package io.github.flemmli97.runecraftory.integration.rei;

import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoContext;
import me.shedaniel.rei.api.common.transfer.info.simple.SimplePlayerInventoryMenuInfo;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("removal")
public record SextupleMenuProvider(
        @NotNull SextupleDisplay display) implements SimplePlayerInventoryMenuInfo<ContainerCrafting, SextupleDisplay> {

    public SextupleMenuProvider(SextupleDisplay display) {
        this.display = display;
    }

    @Override
    public Iterable<SlotAccessor> getInputSlots(MenuInfoContext<ContainerCrafting, ?, SextupleDisplay> context) {
        int size = context.getMenu().slots.size();
        return IntStream.range(size - 6, size)
                .mapToObj(value -> SlotAccessor.fromSlot(context.getMenu().getSlot(value)))
                .collect(Collectors.toList());
    }

    @Override
    public SextupleDisplay getDisplay() {
        return this.display;
    }

}
