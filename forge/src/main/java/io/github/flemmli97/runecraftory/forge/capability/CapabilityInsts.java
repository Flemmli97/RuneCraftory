package io.github.flemmli97.runecraftory.forge.capability;

import io.github.flemmli97.runecraftory.common.attachment.ArmorEffectData;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.StaffData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityInsts {

    public static final Capability<PlayerData> PLAYER_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<StaffData> STAFF_ITEM_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<ArmorEffectData> ARMOR_ITEM_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<EntityData> ENTITY_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(PlayerData.class);
        event.register(StaffData.class);
        event.register(ArmorEffectData.class);
        event.register(EntityData.class);
    }

}
