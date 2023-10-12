package io.github.flemmli97.runecraftory.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class PacketRegistrar {

    public static int registerServerPackets(ServerPacketRegister register, int id) {
        register.registerMessage(id++, C2SOpenInfo.ID, C2SOpenInfo.class, C2SOpenInfo::write, C2SOpenInfo::read, C2SOpenInfo::handle);
        register.registerMessage(id++, C2SRideJump.ID, C2SRideJump.class, C2SRideJump::write, C2SRideJump::read, C2SRideJump::handle);
        register.registerMessage(id++, C2SSpellKey.ID, C2SSpellKey.class, C2SSpellKey::write, C2SSpellKey::read, C2SSpellKey::handle);
        register.registerMessage(id++, C2SSetMonsterBehaviour.ID, C2SSetMonsterBehaviour.class, C2SSetMonsterBehaviour::write, C2SSetMonsterBehaviour::read, C2SSetMonsterBehaviour::handle);
        register.registerMessage(id++, C2SNPCInteraction.ID, C2SNPCInteraction.class, C2SNPCInteraction::write, C2SNPCInteraction::read, C2SNPCInteraction::handle);
        register.registerMessage(id++, C2SShopButton.ID, C2SShopButton.class, C2SShopButton::write, C2SShopButton::read, C2SShopButton::handle);
        register.registerMessage(id++, C2SSelectRecipeCrafting.ID, C2SSelectRecipeCrafting.class, C2SSelectRecipeCrafting::write, C2SSelectRecipeCrafting::read, C2SSelectRecipeCrafting::handle);
        register.registerMessage(id++, C2SDialogueAction.ID, C2SDialogueAction.class, C2SDialogueAction::write, C2SDialogueAction::read, C2SDialogueAction::handle);
        register.registerMessage(id++, C2SQuestSelect.ID, C2SQuestSelect.class, C2SQuestSelect::write, C2SQuestSelect::read, C2SQuestSelect::handle);
        return id;
    }

    public static int registerClientPackets(ClientPacketRegister register, int id) {
        register.registerMessage(id++, S2CAttackDebug.ID, S2CAttackDebug.class, S2CAttackDebug::write, S2CAttackDebug::read, S2CAttackDebug::handle);
        register.registerMessage(id++, S2CCalendar.ID, S2CCalendar.class, S2CCalendar::write, S2CCalendar::read, S2CCalendar::handle);
        register.registerMessage(id++, S2CCapSync.ID, S2CCapSync.class, S2CCapSync::write, S2CCapSync::read, S2CCapSync::handle);
        register.registerMessage(id++, S2CDataPackSync.ID, S2CDataPackSync.class, S2CDataPackSync::write, S2CDataPackSync::read, S2CDataPackSync::handle);
        register.registerMessage(id++, S2CEntityDataSync.ID, S2CEntityDataSync.class, S2CEntityDataSync::write, S2CEntityDataSync::read, S2CEntityDataSync::handle);
        register.registerMessage(id++, S2CEntityDataSyncAll.ID, S2CEntityDataSyncAll.class, S2CEntityDataSyncAll::write, S2CEntityDataSyncAll::read, S2CEntityDataSyncAll::handle);
        register.registerMessage(id++, S2CFoodPkt.ID, S2CFoodPkt.class, S2CFoodPkt::write, S2CFoodPkt::read, S2CFoodPkt::handle);
        register.registerMessage(id++, S2CItemStatBoost.ID, S2CItemStatBoost.class, S2CItemStatBoost::write, S2CItemStatBoost::read, S2CItemStatBoost::handle);
        register.registerMessage(id++, S2CLevelPkt.ID, S2CLevelPkt.class, S2CLevelPkt::write, S2CLevelPkt::read, S2CLevelPkt::handle);
        register.registerMessage(id++, S2CMaxRunePoints.ID, S2CMaxRunePoints.class, S2CMaxRunePoints::write, S2CMaxRunePoints::read, S2CMaxRunePoints::handle);
        register.registerMessage(id++, S2CMoney.ID, S2CMoney.class, S2CMoney::write, S2CMoney::read, S2CMoney::handle);
        register.registerMessage(id++, S2CPlayerStats.ID, S2CPlayerStats.class, S2CPlayerStats::write, S2CPlayerStats::read, S2CPlayerStats::handle);
        register.registerMessage(id++, S2CRecipe.ID, S2CRecipe.class, S2CRecipe::write, S2CRecipe::read, S2CRecipe::handle);
        register.registerMessage(id++, S2CRunePoints.ID, S2CRunePoints.class, S2CRunePoints::write, S2CRunePoints::read, S2CRunePoints::handle);
        register.registerMessage(id++, S2CSkillLevelPkt.ID, S2CSkillLevelPkt.class, S2CSkillLevelPkt::write, S2CSkillLevelPkt::read, S2CSkillLevelPkt::handle);
        register.registerMessage(id++, S2COpenCompanionGui.ID, S2COpenCompanionGui.class, S2COpenCompanionGui::write, S2COpenCompanionGui::read, S2COpenCompanionGui::handle);
        register.registerMessage(id++, S2COpenNPCGui.ID, S2COpenNPCGui.class, S2COpenNPCGui::write, S2COpenNPCGui::read, S2COpenNPCGui::handle);
        register.registerMessage(id++, S2CUpdateNPCData.ID, S2CUpdateNPCData.class, S2CUpdateNPCData::write, S2CUpdateNPCData::read, S2CUpdateNPCData::handle);
        register.registerMessage(id++, S2CShopResponses.ID, S2CShopResponses.class, S2CShopResponses::write, S2CShopResponses::read, S2CShopResponses::handle);
        register.registerMessage(id++, S2CScreenShake.ID, S2CScreenShake.class, S2CScreenShake::write, S2CScreenShake::read, S2CScreenShake::handle);
        register.registerMessage(id++, S2CWeaponUse.ID, S2CWeaponUse.class, S2CWeaponUse::write, S2CWeaponUse::read, S2CWeaponUse::handle);
        register.registerMessage(id++, S2CCraftingRecipes.ID, S2CCraftingRecipes.class, S2CCraftingRecipes::write, S2CCraftingRecipes::read, S2CCraftingRecipes::handle);
        register.registerMessage(id++, S2CNPCLook.ID, S2CNPCLook.class, S2CNPCLook::write, S2CNPCLook::read, S2CNPCLook::handle);
        register.registerMessage(id++, S2CUpdateAttributesWithAdditional.ID, S2CUpdateAttributesWithAdditional.class, S2CUpdateAttributesWithAdditional::write, S2CUpdateAttributesWithAdditional::read, S2CUpdateAttributesWithAdditional::handle);
        register.registerMessage(id++, S2CTriggers.ID, S2CTriggers.class, S2CTriggers::write, S2CTriggers::read, S2CTriggers::handle);
        register.registerMessage(id++, S2CFarmlandUpdatePacket.ID, S2CFarmlandUpdatePacket.class, S2CFarmlandUpdatePacket::write, S2CFarmlandUpdatePacket::read, S2CFarmlandUpdatePacket::handle);
        register.registerMessage(id++, S2CFarmlandRemovePacket.ID, S2CFarmlandRemovePacket.class, S2CFarmlandRemovePacket::write, S2CFarmlandRemovePacket::read, S2CFarmlandRemovePacket::handle);
        register.registerMessage(id++, S2CNpcDialogue.ID, S2CNpcDialogue.class, S2CNpcDialogue::write, S2CNpcDialogue::read, S2CNpcDialogue::handle);
        register.registerMessage(id++, S2COpenQuestGui.ID, S2COpenQuestGui.class, S2COpenQuestGui::write, S2COpenQuestGui::read, S2COpenQuestGui::handle);
        return id;
    }

    public interface ServerPacketRegister {
        <P> void registerMessage(int index, ResourceLocation id, Class<P> clss, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, BiConsumer<P, ServerPlayer> handler);
    }

    public interface ClientPacketRegister {
        <P> void registerMessage(int index, ResourceLocation id, Class<P> clss, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, Consumer<P> handler);
    }
}
