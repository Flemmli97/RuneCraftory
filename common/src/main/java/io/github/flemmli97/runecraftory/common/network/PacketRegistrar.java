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
        register.registerMessage(id++, C2SUpdateCraftingScreen.ID, C2SUpdateCraftingScreen.class, C2SUpdateCraftingScreen::write, C2SUpdateCraftingScreen::read, C2SUpdateCraftingScreen::handle);
        register.registerMessage(id++, C2SSetMonsterBehaviour.ID, C2SSetMonsterBehaviour.class, C2SSetMonsterBehaviour::write, C2SSetMonsterBehaviour::read, C2SSetMonsterBehaviour::handle);
        register.registerMessage(id++, C2SNPCInteraction.ID, C2SNPCInteraction.class, C2SNPCInteraction::write, C2SNPCInteraction::read, C2SNPCInteraction::handle);
        register.registerMessage(id++, C2SShopButton.ID, C2SShopButton.class, C2SShopButton::write, C2SShopButton::read, C2SShopButton::handle);
        return id;
    }

    public static int registerClientPackets(ClientPacketRegister register, int id) {
        register.registerMessage(id++, S2CAttackDebug.ID, S2CAttackDebug.class, S2CAttackDebug::write, S2CAttackDebug::read, S2CAttackDebug::handle);
        register.registerMessage(id++, S2CCalendar.ID, S2CCalendar.class, S2CCalendar::write, S2CCalendar::read, S2CCalendar::handle);
        register.registerMessage(id++, S2CCapSync.ID, S2CCapSync.class, S2CCapSync::write, S2CCapSync::read, S2CCapSync::handle);
        register.registerMessage(id++, S2CDataPackSync.ID, S2CDataPackSync.class, S2CDataPackSync::write, S2CDataPackSync::read, S2CDataPackSync::handle);
        register.registerMessage(id++, S2CEntityDataSync.ID, S2CEntityDataSync.class, S2CEntityDataSync::write, S2CEntityDataSync::read, S2CEntityDataSync::handle);
        register.registerMessage(id++, S2CEntityDataSyncAll.ID, S2CEntityDataSyncAll.class, S2CEntityDataSyncAll::write, S2CEntityDataSyncAll::read, S2CEntityDataSyncAll::handle);
        register.registerMessage(id++, S2CEquipmentUpdate.ID, S2CEquipmentUpdate.class, S2CEquipmentUpdate::write, S2CEquipmentUpdate::read, S2CEquipmentUpdate::handle);
        register.registerMessage(id++, S2CFoodPkt.ID, S2CFoodPkt.class, S2CFoodPkt::write, S2CFoodPkt::read, S2CFoodPkt::handle);
        register.registerMessage(id++, S2CItemStatBoost.ID, S2CItemStatBoost.class, S2CItemStatBoost::write, S2CItemStatBoost::read, S2CItemStatBoost::handle);
        register.registerMessage(id++, S2CLevelPkt.ID, S2CLevelPkt.class, S2CLevelPkt::write, S2CLevelPkt::read, S2CLevelPkt::handle);
        register.registerMessage(id++, S2CMaxRunePoints.ID, S2CMaxRunePoints.class, S2CMaxRunePoints::write, S2CMaxRunePoints::read, S2CMaxRunePoints::handle);
        register.registerMessage(id++, S2CMoney.ID, S2CMoney.class, S2CMoney::write, S2CMoney::read, S2CMoney::handle);
        register.registerMessage(id++, S2CPlayerStats.ID, S2CPlayerStats.class, S2CPlayerStats::write, S2CPlayerStats::read, S2CPlayerStats::handle);
        register.registerMessage(id++, S2CRecipe.ID, S2CRecipe.class, S2CRecipe::write, S2CRecipe::read, S2CRecipe::handle);
        register.registerMessage(id++, S2CRunePoints.ID, S2CRunePoints.class, S2CRunePoints::write, S2CRunePoints::read, S2CRunePoints::handle);
        register.registerMessage(id++, S2CSkillLevelPkt.ID, S2CSkillLevelPkt.class, S2CSkillLevelPkt::write, S2CSkillLevelPkt::read, S2CSkillLevelPkt::handle);
        register.registerMessage(id++, S2CRuneyWeatherData.ID, S2CRuneyWeatherData.class, S2CRuneyWeatherData::write, S2CRuneyWeatherData::read, S2CRuneyWeatherData::handle);
        register.registerMessage(id++, S2COpenCompanionGui.ID, S2COpenCompanionGui.class, S2COpenCompanionGui::write, S2COpenCompanionGui::read, S2COpenCompanionGui::handle);
        register.registerMessage(id++, S2COpenNPCGui.ID, S2COpenNPCGui.class, S2COpenNPCGui::write, S2COpenNPCGui::read, S2COpenNPCGui::handle);
        register.registerMessage(id++, S2CUpdateNPCData.ID, S2CUpdateNPCData.class, S2CUpdateNPCData::write, S2CUpdateNPCData::read, S2CUpdateNPCData::handle);
        register.registerMessage(id++, S2CShopResponses.ID, S2CShopResponses.class, S2CShopResponses::write, S2CShopResponses::read, S2CShopResponses::handle);
        return id;
    }

    public interface ServerPacketRegister {
        <P> void registerMessage(int index, ResourceLocation id, Class<P> clss, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, BiConsumer<P, ServerPlayer> handler);
    }

    public interface ClientPacketRegister {
        <P> void registerMessage(int index, ResourceLocation id, Class<P> clss, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, Consumer<P> handler);
    }
}
