package io.github.flemmli97.runecraftory.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.fabric.config.ClientConfigSpec;
import io.github.flemmli97.runecraftory.fabric.config.GeneralConfigSpec;
import io.github.flemmli97.runecraftory.fabric.config.MobConfigSpec;
import io.github.flemmli97.tenshilib.common.config.ClothConfigScreenHelper;

import java.util.List;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ClothConfigScreenHelper.configScreenOf(parent, RuneCraftory.MODID, List.of(ClientConfigSpec.SPEC.getKey(), GeneralConfigSpec.SPEC.getKey(), MobConfigSpec.SPEC.getKey()));
    }
}
