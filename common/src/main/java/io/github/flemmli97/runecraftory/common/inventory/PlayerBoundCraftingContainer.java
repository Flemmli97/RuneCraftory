package io.github.flemmli97.runecraftory.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeInput;

public class PlayerBoundCraftingContainer extends WrappedContainer implements RecipeInput {

    private final Player player;

    private PlayerBoundCraftingContainer(Container inv, Player player) {
        super(inv);
        this.player = player;
    }

    public static PlayerBoundCraftingContainer create(Container inv, Player player) {
        return new PlayerBoundCraftingContainer(inv, player);
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public int size() {
        return this.getContainerSize();
    }
}
