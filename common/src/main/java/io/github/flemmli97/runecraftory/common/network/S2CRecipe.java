package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public record S2CRecipe(Collection<ResourceLocation> recipes, boolean remove) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CRecipe> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_recipe"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CRecipe> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CRecipe decode(RegistryFriendlyByteBuf buf) {
            boolean remove = buf.readBoolean();
            Set<ResourceLocation> recs = new HashSet<>();
            int size = buf.readInt();
            for (int i = 0; i < size; i++)
                recs.add(buf.readResourceLocation());
            return new S2CRecipe(recs, remove);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CRecipe pkt) {
            buf.writeBoolean(pkt.remove);
            buf.writeInt(pkt.recipes.size());
            pkt.recipes.forEach(buf::writeResourceLocation);
        }
    };

    public static void handle(S2CRecipe pkt, Player player) {
        if (player == null)
            return;
        PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
        if (pkt.remove) {
            data.getRecipeKeeper().lockRecipesRes(player, pkt.recipes);
        } else {
            data.getRecipeKeeper().unlockRecipesRes(player, pkt.recipes);
            ClientHandlers.recipeToast(pkt.recipes);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
