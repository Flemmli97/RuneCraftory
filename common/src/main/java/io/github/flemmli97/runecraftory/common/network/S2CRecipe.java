package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public record S2CRecipe(Collection<ResourceLocation> recipes,
                        boolean remove) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_recipe");

    public static S2CRecipe read(FriendlyByteBuf buf) {
        boolean remove = buf.readBoolean();
        Set<ResourceLocation> recs = new HashSet<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
            recs.add(buf.readResourceLocation());
        return new S2CRecipe(recs, remove);
    }

    public static void handle(S2CRecipe pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            if (pkt.remove) {
                data.getRecipeKeeper().lockRecipesRes(player, pkt.recipes);
            } else {
                data.getRecipeKeeper().unlockRecipesRes(player, pkt.recipes);
                ClientHandlers.recipeToast(pkt.recipes);
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.remove);
        buf.writeInt(this.recipes.size());
        this.recipes.forEach(buf::writeResourceLocation);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
