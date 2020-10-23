package com.flemmli97.runecraftory;

import com.flemmli97.tenshilib.common.config.JsonConfig;
import com.google.gson.JsonObject;

import java.io.File;

public class ModuleConf {

    private final JsonConfig<JsonObject> conf;

    public boolean mobModule;
    public boolean combatModule;

    public ModuleConf(File file) {
        JsonObject def = new JsonObject();
        def.addProperty("__comment", "All Values here need a mc/server restart to take in effect");
        def.addProperty("MobModule", true);
        def.addProperty("CombatModule", true);
        this.conf = new JsonConfig<>(file, JsonObject.class, def);
        this.conf.save();
        JsonObject obj = this.conf.getElement();
        this.mobModule = obj.get("MobModule").getAsBoolean();
        this.combatModule = obj.get("CombatModule").getAsBoolean();
    }

}
