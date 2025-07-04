package io.github.flemmli97.runecraftory.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.function.Consumer;

public class PlayerBoundCraftingContainer extends WrappedContainer implements RecipeInput {

    private final Player player;

    private PlayerBoundCraftingContainer(Container container, Player player, Consumer<Container> listener) {
        super(container, listener);
        this.player = player;
    }

    public static PlayerBoundCraftingContainer create(AbstractContainerMenu menu, Container container, Player player) {
        return new PlayerBoundCraftingContainer(container, player, menu::slotsChanged);
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public int size() {
        return this.getContainerSize();
    }
}
