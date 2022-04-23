package io.github.flemmli97.runecraftory.forge.capability;

import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.PlayerData;
import io.github.flemmli97.runecraftory.common.attachment.StaffData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityInsts {

    public static final Capability<PlayerData> PLAYERCAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<StaffData> ITEMSTACKCAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<EntityData> ENTITYCAP = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(PlayerData.class);
        event.register(StaffData.class);
        event.register(EntityData.class);
    }

}
