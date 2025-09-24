package io.github.flemmli97.runecraftory.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PacketRegistrar {

    public static void registerServerPackets(ServerPacketRegister register) {
        register.register(C2SOpenInfo.TYPE, C2SOpenInfo.STREAM_CODEC, C2SOpenInfo::handle);
        register.register(C2SRideJump.TYPE, C2SRideJump.STREAM_CODEC, C2SRideJump::handle);
        register.register(C2SSpellKey.TYPE, C2SSpellKey.STREAM_CODEC, C2SSpellKey::handle);
        register.register(C2SSetMonsterBehaviour.TYPE, C2SSetMonsterBehaviour.STREAM_CODEC, C2SSetMonsterBehaviour::handle);
        register.register(C2SNPCInteraction.TYPE, C2SNPCInteraction.STREAM_CODEC, C2SNPCInteraction::handle);
        register.register(C2SShopButton.TYPE, C2SShopButton.STREAM_CODEC, C2SShopButton::handle);
        register.register(C2SSelectRecipeCrafting.TYPE, C2SSelectRecipeCrafting.STREAM_CODEC, C2SSelectRecipeCrafting::handle);
        register.register(C2SDialogueAction.TYPE, C2SDialogueAction.STREAM_CODEC, C2SDialogueAction::handle);
        register.register(C2SQuestSelect.TYPE, C2SQuestSelect.STREAM_CODEC, C2SQuestSelect::handle);
        register.register(C2SSubmitQuestBoard.TYPE, C2SSubmitQuestBoard.STREAM_CODEC, C2SSubmitQuestBoard::handle);
        register.register(C2SSpawnEgg.TYPE, C2SSpawnEgg.STREAM_CODEC, C2SSpawnEgg::handle);
        register.register(C2SProcreationRequest.TYPE, C2SProcreationRequest.STREAM_CODEC, C2SProcreationRequest::handle);
    }

    public static void registerClientPackets(ClientPacketRegister register) {
        register.register(S2CAttackDebug.TYPE, S2CAttackDebug.STREAM_CODEC, (pkt, player) -> S2CAttackDebug.handle(pkt));
        register.register(S2CCalendar.TYPE, S2CCalendar.STREAM_CODEC, S2CCalendar::handle);
        register.register(S2CCapSync.TYPE, S2CCapSync.STREAM_CODEC, S2CCapSync::handle);
        register.register(S2CDataPackSync.TYPE, S2CDataPackSync.STREAM_CODEC, S2CDataPackSync::handle);
        register.register(S2CEntityDataSync.TYPE, S2CEntityDataSync.STREAM_CODEC, S2CEntityDataSync::handle);
        register.register(S2CFoodPkt.TYPE, S2CFoodPkt.STREAM_CODEC, S2CFoodPkt::handle);
        register.register(S2CLevelPkt.TYPE, S2CLevelPkt.STREAM_CODEC, S2CLevelPkt::handle);
        register.register(S2CMoney.TYPE, S2CMoney.STREAM_CODEC, S2CMoney::handle);
        register.register(S2CRecipe.TYPE, S2CRecipe.STREAM_CODEC, S2CRecipe::handle);
        register.register(S2CRunePoints.TYPE, S2CRunePoints.STREAM_CODEC, S2CRunePoints::handle);
        register.register(S2CSkillLevelPkt.TYPE, S2CSkillLevelPkt.STREAM_CODEC, S2CSkillLevelPkt::handle);
        register.register(S2COpenCompanionGui.TYPE, S2COpenCompanionGui.STREAM_CODEC, S2COpenCompanionGui::handle);
        register.register(S2COpenNPCGui.TYPE, S2COpenNPCGui.STREAM_CODEC, S2COpenNPCGui::handle);
        register.register(S2CUpdateNPCData.TYPE, S2CUpdateNPCData.STREAM_CODEC, S2CUpdateNPCData::handle);
        register.register(S2CShopResponses.TYPE, S2CShopResponses.STREAM_CODEC, S2CShopResponses::handle);
        register.register(S2CScreenShake.TYPE, S2CScreenShake.STREAM_CODEC, S2CScreenShake::handle);
        register.register(S2CWeaponUse.TYPE, S2CWeaponUse.STREAM_CODEC, S2CWeaponUse::handle);
        register.register(S2CCraftingRecipes.TYPE, S2CCraftingRecipes.STREAM_CODEC, S2CCraftingRecipes::handle);
        register.register(S2CNPCLook.TYPE, S2CNPCLook.STREAM_CODEC, S2CNPCLook::handle);
        register.register(S2CUpdateAttributesWithAdditional.TYPE, S2CUpdateAttributesWithAdditional.STREAM_CODEC, S2CUpdateAttributesWithAdditional::handle);
        register.register(S2CTriggers.TYPE, S2CTriggers.STREAM_CODEC, S2CTriggers::handle);
        register.register(S2CFarmlandUpdatePacket.TYPE, S2CFarmlandUpdatePacket.STREAM_CODEC, S2CFarmlandUpdatePacket::handle);
        register.register(S2CFarmlandRemovePacket.TYPE, S2CFarmlandRemovePacket.STREAM_CODEC, S2CFarmlandRemovePacket::handle);
        register.register(S2CNpcDialogue.TYPE, S2CNpcDialogue.STREAM_CODEC, S2CNpcDialogue::handle);
        register.register(S2COpenQuestGui.TYPE, S2COpenQuestGui.STREAM_CODEC, S2COpenQuestGui::handle);
        register.register(S2CSyncConfig.TYPE, S2CSyncConfig.STREAM_CODEC, S2CSyncConfig::handle);
        register.register(S2CSpawnEggScreen.TYPE, S2CSpawnEggScreen.STREAM_CODEC, S2CSpawnEggScreen::handle);
        register.register(S2CEntityLevelPkt.TYPE, S2CEntityLevelPkt.STREAM_CODEC, S2CEntityLevelPkt::handle);
        register.register(S2CBossbarInfoAdd.TYPE, S2CBossbarInfoAdd.STREAM_CODEC, S2CBossbarInfoAdd::handle);
        register.register(S2CBossbarInfoRemove.TYPE, S2CBossbarInfoRemove.STREAM_CODEC, S2CBossbarInfoRemove::handle);
        register.register(S2CBossbarMusicUpdate.TYPE, S2CBossbarMusicUpdate.STREAM_CODEC, S2CBossbarMusicUpdate::handle);
        register.register(S2CSimpleToast.TYPE, S2CSimpleToast.STREAM_CODEC, S2CSimpleToast::handle);
        register.register(S2CBoundEntityPacket.TYPE, S2CBoundEntityPacket.STREAM_CODEC, S2CBoundEntityPacket::handle);
    }

    public interface ServerPacketRegister {

        <P extends CustomPacketPayload> void register(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, BiConsumer<P, ServerPlayer> handler);
    }

    public interface ClientPacketRegister {

        default <P extends CustomPacketPayload> void register(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, Consumer<P> handler) {
            this.register(type, codec, (pkt, p) -> handler.accept(pkt));
        }

        <P extends CustomPacketPayload> void register(CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec, BiConsumer<P, Player> handler);
    }
}
