package io.github.flemmli97.runecraftory.forge.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.GsonInstances;
import io.github.flemmli97.runecraftory.api.datapack.WeaponTypeProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.common.datapack.manager.WeaponPropertiesManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Locale;

public class WeaponPropertiesgen implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private final DataGenerator gen;
    private final EnumMap<EnumWeaponType, WeaponTypeProperties> weaponProps = new EnumMap<>(EnumWeaponType.class);

    public WeaponPropertiesgen(DataGenerator gen) {
        this.gen = gen;
    }

    @Override
    public void run(HashCache cache) {
        this.weaponProps.put(EnumWeaponType.FARM, new WeaponTypeProperties(0, 10));
        this.weaponProps.put(EnumWeaponType.SHORTSWORD, new WeaponTypeProperties(7, 13));
        this.weaponProps.put(EnumWeaponType.LONGSWORD, new WeaponTypeProperties(40, 17));
        this.weaponProps.put(EnumWeaponType.SPEAR, new WeaponTypeProperties(3, 13));
        this.weaponProps.put(EnumWeaponType.HAXE, new WeaponTypeProperties(23, 16));
        this.weaponProps.put(EnumWeaponType.DUAL, new WeaponTypeProperties(10, 10));
        this.weaponProps.put(EnumWeaponType.GLOVE, new WeaponTypeProperties(3, 10));
        this.weaponProps.put(EnumWeaponType.STAFF, new WeaponTypeProperties(0, 20));

        this.weaponProps.forEach((skill, builder) -> {
            Path path = this.gen.getOutputFolder().resolve("data/" + RuneCraftory.MODID + "/" + WeaponPropertiesManager.DIRECTORY + "/" + skill.name().toLowerCase(Locale.ROOT) + ".json");
            try {
                JsonElement obj = WeaponTypeProperties.CODEC.encodeStart(JsonOps.INSTANCE, builder)
                        .getOrThrow(false, LOGGER::error);
                DataProvider.save(GsonInstances.ATTRIBUTE_EFFECTS, cache, obj, path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save weapon properties {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "WeaponTypeProperties";
    }
}