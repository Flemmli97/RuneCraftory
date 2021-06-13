package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class S2CRecipe {

    private final Collection<ResourceLocation> recipes;
    private final boolean remove;

    public S2CRecipe(Collection<ResourceLocation> recipes, boolean remove) {
        this.recipes = recipes;
        this.remove = remove;
    }

    public static S2CRecipe read(PacketBuffer buf) {
        boolean remove = buf.readBoolean();
        Set<ResourceLocation> recs = new HashSet<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
            recs.add(buf.readResourceLocation());
        return new S2CRecipe(recs, remove);
    }

    public static void write(S2CRecipe pkt, PacketBuffer buf) {
        buf.writeBoolean(pkt.remove);
        buf.writeInt(pkt.recipes.size());
        pkt.recipes.forEach(buf::writeResourceLocation);
    }

    public static void handle(S2CRecipe pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                if (pkt.remove) {
                    cap.getRecipeKeeper().lockRecipesRes(player, pkt.recipes);
                } else {
                    cap.getRecipeKeeper().unlockRecipesRes(player, pkt.recipes);
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandlers.recipeToast(pkt.recipes));
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
